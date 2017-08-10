package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/11/24.
 */

public class ActiveMyTakeResponse extends RBResponse {

    /**
     * code : 1
     * data : {"count":1,"gender":1,"hasMore":false,"id":1,"list":[{"name":"人人网","tickets":5,"userId":666}],"logo":"images/2a4eeb318f5b497584f48fd167570a4d/source.jpg","name":"人人网","thumbnailslogo":"images/2a4eeb318f5b497584f48fd167570a4d/source_100x100.jpg","tickets":5,"users":1}
     * message : success
     */

    private int code;
    /**
     * count : 1
     * gender : 1
     * hasMore : false
     * id : 1
     * list : [{"name":"人人网","tickets":5,"userId":666}]
     * logo : images/2a4eeb318f5b497584f48fd167570a4d/source.jpg
     * name : 人人网
     * thumbnailslogo : images/2a4eeb318f5b497584f48fd167570a4d/source_100x100.jpg
     * tickets : 5
     * users : 1
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
        private int gender;
        private boolean hasMore;
        private String id;
        private int mytakemeout;
        private String logo;
        private String name;
        private String thumbnailslogo;
        private int tickets;
        private int users;
        /**
         * name : 人人网
         * tickets : 5
         * userId : 666
         */

        private List<ListBean> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public boolean isHasMore() {
            return hasMore;
        }

        public void setHasMore(boolean hasMore) {
            this.hasMore = hasMore;
        }

        public String getId() {
            return id;
        }

        public int getMytakemeout() {
            return mytakemeout;
        }

        public void setMytakemeout(int mytakemeout) {
            this.mytakemeout = mytakemeout;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getThumbnailslogo() {
            return thumbnailslogo;
        }

        public void setThumbnailslogo(String thumbnailslogo) {
            this.thumbnailslogo = thumbnailslogo;
        }

        public int getTickets() {
            return tickets;
        }

        public void setTickets(int tickets) {
            this.tickets = tickets;
        }

        public int getUsers() {
            return users;
        }

        public void setUsers(int users) {
            this.users = users;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String name;
            private int tickets;
            private int userId;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getTickets() {
                return tickets;
            }

            public void setTickets(int tickets) {
                this.tickets = tickets;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }
    }
}
