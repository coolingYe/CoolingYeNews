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
import com.example.coolingyenews.activity.BaseViewActivity;
import com.example.coolingyenews.bean.User;
import com.example.coolingyenews.databinding.ActivityUserLoginBinding;
import com.example.coolingyenews.service.UserHandler;
import com.example.coolingyenews.utils.HttpURL;
import com.example.coolingyenews.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
@Route(path = "/user/login")
public class LoginActivity extends AppCompatActivity {

    private AnimatorSet animatorSet;
    private List<User> list= new ArrayList<>();
    private ActivityUserLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_user_login);
        initView();
    }
    private void initView() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(binding.loginBgImage1, "alpha", 1.0f, 0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(binding.loginBgImage2, "alpha", 0f, 1.0f);
        ObjectAnimator animatorScale1 = ObjectAnimator.ofFloat(binding.loginBgImage1, "scaleX", 1.0f, 1.3f);
        ObjectAnimator animatorScale2 = ObjectAnimator.ofFloat(binding.loginBgImage1, "scaleY", 1.0f, 1.3f);
        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.setDuration(5000);
        animatorSet1.play(animator1).with(animator2).with(animatorScale1).with(animatorScale2);
        animatorSet1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 放大的View复位
                binding.loginBgImage1.setScaleX(1.0f);
                binding.loginBgImage1.setScaleY(1.0f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(binding.loginBgImage2, "alpha", 1.0f, 0f);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(binding.loginBgImage1, "alpha", 0f, 1.0f);
        ObjectAnimator animatorScale3 = ObjectAnimator.ofFloat(binding.loginBgImage2, "scaleX", 1.0f, 1.3f);
        ObjectAnimator animatorScale4 = ObjectAnimator.ofFloat(binding.loginBgImage2, "scaleY", 1.0f, 1.3f);
        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.setDuration(5000);
        animatorSet2.play(animator3).with(animator4).with(animatorScale3).with(animatorScale4);
        animatorSet2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 放大的View复位
                binding.loginBgImage2.setScaleX(1.0f);
                binding.loginBgImage2.setScaleY(1.0f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animatorSet1, animatorSet2);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 循环播放
                animation.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void LoginButton_Click(View V){
        String account =binding.editAccount.getText().toString();
        String pwd=binding.editPwd.getText().toString().trim();
        if(account.equals("")&&!pwd.equals("")){
             ToastUtil.show(LoginActivity.this, "账号不能为空");
        }
        else if(!account.equals("")&&pwd.equals("")){
             ToastUtil.show(LoginActivity.this, "密码不能为空");
        }
        else if(account.equals("")&&pwd.equals("")){
             ToastUtil.show(LoginActivity.this, "请输入账号密码");
        } else{
            new UserHandler().loginSendRequestWithHttpURLConnection(LoginActivity.this,list,new HttpURL().HTTP_LOGIN_URL(account,pwd));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        animatorSet.cancel();
    }
}
