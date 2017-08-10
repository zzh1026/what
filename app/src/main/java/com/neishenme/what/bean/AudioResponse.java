package com.neishenme.what.bean;

/**
 * Created by Administrator on 2016/3/26.
 */
public class AudioResponse {


    /**
     * code : 1
     * message : success
     * data : {"audioFile":"http://res.neishenme.com:8888/audios/c9d5839435a948e4a8632b91b78b21fd.mp3"}
     */

    private int code;
    private String message;
    /**
     * audioFile : http://res.neishenme.com:8888/audios/c9d5839435a948e4a8632b91b78b21fd.mp3
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
        private String audioFile;

        public String getAudioFile() {
            return audioFile;
        }

        public void setAudioFile(String audioFile) {
            this.audioFile = audioFile;
        }
    }
}
