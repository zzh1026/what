package com.neishenme.what.baidumap.utils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;

import org.seny.android.utils.ALog;

import java.math.BigDecimal;

import static com.neishenme.what.R.drawable.a1;
import static com.neishenme.what.R.drawable.a2;

/**
 * 作者：zhaozh create on 2016/5/27 12:17
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个百度地图转换坐标的工具类
 * .
 * 其作用是 :
 */
public class LocationToBaiduMap {
    private static double EARTH_RADIUS = 6378137;// 地球半径  单位米（CGCS 国测局采用地球半径长坐标）

    /**
     * GPS或其他转换成百度地址
     *
     * @param type
     * @param latitude
     * @param longgitude
     * @return
     */
    public static LatLng toBaiduMapLocation(LocationToBaiduMap.MapType type, double latitude, double longgitude) {
        CoordinateConverter converter = new CoordinateConverter();
        if (type == MapType.GPS) {
            converter.from(CoordinateConverter.CoordType.GPS);
        } else if (type == MapType.NORMAL) {
            converter.from(CoordinateConverter.CoordType.COMMON);
        }
        LatLng ll = new LatLng(latitude, longgitude);
        CoordinateConverter coord = converter.coord(ll);
        return converter.convert();
    }

    /**
     * 使用两组(四个坐标)获取该两点之间的直线地理距离
     *
     * @param P1latitude
     * @param P1longgitude
     * @param P2latitude
     * @param P2longgitude
     * @return
     */
    public static double getDistance(double P1latitude, double P1longgitude, double P2latitude, double P2longgitude) {
//        LatLng p1 = toBaiduMapLocation(MapType.NORMAL, P1latitude, P1longgitude);
//        LatLng p2 = toBaiduMapLocation(MapType.NORMAL, P2latitude, P2longgitude);
//        double distance = DistanceUtil.getDistance(p1, p2);
//        return distance;
        return getEarthDistance(P1latitude, P1longgitude, P2latitude, P2longgitude);
//        double pk = 180 / 3.14169;
//        double a1 = P1latitude / pk;
//        double a2 = P1longgitude / pk;
//        double b1 = P2latitude / pk;
//        double b2 = P2longgitude / pk;
//        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
//        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
//        double t3 = Math.sin(a1) * Math.sin(b1);
//        double tt = Math.acos(t1 + t2 + t3);
//        return 6366000 * tt;
    }

    /**
     * 获取坐标点间的距离(m)
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getEarthDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);

        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000) / 10000;
        return distance;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static String getDistance(double distance) {
        String s = String.valueOf(distance);
        String[] split = s.split("\\.");
        int i = Integer.parseInt(split[0]);
        if (i > 1000) {
            double div = div(i, 1000, 2);
            return div + " km";
        } else {
            if (split.length == 2) {
                if (split[1].length() > 2) {
                    split[1] = split[1].substring(0, 2);
                }
                if (Integer.parseInt(split[1]) != 0) {
                    return i + "." + split[1] + " m";
                }
            }
            return i + " m";
        }
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String getNum(String distance) {
        String string;
        ALog.i(distance);
        if (distance.contains("E")) {
            BigDecimal db = new BigDecimal(distance);
            string = db.toString();
        } else {
            string = distance;
        }
        ALog.i(string);
        String[] split = string.split("\\.");
        int i = Integer.parseInt(split[0]);
        if (i > 1000) {
            double div = div(i, 1000, 2);
            return div + " km";
        } else {
            if (split.length == 2) {
                split[1] = split[1].substring(0, 2);
                return i + "." + split[1] + " m";
            }
            return i + " m";
        }
    }

    public enum MapType {
        GPS,
        NORMAL
    }
}
