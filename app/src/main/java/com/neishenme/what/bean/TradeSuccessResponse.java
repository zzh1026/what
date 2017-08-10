package com.neishenme.what.bean;

import java.util.ArrayList;

/**
 * 作者：zhaozh create on 2016/3/26 16:20
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class TradeSuccessResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"buyerEmail":15501209991}
     */

    private int code;
    private String message;

    /**
     * buyerEmail : 15501209991
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
        private long buyerEmail;

        public void setBuyerEmail(long buyerEmail) {
            this.buyerEmail = buyerEmail;
        }

        public long getBuyerEmail() {
            return buyerEmail;
        }


        public String getApplyTime() {
            return applyTime;
        }

        public void setApplyTime(String applyTime) {
            this.applyTime = applyTime;
        }

        private String applyTime;

        public String getCurrentStatus() {
            return currentStatus;
        }

        public void setCurrentStatus(String currentStatus) {
            this.currentStatus = currentStatus;
        }

        private String currentStatus;

        public RefundsStates getStatusMap() {
            return statusMap;
        }

        public void setStatusMap(RefundsStates statusMap) {
            this.statusMap = statusMap;
        }

        private RefundsStates statusMap;
    }
}
