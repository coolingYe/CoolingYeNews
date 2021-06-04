package com.example.coolingyenews.bean;

public class SpeechVideo {
    private int rId;
    private String vId;
    private String uName;
    private String uImg;
    private String vImg;
    private String Time;
    private String Title_1;

    public String getTitle_1() {
        return Title_1;
    }

    public void setTitle_1(String title_1) {
        Title_1 = title_1;
    }

    public String getTitle_2() {
        return Title_2;
    }

    public void setTitle_2(String title_2) {
        Title_2 = title_2;
    }

    public String getvTime() {
        return vTime;
    }

    public void setvTime(String vTime) {
        this.vTime = vTime;
    }

    private String Title_2;
    private String vTime;
    private String Text;
    public String getvId() {
        return vId;
    }

    public void setvId(String vId) {
        this.vId = vId;
    }
    public int getRid() {
        return rId;
    }

    public void setRid(int rid) {
        this.rId = rid;
    }



    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuImg() {
        return uImg;
    }

    public void setuImg(String uImg) {
        this.uImg = uImg;
    }

    public String getvImg() {
        return vImg;
    }

    public void setnImg(String vImg) {
        this.vImg = vImg;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }




    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

}
