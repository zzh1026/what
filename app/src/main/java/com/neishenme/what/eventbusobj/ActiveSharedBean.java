package com.neishenme.what.eventbusobj;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/11/30.
 */

public class ActiveSharedBean {
    public String shareTitle;   //分享标题
    public String sharedLink;   //分享连接
    public String sharedDescribe;   //分享内容
    public String sharedImage;  //分享图片

    public ActiveSharedBean(String shareTitle, String sharedLink, String sharedDescribe, String sharedImage) {
        this.shareTitle = shareTitle;
        this.sharedLink = sharedLink;
        this.sharedDescribe = sharedDescribe;
        this.sharedImage = sharedImage;
    }
}
