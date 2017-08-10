package com.neishenme.what.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.seny.android.utils.ALog;

/**
 * 这个类的作用是:  未来 banner 图中的type=20 进入的所有广告界面的基类
 * 其实现类为 {@link com.neishenme.what.activity.ADWebActivity}
 * <p>
 * Created by zhaozh on 2017/4/7.
 */

public abstract class BaseWebActivity extends BaseActivity {

    public static final String AD_URL = "url";
    public static final String AD_FLAG = "flag";
    public static final String AD_TITLE = "title";

    private WebViewCall mAdapter;

    @Override
    protected void initData() {
        setWebViewOptions();
    }

    private void setWebViewOptions() {
        /**
         * 配置webview的setting
         */
        initSeetings();

        /**
         * 请求 焦点
         */
        initFocus();

        /**
         * 配置 webview的监听状态 ,包括点击新的连接, 开始load和 load完毕等状态的监听
         */
        initWebClient();

        /**
         * 配置页面 的加载信息,  包括 加载的进度, 和加载的页面title等信息
         */
        initWebChromeClient();

        /**
         *  配置页面可以下载的情况, 表示下载的开始监听
         */
        initDownLoadListener();

        /**
         * 配置页面初始化的字体大小
         */
        getCurrentWebView().setInitialScale(25);
    }

    protected void initSeetings() {
        WebSettings webSettings = getCurrentWebView().getSettings();
        webSettings.setSupportZoom(true);          //支持缩放
        webSettings.setBuiltInZoomControls(true);  //启用内置缩放装置
        if (mAdapter == null || mAdapter.shouldEnableJS())
            webSettings.setJavaScriptEnabled(true);    //启用JS脚本
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    private void initFocus() {
        getCurrentWebView().requestFocus();
    }

    private void initWebClient() {
        getCurrentWebView().setWebViewClient(new MyWebViewClient());
    }

    private void initWebChromeClient() {
        getCurrentWebView().setWebChromeClient(new MyWebChromeClient());
    }

    private void initDownLoadListener() {
        getCurrentWebView().setDownloadListener(new MyWebViewDownLoadListener());
    }

    @Override
    public void onBackPressed() {
        if (getCurrentWebView().canGoBack()) {
            getCurrentWebView().goBack();   //后退
        } else {
            super.onBackPressed();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        // 如果页面中链接，如果希望点击链接继续在当前browser中响应，
        // 而不是新开Android的系统browser中响应该链接，必须覆盖 webview的WebViewClient对象。

        /**
         * 当前webview将要加载新的url的时候调用
         * <p>
         * 如果没有提供   WebViewClient   ,将由activity manager选择合适的方式处理url
         * 如果提供了webviewclient, 返回ture表示请求应用处理, 自己不处理, 返回 false 代表本webview已经处理
         * <p>
         * 重写此方法表明点击webview里面的连接是有当前webview处理(false) 还是系统自行处理(true)
         *
         * @param view
         * @param url
         * @return
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            ALog.i("应该重新走起");
            if (mAdapter == null || mAdapter.showLoadUrlSelf()) {
                view.loadUrl(url);
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            ALog.i("开始加载");
            if (mAdapter != null) {
                mAdapter.onPageStarted();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            ALog.i("加载完毕");
            if (mAdapter != null) {
                mAdapter.onPageFinished();
            }
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        //当WebView进度改变时更新窗口进度
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            ALog.i("现在的progress为:  " + newProgress);
            if (mAdapter != null) {
                mAdapter.onProgressChanged(newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            ALog.i("页面的title为:  " + title);
            if (mAdapter != null) {
                mAdapter.onReceivedTitle(title);
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

    protected void setWebViewClientAdapter(WebViewCall mAdapter) {
        this.mAdapter = mAdapter;
    }

    /**
     * 向子类提供一些方法用来进行空实现, 可以让子类 在部分操作的时候自定义一些操作
     */
    protected class WebViewClientAdapter implements WebViewCall {
        @Override
        public boolean showLoadUrlSelf() {
            return true;
        }

        @Override
        public void onProgressChanged(int newProgress) {
        }

        @Override
        public void onReceivedTitle(String title) {
        }

        @Override
        public boolean shouldEnableJS() {
            return true;
        }

        @Override
        public void onPageStarted() {
        }

        @Override
        public void onPageFinished() {
        }
    }

    public interface WebClientCall {
        /**
         * 是否允许支持JS, 默认支持, 子类可以重写不支持
         *
         * @return
         */
        boolean shouldEnableJS();

        /**
         * 页面开始加载, 可以做一些展示loading 或者显示 progressbar的处理
         */
        void onPageStarted();

        /**
         * 页面加载完毕 , 可以做一些loading 或者progress的隐藏或其他初始化,此时页面已经加载完毕
         */
        void onPageFinished();
    }

    public interface WebChromCall {
        /**
         * 是否需要自行处理新点击打开的界面
         *
         * @return
         */
        public boolean showLoadUrlSelf();

        /**
         * 页面加载过程中的 progress的改变监听
         *
         * @param newProgress
         */
        public void onProgressChanged(int newProgress);

        /**
         * 页面加载完毕后 的title获取
         *
         * @param title
         */
        public void onReceivedTitle(String title);
    }

    /**
     * 该接口封装了 关于页面的一些回调, 其实现类为 {@link WebViewClientAdapter} , 可以通过集成该类进行处理
     */
    public interface WebViewCall extends WebChromCall, WebClientCall {
    }

    /**
     * 获取当前的webview
     *
     * @return
     */
    protected abstract WebView getCurrentWebView();
}
