package com.neishenme.what.common;

import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.neishenme.what.bean.CityLocationResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.LocationUtils;

import org.seny.android.utils.ALog;

import java.util.HashMap;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/12/26.
 * <p>
 * 城市定位配置类, 对 当前定位城市和 用户选择城市进行定位 ,以此为标准来 获取准确的城市 邀请信息和是否为异地
 */

public class CityLocationConfig {

    /**
     * 定位城市的名字和id ,该城市为定位城市
     */
    public static String cityLocation = "";
    public static int cityLocationId = 0;

    public static double cityLatitude = 39.915291;     //当前城市的经纬度
    public static double cityLonggitude = 116.403857;

    /**
     * 选择的城市名字和id, 该城市为用户切换城市的切换城市
     */
    public static String citySelected = "";
    public static int citySelectedId = 0;

    public static OnNetResponse mOnNetResponse;
    public static OnCitySuccess mOnCitySuccess;

    public static void getLocationUtil() {
        mOnCitySuccess = null;
        getLocationUtil(null);
    }

    public static void getLocationUtil(OnCitySuccess onCitySuccess) {
        mOnCitySuccess = onCitySuccess;
        LocationUtils.getLocationByGCJ_02(new LocationUtils.OnGetLocationListener() {
            @Override
            public void onGetLocation(double latitude, double longgitude, BDLocation location) {
                requestLocation(location.getProvince(), location.getCity(), latitude, longgitude);
            }

            @Override
            public void onGetError() {
                requestLocation("", "", cityLatitude, cityLonggitude);
            }
        });
    }

    private static void requestLocation(String province, String citydouble, double latitude, double longgitude) {
        mOnNetResponse = new OnNetResponse();
        HashMap<String, String> params = new HashMap<>();
        params.put("province", province);
        params.put("city", citydouble);
        params.put("latitude", String.valueOf(latitude));
        params.put("longitude", String.valueOf(longgitude));
        String city = citydouble.endsWith("市") ? citydouble.substring(0, citydouble.length() - 1) : citydouble;
        CityLocationConfig.cityLocation = city;
        CityLocationConfig.citySelected = city;
        CityLocationConfig.cityLatitude = latitude;
        CityLocationConfig.cityLonggitude = longgitude;
        HttpLoader.get(ConstantsWhatNSM.URL_ACTIVE_GET_CITY_LOCATION, params, CityLocationResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_GET_CITY_LOCAGION, mOnNetResponse, false).setTag(mOnNetResponse);
    }

    public static class OnNetResponse implements HttpLoader.ResponseListener {

        @Override
        public void onGetResponseSuccess(int requestCode, RBResponse response) {
            if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_GET_CITY_LOCAGION
                    && response instanceof CityLocationResponse) {
                CityLocationResponse cityLocationResponse = (CityLocationResponse) response;
                if (1 == cityLocationResponse.getCode()) {
                    CityLocationResponse.DataBean.ResultdataBean resultdataBean =
                            cityLocationResponse.getData().getResultdata().get(0);
                    cityLocation = resultdataBean.getName();
                    citySelected = resultdataBean.getName();
                    cityLocationId = resultdataBean.getAreaId();
                    citySelectedId = resultdataBean.getAreaId();
                    ALog.i("确认位置成功! 地址为: " + cityLocation + ",   areaId为: " + cityLocationId);
                    if (mOnCitySuccess != null) {
                        mOnCitySuccess.onCitySuccess();
                        mOnCitySuccess = null;
                    }
                }
            }
        }

        @Override
        public void onGetResponseError(int requestCode, VolleyError error) {
            if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_GET_CITY_LOCAGION) {
                if (mOnCitySuccess != null) {
                    mOnCitySuccess.onCityError();
                    ALog.i("确认位置失败! 此时默认的地址为: " + cityLocation + ",   areaId为: " + cityLocationId);
                }
            }
        }
    }

    public static interface OnCitySuccess {
        void onCitySuccess();

        void onCityError();
    }
}
