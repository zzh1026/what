package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/1/5.
 */

public class HomeJoinLogoListResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"invites":[{"userId":2294,"logo":"images/c9a4ee5a874e4bc0a128cbeb99f6d34a/source.jpg","thumbnailslogo":"images/c9a4ee5a874e4bc0a128cbeb99f6d34a/source_100x100.jpg","joinPrice":0,"vip_type":1,"createTime":1483596129638},{"userId":2295,"logo":"images/06f21a5a896e4e97b7b5fe7844f46126/source.jpg","thumbnailslogo":"images/06f21a5a896e4e97b7b5fe7844f46126/source_100x100.jpg","joinPrice":0,"vip_type":0,"createTime":1483596201976},{"userId":816,"logo":"images/456378c6639b4e359e5abfd25e2c63a8/source.jpg","thumbnailslogo":"images/456378c6639b4e359e5abfd25e2c63a8/source_100x100.jpg","joinPrice":0,"vip_type":1,"createTime":1483596478841}]}
     */

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
        /**
         * userId : 2294
         * logo : images/c9a4ee5a874e4bc0a128cbeb99f6d34a/source.jpg
         * thumbnailslogo : images/c9a4ee5a874e4bc0a128cbeb99f6d34a/source_100x100.jpg
         * joinPrice : 0
         * vip_type : 1
         * createTime : 1483596129638
         */

        private List<InvitesBean> invites;

        public List<InvitesBean> getInvites() {
            return invites;
        }

        public void setInvites(List<InvitesBean> invites) {
            this.invites = invites;
        }

        public static class InvitesBean {
            private long userId;
            private String logo;
            private String thumbnailslogo;
            private double joinPrice;
            private int vip_type;
            private long createTime;

            public long getUserId() {
                return userId;
            }

            public void setUserId(long userId) {
                this.userId = userId;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getThumbnailslogo() {
                return thumbnailslogo;
            }

            public void setThumbnailslogo(String thumbnailslogo) {
                this.thumbnailslogo = thumbnailslogo;
            }

            public double getJoinPrice() {
                return joinPrice;
            }

            public void setJoinPrice(double joinPrice) {
                this.joinPrice = joinPrice;
            }

            public int getVip_type() {
                return vip_type;
            }

            public void setVip_type(int vip_type) {
                this.vip_type = vip_type;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }
        }
    }
}
