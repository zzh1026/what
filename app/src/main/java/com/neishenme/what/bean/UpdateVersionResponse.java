package com.neishenme.what.bean;

/**
 * 作者：zhaozh create on 2016/3/29 12:31
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class UpdateVersionResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"version":{"id":1,"versionSeq":1,"versionNum":"v1.0.9","androidVersion":"v1.0.1","androidUrl":"http://www.neishenme.com/app/app-nsm.apk","createTime":0,"updateTime":0}}
     */

    private int code;
    private String message;
    /**
     * version : {"id":1,"versionSeq":1,"versionNum":"v1.0.9","androidVersion":"v1.0.1","androidUrl":"http://www.neishenme.com/app/app-nsm.apk","createTime":0,"updateTime":0}
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
        /**
         * id : 1
         * versionSeq : 1
         * versionNum : v1.0.9
         * androidVersion : v1.0.1
         * androidUrl : http://www.neishenme.com/app/app-nsm.apk
         * createTime : 0
         * updateTime : 0
         */

        private VersionEntity version;

        public void setVersion(VersionEntity version) {
            this.version = version;
        }

        public VersionEntity getVersion() {
            return version;
        }

        public static class VersionEntity {
            private int id;
            private int versionSeq;
            private String versionNum;
            private String androidVersion;
            private String androidUrl;
            private int createTime;
            private int updateTime;
            private int androidad;

            public void setId(int id) {
                this.id = id;
            }

            public void setVersionSeq(int versionSeq) {
                this.versionSeq = versionSeq;
            }

            public void setVersionNum(String versionNum) {
                this.versionNum = versionNum;
            }

            public void setAndroidVersion(String androidVersion) {
                this.androidVersion = androidVersion;
            }

            public void setAndroidUrl(String androidUrl) {
                this.androidUrl = androidUrl;
            }

            public void setCreateTime(int createTime) {
                this.createTime = createTime;
            }

            public void setUpdateTime(int updateTime) {
                this.updateTime = updateTime;
            }

            public void setAndroidad(int androidad) {
                this.androidad = androidad;
            }

            public int getId() {
                return id;
            }

            public int getVersionSeq() {
                return versionSeq;
            }

            public String getVersionNum() {
                return versionNum;
            }

            public String getAndroidVersion() {
                return androidVersion;
            }

            public String getAndroidUrl() {
                return androidUrl;
            }

            public int getCreateTime() {
                return createTime;
            }

            public int getUpdateTime() {
                return updateTime;
            }

            public int getAndroidad() {
                return androidad;
            }
        }
    }
}
