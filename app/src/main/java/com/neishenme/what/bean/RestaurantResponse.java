package com.neishenme.what.bean;

import java.util.List;

import static android.R.attr.type;

/**
 * Created by Administrator on 2016/5/9.
 */
public class RestaurantResponse extends RBResponse {


    /*code: 1,
message: "success",

data: {}*/
    private int code;
    private String message;
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
        private List<MainpushBean> mainpush;

        public List<MainpushBean> getMainpush() {
            return mainpush;
        }

        public void setMainpush(List<MainpushBean> mainpush) {
            this.mainpush = mainpush;
        }


        public static class MainpushBean {
            /*
            * id: 12,
                title: "清幽闲情",
                type: 1,
                serviceId: 38,
                link: null,
                image: "http://192.168.3.99:8888/images/d9292a91e9c247c7922cbe68fff6a7a1/source.png",
                ord: 6,
                state: 100,
                createTime: 1453384756100,
                updateTime: 1461774545060*/

            private int id;
            private String title;
            private int type;
            private int inviteId;
            private int inviteUserId;
            private int serviceId;
            private String link;
            private String image;
            private int ord;
            private int state;
            private long createTime;
            private long updateTime;


            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public int getInviteId() {
                return inviteId;

            }

            public int getInviteUserId() {
                return inviteUserId;
            }

            public void setInviteId(int inviteId) {
                this.inviteId = inviteId;
            }

            public void setInviteUserId(int inviteUserId) {
                this.inviteUserId = inviteUserId;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getServiceId() {
                return serviceId;
            }

            public void setServiceId(int serviceId) {
                this.serviceId = serviceId;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public int getOrd() {
                return ord;
            }

            public void setOrd(int ord) {
                this.ord = ord;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }
        }

    }

}

