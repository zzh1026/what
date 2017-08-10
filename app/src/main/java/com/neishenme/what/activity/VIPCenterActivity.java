package com.neishenme.what.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.application.App;
import com.neishenme.what.baidumap.utils.LocationToBaiduMap;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.CheckVip;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.VipGetActiveResponse;
import com.neishenme.what.dialog.VipCenterDrawDialog;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.view.CircleImageView;
import com.neishenme.what.view.CustomScrollView;

import org.seny.android.utils.ALog;

import java.util.HashMap;

/**
 * 会员中心界面 , 由主界面 菜单的 会员中心进入, 主要是查看会员的特权和会员开通续费抽奖等功能
 */
public class VIPCenterActivity extends BaseActivity implements HttpLoader.ResponseListener {
    private CircleImageView mVipCenterUserLogo;
    private ImageView mVipCenterCrownImg;
    private ImageView mVipCenterVipImg;
    private TextView mVipCenterUserName;
    private ImageView mVipCenterUserGender;
    private TextView mVipCenterTimeDistence;
    private Button mVipCenterBuyVip;
    private ImageView mVipCenterBack;
    private CustomScrollView mVipCenterScrollLayout;
    private View mVipCenterTitleBar;
    private ImageView mVipCenterActivite;

    private boolean canClick = true;
    private String message;

    @Override
    protected int initContentView() {
        return R.layout.activity_vipcenter;
    }

    @Override
    protected void initView() {
        mVipCenterScrollLayout = (CustomScrollView) findViewById(R.id.vip_center_scroll_layout);
        mVipCenterBack = (ImageView) findViewById(R.id.vip_center_back);
        mVipCenterTitleBar = findViewById(R.id.vip_center_title_bar);

        mVipCenterUserLogo = (CircleImageView) findViewById(R.id.vip_center_user_logo);
        mVipCenterUserName = (TextView) findViewById(R.id.vip_center_user_name);
        mVipCenterUserGender = (ImageView) findViewById(R.id.vip_center_user_gender);
        mVipCenterTimeDistence = (TextView) findViewById(R.id.vip_center_time_distence);

        mVipCenterCrownImg = (ImageView) findViewById(R.id.vip_center_crown_img);
        mVipCenterVipImg = (ImageView) findViewById(R.id.vip_center_vip_img);

        mVipCenterActivite = (ImageView) findViewById(R.id.vip_center_activite);
        mVipCenterBuyVip = (Button) findViewById(R.id.vip_center_buy_vip);
    }

    @Override
    protected void initListener() {
        mVipCenterBuyVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toBuyVip();
            }
        });
        mVipCenterBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mVipCenterScrollLayout.setScrollYChangedListener(new CustomScrollView.ScrollYChangedListener() {
            @Override
            public void scrollYChange(int y) {
                if (y < 0) {
                    y = 0;
                }
                if ((int) (y * (0.005)) >= 1) {
                    mVipCenterTitleBar.setVisibility(View.VISIBLE);
                } else {
                    mVipCenterTitleBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        mVipCenterActivite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryGetActive();
//                if (canClick) {
//                    tryGetActive();
//                } else if (!TextUtils.isEmpty(message)) {
//                    showToast(message);
//                }
            }
        });

//        mVipCenterActivite.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//                return true;
//            }
//        });
    }

    @Override
    protected void initData() {
        String thumbnailslogo = App.USERSP.getString("thumbnailslogo", null);
        if (!TextUtils.isEmpty(thumbnailslogo)) {
            HttpLoader.getImageLoader().get(thumbnailslogo, ImageLoader
                    .getImageListener(mVipCenterUserLogo, R.drawable.picture_moren, R.drawable.picture_moren));
        }
        mVipCenterUserName.setText(App.USERSP.getString("name", ""));
        mVipCenterUserGender.setImageResource(App.USERSP.getString("gender", "").equals("1") ? R.drawable.man_icon :
                R.drawable.woman_icon);

        tryGetVipImg();

//        getDistence();
    }

    private void getDistence() {
        double p1lat = 39.918111;
        double p1lon = 116.45351;

        double p3lat = 39.917548916071;
        double p3lon = 116.45203200572259;
//
//        double p2lat = 39.918028;
//        double p2lon = 116.453469;

//        //四惠站
//        double lat_d = 39.908341d;
//        double lng_d = 116.494977d;
//        //公主坟
//        double lat_e = 39.907716d;
//        double lng_e = 116.30954d;

        double distance = LocationToBaiduMap.getDistance(p1lat, p1lon, p3lat, p3lon);
        ALog.i("p1 到 商家 的距离为: " + distance + "  经过转换后的距离为 :" + LocationToBaiduMap.getDistance(distance));
//        double distances = LocationToBaiduMap.getDistance(p2lat, p2lon, p3lat, p3lon);
//        ALog.i("p2 到 商家 的距离为: " + distances + "  经过转换后的距离为 :" + LocationToBaiduMap.getDistance(distances));
//        double distancesss = LocationToBaiduMap.getDistance(p1lat, p1lon, p2lat, p2lon);
//        ALog.i("p2 到 p2 的距离为: " + distancesss + "  经过转换后的距离为 :" + LocationToBaiduMap.getDistance(distancesss));

//        double distance = LocationToBaiduMap.getDistance(lat_d, lng_d, lat_e, lng_e);
//        ALog.i("p1 到 商家 的距离为: " + distance + "  经过转换后的距离为 :" + LocationToBaiduMap.getDistance(distance));
    }

    private void tryGetActive() {
        mVipCenterActivite.setClickable(false);

        HashMap params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_VIP_ACTIVE_NUM, params, VipGetActiveResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_VIP_ACTIVE_NUM, this, false).setTag(this);
    }

    private void tryGetVipImg() {
        HashMap params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_VIP_ACTIVE_IMG, params, VipGetActiveResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_VIP_ACTIVE_IMG, this, false).setTag(this);
    }

    //跳转到购买会员界面
    private void toBuyVip() {
        Intent intent = new Intent(VIPCenterActivity.this, BuyVipActivity.class);
        startActivity(intent);
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
                long viptime = dataBean.getViptime();
                switch (dataBean.getType()) {
                    case 1:
                        if (isVip(viptime)) {
                            mVipCenterTimeDistence.setText(getVipDays(viptime));
                            mVipCenterBuyVip.setText("续费会员");
                            mVipCenterVipImg.setVisibility(View.VISIBLE);
                        } else {
                            mVipCenterTimeDistence.setText("您的会员已到期");
                            mVipCenterBuyVip.setText("购买会员");
                            mVipCenterVipImg.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 2:
                        if (isVip(viptime)) {
                            mVipCenterTimeDistence.setText(getVipDays(viptime));
                            mVipCenterBuyVip.setText("续费会员");
                            mVipCenterVipImg.setVisibility(View.VISIBLE);
                        } else {
                            mVipCenterTimeDistence.setText("您的会员已到期");
                            mVipCenterBuyVip.setText("购买会员");
                            mVipCenterVipImg.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 3:
                        mVipCenterTimeDistence.setText("您是尊贵的永久会员");
                        mVipCenterBuyVip.setClickable(false);
                        mVipCenterBuyVip.setText("永久会员");
                        mVipCenterVipImg.setVisibility(View.VISIBLE);
                        break;
                    case 0:
                    default:
                        mVipCenterTimeDistence.setText("您尚未开通会员");
                        mVipCenterBuyVip.setText("购买会员");
                        mVipCenterVipImg.setVisibility(View.INVISIBLE);
                        break;
                }

                switch (dataBean.getVipIdentity()) {
                    case 4:
                    case 5:
                        mVipCenterCrownImg.setVisibility(View.VISIBLE);
                        break;
                    case 0:
                    default:
                        mVipCenterCrownImg.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_VIP_ACTIVE_NUM
                && response instanceof VipGetActiveResponse) {
            VipGetActiveResponse mVipGetActiveResponse = (VipGetActiveResponse) response;
            if (1 == mVipGetActiveResponse.getCode()) {
                if (null != mVipGetActiveResponse.getData()) {
                    VipGetActiveResponse.DataBean data = mVipGetActiveResponse.getData();
//                    if (data.getCurrentTimes() == data.getAllTimes()) {
//                        canClick = false;
//                        message = "您的抽奖次数已经用完了,请明天再来吧";
//                    }
//                    VipCenterActiveDialog mVipCenterActiveDialog = new VipCenterActiveDialog(VIPCenterActivity.this, data);
//                    mVipCenterActiveDialog.show();
                    VipCenterDrawDialog mVipCenterActiveDialog = new VipCenterDrawDialog(VIPCenterActivity.this, data);
                    mVipCenterActiveDialog.show();
                }
            } else if (-100 == mVipGetActiveResponse.getCode()) {
                showToastInfo(mVipGetActiveResponse.getMessage());
//                canClick = false;
//                message = "您的抽奖次数已经用完了,请明天再来吧";
            } else if (-80 == mVipGetActiveResponse.getCode()) {
                toBuyVip();
            } else {
                showToastInfo(mVipGetActiveResponse.getMessage());
            }
            mVipCenterActivite.setClickable(true);
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_VIP_ACTIVE_IMG
                && response instanceof VipGetActiveResponse) {
            VipGetActiveResponse mVipGetActiveResponse = (VipGetActiveResponse) response;
            if (1 == mVipGetActiveResponse.getCode()) {
                String vipImgUrl = mVipGetActiveResponse.getData().getVipImgUrl();
                if (!TextUtils.isEmpty(vipImgUrl)) {
                    HttpLoader.getImageLoader().get(vipImgUrl, ImageLoader.getImageListener(mVipCenterActivite,
                            R.drawable.picture_moren, R.drawable.picture_moren));
                } else {
                    canClick = false;
                }
            } else {
                canClick = false;
            }
        }
    }

    private boolean isVip(long viptime) {
        return viptime > TimeUtils.getCurrentTimeInLong();
    }

    private String getVipDays(long viptime) {
        long timeDistance = (viptime - TimeUtils.getCurrentTimeInLong()) / 1000;
        int day = (int) (timeDistance / 86400);
        if (day == 0) {
            double hour = Math.floor(timeDistance % 86400 / 3600);
            return "会员还有" + hour + "小时到期";
        } else {
            return "会员还有" + day + "天到期";
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_VIP_ACTIVE_NUM) {
            mVipCenterActivite.setClickable(true);
            showToastError("连接失败,请重试");
        }
    }
}
