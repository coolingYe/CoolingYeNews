package com.example.coolingyenews.message;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.coolingyenews.R;
import com.example.coolingyenews.adapter.BaseFragmentPageAdapter;
import com.example.coolingyenews.bean.SpeechNews;
import com.example.coolingyenews.collect.TextFragment;
import com.example.coolingyenews.collect.VFragment;
import com.example.coolingyenews.databinding.BaseSpeechHeaderViewBinding;
import com.example.coolingyenews.databinding.FragmentSpeechItemListBinding;
import com.example.coolingyenews.utils.GsonUtils;
import com.example.coolingyenews.utils.ReplaceStr;
import com.example.coolingyenews.utils.UserClass;
import com.google.android.material.tabs.TabLayout;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.coolingyenews.utils.HttpURL.localhost;

@Route(path = "/home/message")
public class MessageActivity extends AppCompatActivity {
    private FragmentSpeechItemListBinding binding;
    private TabLayout mTab;
    private ViewPager mViewPager;
    private BaseFragmentPageAdapter pageAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_speech_item_list);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initData();
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

    private void initData() {
        //getNewsByHistory("http://" + localhost + ":8080/NewsMaven/getReviewWitchLikeByUser?uid=" + UserClass.getUid());
        binding.tvSpeech.setOnClickListener(v -> ARouter.getInstance()
                .build("/News/message/reply")
                .navigation() );
        binding.tvLike.setOnClickListener(v -> ARouter.getInstance()
                .build("/News/message/like")
                .navigation());
    }
    private void initView() {
        mTab = findViewById(R.id.tabfornewsspeech_collect);
        mViewPager = findViewById(R.id.view_pagerfornewsspeech_collect);
    }

    private void initFragment() {
        String[] tabname = {"专栏", "视频"};
        fragments.add(MessageNewsFragment.newInstance(String.valueOf(UserClass.getUid())));
        fragments.add(MessageVideoFragment.newInstance(String.valueOf(UserClass.getUid())));
        pageAdapter = new BaseFragmentPageAdapter(getSupportFragmentManager(), fragments, tabname);
        mViewPager.setAdapter(pageAdapter);
        mTab.setupWithViewPager(mViewPager);
    }

}