package uk.ac.prco.plymouth.thecentrecircle;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class TeamDetailActivity extends AppCompatActivity {

    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        setupActionBar();

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.menu_bottombar, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if(menuItemId == R.id.bottomBarItemOne) {
                    TeamDetailInformationFragment tdpf = new TeamDetailInformationFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.myCoordinator, tdpf);
                            ft.addToBackStack(null);
                            ft.commit();

                } else if(menuItemId == R.id.bottomBarItemTwo) {
                    TeamDetailStatisticsFragment tdsf = new TeamDetailStatisticsFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.myCoordinator, tdsf);
                    ft.addToBackStack(null);
                    ft.commit();

                } else if(menuItemId == R.id.bottomBarItemThree) {
                    TeamDetailPlayersFragment tpf = new TeamDetailPlayersFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.myCoordinator, tpf);
                    ft.addToBackStack(null);
                    ft.commit();

                } else if(menuItemId == R.id.bottomBarItemFour) {
                    TeamDetailFixturesFragment tdff = new TeamDetailFixturesFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.myCoordinator, tdff);
                    ft.addToBackStack(null);
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

        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorPrimary));
        mBottomBar.mapColorForTab(1, "#3F51B5");
        mBottomBar.mapColorForTab(2, "#8BC34A");
        mBottomBar.mapColorForTab(3, "#607D8B");
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
