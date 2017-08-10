package com.neishenme.what.bean;

/**
 * 作者：zhaozh create on 2016/6/13 16:07
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class SocketResultBean {

    /**
     * type : swapslocation
     * code : 200
     * message : 成功
     * data : {"inviteId":42228,"longitude":116.448,"latitude":39.90339}
     */

    private String type;
    private int code;
    private String message;
    /**
     * inviteId : 42228
     * longitude : 116.448
     * latitude : 39.90339
     */

    private DataEntity data;

    public void setType(String type) {
        this.type = type;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private int inviteId;
        private double longitude;
        private double latitude;
        private int userId;
        private long lasttime;
        private String msg;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public long getLasttime() {
            return lasttime;
        }

        public void setLasttime(long lasttime) {
            this.lasttime = lasttime;
        }

        public void setInviteId(int inviteId) {
            this.inviteId = inviteId;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public int getInviteId() {
            return inviteId;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }



    @Override
    public String toString() {
        return "SocketResultBean{" +
                "type='" + type + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
