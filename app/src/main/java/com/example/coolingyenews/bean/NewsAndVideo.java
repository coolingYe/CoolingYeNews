package com.example.coolingyenews.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class NewsAndVideo implements MultiItemEntity {
    private News news;
    private Video video;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    private int itemType;

    public static final int TYPE1 = 1;
    public static final int TYPE2 = 2;
    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }


    @Override
    public int getItemType() {
        return itemType;
    }
}
