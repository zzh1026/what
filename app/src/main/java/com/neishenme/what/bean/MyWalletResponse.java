package com.neishenme.what.bean;

import com.neishenme.what.utils.NSMTypeUtils;

/**
 * 作者：zhaozh create on 2016/3/21 14:23
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class MyWalletResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"purse":6,"activitypurse":0}
     */

    private int code;
    private String message;
    /**
     * purse : 6.0
     * activitypurse : 0.0
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
        private double purse;
        private double activitypurse;

        public void setPurse(double purse) {
            this.purse = purse;
        }

        public void setActivitypurse(double activitypurse) {
            this.activitypurse = activitypurse;
        }

        public double getPurse() {
            return purse;
        }

        public double getActivitypurse() {
            return activitypurse;
        }

        public double getAllPurse() {
            return NSMTypeUtils.add(purse, activitypurse);
        }
    }
}
