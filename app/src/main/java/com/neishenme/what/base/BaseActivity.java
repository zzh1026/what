package com.neishenme.what.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.neishenme.what.application.App;
import com.neishenme.what.dialog.LoadingDialog;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.SystemBarTintManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.seny.android.utils.ALog;
import org.seny.android.utils.MyToast;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者：zhaozh create on 2016/3/8 20:06
 * .
 * 版权: 内什么
 * .
 * =====================================
 * .
 * 这是一个所有activity需要继承的基类的类
 * <p/>
 * 继承了OnClickListener,子类可以直接实现方法
 * .
 * 其作用是
 * <p/>
 * 限制所有的类的初始化所有数据和布局的方法
 */
public abstract class BaseActivity extends FragmentActivity {
    protected final static String TAG = "BaseActivity";
    public LoadingDialog loadingDialog;
    protected int mDefaultStatusColor = Color.RED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        //设置窗口背景不透明
        getWindow().setBackgroundDrawable(null);

        //有EditView的界面，默认不弹窗软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//
//            setTranslucentStatus(this, true);
//
//        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);

        tintManager.setStatusBarTintEnabled(true);

        // 使用颜色资源
        //tintManager.setStatusBarTintResource(R.color.colorTitleBarBg);

        //强行竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(initContentView());

        initView();
        initListener();

        loadingDismiss();
        initLoadingDialog();

        if (NSMTypeUtils.isLogin() && JPushInterface.isPushStopped(App.getApplication())) {
            JPushInterface.resumePush(App.getApplication());
        }

        initData();

    }

    private void initLoadingDialog() {
        loadingDialog = new LoadingDialog(this);
    }

    @TargetApi(19)

    private static void setTranslucentStatus(Activity activity, boolean on) {

        Window win = activity.getWindow();

        WindowManager.LayoutParams winParams = win.getAttributes();

        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        if (on) {

            winParams.flags |= bits;

        } else {

            winParams.flags &= ~bits;

        }

        win.setAttributes(winParams);

    }

    /**
     * 返回界面的布局,设置进activity中,子类直接返回一个布局即可.
     *
     * @return layout id
     */
    protected abstract int initContentView();

    /**
     * 初始化控件，关联控件和activity , findViewById();
     */
    protected abstract void initView();

    /**
     * 给控件设置监听器，初始化监听器 , setOnclickListener;
     */
    protected abstract void initListener();

    /**
     * 初始化数据（网络加载、本地加载数据等操作）
     */
    protected abstract void initData();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    protected boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    protected void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config.build());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        HttpLoader.cancelRequest(this);
        super.onDestroy();
    }

    public void showToast(String str) {
        MyToast.showConterToast(App.getApplication(), str);
    }

    public void showToastInfo(String str) {
        MyToast.showInfo(App.getApplication(), str);
    }

    public void showToastWarning(String str) {
        MyToast.showWarning(App.getApplication(), str);
    }

    public void showToastError(String str) {
        MyToast.showError(App.getApplication(), str);
    }

    public void showToastSuccess(String str) {
        MyToast.showSuccess(App.getApplication(), str);
    }

    protected void loadingShow() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            try {
                loadingDialog.show();
            } catch (Exception e) {
                // e.printStackTrace();
                ALog.e("显示失败");
            }
        }
    }

    protected void loadingDismiss() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            try {
                loadingDialog.dismiss();
            } catch (Exception e) {
                //e.printStackTrace();
                ALog.e("隐藏失败");
            }
        }
    }
}
