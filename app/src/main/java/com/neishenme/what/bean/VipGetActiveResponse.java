package com.neishenme.what.bean;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/1/10.
 */

public class VipGetActiveResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"prizeName":"9.5折优惠券","prizeMessage":"已放入您的钱包优惠券中,快去看看吧!","currentTimes":2,"allTimes":3}
     */

    private int code;
    private String message;
    /**
     * prizeName : 9.5折优惠券
     * prizeMessage : 已放入您的钱包优惠券中,快去看看吧!
     * currentTimes : 2
     * allTimes : 3
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
        private String prizeName;
        private String prizeMessage;

        public String getVipImgUrl() {
            return vipImgUrl;
        }

        public void setVipImgUrl(String vipImgUrl) {
            this.vipImgUrl = vipImgUrl;
        }

        private String vipImgUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        private String title;
        private int currentTimes;
        private int allTimes;

        public String getPrizeName() {
            return prizeName;
        }

        public void setPrizeName(String prizeName) {
            this.prizeName = prizeName;
        }

        public String getPrizeMessage() {
            return prizeMessage;
        }

        public void setPrizeMessage(String prizeMessage) {
            this.prizeMessage = prizeMessage;
        }

        public int getCurrentTimes() {
            return currentTimes;
        }

        public void setCurrentTimes(int currentTimes) {
            this.currentTimes = currentTimes;
        }

        public int getAllTimes() {
            return allTimes;
        }

        public void setAllTimes(int allTimes) {
            this.allTimes = allTimes;
        }
    }
}
