package com.example.coolingyenews.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import com.example.coolingyenews.R;
import com.example.coolingyenews.bean.Video;
import com.example.coolingyenews.history.VideoHistoryItemListAdapter;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UploadAvatarUtil {
    public static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public static final int PHOTO_REQUEST_CUT = 3;// 结果
    private ImageView iv_img;
    /**
     * 上传头像
     * @param imgStr
     */
    public void setImgByStr(String imgStr,String httpUrl,String imgName) {
        //这里是头像接口，通过Post请求，拼接接口地址和ID，上传数据。
        EasyHttp.post(httpUrl)
                .params("imgName",imgStr)
                .params("data",imgStr)
                .params("imgName",imgName)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s != null) {
                        }
                    }
                });
    }
}
