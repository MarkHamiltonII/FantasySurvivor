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

}
