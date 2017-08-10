package com.neishenme.what.common;

/**
 * 这个类的作用是 对app中的 主sharedPrefrece 进行配置,并提供相应的方法
 * <p>
 * 详见 {@link com.neishenme.what.application.App#SP}
 * Created by zhaozh on 2017/1/16.
 */

public class AppSharePreConfig {
    /**
     * 用户首次注册成功
     */
    public static final String IS_FIRST_REGEST = "is_first_regest";

    /**
     * 用户更新版本后首次进入(新版的首次进入,主要用来判断是否展示欢迎界面或者引导界面)
     */
    public static final String IS_FIRST_ENTER = "is_first_enter";

    /**
     * 用户下载安装成功,,,用来判断是否需要进行统计一次下载次数
     */
    public static final String IS_FIRST_ENTER_APP = "is_first_enter_app";

    /**
     * 用户被踢下线
     */
    public static final String USER_LOGIN_BE_T = "loginbeT";


}
