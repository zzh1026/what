package com.neishenme.what.bean;

/**
 * Created by Administrator on 2017/3/17.
 */

public class ReleaseMoneyInfo extends RBResponse {

    private ReleaseInfo data;
    private String message;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ReleaseInfo getData() {
        return data;
    }

    public void setData(ReleaseInfo data) {
        this.data = data;
    }
}
