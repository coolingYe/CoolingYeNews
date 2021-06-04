package com.example.coolingyenews.video;

import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.coolingyenews.R;
import com.example.coolingyenews.bean.Video;
import com.example.coolingyenews.databinding.FragmentVideoItemBinding;
import com.example.coolingyenews.utils.HttpURL;
import com.example.coolingyenews.utils.ToastUtil;
import com.example.coolingyenews.utils.UserClass;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VideoAdapter extends BaseQuickAdapter<Video, BaseViewHolder> {

    public VideoAdapter(@Nullable List<Video> data) {
        super(R.layout.fragment_video_item, data);
    }
    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder baseViewHolder, int viewType) {
        DataBindingUtil.bind(baseViewHolder.itemView);

    }
    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Video video) {
        FragmentVideoItemBinding binding =baseViewHolder.getBinding();
        if(binding!=null){
            baseViewHolder.itemView.setOnClickListener(v -> {
                setVideoByHistory(new HttpURL().HTTP_VIDEO_HISTORY_ADD_OR_DEL_URL("setVideoByHistory", UserClass.getUid(),video.getVid()));
                ARouter.getInstance()
                        .build("/video/player")
                        .withSerializable("videoInfo",video)
                        .navigation();
            });
            binding.setViewModel(video);
            binding.executePendingBindings();
        }
    }
    private void setVideoByHistory(String httpUrl) {
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s.equals("true")) {
                            ToastUtil.show(getContext(),"记录成功");
                        }
                    }
                });
    }
}