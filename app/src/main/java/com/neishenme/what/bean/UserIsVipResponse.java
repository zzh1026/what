package com.neishenme.what.bean;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/12/26.
 * 监测用户是否为vip用户的model
 */

public class UserIsVipResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"isvip":false}
     */

    private int code;
    private String message;
    /**
     * isvip : false
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
        private boolean isvip;

        public boolean isIsvip() {
            return isvip;
        }

        public void setIsvip(boolean isvip) {
            this.isvip = isvip;
        }
    }
}
