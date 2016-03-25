package uk.ac.prco.plymouth.thecentrecircle.classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Charles Waite on 18/02/2016.
 */
public class Match implements Serializable {

    private int matchId;
    private String competitionId;
    private String homeTeam;
    private String homeTeamId;
    private String awayTeam;
    private String awayTeamId;
    private String homeScore;
    private String awayScore;
    private int homeBadge;
    private int awayBadge;
    private String matchStatus;
    private String halfTimeScore;
    private String fullTimeScore;
    private String extraTimeScore;
    private String matchTime;
    private ArrayList<Event> events;

    public String getMatchStatus() {
        return matchStatus;
    }
//A lot more will be added to this


    public Match() {

    }

    public Match(String homeTeam, String awayTeam, String homeScore, String awayScore, int matchId,
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

    public Match(String homeTeam, String awayTeam, String homeScore, String awayScore, int matchId,
                 int homeBadge, int awayBadge, String matchStatus, ArrayList<Event> events, String competitionId,
                 String homeTeamId, String awayTeamId) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.matchId = matchId;
        this.homeBadge = homeBadge;
        this.awayBadge = awayBadge;
        this.matchStatus = matchStatus;
        this.events = events;
        this.competitionId = competitionId;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
    }

    public Match(int matchId, String competitionId, String homeTeam, String homeTeamId,
                 String awayTeam, String awayTeamId, String homeScore, String awayScore,
                 int homeBadge, int awayBadge, String matchStatus, String halfTimeScore,
                 String fullTimeScore, String extraTimeScore, ArrayList<Event> events) {

        this.matchId = matchId;
        this.competitionId = competitionId;
        this.homeTeam = homeTeam;
        this.homeTeamId = homeTeamId;
        this.awayTeam = awayTeam;
        this.awayTeamId = awayTeamId;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.homeBadge = homeBadge;
        this.awayBadge = awayBadge;
        this.matchStatus = matchStatus;
        this.halfTimeScore = halfTimeScore;
        this.fullTimeScore = fullTimeScore;
        this.extraTimeScore = extraTimeScore;
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

    public String getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
    }

    public String getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(String awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public String getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(String homeTeamId) {
        this.homeTeamId = homeTeamId;
    }
}
