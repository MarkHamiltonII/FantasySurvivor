package survivor.models;

import java.util.HashMap;
import java.util.Objects;

public class Leaderboard {

    private int currentTribal;
    private HashMap<String, Double> rankings;

    public Leaderboard(int currentTribal, HashMap<String, Double> rankings) {
        this.currentTribal = currentTribal;
        this.rankings = rankings;
    }

    public Leaderboard() {
        this.rankings = new HashMap<>();
    }

    public int getCurrentTribal() {
        return currentTribal;
    }

    public void setCurrentTribal(int currentTribal) {
        this.currentTribal = currentTribal;
    }

    public HashMap<String, Double> getRankings() {
        return rankings;
    }

    public void addEntryToRankings(String username, double points_to_date){
        this.rankings.put(username,points_to_date);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Leaderboard that = (Leaderboard) o;
        return currentTribal == that.currentTribal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentTribal);
    }
}
