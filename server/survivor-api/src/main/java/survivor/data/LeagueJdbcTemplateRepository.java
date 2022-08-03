package survivor.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import survivor.data.mappers.AppUserMapper;
import survivor.data.mappers.LeagueMapper;
import survivor.models.AppUser;
import survivor.models.League;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class LeagueJdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    public LeagueJdbcTemplateRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public List<League> findAllLeagues(){
        final String sql = "select league_id, `name`, season_id from league order by season_id;";
        List<League> leagues = jdbcTemplate.query(sql, new LeagueMapper());
        return leagues;
    }

    @Transactional
    public League findLeagueById(int leagueId){
        final String sql = "select league_id, `name`, season_id from league where league_id = ?;";
        League league = jdbcTemplate.query(sql, new LeagueMapper(), leagueId).stream()
                .findFirst().orElse(null);

        if (league != null) {
            addAppUsers(league);
        }

        return league;
    }

    @Transactional
    public List<League> findLeaguesByAppUserId(int appUserId){
        final String sql = "select l.league_id, l.`name`, l.season_id from league l "
                + "inner join league_app_user lau on l.league_id = lau.league_id "
                + "where user_id = ?;";
        List<League> leagues = jdbcTemplate.query(sql, new LeagueMapper(), appUserId);

        if (leagues.size() > 0) {
            for (League l : leagues){
            addAppUsers(l);
            }
        }

        return leagues;
    }

    @Transactional
    public boolean createLeague(League league){
        final String sql = "insert into league(`name`, season_id) values (?, ?);";
        return jdbcTemplate.update(sql, league.getName(), league.getSeasonId()) > 0;
    }

    @Transactional
    public boolean updateLeague(League league){
        final String sql = "update league set `name` = ? where league_id = ?;";
        return jdbcTemplate.update(sql, league.getName(), league.getLeagueId()) > 0;
    }

    @Transactional
    public boolean deleteLeagueById(int id){
       return jdbcTemplate.update("delete from league where league_id = ?;", id) > 0;
    }


    /////////////////////////// HELPER ADD METHODS /////////////////////////////////
    private void addAppUsers(League league){
        final String sql = "select distinct "
                + "au.user_id, au.username, au.password_hash, au.disabled "
                + "from app_user au "
                + "inner join league_app_user lau on au.user_id = lau.user_id "
                + "where lau.league_id = ?;";

        List<AppUser> appUsers = jdbcTemplate.query(sql,
                new AppUserMapper(List.of("USER")), league.getLeagueId());
        league.setAppUsers(appUsers);
    }

    public boolean leagueIsEmpty(int league_id){
        List<Integer> rowsUpdated = jdbcTemplate.query(
                "select id from league_app_user where league_id = ?;",
                (rs, rowNum) -> rs.getInt("id"),
                league_id);
        return rowsUpdated.size() == 0;
    }
}
