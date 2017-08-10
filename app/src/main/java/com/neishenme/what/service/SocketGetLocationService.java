package com.neishenme.what.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.neishenme.what.R;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.application.App;
import com.neishenme.what.bean.SocketLoginBean;
import com.neishenme.what.bean.SocketResultBean;
import com.neishenme.what.bean.SocketSendBean;
import com.neishenme.what.common.AppSharePreConfig;
import com.neishenme.what.common.CityLocationConfig;
import com.neishenme.what.eventbusobj.UserMeetingSuccess;
import com.neishenme.what.nsminterface.OnDanMuInfoCallback;
import com.neishenme.what.socket.SocketClient;
import com.neishenme.what.socket.helper.SocketClientAddress;
import com.neishenme.what.socket.helper.SocketClientDelegate;
import com.neishenme.what.socket.helper.SocketResponsePacket;
import com.neishenme.what.socket.util.CharsetUtil;
import com.neishenme.what.utils.AppManager;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.LocationUtils;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.UpdataPersonInfo;

import org.seny.android.utils.ALog;

import java.io.UnsupportedEncodingException;

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
public class SocketGetLocationService extends Service implements ClientSocket.OnSocketRecieveCallBack {
    private static final int NOTIFICATION_FLAG = 1;
    //连接
    public static final int FLAG_LOCAGION_LOGIN = 1;
    //获取位置
    public static final int FLAG_LOCAGION_GET = 2;
    //推送位置
    public static final int FLAG_LOCAGION_PUSH_GET = 3;
    //默认
    public static final int FLAG_LOCAGION_DEFAULT = 0;

    public static int mCurrentLocation = FLAG_LOCAGION_DEFAULT;

    private static SocketClient localSocketClient;

    double latitude, longgitude;
    private Gson gson;
    private int inviteId;

    private int mCurrentLinkTime = 1;
    private int mCurrentSwap = 0;   //标记第几次请求mainthread, 每三次会连接一次
    private boolean send = false;

    private static OnDanMuInfoCallback mMuInfoCallback;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gson = new Gson();
//        mClientSocket = App.getClientSocket();
//        mClientSocket.setOnSocketRecieveCallBack(this);
//        mClientSocket.setOnSocketStateListener(this);
//        try {
//            mClientSocket.start();
//            mClientSocket.startReciveMsg();
//        } catch (Exception e) {
//        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                getLocalSocketClient().connect();
            }
        }).start();

        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(SocketSendBean socketSendBean) {
        if (mCurrentSwap >= 4) {
            mCurrentSwap = 0;
            mCurrentLocation = FLAG_LOCAGION_LOGIN;
            send = true;
            getLocation();
        } else {
            mCurrentSwap++;
            String sendBean = gson.toJson(socketSendBean);
            sendMessage(sendBean);
            ALog.i("即将发送位置 ,数据为: " + sendBean);
            EventBus.clearCaches();
        }
    }

    public SocketClient getLocalSocketClient() {
        if (this.localSocketClient == null) {
            this.localSocketClient = new SocketClient(
                    new SocketClientAddress(ConstantsWhatNSM.SOCKET_URL_SERVER_URL, ConstantsWhatNSM.SOCKET_URL_SERVER_PORT));

            __i__setupAddress(this.localSocketClient);
            __i__setupEncoding(this.localSocketClient);

            __i__setupConstantHeartBeat(this.localSocketClient);

            __i__setupReadByLengthForSender(this.localSocketClient);
            __i__setupReadByLengthForReceiver(this.localSocketClient);

            localSocketClient.getSocketPacketHelper().setSegmentLength(8 * 1024);

            // 对应removeSocketClientDelegate
            this.localSocketClient.registerSocketClientDelegate(new SocketClientDelegate() {
                @Override
                public void onConnected(SocketClient client) {
                    ALog.i("socket连接成功!!!!");

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            mCurrentLocation = FLAG_LOCAGION_LOGIN;
                            send = true;
                            getLocation();
                            ALog.i("socket 重新登录!!!!");
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);

                        }
                    }.execute();
                }

                @Override
                public void onDisconnected(final SocketClient client) {
                    ALog.i("socket 断开连接了!!!!");
                    if (!NSMTypeUtils.isLogin()) {
                        ALog.i("未登录状态, 退出");
                        return;
                    }

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(3 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            client.connect();
                            ALog.i("socket 尝试重新连接!!!!");
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);

                        }
                    }.execute();
                }

                @Override
                public void onResponse(final SocketClient client, @NonNull SocketResponsePacket responsePacket) {
//                    ALog.i("socket 获取到了数据!!!! "
//                            + " 【" + responsePacket.getMessage() + "】 ");

                    String message = responsePacket.getMessage(); // 获取按默认设置的编码转化的String，可能为null

                    if (TextUtils.isEmpty(message)) {
                        return;
                    }
                    OnRecieveFromServerMsg(message);
                }
            });
        }
        return this.localSocketClient;
    }

    private void __i__setupAddress(SocketClient socketClient) {
//        socketClient.getAddress().setRemoteIP(ConstantsWhatNSM.SOCKET_URL_SERVER_URL); // 远程端IP地址
//        socketClient.getAddress().setRemotePort(ConstantsWhatNSM.SOCKET_URL_SERVER_PORT); // 远程端端口号
        socketClient.getAddress().setConnectionTimeout(10 * 1000); // 连接超时时长，单位毫秒
    }

    private void __i__setupEncoding(SocketClient socketClient) {
        socketClient.setCharsetName(CharsetUtil.UTF_8); // 设置编码为UTF-8
    }

    private void __i__setupConstantHeartBeat(SocketClient socketClient) {
        socketClient.getHeartBeatHelper().setSendString(null); // 设置自动发送心跳包的间隔时长，单位毫秒
        socketClient.getHeartBeatHelper().setHeartBeatInterval(10 * 1000); // 设置自动发送心跳包的间隔时长，单位毫秒
//        socketClient.getHeartBeatHelper().setRemoteNoReplyAliveTimeout(30 * 1000); // 设置自动发送心跳包的间隔时长，单位毫秒
    }

    private void __i__setupReadByLengthForSender(SocketClient localSocketClient) {
        localSocketClient.getSocketPacketHelper().setSendHeaderData(null);
//        localSocketClient.getSocketPacketHelper().setSendHeaderData("\r\n".getBytes());
    }

    private void __i__setupReadByLengthForReceiver(SocketClient socketClient) {
//        socketClient.getSocketPacketHelper().setReceiveTrailerString("\r\n");
        socketClient.getSocketPacketHelper().setReceiveTrailerString(null);
//        socketClient.getSocketPacketHelper().setReceiveHeaderString("\r\n"); // 同上
    }

    private void sendMessage(String message) {
        try {
            localSocketClient.sendData(message.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            ALog.e("发送失败!" + message);
            e.printStackTrace();
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
//                        Gps gps = PositionUtil.bd09_To_Gcj02(location.getLatitude(), location.getLongitude());
                        SocketGetLocationService.this.latitude = latitude;
                        SocketGetLocationService.this.longgitude = longgitude;
                        switch (mCurrentLocation) {
                            case FLAG_LOCAGION_LOGIN:
                                SocketLoginBean socketLoginBean = NSMTypeUtils.getSocketLoginBean(latitude, longgitude);
                                String sendBean = gson.toJson(socketLoginBean);
                                sendMessage(sendBean);
                                ALog.i("logining发送成功,标志是 login " + sendBean);
                                break;
                            case FLAG_LOCAGION_GET:
                                SocketSendBean socketSendBean = NSMTypeUtils.getSocketSendBean(
                                        NSMTypeUtils.RequestType.LOCATION, latitude, longgitude, inviteId);
                                String sendBeans = gson.toJson(socketSendBean);
                                sendMessage(sendBeans);
                                ALog.i("sendBean发送成功,标志是 swapslocation" + sendBeans);
                                break;
                            case FLAG_LOCAGION_PUSH_GET:
                                SocketLoginBean socketLoginBeans = NSMTypeUtils.getSocketLoginBean(latitude, longgitude);
                                String sendBeanss = gson.toJson(socketLoginBeans);
                                sendMessage(sendBeanss);
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
            CityLocationConfig.getLocationUtil(new CityLocationConfig.OnCitySuccess() {
                @Override
                public void onCitySuccess() {
                    if (mCurrentLinkTime < 50) {
                        mCurrentLocation = FLAG_LOCAGION_LOGIN;
                        send = true;
                        mCurrentLinkTime++;
                        ALog.i("SOCKET 没有获取到areaId, 现在的次数为: " + mCurrentLinkTime);
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                try {
                                    Thread.sleep(3 * 1000);
                                    getLocation();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                ALog.i("socket 尝试重新连接!!!!");
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);

                            }
                        }.execute();
                    } else {
                        mCurrentLinkTime = 1;
                    }
                }

                @Override
                public void onCityError() {

                }
            });
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ALog.i("又一次启动了");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void OnRecieveFromServerMsg(String msg) {
        mCurrentSwap = 0;
        mCurrentLocation = FLAG_LOCAGION_DEFAULT;
        ALog.i("获取到的字符串是" + msg);
        SocketResultBean socketResultBean;
        try {
            socketResultBean = gson.fromJson(msg, SocketResultBean.class);
        } catch (Exception e) {
            ALog.i("解析收到的字符串失败, 返回");
            return;
        }
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
        } else if (socketResultBean.getType().equals("lucky") && socketResultBean.getCode() == 200) {
            if (mMuInfoCallback != null) {
                mMuInfoCallback.onDanMuInfoCallback(socketResultBean.getData().getMsg());
            }
        } else if (socketResultBean.getType().equals("pushlocation")
                || socketResultBean.getType().equals("message")) {
            EventBus.getDefault().post(socketResultBean);
            ALog.i("获取位置成功");
        } else if (socketResultBean.getType().equals("meeting") && socketResultBean.getCode() == 200) {
            ALog.i("获取meeting成功");
            UserMeetingSuccess userMeetingSuccess = new UserMeetingSuccess(socketResultBean.getData().getInviteId());
            EventBus.getDefault().post(userMeetingSuccess);
        } else if (socketResultBean.getType().equals("missmeet") && socketResultBean.getCode() == 200) {
//            if (socketResultBean.getData().getLasttime() <= System.currentTimeMillis()) {
//                return;
//            }
//            String userId = String.valueOf(socketResultBean.getData().getUserId());
//            String inviteId = String.valueOf(socketResultBean.getData().getInviteId());
//            if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(inviteId)) {
//                return;
//            }
//            ALog.w("获取 对方爽约信息成功!! 该 inviteId 为: " + inviteId + " ,该invite 的 用户 userId 为: " + userId);
//            AppSharePreConfig.saveUserMissMeet(inviteId, userId);
//            if (AppManager.isRunningForeground(this)) {
//                Intent i;
//                if (userId.equals(NSMTypeUtils.getMyUserId())) {
//                    i = new Intent(this, InviteInviterDetailActivity.class);
//                } else {
//                    i = new Intent(this, InviteJoinerDetailActivity.class);
//                }
//                i.putExtra("data", inviteId);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
//                UserMissMeetingBean missMeetingBean = new UserMissMeetingBean(
//                        socketResultBean.getData().getUserId(), socketResultBean.getData().getInviteId());
//                EventBus.getDefault().postSticky(missMeetingBean);
//            }
        } else if (socketResultBean.getType().equals("logout") && socketResultBean.getCode() == 200) {
            App.EDIT.putBoolean(AppSharePreConfig.USER_LOGIN_BE_T, true).commit();
            UpdataPersonInfo.logoutPersonInfo();

            // 判断如果在app中就理消息通知
            if (AppManager.isRunningForeground(this)) {
                Intent i = new Intent(this, MainActivity.class);
                //FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            } else {
                // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                        new Intent(this, MainActivity.class), 0);
                Notification notify = new Notification.Builder(this)
                        .setSmallIcon(R.drawable.push_actionbar)
                        .setTicker("内什么 : 下线通知")
                        .setContentTitle("内什么")
                        .setContentText("您的帐号在其他设备登录,您被迫下线")
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .build();
                notify.flags |= Notification.FLAG_AUTO_CANCEL;
                manager.notify(NOTIFICATION_FLAG, notify);
            }
        } else {
            ALog.i("连接是存在的成功");
        }
    }

    public static void regestAdPush(OnDanMuInfoCallback muInfoCallback) {
        mMuInfoCallback = muInfoCallback;
    }

    public static void unRegestAdPush() {
        mMuInfoCallback = null;
    }

    @Override
    public void onDestroy() {
        localSocketClient.disconnect();
        EventBus.clearCaches();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
