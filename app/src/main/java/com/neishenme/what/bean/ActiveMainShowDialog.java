package com.neishenme.what.bean;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/11/28.
 */

public class ActiveMainShowDialog extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"isdialog":1,"dialog":{"image":"http://192.168.3.99:8888/images/271c52b4e4054fcebe10562da059de0b/scale_454x915.jpg","style":0,"startTime":1480003200000,"id":1,"page":"takemeout","endTime":1480435199000,"title":"take me out","type":1,"version":5}}
     */

    private int code;
    private String message;
    /**
     * isdialog : 1
     * dialog : {"image":"http://192.168.3.99:8888/images/271c52b4e4054fcebe10562da059de0b/scale_454x915.jpg","style":0,"startTime":1480003200000,"id":1,"page":"takemeout","endTime":1480435199000,"title":"take me out","type":1,"version":5}
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
        private int isdialog;
        /**
         * image : http://192.168.3.99:8888/images/271c52b4e4054fcebe10562da059de0b/scale_454x915.jpg
         * style : 0
         * startTime : 1480003200000
         * id : 1
         * page : takemeout
         * endTime : 1480435199000
         * title : take me out
         * type : 1
         * version : 5
         */

        private DialogBean dialog;

        public int getIsdialog() {
            return isdialog;
        }

        public void setIsdialog(int isdialog) {
            this.isdialog = isdialog;
        }

        public DialogBean getDialog() {
            return dialog;
        }

        public void setDialog(DialogBean dialog) {
            this.dialog = dialog;
        }

        public static class DialogBean {
            private String image;
            private int style;
            private long startTime;
            private int id;
            private String page;
            private long endTime;
            private String title;
            private int type;
            private String version;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public int getStyle() {
                return style;
            }

            public void setStyle(int style) {
                this.style = style;
            }

            public long getStartTime() {
                return startTime;
            }

            public void setStartTime(long startTime) {
                this.startTime = startTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPage() {
                return page;
            }

            public void setPage(String page) {
                this.page = page;
            }

            public long getEndTime() {
                return endTime;
            }

            public void setEndTime(long endTime) {
                this.endTime = endTime;
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

            public void setType(int type) {
                this.type = type;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }
        }
    }
}
