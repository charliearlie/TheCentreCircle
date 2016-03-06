package uk.ac.prco.plymouth.thecentrecircle.adapters;

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

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CompetitionFixturesFragment();
            case 1:
                return new CompetitionLeagueTableFragment();
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
