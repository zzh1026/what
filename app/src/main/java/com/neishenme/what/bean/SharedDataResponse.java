package com.neishenme.what.bean;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/3/7.
 */

public class SharedDataResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"invitecode":"t8Xcmym5","shareTitle":"这个App不推荐给你，我良心难安。","shareUrl":"http://192.168.3.200:80/nsmapi/share/invite.html","shareMinFont":"你的肾上腺素充能情况如何？还能出来混么"}
     */

    private int code;
    private String message;
    /**
     * invitecode : t8Xcmym5
     * shareTitle : 这个App不推荐给你，我良心难安。
     * shareUrl : http://192.168.3.200:80/nsmapi/share/invite.html
     * shareMinFont : 你的肾上腺素充能情况如何？还能出来混么
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
        private String shareLink;
        private String shareImage;
        private String shareTitle;
        private String shareDescribe;

        public String getShareLink() {
            return shareLink;
        }

        public void setShareLink(String shareLink) {
            this.shareLink = shareLink;
        }


        public String getShareDescribe() {
            return shareDescribe;
        }

        public void setShareDescribe(String shareDescribe) {
            this.shareDescribe = shareDescribe;
        }


        public String getShareImage() {
            return shareImage;
        }

        public void setShareImage(String shareImage) {
            this.shareImage = shareImage;
        }


        public String getShareTitle() {
            return shareTitle;
        }

        public void setShareTitle(String shareTitle) {
            this.shareTitle = shareTitle;
        }
    }
}
