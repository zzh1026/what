package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/11/24.
 */

public class ActiveMyJoinResponse extends RBResponse {

    /**
     * code : 1
     * data : {"count":3,"hasMore":false,"list":[{"name":"人人网","startNum":1633373,"takeMeOutId":1,"tickets":3,"ticketsPrefix":"WHXaF2DfDIaIQ9LrOVC"},{"name":"人人网","startNum":1633372,"takeMeOutId":1,"tickets":1,"ticketsPrefix":"WHXaF2DfDIaIQ9LrOVC"},{"name":"人人网","startNum":1633368,"takeMeOutId":1,"tickets":1,"ticketsPrefix":"WHXaF2DfDIaIQ9LrOVC"}]}
     * message : success
     */

    private int code;
    /**
     * count : 3
     * hasMore : false
     * list : [{"name":"人人网","startNum":1633373,"takeMeOutId":1,"tickets":3,"ticketsPrefix":"WHXaF2DfDIaIQ9LrOVC"},{"name":"人人网","startNum":1633372,"takeMeOutId":1,"tickets":1,"ticketsPrefix":"WHXaF2DfDIaIQ9LrOVC"},{"name":"人人网","startNum":1633368,"takeMeOutId":1,"tickets":1,"ticketsPrefix":"WHXaF2DfDIaIQ9LrOVC"}]
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
        private int count;
        private boolean hasMore;
        /**
         * name : 人人网
         * startNum : 1633373
         * takeMeOutId : 1
         * tickets : 3
         * ticketsPrefix : WHXaF2DfDIaIQ9LrOVC
         */

        private List<ListBean> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isHasMore() {
            return hasMore;
        }

        public void setHasMore(boolean hasMore) {
            this.hasMore = hasMore;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String name;
            private int startNum;
            private int takeMeOutId;
            private int tickets;
            private String ticketsPrefix;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getStartNum() {
                return startNum;
            }

            public void setStartNum(int startNum) {
                this.startNum = startNum;
            }

            public int getTakeMeOutId() {
                return takeMeOutId;
            }

            public void setTakeMeOutId(int takeMeOutId) {
                this.takeMeOutId = takeMeOutId;
            }

            public int getTickets() {
                return tickets;
            }

            public void setTickets(int tickets) {
                this.tickets = tickets;
            }

            public String getTicketsPrefix() {
                return ticketsPrefix;
            }

            public void setTicketsPrefix(String ticketsPrefix) {
                this.ticketsPrefix = ticketsPrefix;
            }
        }
    }
}
