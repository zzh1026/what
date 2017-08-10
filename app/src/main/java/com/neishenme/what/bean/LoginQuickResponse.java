package com.neishenme.what.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 作者：zhaozh create on 2016/5/12 14:35
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class LoginQuickResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"new":false,"token":"ff4c722b-80a3-4f36-aceb-3c4dd1550336"}
     */

    private int code;
    private String message;
    /**
     * new : false
     * token : ff4c722b-80a3-4f36-aceb-3c4dd1550336
     */

    private DataEntity data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(DataEntity data) {
        this.data = data;
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
        @SerializedName("new")
        private boolean newX;
        private String token;

        public void setNewX(boolean newX) {
            this.newX = newX;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public boolean isNewX() {
            return newX;
        }

        public String getToken() {
            return token;
        }
    }
}
