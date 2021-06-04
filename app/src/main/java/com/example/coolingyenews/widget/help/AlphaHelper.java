package com.example.coolingyenews.widget.help;

import android.view.MotionEvent;
import android.view.View;

public class AlphaHelper {
    /**
     * 设置view点击效果
     * @param v
     */
    public static void setPassAlpha(View v){
        v.setOnTouchListener(onTouchChangeOpacityListener);
    }

    /**
     * touch监听 触摸事件按下 抬起
     */
    private static View.OnTouchListener onTouchChangeOpacityListener = (v, event) -> {
        if (v.isEnabled()) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    v.setAlpha(0.6f);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.setAlpha(1f);
                    break;
            }
        }
        return false;
    };

}
