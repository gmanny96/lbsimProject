package com.example.kartikedutta.lbsimproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class adapterChooseProfilePager extends FragmentPagerAdapter {

    adapterChooseProfilePager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new fragmentFormUser();
        } else if (position == 1){
            return new fragmentFormNgo();
        } else {
            return new fragmentFormCompany();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "User";
            case 1:
                return "Ngo";
            case 2:
                return "Company";
            default:
                return null;
        }
    }
}
