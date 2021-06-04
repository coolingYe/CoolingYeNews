package com.example.coolingyenews.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 应用模块: home
 * <p>
 * 类描述: 首页 - viewpager 适配器
 * <p>
 *
 * @author darryrzhoong
 * @since 2020-02-09
 */
public class BaseFragmentPageAdapter extends FragmentPagerAdapter
{

    List<Fragment> mFragments;
    String[] mTitles;

    public BaseFragmentPageAdapter(FragmentManager fm, List<Fragment> fragments, String[] text) {
        super(fm);
        mFragments = fragments;
        mTitles=text;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a Fragment1 (defined as a static inner class below).
        return mFragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return  mTitles[position];
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
