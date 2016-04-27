package uk.ac.prco.plymouth.thecentrecircle;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uk.ac.prco.plymouth.thecentrecircle.adapters.MatchEventAdapter;
import uk.ac.prco.plymouth.thecentrecircle.classes.Event;
import uk.ac.prco.plymouth.thecentrecircle.classes.Match;
import uk.ac.prco.plymouth.thecentrecircle.classes.Team;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;
import uk.ac.prco.plymouth.thecentrecircle.utilities.CCUtilities;

public class MatchDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private MatchEventAdapter matchEventAdapter;

    ArrayList<String> events = new ArrayList<>();
    private ArrayList<Event> matchEvents = new ArrayList<>();

    private int matchId = 0;

    Firebase mainRef = new Firebase(new Constants().getFirebaseUrl());

    private AuthData authData;

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
        setupActionBar();

        mRecyclerView = (RecyclerView) findViewById(R.id.event_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //Retrieve the date in format DDMMYYYY
        String date = new CCUtilities().getStringDate();

        final Constants cons = new Constants();

        String firebaseRef = getIntent().getStringExtra("matchDate");
        String matchDate = getDateFirebase(firebaseRef);
        int matchId = getIntent().getIntExtra("matchId", 1);
        //Firebase ref = new Firebase(cons.getFirebaseUrl() + "/matches/" + date);
        Firebase ref = new Firebase(cons.getFirebaseUrl() + "/matches/" + matchDate);
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
                final String homeTeamId = dataSnapshot.child("homeTeamId").getValue(String.class);
                final String awayTeamId = dataSnapshot.child("awayTeamId").getValue(String.class);
                String competitionId = dataSnapshot.child("matchCompId").getValue(String.class);
                String venue = dataSnapshot.child("venue").getValue(String.class);
                int retrievedMatchId = dataSnapshot.child("matchId").getValue(int.class);
                getMatchId(retrievedMatchId);
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
                TextView matchVenueTextView = (TextView) findViewById(R.id.match_detail_stadium);
                matchVenueTextView.setText(venue);

                if (competitionId != null) {
                    Firebase badgeRefHome = new Firebase(cons.getFirebaseUrl() + "/badges/" + homeTeamId);
                    Firebase badgeRefAway = new Firebase(cons.getFirebaseUrl() + "/badges/" + awayTeamId);

                    badgeRefHome.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String imageUrl = dataSnapshot.child("badgeUrl").getValue(String.class);
                            ImageView im = (ImageView) findViewById(R.id.detail_home_badge);
                            Picasso.with(getApplicationContext()).load(imageUrl).into(im);

                            if (im != null) {
                                im.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Firebase teamRef = new Firebase(cons.FIREBASE_URL + "/teams/" + homeTeamId);
                                        teamRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Team team = getTeamFromSnapshot(dataSnapshot);
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("team", team);
                                                Intent intent = new Intent(MatchDetailActivity.this, TeamDetailActivity.class);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {

                                            }
                                        });
                                    }
                                });
                            }


                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                    badgeRefAway.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String imageUrl = dataSnapshot.child("badgeUrl").getValue(String.class);
                            ImageView im2 = (ImageView) findViewById(R.id.detail_away_badge);
                            Picasso.with(getApplicationContext()).load(imageUrl).into(im2);

                            if (im2 != null) {
                                im2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Firebase teamRef = new Firebase(cons.FIREBASE_URL + "/teams/" + awayTeamId);
                                        teamRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Team team = getTeamFromSnapshot(dataSnapshot);
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("team", team);
                                                Intent intent = new Intent(MatchDetailActivity.this, TeamDetailActivity.class);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {

                                            }
                                        });
                                    }
                                });
                            }
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
                events.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String eventAssist = postSnapshot.child("eventAssist").getValue(String.class);
                    String eventAssistId = postSnapshot.child("eventAssistId").getValue(String.class);
                    String eventExtraMin = postSnapshot.child("eventExtraMin").getValue(String.class);
                    String eventId = postSnapshot.child("eventId").getValue(String.class);
                    String eventMinute = postSnapshot.child("eventMinute").getValue(String.class);
                    String eventPlayer = postSnapshot.child("eventPlayer").getValue(String.class);
                    String eventPlayerId = postSnapshot.child("eventplayerId").getValue(String.class);
                    String eventTeam = postSnapshot.child("eventTeam").getValue(String.class);
                    String eventType = postSnapshot.child("eventType").getValue(String.class);
                    Event event = new Event(eventAssist, eventAssistId, eventExtraMin, eventId,
                            eventMinute, eventPlayer, eventPlayerId, eventTeam, eventType);
                    matchEvents.add(event);
                    events.add(event.getEventType() + " : " + event.getEventPlayer() + " ('" + event.getEventMinute()
                            + ")");
                }

                matchEventAdapter = new MatchEventAdapter(matchEvents);
                mRecyclerView.setAdapter(matchEventAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        authData = mainRef.getAuth();
//        ImageView homeBadge = (ImageView) findViewById(R.id.detail_home_badge);
//        ImageView awayBadge = (ImageView) findViewById(R.id.detail_away_badge);
//
//        Picasso.with(getApplicationContext()).load(R.drawable.ic_badge_outline).into(homeBadge);
//        Picasso.with(getApplicationContext()).load(R.drawable.ic_badge_outline).into(awayBadge);


    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    boolean favourite = false;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        AuthData menuAuth = mainRef.getAuth();

        if (menuAuth != null) {
            Firebase menuRef = new Firebase(new Constants().getFirebaseUrl() + "/users/" +
                    menuAuth.getUid() + "/trackedMatches");

            menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (String.valueOf(matchId).equals(postSnapshot.getKey())) {
                            menu.findItem(R.id.action_favourite_match).setIcon(R.drawable.ic_star_white_24dp);
                            favourite = true;
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_match_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favourite_match:
                if (favourite) {
                    item.setIcon(R.drawable.ic_star_border_white_24dp);
                    favourite = false;
                    if (authData != null) {
                        //Add the map to the user's data stored within Firebase
                        mainRef.child("users").child(authData.getUid()).child("trackedMatches")
                                .child(String.valueOf(matchId)).removeValue();

                        //Alert the user that the favouriting has been successful
                        Snackbar.make(getCurrentFocus(), "You have stopped tracking this match",
                                Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                } else {
                    item.setIcon(R.drawable.ic_star_white_24dp);
                    favourite = true;
                    if (authData != null) {
                        //Add the map to the user's data stored within Firebase
                        mainRef.child("users").child(authData.getUid()).child("trackedMatches")
                                .child(String.valueOf(matchId)).setValue(true);

                        //Alert the user that the favouriting has been successful
                        Snackbar.make(getCurrentFocus(), "You have started tracking this match", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "As we like your tracked matches " +
                                        "to track across multiple devices, you need to log in to track a match",
                                Toast.LENGTH_LONG).show();
                    }

                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getDateFirebase(String date) {
        String testDate = Character.toString(date.charAt(0)) + date.charAt(1) + date.charAt(3) + date.charAt(4)
                + date.charAt(6) + date.charAt(7) + date.charAt(8) + date.charAt(9);

        return testDate;
    }

    private void getMatchId(int id) {
        matchId = id;
    }

    private Team getTeamFromSnapshot(DataSnapshot dataSnapshot) {
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


}
