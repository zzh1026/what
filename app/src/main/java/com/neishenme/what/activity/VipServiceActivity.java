package com.neishenme.what.activity;

import android.view.View;
import android.widget.ImageView;

import com.neishenme.what.R;
import com.neishenme.what.base.BaseActivity;

/**
 * 作者：zhaozh create on 2017/1/6 19:31
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 vip专享服务的 界面
 * .
 * 其作用是 :
 */
public class VipServiceActivity extends BaseActivity {

    private ImageView mVipServiceCancel;

    @Override
    protected int initContentView() {
        return R.layout.activity_vip_service;
    }

    @Override
    protected void initView() {

        mVipServiceCancel = (ImageView) findViewById(R.id.vip_service_cancel);

    }

    @Override
    protected void initListener() {
        mVipServiceCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

}
