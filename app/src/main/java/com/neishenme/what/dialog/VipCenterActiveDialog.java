package com.neishenme.what.dialog;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.activity.VIPCenterActivity;
import com.neishenme.what.bean.VipGetActiveResponse;
import com.neishenme.what.view.RubberView;

/**
 * 作者：zhaozh create on 2017/1/7 15:03
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个  新版会员中心 会员点击活动的弹窗
 * .
 * 其作用是 :
 */
public class VipCenterActiveDialog extends BaseDialog {
    private VIPCenterActivity mContext;

    private TextView mVipLuckyTitle;
    private TextView mVipLuckyContent;
    private TextView mVipLuckyDiscribe;
    private TextView mVipLuckyTimes;
    private RubberView mScratchOutView;
    private RelativeLayout vipLuckyLayout;

    private VipGetActiveResponse.DataBean data;

    public VipCenterActiveDialog(VIPCenterActivity mContext, VipGetActiveResponse.DataBean data) {
        super(mContext, Gravity.CENTER, 0.0f, AnimationDirection.GROW, true, true);
        this.mContext = mContext;
        this.data = data;
        setContentView(R.layout.dialog_vip_active);
        initView();
        initData();
    }

    @Override
    protected void init(int gravity, float marginVerticle, AnimationDirection animationDirection, boolean backCancelable, boolean outsideCancelable) {
        super.init(gravity, marginVerticle, animationDirection, backCancelable, outsideCancelable);
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = gravity;
        layoutParams.verticalMargin = marginVerticle;
        dialogWindow.setAttributes(layoutParams);
    }

    private void initView() {
        mVipLuckyTitle = (TextView) findViewById(R.id.vip_Lucky_title);
        mVipLuckyContent = (TextView) findViewById(R.id.vip_Lucky_content);
        mVipLuckyDiscribe = (TextView) findViewById(R.id.vip_Lucky_discribe);
        mVipLuckyTimes = (TextView) findViewById(R.id.vip_Lucky_times);
        mScratchOutView = (RubberView) findViewById(R.id.vip_lucky_scratch);
        vipLuckyLayout = (RelativeLayout) findViewById(R.id.vip_lucky_layout);
    }

    private void initData() {
        mVipLuckyTitle.setText(data.getTitle());
        mVipLuckyContent.setText(data.getPrizeName());
        mVipLuckyDiscribe.setText(data.getPrizeMessage());
        mVipLuckyTimes.setText(data.getCurrentTimes() + "/" + data.getAllTimes());

        mScratchOutView.setStrokeWidth(70);
        mScratchOutView.setMaskImage(Color.parseColor("#444444"));

        vipLuckyLayout.setVisibility(View.VISIBLE);
    }
}
