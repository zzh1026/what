package com.neishenme.what.bean;

import java.util.List;

/**
 * Created by zhaozh on 2017/1/6 21:30.
 */
public class MyOrderResponse extends RBResponse {

    /**
     * code : 1
     * data : {"hasMore":false,"ordering":[{"invite_id":119165,"invite_newstatus":50,"invite_payType":1,"invite_position":"西城区西直门外大街18号","invite_price":1800,"invite_time":1483797600000,"invite_title":"一个人好无聊，我请你吃个饭吧","joinCount":0,"joinUser_thumbnailslogo":"","joiner_id":"","joiner_logo":"","joiner_newstatus":"","joiner_userId":"","publishType":0,"services_id":48,"services_price":1800,"stores_id":19,"stores_name":"低温慢煮的创意生活","type":1,"user_id":666,"user_logo":"http://192.168.3.99:8888/users/201605/13/666/logo/ye3pu7mhkt3afeli0twoz9qa/source.jpg","user_logoState":-1,"user_name":"hha","user_thumbnailslogo":"http://192.168.3.99:8888/users/201605/13/666/logo/ye3pu7mhkt3afeli0twoz9qa/scale_300.jpg"}]}
     * message : success
     */

    private int code;
    /**
     * hasMore : false
     * ordering : [{"invite_id":119165,"invite_newstatus":50,"invite_payType":1,"invite_position":"西城区西直门外大街18号","invite_price":1800,"invite_time":1483797600000,"invite_title":"一个人好无聊，我请你吃个饭吧","joinCount":0,"joinUser_thumbnailslogo":"","joiner_id":"","joiner_logo":"","joiner_newstatus":"","joiner_userId":"","publishType":0,"services_id":48,"services_price":1800,"stores_id":19,"stores_name":"低温慢煮的创意生活","type":1,"user_id":666,"user_logo":"http://192.168.3.99:8888/users/201605/13/666/logo/ye3pu7mhkt3afeli0twoz9qa/source.jpg","user_logoState":-1,"user_name":"hha","user_thumbnailslogo":"http://192.168.3.99:8888/users/201605/13/666/logo/ye3pu7mhkt3afeli0twoz9qa/scale_300.jpg"}]
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
        private boolean hasMore;
        /**
         * invite_id : 119165
         * invite_newstatus : 50
         * invite_payType : 1
         * invite_position : 西城区西直门外大街18号
         * invite_price : 1800.0
         * invite_time : 1483797600000
         * invite_title : 一个人好无聊，我请你吃个饭吧
         * joinCount : 0
         * joinUser_thumbnailslogo :
         * joiner_id :
         * joiner_logo :
         * joiner_newstatus :
         * joiner_userId :
         * publishType : 0
         * services_id : 48
         * services_price : 1800.0
         * stores_id : 19
         * stores_name : 低温慢煮的创意生活
         * type : 1
         * user_id : 666
         * user_logo : http://192.168.3.99:8888/users/201605/13/666/logo/ye3pu7mhkt3afeli0twoz9qa/source.jpg
         * user_logoState : -1
         * user_name : hha
         * user_thumbnailslogo : http://192.168.3.99:8888/users/201605/13/666/logo/ye3pu7mhkt3afeli0twoz9qa/scale_300.jpg
         */

        private List<OrderingBean> ordering;

        public boolean isHasMore() {
            return hasMore;
        }

        public void setHasMore(boolean hasMore) {
            this.hasMore = hasMore;
        }

        public List<OrderingBean> getOrdering() {
            return ordering;
        }

        public void setOrdering(List<OrderingBean> ordering) {
            this.ordering = ordering;
        }

        public static class OrderingBean {
            private int invite_id;
            private int invite_newstatus;
            private int invite_payType;
            private String invite_position;
            private double invite_price;
            private long invite_time;
            private String invite_title;
            private int joinCount;
            private String joinUser_thumbnailslogo;
            private String joiner_id;
            private String joiner_logo;
            private String joiner_newstatus;
            private String joiner_userId;
            private int publishType;
            private int services_id;
            private double services_price;
            private int stores_id;
            private String stores_name;
            private int type;
            private int user_id;
            private String user_logo;
            private int user_logoState;
            private String user_name;
            private String user_thumbnailslogo;

            public int getInvite_id() {
                return invite_id;
            }

            public void setInvite_id(int invite_id) {
                this.invite_id = invite_id;
            }

            public int getInvite_newstatus() {
                return invite_newstatus;
            }

            public void setInvite_newstatus(int invite_newstatus) {
                this.invite_newstatus = invite_newstatus;
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

            public String getInvite_title() {
                return invite_title;
            }

            public void setInvite_title(String invite_title) {
                this.invite_title = invite_title;
            }

            public int getJoinCount() {
                return joinCount;
            }

            public void setJoinCount(int joinCount) {
                this.joinCount = joinCount;
            }

            public String getJoinUser_thumbnailslogo() {
                return joinUser_thumbnailslogo;
            }

            public void setJoinUser_thumbnailslogo(String joinUser_thumbnailslogo) {
                this.joinUser_thumbnailslogo = joinUser_thumbnailslogo;
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

            public String getJoiner_newstatus() {
                return joiner_newstatus;
            }

            public void setJoiner_newstatus(String joiner_newstatus) {
                this.joiner_newstatus = joiner_newstatus;
            }

            public String getJoiner_userId() {
                return joiner_userId;
            }

            public void setJoiner_userId(String joiner_userId) {
                this.joiner_userId = joiner_userId;
            }

            public int getPublishType() {
                return publishType;
            }

            public void setPublishType(int publishType) {
                this.publishType = publishType;
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

            public int getStores_id() {
                return stores_id;
            }

            public void setStores_id(int stores_id) {
                this.stores_id = stores_id;
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

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getUser_logo() {
                return user_logo;
            }

            public void setUser_logo(String user_logo) {
                this.user_logo = user_logo;
            }

            public int getUser_logoState() {
                return user_logoState;
            }

            public void setUser_logoState(int user_logoState) {
                this.user_logoState = user_logoState;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getUser_thumbnailslogo() {
                return user_thumbnailslogo;
            }

            public void setUser_thumbnailslogo(String user_thumbnailslogo) {
                this.user_thumbnailslogo = user_thumbnailslogo;
            }
        }
    }
}
