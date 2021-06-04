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
import android.widget.TextView;

import com.example.coolingyenews.R;
import com.google.android.material.textview.MaterialTextView;

public class ItemView extends RelativeLayout {
    private boolean isShowBottomLine,isShowLeftIcon,isShowRightArrow;
    private ImageView leftIcon,rightArrow;
    private MaterialTextView leftTitle,rightDesc;
    private View bottomLine;
    private View inflate;
    @SuppressLint("WrongViewCast")
    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }
    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public ItemView(Context context) {
        super(context);
        init(context,null);
    }

    private void init(Context context,AttributeSet attrs){
        if(attrs==null){
            return;
        }
        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.ItemView);
        isShowBottomLine = ta.getBoolean(R.styleable.ItemView_show_bottom_line, true);//得到是否显示底部下划线属性
        isShowLeftIcon = ta.getBoolean(R.styleable.ItemView_show_left_icon, true);//得到是否显示左侧图标属性标识
        isShowRightArrow = ta.getBoolean(R.styleable.ItemView_show_right_arrow, true);//得到是否显示右侧图标属性标识
        LayoutInflater.from(context).inflate(R.layout.base_item_view, this);
        leftIcon =findViewById(R.id.iv_icon_);
        rightArrow =findViewById(R.id.iv_right);
        leftTitle=findViewById(R.id.tv_left_title);
        rightDesc=findViewById((R.id.tv_right_desc));
        bottomLine =findViewById(R.id.v_bottom_line);
        leftIcon.setBackground(ta.getDrawable(R.styleable.ItemView_left_icon));//设置左侧图标
        leftTitle.setText(ta.getString(R.styleable.ItemView_left_text));//设置左侧标题文字
        rightDesc.setText(ta.getString(R.styleable.ItemView_right_text));//设置右侧文字描述
        ta.recycle();
        leftIcon.setVisibility(isShowLeftIcon ? View.VISIBLE : View.INVISIBLE);//设置左侧箭头图标是否显示
        bottomLine.setVisibility(isShowBottomLine ? View.VISIBLE : View.INVISIBLE);//设置底部图标是否显示
        rightArrow.setVisibility(isShowRightArrow ? View.VISIBLE : View.INVISIBLE);//设置右侧箭头图标是否显示
    }
    //设置左侧图标
    public void setLeftIcon(int value) {
        Drawable drawable=getResources().getDrawable(value);
        leftIcon.setBackground(drawable);
    }

    //设置左侧标题文字
    public void setLeftTitle(String value) {
        leftTitle.setText(value);
    }

    //设置右侧描述文字
    public void setRightDesc(String value) {
        rightDesc.setText(value);
    }
    //设置右侧箭头
    public void setShowRightArrow(boolean value) {
        rightArrow.setVisibility(value ? View.VISIBLE : View.INVISIBLE);//设置右侧箭头图标是否显示
    }

    //设置是否显示下画线
    public void setShowBottomLine(boolean value) {
        bottomLine.setVisibility(value ? View.VISIBLE : View.INVISIBLE);//设置右侧箭头图标是否显示
    }
}
