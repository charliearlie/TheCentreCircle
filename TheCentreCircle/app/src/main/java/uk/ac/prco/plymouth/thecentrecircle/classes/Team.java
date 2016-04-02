package uk.ac.prco.plymouth.thecentrecircle.classes;

import java.io.Serializable;

/**
 * Created by charliewaite on 22/03/2016.
 */

//This class is unfinished and is currently being used for testing
public class Team implements Serializable {

    //Only case where I do not use the recommended camel case as the variable names
    //must match that of the key names in Firebase
    private String coach_id;
    private String coach_name;
    private String country;
    private String founded;
    private String is_national;
    private String leagues;
    private String name;
    private Object sidelined;
    private Object squad;
    private Object statistics;
    private String team_id;
    private Object transfers_in;
    private Object transfers_out;
    private String venue_address;
    private String venue_capacity;
    private String venue_city;
    private String venue_id;
    private String venue_name;
    private String venue_surface;

    public Team(String coach_id, String coach_name, String country, String founded,
                String is_national, String leagues, String name,
                String team_id, String venue_address, String venue_capacity, String venue_city,
                String venue_id, String venue_name, String venue_surface) {

        this.coach_id = coach_id;
        this.coach_name = coach_name;
        this.country = country;
        this.founded = founded;
        this.is_national = is_national;
        this.leagues = leagues;
        this.name = name;
        this.team_id = team_id;
        this.venue_address = venue_address;
        this.venue_capacity = venue_capacity;
        this.venue_city = venue_city;
        this.venue_id = venue_id;
        this.venue_name = venue_name;
        this.venue_surface = venue_surface;


    }

    public Team(String coach_id, String coach_name, String country, String founded,
                String is_national, String leagues, String name,
                Object sidelined, Object squad, Object statistics,
                String team_id, Object transfers_in, Object transfers_out,
                String venue_address, String venue_capacity, String venue_city,
                String venue_id, String venue_name, String venue_surface) {
        this.coach_id = coach_id;
        this.coach_name = coach_name;
        this.country = country;
        this.founded = founded;
        this.is_national = is_national;
        this.leagues = leagues;
        this.name = name;
        this.sidelined = sidelined;
        this.squad = squad;
        this.statistics = statistics;
        this.team_id = team_id;
        this.transfers_in = transfers_in;
        this.transfers_out = transfers_out;
        this.venue_address = venue_address;
        this.venue_capacity = venue_capacity;
        this.venue_city = venue_city;
        this.venue_id = venue_id;
        this.venue_name = venue_name;
        this.venue_surface = venue_surface;
    }

    public String getCoach_id() {
        return coach_id;
    }

    public String getCoach_name() {
        return coach_name;
    }

    public String getCountry() {
        return country;
    }

    public String getFounded() {
        return founded;
    }

    public String getIs_national() {
        return is_national;
    }

    public String getLeagues() {
        return leagues;
    }

    public String getName() {
        return name;
    }

    public Object getSidelined() {
        return sidelined;
    }

    public Object getSquad() {
        return squad;
    }

    public Object getStatistics() {
        return statistics;
    }

    public String getTeam_id() {
        return team_id;
    }

    public Object getTransfers_in() {
        return transfers_in;
    }

    public Object getTransfers_out() {
        return transfers_out;
    }

    public String getVenue_address() {
        return venue_address;
    }

    public String getVenue_capacity() {
        return venue_capacity;
    }

    public String getVenue_city() {
        return venue_city;
    }

    public String getVenue_id() {
        return venue_id;
    }

    public String getVenue_name() {
        return venue_name;
    }

    public String getVenue_surface() {
        return venue_surface;
    }
}
