package survivor.controllers;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import survivor.domain.Result;
import survivor.domain.SeasonService;
import survivor.models.Season;

import java.util.List;

@RestController
@RequestMapping("/api/season")
public class SeasonController {
    private final SeasonService service;
    public SeasonController(SeasonService service){
        this.service = service;
    }

    @GetMapping("/all")
    public List<Season> findAllSeasons(){
        return service.findAllSeasons();
    }

    @GetMapping("/season{id}")
    public ResponseEntity<?> findSeasonById(@PathVariable int id){
        Result<?> result = service.findSeasonById(id);
        if (result.isSuccess()){
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }

    @PostMapping("/new_season")
    public ResponseEntity<?> createSeason(@RequestBody Season season){
        try {
            Result<?> result = service.createSeason(season);
            return ErrorResponse.noContentOrError(result);
        } catch (DuplicateKeyException ex) {
            return new ResponseEntity<>(List.of("Season name taken"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/change_season")
    public ResponseEntity<?> updateSeason(@RequestBody Season season){
        try {
            Result<?> result = service.updateSeason(season);
            return ErrorResponse.noContentOrError(result);
        } catch (DuplicateKeyException ex) {
            return new ResponseEntity<>(List.of("Season name taken"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/season{id}")
    public ResponseEntity<?> deleteSeasonById(@PathVariable int id){
        Result<?> result = service.deleteSeasonById(id);
        return ErrorResponse.noContentOrError(result);
    }
}
