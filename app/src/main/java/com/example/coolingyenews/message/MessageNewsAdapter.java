package com.example.coolingyenews.message;

import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.coolingyenews.R;
import com.example.coolingyenews.bean.News;
import com.example.coolingyenews.bean.SpeechNews;
import com.example.coolingyenews.databinding.FragmentSpeechItemBinding;
import com.example.coolingyenews.databinding.FragmentSpeechVideoItemBinding;
import com.example.coolingyenews.utils.GsonUtils;
import com.example.coolingyenews.utils.HttpURL;
import com.example.coolingyenews.utils.UserClass;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.example.coolingyenews.utils.HttpURL.localhost;

public class MessageNewsAdapter extends BaseQuickAdapter<SpeechNews, BaseViewHolder> {
    private List<SpeechNews> data;
    public MessageNewsAdapter(@Nullable List<SpeechNews> data) {
        super(R.layout.fragment_speech_item, data);
        this.data = data;
    }
    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);

    }
    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SpeechNews speechNews) {
        FragmentSpeechItemBinding binding =baseViewHolder.getBinding();
        if(binding!=null){
            binding.tvReviewDel.setOnClickListener(v -> {
                delReviewByUser(new HttpURL().HTTP_REVIEW_ADD_OR_DEL_URL("delReviewByUser", UserClass.getUid(), String.valueOf(speechNews.getRid())),baseViewHolder.getPosition());
            });
            baseViewHolder.itemView.setOnClickListener(v -> {
                setNews("http://"+localhost+":8080/NewsMaven/getNewsByNid?nid="+ speechNews.getnId());
            });
            binding.setSpeechNews(data.get(baseViewHolder.getPosition()));
            binding.executePendingBindings();
        }
    }
    public void delReviewByUser(String httpUrl,int i){
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Log.e("error----------------->", e.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s.equals("true")) {
                            notifyItemRemoved(i);
                            data.remove(i);
                        }
                    }
                });
    }

    public void setNews(String httpUrl){
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
}
