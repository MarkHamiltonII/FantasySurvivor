package survivor.controllers;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import survivor.domain.Result;
import survivor.domain.TribalPointsService;
import survivor.models.AppUser;
import survivor.models.League;
import survivor.models.TribalPoints;

import java.util.List;

@RestController
@RequestMapping("/api/points")
public class TribalPointsController {

    private final TribalPointsService service;

    public TribalPointsController(TribalPointsService service) {
        this.service = service;
    }

    @GetMapping("/league{leagueId}/user{userId}")
    public ResponseEntity<?> findTribalPointsByLeagueAppUser(@PathVariable int leagueId, @PathVariable int userId, UsernamePasswordAuthenticationToken principal) {
        AppUser appUser = (AppUser) principal.getPrincipal();
        Result<?> result = service.findTribalPointsByLeagueAppUser(leagueId, userId, appUser);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }


    @GetMapping("/league{leagueId}/user{userId}/tribal{tribalNumber}")
    public ResponseEntity<?> findTribalPointsByUserAndTribal(
            @PathVariable int leagueId, @PathVariable int userId,
            @PathVariable int tribalNumber, UsernamePasswordAuthenticationToken principal) {
        AppUser appUser = (AppUser) principal.getPrincipal();
        Result<?> result = service.findTribalPointsByUserAndTribal(leagueId, userId, tribalNumber, appUser);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }

    @PostMapping("/league{leagueId}/user{userId}/tribal{tribalNumber}")
    public ResponseEntity<?> createTribalPoints(
            @PathVariable int leagueId, @PathVariable int userId,
            @PathVariable int tribalNumber, UsernamePasswordAuthenticationToken principal){
        try {
            AppUser appUser = (AppUser) principal.getPrincipal();
            Result<?> result = service.createTribalPoints(leagueId, userId, tribalNumber, appUser);
            if (result.isSuccess()) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            return ErrorResponse.build(result);
        } catch (DuplicateKeyException ex){
            return new ResponseEntity<>(List.of("Duplicate tribal points"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/league/all/tribal{tribalNumber}")
    public ResponseEntity<?> postNewTribalPointsForLeague(
            @RequestBody League league, @PathVariable int tribalNumber,
            UsernamePasswordAuthenticationToken principal) {
        try {
            AppUser appUser = (AppUser) principal.getPrincipal();
            Result<?> result = service.postNewTribalPointsForLeague(league, tribalNumber, appUser);
            if (result.isSuccess()) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            return ErrorResponse.build(result);
        } catch (DuplicateKeyException ex){
            return new ResponseEntity<>(List.of("Duplicate tribal points"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/league/all")
    public ResponseEntity<?> updateAllPointsForLeague(
            @RequestBody League league, UsernamePasswordAuthenticationToken principal) {
        try {
            AppUser appUser = (AppUser) principal.getPrincipal();
            Result<?> result = service.updateAllPointsForLeague(league, appUser);
            if (result.isSuccess()) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            return ErrorResponse.build(result);
        } catch (DuplicateKeyException ex){
            return new ResponseEntity<>(List.of("Duplicate tribal points"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update_individual_points")
    public ResponseEntity<?> updateTribalPointsById(@RequestBody TribalPoints points, UsernamePasswordAuthenticationToken principal){
        try {
            AppUser appUser = (AppUser) principal.getPrincipal();
            Result<?> result = service.updateTribalPointsById(points, appUser);
            return ErrorResponse.noContentOrError(result);
        } catch (DuplicateKeyException ex){
            return new ResponseEntity<>(List.of("Duplicate tribal points"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/league{leagueId}/point{pointId}")
    public ResponseEntity<?> deleteTribalPointsById(
            @PathVariable int leagueId, @PathVariable int pointId,
            UsernamePasswordAuthenticationToken principal) {
        AppUser appUser = (AppUser) principal.getPrincipal();
        Result<?> result = service.deleteTribalPointsById(pointId,leagueId,appUser);
        return ErrorResponse.noContentOrError(result);
    }

    @DeleteMapping("/league{leagueId}/user{userId}")
    public ResponseEntity<?> deleteTribalPointsByUser(
            @PathVariable int leagueId, @PathVariable int userId,
            UsernamePasswordAuthenticationToken principal) {
        AppUser appUser = (AppUser) principal.getPrincipal();
        Result<?> result = service.deleteTribalPointsByUser(userId,leagueId,appUser);
        return ErrorResponse.noContentOrError(result);
    }

    @DeleteMapping("/league{leagueId}/tribal{tribalNumber}")
    public ResponseEntity<?> deleteTribalPointsByTribal(
            @PathVariable int leagueId, @PathVariable int tribalNumber,
            UsernamePasswordAuthenticationToken principal) {
        AppUser appUser = (AppUser) principal.getPrincipal();
        Result<?> result = service.deleteTribalPointsByTribal(leagueId,tribalNumber,appUser);
        return ErrorResponse.noContentOrError(result);
    }
}
