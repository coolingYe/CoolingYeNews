package com.example.coolingyenews.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.coolingyenews.R;
import com.example.coolingyenews.databinding.ActivitySettingsUserBinding;
import com.example.coolingyenews.databinding.ActivitySettingsUserSafetyBinding;
import com.example.coolingyenews.utils.ToastUtil;
import com.example.coolingyenews.utils.UserClass;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import static com.example.coolingyenews.utils.HttpURL.localhost;

@Route(path = "/user/settings/usersafety")
public class UserSafetyActivity extends AppCompatActivity {
    private ActivitySettingsUserSafetyBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings_user_safety);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initOnClick();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initOnClick() {
        binding.mbEnter.setOnClickListener(v -> {
            String oldPwd = binding.etOldPwd.getText().toString().trim();
            String newPwd = binding.etNewPwd.getText().toString().trim();
            String newPwdAgain = binding.etNewPwdAgain.getText().toString().trim();
            if (oldPwd != UserClass.getPwd()) {
                ToastUtil.show(this, "密码不正确");
            } else if (newPwd != newPwdAgain) {
                ToastUtil.show(this, "再次输出密码不一致");
            } else if (oldPwd == UserClass.getPwd() && newPwd == newPwdAgain) {
                EasyHttp.get("http://" + localhost + ":8080/NewsMaven/changeUserPwd?uid=" + UserClass.getUid() + "&pwd=" + newPwd)
                        .execute(new SimpleCallBack<String>() {
                            @Override
                            public void onStart() {
                                super.onStart();
                            }

                            @Override
                            public void onCompleted() {
                                super.onCompleted();
                            }

                            @Override
                            public void onError(ApiException e) {
                            }

                            @Override
                            public void onSuccess(String s) {
                                if (s.equals("true")) {
                                    ToastUtil.show(UserSafetyActivity.this, "密码修改成功");
                                }
                            }
                        });
            }
        });
    }
}
