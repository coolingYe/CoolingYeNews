package com.example.coolingyenews.news.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.coolingyenews.base.loadsir.LoadingCallback;
import com.example.coolingyenews.base.loadsir.TimeoutCallback;
import com.example.coolingyenews.bean.News;
import com.example.coolingyenews.databinding.FragmentNewsItemListBinding;
import com.example.coolingyenews.news.adapter.NewsItemListAdapter;
import com.example.coolingyenews.utils.GsonUtils;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsItemListFragment extends Fragment {

    private static final String KEY_HTTPURL = "param1";
    private List<News> list= new ArrayList<>();
    private String mParam1;
    private NewsItemListAdapter newsItemListAdapter;
    private FragmentNewsItemListBinding binding;
    private LoadService loadService;
    public NewsItemListFragment() {
        // Required empty public constructor
    }

    public static NewsItemListFragment newInstance(String param1) {
        NewsItemListFragment fragment = new NewsItemListFragment();
        Bundle args = new Bundle();
        args.putString(KEY_HTTPURL, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(KEY_HTTPURL);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewsItemListBinding.inflate(inflater,container,false);
        loadService = LoadSir.getDefault().register(binding.newsItemList, (Callback.OnReloadListener) v -> {
        });
        getNews(mParam1);
        return loadService.getLoadLayout();
    }



    private void getNews(String httpUrl) {
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        loadService.showCallback(LoadingCallback.class);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        loadService.showSuccess();
                    }

                    @Override
                    public void onError(ApiException e) {
                        loadService.showCallback(TimeoutCallback.class);
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s != null) {
                            try {
                                JSONArray array = new JSONArray(s);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject obj = array.getJSONObject(i);
                                    News news= GsonUtils.fromLocalJson(obj.toString(), News.class);
                                    list.add(news);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            newsItemListAdapter = new NewsItemListAdapter(list);
                            binding.newsItemList.setAdapter(newsItemListAdapter);
                        }
                    }
                });
    }

}