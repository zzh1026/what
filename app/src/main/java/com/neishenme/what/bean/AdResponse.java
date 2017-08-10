package com.neishenme.what.bean;

/**
 * 作者：zhaozh create on 2016/4/27 20:48
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class AdResponse extends RBResponse {


    /**
     * code : 1
     * message : success
     * data : {"guide":{"vedio":"http://www.neishenme.com/app/1.mp4","type":"1"},"image":"http://192.168.3.99:8888/images/dd7c98b1ee874ad4b5046ffec7d7a35f/source.png","version":23}
     */

    private int code;
    private String message;
    /**
     * guide : {"vedio":"http://www.neishenme.com/app/1.mp4","type":"1"}
     * image : http://192.168.3.99:8888/images/dd7c98b1ee874ad4b5046ffec7d7a35f/source.png
     * version : 23
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
        /**
         * vedio : http://www.neishenme.com/app/1.mp4
         * type : 1
         */

        private GuideBean guide;
        private String image;
        private String link;
        private int version;

        public GuideBean getGuide() {
            return guide;
        }

        public void setGuide(GuideBean guide) {
            this.guide = guide;
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

        public int getVersion() {
            return version;
        }

        public String getLink() {
            return link;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public static class GuideBean {
            private String vedio;
            private String type;

            public String getVedio() {
                return vedio;
            }

            public void setVedio(String vedio) {
                this.vedio = vedio;
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
