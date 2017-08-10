package com.neishenme.what.bean;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/3/29.
 */

public class ActiveJoinInfoResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"imgUrl":"http://192.168.3.99:8888/nsm/activity/ucsb09swlu5b7w5uhndgqgs0/source.png","time":1491213600000,"title":"报名费用","trade":{"id":1001513,"jobType":6,"payprice":100,"price":100,"jobId":8,"remark":"","userId":2292,"tradeNum":"nsm20170329050658160394","paymentStatus":0,"activitypurse":0,"userpurse":0},"name":"加入活动"}
     */

    private int code;
    private String message;
    /**
     * imgUrl : http://192.168.3.99:8888/nsm/activity/ucsb09swlu5b7w5uhndgqgs0/source.png
     * time : 1491213600000
     * title : 报名费用
     * trade : {"id":1001513,"jobType":6,"payprice":100,"price":100,"jobId":8,"remark":"","userId":2292,"tradeNum":"nsm20170329050658160394","paymentStatus":0,"activitypurse":0,"userpurse":0}
     * name : 加入活动
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
        private String imgUrl;
        private long time;
        private String title;
        /**
         * id : 1001513
         * jobType : 6
         * payprice : 100
         * price : 100
         * jobId : 8
         * remark :
         * userId : 2292
         * tradeNum : nsm20170329050658160394
         * paymentStatus : 0
         * activitypurse : 0
         * userpurse : 0
         */

        private TradeBean trade;
        private String name;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public TradeBean getTrade() {
            return trade;
        }

        public void setTrade(TradeBean trade) {
            this.trade = trade;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static class TradeBean {
            private int id;
            private int jobType;
            private double payprice;
            private double price;
            private int jobId;
            private String remark;
            private int userId;
            private String tradeNum;
            private int paymentStatus;
            private int activitypurse;
            private int userpurse;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getJobType() {
                return jobType;
            }

            public void setJobType(int jobType) {
                this.jobType = jobType;
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

            public int getJobId() {
                return jobId;
            }

            public void setJobId(int jobId) {
                this.jobId = jobId;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getTradeNum() {
                return tradeNum;
            }

            public void setTradeNum(String tradeNum) {
                this.tradeNum = tradeNum;
            }

            public int getPaymentStatus() {
                return paymentStatus;
            }

            public void setPaymentStatus(int paymentStatus) {
                this.paymentStatus = paymentStatus;
            }

            public int getActivitypurse() {
                return activitypurse;
            }

            public void setActivitypurse(int activitypurse) {
                this.activitypurse = activitypurse;
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
