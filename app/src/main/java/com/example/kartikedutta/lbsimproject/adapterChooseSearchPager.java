package com.example.kartikedutta.lbsimproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class adapterChooseSearchPager extends FragmentPagerAdapter {

    adapterChooseSearchPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new fragmentFormUser();
        }  else {
            return new fragmentFormCompany();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Jobs";
            case 1:
                return "Tags";
            default:
                return null;
        }
    }
}
