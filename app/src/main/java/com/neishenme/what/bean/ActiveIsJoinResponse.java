package com.neishenme.what.bean;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/3/29.
 */

public class ActiveIsJoinResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"isJoinFlag":"1"}
     */

    private int code;
    private String message;
    /**
     * isJoinFlag : 1
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
        private String isJoinFlag;

        public String getIsJoinFlag() {
            return isJoinFlag;
        }

        public void setIsJoinFlag(String isJoinFlag) {
            this.isJoinFlag = isJoinFlag;
        }
    }
}
