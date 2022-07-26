package survivor.domain;

import org.springframework.stereotype.Service;
import survivor.data.CastawayJdbcTemplateRepository;
import survivor.models.Castaway;

import java.util.List;

@Service
public class CastawayService {

    private final CastawayJdbcTemplateRepository repository;

    public CastawayService(CastawayJdbcTemplateRepository repository){
        this.repository = repository;
    }

    public List<Castaway> findAllCastaways(){
        return repository.findAllCastaways();
    }
}
