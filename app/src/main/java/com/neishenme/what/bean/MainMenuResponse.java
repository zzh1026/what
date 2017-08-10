package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/11/29.
 */

public class MainMenuResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"list":[{"title":"我的活动","ctitle":"My activities","ords":1,"type":"takemeout"}],"size":1}
     */

    private int code;
    private String message;
    /**
     * list : [{"title":"我的活动","ctitle":"My activities","ords":1,"type":"takemeout"}]
     * size : 1
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
        private int size;
        /**
         * title : 我的活动
         * ctitle : My activities
         * ords : 1
         * type : takemeout
         */

        private List<ListBean> list;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String title;
            private String ctitle;
            private int ords;
            private String type;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getCtitle() {
                return ctitle;
            }

            public void setCtitle(String ctitle) {
                this.ctitle = ctitle;
            }

            public int getOrds() {
                return ords;
            }

            public void setOrds(int ords) {
                this.ords = ords;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
