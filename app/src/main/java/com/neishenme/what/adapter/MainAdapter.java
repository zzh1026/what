package com.neishenme.what.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.neishenme.what.fragment.HomeInviteFragment;
import com.neishenme.what.fragment.HomeTrivelFragment;

/**
 * Created by Administrator on 2016/5/12.
 */
public class MainAdapter extends FragmentStatePagerAdapter {
    private HomeInviteFragment homeInviteFragment;
    private HomeTrivelFragment homeTrivelFragment;


    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (homeInviteFragment == null) {
                    homeInviteFragment = HomeInviteFragment.newInstance();
                }
                return homeInviteFragment;
            case 1:
                if (homeTrivelFragment == null) {
                    homeTrivelFragment = HomeTrivelFragment.newInstance();
                }
                return homeTrivelFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
