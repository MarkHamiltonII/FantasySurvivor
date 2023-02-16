package survivor.models;

public class SeasonCastaway extends Castaway{
    private String tribe;
    private String tribeColor;

    public SeasonCastaway(String tribe, String tribeColor) {
        this.tribe = tribe;
        this.tribeColor = tribeColor;
    }

    public SeasonCastaway() {
    }

    public String getTribe() {
        return tribe;
    }

    public void setTribe(String tribe) {
        this.tribe = tribe;
    }

    public String getTribeColor() {
        return tribeColor;
    }

    public void setTribeColor(String tribeColor) {
        this.tribeColor = tribeColor;
    }

    @Override
    public String toString() {
        return "SeasonCastaway{" +
                "tribe='" + tribe + '\'' +
                ", tribeColor='" + tribeColor + '\'' +
                '}';
    }
}
