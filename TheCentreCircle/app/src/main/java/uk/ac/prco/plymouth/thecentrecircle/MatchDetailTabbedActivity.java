package uk.ac.prco.plymouth.thecentrecircle;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
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
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import uk.ac.prco.plymouth.thecentrecircle.fragments.MatchDetailInfoFragment;
import uk.ac.prco.plymouth.thecentrecircle.fragments.MatchDetailStatisticFragment;
import uk.ac.prco.plymouth.thecentrecircle.fragments.MatchLineupsFragment;
import uk.ac.prco.plymouth.thecentrecircle.keys.Constants;

/**
 * Activity which contains three fragments displaying match info, lineups and statistics
 */
public class MatchDetailTabbedActivity extends AppCompatActivity {

    Firebase mainRef = new Firebase(new Constants().getFirebaseUrl());

    private AuthData authData;

    private Bundle bundle = new Bundle();

    private String matchDate;
    private int matchId;

    private Toolbar toolbar;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     * TODO:Analyse resources being used to see if FragmentStatePagerAdapter is more useful
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail_tabbed);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar();
        setTitle("Match details");



        String firebaseRef = getIntent().getStringExtra("matchDate");
        matchDate = getDateFirebase(firebaseRef);
        matchId = getIntent().getIntExtra("matchId", 1);
        bundle.putString("matchDate", getDateFirebase(firebaseRef));
        bundle.putInt("matchId", getIntent().getIntExtra("matchId", 1));
        bundle.putString("matchHomeName", getIntent().getStringExtra("matchHomeName"));
        bundle.putString("matchAwayName", getIntent().getStringExtra("matchAwayName"));
        bundle.putString("matchStatus", getIntent().getStringExtra("matchStatus"));

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        mViewPager = (ViewPager) findViewById(R.id.container_match);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_match);
        if (mViewPager != null) {
            // Set up the ViewPager with the sections adapter.
            mViewPager.setAdapter(mSectionsPagerAdapter);
            if(tabLayout != null) {
                //Set up Tab Layout with the view pager
                tabLayout.setupWithViewPager(mViewPager);
            }
        }

        //See if a user is authorised
        authData = mainRef.getAuth();

    }

    @Override
    public void onStart() {
        super.onStart();

//        if (isFirstUse()) {
//            showFavouriteTutorial();
//        }

        //showFavouriteTutorial();
    }

    private boolean favourite = false;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        //AuthData menuAuth = mainRef.getAuth();

        if (authData != null) {
            Firebase menuRef = new Firebase(new Constants().getFirebaseUrl() + "/users/" +
                    authData.getUid() + "/trackedMatches");

            //Add listener to detect if user has 'favourited' this match
            menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //If user has favourited match, fill the star icon in as selected
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
                        //Remove the match from the user's profile
                        mainRef.child("users").child(authData.getUid()).child("trackedMatches")
                                .child(String.valueOf(matchId)).removeValue();


                        //Alert the user that the 'unfavouriting' has been successful
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
     * Method which breaks a string down to be used with Firebase
     * This works but I am sure it could be achieved more elegantly
     * @param date
     * @return
     */
    private String getDateFirebase(String date) {
        date = Character.toString(date.charAt(0)) + date.charAt(1) + date.charAt(3) + date.charAt(4)
                + date.charAt(6) + date.charAt(7) + date.charAt(8) + date.charAt(9);

        return date;
    }

    private boolean isFirstUse() {
        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if(settings.getBoolean("match_detail_activity_first_time", true)) {
            settings.edit().putBoolean("match_detail_activity_first_time", false).apply();
            return true;
        } else {
            return false;
        }
    }

    public void showFavouriteTutorial() {
        Target showcaseTarget = new Target() {
            @Override
            public Point getPoint() {
                return new ViewTarget(toolbar.findViewById(R.id.action_favourite_match)).getPoint();
            }
        };

        new ShowcaseView.Builder(this)
                .setTarget(showcaseTarget)
                .setStyle(R.style.CustomShowcaseTheme)
                .setContentTitle("Track matches")
                .setContentText("Tracking matches allows you to receive notifications when" +
                        " goals are scored")
                .hideOnTouchOutside()
                .build();

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
            View rootView = inflater.inflate(R.layout.fragment_match_detail_tabbed, container, false);
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

            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case 0:
                    MatchDetailInfoFragment tab1 = new MatchDetailInfoFragment();
                    tab1.setArguments(bundle);
                    return tab1;
                case 1:
                    MatchDetailStatisticFragment tab2 = new MatchDetailStatisticFragment();
                    tab2.setArguments(bundle);
                    return tab2;

                case 2:
                    MatchLineupsFragment tab3 = new MatchLineupsFragment();
                    tab3.setArguments(bundle);
                    return tab3;

            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Match info";
                case 1:
                    return "Stats";
                case 2:
                    return "Lineups";
            }
            return null;
        }
    }
}
