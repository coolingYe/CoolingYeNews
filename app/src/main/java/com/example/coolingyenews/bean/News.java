package com.example.coolingyenews.bean;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.coolingyenews.BR;

import java.io.Serializable;

public class News extends BaseObservable implements Serializable {
    private String ntid;
    private String img;
    private String save_count;
    private String author;
    private String nid;
    private String review_count;
    private String likes_count;
    private String time;
    private String title;
    private String url;

    public void setNtid(String ntid) {
        this.ntid = ntid;
    }
    public String getNtid() {
        return ntid;
    }

    public void setImg(String img) {
        this.img = img;
    }
    public String getImg() {
        return img;
    }

    public void setSave_count(String save_count) {
        this.save_count = save_count;
        notifyPropertyChanged(BR.save_count);//通知变化
    }
    @Bindable
    public String getSave_count() {
        return save_count;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public String getAuthor() {
        return author;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }
    public String getNid() {
        return nid;
    }

    public void setReview_count(String review_count) {
        this.review_count = review_count;
        notifyPropertyChanged(BR.review_count);//通知变化
    }
    @Bindable
    public String getReview_count() {
        return review_count;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getTime() {
        return time;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }
    @Bindable
    public String getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(String likes_count) {
        this.likes_count = likes_count;
        notifyPropertyChanged(BR.likes_count);//通知变化
    }
}
