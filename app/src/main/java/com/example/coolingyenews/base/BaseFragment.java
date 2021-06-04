package com.example.coolingyenews.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.example.coolingyenews.R;
import com.github.nukc.stateview.StateView;

import org.greenrobot.eventbus.EventBus;

/**
 * @author ChayChan
 * @description: Fragment的基类
 */

public abstract class BaseFragment<V extends ViewDataBinding> extends LazyLoadFragment implements StateView.OnRetryClickListener{

    protected V viewDataBinding;
    //protected View rootView;
    protected StateView mStateView;//用于显示加载中、网络异常，空布局、内容布局
    protected Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (viewDataBinding == null) {
            viewDataBinding =  DataBindingUtil.inflate(inflater, provideContentViewId(), container, false);

            mStateView = StateView.inject(getStateViewRoot());
            if (mStateView != null){
                mStateView.setLoadingResource(R.layout.view_loading);
                mStateView.setOnRetryClickListener(this);
            }

            initView(viewDataBinding.getRoot());
            initData();
            initListener();
        } else {
            ViewGroup parent = (ViewGroup) viewDataBinding.getRoot().getParent();
            if (parent != null) {
                parent.removeView(viewDataBinding.getRoot());
            }
        }
        return viewDataBinding.getRoot();
    }

    /**StateView的根布局，默认是整个界面，如果需要变换可以重写此方法*/
    public View getStateViewRoot() {
        return viewDataBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    public final <T extends View> T findViewById(int id) {
       return viewDataBinding.getRoot().findViewById(id);
    }

    public void showView(View view){
        if (view!=null&&view.getVisibility()!= View.VISIBLE){
            view.setVisibility(View.VISIBLE);
        }
    }

    public void showView(@IdRes int id){
        View view = viewDataBinding.getRoot().findViewById(id);
        if (view!=null&&view.getVisibility()!= View.VISIBLE){
            view.setVisibility(View.VISIBLE);
        }
    }

    public void hideView(View view){
        if (view!=null&&view.getVisibility()!= View.GONE){
            view.setVisibility(View.GONE);
        }
    }

    public void hideView(@IdRes int id) {
        View view = viewDataBinding.getRoot().findViewById(id);
        if (view != null && view.getVisibility() != View.GONE) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化一些view
     * @param rootView
     */
    public void initView(View rootView) {
    }
    /**
     * 初始化数据
     */
    public void initData() {

    }

    /**
     * 设置listener的操作
     */
    public void initListener() {

    }

    @Override
    protected void onFragmentFirstVisible() {
        //当第一次可见的时候，加载数据
        loadData();
    }

    //得到当前界面的布局文件id(由子类实现)
    protected abstract int provideContentViewId();

    //加载数据
    protected abstract void loadData();

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewDataBinding = null;
    }

    public boolean isEventBusRegisted(Object subscribe) {
        return EventBus.getDefault().isRegistered(subscribe);
    }

    public void registerEventBus(Object subscribe) {
        if (!isEventBusRegisted(subscribe)) {
            EventBus.getDefault().register(subscribe);
        }
    }

    public void unregisterEventBus(Object subscribe) {
        if (isEventBusRegisted(subscribe)) {
            EventBus.getDefault().unregister(subscribe);
        }
    }

    @Override
    public void onRetryClick() {

    }

}
