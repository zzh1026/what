package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/3/1.
 */

public class HomeInviteOldResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"hasMore":false,"invites":[{"invite_audiofile":"http://192.168.3.99:8888/invites/201703/01/130128/audio/uxxryjh38rktann27377trja.aac","user_gender":1,"invite_target":0,"invite_title":"放弃对。。的抵抗吧","user_thumbnailslogofile":"http://192.168.3.99:8888/images/c9a4ee5a874e4bc0a128cbeb99f6d34a/source_100x100.jpg","invite_price":100,"type":1,"invite_time":1488365040000,"user_name":"**","distance":741.3571973062153,"joiner_newstatus":0,"invite_joinercount":5,"invite_indistinct":"北京市朝阳区东大桥路","invite_audioDuration":5,"invite_id":130128,"user_id":2294,"invite_preview":158}],"count":1}
     */

    private int code;
    private String message;
    /**
     * hasMore : false
     * invites : [{"invite_audiofile":"http://192.168.3.99:8888/invites/201703/01/130128/audio/uxxryjh38rktann27377trja.aac","user_gender":1,"invite_target":0,"invite_title":"放弃对。。的抵抗吧","user_thumbnailslogofile":"http://192.168.3.99:8888/images/c9a4ee5a874e4bc0a128cbeb99f6d34a/source_100x100.jpg","invite_price":100,"type":1,"invite_time":1488365040000,"user_name":"**","distance":741.3571973062153,"joiner_newstatus":0,"invite_joinercount":5,"invite_indistinct":"北京市朝阳区东大桥路","invite_audioDuration":5,"invite_id":130128,"user_id":2294,"invite_preview":158}]
     * count : 1
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
        private int count;
        /**
         * invite_audiofile : http://192.168.3.99:8888/invites/201703/01/130128/audio/uxxryjh38rktann27377trja.aac
         * user_gender : 1
         * invite_target : 0
         * invite_title : 放弃对。。的抵抗吧
         * user_thumbnailslogofile : http://192.168.3.99:8888/images/c9a4ee5a874e4bc0a128cbeb99f6d34a/source_100x100.jpg
         * invite_price : 100
         * type : 1
         * invite_time : 1488365040000
         * user_name : **
         * distance : 741.3571973062153
         * joiner_newstatus : 0
         * invite_joinercount : 5
         * invite_indistinct : 北京市朝阳区东大桥路
         * invite_audioDuration : 5
         * invite_id : 130128
         * user_id : 2294
         * invite_preview : 158
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
            private String invite_audiofile;
            private int user_gender;
            private int invite_target;
            private String invite_title;
            private String user_thumbnailslogofile;
            private String invite_price;
            private int type;
            private long invite_time;
            private String user_name;
            private double distance;
            private int joiner_newstatus;
            private int invite_joinercount;
            private String invite_position;
            private String invite_indistinct;
            private int invite_audioDuration;
            private int invite_id;
            private int user_id;
            private int invite_showCashFlag;
            private double userCash;

            public int getInvite_showCashFlag() {
                return invite_showCashFlag;
            }

            public void setInvite_showCashFlag(int invite_showCashFlag) {
                this.invite_showCashFlag = invite_showCashFlag;
            }

            public long getUser_birthday() {
                return user_birthday;
            }

            public void setUser_birthday(long user_birthday) {
                this.user_birthday = user_birthday;
            }

            public double getUserCash() {
                return userCash;
            }

            public void setUserCash(double userCash) {
                this.userCash = userCash;
            }

            private long user_birthday;
            private int invite_preview;

            public String getInvite_position() {
                return invite_position;
            }

            public void setInvite_position(String invite_position) {
                this.invite_position = invite_position;
            }


            public String getInvite_audiofile() {
                return invite_audiofile;
            }

            public void setInvite_audiofile(String invite_audiofile) {
                this.invite_audiofile = invite_audiofile;
            }

            public int getUser_gender() {
                return user_gender;
            }

            public void setUser_gender(int user_gender) {
                this.user_gender = user_gender;
            }

            public int getInvite_target() {
                return invite_target;
            }

            public void setInvite_target(int invite_target) {
                this.invite_target = invite_target;
            }

            public String getInvite_title() {
                return invite_title;
            }

            public void setInvite_title(String invite_title) {
                this.invite_title = invite_title;
            }

            public String getUser_thumbnailslogofile() {
                return user_thumbnailslogofile;
            }

            public void setUser_thumbnailslogofile(String user_thumbnailslogofile) {
                this.user_thumbnailslogofile = user_thumbnailslogofile;
            }

            public String getInvite_price() {
                return invite_price;
            }

            public void setInvite_price(String invite_price) {
                this.invite_price = invite_price;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public long getInvite_time() {
                return invite_time;
            }

            public void setInvite_time(long invite_time) {
                this.invite_time = invite_time;
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

            public int getJoiner_newstatus() {
                return joiner_newstatus;
            }

            public void setJoiner_newstatus(int joiner_newstatus) {
                this.joiner_newstatus = joiner_newstatus;
            }

            public int getInvite_joinercount() {
                return invite_joinercount;
            }

            public void setInvite_joinercount(int invite_joinercount) {
                this.invite_joinercount = invite_joinercount;
            }

            public String getInvite_indistinct() {
                return invite_indistinct;
            }

            public void setInvite_indistinct(String invite_indistinct) {
                this.invite_indistinct = invite_indistinct;
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

            public int getInvite_preview() {
                return invite_preview;
            }

            public void setInvite_preview(int invite_preview) {
                this.invite_preview = invite_preview;
            }
        }
    }
}
