package com.neishenme.what.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neishenme.what.R;

/**
 * Created by Administrator on 2017/3/16.
 */

/**
 * 马伟
 * 自定义title，避免重复代码
 */
public class AllTitleView extends LinearLayout {
    private ImageView iv_left_back; //左边返回按钮
    public TextView tv_title;      //中间title文字
    public ImageView iv_right;     //右边图片

    public AllTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title,this);
        iv_left_back = (ImageView)findViewById(R.id.iv_left_back);
        tv_title = (TextView)findViewById(R.id.tv_title);
        iv_right = (ImageView)findViewById(R.id.iv_right);

        initListener();
    }

    private void initListener() {
        //返回监听事件
        iv_left_back.setOnClickListener(backlistener);
    }


    OnClickListener backlistener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            ((Activity)getContext()).finish();
        }
    };
}
