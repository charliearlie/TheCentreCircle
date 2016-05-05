package uk.ac.prco.plymouth.thecentrecircle;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import uk.ac.prco.plymouth.thecentrecircle.classes.Team;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;

public class TeamDetailTabbedActivity extends AppCompatActivity {

    private AuthData authData;
    private Firebase mainRef;
    private Team team;
    private Bundle bundle;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail_tabbed);
        mainRef = new Firebase(new Constants().FIREBASE_URL);

        bundle = getIntent().getExtras();
        team = (Team) bundle.getSerializable("team");
        setTitle(team.getName());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container_team);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_team);
        tabLayout.setupWithViewPager(mViewPager);

        authData = mainRef.getAuth();

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

                    Toast.makeText(TeamDetailTabbedActivity.this,
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_team_detail_tabbed, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    TeamDetailInformationFragment tdif = new TeamDetailInformationFragment();
                    tdif.setArguments(bundle);
                    return tdif;
                case 1:
                    TeamDetailStatisticsFragment tdsf = new TeamDetailStatisticsFragment();
                    tdsf.setArguments(bundle);
                    return tdsf;
                case 2:
                    TeamDetailPlayersFragment tpf = new TeamDetailPlayersFragment();
                    tpf.setArguments(bundle);
                    return tpf;
                case 3:
                    TeamDetailFixturesFragment tdff = new TeamDetailFixturesFragment();
                    return tdff;
            }
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Info";
                case 1:
                    return "Stats";
                case 2:
                    return "Players";
                case 3:
                    return "Fixtures";
            }
            return null;
        }
    }
}
