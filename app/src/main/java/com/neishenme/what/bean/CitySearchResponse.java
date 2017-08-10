package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/12/12.
 */

public class CitySearchResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"citylist":[{"id":43,"name":"保定","fulls":"baoding","longitude":115.48,"latitude":38.85,"hotflag":0},{"id":74,"name":"白城","fulls":"baicheng","longitude":122.82,"latitude":45.63,"hotflag":0},{"id":188,"name":"巴中","fulls":"bazhong","longitude":106.73,"latitude":31.86,"hotflag":0},{"id":222,"name":"宝鸡","fulls":"baoji","longitude":107.15,"latitude":34.38,"hotflag":0},{"id":248,"name":"北海","fulls":"beihai","longitude":109.12,"latitude":21.49,"hotflag":0},{"id":251,"name":"百色","fulls":"baise","longitude":106.62,"latitude":23.91,"hotflag":0},{"id":262,"name":"包头","fulls":"baotou","longitude":110,"latitude":40.58,"hotflag":0},{"id":280,"name":"蚌埠","fulls":"bangbu","longitude":117.34,"latitude":32.93,"hotflag":0}]}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 43
         * name : 保定
         * fulls : baoding
         * longitude : 115.48
         * latitude : 38.85
         * hotflag : 0
         */

        private List<CitylistBean> citylist;

        public List<CitylistBean> getCitylist() {
            return citylist;
        }

        public void setCitylist(List<CitylistBean> citylist) {
            this.citylist = citylist;
        }

        public static class CitylistBean {
            private int id;
            private String name;
            private String fulls;
            private double longitude;
            private double latitude;
            private int hotflag;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getFulls() {
                return fulls;
            }

            public void setFulls(String fulls) {
                this.fulls = fulls;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public int getHotflag() {
                return hotflag;
            }

            public void setHotflag(int hotflag) {
                this.hotflag = hotflag;
            }
        }
    }
}
