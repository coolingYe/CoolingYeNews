package com.example.coolingyenews.news.adapter;

import com.chad.library.adapter.base.BaseProviderMultiAdapter;
import com.example.coolingyenews.bean.NewsAndVideo;
import com.example.coolingyenews.common.contract.BaseCustomViewModel;
import com.example.coolingyenews.news.adapter.provider.ItemType;
import com.example.coolingyenews.news.adapter.provider.NewsProvider;
import com.example.coolingyenews.news.adapter.provider.VideoProvider;


import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * 应用模块:
 * <p>
 * 类描述:
 * <p>
 *
 * @author darryrzhoong
 * @since 2020-02-14
 */
public class ProviderNominateAdapter extends BaseProviderMultiAdapter<NewsAndVideo>
{
    
    public ProviderNominateAdapter()
    {
        super();
        // 注册Provide
        addItemProvider(new NewsProvider());
        addItemProvider(new VideoProvider());

        
    }

    @Override
    protected int getItemType(@NotNull List<? extends NewsAndVideo> data,
        int position) {
        if (data.get(position).getNews().getNid()=="") {
            return ItemType.NEWS_VIEW;
        } else if (data.get(position).getNews().getNid()=="") {
            return ItemType.VIDEO_VIEW;
        }

        return -1;
    }
}
