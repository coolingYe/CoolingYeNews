package com.example.coolingyenews.bean;

public class SReview {
    private int sid;
    private int uid;
    private String uname;
    private String icon;
    private String content;
    private String parent_comment_id;
    private String parent_comment_user_id;
    private String reply_comment_id;
    private String reply_comment_user_id;
    private String times;
    private String f_content;
    private int likes_img;
    private String user_value;
    private String likes_count;

    public String getF_content() {
        return f_content;
    }

    public void setF_content(String f_content) {
        this.f_content = f_content;
    }

    public int getLikes_img() {
        return likes_img;
    }

    public void setLikes_img(int likes_img) {
        this.likes_img = likes_img;
    }

    public String getUser_value() {
        return user_value;
    }

    public void setUser_value(String user_value) {
        this.user_value = user_value;
    }

    public String getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(String likes_count) {
        this.likes_count = likes_count;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParent_comment_id() {
        return parent_comment_id;
    }

    public void setParent_comment_id(String parent_comment_id) {
        this.parent_comment_id = parent_comment_id;
    }

    public String getParent_comment_user_id() {
        return parent_comment_user_id;
    }

    public void setParent_comment_user_id(String parent_comment_user_id) {
        this.parent_comment_user_id = parent_comment_user_id;
    }

    public String getReply_comment_id() {
        return reply_comment_id;
    }

    public void setReply_comment_id(String reply_comment_id) {
        this.reply_comment_id = reply_comment_id;
    }

    public String getReply_comment_user_id() {
        return reply_comment_user_id;
    }

    public void setReply_comment_user_id(String reply_comment_user_id) {
        this.reply_comment_user_id = reply_comment_user_id;
    }


}
