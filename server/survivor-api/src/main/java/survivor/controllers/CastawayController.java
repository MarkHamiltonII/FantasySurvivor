package survivor.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
