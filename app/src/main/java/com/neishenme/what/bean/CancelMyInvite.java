package com.neishenme.what.bean;

/**
 * Created by Administrator on 2016/6/1.
 */
public class CancelMyInvite extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"status":100}
     */

    private int code;
    private String message;
    /**
     * status : 100
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
        private int status;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
