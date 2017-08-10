package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/3/20.
 */

public class MyOrderListResponse extends RBResponse {


    /**
     * code : 1
     * message : success
     * data : {"hasMore":true,"allJourney":[{"user_logoState":1,"invite_newstatus":900,"invite_title":"我这么可爱，不能请我吃饭吗？","joiner_userId":"","stores_name":"N种美味吃法全天双人餐","type":0,"user_logo":"http://192.168.3.99:8888/users/201611/14/2292/logo/rqwzkazs4ryb86l7wpvlzu03/source.jpg","services_id":29,"services_price":0.01,"joiner_newstatus":"750","invite_id":118222,"user_id":2292,"invite_payType":1,"invite_position":"翠微路","stores_id":1,"invite_updateTime":1489662130804,"joinUser_thumbnailslogo":"","invite_price":0.01,"invite_time":1483538400000,"user_thumbnailslogo":"http://192.168.3.99:8888/users/201611/14/2292/logo/rqwzkazs4ryb86l7wpvlzu03/scale_300.jpg","user_name":"我的呃呃呃呃呃呃","flag":"1","joiner_id":"800818","joiner_logo":"","joinCount":""}]}
     */

    private int code;
    private String message;
    /**
     * hasMore : true
     * allJourney : [{"user_logoState":1,"invite_newstatus":900,"invite_title":"我这么可爱，不能请我吃饭吗？","joiner_userId":"","stores_name":"N种美味吃法全天双人餐","type":0,"user_logo":"http://192.168.3.99:8888/users/201611/14/2292/logo/rqwzkazs4ryb86l7wpvlzu03/source.jpg","services_id":29,"services_price":0.01,"joiner_newstatus":"750","invite_id":118222,"user_id":2292,"invite_payType":1,"invite_position":"翠微路","stores_id":1,"invite_updateTime":1489662130804,"joinUser_thumbnailslogo":"","invite_price":0.01,"invite_time":1483538400000,"user_thumbnailslogo":"http://192.168.3.99:8888/users/201611/14/2292/logo/rqwzkazs4ryb86l7wpvlzu03/scale_300.jpg","user_name":"我的呃呃呃呃呃呃","flag":"1","joiner_id":"800818","joiner_logo":"","joinCount":""}]
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
        private boolean hasMore;
        /**
         * user_logoState : 1
         * invite_newstatus : 900
         * invite_title : 我这么可爱，不能请我吃饭吗？
         * joiner_userId :
         * stores_name : N种美味吃法全天双人餐
         * type : 0
         * user_logo : http://192.168.3.99:8888/users/201611/14/2292/logo/rqwzkazs4ryb86l7wpvlzu03/source.jpg
         * services_id : 29
         * services_price : 0.01
         * joiner_newstatus : 750
         * invite_id : 118222
         * user_id : 2292
         * invite_payType : 1
         * invite_position : 翠微路
         * stores_id : 1
         * invite_updateTime : 1489662130804
         * joinUser_thumbnailslogo :
         * invite_price : 0.01
         * invite_time : 1483538400000
         * user_thumbnailslogo : http://192.168.3.99:8888/users/201611/14/2292/logo/rqwzkazs4ryb86l7wpvlzu03/scale_300.jpg
         * user_name : 我的呃呃呃呃呃呃
         * flag : 1
         * joiner_id : 800818
         * joiner_logo :
         * joinCount :
         */

        private List<AllJourneyBean> allJourney;

        public boolean isHasMore() {
            return hasMore;
        }

        public void setHasMore(boolean hasMore) {
            this.hasMore = hasMore;
        }

        public List<AllJourneyBean> getAllJourney() {
            return allJourney;
        }

        public void setAllJourney(List<AllJourneyBean> allJourney) {
            this.allJourney = allJourney;
        }

        public static class AllJourneyBean {
            private int user_logoState;
            private String invite_newstatus;
            private String invite_title;
            private String joiner_userId;
            private String stores_name;
            private int type;
            private String user_logo;
            private int services_id;
            private double services_price;
            private String joiner_newstatus;
            private int invite_id;
            private int user_id;
            private int invite_payType;
            private String invite_position;
            private int stores_id;
            private long invite_updateTime;
            private String joinUser_thumbnailslogo;
            private double invite_price;
            private long invite_time;
            private String user_thumbnailslogo;
            private String user_name;
            private String flag;
            private String joiner_id;
            private String joiner_logo;
            private String joinCount;

            public int getUser_logoState() {
                return user_logoState;
            }

            public void setUser_logoState(int user_logoState) {
                this.user_logoState = user_logoState;
            }

            public String getInvite_newstatus() {
                return invite_newstatus;
            }

            public void setInvite_newstatus(String invite_newstatus) {
                this.invite_newstatus = invite_newstatus;
            }

            public String getInvite_title() {
                return invite_title;
            }

            public void setInvite_title(String invite_title) {
                this.invite_title = invite_title;
            }

            public String getJoiner_userId() {
                return joiner_userId;
            }

            public void setJoiner_userId(String joiner_userId) {
                this.joiner_userId = joiner_userId;
            }

            public String getStores_name() {
                return stores_name;
            }

            public void setStores_name(String stores_name) {
                this.stores_name = stores_name;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUser_logo() {
                return user_logo;
            }

            public void setUser_logo(String user_logo) {
                this.user_logo = user_logo;
            }

            public int getServices_id() {
                return services_id;
            }

            public void setServices_id(int services_id) {
                this.services_id = services_id;
            }

            public double getServices_price() {
                return services_price;
            }

            public void setServices_price(double services_price) {
                this.services_price = services_price;
            }

            public String getJoiner_newstatus() {
                return joiner_newstatus;
            }

            public void setJoiner_newstatus(String joiner_newstatus) {
                this.joiner_newstatus = joiner_newstatus;
            }

            public int getInvite_id() {
                return invite_id;
            }

            public void setInvite_id(int invite_id) {
                this.invite_id = invite_id;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getInvite_payType() {
                return invite_payType;
            }

            public void setInvite_payType(int invite_payType) {
                this.invite_payType = invite_payType;
            }

            public String getInvite_position() {
                return invite_position;
            }

            public void setInvite_position(String invite_position) {
                this.invite_position = invite_position;
            }

            public int getStores_id() {
                return stores_id;
            }

            public void setStores_id(int stores_id) {
                this.stores_id = stores_id;
            }

            public long getInvite_updateTime() {
                return invite_updateTime;
            }

            public void setInvite_updateTime(long invite_updateTime) {
                this.invite_updateTime = invite_updateTime;
            }

            public String getJoinUser_thumbnailslogo() {
                return joinUser_thumbnailslogo;
            }

            public void setJoinUser_thumbnailslogo(String joinUser_thumbnailslogo) {
                this.joinUser_thumbnailslogo = joinUser_thumbnailslogo;
            }

            public double getInvite_price() {
                return invite_price;
            }

            public void setInvite_price(double invite_price) {
                this.invite_price = invite_price;
            }

            public long getInvite_time() {
                return invite_time;
            }

            public void setInvite_time(long invite_time) {
                this.invite_time = invite_time;
            }

            public String getUser_thumbnailslogo() {
                return user_thumbnailslogo;
            }

            public void setUser_thumbnailslogo(String user_thumbnailslogo) {
                this.user_thumbnailslogo = user_thumbnailslogo;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getFlag() {
                return flag;
            }

            public void setFlag(String flag) {
                this.flag = flag;
            }

            public String getJoiner_id() {
                return joiner_id;
            }

            public void setJoiner_id(String joiner_id) {
                this.joiner_id = joiner_id;
            }

            public String getJoiner_logo() {
                return joiner_logo;
            }

            public void setJoiner_logo(String joiner_logo) {
                this.joiner_logo = joiner_logo;
            }

            public String getJoinCount() {
                return joinCount;
            }

            public void setJoinCount(String joinCount) {
                this.joinCount = joinCount;
            }
        }
    }
}
