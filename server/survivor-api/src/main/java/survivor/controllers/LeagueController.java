package survivor.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import survivor.domain.LeagueService;
import survivor.domain.Result;
import survivor.models.AppUser;
import survivor.models.League;

import java.util.List;

@RestController
@RequestMapping("/api/leagues")
public class LeagueController {

    private final LeagueService service;

    public LeagueController(LeagueService service){
        this.service = service;
    }

    @GetMapping("/all")
    public List<League> findAllLeagues() {
        return service.findAllLeagues();
    }

    @GetMapping("/league{id}")
    public ResponseEntity<?> findLeagueById(@PathVariable int id){
        Result<?> result = service.findLeagueById(id);
        if (result.isSuccess()){
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }

    @GetMapping("/user")
    public ResponseEntity<?> findLeaguesByUser(UsernamePasswordAuthenticationToken principal){
        AppUser appUser = (AppUser) principal.getPrincipal();
        Result<?> result = service.findLeaguesByAppUserId(appUser.getAppUserId());
        if (result.isSuccess()){
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }
}
