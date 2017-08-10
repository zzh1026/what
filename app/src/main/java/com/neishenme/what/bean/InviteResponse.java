package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/12/29.
 */

public class InviteResponse extends RBResponse {


    /**
     * code : 1
     * data : {"invite":{"areaId":35,"id":118141,"newstatus":50,"payType":1,"preview":1187,"setout":0,"target":0,"time":1483538400000,"title":"钱多人不傻，速来","type":0,"userId":666},"inviteMeets":{"address":"朝阳区 三里屯11号院1号楼瑜舍酒店B1楼","areaId":35,"chatTime":0,"id":126,"latitude":116.461528,"longitude":39.943227,"position":"三里屯","price":2000},"joiners":[{"joiner_acceptType":0,"joiner_areaId":35,"joiner_id":803784,"joiner_joinPrice":0,"joiner_newstatus":100,"joiner_setout":0,"joiner_userId":46,"user_logo":"http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source_100x100.jpg","vip_type":2},{"joiner_acceptType":0,"joiner_areaId":35,"joiner_id":802620,"joiner_joinPrice":0,"joiner_newstatus":100,"joiner_setout":0,"joiner_userId":2019,"user_logo":"http://192.168.3.99:8888/images/92bf70eaed984c439195a11629aa468e/source.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/images/92bf70eaed984c439195a11629aa468e/source_100x100.jpg","vip_type":0},{"joiner_acceptType":0,"joiner_areaId":35,"joiner_id":802221,"joiner_joinPrice":0,"joiner_newstatus":100,"joiner_setout":0,"joiner_userId":2092,"user_logo":"http://192.168.3.99:8888/images/9e2f7461f1d0478d887bf4d772fad746/source.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/images/9e2f7461f1d0478d887bf4d772fad746/source_100x100.jpg","vip_type":0},{"joiner_acceptType":0,"joiner_areaId":35,"joiner_id":801927,"joiner_joinPrice":0,"joiner_newstatus":100,"joiner_setout":0,"joiner_userId":2177,"user_logo":"http://192.168.3.99:8888/images/522a62fff71546df9539a71eb9ea077e/source.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/images/522a62fff71546df9539a71eb9ea077e/source_100x100.jpg","vip_type":0},{"joiner_acceptType":0,"joiner_areaId":35,"joiner_id":801327,"joiner_joinPrice":0,"joiner_newstatus":100,"joiner_setout":0,"joiner_userId":2294,"user_logo":"http://192.168.3.99:8888/images/c9a4ee5a874e4bc0a128cbeb99f6d34a/source.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/images/c9a4ee5a874e4bc0a128cbeb99f6d34a/source_100x100.jpg","vip_type":1},{"joiner_acceptType":0,"joiner_areaId":35,"joiner_id":799088,"joiner_joinPrice":0,"joiner_newstatus":100,"joiner_setout":0,"joiner_userId":2103,"user_logo":"http://192.168.3.99:8888/images/453e1f336be8466ebedffcbf4c31cd2a/source.png","user_thumbnailslogo":"http://192.168.3.99:8888/images/453e1f336be8466ebedffcbf4c31cd2a/source_100x100.png","vip_type":0},{"joiner_acceptType":0,"joiner_areaId":35,"joiner_id":799040,"joiner_joinPrice":0,"joiner_newstatus":100,"joiner_setout":0,"joiner_userId":2011,"user_logo":"http://192.168.3.99:8888/images/7dd9fb56a6684ec18f8a8410d663684e/source.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/images/7dd9fb56a6684ec18f8a8410d663684e/source_100x100.jpg","vip_type":0},{"joiner_acceptType":0,"joiner_areaId":35,"joiner_id":798974,"joiner_joinPrice":0,"joiner_newstatus":100,"joiner_setout":0,"joiner_userId":2220,"user_logo":"http://192.168.3.99:8888/images/5c3b3b0436e3435b95eaf465dc7f233e/source.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/images/5c3b3b0436e3435b95eaf465dc7f233e/source_100x100.jpg","vip_type":0}],"user":{"birthday":631123200000,"gender":1,"hxUserName":"nsm666","id":666,"logo":"http://192.168.3.99:8888/users/201605/13/666/logo/ye3pu7mhkt3afeli0twoz9qa/source.jpg","logoState":-1,"name":"是是是","sign":"","thumbnailslogo":"http://192.168.3.99:8888/users/201605/13/666/logo/ye3pu7mhkt3afeli0twoz9qa/scale_300.jpg"},"userPhotos":[{"id":2476,"photo":"http://192.168.3.99:8888/users/201605/13/666/photos/ghozzz6txsk06goqrb1zjfob/source.jpg","photoState":1,"thumbnails":"http://192.168.3.99:8888/users/201605/13/666/photos/ghozzz6txsk06goqrb1zjfob/source.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/users/201605/13/666/photos/ghozzz6txsk06goqrb1zjfob/scale_600.jpg"}]}
     * message : success
     */

    private int code;
    /**
     * invite : {"areaId":35,"id":118141,"newstatus":50,"payType":1,"preview":1187,"setout":0,"target":0,"time":1483538400000,"title":"钱多人不傻，速来","type":0,"userId":666}
     * inviteMeets : {"address":"朝阳区 三里屯11号院1号楼瑜舍酒店B1楼","areaId":35,"chatTime":0,"id":126,"latitude":116.461528,"longitude":39.943227,"position":"三里屯","price":2000}
     * joiners : [{"joiner_acceptType":0,"joiner_areaId":35,"joiner_id":803784,"joiner_joinPrice":0,"joiner_newstatus":100,"joiner_setout":0,"joiner_userId":46,"user_logo":"http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source_100x100.jpg","vip_type":2},{"joiner_acceptType":0,"joiner_areaId":35,"joiner_id":802620,"joiner_joinPrice":0,"joiner_newstatus":100,"joiner_setout":0,"joiner_userId":2019,"user_logo":"http://192.168.3.99:8888/images/92bf70eaed984c439195a11629aa468e/source.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/images/92bf70eaed984c439195a11629aa468e/source_100x100.jpg","vip_type":0},{"joiner_acceptType":0,"joiner_areaId":35,"joiner_id":802221,"joiner_joinPrice":0,"joiner_newstatus":100,"joiner_setout":0,"joiner_userId":2092,"user_logo":"http://192.168.3.99:8888/images/9e2f7461f1d0478d887bf4d772fad746/source.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/images/9e2f7461f1d0478d887bf4d772fad746/source_100x100.jpg","vip_type":0},{"joiner_acceptType":0,"joiner_areaId":35,"joiner_id":801927,"joiner_joinPrice":0,"joiner_newstatus":100,"joiner_setout":0,"joiner_userId":2177,"user_logo":"http://192.168.3.99:8888/images/522a62fff71546df9539a71eb9ea077e/source.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/images/522a62fff71546df9539a71eb9ea077e/source_100x100.jpg","vip_type":0},{"joiner_acceptType":0,"joiner_areaId":35,"joiner_id":801327,"joiner_joinPrice":0,"joiner_newstatus":100,"joiner_setout":0,"joiner_userId":2294,"user_logo":"http://192.168.3.99:8888/images/c9a4ee5a874e4bc0a128cbeb99f6d34a/source.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/images/c9a4ee5a874e4bc0a128cbeb99f6d34a/source_100x100.jpg","vip_type":1},{"joiner_acceptType":0,"joiner_areaId":35,"joiner_id":799088,"joiner_joinPrice":0,"joiner_newstatus":100,"joiner_setout":0,"joiner_userId":2103,"user_logo":"http://192.168.3.99:8888/images/453e1f336be8466ebedffcbf4c31cd2a/source.png","user_thumbnailslogo":"http://192.168.3.99:8888/images/453e1f336be8466ebedffcbf4c31cd2a/source_100x100.png","vip_type":0},{"joiner_acceptType":0,"joiner_areaId":35,"joiner_id":799040,"joiner_joinPrice":0,"joiner_newstatus":100,"joiner_setout":0,"joiner_userId":2011,"user_logo":"http://192.168.3.99:8888/images/7dd9fb56a6684ec18f8a8410d663684e/source.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/images/7dd9fb56a6684ec18f8a8410d663684e/source_100x100.jpg","vip_type":0},{"joiner_acceptType":0,"joiner_areaId":35,"joiner_id":798974,"joiner_joinPrice":0,"joiner_newstatus":100,"joiner_setout":0,"joiner_userId":2220,"user_logo":"http://192.168.3.99:8888/images/5c3b3b0436e3435b95eaf465dc7f233e/source.jpg","user_thumbnailslogo":"http://192.168.3.99:8888/images/5c3b3b0436e3435b95eaf465dc7f233e/source_100x100.jpg","vip_type":0}]
     * user : {"birthday":631123200000,"gender":1,"hxUserName":"nsm666","id":666,"logo":"http://192.168.3.99:8888/users/201605/13/666/logo/ye3pu7mhkt3afeli0twoz9qa/source.jpg","logoState":-1,"name":"是是是","sign":"","thumbnailslogo":"http://192.168.3.99:8888/users/201605/13/666/logo/ye3pu7mhkt3afeli0twoz9qa/scale_300.jpg"}
     * userPhotos : [{"id":2476,"photo":"http://192.168.3.99:8888/users/201605/13/666/photos/ghozzz6txsk06goqrb1zjfob/source.jpg","photoState":1,"thumbnails":"http://192.168.3.99:8888/users/201605/13/666/photos/ghozzz6txsk06goqrb1zjfob/source.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/users/201605/13/666/photos/ghozzz6txsk06goqrb1zjfob/scale_600.jpg"}]
     */

    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * areaId : 35
         * id : 118141
         * newstatus : 50
         * payType : 1
         * preview : 1187
         * setout : 0
         * target : 0
         * time : 1483538400000
         * title : 钱多人不傻，速来
         * type : 0
         * userId : 666
         */

        private InviteBean invite;
        /**
         * address : 朝阳区 三里屯11号院1号楼瑜舍酒店B1楼
         * areaId : 35
         * chatTime : 0
         * id : 126
         * latitude : 116.461528
         * longitude : 39.943227
         * position : 三里屯
         * price : 2000
         */

        private InviteMeetsBean inviteMeets;
        /**
         * birthday : 631123200000
         * gender : 1
         * hxUserName : nsm666
         * id : 666
         * logo : http://192.168.3.99:8888/users/201605/13/666/logo/ye3pu7mhkt3afeli0twoz9qa/source.jpg
         * logoState : -1
         * name : 是是是
         * sign :
         * thumbnailslogo : http://192.168.3.99:8888/users/201605/13/666/logo/ye3pu7mhkt3afeli0twoz9qa/scale_300.jpg
         */

        private UserBean user;
        /**
         * joiner_acceptType : 0
         * joiner_areaId : 35
         * joiner_id : 803784
         * joiner_joinPrice : 0
         * joiner_newstatus : 100
         * joiner_setout : 0
         * joiner_userId : 46
         * user_logo : http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source.jpg
         * user_thumbnailslogo : http://192.168.3.99:8888/images/dede014e9a5e45469c9592347db4a328/source_100x100.jpg
         * vip_type : 2
         */

        private List<JoinersBean> joiners;
        /**
         * id : 2476
         * photo : http://192.168.3.99:8888/users/201605/13/666/photos/ghozzz6txsk06goqrb1zjfob/source.jpg
         * photoState : 1
         * thumbnails : http://192.168.3.99:8888/users/201605/13/666/photos/ghozzz6txsk06goqrb1zjfob/source.jpg
         * thumbnailsPhoto : http://192.168.3.99:8888/users/201605/13/666/photos/ghozzz6txsk06goqrb1zjfob/scale_600.jpg
         */

        private List<UserPhotosBean> userPhotos;

        public InviteBean getInvite() {
            return invite;
        }

        public void setInvite(InviteBean invite) {
            this.invite = invite;
        }

        public InviteMeetsBean getInviteMeets() {
            return inviteMeets;
        }

        public void setInviteMeets(InviteMeetsBean inviteMeets) {
            this.inviteMeets = inviteMeets;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public List<JoinersBean> getJoiners() {
            return joiners;
        }

        public void setJoiners(List<JoinersBean> joiners) {
            this.joiners = joiners;
        }

        public List<UserPhotosBean> getUserPhotos() {
            return userPhotos;
        }

        public void setUserPhotos(List<UserPhotosBean> userPhotos) {
            this.userPhotos = userPhotos;
        }

        public static class InviteBean {
            private int areaId;
            private int id;
            private int newstatus;
            private int payType;
            private int preview;
            private int setout;
            private int target;
            private long time;
            private String title;
            private int type;
            private int userId;
            private String audioFile;
            private String audioDuration;
            private String videoFile;
            private String videoDuration;

            public String getAudioFile() {
                return audioFile;
            }

            public void setAudioFile(String audioFile) {
                this.audioFile = audioFile;
            }

            public String getAudioDuration() {
                return audioDuration;
            }

            public void setAudioDuration(String audioDuration) {
                this.audioDuration = audioDuration;
            }

            public String getVideoFile() {
                return videoFile;
            }

            public void setVideoFile(String videoFile) {
                this.videoFile = videoFile;
            }

            public String getVideoDuration() {
                return videoDuration;
            }

            public void setVideoDuration(String videoDuration) {
                this.videoDuration = videoDuration;
            }

            public int getAreaId() {
                return areaId;
            }

            public void setAreaId(int areaId) {
                this.areaId = areaId;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getNewstatus() {
                return newstatus;
            }

            public void setNewstatus(int newstatus) {
                this.newstatus = newstatus;
            }

            public int getPayType() {
                return payType;
            }

            public void setPayType(int payType) {
                this.payType = payType;
            }

            public int getPreview() {
                return preview;
            }

            public void setPreview(int preview) {
                this.preview = preview;
            }

            public int getSetout() {
                return setout;
            }

            public void setSetout(int setout) {
                this.setout = setout;
            }

            public int getTarget() {
                return target;
            }

            public void setTarget(int target) {
                this.target = target;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }

        public static class InviteMeetsBean {
            private String address;
            private int areaId;
            private long chatTime;
            private int id;
            private double latitude;
            private double longitude;
            private String position;
            private String price;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public int getAreaId() {
                return areaId;
            }

            public void setAreaId(int areaId) {
                this.areaId = areaId;
            }

            public long getChatTime() {
                return chatTime;
            }

            public void setChatTime(long chatTime) {
                this.chatTime = chatTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }

        public static class UserBean {
            private long birthday;
            private int gender;
            private String hxUserName;
            private int id;
            private String logo;
            private int logoState;
            private String name;
            private String sign;
            private String thumbnailslogo;

            public long getBirthday() {
                return birthday;
            }

            public void setBirthday(long birthday) {
                this.birthday = birthday;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
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

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public int getLogoState() {
                return logoState;
            }

            public void setLogoState(int logoState) {
                this.logoState = logoState;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public String getThumbnailslogo() {
                return thumbnailslogo;
            }

            public void setThumbnailslogo(String thumbnailslogo) {
                this.thumbnailslogo = thumbnailslogo;
            }
        }

        public static class JoinersBean {
            private int joiner_acceptType;
            private int joiner_areaId;
            private int joiner_id;
            private double joiner_joinPrice;
            private int joiner_newstatus;
            private int joiner_setout;
            private int joiner_userId;
            private String user_logo;
            private String user_thumbnailslogo;
            private int vip_type;

            public int getJoiner_acceptType() {
                return joiner_acceptType;
            }

            public void setJoiner_acceptType(int joiner_acceptType) {
                this.joiner_acceptType = joiner_acceptType;
            }

            public int getJoiner_areaId() {
                return joiner_areaId;
            }

            public void setJoiner_areaId(int joiner_areaId) {
                this.joiner_areaId = joiner_areaId;
            }

            public int getJoiner_id() {
                return joiner_id;
            }

            public void setJoiner_id(int joiner_id) {
                this.joiner_id = joiner_id;
            }

            public double getJoiner_joinPrice() {
                return joiner_joinPrice;
            }

            public void setJoiner_joinPrice(double joiner_joinPrice) {
                this.joiner_joinPrice = joiner_joinPrice;
            }

            public int getJoiner_newstatus() {
                return joiner_newstatus;
            }

            public void setJoiner_newstatus(int joiner_newstatus) {
                this.joiner_newstatus = joiner_newstatus;
            }

            public int getJoiner_setout() {
                return joiner_setout;
            }

            public void setJoiner_setout(int joiner_setout) {
                this.joiner_setout = joiner_setout;
            }

            public int getJoiner_userId() {
                return joiner_userId;
            }

            public void setJoiner_userId(int joiner_userId) {
                this.joiner_userId = joiner_userId;
            }

            public String getUser_logo() {
                return user_logo;
            }

            public void setUser_logo(String user_logo) {
                this.user_logo = user_logo;
            }

            public String getUser_thumbnailslogo() {
                return user_thumbnailslogo;
            }

            public void setUser_thumbnailslogo(String user_thumbnailslogo) {
                this.user_thumbnailslogo = user_thumbnailslogo;
            }

            public int getVip_type() {
                return vip_type;
            }

            public void setVip_type(int vip_type) {
                this.vip_type = vip_type;
            }
        }

        public static class UserPhotosBean {
            private String id;
            private String photo;
            private int photoState;
            private String thumbnails;
            private String thumbnailsPhoto;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

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
