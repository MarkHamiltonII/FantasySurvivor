package survivor.models;

import java.util.List;
import java.util.Objects;

public class Rating {
    private int userId;
    private int leagueId;
    private List<SeasonCastaway> castaways;

    public Rating(int leagueId, int userId, List<SeasonCastaway> castaways){
        this.leagueId = leagueId;
        this.userId = userId;
        this.castaways = castaways;
    }
    public Rating(int leagueId, int userId){
        this.leagueId = leagueId;
        this.userId = userId;
    }
    public Rating(){}

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    public List<SeasonCastaway> getCastaways() {
        return castaways;
    }

    public void setCastaways(List<SeasonCastaway> castaways) {
        this.castaways = castaways;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return userId == rating.userId && leagueId == rating.leagueId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, leagueId);
    }
}
