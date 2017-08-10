package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/12/28.
 */

public class HomePersonResponse extends RBResponse {


    /**
     * code : 1
     * data : {"count":2,"hasMore":true,"nearlyuser":[{"birthday":544032000000,"interests":{"food_name":"浓咖啡朱古力忌廉饼","music_singer":"刘德华","trip_name":"惠灵顿"},"lastlogintime":1482993708993,"orderby":4,"userAreaId":35,"user_gender":1,"user_id":679,"user_logofile":"http://192.168.3.99:8888/images/f65c47d158f14240b722a3b19a27aa33/source.png","user_thumbnailslogofile":"http://192.168.3.99:8888/images/f65c47d158f14240b722a3b19a27aa33/source_100x100.png","username":"痞子伯爵","usersign":"其实女人离开男人。不是女人的错，是我们男人太花心了！","vitality":0},{"birthday":607449600000,"interests":{"food_name":"纽约芝士饼","music_singer":"张国荣","trip_name":"长白山天池"},"lastlogintime":1482993706942,"orderby":4,"userAreaId":35,"user_gender":1,"user_id":817,"user_logofile":"http://192.168.3.99:8888/images/1c12b0a0c82c4b84ada63c0bda0fe143/source.png","user_thumbnailslogofile":"http://192.168.3.99:8888/images/1c12b0a0c82c4b84ada63c0bda0fe143/source_100x100.png","username":"帅到爆","usersign":"节约用水，是不是能和女朋友一起洗澡","vitality":0}]}
     * message : success
     */

    private int code;
    /**
     * count : 2
     * hasMore : true
     * nearlyuser : [{"birthday":544032000000,"interests":{"food_name":"浓咖啡朱古力忌廉饼","music_singer":"刘德华","trip_name":"惠灵顿"},"lastlogintime":1482993708993,"orderby":4,"userAreaId":35,"user_gender":1,"user_id":679,"user_logofile":"http://192.168.3.99:8888/images/f65c47d158f14240b722a3b19a27aa33/source.png","user_thumbnailslogofile":"http://192.168.3.99:8888/images/f65c47d158f14240b722a3b19a27aa33/source_100x100.png","username":"痞子伯爵","usersign":"其实女人离开男人。不是女人的错，是我们男人太花心了！","vitality":0},{"birthday":607449600000,"interests":{"food_name":"纽约芝士饼","music_singer":"张国荣","trip_name":"长白山天池"},"lastlogintime":1482993706942,"orderby":4,"userAreaId":35,"user_gender":1,"user_id":817,"user_logofile":"http://192.168.3.99:8888/images/1c12b0a0c82c4b84ada63c0bda0fe143/source.png","user_thumbnailslogofile":"http://192.168.3.99:8888/images/1c12b0a0c82c4b84ada63c0bda0fe143/source_100x100.png","username":"帅到爆","usersign":"节约用水，是不是能和女朋友一起洗澡","vitality":0}]
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
         * birthday : 544032000000
         * interests : {"food_name":"浓咖啡朱古力忌廉饼","music_singer":"刘德华","trip_name":"惠灵顿"}
         * lastlogintime : 1482993708993
         * orderby : 4
         * userAreaId : 35
         * user_gender : 1
         * user_id : 679
         * user_logofile : http://192.168.3.99:8888/images/f65c47d158f14240b722a3b19a27aa33/source.png
         * user_thumbnailslogofile : http://192.168.3.99:8888/images/f65c47d158f14240b722a3b19a27aa33/source_100x100.png
         * username : 痞子伯爵
         * usersign : 其实女人离开男人。不是女人的错，是我们男人太花心了！
         * vitality : 0
         */

        private List<NearlyuserBean> nearlyuser;

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

        public List<NearlyuserBean> getNearlyuser() {
            return nearlyuser;
        }

        public void setNearlyuser(List<NearlyuserBean> nearlyuser) {
            this.nearlyuser = nearlyuser;
        }

        public static class NearlyuserBean {
            private long birthday;
            /**
             * food_name : 浓咖啡朱古力忌廉饼
             * music_singer : 刘德华
             * trip_name : 惠灵顿
             */

            private InterestsBean interests;
            private long lastlogintime;
            private int orderby;
            private int userAreaId;
            private int user_gender;
            private int user_id;
            private String user_logofile;
            private String user_thumbnailslogofile;
            private String username;
            private String usersign;
            private int vitality;
            private int relation = 0;

            public int getRelation() {
                return relation;
            }

            public void setRelation(int relation) {
                this.relation = relation;
            }

            public long getBirthday() {
                return birthday;
            }

            public void setBirthday(long birthday) {
                this.birthday = birthday;
            }

            public InterestsBean getInterests() {
                return interests;
            }

            public void setInterests(InterestsBean interests) {
                this.interests = interests;
            }

            public long getLastlogintime() {
                return lastlogintime;
            }

            public void setLastlogintime(long lastlogintime) {
                this.lastlogintime = lastlogintime;
            }

            public int getOrderby() {
                return orderby;
            }

            public void setOrderby(int orderby) {
                this.orderby = orderby;
            }

            public int getUserAreaId() {
                return userAreaId;
            }

            public void setUserAreaId(int userAreaId) {
                this.userAreaId = userAreaId;
            }

            public int getUser_gender() {
                return user_gender;
            }

            public void setUser_gender(int user_gender) {
                this.user_gender = user_gender;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getUser_logofile() {
                return user_logofile;
            }

            public void setUser_logofile(String user_logofile) {
                this.user_logofile = user_logofile;
            }

            public String getUser_thumbnailslogofile() {
                return user_thumbnailslogofile;
            }

            public void setUser_thumbnailslogofile(String user_thumbnailslogofile) {
                this.user_thumbnailslogofile = user_thumbnailslogofile;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getUsersign() {
                return usersign;
            }

            public void setUsersign(String usersign) {
                this.usersign = usersign;
            }

            public int getVitality() {
                return vitality;
            }

            public void setVitality(int vitality) {
                this.vitality = vitality;
            }

            public static class InterestsBean {
                private String food_name;
                private String music_singer;
                private String trip_name;

                public String getFood_name() {
                    return food_name;
                }

                public void setFood_name(String food_name) {
                    this.food_name = food_name;
                }

                public String getMusic_singer() {
                    return music_singer;
                }

                public void setMusic_singer(String music_singer) {
                    this.music_singer = music_singer;
                }

                public String getTrip_name() {
                    return trip_name;
                }

                public void setTrip_name(String trip_name) {
                    this.trip_name = trip_name;
                }
            }
        }
    }
}
