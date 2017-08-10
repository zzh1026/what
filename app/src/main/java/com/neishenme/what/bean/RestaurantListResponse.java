package com.neishenme.what.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public class RestaurantListResponse extends RBResponse {

    /**
     * code : 1
     * data : {"count":20,"hasMore":true,"service":[{"distance":1.3331068279434213E7,"services_hotStatus":1,"services_id":31,"services_isNew":"","services_logo":"http://192.168.3.99:8888/images/a0a467ee735549eaa0ae9def5ed1a56b/source.jpg","services_mark":"","services_name":"品味生活","services_price":2,"services_status":0,"store_addrDetail":"东长安街","store_id":2,"store_name":"北京东方君悦大酒店悦庭"},{"distance":1.333956114504972E7,"services_hotStatus":1,"services_id":55,"services_isNew":"","services_logo":"http://192.168.3.99:8888/images/6462864a2c4c48a994e388cb6b033d2d/source.jpg","services_name":"First meeting","services_price":2400,"services_status":0,"store_addrDetail":"朝阳区东三环北路7号","store_id":26,"store_name":"北京威斯汀The Grange扒房"}]}
     * message : success
     */

    private int code;
    /**
     * count : 20
     * hasMore : true
     * service : [{"distance":1.3331068279434213E7,"services_hotStatus":1,"services_id":31,"services_isNew":"","services_logo":"http://192.168.3.99:8888/images/a0a467ee735549eaa0ae9def5ed1a56b/source.jpg","services_mark":"","services_name":"品味生活","services_price":2,"services_status":0,"store_addrDetail":"东长安街","store_id":2,"store_name":"北京东方君悦大酒店悦庭"},{"distance":1.333956114504972E7,"services_hotStatus":1,"services_id":55,"services_isNew":"","services_logo":"http://192.168.3.99:8888/images/6462864a2c4c48a994e388cb6b033d2d/source.jpg","services_name":"First meeting","services_price":2400,"services_status":0,"store_addrDetail":"朝阳区东三环北路7号","store_id":26,"store_name":"北京威斯汀The Grange扒房"}]
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
        private int count;
        private boolean hasMore;
        /**
         * distance : 1.3331068279434213E7
         * services_hotStatus : 1
         * services_id : 31
         * services_isNew :
         * services_logo : http://192.168.3.99:8888/images/a0a467ee735549eaa0ae9def5ed1a56b/source.jpg
         * services_mark :
         * services_name : 品味生活
         * services_price : 2.0
         * services_status : 0
         * store_addrDetail : 东长安街
         * store_id : 2
         * store_name : 北京东方君悦大酒店悦庭
         */

        private List<ServiceEntity> service;

        public void setCount(int count) {
            this.count = count;
        }

        public void setHasMore(boolean hasMore) {
            this.hasMore = hasMore;
        }

        public void setService(List<ServiceEntity> service) {
            this.service = service;
        }

        public int getCount() {
            return count;
        }

        public boolean isHasMore() {
            return hasMore;
        }

        public List<ServiceEntity> getService() {
            return service;
        }

        public static class ServiceEntity {
            private String distance;
            private int services_hotStatus;
            private int services_id;
            private String services_isNew;
            private String services_logo;
            private String services_mark;
            private String services_name;
            private double services_price;
            private int services_status;
            private String store_addrDetail;
            private int store_id;
            private String store_name;

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public void setServices_hotStatus(int services_hotStatus) {
                this.services_hotStatus = services_hotStatus;
            }

            public void setServices_id(int services_id) {
                this.services_id = services_id;
            }

            public void setServices_isNew(String services_isNew) {
                this.services_isNew = services_isNew;
            }

            public void setServices_logo(String services_logo) {
                this.services_logo = services_logo;
            }

            public void setServices_mark(String services_mark) {
                this.services_mark = services_mark;
            }

            public void setServices_name(String services_name) {
                this.services_name = services_name;
            }

            public void setServices_price(double services_price) {
                this.services_price = services_price;
            }

            public void setServices_status(int services_status) {
                this.services_status = services_status;
            }

            public void setStore_addrDetail(String store_addrDetail) {
                this.store_addrDetail = store_addrDetail;
            }

            public void setStore_id(int store_id) {
                this.store_id = store_id;
            }

            public void setStore_name(String store_name) {
                this.store_name = store_name;
            }

            public String getDistance() {
                return distance;
            }

            public int getServices_hotStatus() {
                return services_hotStatus;
            }

            public int getServices_id() {
                return services_id;
            }

            public String getServices_isNew() {
                return services_isNew;
            }

            public String getServices_logo() {
                return services_logo;
            }

            public String getServices_mark() {
                return services_mark;
            }

            public String getServices_name() {
                return services_name;
            }

            public double getServices_price() {
                return services_price;
            }

            public int getServices_status() {
                return services_status;
            }

            public String getStore_addrDetail() {
                return store_addrDetail;
            }

            public int getStore_id() {
                return store_id;
            }

            public String getStore_name() {
                return store_name;
            }
        }
    }
}

