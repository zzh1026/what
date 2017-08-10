package com.neishenme.what.bean;

import java.util.List;

/**
 * 作者：zhaozh create on 2016/5/18 15:33
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class UserDetailResponse extends RBResponse {


    /**
     * code : 1
     * message : success
     * data : {"interests":[{"content":null,"placeholder":"请填写旅途","name":"旅途","key":"trip_name","sortord":4,"alias":"我的旅途"},{"content":null,"placeholder":"请填写运动","name":"运动","key":"sport_name","sortord":5,"alias":"我喜欢的运动"},{"content":null,"placeholder":"请填写歌手","name":"歌手","key":"music_singer","sortord":1,"alias":"我喜欢的歌手"},{"content":null,"placeholder":"请填写电影","name":"电影","key":"movie_name","sortord":2,"alias":"我喜欢的电影"},{"content":null,"placeholder":"请填写美食","name":"美食","key":"food_name","sortord":3,"alias":"我喜欢的美食"}],"photos":[{"photo":"http://neishenmeapp.imwork.net:8888/images/c56f19e59d5a4ad4bd13f7890d066a85/source.jpg","photoState":0,"id":null,"thumbnails":"http://neishenmeapp.imwork.net:8888/images/c56f19e59d5a4ad4bd13f7890d066a85/source_100x100.jpg","thumbnailsPhoto":null}],"user":{"birthday":235152000000,"audioFile":"http://neishenmeapp.imwork.net:8888/audios/78ff328a9f3e422e863668f9c5c65533.mp3","gender":1,"sign":"逻辑咯UFO","placeId":null,"videoThumb":"http://neishenmeapp.imwork.net:8888/images/8ff65e459bca41b7a30d1f97807d3f54/source.jpg","thumbnailslogo":"http://neishenmeapp.imwork.net:8888/images/c56f19e59d5a4ad4bd13f7890d066a85/source_100x100.jpg","logoState":0,"videoDuration":0,"phone":"28795","background":null,"name":"进了我一","logo":"http://neishenmeapp.imwork.net:8888/images/c56f19e59d5a4ad4bd13f7890d066a85/source.jpg","hxUserName":"nsm568","id":568,"videoFile":"http://neishenmeapp.imwork.net:8888/files/bb60411f9a2d44e5ae7d63071f2a9142.mp4","audioDuration":2000,"account":"568"}}
     */

    private int code;
    private String message;
    /**
     * interests : [{"content":null,"placeholder":"请填写旅途","name":"旅途","key":"trip_name","sortord":4,"alias":"我的旅途"},{"content":null,"placeholder":"请填写运动","name":"运动","key":"sport_name","sortord":5,"alias":"我喜欢的运动"},{"content":null,"placeholder":"请填写歌手","name":"歌手","key":"music_singer","sortord":1,"alias":"我喜欢的歌手"},{"content":null,"placeholder":"请填写电影","name":"电影","key":"movie_name","sortord":2,"alias":"我喜欢的电影"},{"content":null,"placeholder":"请填写美食","name":"美食","key":"food_name","sortord":3,"alias":"我喜欢的美食"}]
     * photos : [{"photo":"http://neishenmeapp.imwork.net:8888/images/c56f19e59d5a4ad4bd13f7890d066a85/source.jpg","photoState":0,"id":null,"thumbnails":"http://neishenmeapp.imwork.net:8888/images/c56f19e59d5a4ad4bd13f7890d066a85/source_100x100.jpg","thumbnailsPhoto":null}]
     * user : {"birthday":235152000000,"audioFile":"http://neishenmeapp.imwork.net:8888/audios/78ff328a9f3e422e863668f9c5c65533.mp3","gender":1,"sign":"逻辑咯UFO","placeId":null,"videoThumb":"http://neishenmeapp.imwork.net:8888/images/8ff65e459bca41b7a30d1f97807d3f54/source.jpg","thumbnailslogo":"http://neishenmeapp.imwork.net:8888/images/c56f19e59d5a4ad4bd13f7890d066a85/source_100x100.jpg","logoState":0,"videoDuration":0,"phone":"28795","background":null,"name":"进了我一","logo":"http://neishenmeapp.imwork.net:8888/images/c56f19e59d5a4ad4bd13f7890d066a85/source.jpg","hxUserName":"nsm568","id":568,"videoFile":"http://neishenmeapp.imwork.net:8888/files/bb60411f9a2d44e5ae7d63071f2a9142.mp4","audioDuration":2000,"account":"568"}
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
         * birthday : 235152000000
         * audioFile : http://neishenmeapp.imwork.net:8888/audios/78ff328a9f3e422e863668f9c5c65533.mp3
         * gender : 1
         * sign : 逻辑咯UFO
         * placeId : null
         * videoThumb : http://neishenmeapp.imwork.net:8888/images/8ff65e459bca41b7a30d1f97807d3f54/source.jpg
         * thumbnailslogo : http://neishenmeapp.imwork.net:8888/images/c56f19e59d5a4ad4bd13f7890d066a85/source_100x100.jpg
         * logoState : 0
         * videoDuration : 0
         * phone : 28795
         * background : null
         * name : 进了我一
         * logo : http://neishenmeapp.imwork.net:8888/images/c56f19e59d5a4ad4bd13f7890d066a85/source.jpg
         * hxUserName : nsm568
         * id : 568
         * videoFile : http://neishenmeapp.imwork.net:8888/files/bb60411f9a2d44e5ae7d63071f2a9142.mp4
         * audioDuration : 2000
         * account : 568
         */
        private VipBean vip;
        private int foucs;

        private UserBean user;
        /**
         * content : null
         * placeholder : 请填写旅途
         * name : 旅途
         * key : trip_name
         * sortord : 4
         * alias : 我的旅途
         */

        private List<InterestsBean> interests;
        /**
         * photo : http://neishenmeapp.imwork.net:8888/images/c56f19e59d5a4ad4bd13f7890d066a85/source.jpg
         * photoState : 0
         * id : null
         * thumbnails : http://neishenmeapp.imwork.net:8888/images/c56f19e59d5a4ad4bd13f7890d066a85/source_100x100.jpg
         * thumbnailsPhoto : null
         */

        private List<PhotosBean> photos;

        public VipBean getVip() {
            return vip;
        }

        public void setVip(VipBean vip) {
            this.vip = vip;
        }

        public UserBean getUser() {
            return user;
        }

        public int getFoucs() {
            return foucs;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public void setFoucs(int foucs) {
            this.foucs = foucs;
        }

        public List<InterestsBean> getInterests() {
            return interests;
        }

        public void setInterests(List<InterestsBean> interests) {
            this.interests = interests;
        }

        public List<PhotosBean> getPhotos() {
            return photos;
        }

        public void setPhotos(List<PhotosBean> photos) {
            this.photos = photos;
        }

        public static class VipBean {
            private long viptime;
            private int vipIdentity;
            private int vipFlag;
            private int type;
            private int isvip;

            public long getViptime() {
                return viptime;
            }

            public void setViptime(long viptime) {
                this.viptime = viptime;
            }

            public int getVipIdentity() {
                return vipIdentity;
            }

            public void setVipIdentity(int vipIdentity) {
                this.vipIdentity = vipIdentity;
            }

            public int getVipFlag() {
                return vipFlag;
            }

            public void setVipFlag(int vipFlag) {
                this.vipFlag = vipFlag;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getIsvip() {
                return isvip;
            }

            public void setIsvip(int isvip) {
                this.isvip = isvip;
            }
        }

        public static class UserBean {
            private long birthday;
            private String audioFile;
            private int gender;
            private String sign;
            private int placeId;
            private String videoThumb;
            private String thumbnailslogo;
            private int logoState;
            private int videoDuration;
            private String phone;
            private String background;
            private String name;
            private String logo;
            private String hxUserName;
            private int id;
            private String videoFile;
            private int audioDuration;
            private int foucsUsersCount;

            public int getFoucsUsersCount() {
                return foucsUsersCount;
            }

            public void setFoucsUsersCount(int foucsUsersCount) {
                this.foucsUsersCount = foucsUsersCount;
            }

            private String account;

            public long getBirthday() {
                return birthday;
            }

            public void setBirthday(long birthday) {
                this.birthday = birthday;
            }

            public String getAudioFile() {
                return audioFile;
            }

            public void setAudioFile(String audioFile) {
                this.audioFile = audioFile;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public int getPlaceId() {
                return placeId;
            }

            public void setPlaceId(int placeId) {
                this.placeId = placeId;
            }

            public String getVideoThumb() {
                return videoThumb;
            }

            public void setVideoThumb(String videoThumb) {
                this.videoThumb = videoThumb;
            }

            public String getThumbnailslogo() {
                return thumbnailslogo;
            }

            public void setThumbnailslogo(String thumbnailslogo) {
                this.thumbnailslogo = thumbnailslogo;
            }

            public int getLogoState() {
                return logoState;
            }

            public void setLogoState(int logoState) {
                this.logoState = logoState;
            }

            public int getVideoDuration() {
                return videoDuration;
            }

            public void setVideoDuration(int videoDuration) {
                this.videoDuration = videoDuration;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getBackground() {
                return background;
            }

            public void setBackground(String background) {
                this.background = background;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getHxUserName() {
                return hxUserName;
            }

            public void setHxUserName(String hxUserName) {
                this.hxUserName = hxUserName;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getVideoFile() {
                return videoFile;
            }

            public void setVideoFile(String videoFile) {
                this.videoFile = videoFile;
            }

            public int getAudioDuration() {
                return audioDuration;
            }

            public void setAudioDuration(int audioDuration) {
                this.audioDuration = audioDuration;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }
        }

        public static class InterestsBean {
            private String content;
            private String placeholder;
            private String name;
            private String key;
            private int sortord;
            private String alias;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getPlaceholder() {
                return placeholder;
            }

            public void setPlaceholder(String placeholder) {
                this.placeholder = placeholder;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public int getSortord() {
                return sortord;
            }

            public void setSortord(int sortord) {
                this.sortord = sortord;
            }

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }
        }

        public static class PhotosBean {
            private String photo;
            private int photoState;
            private int id;
            private String thumbnails;
            private String thumbnailsPhoto;

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public int getPhotoState() {
                return photoState;
            }

            public void setPhotoState(int photoState) {
                this.photoState = photoState;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getThumbnails() {
                return thumbnails;
            }

            public void setThumbnails(String thumbnails) {
                this.thumbnails = thumbnails;
            }

            public String getThumbnailsPhoto() {
                return thumbnailsPhoto;
            }

            public void setThumbnailsPhoto(String thumbnailsPhoto) {
                this.thumbnailsPhoto = thumbnailsPhoto;
            }
        }
    }
}
