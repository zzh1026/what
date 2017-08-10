package com.neishenme.what.bean;

/**
 * 作者：zhaozh create on 2016/8/4 17:12
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class UpPhotoBean {

    /**
     * code : 1
     * data : {"logo":"http://192.168.3.99:8888/images/ecff7d70eb8543258e7f928286bd3c29/source.jpg"}
     * message : success
     */

    private int code;
    /**
     * logo : http://192.168.3.99:8888/images/ecff7d70eb8543258e7f928286bd3c29/source.jpg
     */

    private DataEntity data;
    private String message;

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public DataEntity getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static class DataEntity {
        private String logo;

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getLogo() {
            return logo;
        }
    }
}
