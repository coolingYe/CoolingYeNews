package com.example.coolingyenews.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.coolingyenews.BR;

import java.io.Serializable;
import java.util.List;

public class Review extends BaseObservable implements Serializable {
    private int ReviewId;                  //评论id
    private int ReviewUId;                 //评论的用户id
    private String ReviewContent;          //记录的内容
    private String UserName;               //评论的用户名称
    private String UserIconSrc;            //评论的用户头像
    private String ReviewTime;             //保存记录的时间
    private String likes_count;            //点赞数量
    private String level;                  //评论级数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    private String nid;                     //被评论的id
    private String vid;                     //被评论的id
    private String user_value;             //
    private int likes_img;                 //
    private int hate_img;                  //
    private String speech_count;           //
    //当前一级评论的位置（下标）
    private int position;
    //当前一级评论所在的位置(下标)
    private int positionCount;
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPositionCount() {
        return positionCount;
    }

    public void setPositionCount(int positionCount) {
        this.positionCount = positionCount;
    }
    @Bindable
    public String getSpeech_count() {
        return speech_count;
    }

    public void setSpeech_count(String speech_count) {
        this.speech_count = speech_count;
        notifyPropertyChanged(BR.speech_count);//通知变化
    }
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


    @Bindable
    public String getUser_value() {
        return user_value;
    }

    public void setUser_value(String user_value) {
        this.user_value = user_value;
        notifyPropertyChanged(BR.user_value);//通知变化
    }
    @Bindable
    public int getLikes_img() {
        return likes_img;
    }

    public void setLikes_img(int likes_img) {
        this.likes_img = likes_img;
        notifyPropertyChanged(BR.likes_img);//通知变化
    }

    @Bindable
    public int getHate_img() {
        return hate_img;
    }

    public void setHate_img(int hate_img) {
        this.hate_img = hate_img;
        notifyPropertyChanged(BR.hate_img);//通知变化

    }

    public int getReviewId() {
        return ReviewId;
    }
    public void setReviewId(int ReviewId) {
        this.ReviewId = ReviewId;
    }
    public int getReviewUId() {
        return ReviewUId;
    }
    public void setReviewUId(int ReviewUId) {
        this.ReviewUId = ReviewUId;
    }
    public String getUserName() {
        return UserName;
    }
    public void setUserName(String UserName) {
        this.UserName = UserName;
    }
    public String getReviewContent() {
        return ReviewContent;
    }
    public void setReviewContent(String ReviewContent) {
        this.ReviewContent = ReviewContent;
    }
    public String getReviewTime() {
        return ReviewTime;
    }
    public void setReviewTime(String ReviewTime) {
        this.ReviewTime = ReviewTime;
    }

    public String getUserIconSrc() {
        return UserIconSrc;
    }

    public void setUserIconSrc(String userIconSrc) {
        this.UserIconSrc = userIconSrc;
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
