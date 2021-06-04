package com.example.coolingyenews.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Gallery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.coolingyenews.R;
import com.example.coolingyenews.base.loadsir.LoadingCallback;
import com.example.coolingyenews.bean.News;
import com.example.coolingyenews.bean.Weather;
import com.example.coolingyenews.databinding.ActivityWeatherBinding;
import com.example.coolingyenews.news.adapter.NewsItemListAdapter;
import com.example.coolingyenews.utils.GsonUtils;
import com.example.coolingyenews.utils.LocationUtil;
import com.example.coolingyenews.utils.ToastUtil;
import com.example.coolingyenews.utils.WeatherUtil;
import com.example.coolingyenews.utils.utilsTools;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.coolingyenews.utils.HttpURL.localhost;

@Route(path = "/user/weather")
public class WeatherActivity extends AppCompatActivity {
    private ActivityWeatherBinding binding;
//    private LoadService viewLoadService;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather);
//        viewLoadService = LoadSir.getDefault().register(binding.getRoot(), (Callback.OnReloadListener) v -> {
//        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //LocationUtil.getCNBylocation(this);
        getWeather("http://api.help.bj.cn/apis/weather2d/?id=" + LocationUtil.cityName);
        binding.tvSearchSearch.setOnClickListener(v -> {
            String searchKey = binding.etSearch.getTrimmedString();
            if (!TextUtils.isEmpty(searchKey)) {
                binding.etSearch.clearFocus();
                binding.drawerLayout.closeDrawer(Gravity.START);
                getWeather("http://api.help.bj.cn/apis/weather2d/?id=" + searchKey);
            }
        });
        binding.tvCloseSearch.setOnClickListener(v -> binding.drawerLayout.closeDrawer(Gravity.START));
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

    private void getWeather(String httpUrl) {
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s != null) {
                            try {
                                JSONObject array = new JSONObject(s);
                                Weather weather= GsonUtils.fromLocalJson(array.toString(), Weather.class);
                                binding.setViewModel(weather);
                                binding.setStr("https:"+weather.getWeatherimg());
                                binding.executePendingBindings();
                                binding.dateAndtime.setText(utilsTools.getTime2());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}
