package com.example.coolingyenews.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.coolingyenews.R;
import com.example.coolingyenews.common.router.RouterFragmentPath;
import com.example.coolingyenews.databinding.ActivitySettingsBaseViewBinding;
import com.example.coolingyenews.utils.ToastUtil;
import com.example.coolingyenews.utils.UserClass;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import static com.example.coolingyenews.utils.HttpURL.localhost;

@Route(path = "/user/settings/user/base")
public class BaseViewActivity extends AppCompatActivity {
    @Autowired
    public String str,title;
    private ActivitySettingsBaseViewBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_settings_base_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if(title!=null) {
                actionBar.setTitle(title);
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if(str!=null){
            binding.setViewModel(str);
            binding.executePendingBindings();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main1, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_enter:
                if(binding.edChange.getText().toString().trim().isEmpty()){
                    break;
                }
                switch (title){
                    case "修改昵称":
                        UserClass.setUname(binding.edChange.getText().toString().trim());
                        break;
                    case "修改生日":
                        UserClass.setBirthday(binding.edChange.getText().toString().trim());
                        break;
                    case "修改个性签名":
                        UserClass.setSignature(binding.edChange.getText().toString().trim());
                        break;
                }
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
