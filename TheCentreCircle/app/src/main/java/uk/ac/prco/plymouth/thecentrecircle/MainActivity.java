package uk.ac.prco.plymouth.thecentrecircle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;


import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.prco.plymouth.thecentrecircle.classes.Match;
import uk.ac.prco.plymouth.thecentrecircle.classes.Team;
import uk.ac.prco.plymouth.thecentrecircle.fragments.DatePickerFragment;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;
import uk.ac.prco.plymouth.thecentrecircle.utilities.CCUtilities;
import uk.ac.prco.plymouth.thecentrecircle.adapters.ScoreCardAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int num = 0;
    //Variables needed for the list to be displayed
    private RecyclerView mRecyclerView;
    private ArrayList<Match> matches = new ArrayList<>();
    private ArrayList<String> favouriteMatches = new ArrayList<>();
    private ScoreCardAdapter adapter;

    private Constants cons = new Constants(); //Constants such as URLs and API keys

    private AuthData aData; //Authorisation date if user is logged in

    private NavigationView navigationView;

    private Toolbar toolbar;

    private Team favouriteTeam;

    CCUtilities utils = new CCUtilities();

    //Firebase reference to main application URL and 'today's' matches
    final Firebase mainRef = new Firebase(cons.getFirebaseUrl());

    String date = utils.getStringDate(); //Get date in string format to get todays matches


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TheCentreCircle)getApplication()).startTracking();


        setTitle("The Centre Circle"); //Set action bar title
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Firebase todaysMatchesRef = new Firebase(cons.getFirebaseUrl() + "/matches/" + date);
        //final Firebase todaysMatchesRef = new Firebase(cons.getFirebaseUrl() + "/matches/28042016");

        //DrawerLayout settings for the navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Retrieve the navigation drawer and set it's listener for menu item presses
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*
           Retrieve the header of the nav drawer to set the text view to the user's display name
         * and the image view to their Facebook profile picture if they used Facebook login
         */
        View header = navigationView.getHeaderView(0);

        setUpRecyclerView();
        Query queryRef = todaysMatchesRef.orderByChild("matchTime");
        fillRecyclerWithTodaysMatches(queryRef);

        /*
            AuthStateListener to detect whether a user has now been authorised
         */
        mainRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                aData = authData;
                //If the user is logged in display the more detailed menu including 'logout' item
                if (authData != null) {
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.activity_main_drawer_auth);
                    Toast.makeText(MainActivity.this, authData.getUid(), Toast.LENGTH_LONG).show();
                    updateNavHeader(aData);


                }
                //If the user is not logged in, display the default menu containing a 'login' item
                else {
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.activity_main_drawer);
                }
            }
        });

        /*
            Child Event listener to detect changes to data stored in the Firebase DB
            Not used to initially fill recycler view because that would mean
            creating the recycler 'number of matches' times
         */
        queryRef.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }


            /**
             * Detect changes in matches, such as scores and major events
             * and as of now we refresh the adapter, I hope to just refresh the individual item
             * @param dataSnapshot new data given to us from Firebase
             * @param s String of the item changed
             */
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                final DataSnapshot ds = dataSnapshot;
                int matchId = dataSnapshot.child("matchId").getValue(int.class);
                int index = findMatchById(matchId);
                String homeScore = dataSnapshot.child("homeScore").getValue(String.class);
                String awayScore = dataSnapshot.child("awayScore").getValue(String.class);
                if (!matches.get(index).getHomeScore().equals(homeScore)
                        || !matches.get(index).getAwayScore().equals(awayScore)) {

                    matches.get(index).setHomeScore(dataSnapshot.child("homeScore").getValue(String.class));
                    matches.get(index).setAwayScore(dataSnapshot.child("awayScore").getValue(String.class));
                    matches.get(index).setMatchStatus(dataSnapshot.child("matchStatus").getValue(String.class));

                    adapter.notifyItemChanged(index);

                    if (!homeScore.equals("0") || !awayScore.equals("0")) {
                        if (!homeScore.equals("?") || !awayScore.equals("?")) {
                            for (String favouriteMatchId : favouriteMatches) {
                                if (favouriteMatchId.equals(String.valueOf(matches.get(index).getMatchId()))) {
                                    addOneToNum();
                                    Intent intent = new Intent(MainActivity.this, MatchNotificationService.class);
                                    intent.putExtra("match", matches.get(index));
                                    startService(intent);
                                }
                            }

                        }

                    }
                } else {
                    matches.get(index).setMatchStatus(dataSnapshot.child("matchStatus").getValue(String.class));
                    adapter.notifyItemChanged(index);
                }

            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            public void onCancelled(FirebaseError firebaseError) {
                sendFirebaseErrorToAnalytics(firebaseError);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        AuthData authData = mainRef.getAuth();


        if (authData != null) {
            Firebase userRef = mainRef.child("users/" + authData.getUid() + "/trackedMatches");

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    favouriteMatches.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        favouriteMatches.add(postSnapshot.getKey());
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    sendFirebaseErrorToAnalytics(firebaseError);
                }
            });
        }


    }

    /**
     * Method which detects if this is the first time the user has loaded the application
     * @return boolean determining whether it is their first time
     */
    private boolean isFirstUse() {
        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if(settings.getBoolean("my_first_time_using_app", true)) {
            settings.edit().putBoolean("my_first_time_using_app", false).apply();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method which sets up the recycler view by setting it's layout manager and adapter
     * Recycler is also set to hidden until all the matches are retrieved
     */
    private void setUpRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.score_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setVisibility(View.GONE); //Recycler view not visible until ready to display
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new ScoreCardAdapter(matches);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * Add a single value event listener to retrieve all matches being played today
     * @param queryRef Firebase query of todays matches ordered by their kick-off time
     */
    private void fillRecyclerWithTodaysMatches(Query queryRef) {
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * Fills the matches ArrayList with all of 'todays' matches from matchRef URL
             * @param dataSnapshot
             */
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    Match match = utils.getMatchFromSnapshot(postSnapShot);
                    matches.add(match);
                }
                adapter.setListener(new ScoreCardAdapter.Listener() {

                    /**
                     * After the user has pressed a match, the information to retrieve
                     * further match details is passed to MatchDetailActivity
                     * @param position
                     */
                    @Override
                    public void onClick(int position) {
                        Match detailedMatch = matches.get(position);
                        Intent intent = new Intent(MainActivity.this, MatchDetailTabbedActivity.class);
                        intent.putExtra("matchId", detailedMatch.getMatchId());
                        intent.putExtra("matchDate", detailedMatch.getDate());
                        intent.putExtra("matchHomeName", detailedMatch.getHomeTeam());
                        intent.putExtra("matchAwayName", detailedMatch.getAwayTeam());
                        intent.putExtra("matchStatus", detailedMatch.getMatchStatus());
                        startActivity(intent);
                    }
                });

                //Alert the alphaAdapter that there is new data to be displayed
                adapter.notifyDataSetChanged();

                //Hide the loading icon and display the list of matches
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                mRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                sendFirebaseErrorToAnalytics(firebaseError);
            }
        });
    }

    /**
     * If the nav drawer is open, the back button will make it slide in
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        if (isFirstUse()) {
            Target showcaseTarget = new Target() {
                @Override
                public Point getPoint() {
                    return new ViewTarget(toolbar.findViewById(R.id.action_date_picker)).getPoint();
                }
            };

            ShowcaseView showcaseView = new ShowcaseView.Builder(this)
                    .setTarget(showcaseTarget)
                    .setStyle(R.style.CustomShowcaseTheme)
                    .setContentTitle("View fixtures by date")
                    .setContentText("Select the calendar to navigate to a different date")
                    .hideOnTouchOutside()
                    .build();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_date_picker) {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        } else if (id == R.id.action_about) {
            Match detailedMatch = matches.get(1);
            Intent intent = new Intent(MainActivity.this, MatchDetailTabbedActivity.class);
            intent.putExtra("matchId", detailedMatch.getMatchId());
            intent.putExtra("matchDate", detailedMatch.getDate());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (aData == null) {
            if (id == R.id.nav_home) {

            } else if (id == R.id.nav_competitions) {
                Intent intent = new Intent(MainActivity.this, AllCompetitionsActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_football_videos) {
                Intent intent = new Intent(MainActivity.this, VideoListActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_settings) {

            } else if (id == R.id.nav_about) {
                Toast.makeText(MainActivity.this, "num: " + num, Toast.LENGTH_LONG).show();

            } else if (id == R.id.nav_login) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        } else {
            if (id == R.id.nav_home) {
                // Handle the camera action
            } else if (id == R.id.nav_competitions) {
                Intent intent = new Intent(MainActivity.this, AllCompetitionsActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_fav_competitions) {
                Intent intent = new Intent(MainActivity.this, FavouriteCompetitionsActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_my_team) {
                getUsersFavouriteTeam();
                Bundle bundle = new Bundle();
                bundle.putSerializable("team", favouriteTeam);
                Intent intent = new Intent(MainActivity.this, TeamDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            } else if (id == R.id.nav_my_bets) {
                Intent intent = new Intent(MainActivity.this, VideoListActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_football_videos) {
                Intent intent = new Intent(MainActivity.this, VideoListActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_settings) {
                //TODO: Remove this as it's just so i can get to the team detail quickly.
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_about) {


            } else if (id == R.id.nav_logout) {
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.activity_main_drawer);
                Toast.makeText(MainActivity.this, "Log out button pressed", Toast.LENGTH_LONG).show();
                Firebase logRef = new Firebase(cons.getFirebaseUrl());
                aData = logRef.getAuth();
                LoginManager.getInstance().logOut();
                logRef.unauth();
                aData = logRef.getAuth();
                updateNavHeader(aData);
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    /**
     * When a match is updated, the onDataChange method needs to find the match to update in the array list
     *
     * @param matchId The match ID accompanying the match in the DB
     * @return Index of match which needs updating
     */
    private int findMatchById(int matchId) {
        int index = 0;
        for (int i = 0; i < matches.size(); i++) {
            if (matches.get(i).getMatchId() == matchId) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Update the navigation drawer header with the user's profile data
     *
     * @param authData The user's authentication data
     */
    private void updateNavHeader(AuthData authData) {
        View header = navigationView.getHeaderView(0);
        TextView navHeaderTextView = (TextView) header.findViewById(R.id.nav_email);
        CircleImageView profilePicture = (CircleImageView) header.findViewById(R.id.nav_profile_image);

        if (authData != null) {
            if (authData.getProvider().equals("facebook")) {
                navHeaderTextView.setText((String) authData.getProviderData().get("displayName"));
                new DownloadImageTask(profilePicture)
                        .execute((String) authData.getProviderData().get("profileImageURL"));
            } else {
                navHeaderTextView = (TextView) header.findViewById(R.id.nav_email);
                navHeaderTextView.setText((String) authData.getProviderData().get("email"));
                new DownloadImageTask(profilePicture)
                        .execute((String) authData.getProviderData().get("profileImageURL"));
            }
        } else {
            navHeaderTextView.setText(getResources().getText(R.string.app_name));
            Picasso.with(getApplicationContext()).load(R.mipmap.ic_centrecircle).into(profilePicture);
        }

    }

    private void sendFirebaseErrorToAnalytics(FirebaseError firebaseError) {
        Tracker mTracker = ((TheCentreCircle) getApplication()).getTracker();
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Firebase error")
                .setAction(firebaseError.getDetails())
                .setLabel(firebaseError.getMessage())
                .build());
    }



    /**
     * An asynchronous task class to download the user's Facebook profile image in the background
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView bmImage;

        public DownloadImageTask(CircleImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mFacebookProfile = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mFacebookProfile = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mFacebookProfile;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    /**
     * ---This is currently deprecated---
     * Return the date as a string
     * @return the string date properly formatted for Firebase
     */
    private String getStringDate() {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String[] dateArray = date.split("-");
        date = dateArray[0] + dateArray[1] + dateArray[2];

        return date;
    }

    /**
     * Replace todays fixtures with fixtures from the date the user selected
     *
     * @param targetFragment the fixture fragment of the date selected
     */
    public void openFixtureFragment(Fragment targetFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, targetFragment);
        ft.commit();
        mRecyclerView.setVisibility(mRecyclerView.GONE);
    }


    /**
     * Allows the fixtures by date fragment to set the title on the action bar
     * @param title
     */
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    /**
     * Test method to keep track of notifications because we've had issues with
     * duplicate notifications
     */
    public void addOneToNum() {
        num++;
    }


    /**
     * A method to retrieve the users favourite team and set it to a global variable
     */
    public void getUsersFavouriteTeam() {

        //TODO: Stop accessing mainRef.getAuth() in multiple places
        AuthData authData = mainRef.getAuth();

        //firebase reference to find the ID of the user's favourite team
        final Firebase favouriteTeamRef = mainRef.child("users/" + authData.getUid() + "/favouriteTeam");

        //Add the event listener
        favouriteTeamRef.addListenerForSingleValueEvent(new ValueEventListener() {
            String teamId;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Store the id of the user's favourite team in teamId
                teamId = dataSnapshot.getValue(String.class);

                //Create the query so the team returned has the same ID as the user's favourite
                Firebase usersTeamRef = mainRef.child("teams");
                Query teamQuery = usersTeamRef.orderByKey().equalTo(teamId);

                teamQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot teamSnapshot) {
                        //Retrieve the team from the snapshot
                        //This currently has to be achieved like this because of the way TeamDetail is
                        //set up
                        favouriteTeam = new CCUtilities().getTeamFromSnapshot(teamSnapshot.child(teamId));

                        //Remove the event listener
                        favouriteTeamRef.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
