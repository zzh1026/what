package com.neishenme.what.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/23.
 */
public class HobbyListResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"interestBaseInfos":[{"id":7,"interestype":"music_singer","content":"许茹芸"},{"id":8,"interestype":"music_singer","content":"Beyond"}]}
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
         * id : 7
         * interestype : music_singer
         * content : 许茹芸
         */

        private List<InterestBaseInfosEntity> interestBaseInfos;

        public void setInterestBaseInfos(List<InterestBaseInfosEntity> interestBaseInfos) {
            this.interestBaseInfos = interestBaseInfos;
        }

        public List<InterestBaseInfosEntity> getInterestBaseInfos() {
            return interestBaseInfos;
        }

        public static class InterestBaseInfosEntity {
            private int id;
            private String interestype;
            private String content;

            public void setId(int id) {
                this.id = id;
            }

            public void setInterestype(String interestype) {
                this.interestype = interestype;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getId() {
                return id;
            }

            public String getInterestype() {
                return interestype;
            }

            public String getContent() {
                return content;
            }
        }
    }
}
