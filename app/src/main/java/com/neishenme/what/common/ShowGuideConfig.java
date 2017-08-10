package com.neishenme.what.common;

import com.neishenme.what.application.App;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/1/6.
 * 展示功能引导的 配置, 因为在@see {@link com.neishenme.what.activity.SplashActivity}
 * 和 {@link com.neishenme.what.activity.MainActivity}
 * 和 {@link com.neishenme.what.activity.InviteJoinerDetailActivity}
 * 以及 {@link com.neishenme.what.activity.InviteInviterDetailActivity}
 * 都可能用到,所以需要进行同一的配置
 * 该字段 存储在 {@link com.neishenme.what.application.App#SP} 中
 */

public class ShowGuideConfig {

    /**
     * 首页展示加入的功能引导
     */
    private static final String SHOW_GUIDE_INVITE_JOIN = "guide_join";

    /**
     * 首页展示发布的功能引导
     */
    private static final String SHOW_GUIDE_RELEASE_TYPE = "guide_release";

    /**
     * 邀请详情展示聊天的功能引导
     */
    private static final String SHOW_GUIDE_INVITE_TALK = "guide_talk";

    /**
     * 添加应该展示所有的功能引导
     */
    public static void shouldShownAll() {
//        App.EDIT.putBoolean(ShowGuideConfig.SHOW_GUIDE_INVITE_JOIN, true).commit();
//        App.EDIT.putBoolean(ShowGuideConfig.SHOW_GUIDE_RELEASE_TYPE, true).commit();
//        App.EDIT.putBoolean(ShowGuideConfig.SHOW_GUIDE_INVITE_TALK, true).commit();
    }

    /**
     * 是否应该展示加入功能引导
     * @return
     */
    public static boolean shouldShowJoin() {
        return App.SP.getBoolean(ShowGuideConfig.SHOW_GUIDE_INVITE_JOIN, false);
    }

    public static boolean shouldShowRelease() {
        return App.SP.getBoolean(ShowGuideConfig.SHOW_GUIDE_RELEASE_TYPE, false);
    }

    public static boolean shouldShowTalk() {
        return App.SP.getBoolean(ShowGuideConfig.SHOW_GUIDE_INVITE_TALK, false);
    }

    /**
     * 已经显示完毕谈话功能引导后调用
     *
     * @return
     */
    public static void shownJoined() {
        App.EDIT.remove(ShowGuideConfig.SHOW_GUIDE_INVITE_JOIN).commit();
    }

    public static void shownReleaseed() {
        App.EDIT.remove(ShowGuideConfig.SHOW_GUIDE_RELEASE_TYPE).commit();
    }

    public static void shownTalked() {
        App.EDIT.remove(ShowGuideConfig.SHOW_GUIDE_INVITE_TALK).commit();
    }
}
