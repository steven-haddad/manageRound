package com.example.haddad.managemyrounds.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.haddad.managemyrounds.controller.fragment.displayFacesFragment;
import com.example.haddad.managemyrounds.controller.fragment.displayFurnituresFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private static int TAB_COUNT = 2;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return displayFurnituresFragment.newInstance();

            case 1:
                return displayFacesFragment.newInstance();

        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return displayFurnituresFragment.TITLE;

            case 1:
                return displayFacesFragment.TITLE;

        }
        return super.getPageTitle(position);
    }
}