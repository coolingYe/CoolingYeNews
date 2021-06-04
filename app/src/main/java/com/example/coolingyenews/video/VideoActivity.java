package com.example.coolingyenews.video;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.coolingyenews.R;
import com.example.coolingyenews.adapter.BaseFragmentPageAdapter;
import com.example.coolingyenews.base.activity.MvvmBaseActivity;
import com.example.coolingyenews.base.loadsir.LoadingCallback;
import com.example.coolingyenews.base.viewmodel.IMvvmBaseViewModel;
import com.example.coolingyenews.bean.Review;
import com.example.coolingyenews.bean.Video;
import com.example.coolingyenews.common.contract.VideoHeaderBean;
import com.example.coolingyenews.databinding.ActivityVideoPlayerBinding;
import com.example.coolingyenews.databinding.FragmentVideoItemHeaderViewBinding;
import com.example.coolingyenews.news.adapter.UserNewsReviewAdapter;
import com.example.coolingyenews.utils.HttpURL;
import com.example.coolingyenews.utils.NewsClass;
import com.example.coolingyenews.utils.ReplaceStr;
import com.example.coolingyenews.utils.ToastUtil;
import com.example.coolingyenews.utils.UserClass;
import com.example.coolingyenews.utils.utilsTools;
import com.example.coolingyenews.widget.dialog.InputTextMsgDialog;
import com.example.coolingyenews.widget.help.SoftKeyBoardListener;
import com.example.coolingyenews.widget.video.helper.VideoPlayerHelper;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.example.coolingyenews.utils.HttpURL.localhost;

/**

 */
@Route(path ="/video/player")
public class VideoActivity extends MvvmBaseActivity<ActivityVideoPlayerBinding, IMvvmBaseViewModel> {

    @Autowired(name = "videoInfo")
    public Video video;
    private boolean onPauseByBooler = false;
    private VideoHeaderBean liveHeaderBean;
    private InputTextMsgDialog inputTextMsgDialog;
    private SoftKeyBoardListener mKeyBoardListener;
    private VideoReviewAdapter videoReviewAdapter;
    // 旋转帮助类
    private OrientationUtils orientationUtils;
    private List<Review> list = new ArrayList<>();
    private boolean isPlay = true;
    private LoadService recycleLoadService;
    private boolean isPause;
    private boolean strByNewsSave =false;
    private boolean strByNewsLike =false;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        recycleLoadService = LoadSir.getDefault().register(viewDataBinding.reviewItemList, (Callback.OnReloadListener) v -> {});
        //ImmersionBar.with(this).statusBarView(viewDataBinding.topView).init();
        if (video != null)
        {
            initView(video);
            initSendReview();
        }
        getReviewWithHttpURLConnection(new HttpURL().HTTP_VIDEO_REVIEW_URL(String.valueOf(UserClass.getUid()), video.getVid()));
        getVideoByCount("http://"+localhost+":8080/NewsMaven/getVideoByCount?vid="+video.getVid());
        setNewsSaveimage(new HttpURL().HTTP_VIDEO_COLLECT_ADD_OR_DEL_URL("selectVideoByLikesNumber", UserClass.getUid(), video.getVid()), 0);
        setNewsSaveimage(new HttpURL().HTTP_VIDEO_COLLECT_ADD_OR_DEL_URL("selectCollectionVideo", UserClass.getUid(), video.getVid()), 1);
        viewDataBinding.setVideo(video);
        viewDataBinding.ivLike.setImageResource(R.drawable.icon_like);
        viewDataBinding.executePendingBindings();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView(Video video)
    {   viewDataBinding.setBlurred(video.getBlurredUrl());
        viewDataBinding.executePendingBindings();
        initVideoView(video);
        initOnClick();
    }

    private void initVideoView(Video video)
    {
        // 设置返回键
        viewDataBinding.cvVideoView.getBackButton()
                .setOnClickListener(v -> finish());
        // 全屏辅助
        orientationUtils =
                new OrientationUtils(this, viewDataBinding.cvVideoView);
        // 初始化不打开外部的旋转
        orientationUtils.setEnable(false);
        // 初始化配置
        VideoPlayerHelper.optionPlayer(viewDataBinding.cvVideoView,
                video.getPlayerUrl(),
                true,
                video.getTitle());
        //ToastUtil.show(this,headerBean.getPlayerUrl());
        // VideoPlayerHelper.clonePlayState(viewDataBinding.cvVideoView);

        viewDataBinding.cvVideoView.setIsTouchWiget(true);

        viewDataBinding.cvVideoView.setVideoAllCallBack(new GSYSampleCallBack()
        {
            @Override
            public void onPrepared(String url, Object... objects)
            {
                super.onPrepared(url, objects);
                // 开始播放了才能旋转和全屏
                orientationUtils.setEnable(true);
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects)
            {
                super.onQuitFullscreen(url, objects);
                if (orientationUtils != null)
                {
                    orientationUtils.backToProtVideo();
                }
            }
        });
        viewDataBinding.cvVideoView.startPlayLogic();
    }

    @Override
    protected int getLayoutId()
    {
        return R.layout.activity_video_player;
    }



    @Override
    protected int getBindingVariable()
    {
        return 0;
    }

    @Override
    protected void onRetryBtnClick()
    {

    }

    @Override
    public void onBackPressed()
    {
        if (orientationUtils != null)
        {
            orientationUtils.backToProtVideo();
        }
        if (GSYVideoManager.backFromWindowFull(this))
        {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause()
    {
        viewDataBinding.cvVideoView.getCurrentPlayer().onVideoPause();
        super.onPause();
        isPause = true;
        onPauseByBooler=true;
    }

    @Override
    protected void onResume()
    {
        viewDataBinding.cvVideoView.getCurrentPlayer().onVideoResume(false);
        super.onResume();
        isPause = false;
        if(onPauseByBooler){
            videoReviewAdapter.getItem(NewsClass.getPosition()).setSpeech_count(NewsClass.getSpeech_count());
            onPauseByBooler=false;
        }

    }
    private void dismissInputDialog() {
        if (inputTextMsgDialog != null) {
            if (inputTextMsgDialog.isShowing()) inputTextMsgDialog.dismiss();
            inputTextMsgDialog.cancel();
            inputTextMsgDialog = null;
        }
    }
    private void initSendReview(){
        viewDataBinding.mtDiscuss.setOnClickListener(v -> {
            //dismissInputDialog();
            if (inputTextMsgDialog == null) {
                inputTextMsgDialog = new InputTextMsgDialog(VideoActivity.this, R.style.dialog);
                inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msg) {
                        if(UserClass.getUid()==0){
                            ToastUtil.show(VideoActivity.this,"登录，发表你的评论");
                            return;
                        }
                        sendReviewWithHttpURLConnection(new HttpURL().HTTP_VIDEO_REVIEW_SET_URL(UserClass.getUid(),String.valueOf(video.getVid()),msg,"1"),msg);
                    }

                    @Override
                    public void dismiss() {

                    }
                });
            }
            inputTextMsgDialog.show();
        });
        mKeyBoardListener = new SoftKeyBoardListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
            }

            @Override
            public void keyBoardHide(int height) {
                dismissInputDialog();
            }
        });
    }

    private void initOnClick() {
        viewDataBinding.ivSave.setOnClickListener(v -> {
            if(UserClass.getUid()==0){
                ToastUtil.show(VideoActivity.this,"登录，更精彩呦");
                return;
            }
            if(strByNewsSave) {
                delNewsSaveImage(new HttpURL().HTTP_VIDEO_COLLECT_ADD_OR_DEL_URL("delCollectionVideo", UserClass.getUid(), video.getVid()),1);;
            }else addNewsSaveImage(new HttpURL().HTTP_VIDEO_COLLECT_ADD_OR_DEL_URL("insertCollectionVideo", UserClass.getUid(), video.getVid()),1);;
        });
        viewDataBinding.ivLike.setOnClickListener(v -> {
            if(UserClass.getUid()==0){
                ToastUtil.show(VideoActivity.this,"登录，更精彩呦");
                return;
            }
            if(strByNewsLike) {
                delNewsSaveImage(new HttpURL().HTTP_VIDEO_COLLECT_ADD_OR_DEL_URL("delVideoByLikesNumber", UserClass.getUid(), video.getVid()),0);;
            }else addNewsSaveImage(new HttpURL().HTTP_VIDEO_COLLECT_ADD_OR_DEL_URL("insertVideoByLikesNumber", UserClass.getUid(), video.getVid()),0);;
        });
        viewDataBinding.mtShare.setOnClickListener(v -> {
            if(UserClass.getUid()==0){
                ToastUtil.show(VideoActivity.this,"登录，更精彩呦");
                return;
            }
            Intent textIntent = new Intent(Intent.ACTION_SEND);
            textIntent.setType("text/plain");
            textIntent.putExtra(Intent.EXTRA_TEXT, video.getPlayerUrl());
            startActivity(Intent.createChooser(textIntent, "分享"));
        });
    }
    private void addNewsSaveImage(String httpUrl,int i){
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Log.e("error----------------->", e.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s.equals("true")) {
                            switch (i) {
                                case 0:
                                    strByNewsLike = true;
                                    viewDataBinding.ivLike.setImageResource(R.drawable.icon_like_fill);
                                    video.setLikes_count(String.valueOf(Integer.parseInt(video.getLikes_count())+1));
                                    break;
                                case 1:
                                    strByNewsSave = true;
                                    viewDataBinding.setImageInt(R.drawable.icon_news_save_black);
                                    video.setSave_count(String.valueOf(Integer.parseInt(video.getSave_count())+1));
                                    break;
                            }
                        }
                    }
                });
    }
    private void delNewsSaveImage(String httpUrl,int i){
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Log.e("error----------------->", e.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s.equals("true")) {
                            switch (i) {
                                case 0:
                                    strByNewsLike = false;
                                    viewDataBinding.ivLike.setImageResource(R.drawable.icon_like);
                                    video.setLikes_count(String.valueOf(Integer.parseInt(video.getLikes_count())-1));
                                    break;
                                case 1:
                                    strByNewsSave = false;
                                    viewDataBinding.setImageInt(R.drawable.icon_news_save);
                                    video.setSave_count(String.valueOf(Integer.parseInt(video.getSave_count())-1));
                                    break;
                            }
                        }
                    }
                });
    }
    private void sendReviewWithHttpURLConnection(String httpUrl,String msg) {
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if(s.equals("true")){
                            Review review =new Review();
                            review.setReviewUId(UserClass.getUid());
                            review.setUserName(UserClass.getUname());
                            review.setReviewContent(msg);
                            review.setReviewTime(utilsTools.getTime());
                            review.setUserIconSrc(UserClass.getIcon());
                            review.setUser_value("0");
                            review.setLikes_count("0");
                            review.setLikes_img(review.getUser_value().equals("1")?R.drawable.icon_like_fill:R.drawable.icon_like);
                            list.add(0,review);
                            video.setReview_count(String.valueOf(list.size()));
                            if (list.size()!=1) {
                                videoReviewAdapter.notifyItemInserted(0);
                                viewDataBinding.reviewItemList.getLayoutManager().scrollToPosition(0);
                            }
                            else {
                                videoReviewAdapter = new VideoReviewAdapter(list,VideoActivity.this);
                                viewDataBinding.reviewItemList.setAdapter(videoReviewAdapter);
                            }
                        }
                    }
                });
    }

    private void getReviewWithHttpURLConnection(String httpUrl){
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        recycleLoadService.showCallback(LoadingCallback.class);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        recycleLoadService.showSuccess();
                    }

                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        JSONArray array = null;
                        try {
                            array = new JSONArray(s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for(int i=0;i<array.length();i++) {
                            try {
                                JSONObject obj = array.getJSONObject(i);
                                Review review =new Review();
                                review.setReviewId(obj.optInt("rid"));
                                review.setReviewUId(obj.optInt("uid"));
                                review.setUserName(obj.optString("uname"));
                                review.setReviewContent(obj.optString("text"));
                                review.setReviewTime(obj.optString("time"));
                                review.setLikes_count(obj.optString("likes_count"));
                                review.setUser_value(obj.getString("user_value"));
                                review.setUserIconSrc(new ReplaceStr().newStr(obj.optString("icon"), localhost));
                                review.setLikes_img(review.getUser_value().equals("1")?R.drawable.icon_like_fill:R.drawable.icon_like);
                                review.setId(obj.getString("id"));
                                review.setSpeech_count(obj.optString("speech_count"));
                                list.add(review);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                        //activityNewsBinding.tvReviewCount.setText(String.valueOf(list.size()));
                        video.setReview_count(String.valueOf(list.size()));
                        videoReviewAdapter = new VideoReviewAdapter(list,VideoActivity.this);
//                        videoReviewAdapter.setHeaderView(getHeaderView(video));
                        viewDataBinding.reviewItemList.setAdapter(videoReviewAdapter);
                    }
                });
    }

    private void setNewsSaveimage(String httpUrl,int i) {
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s.equals("true")) {
                            switch (i) {
                                case 0:
                                    strByNewsLike = true;
                                    viewDataBinding.ivLike.setImageResource(R.drawable.icon_like_fill);
                                    break;
                                case 1:
                                    strByNewsSave = true;
                                    viewDataBinding.setImageInt(R.drawable.icon_news_save_black);
                                    break;
                            }
                        }else {
                            switch (i) {
                                case 0:
                                    viewDataBinding.ivLike.setImageResource(R.drawable.icon_like);
                                    break;
                                case 1:
                                    viewDataBinding.setImageInt(R.drawable.icon_news_save);
                                    break;
                            }
                        }
                    }
                });
    }
    private void getVideoByCount(String httpUrl){
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Log.e("error----------------->", e.getMessage());
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s!=null) {
                            JSONArray array = null;
                            try {
                                array = new JSONArray(s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            for(int i=0;i<array.length();i++) {
                                try {
                                    JSONObject obj = array.getJSONObject(i);
                                    video.setLikes_count(obj.optString("likes_count"));
                                    video.setSave_count(obj.optString("save_count"));
                                    video.setReview_count(obj.optString("review_count"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    return;
                                }
                            }
                        }
                    }
                });
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        viewDataBinding.cvVideoView.getGSYVideoManager()
                .setListener(viewDataBinding.cvVideoView.getGSYVideoManager()
                        .lastListener());
        viewDataBinding.cvVideoView.getGSYVideoManager().setLastListener(null);
        viewDataBinding.cvVideoView.cancel();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
        {
            orientationUtils.releaseListener();
            try
            {
                // 第三方库中的内存泄漏,只能利用反射来解决
                Field mOrientationEventListener = OrientationUtils.class
                        .getDeclaredField("mOrientationEventListener");
                Field contextField =
                        OrientationUtils.class.getField("mActivity");
                contextField.setAccessible(true);
                contextField.set(orientationUtils, null);
                mOrientationEventListener.setAccessible(true);
                OrientationEventListener listener =
                        (OrientationEventListener)mOrientationEventListener
                                .get(orientationUtils);
                Field mSensorEventListener = OrientationEventListener.class
                        .getDeclaredField("mSensorEventListener");
                mSensorEventListener.setAccessible(true);
                mSensorEventListener.set(listener, null);
                Field mSensorManager = OrientationEventListener.class
                        .getDeclaredField("mSensorManager");
                mSensorManager.setAccessible(true);
                mSensorManager.set(listener, null);

            }
            catch (NoSuchFieldException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            orientationUtils = null;
        }
        VideoPlayerHelper.release();
    }

    @Override
    protected IMvvmBaseViewModel getViewModel() {
        return null;
    }

    private View getHeaderView(Video video)
    {
        if (video != null)
        {
            FragmentVideoItemHeaderViewBinding binding =
                    DataBindingUtil.inflate(getLayoutInflater(),
                            R.layout.fragment_video_item_header_view,
                            viewDataBinding.reviewItemList,
                            false);
            if (binding != null)
            {
                binding.setViewModel(video);
                binding.executePendingBindings();
            }
            return binding.getRoot();
        }
        return null;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        // 如果旋转了就全屏
        if (isPlay && !isPause)
        {
            viewDataBinding.cvVideoView.onConfigurationChanged(this,
                    newConfig,
                    orientationUtils,
                    true,
                    true);
        }
    }
}
