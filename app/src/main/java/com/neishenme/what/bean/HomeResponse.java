package com.neishenme.what.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 * 老moldel , 现在弃用了 : time:2017/3/1
 */
@Deprecated
public class HomeResponse extends RBResponse {

    private int code;
    private String message;

    private DataBean data;

    /**
     * distance : 1.3331068279434213E7
     * invite_audioDuration : 1
     * invite_id : 55465
     * invite_payType : 1
     * invite_preview : 2
     * invite_target : 2
     * invite_time : 1468401304252
     * invite_title : 天这么冷，我帮你暖暖
     * joiner_newstatus : 0
     * payTypeasc : 2
     * services_id : 31
     * services_logofile : http://192.168.3.99:8888/images/a0a467ee735549eaa0ae9def5ed1a56b/source.jpg
     * services_name : 品味生活
     * services_price : 2.0
     * stores_id : 2
     * stores_latitude : 116.4145148595
     * stores_longitude : 39.9097649644
     * stores_name : 北京东方君悦大酒店悦庭
     * user_gender : 1
     * user_id : 488
     * user_logoState : 1
     * user_logofile : http://192.168.3.99:8888/images/0379e97599e543adb8a3baa540bb5a95/source.jpg
     * user_name : 男囚
     * user_thumbnailslogofile : http://192.168.3.99:8888/images/0379e97599e543adb8a3baa540bb5a95/source_100x100.jpg
     * userfoucs_state : 0
     */

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
        private int count;
        /**
         * user_gender : 2
         * invite_audiofile : http://192.168.3.99:8888/audios/5c5b4f7400f84ecab8c5dd3bf304dafc.m4a
         * user_logoState : 1
         * invite_target : 1
         * user_thumbnailslogofile : http://192.168.3.99:8888/users/144/91058d6b-bb4c-4084-a399-8faa0ae89a48_100x100.jpg
         * invite_title : 人家好饿呀，你能请我吃饭吗？
         * user_logofile : http://192.168.3.99:8888/users/144/91058d6b-bb4c-4084-a399-8faa0ae89a48.jpg
         * stores_latitude : 116.461528
         * services_name : Sureno
         * stores_name : 北京瑜舍Sure?o餐厅
         * invite_time : 1461676500678
         * services_id : 35
         * services_price : 2000
         * user_name : 青巷旧街
         * distance : 1.3338470837324765E7
         * services_logofile : http://192.168.3.99:8888/images/26fd1e32964148e7a1854c1db48b94ee/source.jpg
         * user_phone : null
         * invite_audioDuration : 1
         * invite_id : 31965
         * user_id : 144
         * invite_payType : 2
         * stores_longitude : 39.943227
         * stores_id : 5
         * invite_preview : 0
         * <p>
         * "joiner_newstatus":0,
         * "payTypeasc":2,
         * "userfoucs_state":0
         */

        private List<InvitesBean> invites;

        public boolean isHasMore() {
            return hasMore;
        }

        public void setHasMore(boolean hasMore) {
            this.hasMore = hasMore;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<InvitesBean> getInvites() {
            return invites;
        }

        public void setInvites(List<InvitesBean> invites) {
            this.invites = invites;
        }

        public static class InvitesBean {
            private int user_gender;
            private String invite_audiofile;
            private int user_logoState;
            private int invite_target;
            private String user_thumbnailslogofile;
            private String invite_title;
            private String user_logofile;
            private double stores_latitude;
            private String services_name;
            private String stores_name;
            private long invite_time;
            private int services_id;
            private int services_price;
            private String user_name;
            private double distance;
            private String services_logofile;
            private Object user_phone;
            private int invite_audioDuration;
            private int invite_id;
            private int user_id;
            private int invite_payType;
            private double stores_longitude;
            private int stores_id;
            private int invite_preview;
            private int joiner_newstatus;
            private int payTypeasc;
            private int userfoucs_state;
            private String invite_price;
            private double invite_paymentStatus;
            private String invite_payprice;
            private int type;

            public String getInvite_price() {
                return invite_price;
            }

            public void setInvite_price(String invite_price) {
                this.invite_price = invite_price;
            }

            public double getInvite_paymentStatus() {
                return invite_paymentStatus;
            }

            public void setInvite_paymentStatus(double invite_paymentStatus) {
                this.invite_paymentStatus = invite_paymentStatus;
            }

            public String getInvite_payprice() {
                return invite_payprice;
            }

            public void setInvite_payprice(String invite_payprice) {
                this.invite_payprice = invite_payprice;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getUser_gender() {
                return user_gender;
            }

            public void setUser_gender(int user_gender) {
                this.user_gender = user_gender;
            }

            public String getInvite_audiofile() {
                return invite_audiofile;
            }

            public void setInvite_audiofile(String invite_audiofile) {
                this.invite_audiofile = invite_audiofile;
            }

            public int getUser_logoState() {
                return user_logoState;
            }

            public void setUser_logoState(int user_logoState) {
                this.user_logoState = user_logoState;
            }

            public int getInvite_target() {
                return invite_target;
            }

            public void setInvite_target(int invite_target) {
                this.invite_target = invite_target;
            }

            public String getUser_thumbnailslogofile() {
                return user_thumbnailslogofile;
            }

            public void setUser_thumbnailslogofile(String user_thumbnailslogofile) {
                this.user_thumbnailslogofile = user_thumbnailslogofile;
            }

            public String getInvite_title() {
                return invite_title;
            }

            public void setInvite_title(String invite_title) {
                this.invite_title = invite_title;
            }

            public String getUser_logofile() {
                return user_logofile;
            }

            public void setUser_logofile(String user_logofile) {
                this.user_logofile = user_logofile;
            }

            public double getStores_latitude() {
                return stores_latitude;
            }

            public void setStores_latitude(double stores_latitude) {
                this.stores_latitude = stores_latitude;
            }

            public String getServices_name() {
                return services_name;
            }

            public void setServices_name(String services_name) {
                this.services_name = services_name;
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

            public int getServices_id() {
                return services_id;
            }

            public void setServices_id(int services_id) {
                this.services_id = services_id;
            }

            public int getServices_price() {
                return services_price;
            }

            public void setServices_price(int services_price) {
                this.services_price = services_price;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public double getDistance() {
                return distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            public String getServices_logofile() {
                return services_logofile;
            }

            public void setServices_logofile(String services_logofile) {
                this.services_logofile = services_logofile;
            }

            public Object getUser_phone() {
                return user_phone;
            }

            public void setUser_phone(Object user_phone) {
                this.user_phone = user_phone;
            }

            public int getInvite_audioDuration() {
                return invite_audioDuration;
            }

            public void setInvite_audioDuration(int invite_audioDuration) {
                this.invite_audioDuration = invite_audioDuration;
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

            public double getStores_longitude() {
                return stores_longitude;
            }

            public void setStores_longitude(double stores_longitude) {
                this.stores_longitude = stores_longitude;
            }

            public int getStores_id() {
                return stores_id;
            }

            public void setStores_id(int stores_id) {
                this.stores_id = stores_id;
            }

            public int getInvite_preview() {
                return invite_preview;
            }

            public void setInvite_preview(int invite_preview) {
                this.invite_preview = invite_preview;
            }

            public void setJoiner_newstatus(int joiner_newstatus) {
                this.joiner_newstatus = joiner_newstatus;
            }

            public void setPayTypeasc(int payTypeasc) {
                this.payTypeasc = payTypeasc;
            }

            public void setUserfoucs_state(int userfoucs_state) {
                this.userfoucs_state = userfoucs_state;
            }

            public int getJoiner_newstatus() {
                return joiner_newstatus;
            }

            public int getPayTypeasc() {
                return payTypeasc;
            }

            public int getUserfoucs_state() {
                return userfoucs_state;
            }
        }
    }

}
