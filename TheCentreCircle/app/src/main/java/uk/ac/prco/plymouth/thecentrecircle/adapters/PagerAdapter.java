package uk.ac.prco.plymouth.thecentrecircle.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import uk.ac.prco.plymouth.thecentrecircle.CompetitionFixturesFragment;
import uk.ac.prco.plymouth.thecentrecircle.CompetitionLeagueTableFragment;
import uk.ac.prco.plymouth.thecentrecircle.CompetitionStatisticsFragment;

/**
 * Created by charliewaite on 06/03/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private Bundle fragmentBundle = new Bundle();

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    public PagerAdapter(FragmentManager fm, int NumOfTabs, Bundle bundle) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        fragmentBundle = bundle;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CompetitionFixturesFragment tab1 = new CompetitionFixturesFragment();
                tab1.setArguments(fragmentBundle);
                return tab1;
            case 1:
                CompetitionLeagueTableFragment tab2 = new CompetitionLeagueTableFragment();
                tab2.setArguments(fragmentBundle);
                return tab2;
            case 2:
                return new CompetitionStatisticsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
