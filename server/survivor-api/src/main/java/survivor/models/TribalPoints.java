package survivor.models;

import java.util.Objects;

public class TribalPoints {
    private int id;
    private double weekPoints;
    private double pointsToDate;
    private int leagueAppUserId;
    private int tribalId;

    public TribalPoints(int id, double weekPoints, double pointsToDate, int leagueAppUserId, int tribalId) {
        this.id = id;
        this.weekPoints = weekPoints;
        this.pointsToDate = pointsToDate;
        this.leagueAppUserId = leagueAppUserId;
        this.tribalId = tribalId;
    }

    public TribalPoints(double weekPoints, double pointsToDate, int leagueAppUserId, int tribalId) {
        this.weekPoints = weekPoints;
        this.pointsToDate = pointsToDate;
        this.leagueAppUserId = leagueAppUserId;
        this.tribalId = tribalId;
    }

    public TribalPoints(int leagueAppUserId, int tribalId) {
        this.leagueAppUserId = leagueAppUserId;
        this.tribalId = tribalId;
    }

    public TribalPoints() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getWeekPoints() {
        return weekPoints;
    }

    public void setWeekPoints(double weekPoints) {
        this.weekPoints = weekPoints;
    }

    public double getPointsToDate() {
        return pointsToDate;
    }

    public void setPointsToDate(double pointsToDate) {
        this.pointsToDate = pointsToDate;
    }

    public int getLeagueAppUserId() {
        return leagueAppUserId;
    }

    public void setLeagueAppUserId(int leagueAppUserId) {
        this.leagueAppUserId = leagueAppUserId;
    }

    public int getTribalId() {
        return tribalId;
    }

    public void setTribalId(int tribalId) {
        this.tribalId = tribalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TribalPoints that = (TribalPoints) o;
        return id == that.id && leagueAppUserId == that.leagueAppUserId && tribalId == that.tribalId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, leagueAppUserId, tribalId);
    }
}
