package com.neishenme.what.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
public class BuyVipResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"vipCards":[{"id":1,"name":"购买1月","price":0.01,"oldPrice":30,"lifeDay":30,"joinfranchise":10,"invitefranchise":5,"intro":"会员身份尊享,另赠5次发单特权和10次加入特权","state":200,"createTime":1458026558322,"updateTime":1458631575601},{"id":2,"name":"购买3月","price":60,"oldPrice":90,"lifeDay":90,"joinfranchise":30,"invitefranchise":15,"intro":"会员身份尊享,另赠15次发单特权和30次加入特权","state":200,"createTime":1458026558322,"updateTime":1458631612777}]}
     */

    private int code;
    private String message;
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
         * id : 1
         * name : 购买1月
         * price : 0.01
         * oldPrice : 30
         * lifeDay : 30
         * joinfranchise : 10
         * invitefranchise : 5
         * intro : 会员身份尊享,另赠5次发单特权和10次加入特权
         * state : 200
         * createTime : 1458026558322
         * updateTime : 1458631575601
         */

        private List<VipCardsBean> vipCards;

        public List<VipCardsBean> getVipCards() {
            return vipCards;
        }

        public void setVipCards(List<VipCardsBean> vipCards) {
            this.vipCards = vipCards;
        }

        public static class VipCardsBean {
            private int id;
            private String name;
            private double price;
            private int oldPrice;
            private int lifeDay;
            private int joinfranchise;
            private int invitefranchise;
            private String intro;
            private int state;
            private long createTime;
            private long updateTime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getOldPrice() {
                return oldPrice;
            }

            public void setOldPrice(int oldPrice) {
                this.oldPrice = oldPrice;
            }

            public int getLifeDay() {
                return lifeDay;
            }

            public void setLifeDay(int lifeDay) {
                this.lifeDay = lifeDay;
            }

            public int getJoinfranchise() {
                return joinfranchise;
            }

            public void setJoinfranchise(int joinfranchise) {
                this.joinfranchise = joinfranchise;
            }

            public int getInvitefranchise() {
                return invitefranchise;
            }

            public void setInvitefranchise(int invitefranchise) {
                this.invitefranchise = invitefranchise;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }
        }
    }
}
