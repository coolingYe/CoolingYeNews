package com.example.coolingyenews.widget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.coolingyenews.R;
import com.google.android.material.textview.MaterialTextView;

public class ImageTextView extends RelativeLayout {
    private ImageView topIcon;
    private MaterialTextView bottomTitle;
    @SuppressLint("WrongViewCast")
    public ImageTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }
    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public ImageTextView(Context context) {
        super(context);
        init(context,null);
    }

    private void init(Context context,AttributeSet attrs){
        if(attrs==null){
            return;
        }
        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.ImageTextView);
        LayoutInflater.from(context).inflate(R.layout.base_image_text_view, this);
        topIcon =findViewById(R.id.iv_icon_top);
        bottomTitle=findViewById(R.id.tv_title_bottom);
        topIcon.setBackground(ta.getDrawable(R.styleable.ImageTextView_Top_icon));//设置左侧图标
        bottomTitle.setText(ta.getString(R.styleable.ImageTextView_Bottom_text));//设置左侧标题文字
        ta.recycle();
    }
    //设置左侧图标
    public void setTopIcon(int value) {
        Drawable drawable=getResources().getDrawable(value);
        topIcon.setBackground(drawable);
    }

    //设置左侧标题文字
    public void setBottomTitle(String value) {
        bottomTitle.setText(value);
    }
}
