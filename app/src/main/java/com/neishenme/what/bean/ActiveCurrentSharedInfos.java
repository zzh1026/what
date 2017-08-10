package com.neishenme.what.bean;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/11/30.
 */

public class ActiveCurrentSharedInfos extends RBResponse {

    /**
     * code : 1
     * data : {"active":{"ctitle":"My activities","endJoinTime":1480867199999,"endPublishTime":1481212799999,"endShowTime":1481212799999,"shareDescribe":"内什么活动等你来","shareImage":"http://www.neishenme.com/image_new/download.png","shareLink":"http://www.neishenme.com/","shareTitle":"内什么都行","startJoinTime":1480348800000,"startPublishTime":1480867200000,"startShowTime":1480348800000,"title":"我的活动","type":"takemeout"}}
     * message : success
     */

    private int code;
    /**
     * active : {"ctitle":"My activities","endJoinTime":1480867199999,"endPublishTime":1481212799999,"endShowTime":1481212799999,"shareDescribe":"内什么活动等你来","shareImage":"http://www.neishenme.com/image_new/download.png","shareLink":"http://www.neishenme.com/","shareTitle":"内什么都行","startJoinTime":1480348800000,"startPublishTime":1480867200000,"startShowTime":1480348800000,"title":"我的活动","type":"takemeout"}
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
         * ctitle : My activities
         * endJoinTime : 1480867199999
         * endPublishTime : 1481212799999
         * endShowTime : 1481212799999
         * shareDescribe : 内什么活动等你来
         * shareImage : http://www.neishenme.com/image_new/download.png
         * shareLink : http://www.neishenme.com/
         * shareTitle : 内什么都行
         * startJoinTime : 1480348800000
         * startPublishTime : 1480867200000
         * startShowTime : 1480348800000
         * title : 我的活动
         * type : takemeout
         */

        private ActiveBean active;

        public ActiveBean getActive() {
            return active;
        }

        public void setActive(ActiveBean active) {
            this.active = active;
        }

        public static class ActiveBean {
            private String ctitle;
            private long endJoinTime;
            private long endPublishTime;
            private long endShowTime;
            private String shareDescribe;
            private String shareImage;
            private String shareLink;
            private String shareTitle;
            private long startJoinTime;
            private long startPublishTime;
            private long startShowTime;
            private String title;
            private String type;

            public String getCtitle() {
                return ctitle;
            }

            public void setCtitle(String ctitle) {
                this.ctitle = ctitle;
            }

            public long getEndJoinTime() {
                return endJoinTime;
            }

            public void setEndJoinTime(long endJoinTime) {
                this.endJoinTime = endJoinTime;
            }

            public long getEndPublishTime() {
                return endPublishTime;
            }

            public void setEndPublishTime(long endPublishTime) {
                this.endPublishTime = endPublishTime;
            }

            public long getEndShowTime() {
                return endShowTime;
            }

            public void setEndShowTime(long endShowTime) {
                this.endShowTime = endShowTime;
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

            public String getShareLink() {
                return shareLink;
            }

            public void setShareLink(String shareLink) {
                this.shareLink = shareLink;
            }

            public String getShareTitle() {
                return shareTitle;
            }

            public void setShareTitle(String shareTitle) {
                this.shareTitle = shareTitle;
            }

            public long getStartJoinTime() {
                return startJoinTime;
            }

            public void setStartJoinTime(long startJoinTime) {
                this.startJoinTime = startJoinTime;
            }

            public long getStartPublishTime() {
                return startPublishTime;
            }

            public void setStartPublishTime(long startPublishTime) {
                this.startPublishTime = startPublishTime;
            }

            public long getStartShowTime() {
                return startShowTime;
            }

            public void setStartShowTime(long startShowTime) {
                this.startShowTime = startShowTime;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
