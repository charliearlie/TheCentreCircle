package uk.ac.prco.plymouth.thecentrecircle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import uk.ac.prco.plymouth.thecentrecircle.adapters.MatchEventAdapter;
import uk.ac.prco.plymouth.thecentrecircle.classes.Event;
import uk.ac.prco.plymouth.thecentrecircle.classes.Match;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;

public class MatchDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MatchEventAdapter matchEventAdapter;
    ArrayList<String> events = new ArrayList<>();
    private ArrayList<Event> matchEvents = new ArrayList<>();
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }

    public static final int EXTRA_MATCHID = 0;
    public static final ArrayList<Match> EXTRA_MATCHES = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);

        mRecyclerView = (RecyclerView) findViewById(R.id.event_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);




        final Constants cons = new Constants();

        final String firebaseRef = getIntent().getStringExtra("firebaseurl");
        int matchId = getIntent().getIntExtra("matchId", 1);
        Firebase ref = new Firebase(cons.getFirebaseUrl() + "/matches/26032016");
        System.out.println(firebaseRef);
        Firebase matchRef = ref.child(String.valueOf(matchId));
        System.out.println("CUNT CHOPS: " + matchRef);

        //final Firebase matchRef = new Firebase(cons.getFirebaseUrl() + "/premierleague/matches/" + match.getMatchId());

        matchRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //System.out.println(dataSnapshot);
                if (dataSnapshot.hasChild("homeTeam")) {
                    System.out.println("Home team was in this update " + new Date());
                }
                String homeTeam = dataSnapshot.child("homeTeam").getValue(String.class);
                String awayTeam = dataSnapshot.child("awayTeam").getValue(String.class);
                String homeScore = dataSnapshot.child("homeScore").getValue(String.class);
                String awayScore = dataSnapshot.child("awayScore").getValue(String.class);
                String matchStatus = dataSnapshot.child("matchStatus").getValue(String.class);
                String homeTeamId = dataSnapshot.child("homeTeamId").getValue(String.class);
                String awayTeamId = dataSnapshot.child("awayTeamId").getValue(String.class);
                String competitionId = dataSnapshot.child("matchCompId").getValue(String.class);

                TextView homeTeamTextView = (TextView) findViewById(R.id.detail_home_team);
                homeTeamTextView.setText(homeTeam);
                TextView awayTeamTextView = (TextView) findViewById(R.id.detail_away_team);
                awayTeamTextView.setText(awayTeam);
                final TextView homeScoreTextView = (TextView) findViewById(R.id.detail_home_score);
                homeScoreTextView.setText(homeScore);
                final TextView awayScoreTextView = (TextView) findViewById(R.id.detail_away_score);
                awayScoreTextView.setText(awayScore);
                TextView matchStatusTextView = (TextView) findViewById(R.id.detail_match_status);
                matchStatusTextView.setText(matchStatus);

                if(competitionId.equals("1204") || competitionId.equals("1265")) {
                    Firebase badgeRefHome = new Firebase(cons.getFirebaseUrl() + "/badges/" + homeTeamId);
                    Firebase badgeRefAway = new Firebase(cons.getFirebaseUrl() + "/badges/" + awayTeamId);

                    badgeRefHome.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String imageUrl = dataSnapshot.child("badgeUrl").getValue(String.class);
                            ImageView im = (ImageView)findViewById(R.id.detail_home_badge);
                            Picasso.with(getApplicationContext()).load(imageUrl).into(im);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                    badgeRefAway.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String imageUrl = dataSnapshot.child("badgeUrl").getValue(String.class);
                            ImageView im2 = (ImageView)findViewById(R.id.detail_away_badge);
                            Picasso.with(getApplicationContext()).load(imageUrl).into(im2);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });


                } else {
                    ImageView im = (ImageView) findViewById(R.id.detail_home_badge);
                    Picasso.with(getApplicationContext()).load(R.drawable.arsenal).into(im);

                    ImageView im2 = (ImageView) findViewById(R.id.detail_away_badge);
                    Picasso.with(getApplicationContext()).load(R.drawable.arsenal).into(im2);
                }


                System.out.println(homeTeam + new Date());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase eventRef = matchRef.child("events");

        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Event event = postSnapshot.getValue(Event.class);
                    matchEvents.add(event);
                    events.add(event.getEventType() + " : " + event.getEventPlayer() + " ('" + event.getEventMinute()
                            +  ")");
                }

                matchEventAdapter = new MatchEventAdapter(matchEvents);
                mRecyclerView.setAdapter(matchEventAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        ImageView homeBadge = (ImageView) findViewById(R.id.detail_home_badge);
        ImageView awayBadge = (ImageView) findViewById(R.id.detail_away_badge);


//        if (match.getEvents() != null) {
//            ArrayList<Event> events = match.getEvents();
//
//            for (Event event : events) {
//                adapter.add(event.getEventType() + " : " + event.getEventPlayer() + " ('" + event.getEventMinute()
//                +  ")");
//            }
//        }

//



       // homeScore.setText(Integer.toString(position));
//        homeTeamTextView.setText(match.getHomeTeam());
//        awayTeamTextView.setText(match.getAwayTeam());
//        homeScoreTextView.setText(match.getHomeScore());
//        awayScoreTextView.setText(match.getAwayScore());
        Picasso.with(getApplicationContext()).load(R.drawable.arsenal).into(homeBadge);
        Picasso.with(getApplicationContext()).load(R.drawable.arsenal).into(awayBadge);



//        assert getSupportActionBar() != null;
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }




}
