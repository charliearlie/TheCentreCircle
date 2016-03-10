package uk.ac.prco.plymouth.thecentrecircle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInRightAnimationAdapter;
import uk.ac.prco.plymouth.thecentrecircle.uk.ac.prco.plymouth.thecentrecircle.classes.Match;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    //URL of Firebase database
    final String FIREBASE_URL = "https://cwprco304.firebaseio.com";

    //Variables needed for the list to be displayed
    private RecyclerView mRecyclerView;
    private ArrayList<Match> matches = new ArrayList<>();
    private ScoreCardAdapter adapter;

    private AuthData aData;

    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set action bar title
        setTitle(R.string.football);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
         Initiate Firebase SDK for real time database connections
         Initiate Facebook SDK to allow the user to log out from the nav drawer
         */
        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(getApplicationContext());

        //Firebase reference to main application URL and what will be 'today's' matches
        Firebase mainRef = new Firebase(FIREBASE_URL);
        Firebase matchRef = new Firebase(FIREBASE_URL + "/premierleague/matches");


        Intent intent = getIntent();
        if (intent.hasExtra("userLogged")) {
            Snackbar.make(findViewById(R.id.content_main), "User logged in", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }


        /**
         * TO COMPLETE:
         * Floating action button for user to view their message inbox or 'liked' matches
         * I haven't decided which one yet
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //DrawerLayout settings for the navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        //Retrieve the navigation drawer and set it's listener for menu item presses
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        /*
           Retrieve the header of the nav drawer to set the text view to the user's display name
         * and the image view to their Facebook profile picture if they used Facebook login
         */
        View header = navigationView.getHeaderView(0);

        if (intent.hasExtra("userName") && intent.hasExtra("profileImage")) {
            TextView navHeaderTextView = (TextView)header.findViewById(R.id.nav_email);
            navHeaderTextView.setText(intent.getStringExtra("userName"));
            CircleImageView profileImage = (CircleImageView)header.findViewById(R.id.nav_profile_image);
            new DownloadImageTask((CircleImageView) header.findViewById(R.id.nav_profile_image))
                    .execute(intent.getStringExtra("profileImage"));
        } else if (intent.hasExtra("userName")) {
            TextView navHeaderTextView = (TextView)header.findViewById(R.id.nav_email);
            navHeaderTextView.setText(intent.getStringExtra("userName"));
        }

        mRecyclerView = (RecyclerView)findViewById(R.id.score_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setVisibility(View.GONE); //Recycler view not visible until ready to display
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new ScoreCardAdapter(matches);

        /*
            Animations for the recycler view
         */
        final SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
        alphaAdapter.setDuration(1000);
        alphaAdapter.setInterpolator(new OvershootInterpolator());
        //mRecyclerView.setAdapter(new SlideInRightAnimationAdapter(alphaAdapter));
        mRecyclerView.setAdapter(alphaAdapter);


        /*
            Listener for Single Value Event to initially fill the recycler view
            ******I might be able to get all this in the child event listener******
         */
        matchRef.addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * Fills the matches ArrayList with all of 'todays' matches from matchRef URL
             * @param dataSnapshot
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    Match match = postSnapShot.getValue(Match.class);
                    matches.add(match);
                }

                //Set listener for recycler view to detect presses on a certain match
                adapter.setListener(new ScoreCardAdapter.Listener() {

                    /**
                     * After the user has pressed a match, the information to retrieve
                     * further match details is passed to MatchDetailActivity
                     * @param position
                     */
                    @Override
                    public void onClick(int position) {
                        Match detailedMatch = matches.get(position);
                        Intent intent = new Intent(MainActivity.this, MatchDetailActivity.class);
                        intent.putExtra("match", detailedMatch);
                        startActivity(intent);
                    }
                });
                //Alert the alphaAdapter that there is new data to be displayed
                alphaAdapter.notifyDataSetChanged();

                //Hide the loading icon and display the list of matches
                findViewById(R.id.progress_bar).setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

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
        matchRef.addChildEventListener(new ChildEventListener() {
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
                int matchId = dataSnapshot.child("matchId").getValue(int.class);
                int index = findMatchById(matchId);
                matches.get(index).setHomeScore(dataSnapshot.child("homeScore").getValue(int.class));
                matches.get(index).setAwayScore(dataSnapshot.child("awayScore").getValue(int.class));
                Intent intent = new Intent(MainActivity.this, MatchNotificationService.class);
                intent.putExtra("match", matches.get(index));
                adapter = new ScoreCardAdapter(matches);
                adapter.setListener(new ScoreCardAdapter.Listener() {

                    /**
                     * Main reason I want to somehow change this. This method appears twice. Not DRY
                     * @param position
                     */
                    @Override
                    public void onClick(int position) {
                        Match detailMatch = matches.get(position);
                        Intent intent = new Intent(MainActivity.this, MatchDetailActivity.class);
                        intent.putExtra("match", detailMatch);
                        intent.putExtra("matchId", position);
                        intent.putExtra("matches", matches);
                        startActivity(intent);
                    }
                });
                //Set new recycler animations. Will try to have a reusable version of this
                SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
                alphaAdapter.setDuration(1000);
                alphaAdapter.setInterpolator(new OvershootInterpolator());
                mRecyclerView.setAdapter(new SlideInRightAnimationAdapter(alphaAdapter));

                startService(intent);
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            public void onCancelled(FirebaseError firebaseError) {
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
                // Handle the camera action
            } else if (id == R.id.nav_competitions) {
                Intent intent = new Intent(MainActivity.this, AllCompetitionsActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_settings) {

            } else if (id == R.id.nav_about) {

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

            } else if (id == R.id.nav_my_bets) {
                Intent intent = new Intent(MainActivity.this, MyBetsActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_football_videos) {
                Intent intent = new Intent(MainActivity.this, MyBetsActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_settings) {

            } else if (id == R.id.nav_about) {

            } else if (id == R.id.nav_logout) {
                Toast.makeText(MainActivity.this, "Log out button pressed", Toast.LENGTH_LONG).show();
                Firebase logRef = new Firebase(FIREBASE_URL);
                LoginManager.getInstance().logOut();
                logRef.unauth();
                finish();
                startActivity(getIntent());
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * When a match is updated, the onDataChange method needs to find the match to update in the array list
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

    private void updateNavHeader(AuthData authData) {
        View header = navigationView.getHeaderView(0);
        if(authData.getProvider().equals("facebook")) {
            TextView navHeaderTextView = (TextView)header.findViewById(R.id.nav_email);
            navHeaderTextView.setText((String) authData.getProviderData().get("displayName"));
            new DownloadImageTask((CircleImageView) header.findViewById(R.id.nav_profile_image))
                    .execute((String) authData.getProviderData().get("profileImageURL"));
        } else {
            TextView navHeaderTextView = (TextView)header.findViewById(R.id.nav_email);
            navHeaderTextView.setText((String) authData.getProviderData().get("email"));
            new DownloadImageTask((CircleImageView) header.findViewById(R.id.nav_profile_image))
                    .execute((String) authData.getProviderData().get("profileImageURL"));
        }
    }

    /**
     * An asynchronous task class to download the user's Facebook profile image in the background
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView bmImage;

        public DownloadImageTask(CircleImageView bmImage){
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls){
            String urldisplay = urls[0];
            Bitmap mFacebookProfile = null;
            try{
                InputStream in = new java.net.URL(urldisplay).openStream();
                mFacebookProfile = BitmapFactory.decodeStream(in);
            } catch (Exception e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mFacebookProfile;
        }

        protected void onPostExecute(Bitmap result){
            bmImage.setImageBitmap(result);
        }
    }

}
