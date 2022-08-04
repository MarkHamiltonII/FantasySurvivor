package survivor.models;

import java.util.Objects;

public class Castaway {

    private int id;

    private String firstName;

    private String lastName;

    private int age;

    private String currentResidence;

    private String occupation;

    private String iconURL;

    private String pageURL;

    public Castaway() {

    }

    public Castaway(int id, String firstName, String lastName, String iconURL, String pageURL) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.iconURL = iconURL;
        this.pageURL = pageURL;
    }

    public Castaway(int id, String firstName, String lastName, int age, String currentResidence, String occupation, String iconURL, String pageURL) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.currentResidence = currentResidence;
        this.occupation = occupation;
        this.iconURL = iconURL;
        this.pageURL = pageURL;
    }

    public Castaway(String firstName, String lastName, int age, String currentResidence, String occupation, String iconURL, String pageURL) {
        this.id = 0;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.currentResidence = currentResidence;
        this.occupation = occupation;
        this.iconURL = iconURL;
        this.pageURL = pageURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCurrentResidence() {
        return currentResidence;
    }

    public void setCurrentResidence(String currentResidence) {
        this.currentResidence = currentResidence;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getPageURL() {
        return pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Castaway castaway = (Castaway) o;
        return id == castaway.id && Objects.equals(firstName, castaway.firstName) && Objects.equals(lastName, castaway.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }
}
