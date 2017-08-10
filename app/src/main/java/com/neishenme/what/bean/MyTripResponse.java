package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/3/2.
 */

public class MyTripResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"inviteUserLogo":"images/c9a4ee5a874e4bc0a128cbeb99f6d34a/source_100x100.jpg","invite_title":"一起勉励对方的小姐姐在哪里","flag":2,"invite_longitude":116.45187936556829,"invite_latitude":39.91728591956807,"invite_id":130142,"joinerUserLogo":"users/201611/14/2292/logo/rqwzkazs4ryb86l7wpvlzu03/scale_300.jpg","joinerUserId":2292,"inviteUserId":2294,"invite_time":1488452940000,"journeyLists":[{"longitude":116.4537929367549,"latitude":39.918038183130825,"setout_status":100,"createtime":1488446220736},{"longitude":116.4537929367549,"latitude":39.918038183130825,"setout_status":200,"createtime":1488446226450}]}
     */

    private int code;
    private String message;
    /**
     * inviteUserLogo : images/c9a4ee5a874e4bc0a128cbeb99f6d34a/source_100x100.jpg
     * invite_title : 一起勉励对方的小姐姐在哪里
     * flag : 2
     * invite_longitude : 116.45187936556829
     * invite_latitude : 39.91728591956807
     * invite_id : 130142
     * joinerUserLogo : users/201611/14/2292/logo/rqwzkazs4ryb86l7wpvlzu03/scale_300.jpg
     * joinerUserId : 2292
     * inviteUserId : 2294
     * invite_time : 1488452940000
     * journeyLists : [{"longitude":116.4537929367549,"latitude":39.918038183130825,"setout_status":100,"createtime":1488446220736},{"longitude":116.4537929367549,"latitude":39.918038183130825,"setout_status":200,"createtime":1488446226450}]
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
        private String inviteUserLogo;
        private String invite_title;
        private int flag;
        private double invite_longitude;
        private double invite_latitude;
        private int invite_id;
        private String joinerId;
        private String joinerUserLogo;
        private int joinerUserId;
        private int operatestatus;
        private int inviteUserId;
        private long invite_time;
        private long chatTime;
        private List<JourneyListsBean> journeyLists;

        public long getChatTime() {
            return chatTime;
        }

        public void setChatTime(long chatTime) {
            this.chatTime = chatTime;
        }


        public int getOperatestatus() {
            return operatestatus;
        }

        public String getJoinerId() {
            return joinerId;
        }

        public void setJoinerId(String joinerId) {
            this.joinerId = joinerId;
        }
        public void setOperatestatus(int operatestatus) {
            this.operatestatus = operatestatus;
        }

        public String getInviteUserLogo() {
            return inviteUserLogo;
        }

        public void setInviteUserLogo(String inviteUserLogo) {
            this.inviteUserLogo = inviteUserLogo;
        }

        public String getInvite_title() {
            return invite_title;
        }

        public void setInvite_title(String invite_title) {
            this.invite_title = invite_title;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public double getInvite_longitude() {
            return invite_longitude;
        }

        public void setInvite_longitude(double invite_longitude) {
            this.invite_longitude = invite_longitude;
        }

        public double getInvite_latitude() {
            return invite_latitude;
        }

        public void setInvite_latitude(double invite_latitude) {
            this.invite_latitude = invite_latitude;
        }

        public int getInvite_id() {
            return invite_id;
        }

        public void setInvite_id(int invite_id) {
            this.invite_id = invite_id;
        }

        public String getJoinerUserLogo() {
            return joinerUserLogo;
        }

        public void setJoinerUserLogo(String joinerUserLogo) {
            this.joinerUserLogo = joinerUserLogo;
        }

        public int getJoinerUserId() {
            return joinerUserId;
        }

        public void setJoinerUserId(int joinerUserId) {
            this.joinerUserId = joinerUserId;
        }

        public int getInviteUserId() {
            return inviteUserId;
        }

        public void setInviteUserId(int inviteUserId) {
            this.inviteUserId = inviteUserId;
        }

        public long getInvite_time() {
            return invite_time;
        }

        public void setInvite_time(long invite_time) {
            this.invite_time = invite_time;
        }

        public List<JourneyListsBean> getJourneyLists() {
            return journeyLists;
        }

        public void setJourneyLists(List<JourneyListsBean> journeyLists) {
            this.journeyLists = journeyLists;
        }

        public static class JourneyListsBean {
            private double longitude;
            private double latitude;
            private int setout_status;
            private long createtime;

            public int getGradelevel() {
                return gradelevel;
            }

            public void setGradelevel(int gradelevel) {
                this.gradelevel = gradelevel;
            }

            private int gradelevel;

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public int getSetout_status() {
                return setout_status;
            }

            public void setSetout_status(int setout_status) {
                this.setout_status = setout_status;
            }

            public long getCreatetime() {
                return createtime;
            }

            public void setCreatetime(long createtime) {
                this.createtime = createtime;
            }
        }
    }
}
