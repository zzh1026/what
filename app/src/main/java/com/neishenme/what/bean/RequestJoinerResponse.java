package com.neishenme.what.bean;

import static android.R.attr.id;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/1/4.
 * <p>
 * <p>
 * 新的用户加入活动的model
 * <p>
 * 1,主界面 点击申请加入
 * 2,邀请详情加入者 界面  点击申请加入
 */

public class RequestJoinerResponse extends RBResponse {

    /**
     * code : 1
     * data : {"joiner":{"acceptTime":0,"acceptType":0,"createTime":1483524247473,"id":801327,"inviteId":118141,"isfranchise":0,"ispay":1,"newstatus":100,"setout":0,"signing":0,"status":100,"userId":2294},"trade":{"activitypurse":0,"id":905330,"jobId":801327,"jobType":2,"paymentStatus":100,"payprice":0,"price":0,"remark":"","tradeNum":"nsm20170104060407538391","userId":2294,"userpurse":0}}
     * message : success
     */

    private int code;
    /**
     * joiner : {"acceptTime":0,"acceptType":0,"createTime":1483524247473,"id":801327,"inviteId":118141,"isfranchise":0,"ispay":1,"newstatus":100,"setout":0,"signing":0,"status":100,"userId":2294}
     * trade : {"activitypurse":0,"id":905330,"jobId":801327,"jobType":2,"paymentStatus":100,"payprice":0,"price":0,"remark":"","tradeNum":"nsm20170104060407538391","userId":2294,"userpurse":0}
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
        /**
         * acceptTime : 0
         * acceptType : 0
         * createTime : 1483524247473
         * id : 801327
         * inviteId : 118141
         * isfranchise : 0
         * ispay : 1
         * newstatus : 100
         * setout : 0
         * signing : 0
         * status : 100
         * userId : 2294
         */

        private JoinerBean joiner;
        /**
         * activitypurse : 0
         * id : 905330
         * jobId : 801327
         * jobType : 2
         * paymentStatus : 100
         * payprice : 0
         * price : 0
         * remark :
         * tradeNum : nsm20170104060407538391
         * userId : 2294
         * userpurse : 0
         */

        private TradeBean trade;

        public JoinerBean getJoiner() {
            return joiner;
        }

        public void setJoiner(JoinerBean joiner) {
            this.joiner = joiner;
        }

        public TradeBean getTrade() {
            return trade;
        }

        public void setTrade(TradeBean trade) {
            this.trade = trade;
        }

        public static class JoinerBean {
            private long acceptTime;
            private int acceptType;
            private long createTime;
            private int id;
            private int inviteId;
            private int isfranchise;
            private int ispay;
            private int newstatus;
            private int setout;
            private int signing;
            private int status;
            private int userId;

            public long getAcceptTime() {
                return acceptTime;
            }

            public void setAcceptTime(long acceptTime) {
                this.acceptTime = acceptTime;
            }

            public int getAcceptType() {
                return acceptType;
            }

            public void setAcceptType(int acceptType) {
                this.acceptType = acceptType;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getInviteId() {
                return inviteId;
            }

            public void setInviteId(int inviteId) {
                this.inviteId = inviteId;
            }

            public int getIsfranchise() {
                return isfranchise;
            }

            public void setIsfranchise(int isfranchise) {
                this.isfranchise = isfranchise;
            }

            public int getIspay() {
                return ispay;
            }

            public void setIspay(int ispay) {
                this.ispay = ispay;
            }

            public int getNewstatus() {
                return newstatus;
            }

            public void setNewstatus(int newstatus) {
                this.newstatus = newstatus;
            }

            public int getSetout() {
                return setout;
            }

            public void setSetout(int setout) {
                this.setout = setout;
            }

            public int getSigning() {
                return signing;
            }

            public void setSigning(int signing) {
                this.signing = signing;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }

        public static class TradeBean {
            private String activitypurse;
            private long id;
            private long jobId;
            private int jobType;
            private int paymentStatus;
            private double payprice;
            private double price;
            private String remark;
            private String tradeNum;
            private int userId;
            private String userpurse;

            public String getActivitypurse() {
                return activitypurse;
            }

            public void setActivitypurse(String activitypurse) {
                this.activitypurse = activitypurse;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public long getJobId() {
                return jobId;
            }

            public void setJobId(long jobId) {
                this.jobId = jobId;
            }

            public int getJobType() {
                return jobType;
            }

            public void setJobType(int jobType) {
                this.jobType = jobType;
            }

            public int getPaymentStatus() {
                return paymentStatus;
            }

            public void setPaymentStatus(int paymentStatus) {
                this.paymentStatus = paymentStatus;
            }

            public double getPayprice() {
                return payprice;
            }

            public void setPayprice(double payprice) {
                this.payprice = payprice;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getTradeNum() {
                return tradeNum;
            }

            public void setTradeNum(String tradeNum) {
                this.tradeNum = tradeNum;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getUserpurse() {
                return userpurse;
            }

            public void setUserpurse(String userpurse) {
                this.userpurse = userpurse;
            }
        }
    }
}
