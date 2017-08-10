package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/11/7.
 */

public class GetServiceListResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"service":[{"store_id":5,"services_id":35,"services_mark":"","store_addrDetail":"三里屯","distance":1.3338470837324765E7,"services_logo":"http://192.168.3.99:8888/images/26fd1e32964148e7a1854c1db48b94ee/source.jpg","services_status":0,"services_price":2000,"services_name":"Sureno","services_isNew":null,"store_name":"精致菜式尽显法式浪漫","services_hotStatus":1},{"store_id":9,"services_id":38,"services_mark":"","store_addrDetail":"百子湾","distance":1.3339623206528809E7,"services_logo":"http://192.168.3.99:8888/images/4bcc6c28e3ab442cbea227a84ed84f3b/source.jpg","services_status":0,"services_price":2400,"services_name":"清幽闲情","services_isNew":null,"store_name":"无限美味唤醒味蕾双人餐","services_hotStatus":1},{"store_id":2,"services_id":31,"services_mark":"餐厅推荐","store_addrDetail":"东长安街","distance":1.3331068279434213E7,"services_logo":"http://192.168.3.99:8888/images/a0a467ee735549eaa0ae9def5ed1a56b/source.jpg","services_status":0,"services_price":2,"services_name":"品味生活","services_isNew":1,"store_name":"正宗西班牙料理双人餐","services_hotStatus":0},{"store_id":17,"services_id":46,"services_mark":"","store_addrDetail":"朝阳区东三环中路7号","distance":1.3338668276496189E7,"services_logo":"http://192.168.3.99:8888/images/f4a68eccecfa4a2c9854f00700a04f72/source.jpg","services_status":0,"services_price":3000,"services_name":"甜蜜王国的初遇","services_isNew":null,"store_name":"传统中式料理双人餐","services_hotStatus":0},{"store_id":13,"services_id":42,"services_mark":"","store_addrDetail":"东城区王府井东街8号","distance":1.3331725086575486E7,"services_logo":"http://192.168.3.99:8888/images/006c4d8e0c3448d6a9ee8bdd36796985/source.jpg","services_status":0,"services_price":2000,"services_name":"斗牛士的风情","services_isNew":null,"store_name":"大厨的创意时刻","services_hotStatus":0},{"store_id":21,"services_id":50,"services_mark":"","store_addrDetail":"朝阳区红街大厦1号","distance":1.3337789445715683E7,"services_logo":"http://192.168.3.99:8888/images/2884869fe1b54aab97f3b23abb2e821e/source.jpg","services_status":0,"services_price":2059,"services_name":"丰盛蟹宴","services_isNew":null,"store_name":"看海 慢思 慢生活","services_hotStatus":0},{"store_id":19,"services_id":48,"services_mark":"","store_addrDetail":"西城区西直门外大街18号","distance":1.3323571639457176E7,"services_logo":"http://192.168.3.99:8888/images/be5d2dfbff1548ef9d33582d26ed1700/source.jpg","services_status":0,"services_price":1800,"services_name":"帝王奢华","services_isNew":null,"store_name":"低温慢煮的创意生活","services_hotStatus":0},{"store_id":11,"services_id":40,"services_mark":"","store_addrDetail":"东城区东长安街33号","distance":1.3331337938136306E7,"services_logo":"http://192.168.3.99:8888/images/4041dd288867435294cd394bd142e5b8/source.jpg","services_status":0,"services_price":2200,"services_name":"古香名流","services_isNew":null,"store_name":"新潮与摩登的结合","services_hotStatus":0},{"store_id":8,"services_id":36,"services_mark":"","store_addrDetail":"亮马桥","distance":1.3339989182376664E7,"services_logo":"http://192.168.3.99:8888/images/a213de64b0b24230b31db843b9ba2b19/source.jpg","services_status":0,"services_price":1800,"services_name":"罗马假日","services_isNew":null,"store_name":"私人定制轻奢双人餐","services_hotStatus":0},{"store_id":6,"services_id":34,"services_mark":"","store_addrDetail":"沙滩北街","distance":1.3330903515798816E7,"services_logo":"http://192.168.3.99:8888/images/8f4f8bdbaef84b5380b40f820236bfb6/source.jpg","services_status":0,"services_price":2000,"services_name":"欧式风情","services_isNew":null,"store_name":"独具匠心的艺术美食","services_hotStatus":0},{"store_id":7,"services_id":37,"services_mark":"","store_addrDetail":"工体","distance":1.3337394941686563E7,"services_logo":"http://192.168.3.99:8888/images/b67d6208d9884c28841435768423e02d/source.jpg","services_status":0,"services_price":2200,"services_name":"蚝情","services_isNew":null,"store_name":"高颜值海鲜大餐","services_hotStatus":0},{"store_id":10,"services_id":39,"services_mark":"","store_addrDetail":"朝阳区新源南路2号","distance":1.333938982490289E7,"services_logo":"http://192.168.3.99:8888/images/b98265f7222a4b7c9d2519a1cf57444e/source.jpg","services_status":0,"services_price":3450,"services_name":"顶峰奢享","services_isNew":null,"store_name":"日式美味全天可享","services_hotStatus":0},{"store_id":14,"services_id":43,"services_mark":"","store_addrDetail":"西城区南长街38号","distance":1.3328903980734088E7,"services_logo":"http://192.168.3.99:8888/images/2a2954b8cb92440aa67b9c9847298c74/source.jpg","services_status":0,"services_price":4700,"services_name":"御厨国宴","services_isNew":null,"store_name":"超有范儿双人餐","services_hotStatus":0},{"store_id":1,"services_id":29,"services_mark":"新品特惠","store_addrDetail":"翠微路","distance":1.3331068279434213E7,"services_logo":"http://192.168.3.99:8888/images/bfad8e84c9fc467da093f0818eb20c6a/source.jpg","services_status":0,"services_price":2,"services_name":"生与熟转换","services_isNew":1,"store_name":"N种美味吃法全天双人餐","services_hotStatus":0},{"store_id":24,"services_id":53,"services_mark":"","store_addrDetail":"东城区金鱼胡同5-15号","distance":1.3332178770159433E7,"services_logo":"http://192.168.3.99:8888/images/c84826c2b4334d79b6a34c1f4a651309/source.jpg","services_status":0,"services_price":1800,"services_name":"European style","services_isNew":null,"store_name":"从料理细节中品味高端","services_hotStatus":0},{"store_id":25,"services_id":54,"services_mark":"","store_addrDetail":"北京市东城区金宝街99号","distance":1.3332852956104742E7,"services_logo":"http://192.168.3.99:8888/images/9dc5c69dac484a68a5c0030c4cd9e80a/source.jpg","services_status":0,"services_price":1400,"services_name":"Time difference","services_isNew":null,"store_name":"西餐融合创意好滋味","services_hotStatus":0},{"store_id":4,"services_id":56,"services_mark":"","store_addrDetail":"建国门","distance":1.3334087906129949E7,"services_logo":"http://192.168.3.99:8888/images/794fab389e094e4b8f36eb71785693c2/source.jpg","services_status":0,"services_price":4600,"services_name":"与浪漫旋转","services_isNew":null,"store_name":"有内涵的美味料理体验","services_hotStatus":0},{"store_id":23,"services_id":52,"services_mark":"","store_addrDetail":"朝阳区工人体育场西路","distance":1.333687132134037E7,"services_logo":"http://192.168.3.99:8888/images/aebfb11102da43b19f43fa637454f315/source.jpg","services_status":0,"services_price":2000,"services_name":"静享食光","services_isNew":null,"store_name":"超洋范儿美食体验","services_hotStatus":0},{"store_id":22,"services_id":51,"services_mark":"","store_addrDetail":"朝阳区永安东里8号","distance":1.3337393953873193E7,"services_logo":"http://192.168.3.99:8888/service/51/ec9f36f7-6877-4ce0-991e-d47fa8078456.jpg","services_status":0,"services_price":2255,"services_name":"刀光倩影","services_isNew":null,"store_name":"创意料理散发的浓情韵味","services_hotStatus":0},{"store_id":26,"services_id":55,"services_mark":"","store_addrDetail":"朝阳区东三环北路7号","distance":1.333956114504972E7,"services_logo":"http://192.168.3.99:8888/images/6462864a2c4c48a994e388cb6b033d2d/source.jpg","services_status":0,"services_price":2400,"services_name":"First meeting","services_isNew":null,"store_name":"潮流梦幻味蕾体验","services_hotStatus":0}],"count":20,"hasMore":true}
     */

    private int code;
    private String message;
    /**
     * service : [{"store_id":5,"services_id":35,"services_mark":"","store_addrDetail":"三里屯","distance":1.3338470837324765E7,"services_logo":"http://192.168.3.99:8888/images/26fd1e32964148e7a1854c1db48b94ee/source.jpg","services_status":0,"services_price":2000,"services_name":"Sureno","services_isNew":null,"store_name":"精致菜式尽显法式浪漫","services_hotStatus":1},{"store_id":9,"services_id":38,"services_mark":"","store_addrDetail":"百子湾","distance":1.3339623206528809E7,"services_logo":"http://192.168.3.99:8888/images/4bcc6c28e3ab442cbea227a84ed84f3b/source.jpg","services_status":0,"services_price":2400,"services_name":"清幽闲情","services_isNew":null,"store_name":"无限美味唤醒味蕾双人餐","services_hotStatus":1},{"store_id":2,"services_id":31,"services_mark":"餐厅推荐","store_addrDetail":"东长安街","distance":1.3331068279434213E7,"services_logo":"http://192.168.3.99:8888/images/a0a467ee735549eaa0ae9def5ed1a56b/source.jpg","services_status":0,"services_price":2,"services_name":"品味生活","services_isNew":1,"store_name":"正宗西班牙料理双人餐","services_hotStatus":0},{"store_id":17,"services_id":46,"services_mark":"","store_addrDetail":"朝阳区东三环中路7号","distance":1.3338668276496189E7,"services_logo":"http://192.168.3.99:8888/images/f4a68eccecfa4a2c9854f00700a04f72/source.jpg","services_status":0,"services_price":3000,"services_name":"甜蜜王国的初遇","services_isNew":null,"store_name":"传统中式料理双人餐","services_hotStatus":0},{"store_id":13,"services_id":42,"services_mark":"","store_addrDetail":"东城区王府井东街8号","distance":1.3331725086575486E7,"services_logo":"http://192.168.3.99:8888/images/006c4d8e0c3448d6a9ee8bdd36796985/source.jpg","services_status":0,"services_price":2000,"services_name":"斗牛士的风情","services_isNew":null,"store_name":"大厨的创意时刻","services_hotStatus":0},{"store_id":21,"services_id":50,"services_mark":"","store_addrDetail":"朝阳区红街大厦1号","distance":1.3337789445715683E7,"services_logo":"http://192.168.3.99:8888/images/2884869fe1b54aab97f3b23abb2e821e/source.jpg","services_status":0,"services_price":2059,"services_name":"丰盛蟹宴","services_isNew":null,"store_name":"看海 慢思 慢生活","services_hotStatus":0},{"store_id":19,"services_id":48,"services_mark":"","store_addrDetail":"西城区西直门外大街18号","distance":1.3323571639457176E7,"services_logo":"http://192.168.3.99:8888/images/be5d2dfbff1548ef9d33582d26ed1700/source.jpg","services_status":0,"services_price":1800,"services_name":"帝王奢华","services_isNew":null,"store_name":"低温慢煮的创意生活","services_hotStatus":0},{"store_id":11,"services_id":40,"services_mark":"","store_addrDetail":"东城区东长安街33号","distance":1.3331337938136306E7,"services_logo":"http://192.168.3.99:8888/images/4041dd288867435294cd394bd142e5b8/source.jpg","services_status":0,"services_price":2200,"services_name":"古香名流","services_isNew":null,"store_name":"新潮与摩登的结合","services_hotStatus":0},{"store_id":8,"services_id":36,"services_mark":"","store_addrDetail":"亮马桥","distance":1.3339989182376664E7,"services_logo":"http://192.168.3.99:8888/images/a213de64b0b24230b31db843b9ba2b19/source.jpg","services_status":0,"services_price":1800,"services_name":"罗马假日","services_isNew":null,"store_name":"私人定制轻奢双人餐","services_hotStatus":0},{"store_id":6,"services_id":34,"services_mark":"","store_addrDetail":"沙滩北街","distance":1.3330903515798816E7,"services_logo":"http://192.168.3.99:8888/images/8f4f8bdbaef84b5380b40f820236bfb6/source.jpg","services_status":0,"services_price":2000,"services_name":"欧式风情","services_isNew":null,"store_name":"独具匠心的艺术美食","services_hotStatus":0},{"store_id":7,"services_id":37,"services_mark":"","store_addrDetail":"工体","distance":1.3337394941686563E7,"services_logo":"http://192.168.3.99:8888/images/b67d6208d9884c28841435768423e02d/source.jpg","services_status":0,"services_price":2200,"services_name":"蚝情","services_isNew":null,"store_name":"高颜值海鲜大餐","services_hotStatus":0},{"store_id":10,"services_id":39,"services_mark":"","store_addrDetail":"朝阳区新源南路2号","distance":1.333938982490289E7,"services_logo":"http://192.168.3.99:8888/images/b98265f7222a4b7c9d2519a1cf57444e/source.jpg","services_status":0,"services_price":3450,"services_name":"顶峰奢享","services_isNew":null,"store_name":"日式美味全天可享","services_hotStatus":0},{"store_id":14,"services_id":43,"services_mark":"","store_addrDetail":"西城区南长街38号","distance":1.3328903980734088E7,"services_logo":"http://192.168.3.99:8888/images/2a2954b8cb92440aa67b9c9847298c74/source.jpg","services_status":0,"services_price":4700,"services_name":"御厨国宴","services_isNew":null,"store_name":"超有范儿双人餐","services_hotStatus":0},{"store_id":1,"services_id":29,"services_mark":"新品特惠","store_addrDetail":"翠微路","distance":1.3331068279434213E7,"services_logo":"http://192.168.3.99:8888/images/bfad8e84c9fc467da093f0818eb20c6a/source.jpg","services_status":0,"services_price":2,"services_name":"生与熟转换","services_isNew":1,"store_name":"N种美味吃法全天双人餐","services_hotStatus":0},{"store_id":24,"services_id":53,"services_mark":"","store_addrDetail":"东城区金鱼胡同5-15号","distance":1.3332178770159433E7,"services_logo":"http://192.168.3.99:8888/images/c84826c2b4334d79b6a34c1f4a651309/source.jpg","services_status":0,"services_price":1800,"services_name":"European style","services_isNew":null,"store_name":"从料理细节中品味高端","services_hotStatus":0},{"store_id":25,"services_id":54,"services_mark":"","store_addrDetail":"北京市东城区金宝街99号","distance":1.3332852956104742E7,"services_logo":"http://192.168.3.99:8888/images/9dc5c69dac484a68a5c0030c4cd9e80a/source.jpg","services_status":0,"services_price":1400,"services_name":"Time difference","services_isNew":null,"store_name":"西餐融合创意好滋味","services_hotStatus":0},{"store_id":4,"services_id":56,"services_mark":"","store_addrDetail":"建国门","distance":1.3334087906129949E7,"services_logo":"http://192.168.3.99:8888/images/794fab389e094e4b8f36eb71785693c2/source.jpg","services_status":0,"services_price":4600,"services_name":"与浪漫旋转","services_isNew":null,"store_name":"有内涵的美味料理体验","services_hotStatus":0},{"store_id":23,"services_id":52,"services_mark":"","store_addrDetail":"朝阳区工人体育场西路","distance":1.333687132134037E7,"services_logo":"http://192.168.3.99:8888/images/aebfb11102da43b19f43fa637454f315/source.jpg","services_status":0,"services_price":2000,"services_name":"静享食光","services_isNew":null,"store_name":"超洋范儿美食体验","services_hotStatus":0},{"store_id":22,"services_id":51,"services_mark":"","store_addrDetail":"朝阳区永安东里8号","distance":1.3337393953873193E7,"services_logo":"http://192.168.3.99:8888/service/51/ec9f36f7-6877-4ce0-991e-d47fa8078456.jpg","services_status":0,"services_price":2255,"services_name":"刀光倩影","services_isNew":null,"store_name":"创意料理散发的浓情韵味","services_hotStatus":0},{"store_id":26,"services_id":55,"services_mark":"","store_addrDetail":"朝阳区东三环北路7号","distance":1.333956114504972E7,"services_logo":"http://192.168.3.99:8888/images/6462864a2c4c48a994e388cb6b033d2d/source.jpg","services_status":0,"services_price":2400,"services_name":"First meeting","services_isNew":null,"store_name":"潮流梦幻味蕾体验","services_hotStatus":0}]
     * count : 20
     * hasMore : true
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
        private int count;
        private boolean hasMore;

        public boolean isCityOpen() {
            return isCityOpen;
        }

        public void setCityOpen(boolean cityOpen) {
            isCityOpen = cityOpen;
        }

        private boolean isCityOpen;
        /**
         * store_id : 5
         * services_id : 35
         * services_mark :
         * store_addrDetail : 三里屯
         * distance : 1.3338470837324765E7
         * services_logo : http://192.168.3.99:8888/images/26fd1e32964148e7a1854c1db48b94ee/source.jpg
         * services_status : 0
         * services_price : 2000.0
         * services_name : Sureno
         * services_isNew : null
         * store_name : 精致菜式尽显法式浪漫
         * services_hotStatus : 1
         */

        private List<ServiceBean> service;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isHasMore() {
            return hasMore;
        }

        public void setHasMore(boolean hasMore) {
            this.hasMore = hasMore;
        }

        public List<ServiceBean> getService() {
            return service;
        }

        public void setService(List<ServiceBean> service) {
            this.service = service;
        }

        public static class ServiceBean {
            private int store_id;
            private int services_id;
            private String services_mark;
            private String store_addrDetail;
            private double distance;
            private String services_logo;
            private int services_status;
            private double services_price;
            private String services_name;
            private String services_isNew;
            private String store_name;
            private int services_hotStatus;

            public int getStore_id() {
                return store_id;
            }

            public void setStore_id(int store_id) {
                this.store_id = store_id;
            }

            public int getServices_id() {
                return services_id;
            }

            public void setServices_id(int services_id) {
                this.services_id = services_id;
            }

            public String getServices_mark() {
                return services_mark;
            }

            public void setServices_mark(String services_mark) {
                this.services_mark = services_mark;
            }

            public String getStore_addrDetail() {
                return store_addrDetail;
            }

            public void setStore_addrDetail(String store_addrDetail) {
                this.store_addrDetail = store_addrDetail;
            }

            public double getDistance() {
                return distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            public String getServices_logo() {
                return services_logo;
            }

            public void setServices_logo(String services_logo) {
                this.services_logo = services_logo;
            }

            public int getServices_status() {
                return services_status;
            }

            public void setServices_status(int services_status) {
                this.services_status = services_status;
            }

            public double getServices_price() {
                return services_price;
            }

            public void setServices_price(double services_price) {
                this.services_price = services_price;
            }

            public String getServices_name() {
                return services_name;
            }

            public void setServices_name(String services_name) {
                this.services_name = services_name;
            }

            public String getServices_isNew() {
                return services_isNew;
            }

            public void setServices_isNew(String services_isNew) {
                this.services_isNew = services_isNew;
            }

            public String getStore_name() {
                return store_name;
            }

            public void setStore_name(String store_name) {
                this.store_name = store_name;
            }

            public int getServices_hotStatus() {
                return services_hotStatus;
            }

            public void setServices_hotStatus(int services_hotStatus) {
                this.services_hotStatus = services_hotStatus;
            }
        }
    }
}
