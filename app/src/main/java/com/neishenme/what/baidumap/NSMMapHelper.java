package com.neishenme.what.baidumap;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.VersionInfo;
import com.neishenme.what.baidumap.service.LocationService;

import org.seny.android.utils.ALog;

/**
 * 作者：zhaozh create on 2016/5/26 16:54
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class NSMMapHelper {
    private SDKReceiver mReceiver;
    private Context mContext;
    public LocationService locationService;
    public Vibrator mVibrator;

    // 单例对象
    private static NSMMapHelper instance;

    private NSMMapHelper() {
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static NSMMapHelper getInstance() {
        if (instance == null) {
            instance = new NSMMapHelper();
        }
        return instance;
    }

    /**
     * 初始化方法，调用EaseUI的初始化，
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;

        locationService = new LocationService(mContext);
        mVibrator = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);

        SDKInitializer.initialize(mContext);

        ALog.i("NSM百度地图版本为 v" + VersionInfo.getApiVersion());

        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        mContext.registerReceiver(mReceiver, iFilter);
    }

    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            ALog.i("action: " + s);
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                ALog.i("key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置");
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                ALog.i("key 验证成功! 功能可以正常使用");
            } else if (s
                    .equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                ALog.i("网络出错");
            }
        }
    }
}
