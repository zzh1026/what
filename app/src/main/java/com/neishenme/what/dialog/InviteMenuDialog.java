package com.neishenme.what.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.bean.HomeResponse;
import com.neishenme.what.fragment.InviteFragment;
import com.neishenme.what.utils.NSMTypeUtils;

/**
 * Created by Administrator on 2016/5/11.
 * 该dialog已经弃用 : time:2017/3/1
 */
@Deprecated
public class InviteMenuDialog extends BaseDialog {
    private TextView mTvFocus;
    private TextView mTvJoinDate;
    private TextView mTvInfoTa;
    private TextView mTvPingbiTa;
    private TextView mTvCancel;
    private View mMenuLine1;
    private View mMenuLine2;

    private MainActivity mainActivity;
    private InviteFragment inviteFragment;
    private HomeResponse.DataBean.InvitesBean invitesBean;

    private int joiner_newstatus;

    public InviteMenuDialog(Activity context, InviteFragment inviteFragment, HomeResponse.DataBean.InvitesBean invitesBean) {
        super(context, Gravity.BOTTOM, 0.0f, AnimationDirection.VERTICLE, true, true);
        setContentView(R.layout.dialog_invite_menu);
        this.mainActivity = (MainActivity) context;
        this.inviteFragment = inviteFragment;
        this.invitesBean = invitesBean;
        initView();
        initListener();
        initData();
    }

    public void initView() {
        mTvFocus = (TextView) findViewById(R.id.tv_focus);
        mTvJoinDate = (TextView) findViewById(R.id.tv_join_date);
        mTvInfoTa = (TextView) findViewById(R.id.tv_info_ta);
        mTvPingbiTa = (TextView) findViewById(R.id.tv_pingbi_ta);
        mTvCancel = (TextView) findViewById(R.id.tv_cancel);
        mMenuLine1 = (View) findViewById(R.id.menu_line_1);
        mMenuLine2 = (View) findViewById(R.id.menu_line_2);

    }

    private void initListener() {
        mTvFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteFragment.onPopFocusClick(invitesBean);
                dismiss();
            }
        });

        mTvJoinDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (joiner_newstatus == 50 || joiner_newstatus == 100 || joiner_newstatus == 120 || NSMTypeUtils.isMyUserId(String.valueOf(invitesBean.getUser_id()))) {
                    inviteFragment.onPopJoinDateClick(true);
                } else {
                    inviteFragment.onPopJoinDateClick(false);
                }
                dismiss();
            }
        });

        mTvInfoTa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteFragment.onPopInfoTa(invitesBean.getUser_id());
                dismiss();
            }
        });

        mTvPingbiTa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteFragment.onPopPinbbiTa(invitesBean.getInvite_id());
                dismiss();
            }
        });

        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initData() {
        if (NSMTypeUtils.isMyUserId(String.valueOf(invitesBean.getUser_id()))) {
            mTvFocus.setVisibility(View.GONE);
            mTvJoinDate.setText("活动详情");
            mTvInfoTa.setText("我的信息");
            mTvPingbiTa.setVisibility(View.GONE);
            mMenuLine1.setVisibility(View.GONE);
            mMenuLine2.setVisibility(View.GONE);
        } else {
            if (invitesBean.getUserfoucs_state() == 1) {
                mTvFocus.setText("取消关注");
            } else {
                mTvFocus.setText("关注TA");
            }
            joiner_newstatus = invitesBean.getJoiner_newstatus();
            if (joiner_newstatus == 50 || joiner_newstatus == 100 || joiner_newstatus == 120) {
                mTvJoinDate.setText("活动详情");
            } else {
                mTvJoinDate.setText("加入活动");
            }
        }

    }

}
