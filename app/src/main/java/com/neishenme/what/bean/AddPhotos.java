package com.neishenme.what.bean;

/**
 * Created by Administrator on 2016/6/8.
 */
public class AddPhotos  {


    /**
     * code : 1
     * message : success
     * data : {"photo":{"id":1936,"holderType":0,"holderId":577,"photo":"http://192.168.3.99:8888/images/effb8ea2de274f69bf225a27ef9eb110/source.jpg","thumbnails":"images/78152da6a9984c4e9ad5f8f8017ae3de/source.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/images/effb8ea2de274f69bf225a27ef9eb110/source_100x100.jpg","state":0,"photoState":0,"bestThum":"http://192.168.3.99:8888/images/78152da6a9984c4e9ad5f8f8017ae3de/source.jpg"}}
     */

    private int code;
    private String message;
    /**
     * photo : {"id":1936,"holderType":0,"holderId":577,"photo":"http://192.168.3.99:8888/images/effb8ea2de274f69bf225a27ef9eb110/source.jpg","thumbnails":"images/78152da6a9984c4e9ad5f8f8017ae3de/source.jpg","thumbnailsPhoto":"http://192.168.3.99:8888/images/effb8ea2de274f69bf225a27ef9eb110/source_100x100.jpg","state":0,"photoState":0,"bestThum":"http://192.168.3.99:8888/images/78152da6a9984c4e9ad5f8f8017ae3de/source.jpg"}
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
         * id : 1936
         * holderType : 0
         * holderId : 577
         * photo : http://192.168.3.99:8888/images/effb8ea2de274f69bf225a27ef9eb110/source.jpg
         * thumbnails : images/78152da6a9984c4e9ad5f8f8017ae3de/source.jpg
         * thumbnailsPhoto : http://192.168.3.99:8888/images/effb8ea2de274f69bf225a27ef9eb110/source_100x100.jpg
         * state : 0
         * photoState : 0
         * bestThum : http://192.168.3.99:8888/images/78152da6a9984c4e9ad5f8f8017ae3de/source.jpg
         */

        private PhotoBean photo;

        public PhotoBean getPhoto() {
            return photo;
        }

        public void setPhoto(PhotoBean photo) {
            this.photo = photo;
        }

        public static class PhotoBean {
            private int id;
            private int holderType;
            private int holderId;
            private String photo;
            private String thumbnails;
            private String thumbnailsPhoto;
            private int state;
            private int photoState;
            private String bestThum;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getHolderType() {
                return holderType;
            }

            public void setHolderType(int holderType) {
                this.holderType = holderType;
            }

            public int getHolderId() {
                return holderId;
            }

            public void setHolderId(int holderId) {
                this.holderId = holderId;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
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

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public int getPhotoState() {
                return photoState;
            }

            public void setPhotoState(int photoState) {
                this.photoState = photoState;
            }

            public String getBestThum() {
                return bestThum;
            }

            public void setBestThum(String bestThum) {
                this.bestThum = bestThum;
            }
        }
    }
}
