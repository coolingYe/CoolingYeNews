package com.example.coolingyenews.base.loadsir;

import com.example.coolingyenews.R;
import com.kingja.loadsir.callback.Callback;

/**
 * 应用模块:
 * <p>
 * 类描述: 网络超时
 * <p>
 *
 * @author darryrzhoong
 * @since 2020-01-27
 */
public class TimeoutCallback extends Callback
{
    @Override
    protected int onCreateView()
    {
        return R.layout.base_layout_timeout;
    }
    
}
