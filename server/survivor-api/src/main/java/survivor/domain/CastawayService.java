package survivor.domain;

import org.springframework.stereotype.Service;
import survivor.data.CastawayJdbcTemplateRepository;
import survivor.models.Castaway;
import survivor.models.SeasonCastaway;

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

    public Result<Castaway> findCastawayById(int id){
        Result<Castaway> result = new Result<>();
        Castaway castaway = repository.findCastawayById(id);
        if (castaway == null){
            result.addMessage("Cannot find castaway with ID: " + id, ResultType.NOT_FOUND);
            return result;
        }
        result.setPayload(castaway);
        return result;
    }

    public Result<List<SeasonCastaway>> findCastawaysBySeason(int id) {
        List<SeasonCastaway> castaways = repository.findCastawayBySeason(id);
        Result<List<SeasonCastaway>> result = new Result<>();
        if (castaways.size() == 0){
            result.addMessage("Season not found", ResultType.INVALID);
            return result;
        }

        result.setPayload(castaways);

        return result;
    }

    public Result<List<SeasonCastaway>> findCastawaysByTribal(int seasonId, int tribalNumber) {
        List<SeasonCastaway> castaways = repository.findCastawayByTribal(seasonId, tribalNumber);
        Result<List<SeasonCastaway>> result = new Result<>();
        if (castaways.size() == 0){
            result.addMessage("Tribal not found", ResultType.INVALID);
            return result;
        }

        result.setPayload(castaways);

        return result;
    }

    public Result<?> createCastaway(Castaway castaway){
        Result<?> result = validateCastaway(castaway);

        if (!result.isSuccess()){
            return result;
        }
        if (castaway.getId() > 0){
            result.addMessage("Cannot set id during castaway creation", ResultType.INVALID);
            return result;
        }
        if (!repository.createCastaway(castaway)){
            result.addMessage("Castaway could not be created", ResultType.INVALID);
        }

        return result;
    }

    public Result<?> updateCastaway(Castaway castaway){
        Result<?> result = validateCastaway(castaway);

        if (!result.isSuccess()){
            return result;
        }

        Castaway oldCastaway = repository.findCastawayById(castaway.getId());
        if (oldCastaway == null){
            result.addMessage("Castaway not found", ResultType.NOT_FOUND);
            return result;
        }

        if (!repository.updateCastaway(castaway)){
            result.addMessage("Castaway could not be updated", ResultType.INVALID);
        }

        return result;
    }

    public Result<?> deleteCastawayById(int castawayId){
        Result<?> result = new Result<>();
        if (repository.castawayInUse(castawayId)){
            result.addMessage("Cannot delete castaway in use", ResultType.INVALID);
            return result;
        }
        if (!repository.deleteCastawayById(castawayId)){
            result.addMessage("Castaway not found", ResultType.NOT_FOUND);
        }
        return result;
    }

    public Result<?> addCastawayToSeason(int seasonId, int castawayId){
        Result<?> result = new Result<>();
        if (!repository.addCastawayToSeason(seasonId,castawayId)){
            result.addMessage("Could not add castaway to season " + seasonId, ResultType.INVALID);
        }
        return result;
    }

    public Result<?> removeCastawayFromSeason(int seasonId, int castawayId){
        Result<?> result = new Result<>();
        if (!repository.removeCastawayFromSeason(seasonId,castawayId)){
            result.addMessage("Could not remove castaway from season " + seasonId, ResultType.INVALID);
        }
        return result;
    }

    public Result<?> createTribal(int seasonId, int tribalNumber, List<Castaway> castaways){
        Result<?> result = validateTribal(seasonId,castaways);

        if (!result.isSuccess()){
            return result;
        }

        List<SeasonCastaway> oldTribal = repository.findCastawayByTribal(seasonId, tribalNumber);
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
        Result<?> result = validateTribal(seasonId,castaways);

        if (!result.isSuccess()){
            return result;
        }

        List<SeasonCastaway> oldTribal = repository.findCastawayByTribal(seasonId, tribalNumber);
        if (oldTribal.size() == 0){
            result.addMessage("Tribal number does not exists, cannot update", ResultType.INVALID);
            return result;
        }

        if (!repository.updateTribal(seasonId, tribalNumber, castaways)){
            result.addMessage("Tribal could not be updated", ResultType.NOT_FOUND);
        }

        return result;
    }

    public Result<List<Integer>> findTribalNumbersBySeason(int seasonId) {
        List<Integer> tribals = repository.findTribalNumbersBySeason(seasonId);
        Result<List<Integer>> result = new Result<>();
        if (tribals.size() == 0){
            result.addMessage("No tribals found", ResultType.INVALID);
            return result;
        }

        result.setPayload(tribals);

        return result;
    }

    /////////////////////////////////// Validation Methods //////////////////////////////////////////

    public Result<?> validateCastaway(Castaway castaway){
        Result<?> result = new Result<>();

        if (castaway == null) {
            result.addMessage("Castaway cannot be null", ResultType.INVALID);
            return result;
        }
        if (castaway.getFirstName() == null || castaway.getFirstName().isEmpty()){
            result.addMessage("First name is required", ResultType.INVALID);
        }
        if (castaway.getLastName() == null || castaway.getLastName().isEmpty()){
            result.addMessage("Last name is required", ResultType.INVALID);
        }
        if (castaway.getAge() < 14 || castaway.getAge() > 110){
            result.addMessage("Invalid age", ResultType.INVALID);
        }
        if (castaway.getIconURL() == null || castaway.getIconURL().isBlank()){
            result.addMessage("Icon required", ResultType.INVALID);
        }
        if (castaway.getPageURL() == null || castaway.getPageURL().isBlank()){
            result.addMessage("Page link required", ResultType.INVALID);
        }
        return result;
    }

    public Result<?> validateTribal(int seasonId, List<Castaway> castaways){
        Result<?> result = new Result<>();

        List<SeasonCastaway> season = repository.findCastawayBySeason(seasonId);
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
