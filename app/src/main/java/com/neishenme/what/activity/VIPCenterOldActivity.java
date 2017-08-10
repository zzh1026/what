package com.neishenme.what.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.application.App;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.CheckVip;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.view.CircleImageView;

import java.util.HashMap;

/**
 * 旧的会员中心界面,已弃用,新的会员中心界面见 @{@link VIPCenterActivity}
 */
@Deprecated
public class VIPCenterOldActivity extends BaseActivity implements HttpLoader.ResponseListener {
    private RelativeLayout rlSignOne;
    private ImageView ivBack;
    private RelativeLayout rlSignTwo;
    private FrameLayout flUserLogo;
    private CircleImageView userHeadItem;
    private LinearLayout llSignTwo;
    private TextView tvUserName;
    private ImageView ivGenderIcon;
    private TextView VipTimeDistance;
    private Button btnBuyVip;
    private LinearLayout llSignThree;
    private LinearLayout llSignFour;
    private TextView tvPreviewOrder;
    private TextView tvPreviewInsertOrder;
    private TextView tvSignThree;
    private View vSignTwo;
    private View vSignThree;
    private View vSignFive;

    private ImageView ivSignOne;
    private ImageView ivSignTwo;
    private ImageView ivSignThree;
    private ImageView ivSignFour;


    @Override
    protected int initContentView() {
        return R.layout.activity_vipcenter_old;
    }

    @Override
    protected void initView() {
        rlSignOne = (RelativeLayout) findViewById(R.id.rl_sign_one);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        rlSignTwo = (RelativeLayout) findViewById(R.id.rl_sign_two);
        flUserLogo = (FrameLayout) findViewById(R.id.fl_user_logo);
        userHeadItem = (CircleImageView) findViewById(R.id.user_head_item);
        llSignTwo = (LinearLayout) findViewById(R.id.ll_sign_two);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        ivGenderIcon = (ImageView) findViewById(R.id.iv_gender_icon);
        VipTimeDistance = (TextView) findViewById(R.id.Vip_time_distance);
        btnBuyVip = (Button) findViewById(R.id.btn_buy_vip);
        llSignThree = (LinearLayout) findViewById(R.id.ll_sign_three);
        llSignFour = (LinearLayout) findViewById(R.id.ll_sign_four);
        tvPreviewOrder = (TextView) findViewById(R.id.tv_preview_order);
        tvPreviewInsertOrder = (TextView) findViewById(R.id.tv_preview_insert_order);
        tvSignThree = (TextView) findViewById(R.id.tv_sign_three);
        vSignTwo = (View) findViewById(R.id.v_sign_two);
        vSignThree = (View) findViewById(R.id.v_sign_three);
        vSignFive = (View) findViewById(R.id.v_sign_five);
        ivSignOne = (ImageView) findViewById(R.id.iv_sign_one);
        ivSignTwo = (ImageView) findViewById(R.id.iv_sign_two);
        ivSignThree = (ImageView) findViewById(R.id.iv_sign_three);
        ivSignFour = (ImageView) findViewById(R.id.iv_sign_four);
    }

    @Override
    protected void initListener() {
        btnBuyVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VIPCenterOldActivity.this, BuyVipActivity.class);
                startActivity(intent);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VIPCenterOldActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        String thumbnailslogo = App.USERSP.getString("thumbnailslogo", null);
        if (!TextUtils.isEmpty(thumbnailslogo)) {
            HttpLoader.getImageLoader().get(App.USERSP.getString("thumbnailslogo", null), ImageLoader
                    .getImageListener(userHeadItem, R.drawable.picture_moren, R.drawable.picture_moren));
        }
        tvUserName.setText(App.USERSP.getString("name", ""));
        ivGenderIcon.setImageResource(App.USERSP.getString("gender", "").equals("1") ? R.drawable.man_icon :
                R.drawable.woman_icon);
    }

    @Override
    protected void onStart() {
        super.onStart();
        HashMap params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_CHECK_VIP, params, CheckVip.class,
                ConstantsWhatNSM.REQUEST_CHECK_VIP, this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CHECK_VIP
                && response instanceof CheckVip) {
            CheckVip mCheckVip = (CheckVip) response;
            if (mCheckVip.getCode() == 1) {
                CheckVip.DataBean dataBean = mCheckVip.getData();
                long timeDistance = (dataBean.getViptime() - TimeUtils.getCurrentTimeInLong()) / 1000;
                if (timeDistance > 0) {
                    int day = (int) (timeDistance / 86400);
                    VipTimeDistance.setText("会员还有" + day + "天到期");
                    if (day == 0) {
                        double hour = Math.floor(timeDistance % 86400 / 3600);
                        VipTimeDistance.setText("会员还有" + hour + "小时到期");
                    }
                } else {
                    btnBuyVip.setVisibility(View.VISIBLE);
                    VipTimeDistance.setText("您尚未开通会员");
                    btnBuyVip.setText("购买会员");
                }

                if (dataBean.getVip().getInviteLastTime() - TimeUtils.getCurrentTimeInLong() > 0) {
                    tvPreviewOrder.setText(dataBean.getVip().getInviteTimes() + "");
                } else {
                    tvPreviewOrder.setText("0");
                }
                if (dataBean.getVip().getJoinLastTime() - TimeUtils.getCurrentTimeInLong() > 0) {
                    tvPreviewInsertOrder.setText(dataBean.getVip().getJoinTimes() + "");
                } else {
                    tvPreviewInsertOrder.setText("0");
                }

            }

        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }
}
