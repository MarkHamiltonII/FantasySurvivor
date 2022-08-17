package survivor.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import survivor.domain.LeaderboardService;
import survivor.domain.Result;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final LeaderboardService service;

    public LeaderboardController(LeaderboardService service){
        this.service = service;
    }

    @GetMapping("/season{seasonId}/league{leagueId}")
    public ResponseEntity<?> getLeaderboardBySeasonAndLeague(@PathVariable int seasonId, @PathVariable int leagueId){
        Result<?> result = service.getLeaderboardBySeasonAndLeague(seasonId,leagueId);
        if (result.isSuccess()){
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }
}
