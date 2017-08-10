package com.neishenme.what.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.neishenme.what.R;
import com.neishenme.what.base.BaseWebActivity;
import com.neishenme.what.bean.ActiveJoinInfoResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SharedDataResponse;
import com.neishenme.what.dialog.ActiveApplySharedDialog;
import com.neishenme.what.eventbusobj.ActiveJoinTridBean;
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

import java.util.HashMap;

/**
 * 这个类的作用是: 重构之后的webactivity界面 广告
 * <p>
 * Created by zhaozh on 2017/4/7.
 */

@SuppressLint("SetJavaScriptEnabled")
public class ADWebActivity extends BaseWebActivity implements HttpLoader.ResponseListener, SharedDataCallback {
    public static final int REQUEST_CODE_JOIN_ACTIVE = 1;
    private ImageView mAdWebBack;
    private TextView mAdWebTitle;
    private WebView mAdWebWebview;

    private ActiveApplySharedDialog mActiveApplySharedDialog;   //分享的弹窗
    private SharedDataResponse.DataBean mSharedDataResponseData;    //分享的数据

    private boolean isShared = false;   //标记是否是进入了分享界面, 用来区分是在onactivityresult中的处理
    private boolean canClick = true;    //标记是否可以点击,为了防止多次请求

    //标记要load的也页面
    private String url;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_ad_web;
    }

    @Override
    protected void initView() {
        mAdWebBack = (ImageView) findViewById(R.id.ad_web_back);
        mAdWebTitle = (TextView) findViewById(R.id.ad_web_title);
        mAdWebWebview = (WebView) findViewById(R.id.ad_web_webview);
    }

    @Override
    protected void initListener() {
        mAdWebBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdWebWebview.canGoBack()) {
                    mAdWebWebview.goBack();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        url = intent.getStringExtra(AD_URL);
        String title = intent.getStringExtra(AD_TITLE);
        if (!TextUtils.isEmpty(title)) {
            mAdWebTitle.setText(title);
        }
        int flag = intent.getIntExtra(AD_FLAG, 0);
        if (1 == flag) {        //需要登录的, 进行注册调用
            //创建js调用接口
            mAdWebWebview.addJavascriptInterface(this, "android");
        }

        setWebViewClientAdapter(new MyWebViewAdapter());

        mAdWebWebview.loadUrl(url);
    }

    @JavascriptInterface
    public void showShare(final String sharedId) {
        if (!canClick) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                canClick = false;
                isShared = true;
                showSharedUI(sharedId); //显示分享的ui
            }
        });
    }

    @JavascriptInterface
    public void joinPay(final String groupId) {
        if (!canClick) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                canClick = false;
                joinPayGet(groupId);    //加入活动
            }
        });
    }

    @JavascriptInterface
    public void login() {
        if (!canClick) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //跳转到登录界面
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isShared) {
            isShared = false;
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == REQUEST_CODE_JOIN_ACTIVE) {
            if (resultCode == RESULT_OK) {
                showToastSuccess("报名成功");
            } else {
                showToastError("报名失败,请重试");
            }
        }
    }

    private void showSharedUI(String sharedId) {
        if (!NSMTypeUtils.isLogin()) {
            return;
        }
        //获取分享数据
        HashMap params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("bannerId", sharedId);
        HttpLoader.post(ConstantsWhatNSM.URL_AD_SHARED, params, SharedDataResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_SAD_SHARED, this, false).setTag(this);
    }

    private void joinPayGet(String groupId) {
        if (!NSMTypeUtils.isLogin()) {
            return;
        }
        HashMap params = new HashMap();
        params.put("groupId", groupId);
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_GET_ACTIVE_JOIN, params, ActiveJoinInfoResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_GET_ACTIVE_JOIN, this, false).setTag(this);
    }

    @Override
    protected WebView getCurrentWebView() {
        return mAdWebWebview;
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        canClick = true;
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_SAD_SHARED &&
                response instanceof SharedDataResponse) {
            SharedDataResponse mSharedDataResponse = (SharedDataResponse) response;
            int code = mSharedDataResponse.getCode();
            if (1 == code) {
                mSharedDataResponseData = mSharedDataResponse.getData();
                if (mActiveApplySharedDialog == null) {
                    mActiveApplySharedDialog = new ActiveApplySharedDialog(this, this);
                }
                if (!mActiveApplySharedDialog.isShowing()) {
                    mActiveApplySharedDialog.show();
                }
            } else {
                showToastInfo(mSharedDataResponse.getMessage());
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_GET_ACTIVE_JOIN &&
                response instanceof ActiveJoinInfoResponse) {
            ActiveJoinInfoResponse mActiveJoinInfoResponse = (ActiveJoinInfoResponse) response;
            if (mActiveJoinInfoResponse.getCode() == 1) {
                ActiveJoinInfoResponse.DataBean data = mActiveJoinInfoResponse.getData();
                ActiveJoinTridBean activeJoinTridBean = new ActiveJoinTridBean(
                        data.getTrade().getTradeNum(),
                        data.getImgUrl(),
                        data.getTitle(),
                        data.getName(),
                        data.getTime(),
                        data.getTrade().getPayprice()
                );
                PayActiveActivity.startPayActiveActForResult(this, REQUEST_CODE_JOIN_ACTIVE, activeJoinTridBean);
            } else {
                showToastWarning(mActiveJoinInfoResponse.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        canClick = true;
    }

    public class MyWebViewAdapter extends WebViewClientAdapter {

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
        param.put("platform", "android");
        param.put("token", NSMTypeUtils.getMyToken());

        UMWeb web;
        if (mSharedDataResponseData != null) {
            if (!TextUtils.isEmpty(mSharedDataResponseData.getShareLink())) {
                String shareLink = mSharedDataResponseData.getShareLink();
                web = new UMWeb(shareLink + HttpLoader.buildGetParam(param, shareLink));

                if (!TextUtils.isEmpty(mSharedDataResponseData.getShareTitle())) {
                    web.setTitle(mSharedDataResponseData.getShareTitle());
                }
                if (!TextUtils.isEmpty(mSharedDataResponseData.getShareImage())) {
                    web.setThumb(new UMImage(this, mSharedDataResponseData.getShareImage()));
                }
                if (!TextUtils.isEmpty(mSharedDataResponseData.getShareDescribe())) {
                    web.setDescription(mSharedDataResponseData.getShareDescribe());
                }
                shareAction.withMedia(web);
            }
            if (!TextUtils.isEmpty(mSharedDataResponseData.getShareDescribe())) {
                shareAction.withText(mSharedDataResponseData.getShareDescribe());
            }
            shareAction.setCallback(umShareListener)
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
        }
    };

    public static void openAdWebAct(Activity activity, String url, int flag, String title) {
        Intent intent = new Intent(activity, ADWebActivity.class);
        intent.putExtra(AD_URL, url);
        intent.putExtra(AD_FLAG, flag);
        intent.putExtra(AD_TITLE, title);
        activity.startActivity(intent);
    }
}
