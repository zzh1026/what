package com.neishenme.what.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.neishenme.what.R;
import com.neishenme.what.base.BaseActivity;

/**
 * 这个事注册的时候点击协议的时候的
 */
public class RegestXieyiActivity extends BaseActivity {
    public static final String NSM_URL = "http://www.neishenme.com/agree_new.html";
    private ImageView ivBack;
    //private TextView tv_calculate;
    private WebView mAboutUsWv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_regest_xieyi;
    }

    @Override
    protected void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        //tv_calculate = (TextView) findViewById(R.id.tv_calculate);
        mAboutUsWv = (WebView) findViewById(R.id.about_us_wv);
    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegestXieyiActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        mAboutUsWv.loadUrl(NSM_URL);
        WebSettings webSettings = mAboutUsWv.getSettings();
        webSettings.setSupportZoom(true);          //支持缩放
        webSettings.setBuiltInZoomControls(true);  //启用内置缩放装置
        webSettings.setJavaScriptEnabled(true);    //启用JS脚本

        mAboutUsWv.setWebViewClient(new WebViewClient() {
            //当点击链接时,希望覆盖而不是打开新窗口
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);  //加载新的url
                return true;    //返回true,代表事件已处理,事件流到此终止
            }
        });

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
                RegestXieyiActivity.this.setProgress(newProgress * 100);
            }
        });
    }
}
