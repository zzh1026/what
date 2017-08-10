package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/4/5.
 */

public class ActiveIphone7Response extends RBResponse {

    /**
     * code : 1
     * message : success
     * data : {"lists":[{"userName":"**","userLogo":"http://192.168.3.99:8888/images/c9a4ee5a874e4bc0a128cbeb99f6d34a/source.jpg","prizeName":"iPhone7 Red 128G","flag":"iPhone7"},{"userName":"困。  了。","userLogo":"http://192.168.3.99:8888/users/2016010/31/2289/logo/hvhqy9wxerajcg8xg0ukpxh0/source.jpg","prizeName":"iPhone7 Plus Red 128G","flag":"iPhone7Plus"},{"userName":"都","userLogo":"http://192.168.3.99:8888/users/2016010/31/2290/logo/xob68zzlot3wegicbjt5b8th/source.jpg","prizeName":"iPhone7 Red 128G","flag":"iPhone7"},{"userName":"我的呃呃呃呃呃呃","userLogo":"http://192.168.3.99:8888/users/201611/14/2292/logo/rqwzkazs4ryb86l7wpvlzu03/source.jpg","prizeName":"iPhone7 Red 128G","flag":"iPhone7"},{"userName":"熊二","userLogo":"http://192.168.3.99:8888/images/06f21a5a896e4e97b7b5fe7844f46126/source.jpg","prizeName":"iPhone7 Plus Red 128G","flag":"iPhone7Plus"},{"userName":"一二三四五六七八","userLogo":"http://192.168.3.99:8888/images/9f6d6963ff3c44fd8def0d5d8044dfe1/source.jpg","prizeName":"iPhone7 Red 128G","flag":"iPhone7"},{"userName":"七仔","userLogo":"http://192.168.3.99:8888/images/4d0e7241ea3e47aa8b75dab6b82d514d/source.jpg","prizeName":"iPhone7 Red 128G","flag":"iPhone7"},{"userName":"口口口","userLogo":"http://192.168.3.99:8888/users/50/34995995-fc14-4c81-b4db-8f1b87bae294.jpg","prizeName":"iPhone7 Plus Red 128G","flag":"iPhone7Plus"},{"userName":"坶嚕","userLogo":"http://192.168.3.99:8888/images/86a0d7b444204b51ad15a8ff9bbed7b9/source.jpg","prizeName":"iPhone7 Plus Red 128G","flag":"iPhone7Plus"}]}
     */

    private int code;
    private String message;
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
         * userName : **
         * userLogo : http://192.168.3.99:8888/images/c9a4ee5a874e4bc0a128cbeb99f6d34a/source.jpg
         * prizeName : iPhone7 Red 128G
         * flag : iPhone7
         */

        private List<ListsBean> lists;

        public List<ListsBean> getLists() {
            return lists;
        }

        public void setLists(List<ListsBean> lists) {
            this.lists = lists;
        }

        public static class ListsBean {
            private String userName;
            private String userLogo;
            private String prizeName;
            private String flag;

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getUserLogo() {
                return userLogo;
            }

            public void setUserLogo(String userLogo) {
                this.userLogo = userLogo;
            }

            public String getPrizeName() {
                return prizeName;
            }

            public void setPrizeName(String prizeName) {
                this.prizeName = prizeName;
            }

            public String getFlag() {
                return flag;
            }

            public void setFlag(String flag) {
                this.flag = flag;
            }
        }
    }
}
