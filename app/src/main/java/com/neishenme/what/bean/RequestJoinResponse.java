package com.neishenme.what.bean;

/**
 * 作者：zhaozh create on 2016/5/19 13:30
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 *         加入请求的model,已弃用, 因为改动较大, 新的model见  @see {@link RequestJoinerResponse}
 */
@Deprecated
public class RequestJoinResponse extends RBResponse {


    /**
     * code : 1
     * message : success
     * data : {"trade":{"id":295302,"tradeNum":"nsm20160715065720529021","outTradeNo":null,"userId":666,"jobType":2,"jobId":257627,"price":1200,"userpurse":0,"activitypurse":0,"leftmoney":1200,"paymentStatus":0,"thirdPartyTradeNum":null,"remark":"","deviceInfo":null,"createTime":1468580240498,"updateTime":1468580240498,"refundAmount":0,"refundCount":0,"alipayPrice":0,"tradeState":null,"staData":null,"endData":null,"userName":null,"phone":null,"userModel":null},"joiner":{"newstatus":0,"ispay":0,"createTime":1468580240497,"inviteId":55885,"acceptTime":0,"isfranchise":0,"id":257627,"userId":666,"signing":0,"setout":0,"acceptType":0,"status":0}}
     */

    private int code;
    private String message;
    /**
     * trade : {"id":295302,"tradeNum":"nsm20160715065720529021","outTradeNo":null,"userId":666,"jobType":2,"jobId":257627,"price":1200,"userpurse":0,"activitypurse":0,"leftmoney":1200,"paymentStatus":0,"thirdPartyTradeNum":null,"remark":"","deviceInfo":null,"createTime":1468580240498,"updateTime":1468580240498,"refundAmount":0,"refundCount":0,"alipayPrice":0,"tradeState":null,"staData":null,"endData":null,"userName":null,"phone":null,"userModel":null}
     * joiner : {"newstatus":0,"ispay":0,"createTime":1468580240497,"inviteId":55885,"acceptTime":0,"isfranchise":0,"id":257627,"userId":666,"signing":0,"setout":0,"acceptType":0,"status":0}
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
        /**
         * id : 295302
         * tradeNum : nsm20160715065720529021
         * outTradeNo : null
         * userId : 666
         * jobType : 2
         * jobId : 257627
         * price : 1200.0
         * userpurse : 0.0
         * activitypurse : 0.0
         * leftmoney : 1200.0
         * paymentStatus : 0
         * thirdPartyTradeNum : null
         * remark :
         * deviceInfo : null
         * createTime : 1468580240498
         * updateTime : 1468580240498
         * refundAmount : 0.0
         * refundCount : 0
         * alipayPrice : 0.0
         * tradeState : null
         * staData : null
         * endData : null
         * userName : null
         * phone : null
         * userModel : null
         */

        private TradeEntity trade;
        /**
         * newstatus : 0
         * ispay : 0
         * createTime : 1468580240497
         * inviteId : 55885
         * acceptTime : 0
         * isfranchise : 0
         * id : 257627
         * userId : 666
         * signing : 0
         * setout : 0
         * acceptType : 0
         * status : 0
         */

        private JoinerEntity joiner;

        public void setTrade(TradeEntity trade) {
            this.trade = trade;
        }

        public void setJoiner(JoinerEntity joiner) {
            this.joiner = joiner;
        }

        public TradeEntity getTrade() {
            return trade;
        }

        public JoinerEntity getJoiner() {
            return joiner;
        }

        public static class TradeEntity {
            private int id;
            private String tradeNum;
            private Object outTradeNo;
            private int userId;
            private int jobType;
            private int jobId;
            private double price;
            private double userpurse;
            private double activitypurse;
            private double leftmoney;
            private int paymentStatus;
            private Object thirdPartyTradeNum;
            private String remark;
            private Object deviceInfo;
            private long createTime;
            private long updateTime;
            private double refundAmount;
            private int refundCount;
            private double alipayPrice;
            private Object tradeState;
            private Object staData;
            private Object endData;
            private Object userName;
            private Object phone;
            private Object userModel;

            public void setId(int id) {
                this.id = id;
            }

            public void setTradeNum(String tradeNum) {
                this.tradeNum = tradeNum;
            }

            public void setOutTradeNo(Object outTradeNo) {
                this.outTradeNo = outTradeNo;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public void setJobType(int jobType) {
                this.jobType = jobType;
            }

            public void setJobId(int jobId) {
                this.jobId = jobId;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public void setUserpurse(double userpurse) {
                this.userpurse = userpurse;
            }

            public void setActivitypurse(double activitypurse) {
                this.activitypurse = activitypurse;
            }

            public void setLeftmoney(double leftmoney) {
                this.leftmoney = leftmoney;
            }

            public void setPaymentStatus(int paymentStatus) {
                this.paymentStatus = paymentStatus;
            }

            public void setThirdPartyTradeNum(Object thirdPartyTradeNum) {
                this.thirdPartyTradeNum = thirdPartyTradeNum;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public void setDeviceInfo(Object deviceInfo) {
                this.deviceInfo = deviceInfo;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public void setRefundAmount(double refundAmount) {
                this.refundAmount = refundAmount;
            }

            public void setRefundCount(int refundCount) {
                this.refundCount = refundCount;
            }

            public void setAlipayPrice(double alipayPrice) {
                this.alipayPrice = alipayPrice;
            }

            public void setTradeState(Object tradeState) {
                this.tradeState = tradeState;
            }

            public void setStaData(Object staData) {
                this.staData = staData;
            }

            public void setEndData(Object endData) {
                this.endData = endData;
            }

            public void setUserName(Object userName) {
                this.userName = userName;
            }

            public void setPhone(Object phone) {
                this.phone = phone;
            }

            public void setUserModel(Object userModel) {
                this.userModel = userModel;
            }

            public int getId() {
                return id;
            }

            public String getTradeNum() {
                return tradeNum;
            }

            public Object getOutTradeNo() {
                return outTradeNo;
            }

            public int getUserId() {
                return userId;
            }

            public int getJobType() {
                return jobType;
            }

            public int getJobId() {
                return jobId;
            }

            public double getPrice() {
                return price;
            }

            public double getUserpurse() {
                return userpurse;
            }

            public double getActivitypurse() {
                return activitypurse;
            }

            public double getLeftmoney() {
                return leftmoney;
            }

            public int getPaymentStatus() {
                return paymentStatus;
            }

            public Object getThirdPartyTradeNum() {
                return thirdPartyTradeNum;
            }

            public String getRemark() {
                return remark;
            }

            public Object getDeviceInfo() {
                return deviceInfo;
            }

            public long getCreateTime() {
                return createTime;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public double getRefundAmount() {
                return refundAmount;
            }

            public int getRefundCount() {
                return refundCount;
            }

            public double getAlipayPrice() {
                return alipayPrice;
            }

            public Object getTradeState() {
                return tradeState;
            }

            public Object getStaData() {
                return staData;
            }

            public Object getEndData() {
                return endData;
            }

            public Object getUserName() {
                return userName;
            }

            public Object getPhone() {
                return phone;
            }

            public Object getUserModel() {
                return userModel;
            }
        }

        public static class JoinerEntity {
            private int newstatus;
            private int ispay;
            private long createTime;
            private int inviteId;
            private int acceptTime;
            private int isfranchise;
            private int id;
            private int userId;
            private int signing;
            private int setout;
            private int acceptType;
            private int status;

            public void setNewstatus(int newstatus) {
                this.newstatus = newstatus;
            }

            public void setIspay(int ispay) {
                this.ispay = ispay;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public void setInviteId(int inviteId) {
                this.inviteId = inviteId;
            }

            public void setAcceptTime(int acceptTime) {
                this.acceptTime = acceptTime;
            }

            public void setIsfranchise(int isfranchise) {
                this.isfranchise = isfranchise;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public void setSigning(int signing) {
                this.signing = signing;
            }

            public void setSetout(int setout) {
                this.setout = setout;
            }

            public void setAcceptType(int acceptType) {
                this.acceptType = acceptType;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getNewstatus() {
                return newstatus;
            }

            public int getIspay() {
                return ispay;
            }

            public long getCreateTime() {
                return createTime;
            }

            public int getInviteId() {
                return inviteId;
            }

            public int getAcceptTime() {
                return acceptTime;
            }

            public int getIsfranchise() {
                return isfranchise;
            }

            public int getId() {
                return id;
            }

            public int getUserId() {
                return userId;
            }

            public int getSigning() {
                return signing;
            }

            public int getSetout() {
                return setout;
            }

            public int getAcceptType() {
                return acceptType;
            }

            public int getStatus() {
                return status;
            }
        }
    }
}
