package survivor.controllers;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import survivor.domain.CastawayService;
import survivor.domain.Result;
import survivor.models.Castaway;

import java.util.List;

@RestController
@RequestMapping("/api/castaway")
public class CastawayController {

    private final CastawayService service;

    public CastawayController(CastawayService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Castaway> findAllCastaways() {
        return service.findAllCastaways();
    }

    @GetMapping("/castaway{id}")
    public ResponseEntity<?> findCastawayById(@PathVariable int id){
        Result<?> result = service.findCastawayById(id);
        if (result.isSuccess()){
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }

    @PostMapping("/new_castaway")
    public ResponseEntity<?> createCastaway(@RequestBody Castaway castaway){
        Result<?> result = service.createCastaway(castaway);
        if (result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/change_castaway")
    public ResponseEntity<?> updateCastaway(@RequestBody Castaway castaway){
        Result<?> result = service.updateCastaway(castaway);
        return ErrorResponse.noContentOrError(result);
    }

    @DeleteMapping("/castaway{id}")
    public ResponseEntity<?> deleteCastaway(@PathVariable int id){
        Result<?> result = service.deleteCastawayById(id);
        return ErrorResponse.noContentOrError(result);
    }

    @PostMapping("/season{seasonId}/castaway{castawayId}")
    public ResponseEntity<?> addCastawayToSeason(@PathVariable int seasonId, @PathVariable int castawayId){
        try {
            Result<?> result = service.addCastawayToSeason(seasonId, castawayId);
            return ErrorResponse.noContentOrError(result);
        } catch (DuplicateKeyException ex){
            return new ResponseEntity<>(List.of("Castaway already in season"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/season{seasonId}/castaway{castawayId}")
    public ResponseEntity<?> removeCastawayFromSeason(@PathVariable int seasonId, @PathVariable int castawayId){
        Result<?> result = service.removeCastawayFromSeason(seasonId, castawayId);
        return ErrorResponse.noContentOrError(result);
    }

    @GetMapping("/season{id}")
    public ResponseEntity<?> findCastawaysBySeason(@PathVariable int id) {
        Result<List<Castaway>> result = service.findCastawaysBySeason(id);
        if (result.isSuccess()){
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }

    @GetMapping("/season{seasonId}/tribal{tribalNumber}")
    public ResponseEntity<?> findCastawaysByTribal(@PathVariable int seasonId, @PathVariable int tribalNumber) {
        Result<List<Castaway>> result = service.findCastawaysByTribal(seasonId, tribalNumber);
        if (result.isSuccess()){
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }

    @PostMapping("/season{seasonId}/tribal{tribalNumber}")
    public ResponseEntity<?> createTribal(@PathVariable int seasonId, @PathVariable int tribalNumber, @RequestBody List<Castaway> castaways) {
        Result<?> result = service.createTribal(seasonId, tribalNumber, castaways);
        return ErrorResponse.noContentOrError(result);
    }

    @PutMapping("/season{seasonId}/tribal{tribalNumber}")
    public ResponseEntity<?> updateTribal(@PathVariable int seasonId, @PathVariable int tribalNumber, @RequestBody List<Castaway> castaways) {
        Result<?> result = service.updateTribal(seasonId, tribalNumber, castaways);
        return ErrorResponse.noContentOrError(result);
    }

    @DeleteMapping("/season{seasonId}/tribal{tribalNumber}")
    public ResponseEntity<?> deleteTribal(@PathVariable int seasonId, @PathVariable int tribalNumber) {
        Result<?> result = service.deleteTribal(seasonId, tribalNumber);
        return ErrorResponse.noContentOrError(result);
    }

}
