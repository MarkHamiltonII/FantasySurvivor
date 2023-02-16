package survivor.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import survivor.models.Castaway;
import survivor.models.SeasonCastaway;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SeasonCastawayMapper implements RowMapper<SeasonCastaway> {

    @Override
    public SeasonCastaway mapRow(ResultSet rs, int rowNum) throws SQLException {
        SeasonCastaway castaway = new SeasonCastaway();

        castaway.setId(rs.getInt("castaway_id"));
        castaway.setFirstName(rs.getString("first_name"));
        castaway.setLastName(rs.getString("last_name"));
        castaway.setAge(rs.getInt("age"));
        castaway.setCurrentResidence(rs.getString("current_residence"));
        castaway.setOccupation(rs.getString("occupation"));
        castaway.setIconURL(rs.getString("icon_url"));
        castaway.setPageURL(rs.getString("page_url"));
        castaway.setTribe(rs.getString("tribe"));
        castaway.setTribeColor(rs.getString("tribe_color"));

        return castaway;
    }


}
