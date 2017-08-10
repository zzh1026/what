package com.neishenme.what.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.anbetter.danmuku.DanMuView;
import com.android.volley.VolleyError;
import com.neishenme.what.R;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.ActiveIphone7Response;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SharedDataResponse;
import com.neishenme.what.dialog.ActiveApplySharedDialog;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.OnDanMuInfoCallback;
import com.neishenme.what.nsminterface.SharedDataCallback;
import com.neishenme.what.service.SocketGetLocationService;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.DanMuHelper;
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
import java.util.List;

import static org.seny.android.utils.AppInfoUtil.closeProgress;

/**
 *
 *  主界面banner图点击广告进入的广告界面
 *
 */
@SuppressLint("SetJavaScriptEnabled")
public class MainPushADActivity extends BaseActivity implements SharedDataCallback, HttpLoader.ResponseListener {
    public static final String AD_DATA = "data";
    public static final String AD_TYPE = "type";
    public static final String AD_BANNERID = "bannerId";
    public static final String AD_TITLE = "title";
    public static final String AD_SERVICEID = "serviceid";

    private static final long ZOOM_CONTROLS_TIMEOUT =
            ViewConfiguration.getZoomControlsTimeout();

    private String url;
    private String mWebTitle;   //标记进入网页的标题title
    private ImageView ivBack;
    //private TextView tv_calculate;
    private WebView mAboutUsWv;
    private DanMuView mAdMainPushDanmu;

    private TextView mAdMainPushTitle;
    private ImageView mAdMainPushShared;

    private DanMuHelper mDanMuHelper;

    private ActiveApplySharedDialog mActiveApplySharedDialog;
    private int mAdType;    //标记进入广告界面的type
    private SharedDataResponse.DataBean mSharedDataResponseData;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_ad_mainpush;
    }

    @Override
    protected void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        //tv_calculate = (TextView) findViewById(R.id.tv_calculate);
        mAboutUsWv = (WebView) findViewById(R.id.about_us_wv);
        mAdMainPushTitle = (TextView) findViewById(R.id.ad_main_push_title);
        mAdMainPushShared = (ImageView) findViewById(R.id.ad_main_push_shared);
        mAdMainPushDanmu = (DanMuView) findViewById(R.id.ad_main_push_danmu);

    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAboutUsWv.canGoBack()) {
                    mAboutUsWv.goBack();
                } else {
                    finish();
                }
            }
        });
        mAdMainPushShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSharedUI();
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra(AD_DATA);

        initSeetings();

        mAboutUsWv.requestFocus();

        mAboutUsWv.setWebViewClient(new MyWebViewClient());

        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        mAboutUsWv.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mAboutUsWv.canGoBack()) {
                        mAboutUsWv.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });

        mAboutUsWv.setWebChromeClient(new WebChromeClient() {
            //当WebView进度改变时更新窗口进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //Activity的进度范围在0到10000之间,所以这里要乘以100
                ALog.i("现在的progress为:  " + newProgress);
                MainPushADActivity.this.setProgress(newProgress * 100);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (!TextUtils.isEmpty(mWebTitle)) {
                    if (mAboutUsWv.canGoBack()) {
                        mAdMainPushTitle.setText(title);
                    } else {
                        mAdMainPushTitle.setText(mWebTitle);
                    }
                }
                super.onReceivedTitle(view, title);
            }
        });

        mAboutUsWv.setDownloadListener(new MyWebViewDownLoadListener());
        mAboutUsWv.setInitialScale(25);
        mAboutUsWv.loadUrl(url);

        mAdType = intent.getIntExtra(AD_TYPE, 2);
        mAdMainPushShared.setVisibility(View.INVISIBLE);
        if (mAdType == 6) {
            //创建js调用接口
            mAboutUsWv.addJavascriptInterface(this, "android");

            //获取分享数据
            HashMap params = new HashMap();
            params.put("token", NSMTypeUtils.getMyToken());
            params.put("bannerId", intent.getIntExtra(AD_BANNERID, 0) + "");
            HttpLoader.post(ConstantsWhatNSM.URL_AD_SHARED, params, SharedDataResponse.class,
                    ConstantsWhatNSM.REQUEST_CODE_SAD_SHARED, this, false).setTag(this);
            mWebTitle = intent.getStringExtra(AD_TITLE);
            mAdMainPushTitle.setText(mWebTitle);
            mAdMainPushShared.setVisibility(View.VISIBLE);

            if (2 == intent.getIntExtra(AD_SERVICEID, 0)) {
                //获取弹幕是否弹出和弹出数据
                HttpLoader.post(ConstantsWhatNSM.URL_GET_DANMU_INFO, null, ActiveIphone7Response.class,
                        ConstantsWhatNSM.REQUEST_CODE_GET_DANMU_INFO, this, false).setTag(this);
                mDanMuHelper = new DanMuHelper(this, mAdMainPushDanmu);
            }

        }
    }

    @JavascriptInterface
    public void showShare() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showSharedUI();
            }
        });
    }

    @JavascriptInterface
    public void jumpPublishPage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (NSMTypeUtils.isLogin()) {
                    ActivityUtils.startActivity(MainPushADActivity.this, ReleaseQuickActivity.class);
                } else {
                    ActivityUtils.startActivity(MainPushADActivity.this, LoginActivity.class);
                }
            }
        });
    }

    private void showSharedUI() {
        if (mAdType == 6 && NSMTypeUtils.isLogin()) {
            if (mActiveApplySharedDialog == null) {
                mActiveApplySharedDialog = new ActiveApplySharedDialog(MainPushADActivity.this, MainPushADActivity.this);
            }
            if (!mActiveApplySharedDialog.isShowing()) {
                mActiveApplySharedDialog.show();
            }
        }
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
        param.put("token", NSMTypeUtils.getMyToken());

        UMWeb web;
        if (mSharedDataResponseData != null) {
            if (!TextUtils.isEmpty(mSharedDataResponseData.getShareLink())) {
                web = new UMWeb(mSharedDataResponseData.getShareLink() +
                        HttpLoader.buildGetParam(param, mSharedDataResponseData.getShareLink()));

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void initSeetings() {
        WebSettings webSettings = mAboutUsWv.getSettings();
        webSettings.setSupportZoom(true);          //支持缩放
        webSettings.setBuiltInZoomControls(true);  //启用内置缩放装置
        webSettings.setJavaScriptEnabled(true);    //启用JS脚本
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_SAD_SHARED &&
                response instanceof SharedDataResponse) {
            SharedDataResponse mSharedDataResponse = (SharedDataResponse) response;
            int code = mSharedDataResponse.getCode();
            if (1 == code) {
                mSharedDataResponseData = mSharedDataResponse.getData();
            } else {
                showToastInfo(mSharedDataResponse.getMessage());
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_GET_DANMU_INFO &&
                response instanceof ActiveIphone7Response) {
            ActiveIphone7Response mIphone7Response = (ActiveIphone7Response) response;
            int code = mIphone7Response.getCode();
            if (1 == code) {
                if (null != mIphone7Response.getData()) {
                    List<ActiveIphone7Response.DataBean.ListsBean> lists = mIphone7Response.getData().getLists();
                    if (lists != null && lists.size() != 0) {
                        mAdMainPushDanmu.setVisibility(View.VISIBLE);
                        mAdMainPushDanmu.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return false;
                            }
                        });
                        mAdMainPushDanmu.prepare();
                        SocketGetLocationService.regestAdPush(new OnDanMuInfoCallback() {
                            @Override
                            public void onDanMuInfoCallback(final String msg) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mDanMuHelper.addDanMu(msg);
                                    }
                                });
                            }
                        });
                        for (ActiveIphone7Response.DataBean.ListsBean list : lists) {
                            mDanMuHelper.addDanMu(list);
                        }
                    }
                }
            }
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
//            showToastError("分享失败,渠道是:" + platform.toString() + "原因是:" + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            showToastError("取消分享,渠道是:" + platform.toString());
        }
    };

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    @Override
    protected void onDestroy() {
        if (mDanMuHelper != null) {
            mDanMuHelper.release();
            mDanMuHelper = null;
        }
        SocketGetLocationService.unRegestAdPush();

        super.onDestroy();
        //清除缓存
        mAboutUsWv.clearCache(true);

        //清除访问历史记录
        mAboutUsWv.clearHistory();

//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                //释放WebView占用的资源
//                try {
//                    mAboutUsWv.destroy();
//                } catch (Exception e) {
//
//                }
//            }
//        }, ZOOM_CONTROLS_TIMEOUT);


    }

    public class MyWebViewClient extends WebViewClient {
        // 如果页面中链接，如果希望点击链接继续在当前browser中响应，
        // 而不是新开Android的系统browser中响应该链接，必须覆盖 webview的WebViewClient对象。
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            ALog.i("应该重新走起");
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            ALog.i("开始加载");
//            showProgress();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            ALog.i("加载完毕");
            closeProgress();
            if (!mAboutUsWv.canGoBack() && mDanMuHelper != null) {
                mAdMainPushDanmu.setVisibility(View.VISIBLE);
            } else {
                mAdMainPushDanmu.setVisibility(View.INVISIBLE);
            }

            if (!mAboutUsWv.canGoBack() && mAdType == 6) {
                mAdMainPushShared.setVisibility(View.VISIBLE);
            } else {
                mAdMainPushShared.setVisibility(View.INVISIBLE);
            }
        }

    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
