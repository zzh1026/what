package com.neishenme.what.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.neishenme.what.R;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.ActiveIsJoinResponse;
import com.neishenme.what.bean.ActiveJoinInfoResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.eventbusobj.ActiveJoinTridBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.nostra13.universalimageloader.utils.L;

import java.util.HashMap;

import static org.seny.android.utils.AppInfoUtil.closeProgress;

/**
 * banner 图点击广告进来的 可加入别人订单的广告界面.
 */
public class ActiveBannerJoinActivity extends BaseActivity implements HttpLoader.ResponseListener {
    public static final int REQUEST_CODE_JOIN_ACTIVE = 1;

    private String url;
    private String groupId;
    private ImageView mActiveBack;
    private WebView mActiveWebview;
    private Button mActiveJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_active_banner;
    }

    @Override
    protected void initView() {
        mActiveBack = (ImageView) findViewById(R.id.active_back);
        mActiveWebview = (WebView) findViewById(R.id.active_webview);
        mActiveJoin = (Button) findViewById(R.id.active_join);
    }

    @Override
    protected void initListener() {
        mActiveBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActiveBannerJoinActivity.this.finish();
            }
        });

        mActiveJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinActive();
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("data");
        groupId = intent.getStringExtra("groupid");
        initSeetings();

        mActiveWebview.requestFocus();

        initWebClient();

        initKeyListener();

        mActiveWebview.setInitialScale(25);
        mActiveWebview.loadUrl(url);

        getJoinStatus();
    }

    private void getJoinStatus() {
        HashMap params = new HashMap();
        params.put("groupId", groupId);
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_GET_ACTIVE_ISJOIN, params, ActiveIsJoinResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_GET_ACTIVE_ISJOIN, this, false).setTag(this);
    }

    private void joinActive() {
        HashMap params = new HashMap();
        params.put("groupId", groupId);
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_GET_ACTIVE_JOIN, params, ActiveJoinInfoResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_GET_ACTIVE_JOIN, this, false).setTag(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_JOIN_ACTIVE) {
            if (resultCode == RESULT_OK) {
                showToastSuccess("报名成功");
                setEnable(false);
            } else {
                showToastError("报名失败,请重试");
                setEnable(true);
            }
        }
    }

    private void initWebClient() {
        mActiveWebview.setWebViewClient(new MyWebViewClient());

        mActiveWebview.setWebChromeClient(new WebChromeClient() {
            //当WebView进度改变时更新窗口进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //Activity的进度范围在0到10000之间,所以这里要乘以100
                ActiveBannerJoinActivity.this.setProgress(newProgress * 100);
            }
        });
    }

    private void initKeyListener() {
        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        mActiveWebview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mActiveWebview.canGoBack()) {
                        mActiveWebview.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });

        mActiveWebview.setDownloadListener(new MyWebViewDownLoadListener());
    }

    private void initSeetings() {
        WebSettings webSettings = mActiveWebview.getSettings();
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
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_GET_ACTIVE_ISJOIN &&
                response instanceof ActiveIsJoinResponse) {
            ActiveIsJoinResponse mActiveIsJoinResponse = (ActiveIsJoinResponse) response;
            if (mActiveIsJoinResponse.getCode() == 1) {
                if (TextUtils.equals("0", mActiveIsJoinResponse.getData().getIsJoinFlag())) {
                    setEnable(true);
                } else {
                    setEnable(false);
                }
            } else {
                showToastWarning(mActiveIsJoinResponse.getMessage());
                setEnable(false);
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

    private void setEnable(boolean enable) {
        mActiveJoin.setEnabled(enable);
        if (enable) {
            mActiveJoin.setText("我要报名");
        } else {
            mActiveJoin.setText("已经报名");
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    public class MyWebViewClient extends WebViewClient {
        // 如果页面中链接，如果希望点击链接继续在当前browser中响应，
        // 而不是新开Android的系统browser中响应该链接，必须覆盖 webview的WebViewClient对象。
        public boolean shouldOverviewUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            L.i("onPageStarted");
//            showProgress();
        }

        public void onPageFinished(WebView view, String url) {
            L.i("onPageFinished");
            closeProgress();
        }

        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            L.i("onReceivedError");
            closeProgress();
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
