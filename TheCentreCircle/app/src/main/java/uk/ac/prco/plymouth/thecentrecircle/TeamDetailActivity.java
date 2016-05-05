package uk.ac.prco.plymouth.thecentrecircle;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import uk.ac.prco.plymouth.thecentrecircle.classes.Team;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;

public class TeamDetailActivity extends AppCompatActivity {

    private BottomBar mBottomBar;
    private String teamId;
    private AuthData authData;
    private Firebase mainRef;
    private Team team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        setupActionBar();
        mainRef = new Firebase(new Constants().FIREBASE_URL);


        final Bundle bundle = getIntent().getExtras();
        team = (Team) bundle.getSerializable("team");
        setTitle(team.getName());

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.menu_bottombar, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemOne) {
                    TeamDetailInformationFragment tdpf = new TeamDetailInformationFragment();
                    tdpf.setArguments(bundle);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.myCoordinator, tdpf);
                    //ft.addToBackStack(null);
                    ft.commit();

                } else if (menuItemId == R.id.bottomBarItemTwo) {
                    TeamDetailStatisticsFragment tdsf = new TeamDetailStatisticsFragment();
                    tdsf.setArguments(bundle);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.myCoordinator, tdsf);
                    //ft.addToBackStack(null);
                    ft.commit();

                } else if (menuItemId == R.id.bottomBarItemThree) {
                    TeamDetailPlayersFragment tpf = new TeamDetailPlayersFragment();
                    tpf.setArguments(bundle);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.myCoordinator, tpf);
                    //ft.addToBackStack(null);
                    ft.commit();

                } else if (menuItemId == R.id.bottomBarItemFour) {
                    TeamDetailFixturesFragment tdff = new TeamDetailFixturesFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.myCoordinator, tdff);
                    //ft.addToBackStack(null);
                    ft.commit();
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemOne) {
                    // The user reselected item number one, scroll your content to top.
                }
            }
        });

        //Set the colour for the bottom bar tabs
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorPrimary));
        mBottomBar.mapColorForTab(1, "#3F51B5");
        mBottomBar.mapColorForTab(2, "#8BC34A");
        mBottomBar.mapColorForTab(3, "#607D8B");

        authData = mainRef.getAuth();
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

    private boolean favourite = false;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        if (authData != null) {
            Firebase menuRef = mainRef.child("users/" + authData.getUid() + "favouriteTeam");

            //Add listener to detect if user has 'favourited' this match
            menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //If user has favourited match, fill the star icon in as selected
                        if (String.valueOf(team.getTeam_id()).equals(postSnapshot.getKey())) {
                            menu.findItem(R.id.action_like_team).setIcon(R.drawable.ic_heart_white_24dp);
                            favourite = true;
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
        getMenuInflater().inflate(R.menu.menu_team_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_like_team) {
            if (favourite) {
                item.setIcon(R.drawable.ic_heart_outline_white_24dp);
                favourite = false;
                if (authData != null) {
                    //Remove the match from the user's profile
                    mainRef.child("users").child(authData.getUid()).child("favouriteTeam")
                            .removeValue();

                    //Alert the user that the 'unfavouriting' has been successful
//                    Snackbar.make(getCurrentFocus(), "You have stopped tracking this match",
//                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            } else {
                item.setIcon(R.drawable.ic_heart_white_24dp);
                favourite = true;
                if (authData != null) {
                    //Add the map to the user's data stored within Firebase
                    mainRef.child("users").child(authData.getUid()).child("favouriteTeam")
                            .setValue(team.getTeam_id());

                    Toast.makeText(TeamDetailActivity.this,
                            "Your favourite team is now " + team.getName(), Toast.LENGTH_LONG).show();

//                    //Alert the user that the favouriting has been successful
//                    Snackbar.make(getCurrentFocus(), "You have started tracking this match", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

    public void openPlayerFragment(Fragment targetFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.myCoordinator, targetFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

}
