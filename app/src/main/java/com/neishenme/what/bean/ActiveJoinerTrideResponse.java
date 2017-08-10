package com.neishenme.what.bean;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/11/23.
 */

public class ActiveJoinerTrideResponse extends RBResponse {

    /**
     * code : 1
     * data : {"joindetail":{"id":1,"startNum":1,"status":0,"takeMeOutId":1,"takeMeOutJoinId":1,"tickets":2,"ticketsPrefix":"WHXaF2DfDIaIQ9LrOVC","userId":666},"trade":{"activitypurse":0,"id":685600,"jobId":1,"jobType":5,"leftmoney":2,"paymentStatus":0,"price":2,"remark":"","tradeNum":"nsm20161123022356422455","userId":666,"userpurse":0}}
     * message : success
     */

    private int code;
    /**
     * joindetail : {"id":1,"startNum":1,"status":0,"takeMeOutId":1,"takeMeOutJoinId":1,"tickets":2,"ticketsPrefix":"WHXaF2DfDIaIQ9LrOVC","userId":666}
     * trade : {"activitypurse":0,"id":685600,"jobId":1,"jobType":5,"leftmoney":2,"paymentStatus":0,"price":2,"remark":"","tradeNum":"nsm20161123022356422455","userId":666,"userpurse":0}
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
         * id : 1
         * startNum : 1
         * status : 0
         * takeMeOutId : 1
         * takeMeOutJoinId : 1
         * tickets : 2
         * ticketsPrefix : WHXaF2DfDIaIQ9LrOVC
         * userId : 666
         */

        private JoindetailBean joindetail;
        /**
         * activitypurse : 0.0
         * id : 685600
         * jobId : 1
         * jobType : 5
         * leftmoney : 2.0
         * paymentStatus : 0
         * price : 2.0
         * remark :
         * tradeNum : nsm20161123022356422455
         * userId : 666
         * userpurse : 0.0
         */

        private TradeBean trade;

        public JoindetailBean getJoindetail() {
            return joindetail;
        }

        public void setJoindetail(JoindetailBean joindetail) {
            this.joindetail = joindetail;
        }

        public TradeBean getTrade() {
            return trade;
        }

        public void setTrade(TradeBean trade) {
            this.trade = trade;
        }

        public static class JoindetailBean {
            private int id;
            private int startNum;
            private int status;
            private int takeMeOutId;
            private int takeMeOutJoinId;
            private int tickets;
            private String ticketsPrefix;
            private int userId;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getStartNum() {
                return startNum;
            }

            public void setStartNum(int startNum) {
                this.startNum = startNum;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getTakeMeOutId() {
                return takeMeOutId;
            }

            public void setTakeMeOutId(int takeMeOutId) {
                this.takeMeOutId = takeMeOutId;
            }

            public int getTakeMeOutJoinId() {
                return takeMeOutJoinId;
            }

            public void setTakeMeOutJoinId(int takeMeOutJoinId) {
                this.takeMeOutJoinId = takeMeOutJoinId;
            }

            public int getTickets() {
                return tickets;
            }

            public void setTickets(int tickets) {
                this.tickets = tickets;
            }

            public String getTicketsPrefix() {
                return ticketsPrefix;
            }

            public void setTicketsPrefix(String ticketsPrefix) {
                this.ticketsPrefix = ticketsPrefix;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }

        public static class TradeBean {
            private double activitypurse;
            private int id;
            private int jobId;
            private int jobType;
            private double leftmoney;
            private int paymentStatus;
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

            public double getLeftmoney() {
                return leftmoney;
            }

            public void setLeftmoney(double leftmoney) {
                this.leftmoney = leftmoney;
            }

            public int getPaymentStatus() {
                return paymentStatus;
            }

            public void setPaymentStatus(int paymentStatus) {
                this.paymentStatus = paymentStatus;
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
    }
}
