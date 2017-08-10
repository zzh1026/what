package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/12/26.
 */

public class CityLocationResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"resultdata":[{"name":"北京市","areaId":1}]}
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
         * name : 北京市
         * areaId : 1
         */

        private List<ResultdataBean> resultdata;

        public List<ResultdataBean> getResultdata() {
            return resultdata;
        }

        public void setResultdata(List<ResultdataBean> resultdata) {
            this.resultdata = resultdata;
        }

        public static class ResultdataBean {
            private String name;
            private int areaId;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getAreaId() {
                return areaId;
            }

            public void setAreaId(int areaId) {
                this.areaId = areaId;
            }
        }
    }
}
