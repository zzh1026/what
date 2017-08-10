package com.neishenme.what.bean;

/**
 * 作者：zhaozh create on 2016/6/13 16:43
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class SocketLoginBean {

    /**
     * userToken : ab8e33ad-b0b2-42b5-92ad-5bfe22933ad7
     * server : location
     * parameters : {"longitude":0,"latitude":0}
     */

    private String userToken;
    private String server;
    /**
     * longitude : 0
     * latitude : 0
     */

    private ParametersEntity parameters;

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setParameters(ParametersEntity parameters) {
        this.parameters = parameters;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getServer() {
        return server;
    }

    public ParametersEntity getParameters() {
        return parameters;
    }

    public static class ParametersEntity {
        private double longitude;
        private double latitude;

        public int getAreaId() {
            return areaId;
        }

        public void setAreaId(int areaId) {
            this.areaId = areaId;
        }

        private int areaId;

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getLatitude() {
            return latitude;
        }
    }
}
