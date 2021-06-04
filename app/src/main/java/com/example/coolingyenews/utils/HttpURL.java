package com.example.coolingyenews.utils;

public class HttpURL {
    public static String localhost = "192.168.112.71";//本机IP地址
    public static final String WeatherUrl = "http://api.help.bj.cn/apis/weather2d/?id=";
    public static final String HTTP_REQUEST_URL = "http://"+localhost+":8080/NewsMaven/getNewsByNtid?ntid=";
    public static final String HTTP_NEWS_COLLECT_URL="http://"+localhost+":8080/NewsMaven/getUserFavoritesByNews?uid=";
    public static final String HTTP_VIDEO_COLLECT_URL="http://"+localhost+":8080/NewsMaven/getUserFavoritesByVideo?uid=";
    public static final String HTTP_NEWS_HISTORY_URL="http://"+localhost+":8080/NewsMaven/getNewsByHistory?uid=";
    public static final String HTTP_VIDEO_HISTORY_URL="http://"+localhost+":8080/NewsMaven/getVideoByHistory?uid=";
    public String HTTP_NEWS_REVIEW_URL(String uid,String nid){
        return "http://"+localhost+":8080/NewsMaven/getNewsReview?uid="+uid+"&nid="+nid;
    }
    public String HTTP_VIDEO_REVIEW_URL(String uid,String vid){
        return "http://"+localhost+":8080/NewsMaven/getVideoReview?uid="+uid+"&vid="+vid;
    }
    public String HTTP_COLLECT_ICON_URL(int uid, String nid) {
        return "http://" + localhost + ":8080/NewsMaven/selectCollectionNews?uid=" + uid + "&nid=" + nid;
    }

    public String HTTP_COLLECT_ADD_OR_DEL_URL(String Str,int uid, String nid) {
        return "http://" + localhost + ":8080/NewsMaven/"+Str+"?uid=" + uid + "&nid=" + nid;
    }
    public String HTTP_VIDEO_COLLECT_ADD_OR_DEL_URL(String Str,int uid, String vid) {
        return "http://" + localhost + ":8080/NewsMaven/"+Str+"?uid=" + uid + "&vid=" + vid;
    }
    public String HTTP_HISTORY_ADD_OR_DEL_URL(String Str,int uid, String nid) {
        return "http://" + localhost + ":8080/NewsMaven/"+Str+"?uid=" + uid + "&nid=" + nid;
    }
    public String HTTP_VIDEO_HISTORY_ADD_OR_DEL_URL(String Str,int uid, String vid) {
        return "http://" + localhost + ":8080/NewsMaven/"+Str+"?uid=" + uid + "&vid=" + vid;
    }
    public String HTTP_REVIEW_ADD_OR_DEL_URL(String Str,int uid, String rid) {
        return "http://" + localhost + ":8080/NewsMaven/"+Str+"?uid=" + uid + "&rid=" + rid;
    }
    public String HTTP_SREVIEW_ADD_OR_DEL_URL(String Str,int uid, String sid) {
        return "http://" + localhost + ":8080/NewsMaven/"+Str+"?uid=" + uid + "&sid=" + sid;
    }

    public String HTTP_LOGIN_URL(String account, String pwd) {
        return "http://" + localhost + ":8080/NewsMaven/Login?tel=" + account + "&pwd=" + pwd;
    }

    public String HTTP_LOGIN_STATUS_URL(String account, String status) {
        return "http://" + localhost + ":8080/NewsMaven/LoginStatus?uname=" + account + "&status=" + status;
    }
    public String HTTP_blackLogin_STATUS_URL(String account, String status) {
        return "http://" + localhost + ":8080/NewsMaven/blackLogin?uid=";
    }

    public String HTTP_NEWS_REVIEW_SET_URL(int uid, String nid,String rtext,String level) {
        return "http://" + localhost + ":8080/NewsMaven/setNewsReview?uid=" + uid + "&nid=" + nid+"&rtime="+utilsTools.getTime()+"&rtext="+rtext+"&level="+level;
    }
    public String HTTP_VIDEO_REVIEW_SET_URL(int uid, String nid,String rtext,String level) {
        return "http://" + localhost + ":8080/NewsMaven/setVideoReview?uid=" + uid + "&vid=" + nid+"&rtime="+utilsTools.getTime()+"&rtext="+rtext+"&level="+level;
    }
}
