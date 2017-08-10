package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/3/1.
 *
 * 2017/5/22 修改完善该 bean类
 */

public class HomeInviteResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"hasMore":false,"invites":[{"user_gender":1,"invite_newstatus":100,"invite_target":2,"invite_title":"姑娘 来玩吗","user_thumbnailslogofile":"http://192.168.3.99:8888/users/201702/06/2798/logo/lwtukrrizt9s57j51jesoecu/scale_300.jpg","invite_price":2,"type":1,"invite_time":1495448821273,"user_name":"妳回来了","distance":1.3358165742601302E7,"joiner_newstatus":0,"invite_id":139306,"user_id":2798,"user_birthday":918576000000,"invite_position":"北京17.5影城(管庄店)","invite_preview":0},{"user_gender":1,"invite_newstatus":100,"invite_target":2,"invite_title":"想去滑雪 找个妹子","user_thumbnailslogofile":"http://192.168.3.99:8888/users/201705/03/46/logo/ktzilhqtjsuk9peaysfej23r/source.jpg","invite_price":0,"type":1,"invite_time":1495442400000,"user_name":"好啊","distance":1.3336877906002462E7,"joiner_newstatus":0,"invite_id":139298,"user_id":46,"user_birthday":895939200000,"invite_position":"朝阳区呼家楼街道新城·国际","invite_preview":8},{"user_gender":2,"invite_newstatus":100,"invite_target":0,"invite_title":"有没有一起脱贫的哥哥","user_thumbnailslogofile":"http://192.168.3.99:8888/users/201702/07/4570/logo/tbs7xq9bdrrogdi8nrpsaazy/scale_300.jpg","invite_price":1,"type":1,"invite_time":1495447741109,"user_name":"゛所谓嘚莪想伱","distance":1.334366021391889E7,"joiner_newstatus":0,"invite_id":139292,"user_id":4570,"user_birthday":925228800000,"invite_position":"利星行广场C座","invite_preview":12}],"count":3}
     */

    private int code;
    private String message;
    /**
     * hasMore : false
     * invites : [{"user_gender":1,"invite_newstatus":100,"invite_target":2,"invite_title":"姑娘 来玩吗","user_thumbnailslogofile":"http://192.168.3.99:8888/users/201702/06/2798/logo/lwtukrrizt9s57j51jesoecu/scale_300.jpg","invite_price":2,"type":1,"invite_time":1495448821273,"user_name":"妳回来了","distance":1.3358165742601302E7,"joiner_newstatus":0,"invite_id":139306,"user_id":2798,"user_birthday":918576000000,"invite_position":"北京17.5影城(管庄店)","invite_preview":0},{"user_gender":1,"invite_newstatus":100,"invite_target":2,"invite_title":"想去滑雪 找个妹子","user_thumbnailslogofile":"http://192.168.3.99:8888/users/201705/03/46/logo/ktzilhqtjsuk9peaysfej23r/source.jpg","invite_price":0,"type":1,"invite_time":1495442400000,"user_name":"好啊","distance":1.3336877906002462E7,"joiner_newstatus":0,"invite_id":139298,"user_id":46,"user_birthday":895939200000,"invite_position":"朝阳区呼家楼街道新城·国际","invite_preview":8},{"user_gender":2,"invite_newstatus":100,"invite_target":0,"invite_title":"有没有一起脱贫的哥哥","user_thumbnailslogofile":"http://192.168.3.99:8888/users/201702/07/4570/logo/tbs7xq9bdrrogdi8nrpsaazy/scale_300.jpg","invite_price":1,"type":1,"invite_time":1495447741109,"user_name":"゛所谓嘚莪想伱","distance":1.334366021391889E7,"joiner_newstatus":0,"invite_id":139292,"user_id":4570,"user_birthday":925228800000,"invite_position":"利星行广场C座","invite_preview":12}]
     * count : 3
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
         * user_gender : 1
         * invite_newstatus : 100
         * invite_target : 2
         * invite_title : 姑娘 来玩吗
         * user_thumbnailslogofile : http://192.168.3.99:8888/users/201702/06/2798/logo/lwtukrrizt9s57j51jesoecu/scale_300.jpg
         * invite_price : 2.0
         * type : 1
         * invite_time : 1495448821273
         * user_name : 妳回来了
         * distance : 1.3358165742601302E7
         * joiner_newstatus : 0
         * invite_id : 139306
         * user_id : 2798
         * user_birthday : 918576000000
         * invite_position : 北京17.5影城(管庄店)
         * invite_preview : 0
         */

        private List<InvitesBean> invites;

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

        public List<InvitesBean> getInvites() {
            return invites;
        }

        public void setInvites(List<InvitesBean> invites) {
            this.invites = invites;
        }

        public static class InvitesBean {
            private int user_gender;
            private int invite_newstatus;
            private int invite_target;
            private String invite_title;
            private String user_thumbnailslogofile;
            private double invite_price;
            private int type;
            private long invite_time;
            private String user_name;
            private double distance;
            private int joiner_newstatus;
            private int invite_id;
            private int user_id;
            private long user_birthday;
            private String invite_position;
            private int invite_preview;
            private int hot;

            public int getHot() {
                return hot;
            }

            public void setHot(int hot) {
                this.hot = hot;
            }


            public int getUser_gender() {
                return user_gender;
            }

            public void setUser_gender(int user_gender) {
                this.user_gender = user_gender;
            }

            public int getInvite_newstatus() {
                return invite_newstatus;
            }

            public void setInvite_newstatus(int invite_newstatus) {
                this.invite_newstatus = invite_newstatus;
            }

            public int getInvite_target() {
                return invite_target;
            }

            public void setInvite_target(int invite_target) {
                this.invite_target = invite_target;
            }

            public String getInvite_title() {
                return invite_title;
            }

            public void setInvite_title(String invite_title) {
                this.invite_title = invite_title;
            }

            public String getUser_thumbnailslogofile() {
                return user_thumbnailslogofile;
            }

            public void setUser_thumbnailslogofile(String user_thumbnailslogofile) {
                this.user_thumbnailslogofile = user_thumbnailslogofile;
            }

            public double getInvite_price() {
                return invite_price;
            }

            public void setInvite_price(double invite_price) {
                this.invite_price = invite_price;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public long getInvite_time() {
                return invite_time;
            }

            public void setInvite_time(long invite_time) {
                this.invite_time = invite_time;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public double getDistance() {
                return distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            public int getJoiner_newstatus() {
                return joiner_newstatus;
            }

            public void setJoiner_newstatus(int joiner_newstatus) {
                this.joiner_newstatus = joiner_newstatus;
            }

            public int getInvite_id() {
                return invite_id;
            }

            public void setInvite_id(int invite_id) {
                this.invite_id = invite_id;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public long getUser_birthday() {
                return user_birthday;
            }

            public void setUser_birthday(long user_birthday) {
                this.user_birthday = user_birthday;
            }

            public String getInvite_position() {
                return invite_position;
            }

            public void setInvite_position(String invite_position) {
                this.invite_position = invite_position;
            }

            public int getInvite_preview() {
                return invite_preview;
            }

            public void setInvite_preview(int invite_preview) {
                this.invite_preview = invite_preview;
            }
        }
    }
}
