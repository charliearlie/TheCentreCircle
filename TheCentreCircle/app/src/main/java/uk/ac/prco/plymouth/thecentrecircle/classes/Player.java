package uk.ac.prco.plymouth.thecentrecircle.classes;

import java.io.Serializable;

/**
 * Created by charliewaite on 11/04/2016.
 */
public class Player implements Serializable {
    private String age;
    private String appearences; //Spelling mistake provided by the API...
    private String assists;
    private String goals;
    private String id;
    private String injured;
    private String lineups;
    private String minutes;
    private String name;
    private String number;
    private String position;
    private String redcards;
    private String substitute_in; //Again underscore because of API key names
    private String substitute_out;
    private String substitutes_on_bench;
    private String yellowcards;
    private String yellowred;

    public Player() {}

    public Player(String yellowred, String yellowcards, String age, String appearences,
                  String assists, String goals, String id, String injured, String lineups,
                  String minutes, String name, String number, String position, String redcards,
                  String substitute_in, String substitute_out, String substitutes_on_bench) {
        this.yellowred = yellowred;
        this.yellowcards = yellowcards;
        this.age = age;
        this.appearences = appearences;
        this.assists = assists;
        this.goals = goals;
        this.id = id;
        this.injured = injured;
        this.lineups = lineups;
        this.minutes = minutes;
        this.name = name;
        this.number = number;
        this.position = position;
        this.redcards = redcards;
        this.substitute_in = substitute_in;
        this.substitute_out = substitute_out;
        this.substitutes_on_bench = substitutes_on_bench;
    }

    public String getYellowred() {
        return yellowred;
    }

    public String getAge() {
        return age;
    }

    public String getAppearences() {
        return appearences;
    }

    public String getAssists() {
        return assists;
    }

    public String getGoals() {
        return goals;
    }

    public String getId() {
        return id;
    }

    public String getInjured() {
        return injured;
    }

    public String getLineups() {
        return lineups;
    }

    public String getMinutes() {
        return minutes;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getPosition() {
        return position;
    }

    public String getRedcards() {
        return redcards;
    }

    public String getSubstitute_in() {
        return substitute_in;
    }

    public String getSubstitute_out() {
        return substitute_out;
    }

    public String getSubstitutes_on_bench() {
        return substitutes_on_bench;
    }

    public String getYellowcards() {
        return yellowcards;
    }
}
