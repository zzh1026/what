package com.neishenme.what.bean;

/**
 * Created by Administrator on 2016/4/13.
 */
public class VideoResponse {

    /**
     * code : 1
     * message : success
     * data : {"videoFile":"http://192.168.3.99:8888/files/4bcc2b3c69e8489cb1d148b938be4f96.mp4 ","videoThumb":"http://192.168.3.99:8888/images/07335a7a00c34814b79a3b65aee01a55/source.jpg"}
     */

    private int code;
    private String message;
    /**
     * videoFile : http://192.168.3.99:8888/files/4bcc2b3c69e8489cb1d148b938be4f96.mp4
     * videoThumb : http://192.168.3.99:8888/images/07335a7a00c34814b79a3b65aee01a55/source.jpg
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
        private String videoFile;
        private String videoThumb;

        public String getVideoFile() {
            return videoFile;
        }

        public void setVideoFile(String videoFile) {
            this.videoFile = videoFile;
        }

        public String getVideoThumb() {
            return videoThumb;
        }

        public void setVideoThumb(String videoThumb) {
            this.videoThumb = videoThumb;
        }
    }
}
