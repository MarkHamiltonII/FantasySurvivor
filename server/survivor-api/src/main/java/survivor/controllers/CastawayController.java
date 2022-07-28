package survivor.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import survivor.domain.CastawayService;
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
}
