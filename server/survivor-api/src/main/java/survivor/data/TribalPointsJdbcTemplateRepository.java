package survivor.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import survivor.data.mappers.CastawayMapper;
import survivor.data.mappers.LeagueMapper;
import survivor.data.mappers.TribalPointsMapper;
import survivor.models.Castaway;
import survivor.models.League;
import survivor.models.TribalPoints;

import java.util.List;

@Repository
public class TribalPointsJdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    public TribalPointsJdbcTemplateRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public List<TribalPoints> findTribalPointsByLeagueAppUser(int leagueId, int userId){
        int lauId = getLauId(leagueId,userId);
        final String sql = "select laut_id, tribal_points, points_to_date from "
                + "league_app_user_tribal where lau_id = ?;";
        List<TribalPoints> points = jdbcTemplate.query(sql, new TribalPointsMapper(), lauId);
        if (points.size() != 0){
            for (TribalPoints p : points){
                p.setLeague(findLeagueById(leagueId));
                p.setUserId(userId);
                addCastaways(p, leagueId, userId);
            }
        }
        return points;
    }

    @Transactional
    public TribalPoints findTribalPointsById(int lautId){
        final String sql = "select laut_id, tribal_points, points_to_date from "
                + "league_app_user_tribal where laut_id = ?;";
        TribalPoints points = jdbcTemplate.query(sql, new TribalPointsMapper(), lautId)
                .stream().findFirst().orElse(null);
        return points;
    }

    @Transactional
    public TribalPoints findTribalPointsByUserAndTribal(int leagueId, int userId, int tribalNumber){
        int lauId = getLauId(leagueId,userId);
        League league = findLeagueById(leagueId);
        int tribalId = getTribalId(league, tribalNumber);
        final String sql = "select laut_id, tribal_points, points_to_date from "
                + "league_app_user_tribal where lau_id = ? and tribal_id = ?;";
        TribalPoints tribalPoints = jdbcTemplate.query(sql, new TribalPointsMapper(),
                lauId, tribalId).stream()
                .findFirst().orElse(null);

        if (tribalPoints != null){
            addTribal(tribalPoints,league.getSeasonId(),tribalNumber);
            addCastaways(tribalPoints,leagueId,userId);
        }
        return tribalPoints;
    }

    @Transactional
    public boolean createTribalPoints(TribalPoints points, int tribalNumber){
        int lauId = getLauId(points.getLeague().getLeagueId(), points.getUserId());
        int tribalId = getTribalId(points.getLeague(),tribalNumber);
        final String sql = "insert into league_app_user_tribal"
                + "(tribal_points, points_to_date, lau_id, tribal_id) "
                + "values (?, ?, ?, ?);";
        return jdbcTemplate.update(sql, points.getWeekPoints(), points.getPointsToDate(), lauId, tribalId) > 0;
    }

    @Transactional
    public boolean deleteTribalPointsById(int lautId){
        final String sql = "delete from league_app_user_tribal where laut_id = ?;";
        return jdbcTemplate.update(sql, lautId) > 0;
    }

    @Transactional
    public boolean deleteTribalPointsByUser(int leagueId, int userId){
        int lauId = getLauId(leagueId,userId);
        final String sql = "delete from league_app_user_tribal where lau_id = ?;";
        return jdbcTemplate.update(sql, lauId) > 0;
    }

    @Transactional
    public boolean deleteTribalPointsByTribal(League league, int tribalNumber){
        int tribalId = getTribalId(league,tribalNumber);
        final String sql = "delete from league_app_user_tribal where tribal id = ?;";
        return jdbcTemplate.update(sql, tribalId) > 0;
    }

    @Transactional
    public boolean updateTribalPoints(TribalPoints points){
        final String sql = "update league_app_user_tribal set "
                + "tribal_points = ?, "
                + "points_to_date = ? "
                + "where laut_id = ?;";
        return jdbcTemplate.update(sql, points.getWeekPoints(), points.getPointsToDate()) > 0;
    }

    //////////////// Validation Helpers ////////////////
    public int getLauId(int leagueId, int userId) {
        return jdbcTemplate.query(
                        "select id from league_app_user where league_id = ? and user_id = ?;",
                        (rs, rowNum) -> rs.getInt("id"),
                        leagueId, userId)
                .stream().findFirst().orElse(0);
    }

    public int getTribalId(League league, int tribalNumber) {
        return jdbcTemplate.query(
                        "select tribal_id from tribal where season_id = ? and tribal_number = ?;",
                        (rs, rowNum) -> rs.getInt("tribal_id"),
                        league.getSeasonId(), tribalNumber)
                .stream().findFirst().orElse(0);
    }

    //////////////// Adding castaways /////////////////

    private void addTribal(TribalPoints tribalPoints, int seasonId, int tribalNumber){
        final String sql = "select c.castaway_id, c.first_name, c.last_name, c.age, c.current_residence, c.occupation, "
                + "c.icon_url, page_url from castaway c "
                + "inner join tribal t on c.castaway_id = t.castaway_id "
                + "where t.season_id = ? and t.tribal_number = ?;";
        List<Castaway> tribal = jdbcTemplate.query(sql, new CastawayMapper(), seasonId, tribalNumber);
        tribalPoints.setTribal(tribal);

    }

    private void addCastaways(TribalPoints tribalPoints, int leagueId, int userId){
        final String sql = "select c.castaway_id, c.first_name, c.last_name, c.age, c.current_residence, "
                + "c.occupation, c.icon_url, c.page_url from castaway c "
                + "inner join league_app_user_rating laur on c.castaway_id = laur.castaway_id "
                + "inner join league_app_user lau on laur.lau_id = lau.id "
                + "where lau.league_id = ? and lau.user_id = ? "
                + "order by laur.rating;";
        List<Castaway> castaways = jdbcTemplate.query(sql, new CastawayMapper(), leagueId, userId);
        tribalPoints.setCastaways(castaways);
    }

    @Transactional
    public League findLeagueById(int leagueId){
        final String sql = "select league_id, `name`, season_id, owner_id from league where league_id = ?;";
        League league = jdbcTemplate.query(sql, new LeagueMapper(), leagueId).stream()
                .findFirst().orElse(null);

        return league;
    }
}
