package com.neishenme.what.common;

import android.text.TextUtils;

import com.neishenme.what.application.App;

import org.seny.android.utils.DateUtils;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/11/28.
 */

public class ActiveSPConfig {
    //该SharedPreferences的名称
    public static final String ACTIVE_SP_NAME = "activesconfig";

    public static final String ACTIVE_SP_VERSION = "version";
    public static final String ACTIVE_SP_SHOW_DAY = "showday";

    //活动type分类
    public static final int SHOW_DIALOG_ONCE = 0;
    public static final int SHOW_DIALOG_EVERYDAY = 1;
    public static final int SHOW_DIALOG_EVERYTIME = 2;


    public static String getVersion() {
        String string = App.ACTIVESP.getString(ACTIVE_SP_VERSION, "");
        return string;
    }

    public static boolean isTodayAlreadyShow() {
        String showDay = App.ACTIVESP.getString(ACTIVE_SP_SHOW_DAY, "");
        if (TextUtils.isEmpty(showDay)) {
            return false;
        }
        return String.valueOf(DateUtils.formatDay(System.currentTimeMillis())).equals(showDay);
    }

    public static void updateVersionAndShowday(String version) {
        updateVersion(version);
        App.ACTIVEEDIT.putString(ACTIVE_SP_SHOW_DAY, String.valueOf(DateUtils.formatDay(System.currentTimeMillis()))).commit();
    }

    public static void updateVersion(String version) {
        App.ACTIVEEDIT.putString(ACTIVE_SP_VERSION, version).commit();
    }
}
