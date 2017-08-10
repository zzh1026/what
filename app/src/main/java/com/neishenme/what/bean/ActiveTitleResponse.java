package com.neishenme.what.bean;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/11/28.
 */

public class ActiveTitleResponse extends RBResponse {

    /**
     * code : 1
     * data : {"list":{"application":455,"countdown":1481299200000,"twinning":3,"voters":165,"votes":22054}}
     * message : success
     */

    private int code;
    /**
     * list : {"application":455,"countdown":1481299200000,"twinning":3,"voters":165,"votes":22054}
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
        /**
         * application : 455
         * countdown : 1481299200000
         * twinning : 3
         * voters : 165
         * votes : 22054
         */

        private ListBean list;

        public ListBean getList() {
            return list;
        }

        public void setList(ListBean list) {
            this.list = list;
        }

        public static class ListBean {
            private int application;
            private long countdown;
            private int twinning;
            private int voters;
            private int votes;

            public int getApplication() {
                return application;
            }

            public void setApplication(int application) {
                this.application = application;
            }

            public long getCountdown() {
                return countdown;
            }

            public void setCountdown(long countdown) {
                this.countdown = countdown;
            }

            public int getTwinning() {
                return twinning;
            }

            public void setTwinning(int twinning) {
                this.twinning = twinning;
            }

            public int getVoters() {
                return voters;
            }

            public void setVoters(int voters) {
                this.voters = voters;
            }

            public int getVotes() {
                return votes;
            }

            public void setVotes(int votes) {
                this.votes = votes;
            }
        }
    }
}
