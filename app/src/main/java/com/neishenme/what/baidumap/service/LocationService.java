package com.neishenme.what.baidumap.service;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

/**
 * @author baidu
 */
public class LocationService {
    private LocationClient client = null;
    private LocationClientOption mOption, DIYoption;
    private Object objLock = new Object();

    /***
     * @param locationContext
     */
    public LocationService(Context locationContext) {
        synchronized (objLock) {
            if (client == null) {
                client = new LocationClient(locationContext);
                client.setLocOption(getDefaultLocationClientOption(CoorType.CoorType_BD09LL, TimeType.TIME_0));
            }
        }
    }

    /***
     * @param listener
     * @return
     */

    public boolean registerListener(BDLocationListener listener) {
        boolean isSuccess = false;
        if (listener != null) {
            client.registerLocationListener(listener);
            isSuccess = true;
        }
        return isSuccess;
    }

    public void unregisterListener(BDLocationListener listener) {
        if (listener != null) {
            client.unRegisterLocationListener(listener);
            if (mOption != null) {
                mOption = null;
            }
        }
    }

    /***
     * @return isSuccessSetOption
     */
    public void setLocationOption(CoorType coorType, TimeType timeType) {
        if (client.isStarted())
            client.stop();
        DIYoption = getDefaultLocationClientOption(coorType, timeType);
        client.setLocOption(DIYoption);
    }

    public LocationClientOption getOption() {
        return DIYoption;
    }

    /***
     * @return DefaultLocationClientOption
     */
    public LocationClientOption getDefaultLocationClientOption(CoorType coorType, TimeType timeType) {

        mOption = new LocationClientOption();
        mOption.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.setOpenGps(true);//打开GPS
        if (coorType == CoorType.CoorType_GCJ02) {
            mOption.setCoorType("gcj02");
        } else {
            mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        }
        if (timeType == TimeType.TIME_LONG) {
            mOption.setScanSpan(1000 * 60 * 5);
        } else if (timeType == TimeType.TIME_QUICK_5000) {
            mOption.setScanSpan(10000);
        } else {
            mOption.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        }
        mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mOption.setNeedDeviceDirect(true);//可选，设置是否需要设备方向结果
        mOption.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mOption.setEnableSimulateGps(true);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集

        return mOption;
    }

    public void start() {
        synchronized (objLock) {
            if (client != null && !client.isStarted()) {
                client.start();
            }
        }
    }

    public void stop() {
        synchronized (objLock) {
            if (client != null && client.isStarted()) {
                client.stop();
            }
        }
    }

    public enum TimeType {
        TIME_0,
        TIME_QUICK_5000,
        TIME_LONG;

        private TimeType() {
        }
    }

    public enum CoorType {
        CoorType_GCJ02,
        CoorType_BD09LL;

        private CoorType() {
        }
    }
}
