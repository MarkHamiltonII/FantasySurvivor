package survivor.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import survivor.models.League;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LeagueMapper implements RowMapper<League> {

    @Override
    public League mapRow(ResultSet rs, int rowNum) throws SQLException {
        League league = new League();

        league.setLeagueId(rs.getInt("league_id"));
        league.setName(rs.getString("name"));
        league.setSeasonId(rs.getInt("season_id"));

        return league;
    }
}
