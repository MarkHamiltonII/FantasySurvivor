package survivor.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import survivor.data.mappers.CastawayMapper;
import survivor.models.Castaway;

import java.util.List;

@Repository
public class CastawayJdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    public CastawayJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public List<Castaway> findAllCastaways(){

        final String sql = "select castaway_id, first_name, last_name, age, current_residence, occupation, icon_url, "
        + "page_url from castaway order by first_name, last_name;";
        List<Castaway> castaways = jdbcTemplate.query(sql, new CastawayMapper());
        return castaways;
    }

    @Transactional
    public List<Castaway> findCastawayBySeason(int id){

        final String sql = "select c.castaway_id, c.first_name, c.last_name, c.age, c.current_residence, c.occupation, "
                + "c.icon_url, page_url from castaway c "
                + "inner join season_castaway sc on c.castaway_id = sc.castaway_id "
                + "where sc.season_id = ?;";

        List<Castaway> castaways = jdbcTemplate.query(sql, new CastawayMapper(), id);
        return castaways;
    }

    @Transactional
    public Castaway findCastawayById(int id){

        final String sql = "select c.castaway_id, c.first_name, c.last_name, c.age, c.current_residence, c.occupation, "
                + "c.icon_url, page_url from castaway c "
                + "where c.castaway_id = ?;";

        return jdbcTemplate.query(sql, new CastawayMapper(), id).stream()
                .findFirst().orElse(null);
    }

    @Transactional
    public List<Castaway> findCastawayByTribal(int seasonId, int tribalNumber){

        final String sql = "select c.castaway_id, c.first_name, c.last_name, c.age, c.current_residence, c.occupation, "
                + "c.icon_url, page_url from castaway c "
                + "inner join tribal t on c.castaway_id = t.castaway_id "
                + "where t.season_id = ? and t.tribal_number = ?;";

        List<Castaway> castaways = jdbcTemplate.query(sql, new CastawayMapper(), seasonId, tribalNumber);
        return castaways;
    }

    @Transactional
    public boolean createCastaway(Castaway castaway){
        final String sql = "insert into castaway(first_name, last_name, age, current_residence, occupation, "
                + "icon_url, page_url) values (?, ?, ?, ?, ?, ?, ?);";

        int rowsUpdated = jdbcTemplate.update(sql,
                castaway.getFirstName(),
                castaway.getLastName(),
                castaway.getAge(),
                castaway.getCurrentResidence(),
                castaway.getOccupation(),
                castaway.getIconURL(),
                castaway.getPageURL());

        return rowsUpdated > 0;
    }

    @Transactional
    public boolean updateCastaway(Castaway castaway){
        final String sql = "update castaway set first_name = ?, "
                + "last_name = ?, "
                + "age = ?, "
                + "current_residence = ?, "
                + "occupation = ?, "
                + "icon_url = ?, "
                + "page_url = ? "
                + "where castaway_id = ?;";

        int rowsUpdated = jdbcTemplate.update(sql,
                castaway.getFirstName(),
                castaway.getLastName(),
                castaway.getAge(),
                castaway.getCurrentResidence(),
                castaway.getOccupation(),
                castaway.getIconURL(),
                castaway.getPageURL(),
                castaway.getId());

        return rowsUpdated > 0;
    }

    @Transactional
    public boolean deleteCastawayById(int id){
        jdbcTemplate.update("delete from season_castaway where castaway_id = ?;", id);
        return jdbcTemplate.update("delete from castaway where castaway_id = ?;", id) > 0;
    }

    @Transactional
    public boolean addCastawayToSeason(int seasonId, int castawayId){
        final String sql = "insert into season_castaway values (?, ?);";
        return jdbcTemplate.update(sql, seasonId, castawayId) > 0;
    }

    @Transactional
    public boolean removeCastawayFromSeason(int seasonId, int castawayId){
        final String sql = "delete from season_castaway where season_id = ? and castaway_id = ?;";
        return jdbcTemplate.update(sql, seasonId, castawayId) > 0;
    }

    @Transactional
    public boolean createTribal(int seasonId, int tribalNumber, List<Castaway> castaways){
        final String sql = "insert into tribal(tribal_number, season_id, castaway_id) values (?, ?, ?);";
        int rowsUpdated = 0;

        for (Castaway c : castaways){
            rowsUpdated = jdbcTemplate.update(sql, tribalNumber, seasonId, c.getId());
        }

        return rowsUpdated > 0;
    }

    @Transactional
    public boolean deleteTribal(int seasonId, int tribalNumber){
        final String sql = "delete from tribal where season_id = ? and tribal_number = ?;";

        return jdbcTemplate.update(sql, seasonId, tribalNumber) > 0;
    }

    @Transactional
    public boolean updateTribal(int seasonId, int tribalNumber, List<Castaway> castaways){
        if (!deleteTribal(seasonId, tribalNumber)){
            return false;
        }

        return createTribal(seasonId, tribalNumber, castaways);
    }

    @Transactional
    public boolean castawayInUse(int castawayId){
        List<Integer> laur = jdbcTemplate.query(
                "select laur_id from league_app_user_rating where castaway_id = ?;",
                (rs, rowNum) -> rs.getInt("laur_id"),
                castawayId);
        List<Integer> tribal = jdbcTemplate.query(
                "select tribal_id from tribal where castaway_id = ?;",
                (rs, rowNum) -> rs.getInt("tribal_id"),
                castawayId);
        return laur.size() > 0 || tribal.size() > 0;
    }

    @Transactional
    public List<Integer> findTribalNumbersBySeason(int seasonId){

        final String sql = "select distinct tribal_number from tribal where season_id = ?;";

        return jdbcTemplate.query(sql,
        (rs, rowNum) -> rs.getInt("tribal_Number"), seasonId);
    }

}
