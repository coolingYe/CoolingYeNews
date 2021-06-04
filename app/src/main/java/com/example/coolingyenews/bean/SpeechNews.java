package com.example.coolingyenews.bean;

public class SpeechNews {
    private int rId;
    private String nId;
    private String vId;
    private String uName;
    private String uImg;
    private String nImg;
    private String Time;
    private String nTitle;
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

    public String getnId() {
        return nId;
    }

    public void setnId(String nId) {
        this.nId = nId;
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

    public String getnImg() {
        return nImg;
    }

    public void setnImg(String nImg) {
        this.nImg = nImg;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public String getnTitle() {
        return nTitle;
    }

    public void setnTitle(String nTitle) {
        this.nTitle = nTitle;
    }



    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

}
