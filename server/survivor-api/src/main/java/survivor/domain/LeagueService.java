package survivor.domain;

import org.springframework.stereotype.Service;
import survivor.data.LeagueJdbcTemplateRepository;
import survivor.models.League;

import java.util.List;

@Service
public class LeagueService {

    private final LeagueJdbcTemplateRepository repository;

    public LeagueService(LeagueJdbcTemplateRepository repository){
        this.repository = repository;
    }

    public List<League> findAllLeagues(){
        return repository.findAllLeagues();
    }

    public Result<?> findLeagueById(int leagueId){
        Result<League> result = new Result<>();
        League league =  repository.findLeagueById(leagueId);
        if (league == null){
            result.addMessage("Can not find league with ID: " + leagueId, ResultType.NOT_FOUND);
            return result;
        }
        result.setPayload(league);
        return result;
    }

    public Result<?> findLeaguesByAppUserId(int appUserId){
        Result<List<League>> result = new Result<>();
        List<League> leagues = repository.findLeaguesByAppUserId(appUserId);
        if (leagues.size() == 0){
            result.addMessage("Leagues not found", ResultType.INVALID);
            return  result;
        }
        result.setPayload(leagues);
        return result;
    }
}
