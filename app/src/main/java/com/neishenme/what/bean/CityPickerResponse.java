package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/12/12.
 */

public class CityPickerResponse extends RBResponse {

    /**
     * code : 1
     * message : success
     */

    private int code;
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
         * parent : A
         */

        private List<AllCityListBean> allCityList;
        /**
         * fulls : beijingshi
         * hotflag : 1
         * id : 35
         * latitude : 39.92
         * longitude : 116.46
         * name : 北京市
         */

        private List<HotCitylistBean> hotCitylist;

        public List<AllCityListBean> getAllCityList() {
            return allCityList;
        }

        public void setAllCityList(List<AllCityListBean> allCityList) {
            this.allCityList = allCityList;
        }

        public List<HotCitylistBean> getHotCitylist() {
            return hotCitylist;
        }

        public void setHotCitylist(List<HotCitylistBean> hotCitylist) {
            this.hotCitylist = hotCitylist;
        }

        public static class AllCityListBean {
            private String parent;
            /**
             * fulls : aba
             * hotflag : 0
             * id : 190
             * latitude : 31.93
             * longitude : 101.72
             * name : 阿坝
             */

            private List<ChildsBean> childs;

            public String getParent() {
                return parent;
            }

            public void setParent(String parent) {
                this.parent = parent;
            }

            public List<ChildsBean> getChilds() {
                return childs;
            }

            public void setChilds(List<ChildsBean> childs) {
                this.childs = childs;
            }

            public static class ChildsBean {
                private String fulls;
                private int hotflag;
                private int id;
                private double latitude;
                private double longitude;
                private String name;

                public String getFulls() {
                    return fulls;
                }

                public void setFulls(String fulls) {
                    this.fulls = fulls;
                }

                public int getHotflag() {
                    return hotflag;
                }

                public void setHotflag(int hotflag) {
                    this.hotflag = hotflag;
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

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }

        public static class HotCitylistBean {
            private String fulls;
            private int hotflag;
            private int id;
            private double latitude;
            private double longitude;
            private String name;

            public String getFulls() {
                return fulls;
            }

            public void setFulls(String fulls) {
                this.fulls = fulls;
            }

            public int getHotflag() {
                return hotflag;
            }

            public void setHotflag(int hotflag) {
                this.hotflag = hotflag;
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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
