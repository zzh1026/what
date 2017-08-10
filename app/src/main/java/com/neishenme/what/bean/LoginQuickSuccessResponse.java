package com.neishenme.what.bean;

import java.util.List;

/**
 * 作者：zhaozh create on 2016/5/13 20:59
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class LoginQuickSuccessResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"photos":[{"id":1869,"photoState":1,"thumbnails":"http://192.168.3.99:8888/images/43f44592ef26453fba1c5128799c309b/source.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/images/7b0de67323dd436abb5d58b363307918/source_100x100.jpg","photo":"http://192.168.3.99:8888/images/7b0de67323dd436abb5d58b363307918/source.jpg"},{"id":1870,"photoState":1,"thumbnails":"http://192.168.3.99:8888/images/213b01a28a1844a88912c67627c4c0bb/source.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/images/f5c2b198d8d14efb8d7fbcbc0de21c8c/source_100x100.jpg","photo":"http://192.168.3.99:8888/images/f5c2b198d8d14efb8d7fbcbc0de21c8c/source.jpg"},{"id":1871,"photoState":1,"thumbnails":"http://192.168.3.99:8888/images/57e8b2ddd48545198d455296d7759221/source.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/images/bab6c67f83fd4c2c85715874f19bfe0f/source_100x100.jpg","photo":"http://192.168.3.99:8888/images/bab6c67f83fd4c2c85715874f19bfe0f/source.jpg"},{"id":1872,"photoState":0,"thumbnails":"http://192.168.3.99:8888/users/58/source_150x150.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/users/58/source_100x100.jpg","photo":"http://192.168.3.99:8888/users/58/source.jpg"},{"id":1873,"photoState":0,"thumbnails":"http://192.168.3.99:8888/users/58/source1_150x150.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/users/58/source1_100x100.jpg","photo":"http://192.168.3.99:8888/users/58/source1.jpg"},{"id":1874,"photoState":0,"thumbnails":"http://192.168.3.99:8888/users/58/source2_150x150.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/users/58/source2_100x100.jpg","photo":"http://192.168.3.99:8888/users/58/source2.jpg"},{"id":1875,"photoState":0,"thumbnails":"http://192.168.3.99:8888/users/58/source.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/users/58/source_100x100.jpg","photo":"http://192.168.3.99:8888/users/58/source.jpg"},{"id":1876,"photoState":0,"thumbnails":"http://192.168.3.99:8888/users/58/source1.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/users/58/source1_100x100.jpg","photo":"http://192.168.3.99:8888/users/58/source1.jpg"},{"id":1877,"photoState":0,"thumbnails":"http://192.168.3.99:8888/users/58/source2.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/users/58/source2_100x100.jpg","photo":"http://192.168.3.99:8888/users/58/source2.jpg"}],"interests":[{"content":null,"placeholder":"请填写旅途","name":"旅途","key":"trip_name","sortord":4,"alias":"我的旅途"},{"content":null,"placeholder":"请填写运动","name":"运动","key":"sport_name","sortord":5,"alias":"我喜欢的运动"},{"content":"high","placeholder":"请填写歌手","name":"歌手","key":"music_singer","sortord":1,"alias":"我喜欢的歌手"},{"content":"adsa","placeholder":"请填写电影","name":"电影","key":"movie_name","sortord":2,"alias":"我喜欢的电影"},{"content":null,"placeholder":"请填写美食","name":"美食","key":"food_name","sortord":3,"alias":"我喜欢的美食"}],"user":{"birthday":461347200000,"logo":"http://192.168.3.99:8888/images/ca08312290d44bbd955ff87f16cb4f55/source.jpg","phone":"06786","videoThumb":"http://192.168.3.99:8888/images/1dc427de6ae142dd8bc27586c35b805a/source.jpg","thumbnailslogo":"http://192.168.3.99:8888/images/ca08312290d44bbd955ff87f16cb4f55/source_100x100.jpg","sign":"hdjidiiid","id":46,"videoFile":"http://192.168.3.99:8888/files/1c88ce9c069444349c80aabe00aba428.mp4","hxUserName":"nsm46","placeId":null,"background":null,"name":"ellzu1","account":"ellzu1","gender":1,"logoState":0,"audioDuration":5000,"audioFile":"http://192.168.3.99:8888/audios/154c40f5912247cdbf8a0acfe54987c2.mp3","videoDuration":0}}
     */

    private int code;
    private String message;
    /**
     * photos : [{"id":1869,"photoState":1,"thumbnails":"http://192.168.3.99:8888/images/43f44592ef26453fba1c5128799c309b/source.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/images/7b0de67323dd436abb5d58b363307918/source_100x100.jpg","photo":"http://192.168.3.99:8888/images/7b0de67323dd436abb5d58b363307918/source.jpg"},{"id":1870,"photoState":1,"thumbnails":"http://192.168.3.99:8888/images/213b01a28a1844a88912c67627c4c0bb/source.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/images/f5c2b198d8d14efb8d7fbcbc0de21c8c/source_100x100.jpg","photo":"http://192.168.3.99:8888/images/f5c2b198d8d14efb8d7fbcbc0de21c8c/source.jpg"},{"id":1871,"photoState":1,"thumbnails":"http://192.168.3.99:8888/images/57e8b2ddd48545198d455296d7759221/source.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/images/bab6c67f83fd4c2c85715874f19bfe0f/source_100x100.jpg","photo":"http://192.168.3.99:8888/images/bab6c67f83fd4c2c85715874f19bfe0f/source.jpg"},{"id":1872,"photoState":0,"thumbnails":"http://192.168.3.99:8888/users/58/source_150x150.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/users/58/source_100x100.jpg","photo":"http://192.168.3.99:8888/users/58/source.jpg"},{"id":1873,"photoState":0,"thumbnails":"http://192.168.3.99:8888/users/58/source1_150x150.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/users/58/source1_100x100.jpg","photo":"http://192.168.3.99:8888/users/58/source1.jpg"},{"id":1874,"photoState":0,"thumbnails":"http://192.168.3.99:8888/users/58/source2_150x150.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/users/58/source2_100x100.jpg","photo":"http://192.168.3.99:8888/users/58/source2.jpg"},{"id":1875,"photoState":0,"thumbnails":"http://192.168.3.99:8888/users/58/source.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/users/58/source_100x100.jpg","photo":"http://192.168.3.99:8888/users/58/source.jpg"},{"id":1876,"photoState":0,"thumbnails":"http://192.168.3.99:8888/users/58/source1.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/users/58/source1_100x100.jpg","photo":"http://192.168.3.99:8888/users/58/source1.jpg"},{"id":1877,"photoState":0,"thumbnails":"http://192.168.3.99:8888/users/58/source2.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/users/58/source2_100x100.jpg","photo":"http://192.168.3.99:8888/users/58/source2.jpg"}]
     * interests : [{"content":null,"placeholder":"请填写旅途","name":"旅途","key":"trip_name","sortord":4,"alias":"我的旅途"},{"content":null,"placeholder":"请填写运动","name":"运动","key":"sport_name","sortord":5,"alias":"我喜欢的运动"},{"content":"high","placeholder":"请填写歌手","name":"歌手","key":"music_singer","sortord":1,"alias":"我喜欢的歌手"},{"content":"adsa","placeholder":"请填写电影","name":"电影","key":"movie_name","sortord":2,"alias":"我喜欢的电影"},{"content":null,"placeholder":"请填写美食","name":"美食","key":"food_name","sortord":3,"alias":"我喜欢的美食"}]
     * user : {"birthday":461347200000,"logo":"http://192.168.3.99:8888/images/ca08312290d44bbd955ff87f16cb4f55/source.jpg","phone":"06786","videoThumb":"http://192.168.3.99:8888/images/1dc427de6ae142dd8bc27586c35b805a/source.jpg","thumbnailslogo":"http://192.168.3.99:8888/images/ca08312290d44bbd955ff87f16cb4f55/source_100x100.jpg","sign":"hdjidiiid","id":46,"videoFile":"http://192.168.3.99:8888/files/1c88ce9c069444349c80aabe00aba428.mp4","hxUserName":"nsm46","placeId":null,"background":null,"name":"ellzu1","account":"ellzu1","gender":1,"logoState":0,"audioDuration":5000,"audioFile":"http://192.168.3.99:8888/audios/154c40f5912247cdbf8a0acfe54987c2.mp3","videoDuration":0}
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
         * birthday : 461347200000
         * logo : http://192.168.3.99:8888/images/ca08312290d44bbd955ff87f16cb4f55/source.jpg
         * phone : 06786
         * videoThumb : http://192.168.3.99:8888/images/1dc427de6ae142dd8bc27586c35b805a/source.jpg
         * thumbnailslogo : http://192.168.3.99:8888/images/ca08312290d44bbd955ff87f16cb4f55/source_100x100.jpg
         * sign : hdjidiiid
         * id : 46
         * videoFile : http://192.168.3.99:8888/files/1c88ce9c069444349c80aabe00aba428.mp4
         * hxUserName : nsm46
         * placeId : null
         * background : null
         * name : ellzu1
         * account : ellzu1
         * gender : 1
         * logoState : 0
         * audioDuration : 5000
         * audioFile : http://192.168.3.99:8888/audios/154c40f5912247cdbf8a0acfe54987c2.mp3
         * videoDuration : 0
         */

        private UserEntity user;
        /**
         * id : 1869
         * photoState : 1
         * thumbnails : http://192.168.3.99:8888/images/43f44592ef26453fba1c5128799c309b/source.jpg
         * thumbnailsPhoto : http://192.168.3.99:8888/images/7b0de67323dd436abb5d58b363307918/source_100x100.jpg
         * photo : http://192.168.3.99:8888/images/7b0de67323dd436abb5d58b363307918/source.jpg
         */

        private List<PhotosEntity> photos;
        /**
         * content : null
         * placeholder : 请填写旅途
         * name : 旅途
         * key : trip_name
         * sortord : 4
         * alias : 我的旅途
         */

        private List<InterestsEntity> interests;

        public void setUser(UserEntity user) {
            this.user = user;
        }

        public void setPhotos(List<PhotosEntity> photos) {
            this.photos = photos;
        }

        public void setInterests(List<InterestsEntity> interests) {
            this.interests = interests;
        }

        public UserEntity getUser() {
            return user;
        }

        public List<PhotosEntity> getPhotos() {
            return photos;
        }

        public List<InterestsEntity> getInterests() {
            return interests;
        }

        public static class UserEntity {
            private long birthday;
            private String logo;
            private String phone;
            private String videoThumb;
            private String token;
            private String thumbnailslogo;
            private String sign;
            private int id;
            private String videoFile;
            private String hxUserName;
            private int placeId;
            private String background;
            private String name;
            private String account;
            private String gender;
            private int logoState;
            private int audioDuration;
            private String audioFile;
            private int videoDuration;

            public void setBirthday(long birthday) {
                this.birthday = birthday;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public void setVideoThumb(String videoThumb) {
                this.videoThumb = videoThumb;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public void setThumbnailslogo(String thumbnailslogo) {
                this.thumbnailslogo = thumbnailslogo;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setVideoFile(String videoFile) {
                this.videoFile = videoFile;
            }

            public void setHxUserName(String hxUserName) {
                this.hxUserName = hxUserName;
            }

            public void setPlaceId(int placeId) {
                this.placeId = placeId;
            }

            public void setBackground(String background) {
                this.background = background;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public void setLogoState(int logoState) {
                this.logoState = logoState;
            }

            public void setAudioDuration(int audioDuration) {
                this.audioDuration = audioDuration;
            }

            public void setAudioFile(String audioFile) {
                this.audioFile = audioFile;
            }

            public void setVideoDuration(int videoDuration) {
                this.videoDuration = videoDuration;
            }

            public long getBirthday() {
                return birthday;
            }

            public String getLogo() {
                return logo;
            }

            public String getPhone() {
                return phone;
            }

            public String getVideoThumb() {
                return videoThumb;
            }

            public String getToken() {
                return token;
            }

            public String getThumbnailslogo() {
                return thumbnailslogo;
            }

            public String getSign() {
                return sign;
            }

            public int getId() {
                return id;
            }

            public String getVideoFile() {
                return videoFile;
            }

            public String getHxUserName() {
                return hxUserName;
            }

            public int getPlaceId() {
                return placeId;
            }

            public String getBackground() {
                return background;
            }

            public String getName() {
                return name;
            }

            public String getAccount() {
                return account;
            }

            public String getGender() {
                return gender;
            }

            public int getLogoState() {
                return logoState;
            }

            public int getAudioDuration() {
                return audioDuration;
            }

            public String getAudioFile() {
                return audioFile;
            }

            public int getVideoDuration() {
                return videoDuration;
            }
        }

        public static class PhotosEntity {
            private int id;
            private int photoState;
            private String thumbnails;
            private String thumbnailsPhoto;
            private String photo;

            public void setId(int id) {
                this.id = id;
            }

            public void setPhotoState(int photoState) {
                this.photoState = photoState;
            }

            public void setThumbnails(String thumbnails) {
                this.thumbnails = thumbnails;
            }

            public void setThumbnailsPhoto(String thumbnailsPhoto) {
                this.thumbnailsPhoto = thumbnailsPhoto;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public int getId() {
                return id;
            }

            public int getPhotoState() {
                return photoState;
            }

            public String getThumbnails() {
                return thumbnails;
            }

            public String getThumbnailsPhoto() {
                return thumbnailsPhoto;
            }

            public String getPhoto() {
                return photo;
            }
        }

        public static class InterestsEntity {
            private Object content;
            private String placeholder;
            private String name;
            private String key;
            private int sortord;
            private String alias;

            public void setContent(Object content) {
                this.content = content;
            }

            public void setPlaceholder(String placeholder) {
                this.placeholder = placeholder;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public void setSortord(int sortord) {
                this.sortord = sortord;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public Object getContent() {
                return content;
            }

            public String getPlaceholder() {
                return placeholder;
            }

            public String getName() {
                return name;
            }

            public String getKey() {
                return key;
            }

            public int getSortord() {
                return sortord;
            }

            public String getAlias() {
                return alias;
            }
        }
    }
}
