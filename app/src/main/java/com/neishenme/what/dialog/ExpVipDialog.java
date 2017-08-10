package com.neishenme.what.dialog;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.neishenme.what.R;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.application.App;

/**
 * 作者：zhaozh create on 2016/6/16 15:03
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class ExpVipDialog extends BaseDialog {
    private ImageView mExpVipCancle;
    private ImageView mExpVipContent;
    private ImageView mExpVipUes;
    private MainActivity mContext;

    public ExpVipDialog(MainActivity mContext) {
        super(mContext, Gravity.CENTER, 0.0f, AnimationDirection.GROW, false, false);
        this.mContext = mContext;
        setContentView(R.layout.dialog_exp_vip);
        initView();
        initListener();
    }

    private void initView() {
        mExpVipCancle = (ImageView) findViewById(R.id.exp_vip_cancle);
        mExpVipContent = (ImageView) findViewById(R.id.exp_vip_content);
        mExpVipUes = (ImageView) findViewById(R.id.exp_vip_ues);

    }

    private void initListener() {
        mExpVipCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mExpVipUes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mContext.expVip();
            }
        });
    }

    @Override
    public void dismiss() {
//        App.EDIT.remove("is_regest_user").commit();
        super.dismiss();
    }
}
