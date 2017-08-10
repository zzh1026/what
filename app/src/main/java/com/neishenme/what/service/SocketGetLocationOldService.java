package com.neishenme.what.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.Gson;
import com.neishenme.what.application.App;
import com.neishenme.what.baidumap.NSMMapHelper;
import com.neishenme.what.baidumap.service.LocationService;
import com.neishenme.what.bean.SocketLoginBean;
import com.neishenme.what.bean.SocketResultBean;
import com.neishenme.what.bean.SocketSendBean;
import com.neishenme.what.utils.Gps;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.PositionUtil;

import org.seny.android.utils.ALog;

import de.greenrobot.event.EventBus;

/**
 * 作者：zhaozh create on 2017/1/9 15:36
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 旧的socket获取位置的服务,已弃用, 新的获取服务见 {@link SocketGetLocationService}
 * 该类的作用是为了保留之前的代码而 存在
 * .
 * 其作用是 :
 */
@Deprecated
public class SocketGetLocationOldService extends Service implements ClientSocket.OnSocketRecieveCallBack {
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

    private boolean send = false;
    private LocationService locationService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        ALog.i("启动成功");
        gson = new Gson();
//        mClientSocket = App.getClientSocket();
        if (mClientSocket == null) {
//            mClientSocket = new ClientSocket();
        }
//        mClientSocket.setOnSocketRecieveCallBack(this);
        try {
//            mClientSocket.start();
        } catch (Exception e) {
        }

        send = true;
        locationService = NSMMapHelper.getInstance().locationService;
        locationService.registerListener(mListener);
        locationService.setLocationOption(LocationService.CoorType.CoorType_GCJ02, LocationService.TimeType.TIME_0);
        mCurrentLocation = FLAG_LOCAGION_LOGIN;
        locationService.start();

        //连接
//        LocationUtils.getLocations(this, new LocationUtils.GetLocations() {
//            @Override
//            public void getLocations(double latitude, double longgitude) {
//                SocketGetLocationService.this.latitude = latitude;
//                SocketGetLocationService.this.longgitude = latitude;
//                SocketLoginBean socketLoginBean = NSMTypeUtils.getSocketLoginBean(latitude, longgitude);
//                String sendBean = gson.toJson(socketLoginBean);
//                mClientSocket.addSendMsgToQueue(sendBean);
//            }
//        });

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ALog.i("又一次启动了");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void OnRecieveFromServerMsg(String msg) {
        ALog.i("获取到的字符串是" + msg);
        SocketResultBean socketResultBean = gson.fromJson(msg, SocketResultBean.class);
        if (socketResultBean.getCode() == 200 && socketResultBean.getType().equals("swapslocation")) {
            EventBus.getDefault().post(socketResultBean.getData());
            inviteId = socketResultBean.getData().getInviteId();
            mCurrentLocation = FLAG_LOCAGION_GET;
            locationService.start();
            //获取位置
//            LocationUtils.getLocations(this, new LocationUtils.GetLocations() {
//                @Override
//                public void getLocations(double latitude, double longgitude) {
//                    SocketGetLocationService.this.latitude = latitude;
//                    SocketGetLocationService.this.longgitude = latitude;
//
//                    SocketSendBean socketSendBean = NSMTypeUtils.getSocketSendBean(
//                            NSMTypeUtils.RequestType.LOCATION, latitude, longgitude, inviteId);
//                    String sendBean = gson.toJson(socketSendBean);
//                    mClientSocket.addSendMsgToQueue(sendBean);
//                    ALog.i("sendBean发送成功,标志是 swapslocation" + sendBean);
//                }
//            });
        } else if (socketResultBean.getCode() == 200 && socketResultBean.getType().equals("location")) {
            mCurrentLocation = FLAG_LOCAGION_GET;
            locationService.start();

//            LocationUtils.getLocations(this, new LocationUtils.GetLocations() {
//                @Override
//                public void getLocations(double latitude, double longgitude) {
//                    SocketGetLocationService.this.latitude = latitude;
//                    SocketGetLocationService.this.longgitude = latitude;
//
//                    SocketLoginBean socketLoginBean = NSMTypeUtils.getSocketLoginBean(latitude, longgitude);
//                    String sendBean = gson.toJson(socketLoginBean);
//                    mClientSocket.addSendMsgToQueue(sendBean);
//                    ALog.i("sendBean发送成功 标志是 location" + sendBean);
//                }
//            });
        } else if (socketResultBean.getCode() == 200 && socketResultBean.getType().equals("message")) {
            ALog.i("成功");
        } else if (socketResultBean.getCode() == 200 && socketResultBean.getType().equals("pushlocation")) {
            ALog.i("获取位置成功");
            EventBus.getDefault().post(socketResultBean.getData());
        } else if (socketResultBean.getCode() == 200 && socketResultBean.getType().equals("meeting")) {
            ALog.i("获取meeting成功");
            EventBus.getDefault().post(socketResultBean);
        } else {
            ALog.i("连接是存在的成功");

        }
    }

    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
//                Gps gps = PositionUtil.gcj_To_Gps84(location.getLatitude(), location.getLongitude());
//                SocketGetLocationOldService.this.latitude = gps.getWgLat();
//                SocketGetLocationOldService.this.longgitude = gps.getWgLon();
//                ALog.i("latitude = " + location.getLatitude() + " longitude = " + location.getLongitude());
//                ALog.i("latitude = " + latitude + " longitude = " + longgitude);
//                switch (mCurrentLocation) {
//                    case FLAG_LOCAGION_LOGIN:
//                        SocketLoginBean socketLoginBean = NSMTypeUtils.getSocketLoginBean(latitude, longgitude);
//                        String sendBean = gson.toJson(socketLoginBean);
//                        mClientSocket.addSendMsgToQueue(sendBean);
//                        break;
//                    case FLAG_LOCAGION_GET:
//                        SocketSendBean socketSendBean = NSMTypeUtils.getSocketSendBean(
//                                NSMTypeUtils.RequestType.LOCATION, latitude, longgitude, inviteId);
//                        String sendBeans = gson.toJson(socketSendBean);
//                        mClientSocket.addSendMsgToQueue(sendBeans);
//                        ALog.i("sendBean发送成功,标志是 swapslocation" + sendBeans);
//                        break;
//                    case FLAG_LOCAGION_PUSH_GET:
//                        SocketLoginBean socketLoginBeans = NSMTypeUtils.getSocketLoginBean(latitude, longgitude);
//                        String sendBeanss = gson.toJson(socketLoginBeans);
//                        mClientSocket.addSendMsgToQueue(sendBeanss);
//                        ALog.i("sendBean发送成功 标志是 location" + sendBeanss);
//                        break;
//                    default:
//                        break;
//                }
                mCurrentLocation = FLAG_LOCAGION_DEFAULT;
//                locationService.stop();
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    @Override
    public void onDestroy() {
        locationService.unregisterListener(mListener); //注销掉监听
//        locationService.stop(); //停止定位服务
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
