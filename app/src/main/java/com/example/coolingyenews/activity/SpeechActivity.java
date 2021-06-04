package com.example.coolingyenews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.coolingyenews.R;
import com.example.coolingyenews.bean.News;
import com.example.coolingyenews.bean.Review;
import com.example.coolingyenews.bean.SReview;
import com.example.coolingyenews.bean.Video;
import com.example.coolingyenews.databinding.ActivitySpeechDetailBinding;
import com.example.coolingyenews.news.activity.NewsActivity;
import com.example.coolingyenews.news.adapter.UserNewsReviewAdapter;
import com.example.coolingyenews.utils.GsonUtils;
import com.example.coolingyenews.utils.HttpURL;
import com.example.coolingyenews.utils.NewsClass;
import com.example.coolingyenews.utils.ReplaceStr;
import com.example.coolingyenews.utils.ToastUtil;
import com.example.coolingyenews.utils.UserClass;
import com.example.coolingyenews.utils.utilsTools;
import com.example.coolingyenews.video.VideoAdapter;
import com.example.coolingyenews.widget.dialog.InputTextMsgDialog;
import com.example.coolingyenews.widget.help.SoftKeyBoardListener;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.coolingyenews.news.activity.NewsActivity.REQUEST_CODE;
import static com.example.coolingyenews.utils.HttpURL.localhost;

/**
 * 评论详情
 */
@Route(path = "/Main/speech")
public class SpeechActivity extends AppCompatActivity {
    @Autowired
    public String rId;
    @Autowired
    public Review review;
    @Autowired
    public int key;
    private ActivitySpeechDetailBinding binding;
    private List<SReview> list =new ArrayList<>();
    private SpeechAdapter speechDetailAdapter;
    private InputTextMsgDialog inputTextMsgDialog;
    private SoftKeyBoardListener mKeyBoardListener;
    public final static int RESULT_CODE = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_speech_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if(rId!=null){
            getSecondarySpeech("http://"+localhost+":8080/NewsMaven/getSpeech?rid="+rId);
        }
        if(review!=null){
            binding.setReview(review);
        }
        if(key==1){
            binding.tvMain.setVisibility(View.VISIBLE);
        }
        initOnCheck();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getSecondarySpeech(String httpUrl){
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s!=null) {
                            try {
                                JSONArray array = new JSONArray(s);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject obj = array.getJSONObject(i);
                                    SReview sReview= GsonUtils.fromLocalJson(obj.toString(), SReview.class);
                                    sReview.setIcon(new ReplaceStr().newStr(sReview.getIcon(), localhost));
                                    sReview.setUser_value("0");
                                    sReview.setLikes_count("0");
                                    sReview.setLikes_img(sReview.getUser_value().equals("1") ? R.drawable.icon_like_fill : R.drawable.icon_like);
                                    list.add(sReview);
                                }
                                speechDetailAdapter =new SpeechAdapter(list,SpeechActivity.this,binding);
                                speechDetailAdapter.setFooterView(getFooterView());
                                binding.speechItemList.setAdapter(speechDetailAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void initOnCheck(){
        binding.ivChat.setOnClickListener(v -> {
            if (inputTextMsgDialog == null) {
                inputTextMsgDialog = new InputTextMsgDialog(this, R.style.dialog);
                inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msg) {
                        if (UserClass.getUid() == 0) {
                            ToastUtil.show(SpeechActivity.this, "登录，发表你的评论");
                            return;
                        }
                        sendReviewWithHttpURLConnection("http://"+localhost+":8080/NewsMaven/setSpeech?uid="+UserClass.getUid()+"&parent_comment_id="+review.getReviewId()+"&parent_comment_user_id="+review.getReviewUId()+"&reply_comment_user_id="+review.getUserName()+"&content="+msg+"&times="+ utilsTools.getTime(),msg);                    }

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

        binding.tvMain.setOnClickListener(v -> {
            if(review.getNid()!=""){
                getNewsByNid("http://"+localhost+":8080/NewsMaven/getNewsByNid?nid="+review.getNid());
            }else {
                getVideo("http://"+localhost+":8080/NewsMaven/getVideoByVid?vid="+review.getVid());
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
    private View getFooterView()
    {
             View view = getLayoutInflater().inflate(R.layout.user_item_footer_view,null);
            return view;

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
                            SReview review = new SReview();
                            review.setUid(UserClass.getUid());
                            review.setUname(UserClass.getUname());
                            review.setContent(msg);
                            review.setTimes(utilsTools.getTime());
                            review.setIcon(UserClass.getIcon());
                            review.setUser_value("0");
                            review.setLikes_count("0");
                            //review.setSpeech_count("0");
                            review.setLikes_img(review.getUser_value().equals("1") ? R.drawable.icon_like_fill : R.drawable.icon_like);
                            list.add(0, review);
                            //activityNewsBinding.tvReviewCount.setText(String.valueOf(list.size()));
                            if (list.size() != 1) {
                                speechDetailAdapter.notifyItemInserted(0);
                                binding.speechItemList.getLayoutManager().scrollToPosition(0);
                            } else {
                                speechDetailAdapter = new SpeechAdapter(list, SpeechActivity.this,binding);
                                binding.speechItemList.setAdapter(speechDetailAdapter);
                            }
                        }
                    }
                });
    }
    @Override
    protected void onPause() {
        super.onPause();
        NewsClass.setSpeech_count(speechDetailAdapter.getItemCount()-1+"");
    }

    public void getNewsByNid(String httpUrl){
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Log.e("error----------------->", e.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s!=null) {
                            try {
                                JSONArray array = new JSONArray(s);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject obj = array.getJSONObject(i);
                                    News news= GsonUtils.fromLocalJson(obj.toString(), News.class);
                                    ARouter.getInstance()
                                            .build("/News/News")
                                            .withSerializable("news",news)
                                            .navigation();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
    private void getVideo(String httpUrl) {
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        //loadService.showCallback(LoadingCallback.class);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        //loadService.showSuccess();
                    }

                    @Override
                    public void onError(ApiException e) {
                        //loadService.showCallback(TimeoutCallback.class);
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s != null) {
                            try {
                                JSONArray array = new JSONArray(s);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject obj = array.getJSONObject(i);
                                    Video video= GsonUtils.fromLocalJson(obj.toString(), Video.class);
                                    ARouter.getInstance()
                                            .build("/video/player")
                                            .withSerializable("videoInfo",video)
                                            .navigation();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}
