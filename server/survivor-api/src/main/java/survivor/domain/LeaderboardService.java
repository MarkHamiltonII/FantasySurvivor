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
