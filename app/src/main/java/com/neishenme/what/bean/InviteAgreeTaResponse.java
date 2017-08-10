package com.neishenme.what.bean;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/1/5.
 */

public class InviteAgreeTaResponse extends RBResponse {


    /**
     * code : 1
     * data : {"invite":{"audioDuration":3,"audioFile":"http://192.168.3.99:8888/invites/201701/05/118748/audio/j6fe9xcjcu9zgc8o2l70wki0.aac","createTime":1483601020706,"id":118748,"newstatus":50,"payType":1,"publishType":0,"reception":0,"serviceId":54,"setout":0,"signing":0,"sort":0,"status":100,"storeLatitude":116.425227,"storeLongitude":39.922265,"target":0,"time":1483628160000,"title":"就请你了","userId":666},"publishType":0,"trade":{"activitypurse":0,"id":920925,"jobId":118748,"jobType":1,"paymentStatus":50,"payprice":1300,"price":1400,"remark":"pursepay","tradeNum":"nsm20170105032340240781","userId":666,"userpurse":0},"userLogo":"http://192.168.3.99:8888/users/201605/13/666/logo/ye3pu7mhkt3afeli0twoz9qa/source.jpg"}
     * message : success
     */

    private int code;
    /**
     * invite : {"audioDuration":3,"audioFile":"http://192.168.3.99:8888/invites/201701/05/118748/audio/j6fe9xcjcu9zgc8o2l70wki0.aac","createTime":1483601020706,"id":118748,"newstatus":50,"payType":1,"publishType":0,"reception":0,"serviceId":54,"setout":0,"signing":0,"sort":0,"status":100,"storeLatitude":116.425227,"storeLongitude":39.922265,"target":0,"time":1483628160000,"title":"就请你了","userId":666}
     * publishType : 0
     * trade : {"activitypurse":0,"id":920925,"jobId":118748,"jobType":1,"paymentStatus":50,"payprice":1300,"price":1400,"remark":"pursepay","tradeNum":"nsm20170105032340240781","userId":666,"userpurse":0}
     * userLogo : http://192.168.3.99:8888/users/201605/13/666/logo/ye3pu7mhkt3afeli0twoz9qa/source.jpg
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
         * audioDuration : 3
         * audioFile : http://192.168.3.99:8888/invites/201701/05/118748/audio/j6fe9xcjcu9zgc8o2l70wki0.aac
         * createTime : 1483601020706
         * id : 118748
         * newstatus : 50
         * payType : 1
         * publishType : 0
         * reception : 0
         * serviceId : 54
         * setout : 0
         * signing : 0
         * sort : 0
         * status : 100
         * storeLatitude : 116.425227
         * storeLongitude : 39.922265
         * target : 0
         * time : 1483628160000
         * title : 就请你了
         * userId : 666
         */

        private InviteBean invite;
        private int publishType;
        /**
         * activitypurse : 0.0
         * id : 920925
         * jobId : 118748
         * jobType : 1
         * paymentStatus : 50
         * payprice : 1300.0
         * price : 1400.0
         * remark : pursepay
         * tradeNum : nsm20170105032340240781
         * userId : 666
         * userpurse : 0.0
         */

        private TradeBean trade;
        private String userLogo;

        public InviteBean getInvite() {
            return invite;
        }

        public void setInvite(InviteBean invite) {
            this.invite = invite;
        }

        public int getPublishType() {
            return publishType;
        }

        public void setPublishType(int publishType) {
            this.publishType = publishType;
        }

        public TradeBean getTrade() {
            return trade;
        }

        public void setTrade(TradeBean trade) {
            this.trade = trade;
        }

        public String getUserLogo() {
            return userLogo;
        }

        public void setUserLogo(String userLogo) {
            this.userLogo = userLogo;
        }

        public static class InviteBean {
            private int audioDuration;
            private String audioFile;
            private long createTime;
            private long id;
            private int newstatus;
            private int payType;
            private int publishType;
            private String reception;
            private int serviceId;
            private int setout;
            private int signing;
            private int sort;
            private int status;
            private double storeLatitude;
            private double storeLongitude;
            private int target;
            private long time;
            private String title;
            private int userId;

            public int getAudioDuration() {
                return audioDuration;
            }

            public void setAudioDuration(int audioDuration) {
                this.audioDuration = audioDuration;
            }

            public String getAudioFile() {
                return audioFile;
            }

            public void setAudioFile(String audioFile) {
                this.audioFile = audioFile;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public int getNewstatus() {
                return newstatus;
            }

            public void setNewstatus(int newstatus) {
                this.newstatus = newstatus;
            }

            public int getPayType() {
                return payType;
            }

            public void setPayType(int payType) {
                this.payType = payType;
            }

            public int getPublishType() {
                return publishType;
            }

            public void setPublishType(int publishType) {
                this.publishType = publishType;
            }

            public String getReception() {
                return reception;
            }

            public void setReception(String reception) {
                this.reception = reception;
            }

            public int getServiceId() {
                return serviceId;
            }

            public void setServiceId(int serviceId) {
                this.serviceId = serviceId;
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

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public double getStoreLatitude() {
                return storeLatitude;
            }

            public void setStoreLatitude(double storeLatitude) {
                this.storeLatitude = storeLatitude;
            }

            public double getStoreLongitude() {
                return storeLongitude;
            }

            public void setStoreLongitude(double storeLongitude) {
                this.storeLongitude = storeLongitude;
            }

            public int getTarget() {
                return target;
            }

            public void setTarget(int target) {
                this.target = target;
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
    }
}
