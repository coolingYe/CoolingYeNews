package com.example.coolingyenews.news.activity;


import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.coolingyenews.R;

import com.example.coolingyenews.base.model.BasePagingModel;
import com.example.coolingyenews.utils.HttpURL;

import com.example.coolingyenews.utils.UserClass;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import io.reactivex.disposables.Disposable;

public class NewsModel<T> extends BasePagingModel<T>{
    private Disposable disposable;
    private Disposable disposable1;
    @Override
    protected void load() {
        disposable = EasyHttp.get("http://baobab.kaiyanapp.com/api/v5/index/tab/allRec")
                .cacheKey(getClass().getSimpleName())
                .execute(new SimpleCallBack<String>()
                {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onError(ApiException e)
                    {
                        loadFail(e.getMessage(), isRefresh);
                    }

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(String s)
                    {

                    }
                });
    }
}
