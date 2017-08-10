package com.neishenme.what.bean;

/**
 * Created by Administrator on 2016/5/30.
 */
public class SendDataPublicResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"trade":{"id":295303,"tradeNum":"nsm20160715073020948309","outTradeNo":null,"userId":666,"jobType":1,"jobId":55923,"price":0,"userpurse":0,"activitypurse":0,"leftmoney":0,"paymentStatus":100,"thirdPartyTradeNum":null,"remark":"","deviceInfo":null,"createTime":1468582220221,"updateTime":1468582220221,"refundAmount":0,"refundCount":0,"alipayPrice":0,"tradeState":null,"staData":null,"endData":null,"userName":null,"phone":null,"userModel":null},"invite":{"id":55923,"serviceId":38,"userId":666,"title":"呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵","target":0,"payType":2,"time":1468587600000,"audioDuration":0,"audioFile":"http://192.168.3.99:8888/audios/be71a7ed68d34d87bcff2e280c5c8d69.mp3","signing":0,"reception":0,"confirming":0,"status":100,"newstatus":100,"storeLongitude":0,"storeLatitude":0,"sort":0,"sorttime":0,"createTime":1468582220218,"updateTime":1468582220218,"remarks":null,"istlement":1,"clearTime":null,"settlement":null,"service":null,"user":null,"userName":null,"acceptType":null,"trade":null,"dispose":null,"isfranchise":0,"ispay":0,"setout":0,"preview":0}}
     */

    private int code;
    private String message;
    /**
     * trade : {"id":295303,"tradeNum":"nsm20160715073020948309","outTradeNo":null,"userId":666,"jobType":1,"jobId":55923,"price":0,"userpurse":0,"activitypurse":0,"leftmoney":0,"paymentStatus":100,"thirdPartyTradeNum":null,"remark":"","deviceInfo":null,"createTime":1468582220221,"updateTime":1468582220221,"refundAmount":0,"refundCount":0,"alipayPrice":0,"tradeState":null,"staData":null,"endData":null,"userName":null,"phone":null,"userModel":null}
     * invite : {"id":55923,"serviceId":38,"userId":666,"title":"呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵","target":0,"payType":2,"time":1468587600000,"audioDuration":0,"audioFile":"http://192.168.3.99:8888/audios/be71a7ed68d34d87bcff2e280c5c8d69.mp3","signing":0,"reception":0,"confirming":0,"status":100,"newstatus":100,"storeLongitude":0,"storeLatitude":0,"sort":0,"sorttime":0,"createTime":1468582220218,"updateTime":1468582220218,"remarks":null,"istlement":1,"clearTime":null,"settlement":null,"service":null,"user":null,"userName":null,"acceptType":null,"trade":null,"dispose":null,"isfranchise":0,"ispay":0,"setout":0,"preview":0}
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
        private String userLogo;

        public int getPublishType() {
            return publishType;
        }

        public void setPublishType(int publishType) {
            this.publishType = publishType;
        }

        private int publishType;


        public String getUserLogo() {
            return userLogo;
        }

        public void setUserLogo(String userLogo) {
            this.userLogo = userLogo;
        }

        /**
         * id : 295303
         * tradeNum : nsm20160715073020948309
         * outTradeNo : null
         * userId : 666
         * jobType : 1
         * jobId : 55923
         * price : 0.0
         * userpurse : 0.0
         * activitypurse : 0.0
         * leftmoney : 0.0
         * paymentStatus : 100
         * thirdPartyTradeNum : null
         * remark :
         * deviceInfo : null
         * createTime : 1468582220221
         * updateTime : 1468582220221
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
         * id : 55923
         * serviceId : 38
         * userId : 666
         * title : 呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵
         * target : 0
         * payType : 2
         * time : 1468587600000
         * audioDuration : 0
         * audioFile : http://192.168.3.99:8888/audios/be71a7ed68d34d87bcff2e280c5c8d69.mp3
         * signing : 0
         * reception : 0
         * confirming : 0
         * status : 100
         * newstatus : 100
         * storeLongitude : 0.0
         * storeLatitude : 0.0
         * sort : 0
         * sorttime : 0
         * createTime : 1468582220218
         * updateTime : 1468582220218
         * remarks : null
         * istlement : 1
         * clearTime : null
         * settlement : null
         * service : null
         * user : null
         * userName : null
         * acceptType : null
         * trade : null
         * dispose : null
         * isfranchise : 0
         * ispay : 0
         * setout : 0
         * preview : 0
         */

        private InviteEntity invite;

        public void setTrade(TradeEntity trade) {
            this.trade = trade;
        }

        public void setInvite(InviteEntity invite) {
            this.invite = invite;
        }

        public TradeEntity getTrade() {
            return trade;
        }

        public InviteEntity getInvite() {
            return invite;
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
            private double payprice;
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

            public double getPayprice() {
                return payprice;
            }

            public void setPayprice(double payprice) {
                this.payprice = payprice;
            }

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

        public static class InviteEntity {
            private int id;
            private int serviceId;
            private int userId;
            private String title;
            private int target;
            private int payType;
            private long time;
            private int audioDuration;
            private String audioFile;
            private int signing;
            private int reception;
            private int confirming;
            private int status;
            private int newstatus;
            private double storeLongitude;
            private double storeLatitude;
            private int sort;
            private int sorttime;
            private long createTime;
            private long updateTime;
            private Object remarks;
            private int istlement;
            private Object clearTime;
            private Object settlement;
            private Object service;
            private Object user;
            private Object userName;
            private Object acceptType;
            private Object trade;
            private Object dispose;
            private int isfranchise;
            private int ispay;
            private int setout;
            private int preview;

            public void setId(int id) {
                this.id = id;
            }

            public void setServiceId(int serviceId) {
                this.serviceId = serviceId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setTarget(int target) {
                this.target = target;
            }

            public void setPayType(int payType) {
                this.payType = payType;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public void setAudioDuration(int audioDuration) {
                this.audioDuration = audioDuration;
            }

            public void setAudioFile(String audioFile) {
                this.audioFile = audioFile;
            }

            public void setSigning(int signing) {
                this.signing = signing;
            }

            public void setReception(int reception) {
                this.reception = reception;
            }

            public void setConfirming(int confirming) {
                this.confirming = confirming;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public void setNewstatus(int newstatus) {
                this.newstatus = newstatus;
            }

            public void setStoreLongitude(double storeLongitude) {
                this.storeLongitude = storeLongitude;
            }

            public void setStoreLatitude(double storeLatitude) {
                this.storeLatitude = storeLatitude;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public void setSorttime(int sorttime) {
                this.sorttime = sorttime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public void setRemarks(Object remarks) {
                this.remarks = remarks;
            }

            public void setIstlement(int istlement) {
                this.istlement = istlement;
            }

            public void setClearTime(Object clearTime) {
                this.clearTime = clearTime;
            }

            public void setSettlement(Object settlement) {
                this.settlement = settlement;
            }

            public void setService(Object service) {
                this.service = service;
            }

            public void setUser(Object user) {
                this.user = user;
            }

            public void setUserName(Object userName) {
                this.userName = userName;
            }

            public void setAcceptType(Object acceptType) {
                this.acceptType = acceptType;
            }

            public void setTrade(Object trade) {
                this.trade = trade;
            }

            public void setDispose(Object dispose) {
                this.dispose = dispose;
            }

            public void setIsfranchise(int isfranchise) {
                this.isfranchise = isfranchise;
            }

            public void setIspay(int ispay) {
                this.ispay = ispay;
            }

            public void setSetout(int setout) {
                this.setout = setout;
            }

            public void setPreview(int preview) {
                this.preview = preview;
            }

            public int getId() {
                return id;
            }

            public int getServiceId() {
                return serviceId;
            }

            public int getUserId() {
                return userId;
            }

            public String getTitle() {
                return title;
            }

            public int getTarget() {
                return target;
            }

            public int getPayType() {
                return payType;
            }

            public long getTime() {
                return time;
            }

            public int getAudioDuration() {
                return audioDuration;
            }

            public String getAudioFile() {
                return audioFile;
            }

            public int getSigning() {
                return signing;
            }

            public int getReception() {
                return reception;
            }

            public int getConfirming() {
                return confirming;
            }

            public int getStatus() {
                return status;
            }

            public int getNewstatus() {
                return newstatus;
            }

            public double getStoreLongitude() {
                return storeLongitude;
            }

            public double getStoreLatitude() {
                return storeLatitude;
            }

            public int getSort() {
                return sort;
            }

            public int getSorttime() {
                return sorttime;
            }

            public long getCreateTime() {
                return createTime;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public Object getRemarks() {
                return remarks;
            }

            public int getIstlement() {
                return istlement;
            }

            public Object getClearTime() {
                return clearTime;
            }

            public Object getSettlement() {
                return settlement;
            }

            public Object getService() {
                return service;
            }

            public Object getUser() {
                return user;
            }

            public Object getUserName() {
                return userName;
            }

            public Object getAcceptType() {
                return acceptType;
            }

            public Object getTrade() {
                return trade;
            }

            public Object getDispose() {
                return dispose;
            }

            public int getIsfranchise() {
                return isfranchise;
            }

            public int getIspay() {
                return ispay;
            }

            public int getSetout() {
                return setout;
            }

            public int getPreview() {
                return preview;
            }
        }
    }
}
