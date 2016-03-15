package uk.ac.prco.plymouth.thecentrecircle.uk.ac.prco.plymouth.thecentrecircle.classes;

import java.io.Serializable;

/**
 * Created by Charles Waite on 18/02/2016.
 */
public class Match implements Serializable{
    private String homeTeam;
    private String awayTeam;
    private String homeScore;
    private String awayScore;
    private int matchId;
    private int homeBadge;
    private int awayBadge;
    private String matchStatus;

    public String getMatchStatus() {
        return matchStatus;
    }
//A lot more will be added to this


    public Match() {

    }

    public Match(String homeTeam, String awayTeam, String homeScore, String awayScore, int matchId,
                 int homeBadge, int awayBadge, String matchStatus) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.matchId = matchId;
        this.homeBadge = homeBadge;
        this.awayBadge = awayBadge;
        this.matchStatus = matchStatus;
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
}
