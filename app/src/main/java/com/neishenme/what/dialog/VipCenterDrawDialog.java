package com.neishenme.what.dialog;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
public class VipCenterDrawDialog extends BaseDialog {
    private VIPCenterActivity mContext;

    private RelativeLayout mDialogVipLuckyLayout;
    private TextView mDialogVipLuckyTimes;
    private TextView mDialogVipLuckyContent;
    private RubberView mDialogVipLuckyScratch;
    private ImageView mDialogVipLuckyCommit;

    private VipGetActiveResponse.DataBean data;

    public VipCenterDrawDialog(VIPCenterActivity mContext, VipGetActiveResponse.DataBean data) {
        super(mContext, Gravity.CENTER, 0.0f, AnimationDirection.GROW, false, false);
        this.mContext = mContext;
        this.data = data;
        setContentView(R.layout.dialog_vip_draw);
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
        mDialogVipLuckyLayout = (RelativeLayout) findViewById(R.id.dialog_vip_lucky_layout);
        mDialogVipLuckyTimes = (TextView) findViewById(R.id.dialog_vip_lucky_times);
        mDialogVipLuckyContent = (TextView) findViewById(R.id.dialog_vip_lucky_content);
        mDialogVipLuckyScratch = (RubberView) findViewById(R.id.dialog_vip_lucky_scratch);
        mDialogVipLuckyCommit = (ImageView) findViewById(R.id.dialog_vip_lucky_commit);
    }

    private void initData() {
        mDialogVipLuckyContent.setText(data.getPrizeName());
        mDialogVipLuckyTimes.setText("您还有" + (data.getAllTimes() - data.getCurrentTimes()) + "次机会");

        mDialogVipLuckyScratch.setStrokeWidth(70);
        mDialogVipLuckyScratch.setMaskImage(BitmapFactory.decodeResource(
                mContext.getResources(), R.drawable.dialog_vip_lucky_content_overlay));

        mDialogVipLuckyLayout.setVisibility(View.VISIBLE);

        mDialogVipLuckyCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
