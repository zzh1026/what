package com.neishenme.what.bean;

import java.util.List;

/**
 * 作者：zhaozh create on 2016/4/15 16:43
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class MyFriendsResponse extends RBResponse {


    /**
     * code : 1
     * message : success
     * data : {"friends":[{"sign":"efwf","id":586,"birthday":723052800000,"logo":"http://192.168.3.99:8888/images/e20b6ad90efb4cf48c020d31c08346db/source.jpg","lastMeetingTime":1458633600000,"hxUserName":"nsm586","name":"滴滴","relation":2,"gender":2,"remarks":null,"district":null,"city":null}]}
     */

    private int code;
    private String message;
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
        /**
         * sign : efwf
         * id : 586
         * birthday : 723052800000
         * logo : http://192.168.3.99:8888/images/e20b6ad90efb4cf48c020d31c08346db/source.jpg
         * lastMeetingTime : 1458633600000
         * hxUserName : nsm586
         * name : 滴滴
         * relation : 2
         * gender : 2
         * remarks : null
         * district : null
         * city : null
         */

        private List<FriendsEntity> friends;

        public void setFriends(List<FriendsEntity> friends) {
            this.friends = friends;
        }

        public List<FriendsEntity> getFriends() {
            return friends;
        }

        public static class FriendsEntity {
            private String sign;
            private int id;
            private long birthday;
            private String logo;
            private long lastMeetingTime;
            private String hxUserName;
            private String name;
            private int relation;
            private int gender;
            private Object remarks;
            private Object district;
            private Object city;

            public void setSign(String sign) {
                this.sign = sign;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setBirthday(long birthday) {
                this.birthday = birthday;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public void setLastMeetingTime(long lastMeetingTime) {
                this.lastMeetingTime = lastMeetingTime;
            }

            public void setHxUserName(String hxUserName) {
                this.hxUserName = hxUserName;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setRelation(int relation) {
                this.relation = relation;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public void setRemarks(Object remarks) {
                this.remarks = remarks;
            }

            public void setDistrict(Object district) {
                this.district = district;
            }

            public void setCity(Object city) {
                this.city = city;
            }

            public String getSign() {
                return sign;
            }

            public int getId() {
                return id;
            }

            public long getBirthday() {
                return birthday;
            }

            public String getLogo() {
                return logo;
            }

            public long getLastMeetingTime() {
                return lastMeetingTime;
            }

            public String getHxUserName() {
                return hxUserName;
            }

            public String getName() {
                return name;
            }

            public int getRelation() {
                return relation;
            }

            public int getGender() {
                return gender;
            }

            public Object getRemarks() {
                return remarks;
            }

            public Object getDistrict() {
                return district;
            }

            public Object getCity() {
                return city;
            }
        }
    }
}
