package uk.ac.prco.plymouth.thecentrecircle;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
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

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.ac.prco.plymouth.thecentrecircle.classes.Competition;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;

/**
 * Tabbed activity to view different information related to a competition
 * @author Charles Waite
 **/
public class CompetitionTabbedActivity extends AppCompatActivity
        implements CompetitionTeamListFragment.CompetitionTeamListListener {
    private Constants constants = new Constants();
    private Bundle bundle = new Bundle();
    private String compId;
    private String compName;
    private ArrayList<String> favouriteComps = new ArrayList<>();
    private Boolean favourited = false;
    private final Firebase ref = new Firebase(constants.getFirebaseUrl());
    private final AuthData authData = ref.getAuth();
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
        setContentView(R.layout.activity_competition_tabbed);


        Firebase.setAndroidContext(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Competition competition = (Competition) getIntent().getSerializableExtra("competition");
        compId = String.valueOf(competition.getId());
        compName = competition.getName();

        bundle.putSerializable("competition", competition);

        setupActionBar();
        setTitle(competition.getName());

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container_comp);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //Detect whether the user is logged in
        if (authData != null) {
            //Look at the user's favourite competitions in Firebase with a single value event listener
            ref.child("/users/" + authData.getUid() + "/favcompetitions")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                favouriteComps.add(childSnapshot.getKey());
                                System.out.println(favouriteComps);
                            }

                            //Search through the users favourite competitions to detect if this competition is found
                            //TODO: Do this searching within the Firebase query
                            for (int i = 0; i < favouriteComps.size(); i++) {
                                if (favouriteComps.get(i).equals(competition.getId())) {
                                    favourited = true;
                                }
                            }

                            //Create the fab if favourited = false or hide it if not
                            createFAB(favourited);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
        } else {
            //If no auth data, we do not want the FAB to be displayed so send the parameter true
            createFAB(true);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_competition_tabbed, menu);
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
        } else if (id == R.id.action_home) {
            Intent intent = new Intent(CompetitionTabbedActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClicked(long id) {
        //Intent intent = new Intent(CompetitionTabbedActivity.this, TeamDetailActivity.class);
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
            View rootView = inflater.inflate(R.layout.fragment_competition_tabbed, container, false);
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
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                case 0:
                    CompetitionFixturesFragment tab1 = new CompetitionFixturesFragment();
                    tab1.setArguments(bundle);
                    return tab1;
                case 1:
                    CompetitionLeagueTableFragment tab2 = new CompetitionLeagueTableFragment();
                    tab2.setArguments(bundle);
                    return tab2;
                case 2:
                    return new CompetitionStatisticsFragment();
                case 3:
                    CompetitionTeamListFragment tab3 = new CompetitionTeamListFragment();
                    tab3.setArguments(bundle);
                    return tab3;
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //Names for each tab
            switch (position) {
                case 0:
                    return "Fixtures";
                case 1:
                    return "Table";
                case 2:
                    return "Statistics";
                case 3:
                    return "Teams";
            }
            return null;
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     **/
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void createFAB(Boolean fav) {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        if (!fav) {
            //Set icon on FAB to a heart
            fab.setImageResource(R.drawable.zc_favorite_white_36dp);

            //Set the ripple to the accent colour of the app: blue
            fab.setRippleColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

            //Set the on click listener to respond to a press
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Tell Google Analytics that someone liked a competition
                    Tracker mTracker = ((TheCentreCircle) getApplication()).getTracker();
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Liked competition")
                            .setAction("User 'favourited' a competition")
                            .setLabel(compName)
                            .build());

                    //Map for the String object key and Boolean value
                    Map<String, Boolean> map = new HashMap<>();
                    map.put(compId, true);

                    //Add the map to the user's data stored within Firebase
                    ref.child("users").child(authData.getUid()).child("favcompetitions").child(compId).setValue(true);

                    //Alert the user that the favouriting has been successful
                    Snackbar.make(view, "You have favourited this competition", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fab.hide();
                }
            });
        } else {

            fab.setVisibility(View.INVISIBLE);
        }
    }
}
