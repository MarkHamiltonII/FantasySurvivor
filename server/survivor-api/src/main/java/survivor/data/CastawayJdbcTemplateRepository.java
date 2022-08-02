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
    public List<Castaway> findCastawayByTribal(int seasonId, int tribalNumber){

        final String sql = "select c.castaway_id, c.first_name, c.last_name, c.age, c.current_residence, c.occupation, "
                + "c.icon_url, page_url from castaway c "
                + "inner join tribal t on c.castaway_id = t.castaway_id "
                + "where t.season_id = ? and t.tribal_number = ?;";

        List<Castaway> castaways = jdbcTemplate.query(sql, new CastawayMapper(), seasonId, tribalNumber);
        return castaways;
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

}
