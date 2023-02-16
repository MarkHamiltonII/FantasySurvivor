package survivor.models;

import java.util.List;
import java.util.Objects;

public class TribalPoints {
    private int id;
    private double weekPoints;
    private double pointsToDate;

    private League league;

    private int userId;

    private List<SeasonCastaway> tribal;
    private List<SeasonCastaway> castaways;

    public TribalPoints(int id, double weekPoints, double pointsToDate) {
        this.id = id;
        this.weekPoints = weekPoints;
        this.pointsToDate = pointsToDate;

    }

    public TribalPoints(int id, double weekPoints, double pointsToDate, League league, int userId, List<SeasonCastaway> tribal, List<SeasonCastaway> castaways) {
        this.id = id;
        this.weekPoints = weekPoints;
        this.pointsToDate = pointsToDate;
        this.league = league;
        this.userId = userId;
        this.tribal = tribal;
        this.castaways = castaways;
    }

    public TribalPoints(int id, double weekPoints, double pointsToDate, League league, int userId) {
        this.id = id;
        this.weekPoints = weekPoints;
        this.pointsToDate = pointsToDate;
        this.league = league;
        this.userId = userId;
    }

    public TribalPoints(double weekPoints, double pointsToDate, League league, int userId) {
        this.weekPoints = weekPoints;
        this.pointsToDate = pointsToDate;
        this.league = league;
        this.userId = userId;
    }

    public TribalPoints(double weekPoints, double pointsToDate) {
        this.weekPoints = weekPoints;
        this.pointsToDate = pointsToDate;
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

    public List<SeasonCastaway> getTribal() {
        return tribal;
    }

    public void setTribal(List<SeasonCastaway> tribal) {
        this.tribal = tribal;
    }

    public List<SeasonCastaway> getCastaways() {
        return castaways;
    }

    public void setCastaways(List<SeasonCastaway> castaways) {
        this.castaways = castaways;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TribalPoints that = (TribalPoints) o;
        return id == that.id && userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}
