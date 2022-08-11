package survivor.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import survivor.data.CastawayJdbcTemplateRepository;
import survivor.data.RatingJdbcTemplateRepository;
import survivor.data.TribalPointsJdbcTemplateRepository;
import survivor.models.*;

import java.util.List;

@Service
public class TribalPointsService {
    private final TribalPointsJdbcTemplateRepository repository;
    private final CastawayJdbcTemplateRepository castawayRepository;
    private final RatingJdbcTemplateRepository ratingRepository;

    public TribalPointsService(TribalPointsJdbcTemplateRepository repository,
                               CastawayJdbcTemplateRepository castawayRepository,
                               RatingJdbcTemplateRepository ratingRepository){
        this.repository = repository;
        this.castawayRepository = castawayRepository;
        this.ratingRepository = ratingRepository;
    }

    public Result<?> findTribalPointsByLeagueAppUser(int leagueId, int userId, AppUser user){
        Result<List<TribalPoints>> result = (Result<List<TribalPoints>>) validateUser(user,leagueId);
        validateUser(leagueId,userId,result);
        if (!result.isSuccess()){
            return result;
        }
        List<TribalPoints> points = repository.findTribalPointsByLeagueAppUser(leagueId,userId);
        if (points.size() == 0){
            result.addMessage("Cannot find points for user id = " + userId, ResultType.INVALID);
            return result;
        }
        result.setPayload(points);
        return result;
    }

    public Result<?> findTribalPointsByUserAndTribal(int leagueId, int userId, int tribalNumber, AppUser user){
        Result<TribalPoints> result = (Result<TribalPoints>) validateUser(user,leagueId);
        validateUser(leagueId,userId,result);
        validateTribal(leagueId,tribalNumber,result);
        if (!result.isSuccess()){
            return result;
        }
        TribalPoints points = repository.findTribalPointsByUserAndTribal(leagueId, userId, tribalNumber);
        if (points == null){
            result.addMessage("Cannot find points for user for tribal "+ tribalNumber, ResultType.INVALID);
        }
        result.setPayload(points);
        return result;
    }

    public Result<?> createTribalPoints(int leagueId, int userId, int tribalNumber, AppUser owner){
        Result<?> result = validateOwner(owner, leagueId);
        validateUser(leagueId,userId,result);
        validateTribal(leagueId,tribalNumber,result);
        validateRating(leagueId, userId, result);
        if (!result.isSuccess()){
            return result;
        }

        TribalPoints points = makeTribalPoints(leagueId,userId,tribalNumber);

        if (!repository.createTribalPoints(points, tribalNumber)){
            result.addMessage("Could not create tribal points", ResultType.INVALID);
        }
        return result;
    }

    @Transactional
    public Result<?> postNewTribalPointsForLeague(League league, int tribalNumber, AppUser owner){
        Result<?> result = validateOwner(owner, league.getLeagueId());
        validateTribal(league.getLeagueId(), tribalNumber, result);
        if (!result.isSuccess()){
            return result;
        }

        for (AppUser member : league.getAppUsers()){
            validateRating(league.getLeagueId(), member.getAppUserId(), result);
            if (!result.isSuccess()){
                return result;
            }
            TribalPoints points = makeTribalPoints(league.getLeagueId(), member.getAppUserId(), tribalNumber);
            if (!repository.createTribalPoints(points,tribalNumber)){
                result.addMessage("Could not create points for user id = "
                        + member.getAppUserId(), ResultType.INVALID);
                return result;
            }
        }
        return result;
    }

    public Result<?> updateTribalPointsById(TribalPoints points, AppUser owner){
        Result<?> result = validateOwner(owner, points.getLeague().getLeagueId());
        if (!result.isSuccess()){
            return result;
        }

        if (points == null || repository.findTribalPointsById(points.getId()) == null){
            result.addMessage("Points not found", ResultType.NOT_FOUND);
            return result;
        }

        if (!repository.updateTribalPoints(points)){
            result.addMessage("Points could not be updated", ResultType.INVALID);
        }
        return result;
    }

    public Result<?> deleteTribalPointsById(int lautId, int leagueId, AppUser owner){
        Result<?> result = validateOwner(owner, leagueId);
        if (!result.isSuccess()){
            return result;
        }
        if (!repository.deleteTribalPointsById(lautId)){
            result.addMessage("Points not found", ResultType.NOT_FOUND);
        }
        return result;
    }

    public Result<?> deleteTribalPointsByUser(int userId, int leagueId, AppUser owner){
        Result<?> result = validateOwner(owner, leagueId);
        if (!result.isSuccess()){
            return result;
        }
        if (!repository.deleteTribalPointsByUser(leagueId, userId)){
            result.addMessage("Points not found", ResultType.NOT_FOUND);
        }
        return result;
    }

    public Result<?> deleteTribalPointsByTribal(int leagueId, int tribalNumber, AppUser owner){
        Result<?> result = validateOwner(owner, leagueId);
        validateTribal(leagueId,tribalNumber,result);
        if (!result.isSuccess()){
            return result;
        }
        League league = repository.findLeagueById(leagueId);
        if (!repository.deleteTribalPointsByTribal(league, tribalNumber)){
            result.addMessage("Points not found", ResultType.NOT_FOUND);
        }
        return result;
    }

    //////////////////////////// VALIDATION METHODS /////////////////////////////////
    private Result<?> validateUser(AppUser user, int leagueId){
        Result<?> result = new Result<>();

        if (!AppUser.convertAuthoritiesToRoles(user.getAuthorities()).contains("ADMIN")){
            if (repository.getLauId(leagueId,user.getAppUserId()) == 0){
                result.addMessage("Must be league member to view points", ResultType.INVALID);
            }
        }
        return result;
    }

    private void validateUser(int leagueId, int userId, Result<?> result){
        if (repository.getLauId(leagueId,userId) == 0){
            result.addMessage("User not in league", ResultType.INVALID);
        }
    }

    private void validateTribal(int leagueId, int tribalNumber, Result<?> result){
        League league = repository.findLeagueById(leagueId);
        if (!repository.tribalInSeason(league,tribalNumber)){
            result.addMessage("Tribal number not found in season", ResultType.INVALID);
        }
    }

    private Result<?> validateOwner(AppUser user, int leagueId){
        Result<?> result = new Result<>();
        League league = repository.findLeagueById(leagueId);
        if (league.getOwnerId() != user.getAppUserId()){
            result.addMessage("Must own league to edit", ResultType.INVALID);
        }
        return result;
    }

    private void validateRating(int leagueId, int userId, Result<?> result){
        Rating rating = ratingRepository.findRatingByIds(leagueId,userId);
        if (rating == null){
            result.addMessage("User rankings not found", ResultType.INVALID);
        }
    }

    //////////////////////// MAKE TRIBAL POINTS ///////////////////////////
    private TribalPoints makeTribalPoints(int leagueId, int userId, int tribalNumber){
        League league = repository.findLeagueById(leagueId);
        List<Castaway> tribal = castawayRepository.findCastawayByTribal(league.getSeasonId(),tribalNumber);
        int numberRemaining = tribal.size();

        List<Castaway> remainingCastaways = ratingRepository.findRatingByIds(leagueId,userId)
                .getCastaways().subList(0, numberRemaining);
        remainingCastaways.retainAll(tribal);

        double previousPoints = 0;

        if (tribalNumber > 1) {
            previousPoints = repository
                    .findTribalPointsByUserAndTribal(leagueId,userId,tribalNumber-1)
                    .getPointsToDate();
        }

        double weekPoints = (double) remainingCastaways.size() / tribal.size() * 100;
        double pointsToDate = previousPoints + weekPoints;

        TribalPoints newPoints = new TribalPoints();
        newPoints.setUserId(userId);
        newPoints.setWeekPoints(weekPoints);
        newPoints.setPointsToDate(pointsToDate);
        newPoints.setLeague(league);
        newPoints.setTribal(tribal);

        return newPoints;
    }
}
