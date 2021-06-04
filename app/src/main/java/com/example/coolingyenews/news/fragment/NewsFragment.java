package com.example.coolingyenews.news.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.coolingyenews.activity.MainActivity;
import com.example.coolingyenews.R;
import com.example.coolingyenews.adapter.BaseFragmentPageAdapter;
import com.example.coolingyenews.base.BaseFragment;
import com.example.coolingyenews.bean.News;
import com.example.coolingyenews.databinding.FragmentHomeBinding;
import com.example.coolingyenews.news.adapter.NewsItemListAdapter;
import com.example.coolingyenews.news.adapter.ProviderNominateAdapter;
import com.example.coolingyenews.utils.GsonUtils;
import com.example.coolingyenews.utils.HttpURL;
import com.example.coolingyenews.utils.ToastUtil;
import com.example.coolingyenews.utils.UserClass;
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

public class NewsFragment extends BaseFragment<FragmentHomeBinding> implements View.OnClickListener {

    private BaseFragmentPageAdapter pageAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<News> list = new ArrayList<>();
    private int mDisplayWidth;
    private MainActivity mMainActivity;
    private NewsItemListAdapter newsItemListAdapter;
    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_home;
    }


    @Override
    public void initView(View rootView) {
        //设置搜索界面宽度为屏幕宽度
        mDisplayWidth = getResources().getDisplayMetrics().widthPixels;
        LinearLayout mLltSearch = rootView.findViewById(R.id.llt_search);
        ViewGroup.LayoutParams layoutParams = mLltSearch.getLayoutParams();
        layoutParams.width = mDisplayWidth;
        mLltSearch.setLayoutParams(layoutParams);
        Glide.with(getContext())
                .load(UserClass.getIcon())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(viewDataBinding.userIcon);
        viewDataBinding.refreshLayout.setRefreshHeader(new MaterialHeader(getContext()));
        viewDataBinding.refreshLayout.setRefreshFooter(new BallPulseFooter(getContext()));
        viewDataBinding.refreshLayout.setOnRefreshListener(refreshlayout -> {
            refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
        });
        viewDataBinding.refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
        });
        viewDataBinding.etSearch.setOnEditorActionListener((v, actionId, event) ->{
            if(actionId== EditorInfo.IME_ACTION_SEARCH){
                String searchKey=viewDataBinding.etSearch.getTrimmedString();
                if(!TextUtils.isEmpty(searchKey)){
                    viewDataBinding.etSearch.clearFocus();
                    EasyHttp.get("http://"+localhost+":8080/NewsMaven/selectNews?title="+searchKey)
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
                                                News news= GsonUtils.fromLocalJson(obj.toString(), News.class);
                                                list.add(news);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        newsItemListAdapter = new NewsItemListAdapter(list);
                                        viewDataBinding.recyclerView.setAdapter(newsItemListAdapter);
                                    }else   ToastUtil.show(getContext(),"没有哦~");
                                }
                            });
                }
                return true;
            }
            return false;
        });
        viewDataBinding.tvSearchSearch.setOnClickListener(v -> {
            String searchKey=viewDataBinding.etSearch.getTrimmedString();
            if(!TextUtils.isEmpty(searchKey)) {
                EasyHttp.get("http://" + localhost + ":8080/NewsMaven/selectNews?title=" + searchKey)
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
                                            News news = GsonUtils.fromLocalJson(obj.toString(), News.class);
                                            list.add(news);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    newsItemListAdapter = new NewsItemListAdapter(list);
                                    viewDataBinding.recyclerView.setAdapter(newsItemListAdapter);
                                } else ToastUtil.show(getContext(), "没有哦~");
                            }
                        });
            }
        });
        viewDataBinding.ivMsg.setOnClickListener(v ->  ARouter.getInstance().build("/home/message").navigation());
    }


    @Override
    protected void loadData() {
        String[] tabname={"推荐","国内","国际","娱乐","体育","军事","科技"};
        for(int i=1;i<8;i++){
            fragments.add(NewsItemListFragment.newInstance(HttpURL.HTTP_REQUEST_URL +i));
        }
        pageAdapter = new BaseFragmentPageAdapter(getChildFragmentManager(), fragments,tabname);
        viewDataBinding.viewpager.setAdapter(pageAdapter);
        viewDataBinding.tabLayout.setupWithViewPager(viewDataBinding.viewpager);
    }

    @Override
    public void initListener() {
        findViewById(R.id.tv_seacher).setOnClickListener(this);
        findViewById(R.id.tv_close_search).setOnClickListener(this);
        viewDataBinding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //position为0的时候才能打开搜索界面
                if (position == 0){
                    unlockDrawer();
                }else {
                    lockDrawer();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewDataBinding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                viewDataBinding.lltNews.scrollTo((int) (-mDisplayWidth * slideOffset * 0.8),0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //mMainActivity.hideBottomBarLayout();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //mMainActivity.showBottomBarLayout();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) getActivity();
    }

    private void lockDrawer(){
        viewDataBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private void unlockDrawer(){
        viewDataBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_seacher:
                viewDataBinding.drawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.tv_close_search:
                viewDataBinding.drawerLayout.closeDrawer(Gravity.START);
                viewDataBinding.etSearch.setText(null);
                if(list.size()!=0){
                    list.clear();
                    viewDataBinding.recyclerView.removeAllViews();
                    newsItemListAdapter =new NewsItemListAdapter(list);
                    viewDataBinding.recyclerView.setAdapter(newsItemListAdapter);
                }
                break;
        }
    }


}