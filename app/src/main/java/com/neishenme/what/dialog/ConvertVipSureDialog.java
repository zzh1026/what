package com.neishenme.what.dialog;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.neishenme.what.R;
import com.neishenme.what.activity.ConvertVipActivity;
import com.neishenme.what.activity.MyWalletActivity;

/**
 * 这个类的作用是: 兑换会员的确认弹窗
 * <p>
 * Created by zhaozh on 2017/5/17.
 */

public class ConvertVipSureDialog extends BaseDialog {
    private MyWalletActivity context;

    private ImageView mConvertVipCancel;
    private ImageView mConvertVipSure;

    public ConvertVipSureDialog(MyWalletActivity context) {
        super(context, Gravity.CENTER, 0.0f, AnimationDirection.GROW, false, false);
        this.context = context;
        setContentView(R.layout.dialog_convert_vip_sure);
        initView();
        initListener();
    }

    private void initView() {
        mConvertVipCancel = (ImageView) findViewById(R.id.convert_vip_cancel);
        mConvertVipSure = (ImageView) findViewById(R.id.convert_vip_sure);
    }

    private void initListener() {
        mConvertVipCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mConvertVipSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConvertVipActivity.startConvertVipActForResult(
                        context, MyWalletActivity.REQUEST_CODE_CONVERT_VIP);
                dismiss();
            }
        });
    }
}
