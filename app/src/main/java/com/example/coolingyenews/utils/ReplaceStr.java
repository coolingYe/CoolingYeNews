package com.example.coolingyenews.utils;

import java.text.SimpleDateFormat;

public class ReplaceStr {

    public String newStr(String a, String url){
        a = a.replaceAll("localhost",url);
        a = a.replaceAll("/ssm_news_system","");
        a =a.replaceAll("\\\\","/");
        return a;
    }
    public String newCityStr(String city){
        return  city.substring(3,5);
    }
    public String newsDataTime(String time){
        return time.substring(0,16);
    }
}
