package uk.ac.prco.plymouth.thecentrecircle.uk.ac.prco.plymouth.thecentrecircle.classes;

import java.io.Serializable;

/**
 * Created by Charles Waite on 18/02/2016.
 */
public class Match implements Serializable{
    private String homeTeam;
    private String awayTeam;
    private int homeScore;
    private int awayScore;
    private int matchId;
    private int homeBadge;
    private int awayBadge;


    //A lot more will be added to this


    public Match() {

    }

    public Match(String homeTeam, String awayTeam, int homeScore, int awayScore, int matchId,
                 int homeBadge, int awayBadge) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.matchId = matchId;
        this.homeBadge = homeBadge;
        this.awayBadge = awayBadge;
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

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public int getAwayScore() {

        return awayScore;
    }

    public int getHomeBadge() {
        return homeBadge;
    }

    public int getAwayBadge() {
        return awayBadge;
    }
}
