package com.neishenme.what.bean;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/5/19.
 */

public class ConvertVipConvertedResponse extends RBResponse {

    /**
     * code : 1
     * data : {"trade":{"activitypurse":0,"id":1037562,"jobId":1,"jobType":3,"paymentStatus":0,"payprice":29.97,"price":30,"remark":"","tradeNum":"nsm20170519113629731216","userId":6588,"userpurse":0},"vipCard":{"createTime":1458026558322,"id":1,"intro":"排名靠前，距离展示，身份铭牌，加入特权，免服务费，大幅度降低预付款和保证金","invitefranchise":5,"joinfranchise":10,"lifeDay":30,"name":"会员(30天)","oldPrice":30,"price":30,"state":200,"type":1,"updateTime":1458631575601}}
     * message : success
     */

    private int code;
    /**
     * trade : {"activitypurse":0,"id":1037562,"jobId":1,"jobType":3,"paymentStatus":0,"payprice":29.97,"price":30,"remark":"","tradeNum":"nsm20170519113629731216","userId":6588,"userpurse":0}
     * vipCard : {"createTime":1458026558322,"id":1,"intro":"排名靠前，距离展示，身份铭牌，加入特权，免服务费，大幅度降低预付款和保证金","invitefranchise":5,"joinfranchise":10,"lifeDay":30,"name":"会员(30天)","oldPrice":30,"price":30,"state":200,"type":1,"updateTime":1458631575601}
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
         * activitypurse : 0.0
         * id : 1037562
         * jobId : 1
         * jobType : 3
         * paymentStatus : 0
         * payprice : 29.97
         * price : 30.0
         * remark :
         * tradeNum : nsm20170519113629731216
         * userId : 6588
         * userpurse : 0.0
         */

        private TradeBean trade;
        /**
         * createTime : 1458026558322
         * id : 1
         * intro : 排名靠前，距离展示，身份铭牌，加入特权，免服务费，大幅度降低预付款和保证金
         * invitefranchise : 5
         * joinfranchise : 10
         * lifeDay : 30
         * name : 会员(30天)
         * oldPrice : 30.0
         * price : 30.0
         * state : 200
         * type : 1
         * updateTime : 1458631575601
         */

        private VipCardBean vipCard;

        public TradeBean getTrade() {
            return trade;
        }

        public void setTrade(TradeBean trade) {
            this.trade = trade;
        }

        public VipCardBean getVipCard() {
            return vipCard;
        }

        public void setVipCard(VipCardBean vipCard) {
            this.vipCard = vipCard;
        }

        public static class TradeBean {
            private double activitypurse;
            private int id;
            private int jobId;
            private int jobType;
            private int paymentStatus;
            private double payprice;
            private double price;
            private String remark;
            private String tradeNum;
            private int userId;
            private double userpurse;

            public double getActivitypurse() {
                return activitypurse;
            }

            public void setActivitypurse(double activitypurse) {
                this.activitypurse = activitypurse;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getJobId() {
                return jobId;
            }

            public void setJobId(int jobId) {
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

            public double getUserpurse() {
                return userpurse;
            }

            public void setUserpurse(double userpurse) {
                this.userpurse = userpurse;
            }
        }

        public static class VipCardBean {
            private long createTime;
            private int id;
            private String intro;
            private int invitefranchise;
            private int joinfranchise;
            private int lifeDay;
            private String name;
            private double oldPrice;
            private double price;
            private int state;
            private int type;
            private long updateTime;

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

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public int getInvitefranchise() {
                return invitefranchise;
            }

            public void setInvitefranchise(int invitefranchise) {
                this.invitefranchise = invitefranchise;
            }

            public int getJoinfranchise() {
                return joinfranchise;
            }

            public void setJoinfranchise(int joinfranchise) {
                this.joinfranchise = joinfranchise;
            }

            public int getLifeDay() {
                return lifeDay;
            }

            public void setLifeDay(int lifeDay) {
                this.lifeDay = lifeDay;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public double getOldPrice() {
                return oldPrice;
            }

            public void setOldPrice(double oldPrice) {
                this.oldPrice = oldPrice;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
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
        }
    }
}
