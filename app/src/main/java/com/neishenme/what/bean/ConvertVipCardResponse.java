package com.neishenme.what.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ConvertVipCardResponse extends RBResponse {

    /**
     * code : 1
     * data : {"deduction":0.03,"vipcards":[{"id":1,"intro":"排名靠前，距离展示，身份铭牌，加入特权，免服务费，大幅度降低预付款和保证金","name":"会员(30天)","oldPrice":30,"price":30,"type":1},{"id":2,"intro":"排名靠前，距离展示，身份铭牌，加入特权，免服务费，大幅度降低预付款和保证金","name":"会员(90天) ","oldPrice":90,"price":79,"type":1},{"id":3,"intro":"排名靠前，距离展示，身份铭牌，加入特权，免服务费，大幅度降低预付款和保证金","name":"会员(180天)","oldPrice":180,"price":139,"type":1},{"id":4,"intro":"首次购买立返278元活动余额！享受所有基础会员服务，没有任何限制的发起和加入约会","name":"年费会员(365天)","oldPrice":360,"price":278,"type":2},{"id":5,"intro":"终身服务，一生有我！","name":"永久会员","oldPrice":1080,"price":599,"type":3}]}
     * message : success
     */

    private int code;
    /**
     * deduction : 0.03
     * vipcards : [{"id":1,"intro":"排名靠前，距离展示，身份铭牌，加入特权，免服务费，大幅度降低预付款和保证金","name":"会员(30天)","oldPrice":30,"price":30,"type":1},{"id":2,"intro":"排名靠前，距离展示，身份铭牌，加入特权，免服务费，大幅度降低预付款和保证金","name":"会员(90天) ","oldPrice":90,"price":79,"type":1},{"id":3,"intro":"排名靠前，距离展示，身份铭牌，加入特权，免服务费，大幅度降低预付款和保证金","name":"会员(180天)","oldPrice":180,"price":139,"type":1},{"id":4,"intro":"首次购买立返278元活动余额！享受所有基础会员服务，没有任何限制的发起和加入约会","name":"年费会员(365天)","oldPrice":360,"price":278,"type":2},{"id":5,"intro":"终身服务，一生有我！","name":"永久会员","oldPrice":1080,"price":599,"type":3}]
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
        private double deduction;
        /**
         * id : 1
         * intro : 排名靠前，距离展示，身份铭牌，加入特权，免服务费，大幅度降低预付款和保证金
         * name : 会员(30天)
         * oldPrice : 30.0
         * price : 30.0
         * type : 1
         */

        private List<VipCardsBean> vipCards;

        public double getDeduction() {
            return deduction;
        }

        public void setDeduction(double deduction) {
            this.deduction = deduction;
        }

        public List<VipCardsBean> getVipCards() {
            return vipCards;
        }

        public void setVipcards(List<VipCardsBean> vipCards) {
            this.vipCards = vipCards;
        }

        public static class VipCardsBean {
            private int id;
            private String intro;
            private String name;
            private double oldPrice;
            private double price;
            private int type;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public double getOldPrice() {
                return oldPrice;
            }

            public void setOldPrice(double oldPrice) {
                this.oldPrice = oldPrice;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
