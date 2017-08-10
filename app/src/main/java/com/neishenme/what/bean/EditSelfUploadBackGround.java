package com.neishenme.what.bean;

/**
 * Created by Administrator on 2016/6/8.
 */
public class EditSelfUploadBackGround {

    /**
     * code : 1
     * message : success
     * data : {"background":"http://192.168.3.99:8888/images/df8801a6ba4345ab81589e27ebe99ca2/source.jpg"}
     */

    private int code;
    private String message;
    /**
     * background : http://192.168.3.99:8888/images/df8801a6ba4345ab81589e27ebe99ca2/source.jpg
     */

    private DataEntity data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private String background;

        public void setBackground(String background) {
            this.background = background;
        }

        public String getBackground() {
            return background;
        }
    }
}
