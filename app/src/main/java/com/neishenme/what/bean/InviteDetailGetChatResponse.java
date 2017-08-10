package com.neishenme.what.bean;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/1/9.
 */

public class InviteDetailGetChatResponse extends RBResponse {


    /**
     * code : 1
     * message : success
     * data : {"hxUserName":"hx666","userlogo":"http://192.168.3.99:8888/images/8f4f29d74565449c8346b5c863be9890/source_100x100.png","userid":666,"username":"昵称"}
     */

    private int code;
    private String message;
    /**
     * hxUserName : hx666
     * userlogo : http://192.168.3.99:8888/images/8f4f29d74565449c8346b5c863be9890/source_100x100.png
     * userid : 666
     * username : 昵称
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
        private String hxUserName;
        private String userlogo;
        private String userid;
        private String username;

        public String getHxUserName() {
            return hxUserName;
        }

        public void setHxUserName(String hxUserName) {
            this.hxUserName = hxUserName;
        }

        public String getUserlogo() {
            return userlogo;
        }

        public void setUserlogo(String userlogo) {
            this.userlogo = userlogo;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
