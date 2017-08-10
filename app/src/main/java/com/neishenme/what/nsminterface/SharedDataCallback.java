package com.neishenme.what.nsminterface;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/3/7.
 */

public interface SharedDataCallback {
    public static final int SHARE_TO_WEIXIN = 0;
    public static final int SHARE_TO_WEIXINFRIEND = 1;
    public static final int SHARE_TO_QQFRIEND = 2;
    public static final int SHARE_TO_SINA = 3;

    public void startShared(int shareChannel);
}
