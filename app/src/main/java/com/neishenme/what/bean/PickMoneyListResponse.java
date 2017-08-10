package com.neishenme.what.bean;

import java.util.List;

/**
 * 作者：zhaozh create on 2016/3/27 13:29
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 * <p>
 * price   当前支付的总金额
 * refundAmount 当前交易号已经退款金额
 * amount 当前交易号 还可款金额
 */
public class PickMoneyListResponse extends RBResponse {


    /**
     * code : 1
     * message : success
     * data : {"list":[{"id":993,"thirdPartyTradeNum":"2017010421001004700220369085","price":0.01,"refundAmount":0,"amount":0.01,"createTime":1483531680463},{"id":2637,"thirdPartyTradeNum":"2017010721001004260243192028","price":0.01,"refundAmount":0,"amount":0.01,"createTime":1483786257442},{"id":5310,"thirdPartyTradeNum":"2017012121001004380266612849","price":0.01,"refundAmount":0,"amount":0.01,"createTime":1484986068453}]}
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
         * id : 993
         * thirdPartyTradeNum : 2017010421001004700220369085
         * price : 0.01
         * refundAmount : 0.0
         * amount : 0.01
         * createTime : 1483531680463
         */

        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private int id;
            private String thirdPartyTradeNum;
            private double price;
            private double refundAmount;
            private double amount;
            private long createTime;
            private boolean isCheckd = false;

            public boolean isCheckd() {
                return isCheckd;
            }

            public void setCheckd(boolean checkd) {
                isCheckd = checkd;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getThirdPartyTradeNum() {
                return thirdPartyTradeNum;
            }

            public void setThirdPartyTradeNum(String thirdPartyTradeNum) {
                this.thirdPartyTradeNum = thirdPartyTradeNum;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public double getRefundAmount() {
                return refundAmount;
            }

            public void setRefundAmount(double refundAmount) {
                this.refundAmount = refundAmount;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }
        }
    }
}
