package com.neishenme.what.dialog;

import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.activity.ActiveVoteSharedActivity;
import com.neishenme.what.activity.VIPCenterActivity;
import com.neishenme.what.adapter.VoteSharedAdapter;
import com.neishenme.what.bean.GetVoteRankResponse;
import com.neishenme.what.bean.VipGetActiveResponse;
import com.neishenme.what.view.RubberView;

/**
 * 作者：zhaozh create on 2017/1/7 15:03
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个  网约女明星付款成功后抽奖 的弹窗
 * .
 * 其作用是 :
 */
public class ActiveDateStarLuckyDialog extends BaseDialog {
    private ActiveVoteSharedActivity mContext;
    private GetVoteRankResponse.DataBean.PrizeBean mLuckyPrize;

    private ImageView mDialogVoteSharedCancel;
    private RubberView mDialogVoteSharedLuckyPrize;
    private TextView mDialogVoteSharedLuckyContent;
    private ImageView mDialogVoteSharedGetPrize;

    public ActiveDateStarLuckyDialog(ActiveVoteSharedActivity mContext,
                                     GetVoteRankResponse.DataBean.PrizeBean prize) {
        super(mContext, Gravity.CENTER, 0.0f, AnimationDirection.GROW, false, false);
        this.mContext = mContext;
        this.mLuckyPrize = prize;
        setContentView(R.layout.dialog_active_date_star);
        initView();
        initData();
    }

    private void initView() {
        mDialogVoteSharedCancel = (ImageView) findViewById(R.id.dialog_vote_shared_cancel);
        mDialogVoteSharedLuckyPrize = (RubberView) findViewById(R.id.dialog_vote_shared_lucky_prize);
        mDialogVoteSharedLuckyContent = (TextView) findViewById(R.id.dialog_vote_shared_lucky_content);
        mDialogVoteSharedGetPrize = (ImageView) findViewById(R.id.dialog_vote_shared_get_prize);
    }

    private void initData() {
        if (mLuckyPrize.getActivityLotteryDetailId() == -1) {
            mDialogVoteSharedGetPrize.setEnabled(false);
        } else {
            mDialogVoteSharedGetPrize.setEnabled(true);
        }

        mDialogVoteSharedLuckyContent.setText(mLuckyPrize.getPrizeName());
        mDialogVoteSharedLuckyPrize.setStrokeWidth(70);
        mDialogVoteSharedLuckyPrize.setMaskImage(BitmapFactory.decodeResource(
                mContext.getResources(), R.drawable.dialog_vip_lucky_content_overlay));

        mDialogVoteSharedGetPrize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getAcceptPrize(mLuckyPrize.getActivityLotteryDetailId());
                dismiss();
            }
        });

        mDialogVoteSharedCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
