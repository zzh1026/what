package com.neishenme.what.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.neishenme.what.activity.InviteInviterDetailActivity;
import com.neishenme.what.activity.InviteJoinerDetailActivity;
import com.neishenme.what.application.App;
import com.neishenme.what.bean.SocketLoginBean;
import com.neishenme.what.bean.SocketResultBean;
import com.neishenme.what.bean.SocketSendBean;
import com.neishenme.what.common.CityLocationConfig;
import com.neishenme.what.eventbusobj.UserMissMeetingBean;
import com.neishenme.what.utils.AppManager;
import com.neishenme.what.utils.LocationUtils;
import com.neishenme.what.utils.NSMTypeUtils;

import org.seny.android.utils.ALog;

import de.greenrobot.event.EventBus;

/**
 * 作者：zhaozh create on 2016/6/13 15:36
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class SocketGetLocationNewOldService extends Service implements ClientSocket.OnSocketRecieveCallBack, ClientSocket.OnSocketStateListener {
    //连接
    public static final int FLAG_LOCAGION_LOGIN = 1;
    //获取位置
    public static final int FLAG_LOCAGION_GET = 2;
    //推送位置
    public static final int FLAG_LOCAGION_PUSH_GET = 3;
    //默认
    public static final int FLAG_LOCAGION_DEFAULT = 0;

    public static int mCurrentLocation = FLAG_LOCAGION_DEFAULT;

    ClientSocket mClientSocket = null;
    double latitude, longgitude;
    private Gson gson;
    private int inviteId;

    private int mCurrentLinkTime = 1;
    private boolean send = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gson = new Gson();
        mClientSocket = App.getClientSocket();
        mClientSocket.setOnSocketRecieveCallBack(this);
        mClientSocket.setOnSocketStateListener(this);
        try {
            mClientSocket.start();
            mClientSocket.startReciveMsg();
        } catch (Exception e) {
        }
    }

    private void getLocation() {
        if (!NSMTypeUtils.isLogin()) {
            return;
        }
        if (CityLocationConfig.cityLocationId != 0) {
            LocationUtils.getLocationByGCJ_02(new LocationUtils.OnGetLocationListener() {
                @Override
                public void onGetLocation(double latitude, double longgitude, BDLocation location) {
                    if (send) {
                        send = false;
//                    Gps gps = PositionUtil.gcj_To_Gps84(location.getLatitude(), location.getLongitude());
                        SocketGetLocationNewOldService.this.latitude = location.getLatitude();
                        SocketGetLocationNewOldService.this.longgitude = location.getLongitude();
                        switch (mCurrentLocation) {
                            case FLAG_LOCAGION_LOGIN:
                                SocketLoginBean socketLoginBean = NSMTypeUtils.getSocketLoginBean(latitude, longgitude);
                                String sendBean = gson.toJson(socketLoginBean);
                                mClientSocket.addSendMsgToQueue(sendBean);
                                ALog.i("logining发送成功,标志是 swapslocation" + sendBean);
                                break;
                            case FLAG_LOCAGION_GET:
                                SocketSendBean socketSendBean = NSMTypeUtils.getSocketSendBean(
                                        NSMTypeUtils.RequestType.LOCATION, latitude, longgitude, inviteId);
                                String sendBeans = gson.toJson(socketSendBean);
                                mClientSocket.addSendMsgToQueue(sendBeans);
                                ALog.i("sendBean发送成功,标志是 swapslocation" + sendBeans);
                                break;
                            case FLAG_LOCAGION_PUSH_GET:
                                SocketLoginBean socketLoginBeans = NSMTypeUtils.getSocketLoginBean(latitude, longgitude);
                                String sendBeanss = gson.toJson(socketLoginBeans);
                                mClientSocket.addSendMsgToQueue(sendBeanss);
                                ALog.i("sendBean发送成功 标志是 location" + sendBeanss);
                                break;
                            default:
                                break;
                        }
                        mCurrentLocation = FLAG_LOCAGION_DEFAULT;
                    } else {
                        stopSelf();
                    }
                }

                @Override
                public void onGetError() {

                }
            });
        } else {
//            CityLocationConfig.getLocationUtil(new CityLocationConfig.OnCitySuccess() {
//                @Override
//                public void onCitySuccess() {
//                    if (mCurrentLinkTime < 50) {
//                        mCurrentLocation = FLAG_LOCAGION_LOGIN;
//                        send = true;
//                        mCurrentLinkTime++;
//                        ALog.i("SOCKET 没有获取到areaId, 现在的次数为: " + mCurrentLinkTime);
//                        getLocation();
//                    } else {
//                        mCurrentLinkTime = 1;
//                    }
//                }
//
//                @Override
//                public void onCityError() {
//
//                }
//            });
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ALog.i("又一次启动了");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onSocketStateException() {
        stopSelf();
    }

    @Override
    public void onSocketStateSuccess() {
        mCurrentLocation = FLAG_LOCAGION_LOGIN;
        send = true;
        getLocation();
    }

    @Override
    public void OnRecieveFromServerMsg(String msg) {
        ALog.i("获取到的字符串是" + msg);
        SocketResultBean socketResultBean = gson.fromJson(msg, SocketResultBean.class);
        if (socketResultBean.getType().equals("swapslocation") && socketResultBean.getCode() == 200) {
            EventBus.getDefault().post(socketResultBean.getData());
            inviteId = socketResultBean.getData().getInviteId();
            mCurrentLocation = FLAG_LOCAGION_GET;
            send = true;
            getLocation();
        } else if (socketResultBean.getType().equals("location") && socketResultBean.getCode() == 200) {
            mCurrentLocation = FLAG_LOCAGION_GET;
            send = true;
            getLocation();
        }
//        else if (socketResultBean.getType().equals("message") && socketResultBean.getCode() == 200) {
//            ALog.i("成功");
//        }
        else if (socketResultBean.getType().equals("pushlocation")
                || socketResultBean.getType().equals("message")) {
            EventBus.getDefault().post(socketResultBean);
            ALog.i("获取位置成功");
        } else if (socketResultBean.getType().equals("meeting") && socketResultBean.getCode() == 200) {
            ALog.i("获取meeting成功");
            EventBus.getDefault().post(socketResultBean.getData());
        } else if (socketResultBean.getType().equals("missmeet") && socketResultBean.getCode() == 200) {
            if (socketResultBean.getData().getLasttime() < System.currentTimeMillis()) {
                return;
            }
            if (AppManager.isRunningForeground(this)) {
                String s = String.valueOf(socketResultBean.getData().getUserId());
                if (TextUtils.isEmpty(s)) {
                    return;
                }
                Intent i;
                if (s.equals(NSMTypeUtils.getMyUserId())) {
                    i = new Intent(this, InviteInviterDetailActivity.class);
                } else {
                    i = new Intent(this, InviteJoinerDetailActivity.class);
                }
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("data", socketResultBean.getData().getInviteId() + "");
                startActivity(i);
                UserMissMeetingBean missMeetingBean = new UserMissMeetingBean(
                        socketResultBean.getData().getUserId(), socketResultBean.getData().getInviteId());
                EventBus.getDefault().postSticky(missMeetingBean);
            }
        } else {
            ALog.i("连接是存在的成功");
        }
    }

    @Override
    public void onDestroy() {
        if (mClientSocket != null) {
            try {
                mClientSocket.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            mClientSocket.closeAll();
            mClientSocket = null;
        }
        super.onDestroy();
    }
}
