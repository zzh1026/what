package com.neishenme.what.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.neishenme.what.R;
import com.neishenme.what.activity.EditPayMoneyActivity;
import com.neishenme.what.activity.InviteJoinerDetailActivity;
import com.neishenme.what.view.InviteJoinDealogAnimator;

import org.seny.android.utils.ALog;

/**
 * 作者：zhaozh create on 2016/6/16 15:03
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个点击申请加入后的dialog界面
 * .
 * 其作用是 :
 */
public class InviteDetailJoinDialog extends BaseDialog {
    private InviteJoinerDetailActivity mContext;

    public static final int SHOW_PIC_1 = 3;
    public static final int SHOW_PIC_2 = 4;
    public static final int SHOW_TEXT = 5;

    public static final int SHOW_PIC_TIME = 600;
    public static final int SHOW_TEXT_TIME = 300;

    private ImageView mAddAnimPic1;
    private ImageView mAddAnimPic2;

    private TextView mAddAnimText1;
    private TextView mAddAnimText2;
    private TextView mAddAnimText3;

    private ImageView mAddInviteCancel;

    private YoYo.AnimationComposer showPic_1;
    private YoYo.AnimationComposer showPic_2;
    private YoYo.AnimationComposer showText;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_PIC_1:
                    showPic_1 = YoYo
                            .with(new InviteJoinDealogAnimator(true))
                            .duration(600);
                    showPic_1.playOn(mAddAnimPic1);
                    break;
                case SHOW_PIC_2:
                    showPic_2 = YoYo
                            .with(new InviteJoinDealogAnimator(false))
                            .duration(300);
                    showPic_2.playOn(mAddAnimPic2);
                    break;
                case SHOW_TEXT:
                    showText = YoYo
                            .with(Techniques.FadeIn)
                            .duration(SHOW_TEXT_TIME);
                    showText.playOn(mAddAnimText1);
                    showText.playOn(mAddAnimText2);
                    showText.playOn(mAddAnimText3);
                    break;
                default:
                    break;
            }
        }
    };

    public InviteDetailJoinDialog(InviteJoinerDetailActivity mContext) {
        super(mContext, Gravity.CENTER, 0.0f, AnimationDirection.GROW, false, false);
        this.mContext = mContext;
        setContentView(R.layout.invite_detail_joiner_join);
        initView();
        initListener();
        initData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void initView() {
        mAddAnimPic1 = (ImageView) findViewById(R.id.add_anim_pic_1);
        mAddAnimPic2 = (ImageView) findViewById(R.id.add_anim_pic_2);

        mAddAnimText1 = (TextView) findViewById(R.id.add_anim_text_1);
        mAddAnimText2 = (TextView) findViewById(R.id.add_anim_text_2);
        mAddAnimText3 = (TextView) findViewById(R.id.add_anim_text_3);

        mAddInviteCancel = (ImageView) findViewById(R.id.add_invite_cancel);
    }

    private void initListener() {
        mAddInviteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mAddAnimPic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ALog.i("免费加入");
                mContext.requestJoin(null);
                dismiss();
            }
        });

        mAddAnimPic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPayMoneyActivity.startEditPayMoneyForResult(mContext, EditPayMoneyActivity.REQUEST_CODE_JOIN, EditPayMoneyActivity.PAY_MONEY_FLAG_JOIN);
                dismiss();
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mContext.hideBlurAll();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHandler.sendEmptyMessageDelayed(SHOW_PIC_1, 200);
        mHandler.sendEmptyMessageDelayed(SHOW_TEXT, 400);
        mHandler.sendEmptyMessageDelayed(SHOW_PIC_2, 500);
    }

    private void initData() {

    }

    @Override
    public void dismiss() {
        showPic_1 = null;
        showPic_2 = null;
        showText = null;
        super.dismiss();
    }

    //    private void subMitTicketNum() {
//        HashMap params = new HashMap();
//        params.put("token", NSMTypeUtils.getMyToken());
//        params.put("takeMeOutId", takemeoutBean.getId() + "");
//        params.put("tickets", mEditText + "");
//        ALog.i("takeMeOutId = " + takemeoutBean.getId());
//        ALog.i("tickets = " + mEditText);
//        HttpLoader.post(ConstantsWhatNSM.URL_ACTIVE_TACK_JOIN, params, ActiveJoinerTrideResponse.class,
//                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_TACK_JOIN, this, false).setTag(this);
//    }
//
//    @Override
//    public void onGetResponseSuccess(int requestCode, RBResponse response) {
//        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_TACK_JOIN
//                && response instanceof ActiveJoinerTrideResponse) {
//            ActiveJoinerTrideResponse activeJoinerTrideResponse = (ActiveJoinerTrideResponse) response;
//            if (activeJoinerTrideResponse.getCode() == 1) {
//                ActiveJoinerTrideResponse.DataBean dataBean = activeJoinerTrideResponse.getData();
//                ActivePayTrideBean mActivePayTrideBean = new ActivePayTrideBean("TakeMeOut",
//                        takemeoutBean.getThumbnailslogo(), takemeoutBean.getName(),
//                        dataBean.getTrade().getPrice(), dataBean.getTrade().getJobType(),
//                        dataBean.getTrade().getTradeNum(), mEditText);
//                ActiveJoinPayActivity.startActiveJoinPayAct(mContext, mActivePayTrideBean);
//                dismiss();
//            } else {
//                mContext.showToast(activeJoinerTrideResponse.getMessage());
//            }
//        }
//    }
//
//    @Override
//    public void onGetResponseError(int requestCode, VolleyError error) {
//
//    }
}
