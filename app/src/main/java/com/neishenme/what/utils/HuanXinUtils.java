package com.neishenme.what.utils;


import android.text.TextUtils;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.neishenme.what.application.App;
import com.neishenme.what.huanxinchat.NSMHXHelper;

import org.seny.android.utils.ALog;
import org.seny.android.utils.MD5Utils;

/**
 * 作者：zhaozh create on 2016/4/15 13:41
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个登录环信的类
 * .
 * 其作用是 :
 */
public class HuanXinUtils {
    private static String DEFAULT_PASSWORD = MD5Utils.addToMD5("www.neishenme.com");
    public static final String MY_HX_USERNAME = "hxUserName";

//    public static boolean loginToHX() {
//        String userName = App.USERSP.getString(MY_HX_USERNAME, "");
//        if (EMClient.getInstance().isLoggedInBefore()) {
//            //登录如果是登录状态直接返回
//            return true;
//        }
//        if (TextUtils.isEmpty(userName)) {
//            if (TextUtils.isEmpty(App.hxUsername)) {
//                //username为空,说明没有帐号需要注册
//                return false;
//            } else {
//                userName = App.hxUsername;
//            }
//        }
//        //登录
//        EMClient.getInstance().login(userName, DEFAULT_PASSWORD, new EMCallBack() {//回调
//            @Override
//            public void onSuccess() {
//                b = true;
//            }
//
//            @Override
//            public void onProgress(int progress, String status) {
//            }
//
//            @Override
//            public void onError(int code, String error) {
//                ALog.w("登录失败,错误码是" + code + " 错误原因是:" + error);
//                b = false;
//            }
//        });
//        return b;
//    }

    public static boolean isLoginToHX() {
        if (EMClient.getInstance().isLoggedInBefore()) {
            return true;
        }
        return false;
    }

    public static void login() {
        String userName = App.USERSP.getString(MY_HX_USERNAME, "");
        if (EMClient.getInstance().isLoggedInBefore()) {
            return;
        }
        if (TextUtils.isEmpty(userName)) {
            if (TextUtils.isEmpty(App.hxUsername)) {
                return;
            } else {
                userName = App.hxUsername;
            }
        }
        if (TextUtils.isEmpty(DEFAULT_PASSWORD)) {
            DEFAULT_PASSWORD = MD5Utils.addToMD5("www.neishenme.com");
        }
        EMClient.getInstance().login(userName, DEFAULT_PASSWORD, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String error) {
                ALog.e("登录失败,错误码是" + code + " 错误原因是:" + error);
            }
        });
    }

    public static void logoutToHX() {
        EMClient.getInstance().logout(true);
    }
}
