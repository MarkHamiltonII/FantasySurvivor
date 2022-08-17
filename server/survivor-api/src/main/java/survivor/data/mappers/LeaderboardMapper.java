package survivor.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import survivor.models.Leaderboard;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LeaderboardMapper implements RowMapper<Leaderboard> {
    @Override
    public Leaderboard mapRow(ResultSet rs, int rowNum) throws SQLException {
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.addEntryToRankings(rs.getString("au.username"),
                rs.getDouble("laut.points_to_date"));

        return leaderboard;
    }
}
