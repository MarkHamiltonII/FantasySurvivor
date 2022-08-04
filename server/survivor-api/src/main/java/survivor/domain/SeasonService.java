package survivor.domain;

import org.springframework.stereotype.Service;
import survivor.data.SeasonJdbcTemplateRepository;
import survivor.models.Season;

import java.util.List;

@Service
public class SeasonService {
    private final SeasonJdbcTemplateRepository repository;
    public SeasonService(SeasonJdbcTemplateRepository repository){
        this.repository = repository;
    }

    public List<Season> findAllSeasons(){
        return repository.findAllSeasons();
    }

    public Result<Season> findSeasonById(int id){
        Result<Season> result = new Result<>();
        Season season = repository.findSeasonById(id);
        if (season == null) {
            result.addMessage("Cannot find season "+id, ResultType.NOT_FOUND);
            return result;
        }
        result.setPayload(season);
        return result;
    }

    public Result<?> createSeason(Season season){
        Result<?> result = new Result<>();
        if (!repository.createSeason(season)){
            result.addMessage("Could not create season", ResultType.INVALID);
        }
        return result;
    }

    public Result<?> updateSeason(Season season){
        Result<?> result = new Result<>();
        Season oldSeason = repository.findSeasonById(season.getSeasonId());
        if (oldSeason == null){
            result.addMessage("Season not found", ResultType.NOT_FOUND);
            return result;
        }
        if (!repository.updateSeason(season)){
            result.addMessage("Season could not be updated", ResultType.INVALID);
        }
        return result;
    }

    public Result<?> deleteSeasonById(int seasonId){
        Result<?> result = new Result<>();
        if (repository.seasonInUse(seasonId)){
            result.addMessage("Cannot delete season in use", ResultType.INVALID);
            return result;
        }
        if (!repository.deleteSeasonById(seasonId)){
            result.addMessage("Season not found", ResultType.NOT_FOUND);
        }
        return result;
    }
}
