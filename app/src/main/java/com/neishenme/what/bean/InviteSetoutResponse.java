package com.neishenme.what.bean;

/**
 * 作者：zhaozh create on 2016/5/23 13:44
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class InviteSetoutResponse extends RBResponse {

    /**
     * code : 1
     * data : {"inviteInfo":{"distance":15742,"inviteId":15652,"otherDistance":-1,"otherSetout":0,"setout":100}}
     * message : success
     */

    private int code;
    /**
     * inviteInfo : {"distance":15742,"inviteId":15652,"otherDistance":-1,"otherSetout":0,"setout":100}
     */

    private DataEntity data;
    private String message;

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public DataEntity getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static class DataEntity {
        /**
         * distance : 15742
         * inviteId : 15652
         * otherDistance : -1
         * otherSetout : 0
         * setout : 100
         */

        private InviteInfoEntity inviteInfo;

        public void setInviteInfo(InviteInfoEntity inviteInfo) {
            this.inviteInfo = inviteInfo;
        }

        public InviteInfoEntity getInviteInfo() {
            return inviteInfo;
        }

        public static class InviteInfoEntity {
            private double distance;
            private int inviteId;
            private int otherDistance;
            private int otherSetout;
            private int setout;

            public void setDistance(double distance) {
                this.distance = distance;
            }

            public void setInviteId(int inviteId) {
                this.inviteId = inviteId;
            }

            public void setOtherDistance(int otherDistance) {
                this.otherDistance = otherDistance;
            }

            public void setOtherSetout(int otherSetout) {
                this.otherSetout = otherSetout;
            }

            public void setSetout(int setout) {
                this.setout = setout;
            }

            public double getDistance() {
                return distance;
            }

            public int getInviteId() {
                return inviteId;
            }

            public int getOtherDistance() {
                return otherDistance;
            }

            public int getOtherSetout() {
                return otherSetout;
            }

            public int getSetout() {
                return setout;
            }
        }
    }
}
