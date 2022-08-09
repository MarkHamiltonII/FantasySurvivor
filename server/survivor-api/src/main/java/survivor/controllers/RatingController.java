package survivor.controllers;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import survivor.domain.RatingService;
import survivor.domain.Result;
import survivor.models.AppUser;
import survivor.models.Rating;

import java.util.List;

@RestController
@RequestMapping("/api/rating")
public class RatingController {

    private final RatingService service;

    public RatingController(RatingService service){
        this.service = service;
    }

    @GetMapping("/league{leagueId}/user{userId}")
    public ResponseEntity<?> findRatingByIds(@PathVariable int leagueId, @PathVariable int userId, UsernamePasswordAuthenticationToken principal){
        AppUser appUser = (AppUser) principal.getPrincipal();
        Result<?> result = service.findRatingsByIds(leagueId, userId, appUser);
        if (result.isSuccess()){
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }

    @PostMapping("/new_rating")
    public ResponseEntity<?> createRating(@RequestBody Rating rating, UsernamePasswordAuthenticationToken principal){
        try {
            AppUser appUser = (AppUser) principal.getPrincipal();
            Result<?> result = service.createRating(rating, appUser);
            if (result.isSuccess()) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            return ErrorResponse.build(result);
        } catch (DuplicateKeyException ex){
            return new ResponseEntity<>(List.of("Duplicate rankings"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/change_rating")
    public ResponseEntity<?> updateRating(@RequestBody Rating rating, UsernamePasswordAuthenticationToken principal){
        try {
            AppUser appUser = (AppUser) principal.getPrincipal();
            Result<?> result = service.updateRating(rating, appUser);
            return ErrorResponse.noContentOrError(result);
        } catch (DuplicateKeyException ex){
            return new ResponseEntity<>(List.of("Duplicate rankings"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/league{leagueId}/user{userId}")
    public ResponseEntity<?> deleteRating(@PathVariable int leagueId, @PathVariable int userId, UsernamePasswordAuthenticationToken principal) {
        AppUser appUser = (AppUser) principal.getPrincipal();
        Result<?> result = service.deleteRating(leagueId, userId, appUser.getAppUserId());
        return ErrorResponse.noContentOrError(result);
    }
}
