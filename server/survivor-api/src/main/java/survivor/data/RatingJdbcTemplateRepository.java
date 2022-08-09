package survivor.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import survivor.data.mappers.CastawayMapper;
import survivor.models.Castaway;
import survivor.models.Rating;

import java.util.List;

@Repository
public class RatingJdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    public RatingJdbcTemplateRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public Rating findCastawayByIds(int leagueId, int userId){
        final String sql = "select c.castaway_id, c.first_name, c.last_name, c.age, c.current_residence, "
                + "c.occupation, c.icon_url, c.page_url from castaway c "
                + "inner join league_app_user_rating laur on c.castaway_id = laur.castaway_id "
                + "inner join league_app_user lau on laur.lau_id = lau.id "
                + "where lau.league_id = ? and lau.user_id = ? "
                + "order by laur.rating;";
        List<Castaway> castaways = jdbcTemplate.query(sql, new CastawayMapper(), leagueId, userId);
        return new Rating(leagueId,userId,castaways);
    }

    @Transactional
    public boolean createRating(Rating rating){
        int lauId = jdbcTemplate.query(
                "select id from league_app_user where league_id = ? and user_id = ?;",
                (rs, rowNum)-> rs.getInt("id"),
                rating.getLeagueId(), rating.getUserId())
                .stream().findFirst().orElse(0);

        if (lauId == 0 || rating.getCastaways() == null){
            return false;
        }

        final String sql = "insert into league_app_user_rating(lau_id, rating, castaway_id) values (?, ?, ?);";
        int rank = 1;
        int rowsUpdated = 0;
        for (Castaway c : rating.getCastaways()){
            rowsUpdated = jdbcTemplate.update(sql, lauId, rank, c.getId());
            rank++;
        }
        return rowsUpdated > 0;
    }

    @Transactional
    public boolean deleteRatingByIds(int leagueId, int userId){
        final String sql = "delete from league_app_user_rating where league_id = ? and user_id = ?;";
        return jdbcTemplate.update(sql, leagueId, userId) > 0;
    }

    @Transactional
    public boolean updateRating(Rating rating){
        if (!deleteRatingByIds(rating.getLeagueId(), rating.getUserId())){
            return false;
        }
        return createRating(rating);
    }
}
