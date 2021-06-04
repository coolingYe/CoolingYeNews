package com.example.coolingyenews.video;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.coolingyenews.R;
import com.example.coolingyenews.base.BaseFragment;
import com.example.coolingyenews.bean.News;
import com.example.coolingyenews.bean.Video;
import com.example.coolingyenews.databinding.FragmentVideoBinding;
import com.example.coolingyenews.news.adapter.NewsItemListAdapter;
import com.example.coolingyenews.utils.GsonUtils;
import com.example.coolingyenews.utils.ToastUtil;
import com.kingja.loadsir.core.LoadService;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.coolingyenews.utils.HttpURL.localhost;

public class VideoFragment_ extends BaseFragment<FragmentVideoBinding> implements View.OnClickListener {
    private LoadService loadService;
    private List<Video>list= new ArrayList<>();
    private VideoAdapter videoAdapter;
    private int mDisplayWidth;
    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_video;
    }


    @Override
    public void initView(View rootView) {
//        loadService = LoadSir.getDefault().register(viewDataBinding.rvNominateView, (Callback.OnReloadListener) v -> {
//        });
        //设置搜索界面宽度为屏幕宽度
        viewDataBinding.refreshLayout.setRefreshHeader(new MaterialHeader(getContext()));
        viewDataBinding.refreshLayout.setRefreshFooter(new BallPulseFooter(getContext()));
        viewDataBinding.refreshLayout.setOnRefreshListener(refreshlayout -> {
            refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
        });
        viewDataBinding.refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
        });
        getVideo("http://"+localhost+":8080/NewsMaven/getVideo");
    }


    @Override
    protected void loadData() {
        viewDataBinding.etSearch.setOnEditorActionListener((v, actionId, event) ->{
            if(actionId== EditorInfo.IME_ACTION_SEARCH){
                String searchKey=viewDataBinding.etSearch.getTrimmedString();
                if(!TextUtils.isEmpty(searchKey)){
                    viewDataBinding.etSearch.clearFocus();
                    EasyHttp.get("http://"+localhost+":8080/NewsMaven/searchForVideo?title="+searchKey)
                            .execute(new SimpleCallBack<String>() {
                                @Override
                                public void onStart() {
                                    super.onStart();
                                }

                                @Override
                                public void onCompleted() {
                                    super.onCompleted();
                                }

                                @Override
                                public void onError(ApiException e) {
                                }

                                @Override
                                public void onSuccess(String s) {
                                    if (!s.equals("[]")) {
                                        list.clear();
                                        viewDataBinding.recyclerView.removeAllViews();
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
                                        videoAdapter = new VideoAdapter(list);
                                        viewDataBinding.recyclerView.setAdapter(videoAdapter);
                                    }
                                }
                            });
                }else  {ToastUtil.show(getContext(),"没有哦~");}
                return true;
            }
            return false;
        });

        viewDataBinding.tvSearchSearch.setOnClickListener(v -> {
            String searchKey=viewDataBinding.etSearch.getTrimmedString();
            if(!TextUtils.isEmpty(searchKey)){
                viewDataBinding.etSearch.clearFocus();
                EasyHttp.get("http://"+localhost+":8080/NewsMaven/searchForVideo?title="+searchKey)
                        .execute(new SimpleCallBack<String>() {
                            @Override
                            public void onStart() {
                                super.onStart();
                            }

                            @Override
                            public void onCompleted() {
                                super.onCompleted();
                            }

                            @Override
                            public void onError(ApiException e) {
                            }

                            @Override
                            public void onSuccess(String s) {
                                if (!s.equals("[]")) {
                                    list.clear();
                                    viewDataBinding.recyclerView.removeAllViews();
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
                                    videoAdapter = new VideoAdapter(list);
                                    viewDataBinding.recyclerView.setAdapter(videoAdapter);
                                }else  {ToastUtil.show(getContext(),"没有哦~");}
                            }
                        });
            }
        });
    }

    @Override
    public void initListener() {
        findViewById(R.id.tv_seacher).setOnClickListener(this);
        findViewById(R.id.tv_close_search).setOnClickListener(this);
    }


    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_seacher:
                viewDataBinding.drawerLayout.post(() -> {
                    viewDataBinding.drawerLayout.openDrawer(Gravity.START);
                });
                break;
            case R.id.tv_close_search:
                viewDataBinding.drawerLayout.closeDrawer(Gravity.START);
                if(!list.isEmpty()){
                    viewDataBinding.etSearch.setText(null);
                    viewDataBinding.recyclerView.removeAllViews();
                    list.clear();
                    videoAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
    private void getVideo(String httpUrl) {
        EasyHttp.get(httpUrl)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        //loadService.showCallback(LoadingCallback.class);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        //loadService.showSuccess();
                    }

                    @Override
                    public void onError(ApiException e) {
                        //loadService.showCallback(TimeoutCallback.class);
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
                            videoAdapter = new VideoAdapter(list);
                            viewDataBinding.rvNominateView.setAdapter(videoAdapter);
                        }
                    }
                });
    }
}