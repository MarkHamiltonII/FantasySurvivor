package survivor.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public Result<?> createTribal(int seasonId, int tribalNumber, List<Castaway> castaways){
        Result<?> result = validateTribal(seasonId,tribalNumber,castaways);

        if (!result.isSuccess()){
            return result;
        }

        List<Castaway> oldTribal = repository.findCastawayByTribal(seasonId, tribalNumber);
        if (oldTribal.size() != 0){
            result.addMessage("Tribal number already exists", ResultType.INVALID);
            return result;
        }

        if (!repository.createTribal(seasonId,tribalNumber,castaways)){
            result.addMessage("Tribal could not be created", ResultType.INVALID);
        }

        return result;
    }

    public Result<?> deleteTribal(int seasonId, int tribalNumber){
        Result<?> result = new Result<>();
        if (!repository.deleteTribal(seasonId, tribalNumber)){
            result.addMessage("Tribal not found", ResultType.NOT_FOUND);
        }
        return result;
    }

    public Result<?> updateTribal(int seasonId, int tribalNumber, List<Castaway> castaways){
        Result<?> result = validateTribal(seasonId,tribalNumber,castaways);

        if (!result.isSuccess()){
            return result;
        }

        List<Castaway> oldTribal = repository.findCastawayByTribal(seasonId, tribalNumber);
        if (oldTribal.size() == 0){
            result.addMessage("Tribal number does not exists, cannot update", ResultType.INVALID);
            return result;
        }

        if (!repository.updateTribal(seasonId, tribalNumber, castaways)){
            result.addMessage("Tribal could not be updated", ResultType.NOT_FOUND);
        }

        return result;
    }

    /////////////////////////////////// Validation Methods //////////////////////////////////////////

    public Result<?> validateTribal(int seasonId, int tribalNumber, List<Castaway> castaways){
        Result<?> result = new Result<>();

        List<Castaway> season = repository.findCastawayBySeason(seasonId);
        if (season.size() == 0) {
            result.addMessage("Season not in system", ResultType.INVALID);
            return result;
        }

        for (Castaway c : castaways){
            if (!season.contains(c)){
                result.addMessage("Invalid Castaway for Season", ResultType.INVALID);
                return result;
            }
        }
        return result;
    }
}
