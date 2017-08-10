package com.neishenme.what.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import com.neishenme.what.application.App;
import com.neishenme.what.bean.LoginQuickSuccessResponse;
import com.neishenme.what.bean.LoginResponse;
import com.neishenme.what.common.AppSharePreConfig;
import com.neishenme.what.db.HomeNewsOpenHelper;
import com.neishenme.what.receiver.MyJPushReceiver;
import com.neishenme.what.service.SocketGetLocationService;

import org.seny.android.utils.ALog;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 作者：zhaozh create on 2016/3/18 17:29
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个更新用户信息往sp中存储数据的类
 * .
 * 其作用是
 * photoUrls 添加方法是 每个字段有三个图片, 中间用and连接, 不同字段用other连接,最后一个不拼接
 * 拼接结果类似于     .jpgand.jpgother.jpg.jpg
 * <p/>
 * 切的时候先用 other 切,就得到每个字段的图片,在用and切,就得到每个图片的地址了.
 * <p/>
 * <p/>
 * <p/>
 * 图片 地址和参数 ;
 * apil/users/photos  参数 :　userId,  token
 * 自己爱好的地址和参数
 * apin/userInterest/interests   参数 : userId;
 * <p/>
 * 暂时不需要添加图片和爱好了.已经有接口了.
 * <p/>
 * userId
 * phone
 * name
 * birthday
 * audioDuration
 * audioUrl
 * videoUrl
 * videoThumb
 * logo
 * thumbnailslogo
 * gender
 * photoUrls
 * password
 * token
 */
public class UpdataPersonInfo {

    public static void updatePersonInfoByNormal(LoginResponse.DataEntity.UserEntity userInfo) {
        App.EDIT.remove(AppSharePreConfig.USER_LOGIN_BE_T).commit();
        App.hxUsername = userInfo.getHxUserName();

        App.EDIT.putString("token", userInfo.getToken()).commit();

        //用户名称和环信Id手机号
        App.USEREDIT.putString("userId", String.valueOf(userInfo.getId())).commit();
        App.USEREDIT.putString("hxUserName", userInfo.getHxUserName()).commit();
        App.USEREDIT.putString("phones", userInfo.getPhone()).commit();

        //手机昵称生日性别签名
        //App.USEREDIT.putString("phone", userInfo.getPhone()).commit();
        App.USEREDIT.putString("birthday", String.valueOf(userInfo.getBirthday())).commit();
        App.USEREDIT.putString("gender", String.valueOf(userInfo.getGender())).commit();
        App.USEREDIT.putString("name", userInfo.getName()).commit();
        App.USEREDIT.putString("signature", userInfo.getSign()).commit();


        //声音时长和地址
        App.USEREDIT.putString("audioDuration", String.valueOf(userInfo.getAudioDuration())).commit();
        App.USEREDIT.putString("audioUrl", userInfo.getAudioFile()).commit();
        //视频地址和缩略图,视频时长
        App.USEREDIT.putString("videoUrl", userInfo.getVideoFile()).commit();
        App.USEREDIT.putString("videoThumb", userInfo.getVideoThumb()).commit();
        App.USEREDIT.putString("videoDuration", String.valueOf(userInfo.getVideoDuration())).commit();

        //正常图片和缩略图
        App.USEREDIT.putString("logo", userInfo.getLogo()).commit();
        App.USEREDIT.putString("thumbnailslogo", userInfo.getThumbnailslogo()).commit();
        App.USEREDIT.putString("background", userInfo.getBackground()).commit();


        //登录到环信
        if (!HuanXinUtils.isLoginToHX()) {
            HuanXinUtils.login();
        }

        if (!PackageVersion.isServiceRunning(UIUtils.getContext())) {
            UIUtils.getContext().startService(new Intent(UIUtils.getContext(), SocketGetLocationService.class));
        }

        startJpush(String.valueOf(userInfo.getId()), userInfo.getGender(), userInfo.getBirthday());
    }

    public static void updatePersonInfoByToken(LoginQuickSuccessResponse.DataEntity dataEntity) {
        LoginQuickSuccessResponse.DataEntity.UserEntity userInfo = dataEntity.getUser();

        App.EDIT.remove(AppSharePreConfig.USER_LOGIN_BE_T).commit();
        App.hxUsername = userInfo.getHxUserName();

        App.EDIT.putString("token", userInfo.getToken()).commit();

        //用户名称和环信Id
        App.USEREDIT.putString("userId", String.valueOf(userInfo.getId())).commit();
        App.USEREDIT.putString("hxUserName", userInfo.getHxUserName()).commit();

        //手机昵称生日性别签名
        //App.USEREDIT.putString("phone", userInfo.getPhone()).commit();
        App.USEREDIT.putString("name", userInfo.getName()).commit();
        App.USEREDIT.putString("birthday", String.valueOf(userInfo.getBirthday())).commit();
        App.USEREDIT.putString("gender", String.valueOf(userInfo.getGender())).commit();
        App.USEREDIT.putString("signature", userInfo.getSign()).commit();


        //声音时长和地址
        App.USEREDIT.putString("audioDuration", String.valueOf(userInfo.getAudioDuration())).commit();
        App.USEREDIT.putString("audioUrl", userInfo.getAudioFile()).commit();
        //视频地址和缩略图,视频时长
        App.USEREDIT.putString("videoUrl", userInfo.getVideoFile()).commit();
        App.USEREDIT.putString("videoThumb", userInfo.getVideoThumb()).commit();
        App.USEREDIT.putString("videoDuration", String.valueOf(userInfo.getVideoDuration())).commit();

        //正常图片和缩略图
        App.USEREDIT.putString("logo", userInfo.getLogo()).commit();
        App.USEREDIT.putString("thumbnailslogo", userInfo.getThumbnailslogo()).commit();
        App.USEREDIT.putString("background", userInfo.getBackground()).commit();

        //登录到环信
        if (!HuanXinUtils.isLoginToHX()) {
            HuanXinUtils.login();
        }

        if (!PackageVersion.isServiceRunning(UIUtils.getContext())) {
            UIUtils.getContext().startService(new Intent(UIUtils.getContext(), SocketGetLocationService.class));
        }

        startJpush(String.valueOf(userInfo.getId()), userInfo.getGender(), userInfo.getBirthday());
    }

    private static void startJpush(String id, String gender, long birthday) {
        JPushInterface.resumePush(App.getApplication());

        JPushInterface.setAlias(App.getApplication(), id, new TagAliasCallback() {
            @Override
            public void gotResult(final int i, final String s, Set<String> set) {
            }
        });

        Set<String> set = new HashSet<>();
        set.add("PLATFORM_ANDROID");
        set.add("CHANNELS_" + AppManager.getAppMetaData(App.getApplication()));
        if (gender.equals("1")) {
            set.add("GENDER_1");
        } else {
            set.add("GENDER_2");
        }
        set.add("ANDROID_VERSION_" + PackageVersion.getPackageVersion(App.getApplication()).replace(".", "_"));
        set.add("AGE" + TimeUtils.getAge(birthday));
        JPushInterface.setTags(App.getApplication(), set, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
            }
        });
    }

    public static void logoutPersonInfo() {
        App.USEREDIT.clear().commit();

        App.EDIT.remove("token").commit();
        App.EDIT.remove(MyJPushReceiver.MSG_ORDER_INVITE_ID).commit();

        File file = new File(FileUtil.getSDVideoDir(), "myVideo.mp4");
        if (file.exists() && file.length() > 0) {
            file.delete();
        }

        HuanXinUtils.logoutToHX();

        JPushInterface.stopPush(App.getApplication());
        App.getApplication().deleteDatabase(HomeNewsOpenHelper.CREATE_TABLE_NAME);
        UIUtils.getContext().stopService(new Intent(UIUtils.getContext(), SocketGetLocationService.class));
    }
}
