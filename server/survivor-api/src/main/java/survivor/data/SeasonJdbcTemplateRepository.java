package survivor.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import survivor.data.mappers.CastawayMapper;
import survivor.data.mappers.SeasonCastawayMapper;
import survivor.data.mappers.SeasonMapper;
import survivor.models.Castaway;
import survivor.models.Season;
import survivor.models.SeasonCastaway;

import java.util.List;

@Repository
public class SeasonJdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    public SeasonJdbcTemplateRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public List<Season> findAllSeasons(){
        final String sql = "select season_id, `name` from season;";
        List<Season> seasons = jdbcTemplate.query(sql, new SeasonMapper());
        if (seasons.size() > 0){
            for (Season s : seasons){
                addCastaways(s);
            }
        }
        return seasons;
    }

    @Transactional
    public Season findSeasonById(int id){
        final String sql = "select season_id, `name` from season where season_id = ?;";
        Season season = jdbcTemplate.query(sql, new SeasonMapper(), id).stream()
                .findFirst().orElse(null);

        if (season != null){
            addCastaways(season);
        }
        return season;
    }

    @Transactional
    public boolean createSeason(Season season){
        final String sql = "insert into season(season_id, `name`) values (?, ?);";
        return jdbcTemplate.update(sql, season.getSeasonId(), season.getName()) > 0;
    }

    @Transactional
    public boolean updateSeason(Season season){
        final String sql = "update season set `name` = ? where season_id = ?;";
        return jdbcTemplate.update(sql, season.getName(), season.getSeasonId()) > 0;
    }

    @Transactional
    public boolean deleteSeasonById(int seasonId){
        final String sql = "delete from season where season_id = ?;";
        return jdbcTemplate.update(sql, seasonId) > 0;
    }

    ///////////////////// HELPER METHODS ////////////////////
    private void addCastaways(Season season){
        final String sql = "select c.castaway_id, c.first_name, c.last_name, c.age, c.current_residence, c.occupation, "
                + "c.icon_url, page_url, sc.tribe, sc.tribe_color from castaway c "
                + "inner join season_castaway sc on c.castaway_id = sc.castaway_id "
                + "where sc.season_id = ?;";
        List<SeasonCastaway> castaways = jdbcTemplate.query(sql,
                new SeasonCastawayMapper(), season.getSeasonId());
        season.setCastaways(castaways);
    }

    @Transactional
    public boolean seasonInUse(int seasonId){
        List<Integer> castaways = jdbcTemplate.query(
                "select castaway_id from season_castaway where season_id = ?;",
                (rs, rowNum) -> rs.getInt("castaway_id"),
                seasonId);
        List<Integer> leagues = jdbcTemplate.query(
                "select league_id from league where season_id = ?;",
                (rs, rowNum) -> rs.getInt("league_id"),
                seasonId);
        return castaways.size() > 0 || leagues.size() > 0;
    }
}
