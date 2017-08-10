package com.neishenme.what.bean;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/3/28.
 */

public class NsmServerInfoResponse extends RBResponse {

    /**
     * code : 1
     * data : {"nsm":{"logo":"http://192.168.3.99:8888/customer/logo1.jpg","hxUserName":"nsmServer","name":"内什么小助手"}}
     */

    private int code;
    /**
     * nsm : {"logo":"http://192.168.3.99:8888/customer/logo1.jpg","hxUserName":"nsmServer","name":"内什么小助手"}
     */

    private DataBean data;

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

    public static class DataBean {
        /**
         * logo : http://192.168.3.99:8888/customer/logo1.jpg
         * hxUserName : nsmServer
         * name : 内什么小助手
         */

        private NsmBean nsm;

        public NsmBean getNsm() {
            return nsm;
        }

        public void setNsm(NsmBean nsm) {
            this.nsm = nsm;
        }

        public static class NsmBean {
            private String logo;
            private String hxUserName;
            private String name;

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getHxUserName() {
                return hxUserName;
            }

            public void setHxUserName(String hxUserName) {
                this.hxUserName = hxUserName;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
