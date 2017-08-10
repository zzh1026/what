package com.neishenme.what.common;

/**
 * 这个类的作用是:  管理所有跳入主界面(MainActivity)的信息, 将这些信息进行分离
 * <p>
 * Created by zhaozh on 2017/1/11.
 */

public class MainConfig {
    /**
     * 所有跳入mainactivity时put进去的 name
     */
    public static final String INTENT_EXTRA_NAME = "type";

    /**
     * 由极光推送广告时需要先进入主界面,然后通过主界面跳转浏览器进入广告
     */
    public static final String TYPE_SHOW_AD = "showad";

    /**
     * 由登录界面进入时需要刷新数据
     */
    public static final String TYPE_FROM_LOGIN = "login";
}
