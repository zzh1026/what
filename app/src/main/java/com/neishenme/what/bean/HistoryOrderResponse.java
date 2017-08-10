package com.neishenme.what.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
public class HistoryOrderResponse extends RBResponse {


    /**
     * code : 1
     * message : success
     * data : {"hasMore":true,"histroyorder":[{"invite_newstatus":100,"user_logoState":-1,"invite_title":"我很会唱歌哦。","invite_price":1,"type":0,"stores_name":"","invite_time":1483711200000,"user_logo":"http://192.168.3.99:8888/images/06f21a5a896e4e97b7b5fe7844f46126/source.jpg","services_id":"","user_thumbnailslogo":"http://192.168.3.99:8888/images/06f21a5a896e4e97b7b5fe7844f46126/source_100x100.jpg","services_price":"","user_name":"熊二","joiner_id":"819443","joiner_newstatus":"650","invite_id":119141,"user_id":2295,"invite_payType":1,"stores_id":"","publishType":1,"invite_position":"花雨容美容美体美甲SPA馆","invite_updateTime":1483705434090},{"invite_newstatus":100,"user_logoState":1,"invite_title":"天下没有免费的大餐哦。嘿嘿。","invite_price":1,"type":0,"stores_name":"","invite_time":1483714560000,"user_logo":"http://192.168.3.99:8888/images/6e44fdea7dd34c79a02dbb7af406fb9a/source.jpg","services_id":"","user_thumbnailslogo":"http://192.168.3.99:8888/images/6e44fdea7dd34c79a02dbb7af406fb9a/source_100x100.jpg","services_price":"","user_name":"A","joiner_id":"819431","joiner_newstatus":"650","invite_id":119139,"user_id":2292,"invite_payType":1,"stores_id":"","publishType":1,"invite_position":"星巴克(金汇路店)","invite_updateTime":1483704263988}]}
     */

    private int code;
    private String message;
    /**
     * hasMore : true
     * histroyorder : [{"invite_newstatus":100,"user_logoState":-1,"invite_title":"我很会唱歌哦。","invite_price":1,"type":0,"stores_name":"","invite_time":1483711200000,"user_logo":"http://192.168.3.99:8888/images/06f21a5a896e4e97b7b5fe7844f46126/source.jpg","services_id":"","user_thumbnailslogo":"http://192.168.3.99:8888/images/06f21a5a896e4e97b7b5fe7844f46126/source_100x100.jpg","services_price":"","user_name":"熊二","joiner_id":"819443","joiner_newstatus":"650","invite_id":119141,"user_id":2295,"invite_payType":1,"stores_id":"","publishType":1,"invite_position":"花雨容美容美体美甲SPA馆","invite_updateTime":1483705434090},{"invite_newstatus":100,"user_logoState":1,"invite_title":"天下没有免费的大餐哦。嘿嘿。","invite_price":1,"type":0,"stores_name":"","invite_time":1483714560000,"user_logo":"http://192.168.3.99:8888/images/6e44fdea7dd34c79a02dbb7af406fb9a/source.jpg","services_id":"","user_thumbnailslogo":"http://192.168.3.99:8888/images/6e44fdea7dd34c79a02dbb7af406fb9a/source_100x100.jpg","services_price":"","user_name":"A","joiner_id":"819431","joiner_newstatus":"650","invite_id":119139,"user_id":2292,"invite_payType":1,"stores_id":"","publishType":1,"invite_position":"星巴克(金汇路店)","invite_updateTime":1483704263988}]
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
         * invite_newstatus : 100
         * user_logoState : -1
         * invite_title : 我很会唱歌哦。
         * invite_price : 1
         * type : 0
         * stores_name :
         * invite_time : 1483711200000
         * user_logo : http://192.168.3.99:8888/images/06f21a5a896e4e97b7b5fe7844f46126/source.jpg
         * services_id :
         * user_thumbnailslogo : http://192.168.3.99:8888/images/06f21a5a896e4e97b7b5fe7844f46126/source_100x100.jpg
         * services_price :
         * user_name : 熊二
         * joiner_id : 819443
         * joiner_newstatus : 650
         * invite_id : 119141
         * user_id : 2295
         * invite_payType : 1
         * stores_id :
         * publishType : 1
         * invite_position : 花雨容美容美体美甲SPA馆
         * invite_updateTime : 1483705434090
         */

        private List<HistroyorderBean> histroyorder;

        public boolean isHasMore() {
            return hasMore;
        }

        public void setHasMore(boolean hasMore) {
            this.hasMore = hasMore;
        }

        public List<HistroyorderBean> getHistroyorder() {
            return histroyorder;
        }

        public void setHistroyorder(List<HistroyorderBean> histroyorder) {
            this.histroyorder = histroyorder;
        }

        public static class HistroyorderBean {
            private String invite_newstatus;
            private int user_logoState;
            private String invite_title;
            private double invite_price;
            private int type;
            private String stores_name;
            private long invite_time;
            private String user_logo;
            private String services_id;
            private String user_thumbnailslogo;
            private String services_price;
            private String user_name;
            private String joiner_id;
            private String joiner_newstatus;
            private int invite_id;
            private int user_id;
            private int invite_payType;
            private String stores_id;
            private int publishType;
            private String invite_position;
            private long invite_updateTime;

            public String getInvite_newstatus() {
                return invite_newstatus;
            }

            public void setInvite_newstatus(String invite_newstatus) {
                this.invite_newstatus = invite_newstatus;
            }

            public int getUser_logoState() {
                return user_logoState;
            }

            public void setUser_logoState(int user_logoState) {
                this.user_logoState = user_logoState;
            }

            public String getInvite_title() {
                return invite_title;
            }

            public void setInvite_title(String invite_title) {
                this.invite_title = invite_title;
            }

            public double getInvite_price() {
                return invite_price;
            }

            public void setInvite_price(double invite_price) {
                this.invite_price = invite_price;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getStores_name() {
                return stores_name;
            }

            public void setStores_name(String stores_name) {
                this.stores_name = stores_name;
            }

            public long getInvite_time() {
                return invite_time;
            }

            public void setInvite_time(long invite_time) {
                this.invite_time = invite_time;
            }

            public String getUser_logo() {
                return user_logo;
            }

            public void setUser_logo(String user_logo) {
                this.user_logo = user_logo;
            }

            public String getServices_id() {
                return services_id;
            }

            public void setServices_id(String services_id) {
                this.services_id = services_id;
            }

            public String getUser_thumbnailslogo() {
                return user_thumbnailslogo;
            }

            public void setUser_thumbnailslogo(String user_thumbnailslogo) {
                this.user_thumbnailslogo = user_thumbnailslogo;
            }

            public String getServices_price() {
                return services_price;
            }

            public void setServices_price(String services_price) {
                this.services_price = services_price;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getJoiner_id() {
                return joiner_id;
            }

            public void setJoiner_id(String joiner_id) {
                this.joiner_id = joiner_id;
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

            public String getStores_id() {
                return stores_id;
            }

            public void setStores_id(String stores_id) {
                this.stores_id = stores_id;
            }

            public int getPublishType() {
                return publishType;
            }

            public void setPublishType(int publishType) {
                this.publishType = publishType;
            }

            public String getInvite_position() {
                return invite_position;
            }

            public void setInvite_position(String invite_position) {
                this.invite_position = invite_position;
            }

            public long getInvite_updateTime() {
                return invite_updateTime;
            }

            public void setInvite_updateTime(long invite_updateTime) {
                this.invite_updateTime = invite_updateTime;
            }
        }
    }
}
