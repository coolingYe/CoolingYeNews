package com.example.coolingyenews.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.coolingyenews.R;
import com.example.coolingyenews.activity.SpeechActivity;
import com.example.coolingyenews.activity.SpeechAdapter;
import com.example.coolingyenews.base.loadsir.LoadingCallback;
import com.example.coolingyenews.bean.News;
import com.example.coolingyenews.bean.Review;
import com.example.coolingyenews.databinding.ActivityNewsBinding;
import com.example.coolingyenews.news.adapter.UserNewsReviewAdapter;
import com.example.coolingyenews.utils.HttpURL;
import com.example.coolingyenews.utils.JsoupHtml;
import com.example.coolingyenews.utils.NewsClass;
import com.example.coolingyenews.utils.ReplaceStr;
import com.example.coolingyenews.utils.ToastUtil;
import com.example.coolingyenews.utils.UserClass;
import com.example.coolingyenews.utils.utilsTools;
import com.example.coolingyenews.widget.dialog.InputTextMsgDialog;
import com.example.coolingyenews.widget.help.MImageGetter;
import com.example.coolingyenews.widget.help.SoftKeyBoardListener;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;
import static com.example.coolingyenews.utils.HttpURL.localhost;

@Route(path = "/News/News")
public class NewsActivity extends AppCompatActivity {
    @Autowired
    public News news;

    private ActivityNewsBinding activityNewsBinding;
    private InputTextMsgDialog inputTextMsgDialog;
    private SoftKeyBoardListener mKeyBoardListener;

    private UserNewsReviewAdapter userNewsReviewAdapter;
    private List<Review> list = new ArrayList<>();
    private boolean strByNewsSave = false;
    private boolean strByNewsLike = false;
    private boolean onPauseByBooler = false;
    private LoadService textLoadService, recycleLoadService, textviewLoadService;
    public final static int REQUEST_CODE = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        activityNewsBinding = DataBindingUtil.setContentView(this, R.layout.activity_news);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textLoadService = LoadSir.getDefault().register(activityNewsBinding.NewsText, (Callback.OnReloadListener) v -> {
        });
        recycleLoadService = LoadSir.getDefault().register(activityNewsBinding.reviewItemList, (Callback.OnReloadListener) v -> {
        });
        textviewLoadService = LoadSir.getDefault().register(activityNewsBinding.tvLikeCount, (Callback.OnReloadListener) v -> {
        });
        if (news != null) {
            initToolBar(news.getTitle());
            initView();
            initOnClick();
            initSendReview();
        }
    }

    /**
     * 失败重试,加载事件
     */
    protected void onRetryBtnClick() {

    }

    private void initView() {
        DownloadToDB();
        activityNewsBinding.reviewItemList.setLayoutManager(new LinearLayoutManager(this));
        getReviewWithHttpURLConnection(new HttpURL().HTTP_NEWS_REVIEW_URL(String.valueOf(UserClass.getUid()), news.getNid()));
        getNewsByCount("http://"+localhost+":8080/NewsMaven/getNewsByCount?nid=" + news.getNid());
        setNewsSaveimage(new HttpURL().HTTP_COLLECT_ADD_OR_DEL_URL("selectNewsByLikesNumber", UserClass.getUid(), news.getNid()), 0);
        setNewsSaveimage(new HttpURL().HTTP_COLLECT_ADD_OR_DEL_URL("selectCollectionNews", UserClass.getUid(), news.getNid()), 1);
        activityNewsBinding.setNews(news);
        activityNewsBinding.ivLike.setImageResource(R.drawable.icon_like);
        activityNewsBinding.executePendingBindings();
    }

    private void initOnClick() {
        activityNewsBinding.ivSave.setOnClickListener(v -> {
            if (UserClass.getUid() == 0) {
                ToastUtil.show(NewsActivity.this, "登录，更精彩呦");
                return;
            }
            if (strByNewsSave) {
                delNewsSaveImage(new HttpURL().HTTP_COLLECT_ADD_OR_DEL_URL("delCollectionNews", UserClass.getUid(), news.getNid()), 1);
                ;
            } else
                addNewsSaveImage(new HttpURL().HTTP_COLLECT_ADD_OR_DEL_URL("insertCollectionNews", UserClass.getUid(), news.getNid()), 1);
            ;
        });
        activityNewsBinding.ivLike.setOnClickListener(v -> {
            if (UserClass.getUid() == 0) {
                ToastUtil.show(NewsActivity.this, "登录，更精彩呦");
                return;
            }
            if (strByNewsLike) {
                delNewsSaveImage(new HttpURL().HTTP_COLLECT_ADD_OR_DEL_URL("delNewsByLikesNumber", UserClass.getUid(), news.getNid()), 0);
                ;
            } else
                addNewsSaveImage(new HttpURL().HTTP_COLLECT_ADD_OR_DEL_URL("insertNewsByLikesNumber", UserClass.getUid(), news.getNid()), 0);
            ;
        });
        activityNewsBinding.mtShare.setOnClickListener(v -> {
            if (UserClass.getUid() == 0) {
                ToastUtil.show(NewsActivity.this, "登录，更精彩呦");
                return;
            }
            Intent textIntent = new Intent(Intent.ACTION_SEND);
            textIntent.setType("text/plain");
            textIntent.putExtra(Intent.EXTRA_TEXT, news.getUrl());
            startActivity(Intent.createChooser(textIntent, "分享"));
        });
    }

    private void initSendReview() {
        activityNewsBinding.mtDiscuss.setOnClickListener(v -> {
            //dismissInputDialog();
            if (inputTextMsgDialog == null) {
                inputTextMsgDialog = new InputTextMsgDialog(NewsActivity.this, R.style.dialog);
                inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msg) {
                        if (UserClass.getUid() == 0) {
                            ToastUtil.show(NewsActivity.this, "登录，发表你的评论");
                            return;
                        }
                        sendReviewWithHttpURLConnection(new HttpURL().HTTP_NEWS_REVIEW_SET_URL(UserClass.getUid(), String.valueOf(news.getNid()), msg, "1"), msg);
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

    private void dismissInputDialog() {
        if (inputTextMsgDialog != null) {
            if (inputTextMsgDialog.isShowing()) inputTextMsgDialog.dismiss();
            inputTextMsgDialog.cancel();
            inputTextMsgDialog = null;
        }
    }

    private void initToolBar(String newsname) {
        activityNewsBinding.toolbar.setTitle(newsname);//这样设置的话，自带的标题就不会显示
        //设置自定义的标题（居中）
//        TextView toolBarTitle = mToolbar.findViewById(R.id.toolbarTitle);//
//        toolBarTitle.setText("");
        setSupportActionBar(activityNewsBinding.toolbar);//
        //设置导航图标要在setSupportActionBar方法之后
        //mToolbar.setNavigationIcon(null);//设置为空的话，就会不显示左侧的图标
        //对NavigationIcon添加点击=
        activityNewsBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mKeyBoardListener != null) {
            mKeyBoardListener.setOnSoftKeyBoardChangeListener(null);
            mKeyBoardListener = null;
        }
    }

    private void sendReviewWithHttpURLConnection(String httpUrl, String msg) {
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s.equals("true")) {
                            Review review = new Review();
                            review.setReviewUId(UserClass.getUid());
                            review.setUserName(UserClass.getUname());
                            review.setReviewContent(msg);
                            review.setReviewTime(utilsTools.getTime());
                            review.setUserIconSrc(UserClass.getIcon());
                            review.setUser_value("0");
                            review.setLikes_count("0");
                            review.setLevel("1");
                            review.setSpeech_count("0");
                            review.setLikes_img(review.getUser_value().equals("1") ? R.drawable.icon_like_fill : R.drawable.icon_like);
                            list.add(0, review);
                            //activityNewsBinding.tvReviewCount.setText(String.valueOf(list.size()));
                            news.setReview_count(String.valueOf(list.size()));
                            activityNewsBinding.setValue("评论 " + list.size());
                            if (list.size() != 1) {
                                userNewsReviewAdapter.notifyItemInserted(0);
                                activityNewsBinding.reviewItemList.getLayoutManager().scrollToPosition(0);
                            } else {
                                userNewsReviewAdapter = new UserNewsReviewAdapter(list, NewsActivity.this);
                                activityNewsBinding.reviewItemList.setAdapter(userNewsReviewAdapter);
                            }
                        }
                    }
                });
    }
    private void getReviewWithHttpURLConnection(String httpUrl) {
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
                        for (int i = 0; i < array.length(); i++) {
                            try {
                                JSONObject obj = array.getJSONObject(i);
                                //getSecondaryReview("http://192.168.0.148:8080/NewsMaven/getSecondaryReview?rid="+obj.optInt("rid")+"&nid="+obj.getString("id"));
                                Review review = new Review();
                                review.setReviewId(obj.optInt("rid"));
                                review.setReviewUId(obj.optInt("uid"));
                                review.setUserName(obj.optString("uname"));
                                review.setReviewContent(obj.optString("text"));
                                review.setReviewTime(obj.optString("time"));
                                review.setLikes_count(obj.optString("likes_count"));
                                review.setUser_value(obj.getString("user_value"));
                                review.setUserIconSrc(new ReplaceStr().newStr(obj.optString("icon"), localhost));
                                review.setLikes_img(review.getUser_value().equals("1") ? R.drawable.icon_like_fill : R.drawable.icon_like);
                                review.setNid(obj.getString("nid"));
                                review.setVid(obj.getString("vid"));
                                review.setId(!(review.getNid()=="")?obj.getString("nid"):obj.getString("vid"));
                                review.setSpeech_count(obj.optString("speech_count"));
                                list.add(review);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                        activityNewsBinding.setValue("评论 " + list.size());
                        //activityNewsBinding.tvReviewCount.setText(String.valueOf(list.size()));
                        news.setReview_count(String.valueOf(list.size()));
                        userNewsReviewAdapter = new UserNewsReviewAdapter(list, NewsActivity.this);
                        activityNewsBinding.reviewItemList.setAdapter(userNewsReviewAdapter);
                    }
                });
    }

    private void setNewsSaveimage(String httpUrl, int i) {
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
                                    activityNewsBinding.ivLike.setImageResource(R.drawable.icon_like_fill);
                                    break;
                                case 1:
                                    strByNewsSave = true;
                                    activityNewsBinding.setImageInt(R.drawable.icon_news_save_black);
                                    break;
                            }
                        } else {
                            switch (i) {
                                case 0:
                                    activityNewsBinding.ivLike.setImageResource(R.drawable.icon_like);
                                    break;
                                case 1:
                                    activityNewsBinding.setImageInt(R.drawable.icon_news_save);
                                    break;
                            }
                        }
                    }
                });
    }

    private void addNewsSaveImage(String httpUrl, int i) {
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
                                    activityNewsBinding.ivLike.setImageResource(R.drawable.icon_like_fill);
                                    news.setLikes_count(String.valueOf(Integer.parseInt(news.getLikes_count()) + 1));
                                    break;
                                case 1:
                                    strByNewsSave = true;
                                    activityNewsBinding.setImageInt(R.drawable.icon_news_save_black);
                                    news.setSave_count(String.valueOf(Integer.parseInt(news.getSave_count()) + 1));
                                    break;
                            }
                        }
                    }
                });
    }

    private void delNewsSaveImage(String httpUrl, int i) {
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
                                    activityNewsBinding.ivLike.setImageResource(R.drawable.icon_like);
                                    news.setLikes_count(String.valueOf(Integer.parseInt(news.getLikes_count()) - 1));
                                    break;
                                case 1:
                                    strByNewsSave = false;
                                    activityNewsBinding.setImageInt(R.drawable.icon_news_save);
                                    news.setSave_count(String.valueOf(Integer.parseInt(news.getSave_count()) - 1));
                                    break;
                            }
                        }
                    }
                });
    }

    private void getNewsByCount(String httpUrl) {
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Log.e("error----------------->", e.getMessage());
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        textviewLoadService.showCallback(LoadingCallback.class);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        textviewLoadService.showSuccess();
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s != null) {
                            JSONArray array = null;
                            try {
                                array = new JSONArray(s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            for (int i = 0; i < array.length(); i++) {
                                try {
                                    JSONObject obj = array.getJSONObject(i);
                                    news.setLikes_count(obj.optString("likes_count"));
                                    news.setSave_count(obj.optString("save_count"));
                                    news.setReview_count(obj.optString("review_count"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    return;
                                }
                            }
                        }
                    }
                });
    }

    private void DownloadToDB() {
        Observable.create((ObservableOnSubscribe<Document>) emitter -> {
            Log.d(TAG, "subscribe");
            emitter.onNext(new JsoupHtml().JsoupNewsHtml(news.getUrl()));
            emitter.onComplete();
        }).observeOn(AndroidSchedulers.mainThread()) //观察者所在线程，即后台任务执行完成后需要回调的线程
                .subscribeOn(Schedulers.io()) //被观察者所在的线程，在后台任务执行的线程
                .subscribe(new Observer<Document>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                        textLoadService.showCallback(LoadingCallback.class);
                    }

                    @Override
                    public void onNext(Document result) {
                        Log.d(TAG, "result = " + result);
                        activityNewsBinding.NewsText.setText(Html.fromHtml(String.valueOf(result), new MImageGetter(activityNewsBinding.NewsText, NewsActivity.this), null));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError = " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                        textLoadService.showSuccess();
                    }
                });
    }
    @Override
    protected void onPause() {
        super.onPause();
        onPauseByBooler=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(onPauseByBooler){
            userNewsReviewAdapter.getItem(NewsClass.getPosition()).setSpeech_count(NewsClass.getSpeech_count());
            onPauseByBooler=false;
        }
    }
}
