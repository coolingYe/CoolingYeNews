package com.example.coolingyenews.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.coolingyenews.bean.Video;
import com.example.coolingyenews.databinding.FragmentNewsItemListBinding;
import com.example.coolingyenews.utils.GsonUtils;
import com.example.coolingyenews.utils.HttpURL;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VHistoryFragment extends Fragment {
    private static final String KEY_USER_ID = "param1";
    private String mParam1;
    private List<Video> list = new ArrayList<>();
    private FragmentNewsItemListBinding binding;
    private VideoHistoryItemListAdapter videoHistoryItemListAdapter;
    public static VHistoryFragment newInstance(String param1) {
        VHistoryFragment textFragment = new VHistoryFragment();
        Bundle args = new Bundle();
        args.putString(KEY_USER_ID, param1);
        textFragment.setArguments(args);
        return textFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        if(getArguments()!=null){
            mParam1=getArguments().getString(KEY_USER_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewsItemListBinding.inflate(inflater);
        getNewsBySave(HttpURL.HTTP_VIDEO_HISTORY_URL + mParam1);
        return binding.getRoot();
    }

    private void getNewsBySave(String httpUrl) {
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s != null) {
                            try {
                                JSONArray array = new JSONArray(s);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject obj = array.getJSONObject(i);
                                    Video video= GsonUtils.fromLocalJson(obj.toString(), Video.class);
                                    list.add(video);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            videoHistoryItemListAdapter = new VideoHistoryItemListAdapter(list);
                            binding.newsItemList.setAdapter(videoHistoryItemListAdapter);
                        }
                    }
                });
    }
}
