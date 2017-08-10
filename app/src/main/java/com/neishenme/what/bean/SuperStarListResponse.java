package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/4/19.
 */

public class SuperStarListResponse extends RBResponse {


    /**
     * code : 1
     * data : {"listStars":[{"superStarId":5,"starName":"李菲儿","starQualifier":"活力女神","starLogo":"","starTicket":1000,"userId":97,"username":"猝不及防","userLogo":"http://192.168.3.99:8888/users/97/9cbd34d0-f871-4e5a-85d7-9312711b482e.jpg"},{"superStarId":7,"starName":"陈静","starQualifier":"美丽女神","starLogo":"","starTicket":1000,"userId":6550,"username":"他心锁℡","userLogo":"http://192.168.3.99:8888/users/201702/07/6550/logo/zmgh3pgwtriweea0aln7yple/source.jpg"},{"superStarId":2,"starName":"柳岩","starQualifier":"性感女神","starLogo":"","starTicket":1000,"userId":94,"username":"宠爱甜心","userLogo":"http://192.168.3.99:8888/users/94/e1fde453-04c7-4a26-be2b-34471430d68f.jpg"},{"superStarId":10,"starName":"张雨琦","starQualifier":"百变女神","starLogo":"","starTicket":1000,"userId":92,"username":"怅忘归","userLogo":"http://192.168.3.99:8888/users/92/a4a89d32-a3d3-4ec6-af19-e70fad553ceb.jpg"},{"superStarId":4,"starName":"徐冬冬","starQualifier":"战力女神","starLogo":"","starTicket":1000,"userId":96,"username":"唇齿朴月","userLogo":"http://192.168.3.99:8888/users/96/738ff9a7-6f78-4147-8d35-9894a8cbe9e4.jpg"},{"superStarId":6,"starName":"霍思燕","starQualifier":"智慧女神","starLogo":"","starTicket":1000,"userId":89,"username":"杯盏余醇","userLogo":"http://192.168.3.99:8888/users/89/8cabe888-48f6-425b-a293-535a375895da.jpg"},{"superStarId":1,"starName":"张馨予","starQualifier":"魅力女神","starLogo":"","starTicket":1000,"userId":58,"username":"咔咔、","userLogo":"http://192.168.3.99:8888/users/58/d29d025f-bb21-4c7e-a3b6-5606c804aa9e.jpg"},{"superStarId":9,"starName":"李颖芝","starQualifier":"火辣女神","starLogo":"","starTicket":1000,"userId":2295,"username":"熊二","userLogo":"http://192.168.3.99:8888/images/06f21a5a896e4e97b7b5fe7844f46126/source.jpg"},{"superStarId":3,"starName":"张天爱","starQualifier":"太子妃","starLogo":"","starTicket":1000,"userId":95,"username":"宠坏我吧i","userLogo":"http://192.168.3.99:8888/users/95/79a55c51-5e74-48e9-8a8b-80e32e7c5bf9.jpg"}]}
     */

    private int code;
    private DataBean data;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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
        private String videoFile;

        public String getVideoFile() {
            return videoFile;
        }

        public void setVideoFile(String videoFile) {
            this.videoFile = videoFile;
        }
        /**
         * superStarId : 5
         * starName : 李菲儿
         * starQualifier : 活力女神
         * starLogo :
         * starTicket : 1000
         * userId : 97
         * username : 猝不及防
         * userLogo : http://192.168.3.99:8888/users/97/9cbd34d0-f871-4e5a-85d7-9312711b482e.jpg
         */

        private List<ListStarsBean> listStars;

        public List<ListStarsBean> getListStars() {
            return listStars;
        }

        public void setListStars(List<ListStarsBean> listStars) {
            this.listStars = listStars;
        }

        public static class ListStarsBean {
            private int superStarId;
            private String starName;
            private String starQualifier;
            private String starLogo;
            private int starTicket;
            private int userId;
            private String username;
            private String userLogo;

            public int getSuperStarId() {
                return superStarId;
            }

            public void setSuperStarId(int superStarId) {
                this.superStarId = superStarId;
            }

            public String getStarName() {
                return starName;
            }

            public void setStarName(String starName) {
                this.starName = starName;
            }

            public String getStarQualifier() {
                return starQualifier;
            }

            public void setStarQualifier(String starQualifier) {
                this.starQualifier = starQualifier;
            }

            public String getStarLogo() {
                return starLogo;
            }

            public void setStarLogo(String starLogo) {
                this.starLogo = starLogo;
            }

            public int getStarTicket() {
                return starTicket;
            }

            public void setStarTicket(int starTicket) {
                this.starTicket = starTicket;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getUserLogo() {
                return userLogo;
            }

            public void setUserLogo(String userLogo) {
                this.userLogo = userLogo;
            }
        }
    }
}
