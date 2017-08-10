package com.neishenme.what.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
public class FocusPeopleListResponse extends RBResponse {


    /**
     * code : 1
     * message : success
     * data : {"hasMore":true,"count":7,"foucs":[{"user_gender":1,"user_name":"好啊","user_sign":"谁都困难","user_id":46,"user_birthday":895939200000,"foucs_relation":1,"foucs_id":22,"user_statu":100,"user_logo":"http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source_100x100.jpg"},{"user_gender":2,"user_name":"西巷姑娘","user_sign":"我要的是一辈子，而不是一阵子","user_id":167,"user_birthday":620233200000,"foucs_relation":1,"foucs_id":30,"user_statu":100,"user_logo":"http://192.168.3.99:8888/users/167/6193de75-091a-4ca7-af7b-a8ed01d5bc2a.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/users/167/6193de75-091a-4ca7-af7b-a8ed01d5bc2a_100x100.jpg"},{"user_gender":2,"user_name":"勿忘初心","user_sign":"夕阳下，童话一般的世界。 ","user_id":166,"user_birthday":667670400000,"foucs_relation":1,"foucs_id":29,"user_statu":100,"user_logo":"http://192.168.3.99:8888/users/166/e40075d1-bf21-447a-bf8e-6ca2182b758b.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/users/166/e40075d1-bf21-447a-bf8e-6ca2182b758b_100x100.jpg"},{"user_gender":2,"user_name":"捂嘴的笑","user_sign":"他不爱我才舍得暧昧","user_id":165,"user_birthday":586018800000,"foucs_relation":1,"foucs_id":28,"user_statu":100,"user_logo":"http://192.168.3.99:8888/users/165/1df38c8c-32a3-4260-90ec-ad482d3d4f24.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/users/165/1df38c8c-32a3-4260-90ec-ad482d3d4f24_100x100.jpg"},{"user_gender":2,"user_name":"呢喃软语","user_sign":"姐妹如手足男人如衣服 y","user_id":134,"user_birthday":776275200000,"foucs_relation":1,"foucs_id":27,"user_statu":100,"user_logo":"http://192.168.3.99:8888/users/134/c7f36ce1-b8b2-47fb-8d5f-4fe32ac14318.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/users/134/c7f36ce1-b8b2-47fb-8d5f-4fe32ac14318_100x100.jpg"},{"user_gender":1,"user_name":"貳零壹壹","user_sign":null,"user_id":332,"user_birthday":602352000000,"foucs_relation":1,"foucs_id":26,"user_statu":100,"user_logo":"http://192.168.3.99:8888/images/7631967af7674167a32a032f196c11b4/source.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/images/7631967af7674167a32a032f196c11b4/source_100x100.jpg"},{"user_gender":2,"user_name":"腻了就走","user_sign":"爱恨忐忑、是你陪我走过","user_id":135,"user_birthday":604857600000,"foucs_relation":1,"foucs_id":25,"user_statu":100,"user_logo":"http://192.168.3.99:8888/users/135/f179f27a-eba5-432f-8335-63504d749ae2.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/users/135/f179f27a-eba5-432f-8335-63504d749ae2_100x100.jpg"}]}
     */

    private int code;
    private String message;
    /**
     * hasMore : true
     * count : 7
     * foucs : [{"user_gender":1,"user_name":"好啊","user_sign":"谁都困难","user_id":46,"user_birthday":895939200000,"foucs_relation":1,"foucs_id":22,"user_statu":100,"user_logo":"http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source_100x100.jpg"},{"user_gender":2,"user_name":"西巷姑娘","user_sign":"我要的是一辈子，而不是一阵子","user_id":167,"user_birthday":620233200000,"foucs_relation":1,"foucs_id":30,"user_statu":100,"user_logo":"http://192.168.3.99:8888/users/167/6193de75-091a-4ca7-af7b-a8ed01d5bc2a.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/users/167/6193de75-091a-4ca7-af7b-a8ed01d5bc2a_100x100.jpg"},{"user_gender":2,"user_name":"勿忘初心","user_sign":"夕阳下，童话一般的世界。 ","user_id":166,"user_birthday":667670400000,"foucs_relation":1,"foucs_id":29,"user_statu":100,"user_logo":"http://192.168.3.99:8888/users/166/e40075d1-bf21-447a-bf8e-6ca2182b758b.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/users/166/e40075d1-bf21-447a-bf8e-6ca2182b758b_100x100.jpg"},{"user_gender":2,"user_name":"捂嘴的笑","user_sign":"他不爱我才舍得暧昧","user_id":165,"user_birthday":586018800000,"foucs_relation":1,"foucs_id":28,"user_statu":100,"user_logo":"http://192.168.3.99:8888/users/165/1df38c8c-32a3-4260-90ec-ad482d3d4f24.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/users/165/1df38c8c-32a3-4260-90ec-ad482d3d4f24_100x100.jpg"},{"user_gender":2,"user_name":"呢喃软语","user_sign":"姐妹如手足男人如衣服 y","user_id":134,"user_birthday":776275200000,"foucs_relation":1,"foucs_id":27,"user_statu":100,"user_logo":"http://192.168.3.99:8888/users/134/c7f36ce1-b8b2-47fb-8d5f-4fe32ac14318.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/users/134/c7f36ce1-b8b2-47fb-8d5f-4fe32ac14318_100x100.jpg"},{"user_gender":1,"user_name":"貳零壹壹","user_sign":null,"user_id":332,"user_birthday":602352000000,"foucs_relation":1,"foucs_id":26,"user_statu":100,"user_logo":"http://192.168.3.99:8888/images/7631967af7674167a32a032f196c11b4/source.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/images/7631967af7674167a32a032f196c11b4/source_100x100.jpg"},{"user_gender":2,"user_name":"腻了就走","user_sign":"爱恨忐忑、是你陪我走过","user_id":135,"user_birthday":604857600000,"foucs_relation":1,"foucs_id":25,"user_statu":100,"user_logo":"http://192.168.3.99:8888/users/135/f179f27a-eba5-432f-8335-63504d749ae2.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/users/135/f179f27a-eba5-432f-8335-63504d749ae2_100x100.jpg"}]
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
        private boolean hasMore;
        private int count;
        /**
         * user_gender : 1
         * user_name : 好啊
         * user_sign : 谁都困难
         * user_id : 46
         * user_birthday : 895939200000
         * foucs_relation : 1
         * foucs_id : 22
         * user_statu : 100
         * user_logo : http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source.jpg
         * user_thumbnailslogo : http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source_100x100.jpg
         */

        private List<FoucsEntity> foucs;

        public void setHasMore(boolean hasMore) {
            this.hasMore = hasMore;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setFoucs(List<FoucsEntity> foucs) {
            this.foucs = foucs;
        }

        public boolean isHasMore() {
            return hasMore;
        }

        public int getCount() {
            return count;
        }

        public List<FoucsEntity> getFoucs() {
            return foucs;
        }

        public static class FoucsEntity {
            private int user_gender;
            private String user_name;
            private String user_sign;
            private int user_id;
            private long user_birthday;
            private int foucs_relation;
            private int foucs_id;
            private int user_statu;
            private String user_logo;
            private String user_thumbnailslogo;

            public void setUser_gender(int user_gender) {
                this.user_gender = user_gender;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public void setUser_sign(String user_sign) {
                this.user_sign = user_sign;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public void setUser_birthday(long user_birthday) {
                this.user_birthday = user_birthday;
            }

            public void setFoucs_relation(int foucs_relation) {
                this.foucs_relation = foucs_relation;
            }

            public void setFoucs_id(int foucs_id) {
                this.foucs_id = foucs_id;
            }

            public void setUser_statu(int user_statu) {
                this.user_statu = user_statu;
            }

            public void setUser_logo(String user_logo) {
                this.user_logo = user_logo;
            }

            public void setUser_thumbnailslogo(String user_thumbnailslogo) {
                this.user_thumbnailslogo = user_thumbnailslogo;
            }

            public int getUser_gender() {
                return user_gender;
            }

            public String getUser_name() {
                return user_name;
            }

            public String getUser_sign() {
                return user_sign;
            }

            public int getUser_id() {
                return user_id;
            }

            public long getUser_birthday() {
                return user_birthday;
            }

            public int getFoucs_relation() {
                return foucs_relation;
            }

            public int getFoucs_id() {
                return foucs_id;
            }

            public int getUser_statu() {
                return user_statu;
            }

            public String getUser_logo() {
                return user_logo;
            }

            public String getUser_thumbnailslogo() {
                return user_thumbnailslogo;
            }
        }
    }
}
