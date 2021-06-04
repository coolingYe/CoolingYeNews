package com.example.coolingyenews.activity;

import android.app.Dialog;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.coolingyenews.R;
import com.example.coolingyenews.bean.Likes;
import com.example.coolingyenews.bean.Review;
import com.example.coolingyenews.bean.SReview;
import com.example.coolingyenews.databinding.FragmentLikeItemBinding;
import com.example.coolingyenews.databinding.FragmentReplyItemBinding;
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

public class LikeAdapter extends BaseQuickAdapter<Review, BaseViewHolder> {
    private List<Review> data;

    public LikeAdapter(@Nullable List<Review> data) {
        super(R.layout.fragment_like_item, data);
        this.data = data;
    }
    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);

    }
    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Review review) {
        FragmentLikeItemBinding binding =baseViewHolder.getBinding();
        if(binding!=null){
            binding.setReview(data.get(baseViewHolder.getPosition()));
            binding.executePendingBindings();
            baseViewHolder.itemView.setOnClickListener(v -> {
                ARouter.getInstance()
                        .build("/Main/speech")
                        .withSerializable("review",review).withString("rId",String.valueOf(review.getReviewId())).withSerializable("review",review).withInt("key",1)
                        .navigation();
            });
        }
    }
}
