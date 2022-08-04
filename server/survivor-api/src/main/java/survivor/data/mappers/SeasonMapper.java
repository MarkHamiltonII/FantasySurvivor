package survivor.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import survivor.models.Season;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SeasonMapper implements RowMapper<Season> {
    @Override
    public Season mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Season(rs.getInt("season_id"),rs.getString("name"));
    }
}
