package survivor.domain;

import org.springframework.stereotype.Service;
import survivor.data.LeaderboardJdbcRepository;
import survivor.models.Leaderboard;

@Service
public class LeaderboardService {

    private final LeaderboardJdbcRepository repository;

    public LeaderboardService(LeaderboardJdbcRepository repository){
        this.repository = repository;
    }

    public Result<Leaderboard> getCurrentTribal(int seasonId){
        Result<Leaderboard> result = new Result<>();
        int currentTribal = repository.getCurrentTribal(seasonId);
        if (currentTribal == 0) {
            result.addMessage("Could not find current tribal", ResultType.NOT_FOUND);
            return result;
        }
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.setCurrentTribal(currentTribal);
        result.setPayload(leaderboard);
        return result;
    }

    public Result<Leaderboard> getLeaderboardBySeasonAndLeague(int seasonId, int leagueId){
        Result<Leaderboard> result = new Result<>();
        Leaderboard leaderboard = repository.getLeaderboardBySeasonAndLeague(seasonId,leagueId);
        if (leaderboard == null){
            result.addMessage("Could not find leaderboard", ResultType.NOT_FOUND);
            return result;
        }
        result.setPayload(leaderboard);
        return result;
    }
}
