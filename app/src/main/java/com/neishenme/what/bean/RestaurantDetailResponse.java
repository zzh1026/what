package com.neishenme.what.bean;

import java.util.List;

/**
 * 作者：zhaozh create on 2016/5/24 14:19
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class RestaurantDetailResponse extends RBResponse {

    /**
     * code : 1
     * data : {"service":{"content":"1","dinner":"16:00-22:00","id":34,"logo":"http://192.168.3.99:8888/images/8f4f8bdbaef84b5380b40f820236bfb6/source.jpg","lunch":"11:00-13:00","name":"欧式风情","price":2000,"sort":30,"status":0,"storeId":6,"storeLatitude":0,"storeLongitude":0,"timeArray":"11,12,13,16,17,18,19,20,21,22"},"servicemenu":[{"engname":"maltang","name":"麻辣烫"},{"engname":"gongbaojiding","name":"宫保鸡丁"},{"engname":"cow","name":"牛排"},{"engname":"japancishen","name":"欧洲进口新鲜牛排配日本进口生鱼片加韩国沙"},{"engname":"zuiduo","name":"菜单最多20个字"}],"servicephotos":[{"photo":"http://192.168.3.99:8888/images/8f4f8bdbaef84b5380b40f820236bfb6/source.jpg","sort":0,"thumbnails":"images/8f4f8bdbaef84b5380b40f820236bfb6/source.jpg"},{"id":335,"photo":"http://192.168.3.99:8888/service/34/abdfe8f6-654b-4166-8d2b-e451a98ad9e5.jpg","sort":0},{"id":336,"photo":"http://192.168.3.99:8888/service/34/4410b694-a55e-4165-8659-4662510e259c.jpg","sort":0},{"id":337,"photo":"http://192.168.3.99:8888/service/34/18af03bd-92a2-4700-8b8b-fc5137b55b9b.jpg","sort":0},{"id":338,"photo":"http://192.168.3.99:8888/service/34/fb8dacfe-1d22-4991-9d41-f97b8ff47548.jpg","sort":0},{"id":339,"photo":"http://192.168.3.99:8888/service/34/2ceb28cf-f609-47ac-b956-397a8e9b9b07.jpg","sort":0},{"id":340,"photo":"http://192.168.3.99:8888/service/34/5f4ddbca-f4fa-41f7-b5b8-1f918dae040a.jpg","sort":0},{"id":341,"photo":"http://192.168.3.99:8888/service/34/55c6af6f-a1c3-4403-baaa-cadd128cd5cb.jpg","sort":0}],"store":{"addr":"东城区沙滩北街嵩祝寺23号(近五四大街)","addrDetail":"沙滩北街","city":"北京","contact":"010-84002232","id":6,"latitude":116.409902,"logo":"http://192.168.3.99:8888/stores/6/65d68001-16d8-4870-94ad-68cd005de40e.jpg","longitude":39.934137,"name":"Temple Restaurant Beijing","status":1},"storephotos":[{"id":330,"photo":"http://192.168.3.99:8888/stores/6/f3970f58-4e62-453a-b84c-bb08accbd291.jpg","sort":0},{"id":331,"photo":"http://192.168.3.99:8888/stores/6/ed7db82e-760a-412e-9936-7ea62034ca09.jpg","sort":0},{"id":332,"photo":"http://192.168.3.99:8888/stores/6/d4b97c87-e797-4ac1-b5ff-b6482e151f82.jpg","sort":0},{"id":334,"photo":"http://192.168.3.99:8888/stores/6/98009896-ba9f-4936-bf2b-7d664daeab86.jpg","sort":0},{"id":626,"photo":"http://192.168.3.99:8888/images/89a88b7a9403488fb8f3bb7cba51ee6d/source.png","sort":0},{"id":333,"photo":"http://192.168.3.99:8888/stores/6/fa20e487-a9c7-439b-b92c-bf061a8027ba.jpg","sort":3},{"id":329,"photo":"http://192.168.3.99:8888/stores/6/a0128845-a5e7-4c3d-9446-2423db244528.jpg","sort":7}]}
     * message : success
     */

    private int code;
    /**
     * service : {"content":"1","dinner":"16:00-22:00","id":34,"logo":"http://192.168.3.99:8888/images/8f4f8bdbaef84b5380b40f820236bfb6/source.jpg","lunch":"11:00-13:00","name":"欧式风情","price":2000,"sort":30,"status":0,"storeId":6,"storeLatitude":0,"storeLongitude":0,"timeArray":"11,12,13,16,17,18,19,20,21,22"}
     * servicemenu : [{"engname":"maltang","name":"麻辣烫"},{"engname":"gongbaojiding","name":"宫保鸡丁"},{"engname":"cow","name":"牛排"},{"engname":"japancishen","name":"欧洲进口新鲜牛排配日本进口生鱼片加韩国沙"},{"engname":"zuiduo","name":"菜单最多20个字"}]
     * servicephotos : [{"photo":"http://192.168.3.99:8888/images/8f4f8bdbaef84b5380b40f820236bfb6/source.jpg","sort":0,"thumbnails":"images/8f4f8bdbaef84b5380b40f820236bfb6/source.jpg"},{"id":335,"photo":"http://192.168.3.99:8888/service/34/abdfe8f6-654b-4166-8d2b-e451a98ad9e5.jpg","sort":0},{"id":336,"photo":"http://192.168.3.99:8888/service/34/4410b694-a55e-4165-8659-4662510e259c.jpg","sort":0},{"id":337,"photo":"http://192.168.3.99:8888/service/34/18af03bd-92a2-4700-8b8b-fc5137b55b9b.jpg","sort":0},{"id":338,"photo":"http://192.168.3.99:8888/service/34/fb8dacfe-1d22-4991-9d41-f97b8ff47548.jpg","sort":0},{"id":339,"photo":"http://192.168.3.99:8888/service/34/2ceb28cf-f609-47ac-b956-397a8e9b9b07.jpg","sort":0},{"id":340,"photo":"http://192.168.3.99:8888/service/34/5f4ddbca-f4fa-41f7-b5b8-1f918dae040a.jpg","sort":0},{"id":341,"photo":"http://192.168.3.99:8888/service/34/55c6af6f-a1c3-4403-baaa-cadd128cd5cb.jpg","sort":0}]
     * store : {"addr":"东城区沙滩北街嵩祝寺23号(近五四大街)","addrDetail":"沙滩北街","city":"北京","contact":"010-84002232","id":6,"latitude":116.409902,"logo":"http://192.168.3.99:8888/stores/6/65d68001-16d8-4870-94ad-68cd005de40e.jpg","longitude":39.934137,"name":"Temple Restaurant Beijing","status":1}
     * storephotos : [{"id":330,"photo":"http://192.168.3.99:8888/stores/6/f3970f58-4e62-453a-b84c-bb08accbd291.jpg","sort":0},{"id":331,"photo":"http://192.168.3.99:8888/stores/6/ed7db82e-760a-412e-9936-7ea62034ca09.jpg","sort":0},{"id":332,"photo":"http://192.168.3.99:8888/stores/6/d4b97c87-e797-4ac1-b5ff-b6482e151f82.jpg","sort":0},{"id":334,"photo":"http://192.168.3.99:8888/stores/6/98009896-ba9f-4936-bf2b-7d664daeab86.jpg","sort":0},{"id":626,"photo":"http://192.168.3.99:8888/images/89a88b7a9403488fb8f3bb7cba51ee6d/source.png","sort":0},{"id":333,"photo":"http://192.168.3.99:8888/stores/6/fa20e487-a9c7-439b-b92c-bf061a8027ba.jpg","sort":3},{"id":329,"photo":"http://192.168.3.99:8888/stores/6/a0128845-a5e7-4c3d-9446-2423db244528.jpg","sort":7}]
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
         * content : 1
         * dinner : 16:00-22:00
         * id : 34
         * logo : http://192.168.3.99:8888/images/8f4f8bdbaef84b5380b40f820236bfb6/source.jpg
         * lunch : 11:00-13:00
         * name : 欧式风情
         * price : 2000.0
         * sort : 30
         * status : 0
         * storeId : 6
         * storeLatitude : 0.0
         * storeLongitude : 0.0
         * timeArray : 11,12,13,16,17,18,19,20,21,22
         */

        private ServiceEntity service;
        /**
         * addr : 东城区沙滩北街嵩祝寺23号(近五四大街)
         * addrDetail : 沙滩北街
         * city : 北京
         * contact : 010-84002232
         * id : 6
         * latitude : 116.409902
         * logo : http://192.168.3.99:8888/stores/6/65d68001-16d8-4870-94ad-68cd005de40e.jpg
         * longitude : 39.934137
         * name : Temple Restaurant Beijing
         * status : 1
         */

        private StoreEntity store;
        /**
         * engname : maltang
         * name : 麻辣烫
         */

        private List<ServicemenuEntity> servicemenu;
        /**
         * photo : http://192.168.3.99:8888/images/8f4f8bdbaef84b5380b40f820236bfb6/source.jpg
         * sort : 0
         * thumbnails : images/8f4f8bdbaef84b5380b40f820236bfb6/source.jpg
         */

        private List<ServicephotosEntity> servicephotos;
        /**
         * id : 330
         * photo : http://192.168.3.99:8888/stores/6/f3970f58-4e62-453a-b84c-bb08accbd291.jpg
         * sort : 0
         */

        private List<StorephotosEntity> storephotos;

        public void setService(ServiceEntity service) {
            this.service = service;
        }

        public void setStore(StoreEntity store) {
            this.store = store;
        }

        public void setServicemenu(List<ServicemenuEntity> servicemenu) {
            this.servicemenu = servicemenu;
        }

        public void setServicephotos(List<ServicephotosEntity> servicephotos) {
            this.servicephotos = servicephotos;
        }

        public void setStorephotos(List<StorephotosEntity> storephotos) {
            this.storephotos = storephotos;
        }

        public ServiceEntity getService() {
            return service;
        }

        public StoreEntity getStore() {
            return store;
        }

        public List<ServicemenuEntity> getServicemenu() {
            return servicemenu;
        }

        public List<ServicephotosEntity> getServicephotos() {
            return servicephotos;
        }

        public List<StorephotosEntity> getStorephotos() {
            return storephotos;
        }

        public static class ServiceEntity {
            private String content;
            private String dinner;
            private int id;
            private String logo;
            private String lunch;
            private String name;
            private double price;
            private int sort;
            private int status;
            private int storeId;
            private double storeLatitude;
            private double storeLongitude;
            private String timeArray;

            public void setContent(String content) {
                this.content = content;
            }

            public void setDinner(String dinner) {
                this.dinner = dinner;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public void setLunch(String lunch) {
                this.lunch = lunch;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public void setStoreId(int storeId) {
                this.storeId = storeId;
            }

            public void setStoreLatitude(double storeLatitude) {
                this.storeLatitude = storeLatitude;
            }

            public void setStoreLongitude(double storeLongitude) {
                this.storeLongitude = storeLongitude;
            }

            public void setTimeArray(String timeArray) {
                this.timeArray = timeArray;
            }

            public String getContent() {
                return content;
            }

            public String getDinner() {
                return dinner;
            }

            public int getId() {
                return id;
            }

            public String getLogo() {
                return logo;
            }

            public String getLunch() {
                return lunch;
            }

            public String getName() {
                return name;
            }

            public double getPrice() {
                return price;
            }

            public int getSort() {
                return sort;
            }

            public int getStatus() {
                return status;
            }

            public int getStoreId() {
                return storeId;
            }

            public double getStoreLatitude() {
                return storeLatitude;
            }

            public double getStoreLongitude() {
                return storeLongitude;
            }

            public String getTimeArray() {
                return timeArray;
            }
        }

        public static class StoreEntity {
            private String addr;
            private String addrDetail;
            private String city;
            private String contact;
            private int id;
            private double latitude;
            private String logo;
            private double longitude;
            private String name;
            private int status;

            public void setAddr(String addr) {
                this.addr = addr;
            }

            public void setAddrDetail(String addrDetail) {
                this.addrDetail = addrDetail;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public void setContact(String contact) {
                this.contact = contact;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getAddr() {
                return addr;
            }

            public String getAddrDetail() {
                return addrDetail;
            }

            public String getCity() {
                return city;
            }

            public String getContact() {
                return contact;
            }

            public int getId() {
                return id;
            }

            public double getLatitude() {
                return latitude;
            }

            public String getLogo() {
                return logo;
            }

            public double getLongitude() {
                return longitude;
            }

            public String getName() {
                return name;
            }

            public int getStatus() {
                return status;
            }
        }

        public static class ServicemenuEntity {
            private String engname;
            private String name;

            public void setEngname(String engname) {
                this.engname = engname;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getEngname() {
                return engname;
            }

            public String getName() {
                return name;
            }
        }

        public static class ServicephotosEntity {
            private String photo;
            private int sort;
            private String thumbnails;

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public void setThumbnails(String thumbnails) {
                this.thumbnails = thumbnails;
            }

            public String getPhoto() {
                return photo;
            }

            public int getSort() {
                return sort;
            }

            public String getThumbnails() {
                return thumbnails;
            }
        }

        public static class StorephotosEntity {
            private int id;
            private String photo;
            private int sort;

            public void setId(int id) {
                this.id = id;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getId() {
                return id;
            }

            public String getPhoto() {
                return photo;
            }

            public int getSort() {
                return sort;
            }
        }
    }
}
