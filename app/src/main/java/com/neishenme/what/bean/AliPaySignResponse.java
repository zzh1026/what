package com.neishenme.what.bean;

/**
 * 作者：zhaozh create on 2016/3/25 14:47
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class AliPaySignResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"sign":{"body":"御厨国宴","subject":"御厨国宴","notify_url":"http://neishenmeapp.imwork.net:8788/ht/refund_refundNotify.do","out_trade_no":"nsm20160325023746150447","return_url":null,"sign":"2b908f83a493bb5f13433eb4ec946c89","_input_charset":"utf-8","it_b_pay":"30m","total_fee":"2350","service":"mobile.securitypay.pay","partner":"2088911923334766","seller_id":"nsmzmh@163.com","payment_type":"1"}}
     */

    private int code;
    private String message;
    /**
     * sign : {"body":"御厨国宴","subject":"御厨国宴","notify_url":"http://neishenmeapp.imwork.net:8788/ht/refund_refundNotify.do","out_trade_no":"nsm20160325023746150447","return_url":null,"sign":"2b908f83a493bb5f13433eb4ec946c89","_input_charset":"utf-8","it_b_pay":"30m","total_fee":"2350","service":"mobile.securitypay.pay","partner":"2088911923334766","seller_id":"nsmzmh@163.com","payment_type":"1"}
     */

    private DataEntity data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * body : 御厨国宴
         * subject : 御厨国宴
         * notify_url : http://neishenmeapp.imwork.net:8788/ht/refund_refundNotify.do
         * out_trade_no : nsm20160325023746150447
         * return_url : null
         * sign : 2b908f83a493bb5f13433eb4ec946c89
         * _input_charset : utf-8
         * it_b_pay : 30m
         * total_fee : 2350
         * service : mobile.securitypay.pay
         * partner : 2088911923334766
         * seller_id : nsmzmh@163.com
         * payment_type : 1
         */

        private SignEntity sign;

        public void setSign(SignEntity sign) {
            this.sign = sign;
        }

        public SignEntity getSign() {
            return sign;
        }

        public static class SignEntity {
            private String body;
            private String subject;
            private String notify_url;
            private String out_trade_no;
            private Object return_url;
            private String sign;
            private String _input_charset;
            private String it_b_pay;
            private String total_fee;
            private String service;
            private String partner;
            private String seller_id;
            private String payment_type;
            private String orderInfo;

            public void setBody(String body) {
                this.body = body;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public void setNotify_url(String notify_url) {
                this.notify_url = notify_url;
            }

            public void setOut_trade_no(String out_trade_no) {
                this.out_trade_no = out_trade_no;
            }

            public void setReturn_url(Object return_url) {
                this.return_url = return_url;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public void set_input_charset(String _input_charset) {
                this._input_charset = _input_charset;
            }

            public void setIt_b_pay(String it_b_pay) {
                this.it_b_pay = it_b_pay;
            }

            public void setTotal_fee(String total_fee) {
                this.total_fee = total_fee;
            }

            public void setService(String service) {
                this.service = service;
            }

            public void setPartner(String partner) {
                this.partner = partner;
            }

            public void setSeller_id(String seller_id) {
                this.seller_id = seller_id;
            }

            public void setPayment_type(String payment_type) {
                this.payment_type = payment_type;
            }
            public void setOrderInfo(String orderInfo) {
                this.orderInfo = orderInfo;
            }

            public String getBody() {
                return body;
            }

            public String getSubject() {
                return subject;
            }

            public String getNotify_url() {
                return notify_url;
            }

            public String getOut_trade_no() {
                return out_trade_no;
            }

            public Object getReturn_url() {
                return return_url;
            }

            public String getSign() {
                return sign;
            }

            public String get_input_charset() {
                return _input_charset;
            }

            public String getIt_b_pay() {
                return it_b_pay;
            }

            public String getTotal_fee() {
                return total_fee;
            }

            public String getService() {
                return service;
            }

            public String getPartner() {
                return partner;
            }

            public String getSeller_id() {
                return seller_id;
            }

            public String getPayment_type() {
                return payment_type;
            }
            public String getOrderInfo() {
                return orderInfo;
            }
        }
    }
}
