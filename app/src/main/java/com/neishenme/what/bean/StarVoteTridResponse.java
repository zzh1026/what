package com.neishenme.what.bean;

/**
 * 这个类的作用是  投票成功 后获取订单的bean对象
 * <p>
 * Created by zhaozh on 2017/4/19.
 */

public class StarVoteTridResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"title":"明星活动","trade":{"id":1019268,"jobType":7,"payprice":0.1,"price":0.1,"jobId":75,"remark":"","userId":2294,"tradeNum":"nsm20170420024401650879","paymentStatus":0,"activitypurse":0,"userpurse":0},"starName":"陈静","starLogo":"http://192.168.3.99:8888/nsm/activity/06ux8srbgtbq4orj9agejknr/source.png","tickets":10}
     */

    private int code;
    private String message;
    /**
     * title : 明星活动
     * trade : {"id":1019268,"jobType":7,"payprice":0.1,"price":0.1,"jobId":75,"remark":"","userId":2294,"tradeNum":"nsm20170420024401650879","paymentStatus":0,"activitypurse":0,"userpurse":0}
     * starName : 陈静
     * starLogo : http://192.168.3.99:8888/nsm/activity/06ux8srbgtbq4orj9agejknr/source.png
     * tickets : 10
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
        private String title;
        /**
         * id : 1019268
         * jobType : 7
         * payprice : 0.1
         * price : 0.1
         * jobId : 75
         * remark :
         * userId : 2294
         * tradeNum : nsm20170420024401650879
         * paymentStatus : 0
         * activitypurse : 0
         * userpurse : 0
         */

        private TradeBean trade;
        private String starName;
        private String starLogo;
        private int tickets;

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

        public String getStarName() {
            return starName;
        }

        public void setStarName(String starName) {
            this.starName = starName;
        }

        public String getStarLogo() {
            return starLogo;
        }

        public void setStarLogo(String starLogo) {
            this.starLogo = starLogo;
        }

        public int getTickets() {
            return tickets;
        }

        public void setTickets(int tickets) {
            this.tickets = tickets;
        }

        public static class TradeBean {
            private String id;
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

            public String getId() {
                return id;
            }

            public void setId(String id) {
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
