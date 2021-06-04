package com.example.coolingyenews.news.activity;



import com.example.coolingyenews.base.activity.IBasePagingView;

import java.util.ArrayList;

/**
 * 应用模块: 首页
 * <p>
 * 类描述: 定制界面 Ui 刷新行为
 * <p>
 *
 * @author darryrzhoong
 * @since 2020-02-11
 */
public interface INewsView extends IBasePagingView
{
    
    /**
     * 数据加载完成
     * 
     * @param viewModels data
     */
    void onDataLoadFinish(ArrayList viewModels,
                          boolean isFirstPage);
    
}
