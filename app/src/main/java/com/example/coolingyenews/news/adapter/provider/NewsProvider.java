package com.example.coolingyenews.news.adapter.provider;

import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.coolingyenews.R;
import com.example.coolingyenews.bean.News;
import com.example.coolingyenews.bean.NewsAndVideo;
import com.example.coolingyenews.databinding.FragmentNewsItemBinding;
import com.example.coolingyenews.utils.HttpURL;
import com.example.coolingyenews.utils.ToastUtil;
import com.example.coolingyenews.utils.UserClass;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NewsProvider extends BaseItemProvider<NewsAndVideo> {


    @Override
    public void onViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);

    }

    @Override
    public void convert(@NotNull BaseViewHolder baseViewHolder, NewsAndVideo newsAndVideo) {
        FragmentNewsItemBinding fragmentNewsItemBinding = baseViewHolder.getBinding();
        if(fragmentNewsItemBinding!=null){
            baseViewHolder.itemView.setOnClickListener(v -> {
                setNewsByHistory(new HttpURL().HTTP_HISTORY_ADD_OR_DEL_URL("setNewsByHistory",UserClass.getUid(),newsAndVideo.getNews().getNid()));
                ARouter.getInstance()
                        .build("/News/News")
                        .withSerializable("news",newsAndVideo.getNews())
                        .navigation();
            });
            fragmentNewsItemBinding.setViewModel(newsAndVideo.getNews());
            fragmentNewsItemBinding.executePendingBindings();
        }
    }
    private void setNewsByHistory(String httpUrl) {
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
        return ItemType.NEWS_VIEW;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_news_item;
    }
}
