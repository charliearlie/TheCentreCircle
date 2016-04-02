package uk.ac.prco.plymouth.thecentrecircle.classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Charles Waite on 16/03/2016.
 */
public class MatchTest implements Serializable {
    private int matchId;
    private String matchCompId;
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
    private Object events;



    public String getMatchStatus() {
        return matchStatus;
    }
//A lot more will be added to this


    public MatchTest() {

    }

    public MatchTest(int matchId, String competitionId, String homeTeam, String homeTeamId,
                     String awayTeam, String awayTeamId, String homeScore, String awayScore,
                     int homeBadge, int awayBadge, String matchStatus, String halfTimeScore,
                     String fullTimeScore, String extraTimeScore, String matchTime, Object events) {
        this.matchId = matchId;
        this.matchCompId = competitionId;
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
        this.matchTime = matchTime;
        this.events = events;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public String getMatchCompId() {
        return matchCompId;
    }

    public void setCompetitionId(String matchCompId) {
        this.matchCompId = matchCompId;
    }

    public String getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(String homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public String getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(String awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public void setHomeBadge(int homeBadge) {
        this.homeBadge = homeBadge;
    }

    public void setAwayBadge(int awayBadge) {
        this.awayBadge = awayBadge;
    }

    public String getHalfTimeScore() {
        return halfTimeScore;
    }

    public void setHalfTimeScore(String halfTimeScore) {
        this.halfTimeScore = halfTimeScore;
    }

    public String getFullTimeScore() {
        return fullTimeScore;
    }

    public void setFullTimeScore(String fullTimeScore) {
        this.fullTimeScore = fullTimeScore;
    }

    public String getExtraTimeScore() {
        return extraTimeScore;
    }

    public void setExtraTimeScore(String extraTimeScore) {
        this.extraTimeScore = extraTimeScore;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public Object getEvents() {
        return events;
    }

    public void setEvents(Object events) {
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
}
