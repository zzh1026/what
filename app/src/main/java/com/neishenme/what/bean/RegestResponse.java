package com.neishenme.what.bean;

/**
 * 作者：zhaozh create on 2016/3/16 13:38
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个注册成功的类
 * .
 * 其作用是 :
 */
public class RegestResponse extends RBResponse {


    /**
     * code : 1
     * message : success
     * data : {"user":{"birthday":0,"logo":null,"phone":"91372","videoThumb":null,"thumbnailslogo":null,"sign":null,"id":576,"videoFile":null,"hxUserName":"nsm576","placeId":null,"name":null,"account":"576","gender":0,"logoState":0,"audioDuration":0,"audioFile":null,"videoDuration":0}}
     */

    private int code;
    private String message;
    /**
     * user : {"birthday":0,"logo":null,"phone":"91372","videoThumb":null,"thumbnailslogo":null,"sign":null,"id":576,"videoFile":null,"hxUserName":"nsm576","placeId":null,"name":null,"account":"576","gender":0,"logoState":0,"audioDuration":0,"audioFile":null,"videoDuration":0}
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
         * birthday : 0
         * logo : null
         * phone : 91372
         * videoThumb : null
         * thumbnailslogo : null
         * sign : null
         * id : 576
         * videoFile : null
         * hxUserName : nsm576
         * placeId : null
         * name : null
         * account : 576
         * gender : 0
         * logoState : 0
         * audioDuration : 0
         * audioFile : null
         * videoDuration : 0
         */

        private UserEntity user;

        public void setUser(UserEntity user) {
            this.user = user;
        }

        public UserEntity getUser() {
            return user;
        }

        public static class UserEntity {
            private int birthday;
            private Object logo;
            private String phone;
            private Object videoThumb;
            private Object thumbnailslogo;
            private Object sign;
            private int id;
            private Object videoFile;
            private String hxUserName;
            private Object placeId;
            private Object name;
            private String account;
            private int gender;
            private int logoState;
            private int audioDuration;
            private Object audioFile;
            private int videoDuration;
            private String token;

            public void setBirthday(int birthday) {
                this.birthday = birthday;
            }

            public void setLogo(Object logo) {
                this.logo = logo;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public void setVideoThumb(Object videoThumb) {
                this.videoThumb = videoThumb;
            }

            public void setThumbnailslogo(Object thumbnailslogo) {
                this.thumbnailslogo = thumbnailslogo;
            }

            public void setSign(Object sign) {
                this.sign = sign;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setVideoFile(Object videoFile) {
                this.videoFile = videoFile;
            }

            public void setHxUserName(String hxUserName) {
                this.hxUserName = hxUserName;
            }

            public void setPlaceId(Object placeId) {
                this.placeId = placeId;
            }

            public void setName(Object name) {
                this.name = name;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public void setLogoState(int logoState) {
                this.logoState = logoState;
            }

            public void setAudioDuration(int audioDuration) {
                this.audioDuration = audioDuration;
            }

            public void setAudioFile(Object audioFile) {
                this.audioFile = audioFile;
            }

            public void setVideoDuration(int videoDuration) {
                this.videoDuration = videoDuration;
            }

            public int getBirthday() {
                return birthday;
            }

            public Object getLogo() {
                return logo;
            }

            public String getPhone() {
                return phone;
            }

            public Object getVideoThumb() {
                return videoThumb;
            }

            public Object getThumbnailslogo() {
                return thumbnailslogo;
            }

            public Object getSign() {
                return sign;
            }

            public int getId() {
                return id;
            }

            public Object getVideoFile() {
                return videoFile;
            }

            public String getHxUserName() {
                return hxUserName;
            }

            public Object getPlaceId() {
                return placeId;
            }

            public Object getName() {
                return name;
            }

            public String getAccount() {
                return account;
            }

            public String getToken() {
                return token;
            }

            public int getGender() {
                return gender;
            }

            public int getLogoState() {
                return logoState;
            }

            public int getAudioDuration() {
                return audioDuration;
            }

            public Object getAudioFile() {
                return audioFile;
            }

            public int getVideoDuration() {
                return videoDuration;
            }
        }
    }
}
