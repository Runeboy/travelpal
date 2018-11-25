package rune.paging;

import android.app.Fragment;
import android.app.FragmentManager;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] mFragments;

    public FragmentPagerAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);

        mFragments = fragments;
    }

//    public FragmentPagerAdapter(FragmentManager fm, Fragment... fragments) {
//
////        if (fragments == null)
////            throw new IllegalArgumentException("Fragment list cannot be null");
//
//    }

    @Override
    public Fragment getItem(int i) {
        return mFragments[i];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}