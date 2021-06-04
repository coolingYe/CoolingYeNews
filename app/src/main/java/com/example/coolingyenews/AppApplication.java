package com.example.coolingyenews;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.coolingyenews.base.BaseApplication;
import com.example.coolingyenews.base.loadsir.EmptyCallback;
import com.example.coolingyenews.base.loadsir.ErrorCallback;
import com.example.coolingyenews.base.loadsir.LoadingCallback;
import com.example.coolingyenews.base.loadsir.TimeoutCallback;
import com.kingja.loadsir.core.LoadSir;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.converter.GsonDiskConverter;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.utils.Utils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class AppApplication extends BaseApplication
{
    //ARouter调试开关
    private boolean isDebugARouter = true;
    @Override
    public void onCreate() {
        super.onCreate();
        if(isDebugARouter){
            ARouter.openLog();
            //开启调试模式
            //线上版本需要关闭，否则有有安全风险
            ARouter.openDebug();
        }
        // 初始化需要初始化的组件
        ARouter.init(this);
        EasyHttp.init(this);
        if (this.issDebug())
        {
            EasyHttp.getInstance().debug("easyhttp", true);
        }
//        EasyHttp.getInstance()
//                .setBaseUrl("http://baobab.kaiyanapp.com")
//                .setReadTimeOut(15 * 1000)
//                .setWriteTimeOut(15 * 1000)
//                .setConnectTimeout(15 * 1000)
//                .setRetryCount(3)
//                .setCacheDiskConverter(new GsonDiskConverter())
//                .setCacheMode(CacheMode.FIRSTREMOTE);
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new TimeoutCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();

    }


}