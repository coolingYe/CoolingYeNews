package com.example.coolingyenews.service;

import android.content.Intent;

import androidx.fragment.app.FragmentActivity;

import com.example.coolingyenews.activity.MainActivity;
import com.example.coolingyenews.bean.User;
import com.example.coolingyenews.utils.ReplaceStr;
import com.example.coolingyenews.utils.ToastUtil;
import com.example.coolingyenews.utils.UserClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.example.coolingyenews.utils.HttpURL.localhost;

public class UserHandler {

    public void RequestUserLoginStatus(FragmentActivity getContext, List<User> list, String httpUrl){
        new Thread(() -> {
            HttpURLConnection connection;
            BufferedReader br;
            try {
                URL url = new URL(httpUrl);
                connection = (HttpURLConnection) url.openConnection();
                //GET 表示获取数据  POST表示发送数据
                connection.setRequestMethod("GET");
                //设置连接超时的时间
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                InputStream in = connection.getInputStream();
                //下面对获取到的输入流进行读取
                br = new BufferedReader(new InputStreamReader(in));
                final String result = br.readLine();
                //List<SpeechBean> list= new ArrayList<SpeechBean>();
                try {
                    if (!result.equals("[]")) {
                        JSONArray array = new JSONArray(result);
                        for (int i = 0; i < array.length(); i++) {
                            try {
                                JSONObject obj = array.getJSONObject(i);
                                User user = new User();
                                user.setUid(obj.optInt("uid"));
                                user.setUname(obj.optString("uname"));
                                user.setPwd(obj.optString("pwd"));
                                user.setSex(obj.optString("sex"));
                                user.setTel(obj.optString("tel"));
                                user.setEmail(obj.optString("email"));
                                user.setBirthday(obj.optString("birthday"));
                                user.setSignature(obj.optString("signature"));
                                user.setIcon(new ReplaceStr().newStr(obj.optString("icon"), localhost));
                                list.add(user);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        showResponse(getContext,list);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                showUI(getContext, "服务器开小差");
                e.printStackTrace();
            }
        });
    }

    public void loginSendRequestWithHttpURLConnection(FragmentActivity getContext, List<User> list, String httpUrl) {
        new Thread(() -> {
            HttpURLConnection connection;
            BufferedReader br;
            try {
                URL url = new URL(httpUrl);
                connection = (HttpURLConnection) url.openConnection();
                //GET 表示获取数据  POST表示发送数据
                connection.setRequestMethod("GET");
                //设置连接超时的时间
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                InputStream in = connection.getInputStream();
                //下面对获取到的输入流进行读取
                br = new BufferedReader(new InputStreamReader(in));
                final String result = br.readLine();
                //List<SpeechBean> list= new ArrayList<SpeechBean>();
                try {
                    if (!result.equals("[]")) {
                        JSONArray array = new JSONArray(result);
                        for (int i = 0; i < array.length(); i++) {
                            try {
                                JSONObject obj = array.getJSONObject(i);
                                User user = new User();
                                user.setUid(obj.optInt("uid"));
                                user.setUname(obj.optString("uname"));
                                user.setPwd(obj.optString("pwd"));
                                user.setSex(obj.optString("sex"));
                                user.setTel(obj.optString("tel"));
                                user.setEmail(obj.optString("email"));
                                user.setBirthday(obj.optString("birthday"));
                                user.setSignature(obj.optString("signature"));
                                user.setIcon(new ReplaceStr().newStr(obj.optString("icon"), localhost));
                                list.add(user);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        showResponse(getContext,list);
                    }else {
                        showUI(getContext,"账号密码错误");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                showUI(getContext,"服务器开小差");
                e.printStackTrace();
            }
        }).start();
    }


    private void showUI(FragmentActivity getContext,final String erro) {
        getContext.runOnUiThread(() ->  ToastUtil.show(getContext, erro));
    }
    private void showResponse(FragmentActivity getContext,final List<User> list) {
        getContext.runOnUiThread(() -> {
            Intent intent = new Intent();
            intent.setClass(getContext, MainActivity.class);
            UserClass userClass = new UserClass();
            userClass.setUid(list.get(0).getUid());
            userClass.setUname(list.get(0).getUname());
            userClass.setPwd(list.get(0).getPwd());
            userClass.setSex(list.get(0).getSex());
            userClass.setTel(list.get(0).getTel());
            userClass.setEmail(list.get(0).getEmail());
            userClass.setBirthday(list.get(0).getBirthday());
            userClass.setSignature(list.get(0).getSignature());
            userClass.setIcon(list.get(0).getIcon());
             ToastUtil.show(getContext, "登录成功");
            getContext.startActivity(intent);
            getContext.finish();

        });
    }
}
