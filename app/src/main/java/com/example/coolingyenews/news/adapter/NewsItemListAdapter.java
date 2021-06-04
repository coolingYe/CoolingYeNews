package com.example.coolingyenews.news.adapter;

import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.coolingyenews.R;
import com.example.coolingyenews.bean.News;
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

public class NewsItemListAdapter extends BaseQuickAdapter<News,BaseViewHolder>{


    public NewsItemListAdapter(@Nullable List<News> data) {
        super(R.layout.fragment_news_item, data);
    }

    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);

    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, News news) {
        FragmentNewsItemBinding fragmentNewsItemBinding = baseViewHolder.getBinding();
        if(fragmentNewsItemBinding!=null){
            baseViewHolder.itemView.setOnClickListener(v -> {
                setNewsByHistory(new HttpURL().HTTP_HISTORY_ADD_OR_DEL_URL("setNewsByHistory",UserClass.getUid(),news.getNid()));
                ARouter.getInstance()
                        .build("/News/News")
                        .withSerializable("news",news)
                        .navigation();
            });
            fragmentNewsItemBinding.setViewModel(news);
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

}
