package com.neishenme.what.bean;

import java.util.List;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/4/20.
 */

public class GetVoteRankResponse extends RBResponse {


    /**
     * code : 1
     * message : success
     * data : {"relatedInfo":{"userId":2289,"userlogo":"http://192.168.3.99:8888/users/2016010/31/2289/logo/hvhqy9wxerajcg8xg0ukpxh0/source.jpg","superStarSharebgImg":"http://192.168.3.99:8888/nsm/activity/7v5pr7n03svn6xlyapunpumq/source.png","rank":0,"percentage":1},"activityShareDO":{"shareLink":"http://192.168.3.200/nsmapi/h5/superStar/shareData?activitySuperStarRankDetailId=1","shareTitle":"网约一大波女明星","activitySuperStarRankDetailId":53,"shareDescribe":"牛X了！一大波女明星来袭，戳我约她。","shareImage":"http://192.168.3.99:8888/nsm/activity/ghh9pvj2jtp3be79dy3tdhta/source.png"},"prize":{"prizeName":"活动余额10元","activityLotteryDetailId":632},"listOverUserLogo":[{"userlogo":"http://192.168.3.99:8888/users/91/86e71d00-81ef-4628-8f3d-506f2a4166dd.jpg","userId":"91"},{"userlogo":"http://192.168.3.99:8888/users/2016010/31/2289/logo/hvhqy9wxerajcg8xg0ukpxh0/source.jpg","userId":"2289"},{"userlogo":"http://192.168.3.99:8888/images/06f21a5a896e4e97b7b5fe7844f46126/source.jpg","userId":"2295"},{"userlogo":"http://192.168.3.99:8888/users/2016010/31/2290/logo/xob68zzlot3wegicbjt5b8th/source1.jpg","userId":"2290"},{"userlogo":"http://192.168.3.99:8888/users/201702/07/6551/logo/4wu23jowosq26pbgbjpqbgei/source.jpg","userId":"6551"},{"userlogo":"http://192.168.3.99:8888/users/201703/22/6588/logo/bl9d1gfnauclepdp6t0hdhcv/source.jpg","userId":"6588"}]}
     */

    private int code;
    private String message;
    /**
     * relatedInfo : {"userId":2289,"userlogo":"http://192.168.3.99:8888/users/2016010/31/2289/logo/hvhqy9wxerajcg8xg0ukpxh0/source.jpg","superStarSharebgImg":"http://192.168.3.99:8888/nsm/activity/7v5pr7n03svn6xlyapunpumq/source.png","rank":0,"percentage":1}
     * activityShareDO : {"shareLink":"http://192.168.3.200/nsmapi/h5/superStar/shareData?activitySuperStarRankDetailId=1","shareTitle":"网约一大波女明星","activitySuperStarRankDetailId":53,"shareDescribe":"牛X了！一大波女明星来袭，戳我约她。","shareImage":"http://192.168.3.99:8888/nsm/activity/ghh9pvj2jtp3be79dy3tdhta/source.png"}
     * prize : {"prizeName":"活动余额10元","activityLotteryDetailId":632}
     * listOverUserLogo : [{"userlogo":"http://192.168.3.99:8888/users/91/86e71d00-81ef-4628-8f3d-506f2a4166dd.jpg","userId":"91"},{"userlogo":"http://192.168.3.99:8888/users/2016010/31/2289/logo/hvhqy9wxerajcg8xg0ukpxh0/source.jpg","userId":"2289"},{"userlogo":"http://192.168.3.99:8888/images/06f21a5a896e4e97b7b5fe7844f46126/source.jpg","userId":"2295"},{"userlogo":"http://192.168.3.99:8888/users/2016010/31/2290/logo/xob68zzlot3wegicbjt5b8th/source1.jpg","userId":"2290"},{"userlogo":"http://192.168.3.99:8888/users/201702/07/6551/logo/4wu23jowosq26pbgbjpqbgei/source.jpg","userId":"6551"},{"userlogo":"http://192.168.3.99:8888/users/201703/22/6588/logo/bl9d1gfnauclepdp6t0hdhcv/source.jpg","userId":"6588"}]
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
         * userId : 2289
         * userlogo : http://192.168.3.99:8888/users/2016010/31/2289/logo/hvhqy9wxerajcg8xg0ukpxh0/source.jpg
         * superStarSharebgImg : http://192.168.3.99:8888/nsm/activity/7v5pr7n03svn6xlyapunpumq/source.png
         * rank : 0
         * percentage : 1
         */

        private RelatedInfoBean relatedInfo;
        /**
         * shareLink : http://192.168.3.200/nsmapi/h5/superStar/shareData?activitySuperStarRankDetailId=1
         * shareTitle : 网约一大波女明星
         * activitySuperStarRankDetailId : 53
         * shareDescribe : 牛X了！一大波女明星来袭，戳我约她。
         * shareImage : http://192.168.3.99:8888/nsm/activity/ghh9pvj2jtp3be79dy3tdhta/source.png
         */

        private ActivityShareDOBean activityShareDO;
        /**
         * prizeName : 活动余额10元
         * activityLotteryDetailId : 632
         */

        private PrizeBean prize;
        /**
         * userlogo : http://192.168.3.99:8888/users/91/86e71d00-81ef-4628-8f3d-506f2a4166dd.jpg
         * userId : 91
         */

        private List<ListOverUserLogoBean> listOverUserLogo;

        public RelatedInfoBean getRelatedInfo() {
            return relatedInfo;
        }

        public void setRelatedInfo(RelatedInfoBean relatedInfo) {
            this.relatedInfo = relatedInfo;
        }

        public ActivityShareDOBean getActivityShareDO() {
            return activityShareDO;
        }

        public void setActivityShareDO(ActivityShareDOBean activityShareDO) {
            this.activityShareDO = activityShareDO;
        }

        public PrizeBean getPrize() {
            return prize;
        }

        public void setPrize(PrizeBean prize) {
            this.prize = prize;
        }

        public List<ListOverUserLogoBean> getListOverUserLogo() {
            return listOverUserLogo;
        }

        public void setListOverUserLogo(List<ListOverUserLogoBean> listOverUserLogo) {
            this.listOverUserLogo = listOverUserLogo;
        }

        public static class RelatedInfoBean {
            private int userId;
            private String userlogo;
            private String superStarSharebgImg;
            private int rank;
            private double percentage;

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getUserlogo() {
                return userlogo;
            }

            public void setUserlogo(String userlogo) {
                this.userlogo = userlogo;
            }

            public String getSuperStarSharebgImg() {
                return superStarSharebgImg;
            }

            public void setSuperStarSharebgImg(String superStarSharebgImg) {
                this.superStarSharebgImg = superStarSharebgImg;
            }

            public int getRank() {
                return rank;
            }

            public void setRank(int rank) {
                this.rank = rank;
            }

            public double getPercentage() {
                return percentage;
            }

            public void setPercentage(double percentage) {
                this.percentage = percentage;
            }
        }

        public static class ActivityShareDOBean {
            private String shareLink;
            private String shareTitle;
            private int activitySuperStarRankDetailId;
            private String shareDescribe;
            private String shareImage;

            public String getShareLink() {
                return shareLink;
            }

            public void setShareLink(String shareLink) {
                this.shareLink = shareLink;
            }

            public String getShareTitle() {
                return shareTitle;
            }

            public void setShareTitle(String shareTitle) {
                this.shareTitle = shareTitle;
            }

            public int getActivitySuperStarRankDetailId() {
                return activitySuperStarRankDetailId;
            }

            public void setActivitySuperStarRankDetailId(int activitySuperStarRankDetailId) {
                this.activitySuperStarRankDetailId = activitySuperStarRankDetailId;
            }

            public String getShareDescribe() {
                return shareDescribe;
            }

            public void setShareDescribe(String shareDescribe) {
                this.shareDescribe = shareDescribe;
            }

            public String getShareImage() {
                return shareImage;
            }

            public void setShareImage(String shareImage) {
                this.shareImage = shareImage;
            }
        }

        public static class PrizeBean {
            private String prizeName;
            private int activityLotteryDetailId;

            public String getPrizeName() {
                return prizeName;
            }

            public void setPrizeName(String prizeName) {
                this.prizeName = prizeName;
            }

            public int getActivityLotteryDetailId() {
                return activityLotteryDetailId;
            }

            public void setActivityLotteryDetailId(int activityLotteryDetailId) {
                this.activityLotteryDetailId = activityLotteryDetailId;
            }
        }

        public static class ListOverUserLogoBean {
            private String userlogo;
            private String userId;

            public String getUserlogo() {
                return userlogo;
            }

            public void setUserlogo(String userlogo) {
                this.userlogo = userlogo;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }
    }
}
