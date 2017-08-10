package com.neishenme.what.bean;

/**
 * 作者：zhaozh create on 2016/3/16 13:59
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个发送短信判断是否成功的类
 * .
 * 其作用是 :
 */
public class SendSuccessResponse extends RBResponse {
    /**
     * code : 1
     * message : success
     * data : {}
     */

    private int code;
    private String message;
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
        private String replaceName;

        public String getReplaceName() {
            return replaceName;
        }

        public void setReplaceName(String replaceName) {
            this.replaceName = replaceName;
        }
    }

}
