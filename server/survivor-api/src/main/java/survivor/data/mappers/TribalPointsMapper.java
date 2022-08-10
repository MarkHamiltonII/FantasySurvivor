package survivor.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import survivor.models.TribalPoints;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TribalPointsMapper implements RowMapper<TribalPoints> {
    @Override
    public TribalPoints mapRow(ResultSet rs, int rowNum) throws SQLException {
        TribalPoints tribalPoints = new TribalPoints();

        tribalPoints.setId(rs.getInt("laut_id"));
        tribalPoints.setWeekPoints(rs.getDouble("tribal_points"));
        tribalPoints.setPointsToDate(rs.getDouble("points_to_date"));
        tribalPoints.setLeagueAppUserId(rs.getInt("lau_id"));
        tribalPoints.setTribalId(rs.getInt("tribal_id"));

        return tribalPoints;
    }
}
