package uk.ac.prco.plymouth.thecentrecircle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import uk.ac.prco.plymouth.thecentrecircle.classes.Event;
import uk.ac.prco.plymouth.thecentrecircle.classes.Match;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;

public class MatchDetailActivity extends AppCompatActivity {

    public static final int EXTRA_MATCHID = 0;
    public static final ArrayList<Match> EXTRA_MATCHES = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);
//        ArrayList<Match> matches = (ArrayList<Match>) getIntent().getSerializableExtra("matches");
//        int position = (Integer) getIntent().getExtras().get("matchId");

        final Match match = (Match) getIntent().getSerializableExtra("match");
        Constants cons = new Constants();
        final Firebase matchRef = new Firebase(cons.getFirebaseUrl() + "/premierleague/matches/" + match.getMatchId());



        TextView homeTeamTextView = (TextView) findViewById(R.id.detail_home_team);
        TextView awayTeamTextView = (TextView) findViewById(R.id.detail_away_team);
        final TextView homeScoreTextView = (TextView) findViewById(R.id.detail_home_score);
        final TextView awayScoreTextView = (TextView) findViewById(R.id.detail_away_score);
        ImageView homeBadge = (ImageView) findViewById(R.id.detail_home_badge);
        ImageView awayBadge = (ImageView) findViewById(R.id.detail_away_badge);
        ListView eventList = (ListView) findViewById(R.id.event_list);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1);

        eventList.setAdapter(adapter);

        System.out.println(match);
        System.out.println(match.getEvents());

        if (match.getEvents() != null) {
            ArrayList<Event> events = match.getEvents();

            for (Event event : events) {
                adapter.add(event.getEventType() + " : " + event.getEventPlayer() + " ('" + event.getEventMinute()
                +  ")");
            }
        }

//



       // homeScore.setText(Integer.toString(position));
        homeTeamTextView.setText(match.getHomeTeam());
        awayTeamTextView.setText(match.getAwayTeam());
        homeScoreTextView.setText(match.getHomeScore());
        awayScoreTextView.setText(match.getAwayScore());
        homeBadge.setImageResource(R.drawable.arsenal);
        awayBadge.setImageResource(R.drawable.manutd);



//        assert getSupportActionBar() != null;
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}
