package com.neishenme.what.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.neishenme.what.fragment.HomeInviteFragment;
import com.neishenme.what.fragment.HomePersonFragment;
import com.neishenme.what.fragment.HomeReleaseFragment;

/**
 * Created by Administrator on 2016/5/12.
 * 旧的主界面的viewpager适配器,已弃用
 */
@Deprecated
public class MainOldAdapter extends FragmentStatePagerAdapter {
    private HomeInviteFragment inviteFragment;
    private HomeReleaseFragment homeReleaseFragment;
    private HomePersonFragment personFragment;


    public MainOldAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (inviteFragment == null) {
                    inviteFragment = HomeInviteFragment.newInstance();
                }
                return inviteFragment;
            case 1:
                if (personFragment == null) {
                    personFragment = HomePersonFragment.newInstance();
                }
                return personFragment;
            case 2:
                if (homeReleaseFragment == null) {
                    homeReleaseFragment = HomeReleaseFragment.newInstance();
                }
                return homeReleaseFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
