package com.neishenme.what.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/23.
 */
public class EditPeopleInfoResponse extends RBResponse {


    /**
     * code : 1
     * message : success
     * data : {"photos":[{"id":null,"photoState":0,"thumbnails":"http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source_100x100.jpg","thumbnailsPhoto":null,"photo":"http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source.jpg"},{"id":1870,"photoState":0,"thumbnails":"http://192.168.3.99:8888/images/9268a4211d4d4ea590232f0a34fa4d7f/source_100x100.jpeg","thumbnailsPhoto":"http://192.168.3.99:8888/images/9268a4211d4d4ea590232f0a34fa4d7f/source_100x100.jpeg","photo":"http://192.168.3.99:8888/images/9268a4211d4d4ea590232f0a34fa4d7f/source.jpeg"},{"id":1873,"photoState":0,"thumbnails":"http://192.168.3.99:8888/images/340aa81a358d4c2295a4a8287607d735/source_100x100.jpeg","thumbnailsPhoto":"http://192.168.3.99:8888/images/340aa81a358d4c2295a4a8287607d735/source_100x100.jpeg","photo":"http://192.168.3.99:8888/images/340aa81a358d4c2295a4a8287607d735/source.jpeg"},{"id":1877,"photoState":1,"thumbnails":"http://192.168.3.99:8888/images/f2996a39c1f549d595f0e7835e415c96/source_100x100.jpeg","thumbnailsPhoto":"http://192.168.3.99:8888/images/f2996a39c1f549d595f0e7835e415c96/source_100x100.jpeg","photo":"http://192.168.3.99:8888/images/f2996a39c1f549d595f0e7835e415c96/source.jpeg"},{"id":2018,"photoState":1,"thumbnails":"http://192.168.3.99:8888/images/0ff0d734a47e4c068a47c36dd3423ee8/source_100x100.jpeg","thumbnailsPhoto":"http://192.168.3.99:8888/images/0ff0d734a47e4c068a47c36dd3423ee8/source_100x100.jpeg","photo":"http://192.168.3.99:8888/images/0ff0d734a47e4c068a47c36dd3423ee8/source.jpeg"},{"id":2019,"photoState":1,"thumbnails":"http://192.168.3.99:8888/images/86a7d211d302433b9cf3ce31ce2c26ec/source_100x100.jpeg","thumbnailsPhoto":"http://192.168.3.99:8888/images/86a7d211d302433b9cf3ce31ce2c26ec/source_100x100.jpeg","photo":"http://192.168.3.99:8888/images/86a7d211d302433b9cf3ce31ce2c26ec/source.jpeg"}],"interests":[{"content":"长城","placeholder":"请填写旅途","name":"旅途","key":"trip_name","sortord":4,"alias":"我的旅途"},{"content":"羽毛球;棒球","placeholder":"请填写运动","name":"运动","key":"sport_name","sortord":5,"alias":"我喜欢的运动"},{"content":"许茹芸;你好","placeholder":"请填写歌手","name":"歌手","key":"music_singer","sortord":1,"alias":"我喜欢的歌手"},{"content":"苦酒;Asdasd;Hahahha","placeholder":"请填写电影","name":"电影","key":"movie_name","sortord":2,"alias":"我喜欢的电影"},{"content":"宫保鸡丁","placeholder":"请填写美食","name":"美食","key":"food_name","sortord":3,"alias":"我喜欢的美食"}],"user":{"birthday":895939200000,"logo":"http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source.jpg","phone":"06786","videoThumb":"http://192.168.3.99:8888/images/1dc427de6ae142dd8bc27586c35b805a/source.jpg","thumbnailslogo":"http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source_100x100.jpg","sign":"谁都困难","id":46,"videoFile":"http://192.168.3.99:8888/files/1c88ce9c069444349c80aabe00aba428.mp4","hxUserName":"nsm46","placeId":null,"background":"http://192.168.3.99:8888/images/bb0c5cdf67024d5e916eadc809821519/source.jpg","name":"好啊","account":"ellzu1","gender":1,"logoState":0,"audioDuration":5000,"audioFile":"http://192.168.3.99:8888/audios/154c40f5912247cdbf8a0acfe54987c2.mp3","videoDuration":0}}
     */

    private int code;
    private String message;
    /**
     * photos : [{"id":null,"photoState":0,"thumbnails":"http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source_100x100.jpg","thumbnailsPhoto":null,"photo":"http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source.jpg"},{"id":1870,"photoState":0,"thumbnails":"http://192.168.3.99:8888/images/9268a4211d4d4ea590232f0a34fa4d7f/source_100x100.jpeg","thumbnailsPhoto":"http://192.168.3.99:8888/images/9268a4211d4d4ea590232f0a34fa4d7f/source_100x100.jpeg","photo":"http://192.168.3.99:8888/images/9268a4211d4d4ea590232f0a34fa4d7f/source.jpeg"},{"id":1873,"photoState":0,"thumbnails":"http://192.168.3.99:8888/images/340aa81a358d4c2295a4a8287607d735/source_100x100.jpeg","thumbnailsPhoto":"http://192.168.3.99:8888/images/340aa81a358d4c2295a4a8287607d735/source_100x100.jpeg","photo":"http://192.168.3.99:8888/images/340aa81a358d4c2295a4a8287607d735/source.jpeg"},{"id":1877,"photoState":1,"thumbnails":"http://192.168.3.99:8888/images/f2996a39c1f549d595f0e7835e415c96/source_100x100.jpeg","thumbnailsPhoto":"http://192.168.3.99:8888/images/f2996a39c1f549d595f0e7835e415c96/source_100x100.jpeg","photo":"http://192.168.3.99:8888/images/f2996a39c1f549d595f0e7835e415c96/source.jpeg"},{"id":2018,"photoState":1,"thumbnails":"http://192.168.3.99:8888/images/0ff0d734a47e4c068a47c36dd3423ee8/source_100x100.jpeg","thumbnailsPhoto":"http://192.168.3.99:8888/images/0ff0d734a47e4c068a47c36dd3423ee8/source_100x100.jpeg","photo":"http://192.168.3.99:8888/images/0ff0d734a47e4c068a47c36dd3423ee8/source.jpeg"},{"id":2019,"photoState":1,"thumbnails":"http://192.168.3.99:8888/images/86a7d211d302433b9cf3ce31ce2c26ec/source_100x100.jpeg","thumbnailsPhoto":"http://192.168.3.99:8888/images/86a7d211d302433b9cf3ce31ce2c26ec/source_100x100.jpeg","photo":"http://192.168.3.99:8888/images/86a7d211d302433b9cf3ce31ce2c26ec/source.jpeg"}]
     * interests : [{"content":"长城","placeholder":"请填写旅途","name":"旅途","key":"trip_name","sortord":4,"alias":"我的旅途"},{"content":"羽毛球;棒球","placeholder":"请填写运动","name":"运动","key":"sport_name","sortord":5,"alias":"我喜欢的运动"},{"content":"许茹芸;你好","placeholder":"请填写歌手","name":"歌手","key":"music_singer","sortord":1,"alias":"我喜欢的歌手"},{"content":"苦酒;Asdasd;Hahahha","placeholder":"请填写电影","name":"电影","key":"movie_name","sortord":2,"alias":"我喜欢的电影"},{"content":"宫保鸡丁","placeholder":"请填写美食","name":"美食","key":"food_name","sortord":3,"alias":"我喜欢的美食"}]
     * user : {"birthday":895939200000,"logo":"http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source.jpg","phone":"06786","videoThumb":"http://192.168.3.99:8888/images/1dc427de6ae142dd8bc27586c35b805a/source.jpg","thumbnailslogo":"http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source_100x100.jpg","sign":"谁都困难","id":46,"videoFile":"http://192.168.3.99:8888/files/1c88ce9c069444349c80aabe00aba428.mp4","hxUserName":"nsm46","placeId":null,"background":"http://192.168.3.99:8888/images/bb0c5cdf67024d5e916eadc809821519/source.jpg","name":"好啊","account":"ellzu1","gender":1,"logoState":0,"audioDuration":5000,"audioFile":"http://192.168.3.99:8888/audios/154c40f5912247cdbf8a0acfe54987c2.mp3","videoDuration":0}
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
         * birthday : 895939200000
         * logo : http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source.jpg
         * phone : 06786
         * videoThumb : http://192.168.3.99:8888/images/1dc427de6ae142dd8bc27586c35b805a/source.jpg
         * thumbnailslogo : http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source_100x100.jpg
         * sign : 谁都困难
         * id : 46
         * videoFile : http://192.168.3.99:8888/files/1c88ce9c069444349c80aabe00aba428.mp4
         * hxUserName : nsm46
         * placeId : null
         * background : http://192.168.3.99:8888/images/bb0c5cdf67024d5e916eadc809821519/source.jpg
         * name : 好啊
         * account : ellzu1
         * gender : 1
         * logoState : 0
         * audioDuration : 5000
         * audioFile : http://192.168.3.99:8888/audios/154c40f5912247cdbf8a0acfe54987c2.mp3
         * videoDuration : 0
         */

        private UserEntity user;
        /**
         * id : null
         * photoState : 0
         * thumbnails : http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source_100x100.jpg
         * thumbnailsPhoto : null
         * photo : http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source.jpg
         */

        private List<PhotosEntity> photos;
        /**
         * content : 长城
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
            private String thumbnailslogo;
            private String sign;
            private int id;
            private String videoFile;
            private String hxUserName;
            private Object placeId;
            private String background;
            private String name;
            private String account;
            private int gender;
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

            public void setPlaceId(Object placeId) {
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

            public void setGender(int gender) {
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

            public Object getPlaceId() {
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

            public int getGender() {
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
            private Object id;
            private int photoState;
            private String thumbnails;
            private Object thumbnailsPhoto;
            private String photo;

            public void setId(Object id) {
                this.id = id;
            }

            public void setPhotoState(int photoState) {
                this.photoState = photoState;
            }

            public void setThumbnails(String thumbnails) {
                this.thumbnails = thumbnails;
            }

            public void setThumbnailsPhoto(Object thumbnailsPhoto) {
                this.thumbnailsPhoto = thumbnailsPhoto;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public Object getId() {
                return id;
            }

            public int getPhotoState() {
                return photoState;
            }

            public String getThumbnails() {
                return thumbnails;
            }

            public Object getThumbnailsPhoto() {
                return thumbnailsPhoto;
            }

            public String getPhoto() {
                return photo;
            }
        }

        public static class InterestsEntity {
            private String content;
            private String placeholder;
            private String name;
            private String key;
            private int sortord;
            private String alias;

            public void setContent(String content) {
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

            public String getContent() {
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
