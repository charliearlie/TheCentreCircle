package uk.ac.prco.plymouth.thecentrecircle.classes;

/**
 * Created by charliewaite on 22/03/2016.
 */

//This class is unfinished and is currently being used for testing
public class Team {

    private String name;
    private int teamId;
    private String teamCity;

    public Team(String name, int teamId, String teamCity) {
        this.name = name;
        this.teamId = teamId;
        this.teamCity = teamCity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamCity() {
        return teamCity;
    }

    public void setTeamCity(String teamCity) {
        this.teamCity = teamCity;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}
