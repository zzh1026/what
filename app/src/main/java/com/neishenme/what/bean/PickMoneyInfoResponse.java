package com.neishenme.what.bean;

/**
 * 作者：zhaozh create on 2016/3/27 13:29
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class PickMoneyInfoResponse extends RBResponse {


    /**
     * code : 1
     * message : success
     * data : {"priceMax":0,"currentStatus":50,"isvip":1}
     */

    private int code;
    private String message;
    /**
     * priceMax : 0.0
     * currentStatus : 50
     * isvip : 1
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
        private double priceMax;
        private int currentStatus;
        private int isvip;

        public double getPriceMax() {
            return priceMax;
        }

        public void setPriceMax(double priceMax) {
            this.priceMax = priceMax;
        }

        public int getCurrentStatus() {
            return currentStatus;
        }

        public void setCurrentStatus(int currentStatus) {
            this.currentStatus = currentStatus;
        }

        public int getIsvip() {
            return isvip;
        }

        public void setIsvip(int isvip) {
            this.isvip = isvip;
        }
    }
}
