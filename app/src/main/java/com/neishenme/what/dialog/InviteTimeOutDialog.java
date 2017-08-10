package com.neishenme.what.dialog;

import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.activity.MyTripActivity;
import com.neishenme.what.adapter.InviteTimeOutAdapter;

import java.util.List;

/**
 * 作者：zhaozh create on 2017/3/20 15:03
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个  v5.0.5 超时30分钟以后的弹窗
 * .
 * 其作用是 :
 */
public class InviteTimeOutDialog extends BaseDialog implements View.OnClickListener {
    private MyTripActivity mContext;
    private OnInviteTimeOutCallback mOnInviteTimeOutCallback;
    private List<String> mListReasons;
    private InviteTimeOutAdapter mAdapter;

    private LinearLayout mInviteTimeOutWaitLayout;
    private CheckBox mInviteTimeOutWaitCb;
    private LinearLayout mInviteTimeOutCancelLayout;
    private CheckBox mInviteTimeOutCancelCb;
    private ListView mInviteTimeOutList;
    private TextView mInviteTimeOutSubmit;
    private ImageView mInviteTimeOutCancel;

    public InviteTimeOutDialog(MyTripActivity mContext, List<String> mListReasons, OnInviteTimeOutCallback mOnInviteTimeOutCallback) {
        super(mContext, Gravity.CENTER, 0.0f, AnimationDirection.GROW, false, false);
        this.mContext = mContext;
        this.mOnInviteTimeOutCallback = mOnInviteTimeOutCallback;
        this.mListReasons = mListReasons;
        setContentView(R.layout.dialog_invite_out_time);
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
        mInviteTimeOutWaitLayout = (LinearLayout) findViewById(R.id.invite_time_out_wait_layout);
        mInviteTimeOutWaitCb = (CheckBox) findViewById(R.id.invite_time_out_wait_cb);

        mInviteTimeOutCancelLayout = (LinearLayout) findViewById(R.id.invite_time_out_cancel_layout);
        mInviteTimeOutCancelCb = (CheckBox) findViewById(R.id.invite_time_out_cancel_cb);

        mInviteTimeOutList = (ListView) findViewById(R.id.invite_time_out_list);

        mInviteTimeOutSubmit = (TextView) findViewById(R.id.invite_time_out_submit);
        mInviteTimeOutCancel = (ImageView) findViewById(R.id.invite_time_out_cancel);
    }

    private void initListener() {
        mInviteTimeOutWaitLayout.setOnClickListener(this);
        mInviteTimeOutCancelLayout.setOnClickListener(this);
        mInviteTimeOutSubmit.setOnClickListener(this);
        mInviteTimeOutCancel.setOnClickListener(this);
    }

    private void initData() {
        mAdapter = new InviteTimeOutAdapter(this, mContext, mListReasons);
        mInviteTimeOutList.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invite_time_out_wait_layout:  //再等等
                mInviteTimeOutCancelCb.setChecked(false);
                mInviteTimeOutWaitCb.setChecked(!mInviteTimeOutWaitCb.isChecked());
                refreshReasonLists();
                break;
            case R.id.invite_time_out_cancel_layout:    //取消活动
                mInviteTimeOutWaitCb.setChecked(false);
                mInviteTimeOutCancelCb.setChecked(!mInviteTimeOutCancelCb.isChecked());
                mInviteTimeOutList.setVisibility(View.VISIBLE);
                refreshReasonLists();
                break;
            case R.id.invite_time_out_submit:   //提交
                trySubmit();
                break;
            case R.id.invite_time_out_cancel:   //取消
                inviteTimeOutWait(1, "");
                break;
        }
    }

    private void trySubmit() {
        if (!mInviteTimeOutWaitCb.isChecked() && !mInviteTimeOutCancelCb.isChecked()) {
            showToast("您尚未选中任何选项");
            return;
        }
        if (mInviteTimeOutWaitCb.isChecked()) {
            inviteTimeOutWait(2, "");
            return;
        }
        if (mInviteTimeOutCancelCb.isChecked()) {
            int position = mAdapter.getCurrentPosition();
            if (position == -1) {
                showToast("请选择原因");
                return;
            }
            String reason = mListReasons.get(position);
            inviteTimeOutWait(3, reason);
        }
    }

    public void changeDate(int position) {
        if (position != -1) {
            mInviteTimeOutWaitCb.setChecked(false);
            mInviteTimeOutCancelCb.setChecked(true);
        }
    }

    private void showToast(String toast) {
        mContext.showToastInfo(toast);
    }

    private void refreshReasonLists() {
        mAdapter.setCurrentPosition(-1);
    }

    private void inviteTimeOutWait(int type, String reason) {
        mOnInviteTimeOutCallback.onInviteTimeOutCall(type, reason);
        dismiss();
    }

    public static interface OnInviteTimeOutCallback {
        void onInviteTimeOutCall(int type, String reason);
    }
}
