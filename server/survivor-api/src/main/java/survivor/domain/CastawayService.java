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

    public Result<List<Castaway>> findCastawaysBySeason(int id) {
        List<Castaway> castaways = repository.findCastawayBySeason(id);
        Result<List<Castaway>> result = new Result<>();
        if (castaways.size() == 0){
            result.addMessage("Season not found", ResultType.INVALID);
            return result;
        }

        result.setPayload(castaways);

        return result;
    }

    public Result<List<Castaway>> findCastawaysByTribal(int seasonId, int tribalNumber) {
        List<Castaway> castaways = repository.findCastawayByTribal(seasonId, tribalNumber);
        Result<List<Castaway>> result = new Result<>();
        if (castaways.size() == 0){
            result.addMessage("Tribal not found", ResultType.INVALID);
            return result;
        }

        result.setPayload(castaways);

        return result;
    }
}
