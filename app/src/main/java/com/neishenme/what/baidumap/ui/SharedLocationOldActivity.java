package com.neishenme.what.baidumap.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.neishenme.what.R;
import com.neishenme.what.baidumap.utils.LocationToBaiduMap;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.SocketResultBean;
import com.neishenme.what.bean.SocketSendBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.view.CircleImageView;

import org.seny.android.utils.ALog;

import de.greenrobot.event.EventBus;

/**
 * 这个类的作用是选择地址的地图界面
 * <p>
 * Created by zhaozh on 2016/12/13.
 * <p>
 * 旧的位置共享界面, 新的界面为:   {@link SharedLocationActivity}
 */

@Deprecated
public class SharedLocationOldActivity extends BaseActivity {
    private static final int REFRESH_OTHER_LOCATION = 1;    //handler发送应该刷新对方位置的值
    private static final int REFRESH_OTHER_LOCTIME = 10 * 1000;    //获取对方的位置的时间
    private static final int REFRESH_MY_LOCATION = 2;    //handler发送应该刷新自己位置的值
    private static final int REFRESH_MY_LOCTIME = 5 * 1000;    //获取自己的位置的时间

    private static final LatLng GEO_BEIJING = new LatLng(39.945, 116.404);
    private static final float X_ANCHOR = 0.5f;     //头像x轴的描点
    private static final float Y_ANCHOR = 0.3f;     //头像Y轴的描点

    private int mCurrentGetNumber = 1;

    private ImageView mSharedLocCancel;

    private MapView mBmapView;
    private BaiduMap mBaiduMap;
    private UiSettings mUiSettings;

    private Marker mMyHeaderMarker;
    private Marker mMyLogoMarker;
    private Marker mOtherHeaderMarker;
    private Marker mOtherLogoMarker;
    private Marker mLocationMarker;

    //餐厅地址
    LatLng mLocationLoc;
    //另一个人地址
    LatLng mOtherLoc;
    private LatLng mMyLoc;     //当前我的位置坐标

    public LocationClient mLocationClient = null;

//    private int mOtherRotate = 0;        //对方的朝向

    BitmapDescriptor mMyHeader = BitmapDescriptorFactory
            .fromResource(R.drawable.shared_loc_user_header_logo);
    BitmapDescriptor mOtherHeader = BitmapDescriptorFactory
            .fromResource(R.drawable.shared_loc_user_header_logo);
    BitmapDescriptor mLocationHeader = BitmapDescriptorFactory
            .fromResource(R.drawable.shared_loc_address);

    BitmapDescriptor mMyHeaderLogo;         //我的头像
    BitmapDescriptor mOtherHeaderLogo;      //另一个的头像

    private View mMyHeaderView;
    private CircleImageView mMyHeaderIv;

    private View mOtherHeaderView;
    private CircleImageView mOtherHeaderIv;

    private boolean shouldRefresh = false;  //标记是否应该获取位置
    private boolean isFirstLoc = true;  //是否首次定位
//    private MyLocationData locData; //我的位置对象

    //    float a = 0.005f;
    private String mOtherLogo;
    private String mMyLogo;
    private int mInviteId;

    private double myLocationLatitude;   //我的GCJ坐标,获取对方位置的时候需要
    private double myLocationLongtitude;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_OTHER_LOCATION:
                    if (shouldRefresh) {
                        ALog.i("获取对放的位置!!!!!!");
                        requestOtherLocation();
                        mHandler.sendEmptyMessageDelayed(REFRESH_OTHER_LOCATION, REFRESH_OTHER_LOCTIME);
                    }
                    break;
                case REFRESH_MY_LOCATION:
//                    if (shouldRefresh) {
//                        setMyLocation();
//                        mHandler.sendEmptyMessageDelayed(REFRESH_MY_LOCATION, REFRESH_MY_LOCTIME);
//                    }
                    break;
            }
        }
    };

    @Override
    protected int initContentView() {
        return R.layout.activity_shared_location;
    }

    @Override
    protected void initView() {
        mSharedLocCancel = (ImageView) findViewById(R.id.shared_loc_cancel);
        mBmapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mBmapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();

        mMyHeaderView = View.inflate(this, R.layout.user_header_view, null);
        mMyHeaderIv = (CircleImageView) mMyHeaderView.findViewById(R.id.user_header_view);

        mOtherHeaderView = View.inflate(this, R.layout.user_header_view, null);
        mOtherHeaderIv = (CircleImageView) mOtherHeaderView.findViewById(R.id.user_header_view);

//        mMyHeaderLogo = BitmapDescriptorFactory.fromView(mMyHeaderView);
//        mOtherHeaderLogo = BitmapDescriptorFactory.fromView(mOtherHeaderView);
    }

    @Override
    protected void initListener() {
        mSharedLocCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
//        EventBus.getDefault().register(this);

        Intent intent = getIntent();
        double storeLatitude = intent.getDoubleExtra("storeLatitude", 0);
        double storeLonggitude = intent.getDoubleExtra("storeLonggitude", 0);
        double otherLatitude = intent.getDoubleExtra("otherLatitude", 39.945);
        double otherLonggitude = intent.getDoubleExtra("otherLonggitude", 116.404);
        String stringInviteId = intent.getStringExtra("inviteId");
        mInviteId = Integer.parseInt(stringInviteId);
        mOtherLogo = intent.getStringExtra("otherLogo");
        mMyLogo = intent.getStringExtra("myLogo");

        mLocationLoc = LocationToBaiduMap.toBaiduMapLocation(LocationToBaiduMap.MapType.NORMAL, storeLatitude, storeLonggitude);
        mOtherLoc = LocationToBaiduMap.toBaiduMapLocation(LocationToBaiduMap.MapType.NORMAL, otherLatitude, otherLonggitude);

        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(GEO_BEIJING, 10.0f);
        mBaiduMap.setMapStatus(msu);

        getMyLocation();

        mUiSettings.setRotateGesturesEnabled(false);
        mUiSettings.setOverlookingGesturesEnabled(false);

        initAddressOverlay(mLocationLoc); //初始化地址的标记
        upChangeOtherOverlay(); //初始化对方的位置

        shouldRefresh = true;
        mHandler.sendEmptyMessageDelayed(REFRESH_OTHER_LOCATION, REFRESH_OTHER_LOCTIME);
        mHandler.sendEmptyMessageDelayed(REFRESH_MY_LOCATION, REFRESH_MY_LOCTIME);
    }

    private void requestOtherLocation() {
        SocketSendBean socketSendBean =
                NSMTypeUtils.getSocketSendBean(NSMTypeUtils.RequestType.TARGET,
                        myLocationLatitude, myLocationLongtitude, mInviteId);
        EventBus.getDefault().post(socketSendBean);
//        String sendBean = gson.toJson(socketSendBean);
//        ALog.i(sendBean);
//        try {
//            App.getClientSocket().addSendMsgToQueue(sendBean);
//        } catch (Exception e) {
//            ALog.i("失败了");
//            e.printStackTrace();
//        }
    }

    public void onEventMainThread(SocketResultBean socketResultBean) {
        if (socketResultBean.getType().equals("pushlocation")) {    //推送位置
            SocketResultBean.DataEntity dataEntity = socketResultBean.getData();
            if (null != dataEntity) {
                ALog.i("获取对方的位置成功 ,对方 的信息是 : " + socketResultBean.toString());
                int inviteId = dataEntity.getInviteId();
                if (inviteId != this.mInviteId) {
                    return;
                }
                mOtherLoc = LocationToBaiduMap.toBaiduMapLocation(
                        LocationToBaiduMap.MapType.NORMAL, dataEntity.getLatitude(), dataEntity.getLongitude());
                upChangeOtherOverlay();
                mCurrentGetNumber = 1;
            } else {
                mCurrentGetNumber++;
                showToastInfo("对方暂时不在线,正在重新尝试第" + mCurrentGetNumber + "次获取对方位置");
            }
        } else {
            if (!socketResultBean.getMessage().equalsIgnoreCase("success")) {
                mCurrentGetNumber++;
                showToastInfo("对方暂时不在线,正在重新尝试第" + mCurrentGetNumber + "次获取对方位置");
            }
        }
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (null != location && location.getLocType() != BDLocation.TypeServerError) {

//                Gps gps = PositionUtil.gcj02_To_Bd09(location.getLatitude(), location.getLongitude());
//                mMyLoc = new LatLng(gps.getWgLat(), gps.getWgLon());
                mMyLoc = LocationToBaiduMap.toBaiduMapLocation(
                        LocationToBaiduMap.MapType.NORMAL, location.getLatitude(), location.getLongitude());

                myLocationLatitude = location.getLatitude();
                myLocationLongtitude = location.getLongitude();

                setMyLocation();

                if (isFirstLoc) {
                    isFirstLoc = false;
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(mMyLoc).zoom(19.0f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    private void getMyLocation() {
        mLocationClient = new LocationClient(this);     //声明LocationClient类
        mLocationClient.registerLocationListener(mListener);    //注册监听函数

        initLocation();

        mLocationClient.start();
//        LocationUtils.getLocationByBD0911Once(new LocationUtils.OnGetLocationListener() {
//            @Override
//            public void onGetLocation(double latitude, double longgitude, BDLocation location) {
//                if (location == null || mBaiduMap == null) {
//                    return;
//                }
//
////                mMyLoc = LocationToBaiduMap.toBaiduMapLocation(LocationToBaiduMap.MapType.NORMAL, latitude, longgitude);
//                mMyLoc = new LatLng(location.getLatitude(),
//                        location.getLongitude());
//
//                Gps gps = PositionUtil.bd09_To_Gcj02(location.getLatitude(), location.getLongitude());
//                myLocationLatitude = gps.getWgLat();
//                myLocationLongtitude = gps.getWgLon();
//
//                setMyLocation();
//
//                if (isFirstLoc) {
//                    isFirstLoc = false;
////                    double[] allLatitude = {mMyLoc.latitude, mLocationLoc.latitude, mOtherLoc.latitude};
////                    double[] allLongitude = {mMyLoc.longitude, mLocationLoc.longitude, mOtherLoc.longitude};
////                    Arrays.sort(allLatitude);
////                    Arrays.sort(allLongitude);
////                    LatLng center = new LatLng((allLatitude[2] + allLatitude[0]) / 2, (allLongitude[2] + allLongitude[0]) / 2);
//                    MapStatus.Builder builder = new MapStatus.Builder();
//                    builder.target(mMyLoc).zoom(19.0f);
//                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//                }
////                upChangeOtherOverlay(new LatLng(mOtherLoc.latitude + a, mOtherLoc.longitude + a));
////                a += 0.001;
////                mOtherRotate += 30;
////                if (mOtherRotate > 360) {
////                    mOtherRotate = 0;
////                }
////                else {
////                    MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(center, 14.0f);
////                    mBaiduMap.setMapStatus(msu);
////                    upChangeOtherOverlay(new LatLng(mOtherLoc.latitude + a, mOtherLoc.longitude + a));
////                    a += 0.001;
////                    mOtherRotate += 30;
////                }
//            }
//
//            @Override
//            public void onGetError() {
//
//            }
//        });
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系 bd09ll
        option.setScanSpan(4 * 1000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(true);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    private void setMyLocation() {
        if (mMyHeaderMarker == null) {
            MarkerOptions headerPoint = new MarkerOptions().position(mMyLoc).icon(mMyHeader)
                    .zIndex(9).anchor(X_ANCHOR, Y_ANCHOR)
//                    .rotate(location.getDirection())  //方向,不要了
                    ;
            mMyHeaderMarker = (Marker) (mBaiduMap.addOverlay(headerPoint));

            HttpLoader.getImageLoader().get(mMyLogo, ImageLoader.getImageListener(mMyHeaderIv,
                    R.drawable.picture_moren, R.drawable.picture_moren, new ImageLoader.BitmapListener() {
                        @Override
                        public void onResponse(ImageLoader.ImageContainer response) {
                            mMyHeaderLogo = BitmapDescriptorFactory.fromView(mMyHeaderView);
                            MarkerOptions headerLogo = new MarkerOptions().position(mMyLoc).icon(mMyHeaderLogo)
                                    .zIndex(7).anchor(1.0f, 1.0f);
                            mMyLogoMarker = (Marker) (mBaiduMap.addOverlay(headerLogo));
                        }
                    }));

        } else {
            mMyHeaderMarker.setPosition(mMyLoc);
//            mMyHeaderMarker.setRotate(location.getDirection());
            if (mMyLogoMarker != null)
                mMyLogoMarker.setPosition(mMyLoc);
            ALog.i("更新自己位置成功!");
        }
    }

    private void initAddressOverlay(LatLng ll) {
        MarkerOptions ooA = new MarkerOptions().position(ll).icon(mLocationHeader)
                .zIndex(10);
        mLocationMarker = (Marker) (mBaiduMap.addOverlay(ooA));
    }

    private void upChangeOtherOverlay() {
        if (mOtherHeaderMarker == null) {
            MarkerOptions headerPoint = new MarkerOptions().position(mOtherLoc).icon(mOtherHeader)
                    .zIndex(8).anchor(X_ANCHOR, Y_ANCHOR)
//                    .rotate(mOtherRotate)   //方向,不用了
                    ;
            mOtherHeaderMarker = (Marker) (mBaiduMap.addOverlay(headerPoint));

            HttpLoader.getImageLoader().get(mOtherLogo, ImageLoader.getImageListener(mOtherHeaderIv,
                    R.drawable.picture_moren, R.drawable.picture_moren, new ImageLoader.BitmapListener() {
                        @Override
                        public void onResponse(ImageLoader.ImageContainer response) {
                            mOtherHeaderLogo = BitmapDescriptorFactory.fromView(mOtherHeaderView);
                            MarkerOptions headerLogo = new MarkerOptions().position(mOtherLoc).icon(mOtherHeaderLogo)
                                    .zIndex(6).anchor(1.0f, 1.0f);
                            mOtherLogoMarker = (Marker) (mBaiduMap.addOverlay(headerLogo));
                        }
                    }));
        } else {
            mOtherHeaderMarker.setPosition(mOtherLoc);
//            mOtherHeaderMarker.setRotate(mOtherRotate);
            if (mOtherLogoMarker != null)
                mOtherLogoMarker.setPosition(mOtherLoc);
            ALog.i("更新对方位置成功!");
        }
    }

    @Override
    protected void onPause() {
        mBmapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mBmapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
//        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.unRegisterLocationListener(mListener); //注销掉监听
        mLocationClient.stop();
        EventBus.clearCaches();
        EventBus.getDefault().unregister(this);
        mBmapView.onDestroy();
        mBmapView = null;
        super.onDestroy();
        mMyHeader.recycle();
        mOtherHeader.recycle();
        mLocationHeader.recycle();
        mMyHeaderLogo.recycle();
        mOtherHeaderLogo.recycle();
        shouldRefresh = false;
        mHandler.removeMessages(REFRESH_OTHER_LOCATION);
        mHandler.removeMessages(REFRESH_MY_LOCATION);
    }

    /**
     * 开启用户详情界面方法
     *
     * @param context
     */
    public static void startSharedLocationAct(Context context, double storeLatitude, double storeLonggitude,
                                              double otherLatitude, double otherLonggitude,
                                              String otherLogo, String myLogo, String inviteId) {
        Intent intent = new Intent(context, SharedLocationOldActivity.class);
        intent.putExtra("storeLatitude", storeLatitude);
        intent.putExtra("storeLonggitude", storeLonggitude);
        intent.putExtra("otherLatitude", otherLatitude);
        intent.putExtra("otherLonggitude", otherLonggitude);
        intent.putExtra("otherLogo", otherLogo);
        intent.putExtra("myLogo", myLogo);
        intent.putExtra("inviteId", inviteId);
        context.startActivity(intent);
    }
}
