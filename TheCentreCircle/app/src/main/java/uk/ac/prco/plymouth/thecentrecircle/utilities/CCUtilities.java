package uk.ac.prco.plymouth.thecentrecircle.utilities;

import com.firebase.client.DataSnapshot;

import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.ac.prco.plymouth.thecentrecircle.classes.Match;
import uk.ac.prco.plymouth.thecentrecircle.classes.Team;

/**
 * Created by charliewaite on 22/03/2016.
 */
public class CCUtilities {
    /**
     * @param rd
     * @return
     * @throws IOException
     */
    public String readAllJson(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();

        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public String getStringDate() {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String[] dateArray = date.split("-");
        date = dateArray[0] + dateArray[1] + dateArray[2];

        return date;
    }

    /**
     * Takes the data snapshot retrieved by Firebase and extracts the teams details from it
     *
     * @param dataSnapshot returned from Firebase query
     * @return the team retrieved from the snapshot
     */
    public Team getTeamFromSnapshot(DataSnapshot dataSnapshot) {
        String coach_id = dataSnapshot.child("coach_id").getValue(String.class);
        String coach_name = dataSnapshot.child("coach_name").getValue(String.class);
        String country = dataSnapshot.child("country").getValue(String.class);
        String founded = dataSnapshot.child("founded").getValue(String.class);
        String is_national = dataSnapshot.child("is_national").getValue(String.class);
        String leagues = dataSnapshot.child("leagues").getValue(String.class);
        String name = dataSnapshot.child("name").getValue(String.class);
        String team_id = dataSnapshot.child("team_id").getValue(String.class);
        String venue_address = dataSnapshot.child("venue_address").getValue(String.class);
        String venue_capacity = dataSnapshot.child("venue_capacity").getValue(String.class);
        String venue_city = dataSnapshot.child("venue_city").getValue(String.class);
        String venue_id = dataSnapshot.child("venue_id").getValue(String.class);
        String venue_name = dataSnapshot.child("venue_name").getValue(String.class);
        String venue_surface = dataSnapshot.child("venue_surface").getValue(String.class);
        Team team = new Team(coach_id, coach_name, country, founded, is_national,
                leagues, name, team_id, venue_address, venue_capacity, venue_city,
                venue_id, venue_name, venue_surface);

        return team;
    }

    /**
     * Method which extracts the data from the Friebase snapshot and returns it as a Match object
     * @param dataSnapshot Snapshot containing all the data retrieved
     * @return The Match object created from the snapshot data
     */
    public Match getMatchFromSnapshot(DataSnapshot dataSnapshot) {
        String homeTeam = dataSnapshot.child("homeTeam").getValue(String.class);
        String awayTeam = dataSnapshot.child("awayTeam").getValue(String.class);
        String homeScore = dataSnapshot.child("homeScore").getValue(String.class);
        String awayScore = dataSnapshot.child("awayScore").getValue(String.class);
        int matchId = dataSnapshot.child("matchId").getValue(int.class);
        String matchStatus = dataSnapshot.child("matchStatus").getValue(String.class);
        String competitionId = dataSnapshot.child("matchCompId").getValue(String.class);
        String homeTeamId = dataSnapshot.child("homeTeamId").getValue(String.class);
        String awayTeamId = dataSnapshot.child("awayTeamId").getValue(String.class);
        String date = dataSnapshot.child("date").getValue(String.class);

        return new Match(homeTeam, awayTeam, homeScore, awayScore,
                matchId, 0, 0, matchStatus, null, competitionId, homeTeamId, awayTeamId, date);
    }
}
