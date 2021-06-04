package com.example.coolingyenews.user.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.coolingyenews.R;
import com.example.coolingyenews.bean.User;
import com.example.coolingyenews.databinding.ActivityUserLoginBinding;
import com.example.coolingyenews.service.UserHandler;
import com.example.coolingyenews.utils.HttpURL;
import com.example.coolingyenews.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/user/register")
public class LoginRegisterActivity extends AppCompatActivity {

    private List<User> list= new ArrayList<>();
    private ActivityUserLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_user_login);
        initView();
    }
    private void initView() {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
