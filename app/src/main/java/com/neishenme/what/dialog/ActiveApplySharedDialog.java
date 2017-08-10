package com.neishenme.what.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.nsminterface.SharedDataCallback;

/**
 * 作者：zhaozh create on 2016/6/16 15:03
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个活动戳我约她的界面
 * .
 * 其作用是 :
 */
public class ActiveApplySharedDialog extends BaseDialog implements View.OnClickListener {
    private SharedDataCallback mCallback;

    private ImageView mDialogSharedSina;
    private ImageView mDialogSharedWeichat;
    private ImageView mDialogSharedWeifriend;
    private ImageView mDialogSharedQqfriend;
    private TextView mDialogSharedCancel;


    public ActiveApplySharedDialog(Context mContext, SharedDataCallback mCallback) {
        super(mContext, Gravity.CENTER, 0.0f, AnimationDirection.VERTICLE, true, true);
        this.mCallback = mCallback;
        setContentView(R.layout.dialog_apply_shared_active);
        initView();
        initListener();
    }

    private void initView() {
        mDialogSharedSina = (ImageView) findViewById(R.id.dialog_shared_sina);
        mDialogSharedWeichat = (ImageView) findViewById(R.id.dialog_shared_weichat);
        mDialogSharedWeifriend = (ImageView) findViewById(R.id.dialog_shared_weifriend);
        mDialogSharedQqfriend = (ImageView) findViewById(R.id.dialog_shared_qqfriend);
        mDialogSharedCancel = (TextView) findViewById(R.id.dialog_shared_cancel);
    }

    private void initListener() {
        mDialogSharedSina.setOnClickListener(this);
        mDialogSharedWeichat.setOnClickListener(this);
        mDialogSharedWeifriend.setOnClickListener(this);
        mDialogSharedQqfriend.setOnClickListener(this);
        mDialogSharedCancel.setOnClickListener(this);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_shared_sina:
                mCallback.startShared(SharedDataCallback.SHARE_TO_SINA);
                break;
            case R.id.dialog_shared_weichat:
                mCallback.startShared(SharedDataCallback.SHARE_TO_WEIXIN);
                break;
            case R.id.dialog_shared_weifriend:
                mCallback.startShared(SharedDataCallback.SHARE_TO_WEIXINFRIEND);
                break;
            case R.id.dialog_shared_qqfriend:
                mCallback.startShared(SharedDataCallback.SHARE_TO_QQFRIEND);
                break;
            case R.id.dialog_shared_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }
}
