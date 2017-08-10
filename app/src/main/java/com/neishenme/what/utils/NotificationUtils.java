package com.neishenme.what.utils;

import android.app.Notification;

import com.neishenme.what.R;
import com.neishenme.what.application.App;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * 作者：zhaozh create on 2016/4/12 15:39
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class NotificationUtils {
    static CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(
            App.getApplication(), R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);

    public static void updateNotification() {
        builder.layoutIconDrawable = R.drawable.push_actionbar;
        builder.statusBarDrawable = R.drawable.push_small_icon;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失

        //设置全属性
        init1();

        //灯,声音
        init2();

        //震动,灯
        init3();

        //灯
        init4();
    }

    private static void init4() {
        builder.notificationDefaults = Notification.DEFAULT_LIGHTS;
        builder.developerArg0 = "developerArg2";
        JPushInterface.setPushNotificationBuilder(4, builder);
    }

    private static void init3() {
        builder.notificationDefaults = Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;
        builder.developerArg0 = "developerArg2";
        JPushInterface.setPushNotificationBuilder(3, builder);
    }

    private static void init2() {
        builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS;
        builder.developerArg0 = "developerArg2";
        JPushInterface.setPushNotificationBuilder(2, builder);
    }

    private static void init1() {
        builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
        builder.developerArg0 = "developerArg2";
        JPushInterface.setPushNotificationBuilder(1, builder);
    }
}
