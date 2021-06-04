package com.example.coolingyenews.message;

import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.coolingyenews.R;
import com.example.coolingyenews.bean.News;
import com.example.coolingyenews.bean.SpeechNews;
import com.example.coolingyenews.bean.SpeechVideo;
import com.example.coolingyenews.bean.Video;
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

public class MessageVideoAdapter extends BaseQuickAdapter<SpeechVideo, BaseViewHolder> {
    private List<SpeechVideo> data;
    public MessageVideoAdapter(@Nullable List<SpeechVideo> data) {
        super(R.layout.fragment_speech_video_item, data);
        this.data = data;
    }
    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);

    }
    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SpeechVideo speechVideo) {
        FragmentSpeechVideoItemBinding binding =baseViewHolder.getBinding();
        if(binding!=null){
            binding.tvReviewDel.setOnClickListener(v -> {
                delReviewByUser(new HttpURL().HTTP_REVIEW_ADD_OR_DEL_URL("delReviewByUser", UserClass.getUid(), String.valueOf(speechVideo.getRid())),baseViewHolder.getPosition());
            });
            baseViewHolder.itemView.setOnClickListener(v -> {
                getVideo("http://"+localhost+":8080/NewsMaven/getVideoByVid?vid="+ speechVideo.getvId());
            });
            binding.setSpeechVideo(data.get(baseViewHolder.getPosition()));
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

    public void getVideo(String httpUrl){
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        Log.e("error----------------->", e.getMessage());
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
