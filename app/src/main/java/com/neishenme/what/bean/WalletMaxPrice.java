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
public class WalletMaxPrice extends RBResponse {

    /**
     * code : 1
     * data : {"priceMax":0.01}
     */

    private int code;
    /**
     * priceMax : 0.01
     */

    private DataEntity data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private double priceMax;

        public void setPriceMax(double priceMax) {
            this.priceMax = priceMax;
        }

        public double getPriceMax() {
            return priceMax;
        }
    }
}
