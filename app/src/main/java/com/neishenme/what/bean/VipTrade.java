package com.neishenme.what.bean;

/**
 * Created by Administrator on 2016/5/27.
 */
public class VipTrade extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"vipCard":{"id":1,"name":"购买1月","price":0.01,"oldPrice":30,"lifeDay":30,"joinfranchise":10,"invitefranchise":5,"intro":"会员身份尊享,另赠5次发单特权和10次加入特权","state":200,"createTime":1458026558322,"updateTime":1458631575601},"trade":{"createTime":1459326832691,"jobType":3,"thirdPartyTradeNum":null,"refundCount":0,"jobId":1,"updateTime":1459326832691,"remark":"","tradeNum":"nsm20160330043352945656","outTradeNo":null,"paymentStatus":0,"deviceInfo":null,"activitypurse":0,"leftmoney":0.01,"id":134030,"refundAmount":0,"price":0.01,"userId":577,"userpurse":0}}
     */

    private int code;
    private String message;
    /**
     * vipCard : {"id":1,"name":"购买1月","price":0.01,"oldPrice":30,"lifeDay":30,"joinfranchise":10,"invitefranchise":5,"intro":"会员身份尊享,另赠5次发单特权和10次加入特权","state":200,"createTime":1458026558322,"updateTime":1458631575601}
     * trade : {"createTime":1459326832691,"jobType":3,"thirdPartyTradeNum":null,"refundCount":0,"jobId":1,"updateTime":1459326832691,"remark":"","tradeNum":"nsm20160330043352945656","outTradeNo":null,"paymentStatus":0,"deviceInfo":null,"activitypurse":0,"leftmoney":0.01,"id":134030,"refundAmount":0,"price":0.01,"userId":577,"userpurse":0}
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
        /**
         * id : 1
         * name : 购买1月
         * price : 0.01
         * oldPrice : 30
         * lifeDay : 30
         * joinfranchise : 10
         * invitefranchise : 5
         * intro : 会员身份尊享,另赠5次发单特权和10次加入特权
         * state : 200
         * createTime : 1458026558322
         * updateTime : 1458631575601
         */

        private VipCardBean vipCard;
        /**
         * createTime : 1459326832691
         * jobType : 3
         * thirdPartyTradeNum : null
         * refundCount : 0
         * jobId : 1
         * updateTime : 1459326832691
         * remark :
         * tradeNum : nsm20160330043352945656
         * outTradeNo : null
         * paymentStatus : 0
         * deviceInfo : null
         * activitypurse : 0
         * leftmoney : 0.01
         * id : 134030
         * refundAmount : 0
         * price : 0.01
         * userId : 577
         * userpurse : 0
         */

        private TradeBean trade;

        public VipCardBean getVipCard() {
            return vipCard;
        }

        public void setVipCard(VipCardBean vipCard) {
            this.vipCard = vipCard;
        }

        public TradeBean getTrade() {
            return trade;
        }

        public void setTrade(TradeBean trade) {
            this.trade = trade;
        }

        public static class VipCardBean {
            private int id;
            private String name;
            private double price;
            private int oldPrice;
            private int lifeDay;
            private int joinfranchise;
            private int invitefranchise;
            private String intro;
            private int state;
            private long createTime;
            private long updateTime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getOldPrice() {
                return oldPrice;
            }

            public void setOldPrice(int oldPrice) {
                this.oldPrice = oldPrice;
            }

            public int getLifeDay() {
                return lifeDay;
            }

            public void setLifeDay(int lifeDay) {
                this.lifeDay = lifeDay;
            }

            public int getJoinfranchise() {
                return joinfranchise;
            }

            public void setJoinfranchise(int joinfranchise) {
                this.joinfranchise = joinfranchise;
            }

            public int getInvitefranchise() {
                return invitefranchise;
            }

            public void setInvitefranchise(int invitefranchise) {
                this.invitefranchise = invitefranchise;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }
        }

        public static class TradeBean {
            private long createTime;
            private int jobType;
            private Object thirdPartyTradeNum;
            private int refundCount;
            private int jobId;
            private long updateTime;
            private String remark;
            private String tradeNum;
            private Object outTradeNo;
            private int paymentStatus;
            private Object deviceInfo;
            private int activitypurse;
            private double leftmoney;
            private int id;
            private int refundAmount;
            private double price;
            private int userId;
            private int userpurse;

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getJobType() {
                return jobType;
            }

            public void setJobType(int jobType) {
                this.jobType = jobType;
            }

            public Object getThirdPartyTradeNum() {
                return thirdPartyTradeNum;
            }

            public void setThirdPartyTradeNum(Object thirdPartyTradeNum) {
                this.thirdPartyTradeNum = thirdPartyTradeNum;
            }

            public int getRefundCount() {
                return refundCount;
            }

            public void setRefundCount(int refundCount) {
                this.refundCount = refundCount;
            }

            public int getJobId() {
                return jobId;
            }

            public void setJobId(int jobId) {
                this.jobId = jobId;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
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

            public Object getOutTradeNo() {
                return outTradeNo;
            }

            public void setOutTradeNo(Object outTradeNo) {
                this.outTradeNo = outTradeNo;
            }

            public int getPaymentStatus() {
                return paymentStatus;
            }

            public void setPaymentStatus(int paymentStatus) {
                this.paymentStatus = paymentStatus;
            }

            public Object getDeviceInfo() {
                return deviceInfo;
            }

            public void setDeviceInfo(Object deviceInfo) {
                this.deviceInfo = deviceInfo;
            }

            public int getActivitypurse() {
                return activitypurse;
            }

            public void setActivitypurse(int activitypurse) {
                this.activitypurse = activitypurse;
            }

            public double getLeftmoney() {
                return leftmoney;
            }

            public void setLeftmoney(double leftmoney) {
                this.leftmoney = leftmoney;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getRefundAmount() {
                return refundAmount;
            }

            public void setRefundAmount(int refundAmount) {
                this.refundAmount = refundAmount;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getUserpurse() {
                return userpurse;
            }

            public void setUserpurse(int userpurse) {
                this.userpurse = userpurse;
            }
        }
    }
}
