package uk.ac.prco.plymouth.thecentrecircle.classes;

import java.util.ArrayList;

/**
 * Created by Charles Waite on 16/03/2016.
 */
public class MatchTest {
    private String homeTeam;
    private String awayTeam;
    private String homeScore;
    private String awayScore;
    private int matchId;
    private int homeBadge;
    private int awayBadge;
    private String matchStatus;
    private ArrayList<Event> events;

    public String getMatchStatus() {
        return matchStatus;
    }
//A lot more will be added to this


    public MatchTest() {

    }

    public MatchTest(String homeTeam, String awayTeam, String homeScore, String awayScore, int matchId,
                 int homeBadge, int awayBadge, String matchStatus, ArrayList<Event> events) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.matchId = matchId;
        this.homeBadge = homeBadge;
        this.awayBadge = awayBadge;
        this.matchStatus = matchStatus;
        this.events = events;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public int getMatchId() {
        return matchId;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public String getHomeScore() {
        return homeScore;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setHomeScore(String homeScore) {
        this.homeScore = homeScore;
    }

    public void setAwayScore(String awayScore) {
        this.awayScore = awayScore;
    }

    public String getAwayScore() {

        return awayScore;
    }

    public int getHomeBadge() {
        return homeBadge;
    }

    public int getAwayBadge() {
        return awayBadge;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
