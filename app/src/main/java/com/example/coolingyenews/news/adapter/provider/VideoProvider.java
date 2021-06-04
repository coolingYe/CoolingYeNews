package com.example.coolingyenews.news.adapter.provider;

import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.coolingyenews.R;
import com.example.coolingyenews.bean.NewsAndVideo;
import com.example.coolingyenews.bean.Video;
import com.example.coolingyenews.databinding.FragmentVideoItemBinding;
import com.example.coolingyenews.utils.HttpURL;
import com.example.coolingyenews.utils.ToastUtil;
import com.example.coolingyenews.utils.UserClass;
import com.example.coolingyenews.widget.view.ItemView;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VideoProvider extends BaseItemProvider<NewsAndVideo> {
    @Override
    public void onViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);

    }
    @Override
    public void convert(@NotNull BaseViewHolder helper, NewsAndVideo newsAndVideo) {

        FragmentVideoItemBinding binding =helper.getBinding();
        if(binding!=null){
            helper.itemView.setOnClickListener(v -> {
                setVideoByHistory(new HttpURL().HTTP_VIDEO_HISTORY_ADD_OR_DEL_URL("setVideoByHistory", UserClass.getUid(),newsAndVideo.getVideo().getVid()));
                ARouter.getInstance()
                        .build("/video/player")
                        .withSerializable("videoInfo",newsAndVideo.getVideo())
                        .navigation();
            });
            binding.setViewModel(newsAndVideo.getVideo());
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

    @Override
    public int getItemViewType() {
        return ItemType.VIDEO_VIEW;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_video_item;
    }
}