package com.neishenme.what.bean;

/**
 * 作者：zhaozh create on 2016/6/13 16:12
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class SocketSendBean {

    /**
     * server : location
     * userToken : d5be8be4-eb6d-4d7d-9f13-785fe21fbcf9
     * parameters : {"longitude":116.4484,"inviteId":42228,"latitude":39.90377}
     */

    private String server;
    private String userToken;
    /**
     * longitude : 116.4484
     * inviteId : 42228
     * latitude : 39.90377
     */

    private ParametersEntity parameters;

    public void setServer(String server) {
        this.server = server;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void setParameters(ParametersEntity parameters) {
        this.parameters = parameters;
    }

    public String getServer() {
        return server;
    }

    public String getUserToken() {
        return userToken;
    }

    public ParametersEntity getParameters() {
        return parameters;
    }

    public static class ParametersEntity {
        private double longitude;
        private int inviteId;
        private double latitude;

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public void setInviteId(int inviteId) {
            this.inviteId = inviteId;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public int getInviteId() {
            return inviteId;
        }

        public double getLatitude() {
            return latitude;
        }
    }
}
