package com.example.coolingyenews.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 中央气象台的天气预报API
 * */
public class WeatherUtil {

    /**
     * 获取所有中国 省份及一级城市
     * */
    public String weather() {
        // TODO Auto-generated method stub
        String ws_url = "http://m.weather.com.cn/data5/city.xml";
        String str= "";
        try {
            URL url = new URL(ws_url);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));//解决乱码问题
            StringBuffer sb = new StringBuffer();
            String s = "";
            while ((s = br.readLine()) != null) {
                sb.append(s + "\r\n"); //将内容读取到StringBuffer中
            }
            br.close();
            //System.out.println(sb.toString()); 屏幕
            str = new String(sb.toString().getBytes());
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 根据传入参数获取二级城市
     * */
    public String secondCity(String id){
        String ws_url = "http://m.weather.com.cn/data5/city"+id+".xml";
        String str= "";
        try {
            URL url = new URL(ws_url);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));//解决乱码问题
            StringBuffer sb = new StringBuffer();
            String s = "";
            while ((s = br.readLine()) != null) {
                sb.append(s + "\r\n"); //将内容读取到StringBuffer中
            }
            br.close();
            //System.out.println(sb.toString()); 屏幕
            str = new String(sb.toString().getBytes());
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }
    /**
     * 根据传入参数得到城市天气预报信息ID, 其实也可以直接调用上面方法，为理解方便，故多加一个
     * */
    public String weatherCityId(String id){
        String ws_url = "http://m.weather.com.cn/data5/city"+id+".xml";
        String str= "";
        try {
            URL url = new URL(ws_url);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));//解决乱码问题
            StringBuffer sb = new StringBuffer();
            String s = "";
            while ((s = br.readLine()) != null) {
                sb.append(s + "\r\n"); //将内容读取到StringBuffer中
            }
            br.close();
            //System.out.println(sb.toString()); 屏幕
            str = new String(sb.toString().getBytes());
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }
    public static void main(String[] args) {
    }
}