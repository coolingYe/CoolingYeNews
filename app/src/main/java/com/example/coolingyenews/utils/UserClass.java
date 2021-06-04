package com.example.coolingyenews.utils;


/**
 * 全局变量
 */
public class UserClass {
    public static int getUid() {
        return uid;
    }

    public static void setUid(int uid) {
        UserClass.uid = uid;
    }

    public static String getUname() {
        return uname;
    }

    public static void setUname(String uname) {
        UserClass.uname = uname;
    }

    public static String getPwd() {
        return pwd;
    }

    public static void setPwd(String pwd) {
        UserClass.pwd = pwd;
    }

    public static String getSex() {
        return sex;
    }

    public static void setSex(String sex) {
        UserClass.sex = sex;
    }

    public static String getTel() {
        return tel;
    }

    public static void setTel(String tel) {
        UserClass.tel = tel;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        UserClass.email = email;
    }

    public static String getBirthday() {
        return birthday;
    }

    public static void setBirthday(String birthday) {
        UserClass.birthday = birthday;
    }

    public static String getSignature() {
        return signature;
    }

    public static void setSignature(String signature) {
        UserClass.signature = signature;
    }

    public static String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    private static int uid;
    private static String uname;
    private static String pwd;
    private static String sex;
    private static String tel;
    private static String email;
    private static String birthday;
    private static String signature;
    private static String icon;

    public void clear() {
        UserClass.uid = 0;
        UserClass.uname = null;
        UserClass.pwd = null;
        UserClass.sex = null;
        UserClass.tel = null;
        UserClass.email = null;
        UserClass.birthday = null;
        UserClass.signature = null;
        UserClass.icon = null;
    }

    public static UserClass UserClass(){
        UserClass userClass =new UserClass();
        return userClass;
    }
}
