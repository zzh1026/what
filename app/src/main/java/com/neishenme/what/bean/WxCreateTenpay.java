package com.neishenme.what.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/4/26.
 */
public class WxCreateTenpay extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"sign":"0FD2E05D85ACBC378200EA55D4E5CDC5","timestamp":1461650485,"partnerid":"1261389301","noncestr":"e5fla3yawb8ljb0ucxp7b3p67oe22ukb","prepayid":"wx20160426140114b56ccfd17e0689436754","package":"Sign=WXPay","appid":"wx65d60c8864c4ce63"}
     */

    private int code;
    private String message;
    /**
     * sign : 0FD2E05D85ACBC378200EA55D4E5CDC5
     * timestamp : 1461650485
     * partnerid : 1261389301
     * noncestr : e5fla3yawb8ljb0ucxp7b3p67oe22ukb
     * prepayid : wx20160426140114b56ccfd17e0689436754
     * package : Sign=WXPay
     * appid : wx65d60c8864c4ce63
     */

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
        private String sign;
        private int timestamp;
        private String partnerid;
        private String noncestr;
        private String prepayid;
        @SerializedName("package")
        private String packageX;
        private String appid;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }
    }
}
