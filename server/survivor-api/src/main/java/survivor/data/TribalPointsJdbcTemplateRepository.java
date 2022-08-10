package survivor.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import survivor.data.mappers.TribalPointsMapper;
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
        final String sql = "select laut_id, tribal_points, points_to_date, lau_id, tribal_id from "
                + "league_app_user_tribal where lau_id = ?;";
        return jdbcTemplate.query(sql, new TribalPointsMapper(), lauId);
    }

    //////////////// Validation Helpers ////////////////
    public int getLauId(int leagueId, int userId) {
        return jdbcTemplate.query(
                        "select id from league_app_user where league_id = ? and user_id = ?;",
                        (rs, rowNum) -> rs.getInt("id"),
                        leagueId, userId)
                .stream().findFirst().orElse(0);
    }
}
