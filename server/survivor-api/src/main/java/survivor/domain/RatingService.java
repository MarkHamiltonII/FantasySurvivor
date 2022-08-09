package survivor.domain;

import org.springframework.stereotype.Service;
import survivor.data.RatingJdbcTemplateRepository;
import survivor.models.Castaway;
import survivor.models.Rating;

import java.util.List;

@Service
public class RatingService {
    private final RatingJdbcTemplateRepository repository;

    public RatingService(RatingJdbcTemplateRepository repository){
        this.repository = repository;

    }

    public Result<?> findRatingsByIds(int leagueId, int userId, int ownerId){
        Result<Rating> result = new Result<>();

        if (repository.getRatingId(leagueId, ownerId) == 0){
            result.addMessage("User must be league member to view league rankings", ResultType.INVALID);
            return result;
        }

        Rating rating = repository.findRatingByIds(leagueId,userId);
        if (rating == null){
            result.addMessage("Cannot find ratings for user id = " + userId, ResultType.NOT_FOUND);
            return result;
        }
        result.setPayload(rating);
        return result;
    }

    public Result<?> createRating(Rating rating, int ownerId){
        Result<?> result = validateRating(rating, ownerId);

        if(!result.isSuccess()){
            return result;
        }

        Rating oldRating = repository.findRatingByIds(rating.getLeagueId(), rating.getUserId());
        if (oldRating != null){
            result.addMessage("User ratings for this league already exist", ResultType.INVALID);
            return result;
        }

        if (!repository.createRating(rating)){
            result.addMessage("Rating could not be created", ResultType.INVALID);
        }

        return result;

    }

    public Result<?> updateRating(Rating rating, int ownerId){
        Result<?> result = validateRating(rating, ownerId);
        if (!result.isSuccess()){
            return result;
        }

        Rating oldRating = repository.findRatingByIds(rating.getLeagueId(), rating.getUserId());
        if (oldRating == null){
            result.addMessage("Rating not found", ResultType.NOT_FOUND);
            return result;
        }

        if (!repository.updateRating(rating)){
            result.addMessage("Could not update rating", ResultType.INVALID);
        }

        return result;
    }

    public Result<?> deleteRating(int leagueId, int userId, int ownerId){
        Result<?> result = new Result<>();
        if (userId != ownerId){
            result.addMessage("Must own team to delete", ResultType.INVALID);
            return result;
        }
        if (!repository.deleteRatingByIds(leagueId, userId)){
            result.addMessage("Rating not found", ResultType.NOT_FOUND);
        }
        return result;
    }

    //////////////////////////// VALIDATION METHODS /////////////////////////

    private Result<?> validateRating(Rating rating, int ownerId){
        Result<?> result = new Result<>();

        if (rating == null){
            result.addMessage("Rating cannot be null", ResultType.INVALID);
            return result;
        }

        if (repository.getRatingId(rating.getLeagueId(), rating.getUserId()) == 0){
            result.addMessage("User must be league member", ResultType.INVALID);
        }

        if (rating.getUserId() != ownerId){
            result.addMessage("Must own team to make changes", ResultType.INVALID);
            return result;
        }

        if (rating.getCastaways() == null || rating.getCastaways().size() == 0){
            result.addMessage("Ratings must contain castaways", ResultType.INVALID);
            return result;
        }

        for (Castaway c : rating.getCastaways()){
            if (c.getId() < 1){
                result.addMessage("Castaway must contain castaway_id", ResultType.INVALID);
            }
        }

        if (repository.isFinalized(rating.getLeagueId(), rating.getUserId())){
            result.addMessage("League is finalized, cannot change", ResultType.INVALID);
        }

        return result;
    }
}
