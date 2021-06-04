package com.example.coolingyenews.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.coolingyenews.BR;

import java.io.Serializable;

public class Video extends BaseObservable implements Serializable {

    private String vid;
    private String likes_count;
    private String times;
    private String save_count;
    private String playerUrl;
    private String author;
    private String review_count;
    private String title;
    private String coverUrl;
    private String authorUrl;
    private String blurredUrl;
    private String userDescription;
    private String nickName;
    private String description;
    private String video_description;

    public String getBlurredUrl() {
        return blurredUrl;
    }

    public void setBlurredUrl(String blurredUrl) {
        this.blurredUrl = blurredUrl;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideo_description() {
        return video_description;
    }

    public void setVideo_description(String video_description) {
        this.video_description = video_description;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }
    public String getVid() {
        return vid;
    }

    public void setLikes_count(String likes_count) {
        this.likes_count = likes_count;
        notifyPropertyChanged(BR.likes_count);//通知变化
    }
    @Bindable
    public String getLikes_count() {
        return likes_count;
    }

    public void setTimes(String times) {
        this.times = times;
    }
    public String getTimes() {
        return times;
    }

    public void setSave_count(String save_count) {
        this.save_count = save_count;
        notifyPropertyChanged(BR.save_count);//通知变化
    }
    @Bindable
    public String getSave_count() {
        return save_count;
    }

    public void setPlayerUrl(String playerUrl) {
        this.playerUrl = playerUrl;
    }
    public String getPlayerUrl() {
        return playerUrl;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public String getAuthor() {
        return author;
    }

    public void setReview_count(String review_count) {
        this.review_count = review_count;
        notifyPropertyChanged(BR.review_count);//通知变化
    }
    @Bindable
    public String getReview_count() {
        return review_count;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
}