package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是 单个明星投票 信息
 * <p>
 * Created by zhaozh on 2017/4/19.
 */

public class StarVoteDetailResponse extends RBResponse {


    /**
     * code : 1
     * message : success
     * data : {"activitySuperStarDO":{"logo":"http://192.168.3.99:8888/nsm/activity/ghh9pvj2jtp3be79dy3tdhta/source.png","ticket":1010,"backgroudImg":"http://192.168.3.99:8888/nsm/activity/4dfjebvn3u684nrqm4fpzulj/source.png","name":"李颖芝"},"userTicketRank":{"rowNo":1,"name":"熊二","userlogo":"http://192.168.3.99:8888/images/06f21a5a896e4e97b7b5fe7844f46126/source.jpg","userTickets":230},"listStars":[{"userId":2295,"name":"熊二","userlogo":"http://192.168.3.99:8888/images/06f21a5a896e4e97b7b5fe7844f46126/source.jpg","userTickets":230},{"userId":6551,"name":"纵时光毫不留情","userlogo":"http://192.168.3.99:8888/users/201702/07/6551/logo/4wu23jowosq26pbgbjpqbgei/source.jpg","userTickets":63},{"userId":91,"name":"不挽离人","userlogo":"http://192.168.3.99:8888/users/91/86e71d00-81ef-4628-8f3d-506f2a4166dd.jpg","userTickets":17}]}
     */

    private int code;
    private String message;
    /**
     * activitySuperStarDO : {"logo":"http://192.168.3.99:8888/nsm/activity/ghh9pvj2jtp3be79dy3tdhta/source.png","ticket":1010,"backgroudImg":"http://192.168.3.99:8888/nsm/activity/4dfjebvn3u684nrqm4fpzulj/source.png","name":"李颖芝"}
     * userTicketRank : {"rowNo":1,"name":"熊二","userlogo":"http://192.168.3.99:8888/images/06f21a5a896e4e97b7b5fe7844f46126/source.jpg","userTickets":230}
     * listStars : [{"userId":2295,"name":"熊二","userlogo":"http://192.168.3.99:8888/images/06f21a5a896e4e97b7b5fe7844f46126/source.jpg","userTickets":230},{"userId":6551,"name":"纵时光毫不留情","userlogo":"http://192.168.3.99:8888/users/201702/07/6551/logo/4wu23jowosq26pbgbjpqbgei/source.jpg","userTickets":63},{"userId":91,"name":"不挽离人","userlogo":"http://192.168.3.99:8888/users/91/86e71d00-81ef-4628-8f3d-506f2a4166dd.jpg","userTickets":17}]
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
         * logo : http://192.168.3.99:8888/nsm/activity/ghh9pvj2jtp3be79dy3tdhta/source.png
         * ticket : 1010
         * backgroudImg : http://192.168.3.99:8888/nsm/activity/4dfjebvn3u684nrqm4fpzulj/source.png
         * name : 李颖芝
         */

        private ActivitySuperStarDOBean activitySuperStarDO;
        /**
         * rowNo : 1
         * name : 熊二
         * userlogo : http://192.168.3.99:8888/images/06f21a5a896e4e97b7b5fe7844f46126/source.jpg
         * userTickets : 230
         */

        private UserTicketRankBean userTicketRank;
        /**
         * userId : 2295
         * name : 熊二
         * userlogo : http://192.168.3.99:8888/images/06f21a5a896e4e97b7b5fe7844f46126/source.jpg
         * userTickets : 230
         */

        private List<ListStarsBean> listStars;

        public ActivitySuperStarDOBean getActivitySuperStarDO() {
            return activitySuperStarDO;
        }

        public void setActivitySuperStarDO(ActivitySuperStarDOBean activitySuperStarDO) {
            this.activitySuperStarDO = activitySuperStarDO;
        }

        public UserTicketRankBean getUserTicketRank() {
            return userTicketRank;
        }

        public void setUserTicketRank(UserTicketRankBean userTicketRank) {
            this.userTicketRank = userTicketRank;
        }

        public List<ListStarsBean> getListStars() {
            return listStars;
        }

        public void setListStars(List<ListStarsBean> listStars) {
            this.listStars = listStars;
        }

        public static class ActivitySuperStarDOBean {
            private String logo;
            private int ticket;
            private String backgroudImg;
            private String name;

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public int getTicket() {
                return ticket;
            }

            public void setTicket(int ticket) {
                this.ticket = ticket;
            }

            public String getBackgroudImg() {
                return backgroudImg;
            }

            public void setBackgroudImg(String backgroudImg) {
                this.backgroudImg = backgroudImg;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class UserTicketRankBean {
            private int rowNo;
            private String name;
            private String userlogo;
            private int userTickets;

            public int getRowNo() {
                return rowNo;
            }

            public void setRowNo(int rowNo) {
                this.rowNo = rowNo;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUserlogo() {
                return userlogo;
            }

            public void setUserlogo(String userlogo) {
                this.userlogo = userlogo;
            }

            public int getUserTickets() {
                return userTickets;
            }

            public void setUserTickets(int userTickets) {
                this.userTickets = userTickets;
            }
        }

        public static class ListStarsBean {
            private int userId;
            private String name;
            private String userlogo;
            private int userTickets;

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUserlogo() {
                return userlogo;
            }

            public void setUserlogo(String userlogo) {
                this.userlogo = userlogo;
            }

            public int getUserTickets() {
                return userTickets;
            }

            public void setUserTickets(int userTickets) {
                this.userTickets = userTickets;
            }
        }
    }
}
