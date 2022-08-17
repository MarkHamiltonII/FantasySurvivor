package survivor.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import survivor.data.mappers.LeaderboardMapper;
import survivor.models.Leaderboard;

@Repository
public class LeaderboardJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public LeaderboardJdbcRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public int getCurrentTribal(int seasonId){
        final String sql = "select max(tribal_number) from tribal group by season_id = ?;";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> rs.getInt("max(tribal_number)"),
                seasonId).stream().findFirst().orElse(0);
    }


    @Transactional
    public Leaderboard getLeaderboardBySeasonAndLeague(int seasonId, int leagueId){
        int currentTribal = getCurrentTribal(seasonId);

        if (currentTribal == 0){
            return null;
        }

        final String sql = "select au.username, laut.points_to_date from league_app_user_tribal laut " +
                "inner join league_app_user lau on laut.lau_id = lau.id " +
                "inner join app_user au on lau.user_id = au.user_id " +
                "where laut.tribal_number = ? and lau.league_id = ? " +
                "order by laut.points_to_date desc;";

        Leaderboard leaderboard = new Leaderboard();

        jdbcTemplate.query(sql,
                (rs, rowNum) -> {
                        leaderboard.addEntryToRankings(
                            rs.getString("au.username"),
                            rs.getDouble("laut.points_to_date"));
                        return leaderboard;},
                currentTribal, leagueId);

        if (leaderboard.getRankings().size() == 0){
            return null;
        }

        leaderboard.setCurrentTribal(currentTribal);
        return leaderboard;
    }
}
