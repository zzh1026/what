package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/12/26.
 *
 *  我的优惠券model
 */

public class MyCouPonsResponse extends RBResponse {

    /**
     * code : 1
     * data : {"coupons":[{"cash":50,"createTime":1482221752530,"endTime":1483199999999,"id":3,"lifespan":1,"minpurse":1000,"number":"NSMC20161200003","obtainType":0,"rebate":0,"restricts":1,"startTime":1482163200000,"status":100,"type":1,"updateTime":1482221752530,"userId":46},{"cash":0,"createTime":1482221801881,"endTime":1483027199999,"id":4,"lifespan":1,"minpurse":1200,"number":"NSMC20161200004","obtainType":0,"rebate":90,"restricts":1,"startTime":1482163200000,"status":100,"type":0,"updateTime":1482221801881,"userId":46}],"hasMore":false}
     * message : success
     */

    private int code;
    /**
     * coupons : [{"cash":50,"createTime":1482221752530,"endTime":1483199999999,"id":3,"lifespan":1,"minpurse":1000,"number":"NSMC20161200003","obtainType":0,"rebate":0,"restricts":1,"startTime":1482163200000,"status":100,"type":1,"updateTime":1482221752530,"userId":46},{"cash":0,"createTime":1482221801881,"endTime":1483027199999,"id":4,"lifespan":1,"minpurse":1200,"number":"NSMC20161200004","obtainType":0,"rebate":90,"restricts":1,"startTime":1482163200000,"status":100,"type":0,"updateTime":1482221801881,"userId":46}]
     * hasMore : false
     */

    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        private boolean hasMore;
        /**
         * cash : 50
         * createTime : 1482221752530
         * endTime : 1483199999999
         * id : 3
         * lifespan : 1
         * minpurse : 1000
         * number : NSMC20161200003
         * obtainType : 0
         * rebate : 0
         * restricts : 1
         * startTime : 1482163200000
         * status : 100
         * type : 1
         * updateTime : 1482221752530
         * userId : 46
         */

        private List<CouponsBean> coupons;

        public boolean isHasMore() {
            return hasMore;
        }

        public void setHasMore(boolean hasMore) {
            this.hasMore = hasMore;
        }

        public List<CouponsBean> getCoupons() {
            return coupons;
        }

        public void setCoupons(List<CouponsBean> coupons) {
            this.coupons = coupons;
        }

        public static class CouponsBean {
            private String cash;
            private long createTime;
            private long endTime;
            private int id;
            private int lifespan;
            private int minpurse;
            private String number;
            private int obtainType;
            private int rebate;
            private int restricts;
            private long startTime;
            private int status;
            private int type;
            private long updateTime;
            private int userId;

            public String getCash() {
                return cash;
            }

            public void setCash(String cash) {
                this.cash = cash;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public long getEndTime() {
                return endTime;
            }

            public void setEndTime(long endTime) {
                this.endTime = endTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getLifespan() {
                return lifespan;
            }

            public void setLifespan(int lifespan) {
                this.lifespan = lifespan;
            }

            public int getMinpurse() {
                return minpurse;
            }

            public void setMinpurse(int minpurse) {
                this.minpurse = minpurse;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public int getObtainType() {
                return obtainType;
            }

            public void setObtainType(int obtainType) {
                this.obtainType = obtainType;
            }

            public int getRebate() {
                return rebate;
            }

            public void setRebate(int rebate) {
                this.rebate = rebate;
            }

            public int getRestricts() {
                return restricts;
            }

            public void setRestricts(int restricts) {
                this.restricts = restricts;
            }

            public long getStartTime() {
                return startTime;
            }

            public void setStartTime(long startTime) {
                this.startTime = startTime;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }
    }
}
