package com.example.coolingyenews.history;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.coolingyenews.R;
import com.example.coolingyenews.adapter.BaseFragmentPageAdapter;
import com.example.coolingyenews.collect.TextFragment;
import com.example.coolingyenews.collect.VFragment;
import com.example.coolingyenews.utils.UserClass;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/user/history")
public class HistoryActivity extends AppCompatActivity {
    private List<Fragment> fragments = new ArrayList<>();
    private BaseFragmentPageAdapter pageAdapter;
    private TabLayout mTab;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
//        if (savedInstanceState == null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.settings, new SettingsActivity.SettingsFragment())
//                    .commit();
//        }
        initView();
        initFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mTab = findViewById(R.id.tabfornewsspeech_collect);
        mViewPager = findViewById(R.id.view_pagerfornewsspeech_collect);
    }

    private void initFragment() {
        String[] tabname = {"专栏", "视频"};
        fragments.add(TextHistoryFragment.newInstance(String.valueOf(UserClass.getUid())));
        fragments.add(VHistoryFragment.newInstance(String.valueOf(UserClass.getUid())));
        pageAdapter = new BaseFragmentPageAdapter(getSupportFragmentManager(), fragments, tabname);
        mViewPager.setAdapter(pageAdapter);
        mTab.setupWithViewPager(mViewPager);
    }

}