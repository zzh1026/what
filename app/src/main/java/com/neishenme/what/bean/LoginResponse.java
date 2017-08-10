package com.neishenme.what.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 作者：zhaozh create on 2016/5/12 15:22
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class LoginResponse extends RBResponse {

    /**
     * code : 1
     * data : {"user":{"account":"568","activitypurse":11000,"audioDuration":8000,"audioFile":"audios/17aabc050d294411bf8a82804a49d6a1.mp3","birthday":235152000000,"class":"com.nsm.nsmserver.model.UserModel","createTime":1456742495026,"extObj":{"interests":[{"alias":"最爱歌手","key":"music_singer","name":"歌手","placeholder":"请填写歌手","sortord":2},{"alias":"最爱歌曲","key":"music_name","name":"曲名","placeholder":"请填写曲名","sortord":3},{"alias":"最爱主演","key":"movie_singer","name":"主演","placeholder":"请填写主演","sortord":2},{"alias":"最爱电影","key":"movie_name","name":"电影","placeholder":"请填写电影","sortord":4},{"alias":"最爱书籍","key":"book_name","name":"书名","placeholder":"请填写书名","sortord":3},{"alias":"最爱作者","key":"book_author","name":"作者","placeholder":"请填写作者","sortord":2}],"photos":[]},"gender":1,"hxUserName":"nsm568","id":568,"logo":"images/c56f19e59d5a4ad4bd13f7890d066a85/source.jpg","logoState":0,"name":"进了我一","password":"1ca6f8ddb0480af179e75b7e34572098","payPassword":"1ca6f8ddb0480af179e75b7e34572098","phone":"18810128795","pulishSms":0,"purse":6556,"sign":"逻辑咯UFO","state":0,"statu":100,"thumbnailslogo":"images/c56f19e59d5a4ad4bd13f7890d066a85/source_100x100.jpg","updateTime":1456742495026,"videoDuration":0,"videoFile":"files/77870e75ca004aa591a5fe3957512c37.mp4","videoThumb":"images/9f5e9ac33d15477ca097c29c7e769dbf/source.jpg","vipLevel":0}}
     * message : success
     */

    private int code;
    /**
     * user : {"account":"568","activitypurse":11000,"audioDuration":8000,"audioFile":"audios/17aabc050d294411bf8a82804a49d6a1.mp3","birthday":235152000000,"class":"com.nsm.nsmserver.model.UserModel","createTime":1456742495026,"extObj":{"interests":[{"alias":"最爱歌手","key":"music_singer","name":"歌手","placeholder":"请填写歌手","sortord":2},{"alias":"最爱歌曲","key":"music_name","name":"曲名","placeholder":"请填写曲名","sortord":3},{"alias":"最爱主演","key":"movie_singer","name":"主演","placeholder":"请填写主演","sortord":2},{"alias":"最爱电影","key":"movie_name","name":"电影","placeholder":"请填写电影","sortord":4},{"alias":"最爱书籍","key":"book_name","name":"书名","placeholder":"请填写书名","sortord":3},{"alias":"最爱作者","key":"book_author","name":"作者","placeholder":"请填写作者","sortord":2}],"photos":[]},"gender":1,"hxUserName":"nsm568","id":568,"logo":"images/c56f19e59d5a4ad4bd13f7890d066a85/source.jpg","logoState":0,"name":"进了我一","password":"1ca6f8ddb0480af179e75b7e34572098","payPassword":"1ca6f8ddb0480af179e75b7e34572098","phone":"18810128795","pulishSms":0,"purse":6556,"sign":"逻辑咯UFO","state":0,"statu":100,"thumbnailslogo":"images/c56f19e59d5a4ad4bd13f7890d066a85/source_100x100.jpg","updateTime":1456742495026,"videoDuration":0,"videoFile":"files/77870e75ca004aa591a5fe3957512c37.mp4","videoThumb":"images/9f5e9ac33d15477ca097c29c7e769dbf/source.jpg","vipLevel":0}
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
        /**
         * account : 568
         * activitypurse : 11000.0
         * audioDuration : 8000
         * audioFile : audios/17aabc050d294411bf8a82804a49d6a1.mp3
         * birthday : 235152000000
         * class : com.nsm.nsmserver.model.UserModel
         * createTime : 1456742495026
         * extObj : {"interests":[{"alias":"最爱歌手","key":"music_singer","name":"歌手","placeholder":"请填写歌手","sortord":2},{"alias":"最爱歌曲","key":"music_name","name":"曲名","placeholder":"请填写曲名","sortord":3},{"alias":"最爱主演","key":"movie_singer","name":"主演","placeholder":"请填写主演","sortord":2},{"alias":"最爱电影","key":"movie_name","name":"电影","placeholder":"请填写电影","sortord":4},{"alias":"最爱书籍","key":"book_name","name":"书名","placeholder":"请填写书名","sortord":3},{"alias":"最爱作者","key":"book_author","name":"作者","placeholder":"请填写作者","sortord":2}],"photos":[]}
         * gender : 1
         * hxUserName : nsm568
         * id : 568
         * logo : images/c56f19e59d5a4ad4bd13f7890d066a85/source.jpg
         * logoState : 0
         * name : 进了我一
         * password : 1ca6f8ddb0480af179e75b7e34572098
         * payPassword : 1ca6f8ddb0480af179e75b7e34572098
         * phone : 18810128795
         * pulishSms : 0
         * purse : 6556.0
         * sign : 逻辑咯UFO
         * state : 0
         * statu : 100
         * thumbnailslogo : images/c56f19e59d5a4ad4bd13f7890d066a85/source_100x100.jpg
         * updateTime : 1456742495026
         * videoDuration : 0
         * videoFile : files/77870e75ca004aa591a5fe3957512c37.mp4
         * videoThumb : images/9f5e9ac33d15477ca097c29c7e769dbf/source.jpg
         * vipLevel : 0
         */

        private UserEntity user;

        public void setUser(UserEntity user) {
            this.user = user;
        }

        public UserEntity getUser() {
            return user;
        }

        public static class UserEntity {
            private String account;
            private double activitypurse;
            private int audioDuration;
            private String audioFile;
            private long birthday;
            @SerializedName("class")
            private String classX;
            private long createTime;
            private ExtObjEntity extObj;
            private String gender;
            private String hxUserName;
            private String token;
            private int id;
            private String logo;
            private int logoState;
            private String name;
            private String password;
            private String payPassword;
            private String phone;
            private String background;
            private int pulishSms;
            private double purse;
            private String sign;
            private int state;
            private int statu;
            private String thumbnailslogo;
            private long updateTime;
            private int videoDuration;
            private String videoFile;
            private String videoThumb;
            private int vipLevel;

            public void setAccount(String account) {
                this.account = account;
            }

            public void setActivitypurse(double activitypurse) {
                this.activitypurse = activitypurse;
            }

            public void setAudioDuration(int audioDuration) {
                this.audioDuration = audioDuration;
            }

            public void setAudioFile(String audioFile) {
                this.audioFile = audioFile;
            }

            public void setBirthday(long birthday) {
                this.birthday = birthday;
            }

            public void setClassX(String classX) {
                this.classX = classX;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public void setExtObj(ExtObjEntity extObj) {
                this.extObj = extObj;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public void setHxUserName(String hxUserName) {
                this.hxUserName = hxUserName;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public void setLogoState(int logoState) {
                this.logoState = logoState;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public void setPayPassword(String payPassword) {
                this.payPassword = payPassword;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public void setBackground(String background) {
                this.background = background;
            }

            public void setPulishSms(int pulishSms) {
                this.pulishSms = pulishSms;
            }

            public void setPurse(double purse) {
                this.purse = purse;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public void setState(int state) {
                this.state = state;
            }

            public void setStatu(int statu) {
                this.statu = statu;
            }

            public void setThumbnailslogo(String thumbnailslogo) {
                this.thumbnailslogo = thumbnailslogo;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public void setVideoDuration(int videoDuration) {
                this.videoDuration = videoDuration;
            }

            public void setVideoFile(String videoFile) {
                this.videoFile = videoFile;
            }

            public void setVideoThumb(String videoThumb) {
                this.videoThumb = videoThumb;
            }

            public void setVipLevel(int vipLevel) {
                this.vipLevel = vipLevel;
            }

            public String getAccount() {
                return account;
            }

            public double getActivitypurse() {
                return activitypurse;
            }

            public int getAudioDuration() {
                return audioDuration;
            }

            public String getAudioFile() {
                return audioFile;
            }

            public long getBirthday() {
                return birthday;
            }

            public String getClassX() {
                return classX;
            }

            public long getCreateTime() {
                return createTime;
            }

            public ExtObjEntity getExtObj() {
                return extObj;
            }

            public String getGender() {
                return gender;
            }

            public String getHxUserName() {
                return hxUserName;
            }

            public String getToken() {
                return token;
            }

            public int getId() {
                return id;
            }

            public String getLogo() {
                return logo;
            }

            public int getLogoState() {
                return logoState;
            }

            public String getName() {
                return name;
            }

            public String getPassword() {
                return password;
            }

            public String getPayPassword() {
                return payPassword;
            }

            public String getPhone() {
                return phone;
            }

            public String getBackground() {
                return background;
            }

            public int getPulishSms() {
                return pulishSms;
            }

            public double getPurse() {
                return purse;
            }

            public String getSign() {
                return sign;
            }

            public int getState() {
                return state;
            }

            public int getStatu() {
                return statu;
            }

            public String getThumbnailslogo() {
                return thumbnailslogo;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public int getVideoDuration() {
                return videoDuration;
            }

            public String getVideoFile() {
                return videoFile;
            }

            public String getVideoThumb() {
                return videoThumb;
            }

            public int getVipLevel() {
                return vipLevel;
            }

            public static class ExtObjEntity {
                /**
                 * alias : 最爱歌手
                 * key : music_singer
                 * name : 歌手
                 * placeholder : 请填写歌手
                 * sortord : 2
                 */

                private List<InterestsEntity> interests;
                private List<?> photos;

                public void setInterests(List<InterestsEntity> interests) {
                    this.interests = interests;
                }

                public void setPhotos(List<?> photos) {
                    this.photos = photos;
                }

                public List<InterestsEntity> getInterests() {
                    return interests;
                }

                public List<?> getPhotos() {
                    return photos;
                }

                public static class InterestsEntity {
                    private String alias;
                    private String key;
                    private String name;
                    private String placeholder;
                    private int sortord;

                    public void setAlias(String alias) {
                        this.alias = alias;
                    }

                    public void setKey(String key) {
                        this.key = key;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public void setPlaceholder(String placeholder) {
                        this.placeholder = placeholder;
                    }

                    public void setSortord(int sortord) {
                        this.sortord = sortord;
                    }

                    public String getAlias() {
                        return alias;
                    }

                    public String getKey() {
                        return key;
                    }

                    public String getName() {
                        return name;
                    }

                    public String getPlaceholder() {
                        return placeholder;
                    }

                    public int getSortord() {
                        return sortord;
                    }
                }
            }
        }
    }
}
