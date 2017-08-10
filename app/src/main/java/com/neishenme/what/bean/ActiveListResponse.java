package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/11/23.
 */

public class ActiveListResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"hasMore":false,"count":1,"takemeout":[{"id":1,"logo":"http://192.168.3.99:8888/images/62054f5a7b3344589bc628391eeb794c/source.jpg","users":0,"name":"人人网","thumbnailslogo":"http://192.168.3.99:8888/images/62054f5a7b3344589bc628391eeb794c/source_100x100.jpg","gender":1,"tickets":0}]}
     */

    private int code;
    private String message;
    /**
     * hasMore : false
     * count : 1
     * takemeout : [{"id":1,"logo":"http://192.168.3.99:8888/images/62054f5a7b3344589bc628391eeb794c/source.jpg","users":0,"name":"人人网","thumbnailslogo":"http://192.168.3.99:8888/images/62054f5a7b3344589bc628391eeb794c/source_100x100.jpg","gender":1,"tickets":0}]
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
        private boolean hasMore;
        private int count;
        /**
         * id : 1
         * logo : http://192.168.3.99:8888/images/62054f5a7b3344589bc628391eeb794c/source.jpg
         * users : 0
         * name : 人人网
         * thumbnailslogo : http://192.168.3.99:8888/images/62054f5a7b3344589bc628391eeb794c/source_100x100.jpg
         * gender : 1
         * tickets : 0
         */

        private List<TakemeoutBean> takemeout;

        public boolean isHasMore() {
            return hasMore;
        }

        public void setHasMore(boolean hasMore) {
            this.hasMore = hasMore;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<TakemeoutBean> getTakemeout() {
            return takemeout;
        }

        public void setTakemeout(List<TakemeoutBean> takemeout) {
            this.takemeout = takemeout;
        }

        public static class TakemeoutBean {
            private String id;
            private String logo;
            private int users;
            private String name;
            private String thumbnailslogo;
            private int gender;
            private int tickets;
            private int userId;

            public String getId() {
                return id;
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

            public int getUsers() {
                return users;
            }

            public void setUsers(int users) {
                this.users = users;
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

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public int getTickets() {
                return tickets;
            }

            public int getUserId() {
                return userId;
            }

            public void setTickets(int tickets) {
                this.tickets = tickets;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }
    }
}
