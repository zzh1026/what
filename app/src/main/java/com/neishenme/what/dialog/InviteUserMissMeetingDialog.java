package com.neishenme.what.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.view.SmoothCheckBox;

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
public class InviteUserMissMeetingDialog extends BaseDialog {
    private Activity mContext;
    private int type;
    private OnMissmeetingItemSelected missmeetingItemSelected;

    public static final int INVITE_TYPE_NORMAL = 0;
    public static final int INVITE_TYPE_QUICK = 1;

    private TextView mMissmeetingItem1;
    private TextView mMissmeetingItem2;
    private TextView mMissmeetingItem3;
    private CheckBox mMissmeetingChecked;

    public InviteUserMissMeetingDialog(Activity mContext, int type, OnMissmeetingItemSelected missmeetingItemSelected) {
        super(mContext, Gravity.CENTER, 0.0f, AnimationDirection.GROW, true, true);
        this.mContext = mContext;
        this.type = type;
        this.missmeetingItemSelected = missmeetingItemSelected;
        setContentView(R.layout.dialog_user_missmeet);
        initView();
        initListener();
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
        mMissmeetingItem1 = (TextView) findViewById(R.id.missmeeting_item_1);
        mMissmeetingItem2 = (TextView) findViewById(R.id.missmeeting_item_2);
        mMissmeetingItem3 = (TextView) findViewById(R.id.missmeeting_item_3);
        mMissmeetingChecked = (CheckBox) findViewById(R.id.missmeeting_checked);
    }

    private void initListener() {
        mMissmeetingItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                missmeetingItemSelected.onMissmeetingItemSelected(0, type, mMissmeetingChecked.isChecked());
                dismiss();
            }
        });

        mMissmeetingItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                missmeetingItemSelected.onMissmeetingItemSelected(1, type, mMissmeetingChecked.isChecked());
                dismiss();
            }
        });

        mMissmeetingItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                missmeetingItemSelected.onMissmeetingItemSelected(2, type, mMissmeetingChecked.isChecked());
                dismiss();
            }
        });
    }

    private void initData() {
        if (type == INVITE_TYPE_NORMAL) {    //专属发布
//            mMissmeetingItem2.setText(mContext.getResources().getText(R.string.user_miss_meeting_eat));
            mMissmeetingItem3.setVisibility(View.VISIBLE);
            mMissmeetingItem3.setClickable(true);
        } else {    //极速发布
            mMissmeetingItem2.setText(mContext.getResources().getText(R.string.user_miss_meeting_cancle));
            mMissmeetingItem3.setVisibility(View.INVISIBLE);
            mMissmeetingItem3.setClickable(false);
        }
    }

    public static interface OnMissmeetingItemSelected {
        void onMissmeetingItemSelected(int item, int type, boolean checked);
    }
}
