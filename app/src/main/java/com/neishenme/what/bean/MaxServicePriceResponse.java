package com.neishenme.what.bean;

/**
 * 作者：zhaozh create on 2016/5/19 16:05
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class MaxServicePriceResponse extends RBResponse {

    /**
     * code : 1
     * data : {"maxPrice":0}
     */

    private int code;
    /**
     * maxPrice : 0.0
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
        private double maxPrice;

        public void setMaxPrice(double maxPrice) {
            this.maxPrice = maxPrice;
        }

        public double getMaxPrice() {
            return maxPrice;
        }
    }
}
