package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是  约会女明星 活动我的投票信息
 * <p>
 * Created by zhaozh on 2017/4/19.
 */

public class StarMyVoteResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"listSuperStarsTicketed":[{"starlogo":"","starname":"","superStarId":12,"tickets":267}],"countTicketed":{"tickets":267,"username":"**","userlogo":"http://192.168.3.99:8888/images/c9a4ee5a874e4bc0a128cbeb99f6d34a/source.jpg"}}
     */

    private int code;
    private String message;
    /**
     * listSuperStarsTicketed : [{"starlogo":"","starname":"","superStarId":12,"tickets":267}]
     * countTicketed : {"tickets":267,"username":"**","userlogo":"http://192.168.3.99:8888/images/c9a4ee5a874e4bc0a128cbeb99f6d34a/source.jpg"}
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
         * tickets : 267
         * username : **
         * userlogo : http://192.168.3.99:8888/images/c9a4ee5a874e4bc0a128cbeb99f6d34a/source.jpg
         */

        private CountTicketedBean countTicketed;
        /**
         * starlogo :
         * starname :
         * superStarId : 12
         * tickets : 267
         */

        private List<ListSuperStarsTicketedBean> listSuperStarsTicketed;

        public CountTicketedBean getCountTicketed() {
            return countTicketed;
        }

        public void setCountTicketed(CountTicketedBean countTicketed) {
            this.countTicketed = countTicketed;
        }

        public List<ListSuperStarsTicketedBean> getListSuperStarsTicketed() {
            return listSuperStarsTicketed;
        }

        public void setListSuperStarsTicketed(List<ListSuperStarsTicketedBean> listSuperStarsTicketed) {
            this.listSuperStarsTicketed = listSuperStarsTicketed;
        }

        public static class CountTicketedBean {
            private int tickets;
            private String username;
            private String userlogo;

            public int getTickets() {
                return tickets;
            }

            public void setTickets(int tickets) {
                this.tickets = tickets;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getUserlogo() {
                return userlogo;
            }

            public void setUserlogo(String userlogo) {
                this.userlogo = userlogo;
            }
        }

        public static class ListSuperStarsTicketedBean {
            private String starlogo;
            private String starname;
            private int superStarId;
            private int tickets;

            public String getStarlogo() {
                return starlogo;
            }

            public void setStarlogo(String starlogo) {
                this.starlogo = starlogo;
            }

            public String getStarname() {
                return starname;
            }

            public void setStarname(String starname) {
                this.starname = starname;
            }

            public int getSuperStarId() {
                return superStarId;
            }

            public void setSuperStarId(int superStarId) {
                this.superStarId = superStarId;
            }

            public int getTickets() {
                return tickets;
            }

            public void setTickets(int tickets) {
                this.tickets = tickets;
            }
        }
    }
}
