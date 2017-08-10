package com.neishenme.what.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/25.
 */
public class NearByPeople extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"hasMore":true,"count":20,"nearlyuser":[{"user_thumbnailslogofile":"http://192.168.3.99:8888/images/0dd6ba24d8914bf59373fb65f8728816/source_100x100.jpg","user_logofile":"http://192.168.3.99:8888/images/0dd6ba24d8914bf59373fb65f8728816/source.jpg","user_id":644,"invite_creatTime":1462263591916}]}
     */

    private int code;
    private String message;
    /**
     * hasMore : true
     * count : 20
     * nearlyuser : [{"user_thumbnailslogofile":"http://192.168.3.99:8888/images/0dd6ba24d8914bf59373fb65f8728816/source_100x100.jpg","user_logofile":"http://192.168.3.99:8888/images/0dd6ba24d8914bf59373fb65f8728816/source.jpg","user_id":644,"invite_creatTime":1462263591916}]
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
         * user_thumbnailslogofile : http://192.168.3.99:8888/images/0dd6ba24d8914bf59373fb65f8728816/source_100x100.jpg
         * user_logofile : http://192.168.3.99:8888/images/0dd6ba24d8914bf59373fb65f8728816/source.jpg
         * user_id : 644
         * invite_creatTime : 1462263591916
         */

        private List<NearlyuserBean> nearlyuser;

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

        public List<NearlyuserBean> getNearlyuser() {
            return nearlyuser;
        }

        public void setNearlyuser(List<NearlyuserBean> nearlyuser) {
            this.nearlyuser = nearlyuser;
        }

        public static class NearlyuserBean {
            private String user_thumbnailslogofile;
            private String user_logofile;
            private int user_id;
            private long invite_creatTime;

            public String getUser_thumbnailslogofile() {
                return user_thumbnailslogofile;
            }

            public void setUser_thumbnailslogofile(String user_thumbnailslogofile) {
                this.user_thumbnailslogofile = user_thumbnailslogofile;
            }

            public String getUser_logofile() {
                return user_logofile;
            }

            public void setUser_logofile(String user_logofile) {
                this.user_logofile = user_logofile;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public long getInvite_creatTime() {
                return invite_creatTime;
            }

            public void setInvite_creatTime(long invite_creatTime) {
                this.invite_creatTime = invite_creatTime;
            }
        }
    }
}
