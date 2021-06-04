package com.example.coolingyenews.activity;

import android.app.Dialog;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.coolingyenews.R;
import com.example.coolingyenews.bean.Review;
import com.example.coolingyenews.bean.SReview;
import com.example.coolingyenews.databinding.FragmentReplyItemBinding;
import com.example.coolingyenews.utils.GsonUtils;
import com.example.coolingyenews.utils.ReplaceStr;
import com.example.coolingyenews.utils.UserClass;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.coolingyenews.utils.HttpURL.localhost;

public class ReplyAdapter extends BaseQuickAdapter<SReview, BaseViewHolder> {
    private List<SReview> data;
    private List<Review> list =new ArrayList<>();
    private View viewDialog;
    private Dialog bottomDialog;
    public ReplyAdapter(@Nullable List<SReview> data) {
        super(R.layout.fragment_reply_item, data);
        this.data = data;
    }
    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);

    }
    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SReview sReview) {
        FragmentReplyItemBinding binding =baseViewHolder.getBinding();
        if(binding!=null){
            binding.setSReview(data.get(baseViewHolder.getPosition()));
            binding.executePendingBindings();
            baseViewHolder.itemView.setOnClickListener(v -> {
                setReview("http://"+localhost+":8080/NewsMaven/getNewsReviewByRid?rid="+sReview.getParent_comment_id()+"&uid="+ UserClass.getUid());
            });
        }
    }
    public void setReview(String httpUrl){
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
                                    ARouter.getInstance()
                                            .build("/Main/speech")
                                            .withSerializable("review",review).withString("rId",String.valueOf(review.getReviewId())).withInt("key",1)
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
