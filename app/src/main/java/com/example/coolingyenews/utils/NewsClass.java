package com.example.coolingyenews.utils;


/**
 * 全局变量
 */
public class NewsClass {

    public static String getSpeech_count() {
        return speech_count;
    }

    public static void setSpeech_count(String speech_count) {
        NewsClass.speech_count = speech_count;
    }

    public static int getPosition() {
        return position;
    }

    public static void setPosition(int position) {
        NewsClass.position = position;
    }

    private static int position=0;
    private static String speech_count;

    public void clear() {
        NewsClass.speech_count = null;
        NewsClass.position = 0;
    }
    public static NewsClass UserClass(){
        NewsClass userClass =new NewsClass();
        return userClass;
    }
}
