package com.neishenme.what.bean;

/**
 * Created by Administrator on 2016/5/26.
 */
public class CheckVip extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"vip":{"inviteTimes":0,"joinLastTime":0,"joinTimes":0,"inviteLastTime":0},"viptime":1505318400000,"vipIdentity":0,"vipFlag":0,"type":1,"isvip":1}
     */

    private int code;
    private String message;
    /**
     * vip : {"inviteTimes":0,"joinLastTime":0,"joinTimes":0,"inviteLastTime":0}
     * viptime : 1505318400000
     * vipIdentity : 0
     * vipFlag : 0
     * type : 1
     * isvip : 1
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
        /**
         * inviteTimes : 0
         * joinLastTime : 0
         * joinTimes : 0
         * inviteLastTime : 0
         */

        private VipBean vip;
        private long viptime;
        private int vipIdentity;
        private int vipFlag;
        private int type;
        private int isvip;

        public VipBean getVip() {
            return vip;
        }

        public void setVip(VipBean vip) {
            this.vip = vip;
        }

        public long getViptime() {
            return viptime;
        }

        public void setViptime(long viptime) {
            this.viptime = viptime;
        }

        public int getVipIdentity() {
            return vipIdentity;
        }

        public void setVipIdentity(int vipIdentity) {
            this.vipIdentity = vipIdentity;
        }

        public int getVipFlag() {
            return vipFlag;
        }

        public void setVipFlag(int vipFlag) {
            this.vipFlag = vipFlag;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getIsvip() {
            return isvip;
        }

        public void setIsvip(int isvip) {
            this.isvip = isvip;
        }

        public static class VipBean {
            private int inviteTimes;
            private int joinLastTime;
            private int joinTimes;
            private int inviteLastTime;

            public int getInviteTimes() {
                return inviteTimes;
            }

            public void setInviteTimes(int inviteTimes) {
                this.inviteTimes = inviteTimes;
            }

            public int getJoinLastTime() {
                return joinLastTime;
            }

            public void setJoinLastTime(int joinLastTime) {
                this.joinLastTime = joinLastTime;
            }

            public int getJoinTimes() {
                return joinTimes;
            }

            public void setJoinTimes(int joinTimes) {
                this.joinTimes = joinTimes;
            }

            public int getInviteLastTime() {
                return inviteLastTime;
            }

            public void setInviteLastTime(int inviteLastTime) {
                this.inviteLastTime = inviteLastTime;
            }
        }
    }
}
