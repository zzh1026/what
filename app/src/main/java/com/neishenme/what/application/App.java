package com.neishenme.what.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;
import android.view.Display;
import android.view.WindowManager;

import com.neishenme.what.baidumap.NSMMapHelper;
import com.neishenme.what.common.ActiveSPConfig;
import com.neishenme.what.huanxinchat.NSMHXHelper;
import com.neishenme.what.service.ClientSocket;
import com.neishenme.what.utils.NotificationUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

import static com.umeng.qq.tencent.h.f;

/**
 * 作者：zhaozh on 2016/3/8 17:09 create
 * <p/>
 * 版权: 内什么
 * <p/>
 * -----------------------------------------
 * <p/>
 * 这是一个 全局Application的子类,维护了全局可能用到的东西
 * <p/>
 * 其作用是 :
 * 1,对外暴露上下文.
 * 2,对外暴露主线程的handler.
 * 3,对外暴露主线程.
 * 4,对外暴露主线程id.
 * 5,对外暴露主线程的looper.
 */
public class App extends MultiDexApplication {


    private static final String TAG = "App";
    public static String cookie = null;

    // login user name
    public final String PREF_USERNAME = "username";
    //登录时候的环信的名字,可能出现没有写入sp中
    public static String hxUsername = "";

    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";

    public static SharedPreferences SP;
    public static SharedPreferences.Editor EDIT;

    public static SharedPreferences USERSP;
    public static SharedPreferences.Editor USEREDIT;

    public static SharedPreferences ACTIVESP;
    public static SharedPreferences.Editor ACTIVEEDIT;

    // 获取到主线程的上下文
    private static App mContext = null;

    // 获取到主线程的handler
    private static Handler mMainThreadHandler = null;

    // 获取到主线程
    private static Thread mMainThread = null;

    // 获取到主线程的id
    private static int mMainThreadId;

    // 获取到主线程的looper
    private static Looper mMainThreadLooper = null;

    private Display display;
    private static Context context;

    private static ClientSocket mClientSocket = null;

    private static ArrayList<Activity> list;


    @Override
    public void onCreate() {
        super.onCreate();

        this.mContext = this;
        this.mMainThreadHandler = new Handler();
        this.mMainThread = Thread.currentThread();
        this.mMainThreadId = android.os.Process.myTid();
        this.mMainThreadLooper = getMainLooper();
        SP = getSharedPreferences("config", MODE_PRIVATE);
        EDIT = SP.edit();
        init();
        USERSP = getSharedPreferences("userconfig", MODE_PRIVATE);
        USEREDIT = USERSP.edit();

        ACTIVESP = getSharedPreferences(ActiveSPConfig.ACTIVE_SP_NAME, MODE_PRIVATE);
        ACTIVEEDIT = ACTIVESP.edit();

        context = getApplicationContext();

        //初始化socket长连接;
        initSocket();

        /**
         * 初始化百度地图
         */
        NSMMapHelper.getInstance().init(this);

        //初始化环信,设为调试模式，打成正式包时，最好设为false，以免消耗额外的资源
        NSMHXHelper.getInstance().init(this);

        /**
         * 初始化激光推送
         */
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);

        NotificationUtils.updateNotification();

        /**
         * 初始化享sdk
         */
        UMShareAPI.get(this);
        Config.isJumptoAppStore = true;


        /**
         * 初始化百分比布局
         */
        AutoLayoutConifg.getInstance().useDeviceSize();

        /**
         * 初始化内存泄漏工具
         */
        initLeakcanary();
    }

    private void initLeakcanary() {
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
    }

    private void initSocket() {
        mClientSocket = new ClientSocket();
    }

    public static void addActivity(Activity activity) {
        if (null == list) {
            list = new ArrayList<>();
        }
        list.add(activity);
    }

    public static void removeAllActivity() {
        if (null != list) {
            for (Activity activity : list) {
                activity.finish();
            }
            list.clear();
            list = null;
        }
    }

    // 对外暴露上下文
    public static App getApplication() {
        return mContext;
    }

    // 对外暴露主线程的handler
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    // 对外暴露主线程
    public static Thread getMainThread() {
        return mMainThread;
    }

    // 对外暴露主线程id
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    // 对外暴露主线程的looper
    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }

    //对外暴露socket接口
    public static ClientSocket getClientSocket() {
        if (mClientSocket == null) {
            mClientSocket = new ClientSocket();
        }
        return mClientSocket;
    }

    private void init() {
        initImageLoader(getApplicationContext());
        //本地图片辅助类初始化
        //LocalImageHelper.init(this);
        if (display == null) {
            WindowManager windowManager = (WindowManager)
                    getSystemService(Context.WINDOW_SERVICE);
            display = windowManager.getDefaultDisplay();
        }
    }

    public static Context getContextObject() {
        return context;
    }


    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY);
        config.denyCacheImageMultipleSizesInMemory();
        config.memoryCacheSize((int) Runtime.getRuntime().maxMemory() / 4);
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 100 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        //修改连接超时时间5秒，下载超时时间5秒
        config.imageDownloader(new BaseImageDownloader(mContext, 5 * 1000, 5 * 1000));
        //		config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public String getCachePath() {
        File cacheDir;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = getExternalCacheDir();
        else
            cacheDir = getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        return cacheDir.getAbsolutePath();
    }

    /**
     * @return
     * @Description： 获取当前屏幕的宽度
     */
    public int getWindowWidth() {
        return display.getWidth();
    }

    /**
     * @return
     * @Description： 获取当前屏幕的高度
     */
    public int getWindowHeight() {
        return display.getHeight();
    }

    /**
     * @return
     * @Description： 获取当前屏幕一半宽度
     */
    public int getHalfWidth() {
        return display.getWidth() / 2;
    }

    /**
     * @return
     * @Description： 获取当前屏幕1/4宽度
     */
    public int getQuarterWidth() {
        return display.getWidth() / 4;

    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    {
        //qq空间分享
        PlatformConfig.setQQZone("1105323708", "QWG9gfyzKsKkFUjV");
        //新浪微博分享
        PlatformConfig.setSinaWeibo("2625556258", "9afc338df026867170405807f58f4e6f", "http://sns.whalecloud.com");
        //微信分享
        PlatformConfig.setWeixin("wx6f8e041d7b879a7d", "LaaWs0WRf1IO77YJ7QCTiNxYyrBQZ9xy");
    }
}
