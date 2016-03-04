package com.example.charliewaite.testrecycler;

/**
 * Created by charliewaite on 19/02/2016.
 */
public class Match {
    private String homeTeam;
    private String awayTeam;
    private int homeScore;
    private int awayScore;
    private int matchId;

    public Match() {
    }

    public Match(String homeTeam, String awayTeam, int homeScore, int awayScore, int matchId) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.matchId = matchId;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public int getMatchId() {
        return matchId;
    }
}
