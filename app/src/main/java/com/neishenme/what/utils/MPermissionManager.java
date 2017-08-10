package com.neishenme.what.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;

import static com.umeng.socialize.utils.ContextUtil.getPackageName;

/**
 * 这个类的作用是: 升级target 23 后的权限管理类
 * <p>
 * Created by zhaozh on 2016/12/28.
 */

public class MPermissionManager {
    public static final int REQUEST_CODE_LOCATION = 101;
    public static final int REQUEST_CODE_AUDIO = 102;
    public static final int REQUEST_CODE_CAMERA = 103;
    public static final int REQUEST_CODE_CAMERA_ER_WEIMA = 105;
    public static final int REQUEST_CODE_CAMERA_HEADER_PHOTO = 106;
    public static final int REQUEST_CODE_SD_CARD = 104;

    public static final String REQUEST_TITLE = "权限提醒";
    public static final String REQUEST_POSITIVE = "同意";
    public static final String REQUEST_NEGATIVE = "取消";

    public static final String REQUEST_LOCATION_DISCRIBE =
            "内什么需要获取您的具体位置获取附近的邀请和您所在的城市,请同意我们位置权限的申请";
    public static final String REQUEST_AUDIO_DISCRIBE =
            "内什么需要您授予麦克风权限来进行录音,请同意我们麦克风权限的申请";
    public static final String REQUEST_AUDIO_SD_CARD =
            "内什么需要访问请同意我们麦克风权限的申请";
    public static final String REQUEST_CAMERA_RELEASE =
            "内什么需要录制视频的权限,请同意我们的申请";


    public static final String REQUEST_TITLE_WARNING = "权限警告";
    public static final String REQUEST_LOCATION_WARNING =
            "系统监测到您当前缺少定位权限,产品功能将无法使用.请单击确定按钮前往设置中心进行权限授权或点击取消按钮退出APP";
    public static final String REQUEST_AUDIO_WARNING =
            "系统监测到您当前缺少录音权限,产品录音功能将无法使用.请单击确定按钮前往设置中心进行权限授权或点击取消按钮取消录制";
    public static final String REQUEST_CAMERA_WARNING =
            "系统监测到您当前缺少摄像权限,个人中心无法使用.请单击确定按钮前往设置中心进行权限授权或点击取消进入";
    public static final String REQUEST_CAMERA_ERWEIMA_WARNING =
            "系统监测到您当前缺少摄像权限,无法扫描二维码.请单击确定按钮前往设置中心进行权限授权或点击取消进入";
    public static final String REQUEST_CAMERA_HEADER_PHOTO_WARNING =
            "系统监测到您当前缺少摄像权限,将无法进行拍照.请单击确定按钮前往设置中心进行权限授权或取消";


    private static String message;

    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Context context = (Context) msg.obj;
                startAppSettings(context);
            }
        }
    };

    /**
     * 请求获取位置的权限
     */
    public static void requestLocationPermission(Activity activity) {
//        AndPermission.with(activity)
//                .requestCode(REQUEST_CODE_LOCATION)
//                .permission(Manifest.permission.ACCESS_FINE_LOCATION)
//                .permission(Manifest.permission.ACCESS_COARSE_LOCATION)
//                .permission(Manifest.permission.ACCESS_COARSE_LOCATION)
//                .rationale(mRationaleListener)
//                .send();
    }

//    private static RationaleListener mRationaleListener = new RationaleListener() {
//        @Override
//        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
//            switch (requestCode) {
//                case 102:
//                    message = "申请摄像机权限";
//                    break;
//                case 103:
//                    message = "申请联系人权限";
//                    break;
//                case REQUEST_CODE_LOCATION:
//                    message = "申请位置权限";
//                    break;
//                case 105:
//                    message = "申请麦克风权限";
//                    break;
//                case 106:
//                    message = "申请sd卡读写权限";
//                    break;
//            }
//            new AlertDialog.Builder(Main8Activity.this)
//                    .setTitle("提醒")
//                    .setMessage(message)
//                    .setPositiveButton("好，给你", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                            rationale.resume();// 用户同意继续申请。
//                        }
//                    })
//                    .setNegativeButton("我拒绝", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                            rationale.cancel(); // 用户拒绝申请。
//                        }
//                }).show();
//        }
//    };

    public static Dialog showToSetting(final Context context, int type, final OnNegativeListner onNegativeListner) {
        String message;
        switch (type) {
            case REQUEST_CODE_LOCATION:
                message = REQUEST_LOCATION_WARNING;
                break;
            case REQUEST_CODE_AUDIO:
                message = REQUEST_AUDIO_WARNING;
                break;
            case REQUEST_CODE_CAMERA:
                message = REQUEST_CAMERA_WARNING;
                break;
            case REQUEST_CODE_CAMERA_ER_WEIMA:
                message = REQUEST_CAMERA_ERWEIMA_WARNING;
                break;
            case REQUEST_CODE_CAMERA_HEADER_PHOTO:
                message = REQUEST_CAMERA_HEADER_PHOTO_WARNING;
                break;
            default:
                message = "";
                break;
        }
        AlertDialog permissionDialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(REQUEST_TITLE_WARNING)
                .setMessage(message)
                .setNegativeButton(REQUEST_NEGATIVE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onNegativeListner != null) {
                            onNegativeListner.onNegative();
                        }
                    }
                })
                .setPositiveButton(REQUEST_POSITIVE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mHandler.sendMessage(mHandler.obtainMessage(1, context));
                    }
                }).show();
        return permissionDialog;
    }

    /**
     * 启动当前应用设置页面
     *
     * @param context
     */
    private static void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        context.startActivity(intent);
    }

    public interface OnNegativeListner {
        public void onNegative();
    }
}
