package com.wildcardenter.myfab.for_jahan.slidingtabhelper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wildcardenter.myfab.for_jahan.AllSongs;
import com.wildcardenter.myfab.for_jahan.PlayLists;



public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence tabTitles[];
    int numOfTabs;

    public ViewPagerAdapter(FragmentManager fm, CharSequence tabTitles[], int numOfTabs) {
        super(fm);
        this.tabTitles = tabTitles;
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            AllSongs allSongs = new AllSongs();
            return allSongs;
        } else {
            PlayLists playLists = new PlayLists();
            return playLists;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
