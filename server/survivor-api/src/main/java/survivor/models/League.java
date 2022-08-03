package survivor.models;

import java.util.List;
import java.util.Objects;

public class League {

    private int leagueId;
    private String name;
    private int seasonId;
    private int ownerId;
    private List<AppUser> appUsers;

    public League(int leagueId, String name, int seasonId, int ownerId, List<AppUser> appUsers) {
        this.leagueId = leagueId;
        this.name = name;
        this.seasonId = seasonId;
        this.ownerId = ownerId;
        this.appUsers = appUsers;
    }

    public League(int leagueId, String name, int seasonId, int ownerId) {
        this.leagueId = leagueId;
        this.name = name;
        this.seasonId = seasonId;
        this.ownerId = ownerId;
    }

    public League(String name, int seasonId) {
        this.name = name;
        this.seasonId = seasonId;
        this.ownerId = 0;
    }

    public League(){
    }

    public int getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public List<AppUser> getAppUsers() {
        return appUsers;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setAppUsers(List<AppUser> appUsers) {
        this.appUsers = appUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        League league = (League) o;
        return leagueId == league.leagueId && seasonId == league.seasonId && Objects.equals(name, league.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leagueId, name, seasonId);
    }
}
