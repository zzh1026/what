package com.neishenme.what.bean;

/**
 * Created by Administrator on 2016/4/28.
 */
public class ActivtyPrice extends RBResponse {
    /**
     * code : 1
     * message : 操作成功
     * data : {"actualPurse":1}
     */

    private int code;
    private String message;
    /**
     * actualPurse : 1.0
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
        private double actualPurse;

        public double getActualPurse() {
            return actualPurse;
        }

        public void setActualPurse(double actualPurse) {
            this.actualPurse = actualPurse;
        }
    }
}
