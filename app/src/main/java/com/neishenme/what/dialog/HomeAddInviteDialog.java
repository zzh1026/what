package com.neishenme.what.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.neishenme.what.R;
import com.neishenme.what.activity.EditPayMoneyActivity;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.bean.HomeJoinLogoListResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.fragment.HomeInviteFragment;
import com.neishenme.what.net.HttpLoader;

import java.util.List;

/**
 * 作者：zhaozh create on 2016/6/16 15:03
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个活动戳我约她的dialog界面
 * .
 * 其作用是 :
 */
public class HomeAddInviteDialog extends BaseDialog implements HttpLoader.ResponseListener {
    private MainActivity mContext;
    private HomeInviteFragment homeInviteFragment;
    private List<HomeJoinLogoListResponse.DataBean.InvitesBean> invites;

    public static final int SHOW_IMAGE_1 = 0;
    public static final int SHOW_IMAGE_2 = 1;
    public static final int SHOW_IMAGE_3 = 2;
    public static final int SHOW_PIC_1 = 3;
    public static final int SHOW_PIC_2 = 4;
    public static final int SHOW_TEXT = 5;

    public static final int SHOW_IMAGE_TIME = 300;
    public static final int SHOW_PIC_TIME_DROP = 500;
    public static final int SHOW_PIC_TIME_ROTATE = 400;
    public static final int SHOW_TEXT_TIME = 300;

    private LinearLayout mAddInviteFree;
    private ImageView mAddAnimPic1;
    private TextView mAddAnimText1;
    private LinearLayout mAddInviteMoney;
    private ImageView mAddAnimPic2;
    private TextView mAddAnimText2;
    private TextView mAddAnimText3;
    private ImageView mAddInviteJoin1;
    private ImageView mAddInviteJoin2;
    private ImageView mAddInviteJoin3;
    private ImageView mAddInviteCancel;

//    private BlurLayout mBlurLayout;

    private YoYo.AnimationComposer showImage;
    private YoYo.AnimationComposer showPic_1;
    private YoYo.AnimationComposer showPic_2;
    private YoYo.AnimationComposer showText;

    private ImageView addInviteJoin11;
    private ImageView addInviteJoin21;
    private ImageView addInviteJoin31;

    private TextView addInviteJoin12;
    private TextView addInviteJoin22;
    private TextView addInviteJoin32;

    private RelativeLayout layout1;
    private RelativeLayout layout2;
    private RelativeLayout layout3;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_IMAGE_1:
                    showImage = YoYo
                            .with(Techniques.FadeIn)
                            .duration(SHOW_IMAGE_TIME);
                    showImage.playOn(mAddInviteJoin1);
                    showImage.playOn(addInviteJoin11);
                    showImage.playOn(addInviteJoin12);
                    break;
                case SHOW_IMAGE_2:
                    showImage = YoYo
                            .with(Techniques.FadeIn)
                            .duration(SHOW_IMAGE_TIME);
                    showImage.playOn(mAddInviteJoin2);
                    showImage.playOn(addInviteJoin21);
                    showImage.playOn(addInviteJoin22);
                    break;
                case SHOW_IMAGE_3:
                    showImage = YoYo
                            .with(Techniques.FadeIn)
                            .duration(SHOW_IMAGE_TIME);
                    showImage.playOn(mAddInviteJoin3);
                    showImage.playOn(addInviteJoin31);
                    showImage.playOn(addInviteJoin32);
                    break;
                case SHOW_PIC_1:
                    showPic_1 = YoYo
                            .with(Techniques.DropOut)
                            .duration(SHOW_PIC_TIME_DROP);
                    showPic_2 = YoYo
                            .with(Techniques.RotateIn)
                            .duration(SHOW_PIC_TIME_ROTATE);
                    showPic_1.playOn(mAddAnimPic1);
                    showPic_2.playOn(mAddAnimPic1);
                    break;
                case SHOW_PIC_2:
                    showPic_1 = YoYo
                            .with(Techniques.DropOut)
                            .duration(SHOW_PIC_TIME_DROP);
                    showPic_2 = YoYo
                            .with(Techniques.RotateIn)
                            .duration(SHOW_PIC_TIME_ROTATE);
                    showPic_1.playOn(mAddAnimPic2);
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

    public HomeAddInviteDialog(MainActivity mContext, HomeInviteFragment homeInviteFragment, List<HomeJoinLogoListResponse.DataBean.InvitesBean> invites) {
        super(mContext, Gravity.CENTER, 0.0f, AnimationDirection.GROW, false, false);
        this.mContext = mContext;
        this.homeInviteFragment = homeInviteFragment;
        this.invites = invites;
        setContentView(R.layout.dialog_home_add_invite);
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
        mAddInviteFree = (LinearLayout) findViewById(R.id.add_invite_free);
        mAddInviteMoney = (LinearLayout) findViewById(R.id.add_invite_money);
        mAddInviteCancel = (ImageView) findViewById(R.id.add_invite_cancel);

        mAddAnimPic1 = (ImageView) findViewById(R.id.add_anim_pic_1);
        mAddAnimPic2 = (ImageView) findViewById(R.id.add_anim_pic_2);

        mAddAnimText1 = (TextView) findViewById(R.id.add_anim_text_1);
        mAddAnimText2 = (TextView) findViewById(R.id.add_anim_text_2);
        mAddAnimText3 = (TextView) findViewById(R.id.add_anim_text_3);

        mAddInviteJoin1 = (ImageView) findViewById(R.id.add_invite_join_1);
        mAddInviteJoin2 = (ImageView) findViewById(R.id.add_invite_join_2);
        mAddInviteJoin3 = (ImageView) findViewById(R.id.add_invite_join_3);

        addInviteJoin11 = (ImageView) findViewById(R.id.add_invite_join_1_1);
        addInviteJoin21 = (ImageView) findViewById(R.id.add_invite_join_2_1);
        addInviteJoin31 = (ImageView) findViewById(R.id.add_invite_join_3_1);

        addInviteJoin12 = (TextView) findViewById(R.id.add_invite_join_1_2);
        addInviteJoin22 = (TextView) findViewById(R.id.add_invite_join_2_2);
        addInviteJoin32 = (TextView) findViewById(R.id.add_invite_join_3_2);

        layout1 = (RelativeLayout) findViewById(R.id.layout_1);
        layout2 = (RelativeLayout) findViewById(R.id.layout_2);
        layout3 = (RelativeLayout) findViewById(R.id.layout_3);

//        mBlurLayout = (BlurLayout) findViewById(R.id.blurLayout);
    }

    private void initListener() {
        mAddInviteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mAddInviteFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeInviteFragment.onMoneyEdit(null);
                dismiss();
            }
        });

        mAddInviteMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPayMoneyActivity.startEditPayMoneyForResult(mContext,
                        MainActivity.REQUEST_CODE_JOIN_INVITE_PRICE, EditPayMoneyActivity.PAY_MONEY_FLAG_JOIN);
                dismiss();
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mContext.blurCancel();
            }
        });
    }


    private void showAnim() {
        int time = 200;
        mHandler.sendEmptyMessageDelayed(SHOW_IMAGE_1, time);
        mHandler.sendEmptyMessageDelayed(SHOW_IMAGE_2, time + 200);
        mHandler.sendEmptyMessageDelayed(SHOW_IMAGE_3, time + 400);
        mHandler.sendEmptyMessageDelayed(SHOW_PIC_1, time + 300);
        mHandler.sendEmptyMessageDelayed(SHOW_TEXT, time + 600);
        mHandler.sendEmptyMessageDelayed(SHOW_PIC_2, time + 500);
    }

    private void initData() {
        HomeJoinLogoListResponse.DataBean.InvitesBean invitesBean;
        if (invites != null && invites.size() != 0) {
            switch (invites.size()) {
                case 0:
                    layout1.setVisibility(View.INVISIBLE);
                case 1:
                    layout2.setVisibility(View.INVISIBLE);
                case 2:
                    layout3.setVisibility(View.INVISIBLE);
                    break;
            }
            for (int i = 0; i < invites.size(); i++) {
                switch (i) {
                    case 0:
                        invitesBean = invites.get(i);
                        HttpLoader.getImageLoader().get(invitesBean.getThumbnailslogo(), ImageLoader.getImageListener(
                                mAddInviteJoin1, R.drawable.picture_moren, R.drawable.picture_moren));
                        if (invitesBean.getVip_type() == 0) {
                            addInviteJoin11.setVisibility(View.GONE);
                        }
                        int joinPrice = (int) invitesBean.getJoinPrice();
                        if (joinPrice != 0) {
                            addInviteJoin12.setText(joinPrice + "元");
                        } else {
                            addInviteJoin12.setVisibility(View.GONE);
                        }
                        break;
                    case 1:
                        invitesBean = invites.get(i);
                        HttpLoader.getImageLoader().get(invitesBean.getThumbnailslogo(), ImageLoader.getImageListener(
                                mAddInviteJoin2, R.drawable.picture_moren, R.drawable.picture_moren));
                        if (invitesBean.getVip_type() == 0) {
                            addInviteJoin21.setVisibility(View.GONE);
                        }
                        int joinPrices = (int) invitesBean.getJoinPrice();
                        if (joinPrices != 0) {
                            addInviteJoin22.setText(joinPrices + "元");
                        } else {
                            addInviteJoin22.setVisibility(View.GONE);
                        }
                        break;
                    case 2:
                        invitesBean = invites.get(i);
                        HttpLoader.getImageLoader().get(invitesBean.getThumbnailslogo(), ImageLoader.getImageListener(
                                mAddInviteJoin3, R.drawable.picture_moren, R.drawable.picture_moren));
                        if (invitesBean.getVip_type() == 0) {
                            addInviteJoin31.setVisibility(View.GONE);
                        }
                        int joinPricess = (int) invitesBean.getJoinPrice();
                        if (joinPricess != 0) {
                            addInviteJoin32.setText(joinPricess + "元");
                        } else {
                            addInviteJoin32.setVisibility(View.GONE);
                        }
                        break;
                }
            }
        } else {
            layout3.setVisibility(View.INVISIBLE);
            layout2.setVisibility(View.INVISIBLE);
            layout1.setVisibility(View.INVISIBLE);
        }
        showAnim();
//        mBlurLayout.invalidate();
//        HttpLoader.getImageLoader().get(HomePersonFragment.url1, ImageLoader.getImageListener(
//                mAddInviteJoin1, R.drawable.picture_moren, R.drawable.picture_moren));
//        HttpLoader.getImageLoader().get(HomePersonFragment.url2, ImageLoader.getImageListener(
//                mAddInviteJoin2, R.drawable.picture_moren, R.drawable.picture_moren));
//        HttpLoader.getImageLoader().get(HomePersonFragment.url3, ImageLoader.getImageListener(
//                mAddInviteJoin3, R.drawable.picture_moren, R.drawable.picture_moren));

    }

    @Override
    public void dismiss() {
        showImage = null;
        showPic_1 = null;
        showPic_2 = null;
        showText = null;
        super.dismiss();
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {

    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

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
