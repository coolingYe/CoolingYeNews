package com.example.coolingyenews.news.adapter.provider;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 应用模块:
 * <p>
 * 类描述: itemType 类型
 * <p>
 *
 * @author darryrzhoong
 * @since 2020-02-15
 */
public interface ItemType extends MultiItemEntity {
    /**
     * 顶部banner
     */
    int NEWS_VIEW = 1;
    
    /**
     * 热门分类
     */
    int VIDEO_VIEW = 2;
}
