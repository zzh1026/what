package com.neishenme.what.bean;

/**
 * Created by Administrator on 2017/2/27.
 */

public class RefundsInfo  extends RBResponse{
    private int code;
    private String message;

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
}
