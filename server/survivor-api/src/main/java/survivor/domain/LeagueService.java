package survivor.domain;

import org.springframework.stereotype.Service;
import survivor.data.CastawayJdbcTemplateRepository;
import survivor.data.LeagueJdbcTemplateRepository;
import survivor.models.League;

import java.util.List;

@Service
public class LeagueService {

    private final LeagueJdbcTemplateRepository repository;
    private final CastawayJdbcTemplateRepository castawayRepository;

    public LeagueService(LeagueJdbcTemplateRepository repository, CastawayJdbcTemplateRepository castawayRepository){
        this.repository = repository;
        this.castawayRepository = castawayRepository;
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
            return result;
        }
        result.setPayload(leagues);
        return result;
    }

    public Result<?> createLeague(League league, int ownerId){
        Result<?> result = validateLeague(league);

        if (!repository.createLeague(league, ownerId)){
            result.addMessage("League could not be created", ResultType.INVALID);
        }

        return result;
    }

    public Result<?> updateLeague(League league, int ownerId){
        Result<?> result = validateLeague(league);
        if (!result.isSuccess()){
            return result;
        }

        League oldLeague = repository.findLeagueById(league.getLeagueId());
        validateOldLeague(oldLeague, ownerId, result);
        if (!result.isSuccess()){
            return result;
        }

        if (oldLeague.getSeasonId() != league.getSeasonId()){
            result.addMessage("Cannot change league's season", ResultType.INVALID);
            return result;
        }

        if (!repository.updateLeague(league)){
            result.addMessage("League could not be updated", ResultType.INVALID);
        }

        return result;
    }

    public Result<?> deleteLeagueById(int leagueId, int ownerId){
        Result<?> result = new Result<>();
        League oldLeague = repository.findLeagueById(leagueId);
        validateOldLeague(oldLeague, ownerId, result);
        if (!result.isSuccess()){
            return result;
        }

        if (!repository.leagueIsEmpty(leagueId)){
            result.addMessage("Cannot delete league unless empty", ResultType.INVALID);
            return result;
        }

        if (!repository.deleteLeagueById(leagueId)){
            result.addMessage("League not found", ResultType.NOT_FOUND);
        }
        return result;
    }

    public Result<?> addAppUserToLeague(int leagueId, int appUserId, int ownerId){
        Result<?> result = new Result<>();
        League oldLeague = repository.findLeagueById(leagueId);
        validateOldLeague(oldLeague, ownerId, result);

        if (!result.isSuccess()){
            return result;
        }

        repository.addAppUserToLeague(leagueId,appUserId);
        return result;
    }

    public Result<?> removeAppUserFromLeague(int leagueId, int appUserId, int ownerId){
        Result<?> result = new Result<>();
        League oldLeague = repository.findLeagueById(leagueId);
        validateOldLeague(oldLeague, ownerId, result);

        if (!result.isSuccess()){
            return result;
        }

        repository.removeAppUserFromLeague(leagueId,appUserId);
        return result;
    }

    /////////////////////////////// Validation Methods //////////////////////////////////

    private Result<?> validateLeague(League league){
        Result<?> result = new Result();

        if (league == null){
            result.addMessage("League cannot be blank", ResultType.INVALID);
            return result;
        }

        if (league.getName() == null || league.getName().isBlank()){
            result.addMessage("League name cannot be blank", ResultType.INVALID);
        }

        if (castawayRepository.findCastawayBySeason(league.getSeasonId()).size() == 0){
            result.addMessage("League must be for valid season", ResultType.INVALID);
        }

        return result;
    }

    private Result<?> validateOldLeague(League oldLeague, int ownerId, Result<?> result){
        if (oldLeague == null) {
            result.addMessage("Cannot update null league", ResultType.INVALID);
            return result;
        }

        if (oldLeague.getOwnerId() != ownerId){
            result.addMessage("You must own the league to update it", ResultType.INVALID);
        }
        return result;
    }
}
