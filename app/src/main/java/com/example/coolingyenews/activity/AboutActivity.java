package com.example.coolingyenews.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.coolingyenews.R;
import com.example.coolingyenews.databinding.ActivityAboutBinding;
@Route(path = "/user/about")
public class AboutActivity extends AppCompatActivity {
    private ActivityAboutBinding activityAboutBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAboutBinding =  DataBindingUtil.setContentView(this, R.layout.activity_about);
        initToolBar(R.string.item_about_author);
        initTextView();
    }

    private void initToolBar(int newsname) {
        activityAboutBinding.toolbar.setTitle(newsname);//这样设置的话，自带的标题就不会显示
        //设置自定义的标题（居中）
//        setSupportActionBar(materialToolbar);//
        activityAboutBinding.toolbar.setNavigationOnClickListener(v -> finish());
    }
    private void initTextView(){
        activityAboutBinding.tvAuthorContent.setText("作者：CoolingYe\n"+"邮箱：wenkuan.ye@foxmail.com\n");
    }
}