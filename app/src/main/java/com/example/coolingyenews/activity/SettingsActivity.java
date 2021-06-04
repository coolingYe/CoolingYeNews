package com.example.coolingyenews.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.coolingyenews.R;
import com.example.coolingyenews.databinding.ActivitySettings1Binding;
import com.example.coolingyenews.utils.HttpURL;
import com.example.coolingyenews.utils.ToastUtil;
import com.example.coolingyenews.utils.UserClass;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import static com.example.coolingyenews.utils.HttpURL.localhost;
import static com.example.coolingyenews.widget.help.AlphaHelper.setPassAlpha;
@Route(path = "/user/tools")
public class SettingsActivity extends AppCompatActivity {
    private ActivitySettings1Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_settings_1);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.settings, new SettingsFragment())
//                    .commit();
//        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Preference button = findPreference("black");
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    EasyHttp.get("http://"+localhost+":8080/UserHandler?m=LoginStatus&uid="+UserClass.getUid())
                            .execute(new SimpleCallBack<String>() {
                                @Override
                                public void onError(ApiException e) {
                                    Log.e("error----------------->", e.getMessage());
                                }

                                @Override
                                public void onSuccess(String str) {
                                    if(str!="false"){
                                        ARouter.getInstance().build("/Main/Main").navigation();
                                    }
                                }
                            });
                    return true;
                }
            });
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(){
        binding.ivUserTools.setOnClickListener(view -> {
            ARouter.getInstance().build("/user/settings/user").navigation();
        });
        binding.ivSecurityPrivacy.setOnClickListener(view -> {
            ARouter.getInstance().build("/user/settings/usersafety").navigation();
        });
        binding.ivBack.setOnClickListener(v -> {
            EasyHttp.get("http://"+localhost+":8080/NewsMaven/blackLogin?uid="+UserClass.getUid())
                    .execute(new SimpleCallBack<String>() {
                        @Override
                        public void onError(ApiException e) {
                            Log.e("error----------------->", e.getMessage());
                        }

                        @Override
                        public void onSuccess(String str) {
                            if(str.equals("true")){
                                new UserClass().clear();
                                ARouter.getInstance().build("/Main/Main").navigation();
                            }
                        }
                    });
        });
    }
}