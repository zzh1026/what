package com.neishenme.what.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.neishenme.what.R;
import com.neishenme.what.baidumap.NSMMapHelper;
import com.neishenme.what.baidumap.service.LocationService;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.dialog.ActiveApplySharedDialog;
import com.neishenme.what.eventbusobj.ActiveSharedBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.SharedDataCallback;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * 活动报名界面,点击我要报名进入
 */
public class ActiveSignUpActivity extends BaseActivity implements HttpLoader.ResponseListener, View.OnClickListener, SharedDataCallback {
    private ImageView mActiveApplyBack;
    private ImageView mActiveApplyShared;
    private TextView mActiveApplyEditInfo;
    private TextView mActiveApplyGetAutocode;
    private TextView mActiveApplySubmit;
    private EditText mActiveApplyAutoCode;
    private ActiveApplySharedDialog mActiveApplySharedDialog;

    private boolean isGetLocation = false;   //是否获取位置
    private LocationService locationService;
    private double latitude = 0;    //经纬度
    private double longgitude = 0;
    private ActiveSharedBean mSharedBean;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_activite_sign_up;
    }

    @Override
    protected void initView() {
        mActiveApplyBack = (ImageView) findViewById(R.id.active_apply_back);
        mActiveApplyShared = (ImageView) findViewById(R.id.active_apply_shared);
        mActiveApplyEditInfo = (TextView) findViewById(R.id.active_apply_edit_info);
        mActiveApplyGetAutocode = (TextView) findViewById(R.id.active_apply_get_autocode);
        mActiveApplySubmit = (TextView) findViewById(R.id.active_apply_submit);
        mActiveApplyAutoCode = (EditText) findViewById(R.id.active_apply_auto_code);

        locationService = NSMMapHelper.getInstance().locationService;
    }

    @Override
    protected void initListener() {
        locationService.registerListener(mListener);

        mActiveApplyBack.setOnClickListener(this);
        mActiveApplyShared.setOnClickListener(this);
        mActiveApplyEditInfo.setOnClickListener(this);
        mActiveApplyGetAutocode.setOnClickListener(this);
        mActiveApplySubmit.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mSharedBean = EventBus.getDefault().getStickyEvent(ActiveSharedBean.class);
        mActiveApplyAutoCode.setSingleLine();   //设置单行属性
    }

    private void getLocation() {
        isGetLocation = true;
        locationService.setLocationOption(LocationService.CoorType.CoorType_GCJ02, LocationService.TimeType.TIME_0);
        locationService.start();
    }

    //获取所有的活动信息
    private void getAllActive() {
//        HashMap params = new HashMap();
//        params.put("page", page + "");
//        params.put("pageSize", "20");
//        HttpLoader.get(ConstantsWhatNSM.URL_NEARLY_USER, params, NearByPeople.class,
//                ConstantsWhatNSM.REQUEST_CODE_URL_NEARLY_USER, this, false).setTag(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.active_apply_back:        //返回
                finish();
                break;
            case R.id.active_apply_shared:      //分享
                showSharedDialog();
                break;
            case R.id.active_apply_edit_info:   //编辑个人信息
                ActivityUtils.startActivity(this, EditSelfInfoActivity.class);
                break;
            case R.id.active_apply_get_autocode://获取验证码
                mActiveApplyGetAutocode.setClickable(false);
                mActiveApplyGetAutocode.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mActiveApplyGetAutocode.setClickable(true);
                    }
                }, 60000);
                ALog.i("点击获取验证码成功!");
                getLocation();
                getAutoCode();
                break;
            case R.id.active_apply_submit:      //提交
                trySubmit();
                break;
            default:
                break;
        }
    }

    private void showSharedDialog() {
        mActiveApplySharedDialog = new ActiveApplySharedDialog(this, this);
        mActiveApplySharedDialog.show();
    }

    private void getAutoCode() {
        if (!NSMTypeUtils.isLogin()) {
            showToastWarning("您尚未登录,请登录后再进行操作");
            ActivityUtils.startActivity(this, LoginActivity.class);
            return;
        }
        HashMap params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("type", "USER_TAKEMEOUT");
        HttpLoader.post(ConstantsWhatNSM.URL_ACTIVE_GET_AUTOCODE, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_GET_AUTOCODE, this, false).setTag(this);
    }

    private void trySubmit() {
        String autoCode = mActiveApplyAutoCode.getText().toString().trim();
        if (TextUtils.isEmpty(autoCode)) {
            showToastInfo("输入验证码");
            return;
        }
        HashMap params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("authCode", autoCode);
        params.put("longitude", longgitude + "");
        params.put("latitude", latitude + "");
        HttpLoader.post(ConstantsWhatNSM.URL_ACTIVE_TACKMEOUT_ADD, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_TACKMEOUT_ADD, this, false).setTag(this);
    }

    @Override
    public void startShared(int shareChannel) {
        ShareAction shareAction = new ShareAction(this);
        HashMap<String, String> param = new HashMap<>();
        switch (shareChannel) {
            case SharedDataCallback.SHARE_TO_WEIXIN:
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN);
                param.put("sharechannel", "weixin");
                break;
            case SharedDataCallback.SHARE_TO_WEIXINFRIEND:
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                param.put("sharechannel", "weixinpengyouquan");
                break;
            case SharedDataCallback.SHARE_TO_QQFRIEND:
                shareAction.setPlatform(SHARE_MEDIA.QZONE);
                param.put("sharechannel", "qqkongjian");
                break;
            case SharedDataCallback.SHARE_TO_SINA:
                shareAction.setPlatform(SHARE_MEDIA.SINA);
                param.put("sharechannel", "xinlangweibo");
                break;
        }
        param.put("shareuserid", NSMTypeUtils.getMyUserId());
        param.put("platform", "android");

        UMWeb web;
        if (mSharedBean != null) {
            web = new UMWeb(mSharedBean.sharedLink + HttpLoader.buildGetParam(param, mSharedBean.sharedLink));
            web.setTitle(mSharedBean.shareTitle);
            web.setThumb(new UMImage(this, mSharedBean.sharedImage));
            web.setDescription(mSharedBean.sharedDescribe);
            shareAction
                    .withText(mSharedBean.sharedDescribe)
                    .setCallback(umShareListener)
                    .withMedia(web)
                    .share();
        } else {
            web = new UMWeb("http://www.neishenme.com");
            web.setTitle("内什么");
            web.setDescription("内什么");
            shareAction
                    .withText("来自内什么的分享")
                    .setCallback(umShareListener)
                    .withMedia(web)
                    .share();
        }
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_TACKMEOUT_ADD &&
                response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            int code = sendSuccessResponse.getCode();
            if (1 == code) {
                showToastSuccess("报名成功!");
                finish();
            } else {
                showToastInfo(sendSuccessResponse.getMessage());
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_GET_AUTOCODE &&
                response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            int code = sendSuccessResponse.getCode();
            if (1 == code) {
                showToastSuccess("获取成功!");
            } else {
                showToastInfo(sendSuccessResponse.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        showToastError("网络连接失败,请重试!");
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                if (isGetLocation) {
                    isGetLocation = false;
                    latitude = location.getLatitude();
                    longgitude = location.getLongitude();
                    getAllActive();
                }
                locationService.stop();
            } else {
                showToastError("获取位置失败");
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (mActiveApplySharedDialog != null && mActiveApplySharedDialog.isShowing()) {
                mActiveApplySharedDialog.dismiss();
            }
            showToastSuccess("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            showToastError("分享失败");

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ALog.i("platform" + platform + " 分享取消了");
        }
    };

    @Override
    protected void onDestroy() {
        HttpLoader.cancelRequest(this);
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onDestroy();
    }
}
