package com.mxkj.yuanyintang.mainui.home.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by LiuJie on 2017/1/10.
 */
public class SlidingFragmentViewPager extends FragmentStatePagerAdapter {
    private List<String> strings;
    private List<Fragment> fragments;
    private Fragment mCurrentFragment;

    public SlidingFragmentViewPager(FragmentManager fm, List<String> strings, List<Fragment> fragments, Context context) {
        super(fm);
        if (strings != null) {
            this.strings = strings;
        }
        this.fragments = fragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return strings.get(position);
    }

    @Override
    public int getCount() {
        return strings==null?0:strings.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }


    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }
}
