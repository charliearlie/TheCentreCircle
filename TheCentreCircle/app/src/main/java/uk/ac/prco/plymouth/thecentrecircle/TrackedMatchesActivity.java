package uk.ac.prco.plymouth.thecentrecircle;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import uk.ac.prco.plymouth.thecentrecircle.adapters.ScoreCardAdapter;
import uk.ac.prco.plymouth.thecentrecircle.classes.Match;
import uk.ac.prco.plymouth.thecentrecircle.fragments.TrackMatchDialogFragment;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;
import uk.ac.prco.plymouth.thecentrecircle.utilities.CCUtilities;

public class TrackedMatchesActivity extends AppCompatActivity {

    //Variables needed for the list to be displayed
    private RecyclerView mRecyclerView;
    private ArrayList<Match> matches = new ArrayList<>();
    private ArrayList<Match> trackedMatches = new ArrayList<>();
    private ArrayList<Integer> trackedIds = new ArrayList<>();
    private ScoreCardAdapter adapter;

    private Constants consts = new Constants(); //Constants such as URLs and API keys

    private AuthData aData; //Authorisation date if user is logged in

    private NavigationView navigationView;

    private Toolbar toolbar;

    private CCUtilities utils = new CCUtilities();

    private final Firebase mainRef = new Firebase(consts.FIREBASE_URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracked_matches);
        setupActionBar();
        setTitle("My matches");
        aData = mainRef.getAuth();

        Firebase matchRef = mainRef.child("/matches/" + utils.getStringDate());
        Firebase userRef = mainRef.child("users/" + aData.getUid() + "/trackedMatches");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    trackedIds.add(Integer.valueOf(postSnapshot.getKey()));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        setUpRecyclerView();
        final Query queryRef = matchRef.orderByChild("matchTime");
        fillRecyclerWithTodaysMatches(queryRef);


    }

    /**
     * Method which sets up the recycler view by setting it's layout manager and adapter
     * Recycler is also set to hidden until all the matches are retrieved
     */
    private void setUpRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.score_recycler_tracked);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setVisibility(View.GONE); //Recycler view not visible until ready to display
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new ScoreCardAdapter(trackedMatches);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * Add a single value event listener to retrieve all matches being played today
     *
     * @param queryRef Firebase query of todays matches ordered by their kick-off time
     */
    private void fillRecyclerWithTodaysMatches(Query queryRef) {
        queryRef.addValueEventListener(new ValueEventListener() {
            /**
             * Fills the matches ArrayList with all of 'todays' matches from matchRef URL
             * @param dataSnapshot
             */
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    Match match = utils.getMatchFromSnapshot(postSnapShot);
                    for (int id : trackedIds) {
                        if (id == match.getMatchId()) {
                            trackedMatches.add(match);
                        }
                    }
                }

                adapter.setOnItemClickListener(new ScoreCardAdapter.ScoreCardClickListener() {

                    @Override
                    public void onItemClick(int position, View v) {
                        Match detailedMatch = trackedMatches.get(position);
                        Intent intent = new Intent(TrackedMatchesActivity.this, MatchDetailTabbedActivity.class);
                        intent.putExtra("matchId", detailedMatch.getMatchId());
                        intent.putExtra("matchDate", detailedMatch.getDate());
                        intent.putExtra("matchHomeName", detailedMatch.getHomeTeam());
                        intent.putExtra("matchAwayName", detailedMatch.getAwayTeam());
                        intent.putExtra("matchStatus", detailedMatch.getMatchStatus());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(int position, View v) {
                        boolean isTracked = false;
                        final Bundle fragmentArguments = new Bundle();
                        final Match trackedMatch = matches.get(position);
                        fragmentArguments.putSerializable("trackedMatch", trackedMatch);
                        final TrackMatchDialogFragment dialog = new TrackMatchDialogFragment();
                        if (aData != null) {
                            Firebase menuRef = mainRef.child("/users/" + aData.getUid() + "/trackedMatches");

                            //Add listener to detect if user has 'favourited' this match
                            menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        //If user has favourited match, fill the star icon in as selected
                                        if (String.valueOf(trackedMatch.getMatchId()).equals(postSnapshot.getKey())) {
                                            fragmentArguments.putBoolean("isTracked", true);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                        }

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.setArguments(fragmentArguments);
                                dialog.show(getSupportFragmentManager(), "dialog");
                            }
                        }, 500);



                    }
                });

                //Alert the alphaAdapter that there is new data to be displayed
                adapter.notifyDataSetChanged();

                mRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
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
}
