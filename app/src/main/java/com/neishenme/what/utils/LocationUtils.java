package com.neishenme.what.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.neishenme.what.application.App;
import com.neishenme.what.baidumap.NSMMapHelper;
import com.neishenme.what.baidumap.service.LocationService;

import org.seny.android.utils.ALog;

/**
 * 作者：zhaozh create on 2016/3/14 16:14
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个获取用户位置信息的类
 * .
 * 其作用是 :
 */
public class LocationUtils {
    private static LocationGet locationGet = new LocationGet(App.getApplication());
    private static boolean isGetLocation = false;   //是否获取位置
    private static boolean isGetLong = false;   //是否获取多次
    private static LocationService locationService = NSMMapHelper.getInstance().locationService;
    private static OnGetLocationListener mOnGetLocationListener;

    /**
     * 经度
     *
     * @param context
     * @return
     */
    public static double getLatitude(Context context) {
        locationGet = new LocationGet(context);
        return locationGet.getLatitude();
    }

    /**
     * 纬度
     *
     * @param context
     * @return
     */
    public static double getLongitude(Context context) {
        locationGet = new LocationGet(context);
        return locationGet.getLongitude();
    }

    public static void getLocations(Context context, GetLocations getLocations) {
        locationGet = new LocationGet(context);
        locationGet.getLocations(getLocations);
    }

    public interface GetLocations {
        void getLocations(double latitude, double longgitude);
    }

    public static void setIsGetLong(boolean getLong) {
        isGetLong = getLong;
    }

    public static void getLocation(OnGetLocationListener mOnGetLocationListener, LocationService.CoorType coorType, LocationService.TimeType timeType) {
        LocationUtils.mOnGetLocationListener = mOnGetLocationListener;
        if (!isGetLong) {   //这里可以手动赋值,如果默认的不是获取多次才需要对isgetlong赋值,如果手动设置了isgetlong为true,就不在进行判断
            isGetLong = !(timeType == LocationService.TimeType.TIME_0);
        }
        locationService.registerListener(mListener);
        isGetLocation = true;
        locationService.setLocationOption(coorType, timeType);
        locationService.start();
    }

    public static void getLocation(OnGetLocationListener mOnGetLocationListener, LocationService.CoorType coorType) {
        getLocation(mOnGetLocationListener, coorType, LocationService.TimeType.TIME_0);
    }

    /**
     * 获取位置,默认一次
     *
     * @param mOnGetLocationListener
     */
    public static void getLocationByGCJ_02(OnGetLocationListener mOnGetLocationListener) {
        getLocation(mOnGetLocationListener, LocationService.CoorType.CoorType_GCJ02);
    }

    /**
     * 获取位置一次
     *
     * @param mOnGetLocationListener
     */
    public static void getLocationByBD0911Once(OnGetLocationListener mOnGetLocationListener) {
        getLocation(mOnGetLocationListener, LocationService.CoorType.CoorType_BD09LL, LocationService.TimeType.TIME_0);
    }

    /**
     * 获取位置多次
     *
     * @param mOnGetLocationListener
     */
    public static void getLocationByBD0911Quick_5000(OnGetLocationListener mOnGetLocationListener) {
        getLocation(mOnGetLocationListener, LocationService.CoorType.CoorType_BD09LL, LocationService.TimeType.TIME_QUICK_5000);
    }

    public interface OnGetLocationListener {
        void onGetLocation(double latitude, double longgitude, BDLocation location);

        void onGetError();
    }

    private static BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (isGetLocation) {
                if (!isGetLong)
                    isGetLocation = false;
                if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                    /**
                     * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                     * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                     */
//                    Gps gps = PositionUtil.gcj_To_Gps84(location.getLatitude(), location.getLongitude());
//                    double latitude = gps.getWgLat();
//                    double longgitude = gps.getWgLon();
                    if (null != mOnGetLocationListener) {
                        mOnGetLocationListener.onGetLocation(location.getLatitude(), location.getLongitude(), location);
                    }
                } else {
                    if (null != mOnGetLocationListener) {
                        mOnGetLocationListener.onGetError();
                    }
                }
            }
            if (!isGetLong) {
                locationService.unregisterListener(mListener);
                locationService.stop();
                mOnGetLocationListener = null;
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    /**
     * 如果是多次获取则需要在合适的时候调用该方法;
     */
    public static void cancelGetLocation() {
        isGetLocation = false;
        isGetLong = false;
        locationService.unregisterListener(mListener);
        locationService.stop();
        mOnGetLocationListener = null;
    }
}
