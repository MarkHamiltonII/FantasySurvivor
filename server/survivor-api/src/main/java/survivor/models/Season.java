package survivor.models;

import java.util.List;
import java.util.Objects;

public class Season {
    private int seasonId;
    private String name;
    private List<SeasonCastaway> castaways;

    public Season(int seasonId, String name, List<SeasonCastaway> castaways){
        this.seasonId = seasonId;
        this.name = name;
        this.castaways = castaways;
    }

    public Season(int seasonId, String name){
        this.seasonId = seasonId;
        this.name = name;
    }

    public Season(){

    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        Season season = (Season) o;
        return seasonId == season.seasonId && Objects.equals(name, season.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seasonId, name);
    }
}
