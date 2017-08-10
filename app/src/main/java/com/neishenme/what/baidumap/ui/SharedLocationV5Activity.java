package com.neishenme.what.baidumap.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Circle;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.neishenme.what.R;
import com.neishenme.what.baidumap.utils.LocationToBaiduMap;
import com.neishenme.what.baidumap.utils.Utils;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.SocketResultBean;
import com.neishenme.what.bean.SocketSendBean;
import com.neishenme.what.utils.NSMTypeUtils;

import org.seny.android.utils.ALog;

import java.util.LinkedList;

import de.greenrobot.event.EventBus;

import static com.baidu.mapapi.map.MapStatusUpdateFactory.newLatLngBounds;

/**
 * 这个 界面是位置共享界面  为 v5.1.7版本  的新版位置共享界面  旧的 界面为
 * {@link SharedLocationActivity} ,那个界面已经弃用
 * <p>
 * Created by zhaozh on 2017/4/24.
 */

public class SharedLocationV5Activity extends BaseActivity {
    private static final int REFRESH_OTHER_LOCATION = 1;    //handler发送应该刷新对方位置的值
    private static final int REFRESH_OTHER_LOCTIME = 5 * 1000;    //获取对方的位置的时间
    private static final String COLOR_MY_STROKE = "#006cff";
    private static final String COLOR_MY_FILTER = "#55006cff";
    private static final String COLOR_OTHER_STROKE = "#ff0000";
    private static final String COLOR_OTHER_FILTER = "#55ff0000";

    // 存放历史定位结果的链表，最大存放当前结果的前5次定位结果
    private LinkedList<LocationEntity> locationList = new LinkedList<LocationEntity>();
    private int iscalculate = 0;    //标记平滑处理的int值

    private ImageView mSharedLocCancel;

    private MapView mBmapView;
    private BaiduMap mBaiduMap;
    private UiSettings mUiSettings;

    //餐厅地址 和图标
    private LatLng mLocationLoc;
    private Marker mLocationMarker;
    BitmapDescriptor mLocationHeader = BitmapDescriptorFactory
            .fromResource(R.drawable.shared_loc_address);

    //另一个人地址 和 圆圈
    private LatLng mOtherLoc;
    private Circle mOtherCircleMarker;

    //当前我的位置坐标
    private LatLng mMyLoc;
    //    private Circle mMyCircleMarker;
    private Marker mMyHeaderMarker; //当前我的图标
    BitmapDescriptor mMyHeader = BitmapDescriptorFactory
            .fromResource(R.drawable.shared_loc_user_header_logo);

    private int mLastMyDistence = 0;
    private int mLastOtherDistence = 0;

    public LocationClient mLocationClient = null;

    private boolean shouldRefresh = false;  //标记是否应该获取位置
    private boolean shouldShowOther = true;  //标记是否应该展示对方的位置,默认展示

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
            }
        }
    };

    @Override
    protected int initContentView() {
        return R.layout.activity_shared_location_v5;
    }

    @Override
    protected void initView() {
        mSharedLocCancel = (ImageView) findViewById(R.id.shared_loc_cancel);
        mBmapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mBmapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();
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
        Intent intent = getIntent();
        double storeLatitude = intent.getDoubleExtra("storeLatitude", 0);
        double storeLonggitude = intent.getDoubleExtra("storeLonggitude", 0);
        double otherLatitude = intent.getDoubleExtra("otherLatitude", 0);
        double otherLonggitude = intent.getDoubleExtra("otherLonggitude", 0);
        String stringInviteId = intent.getStringExtra("inviteId");
        mInviteId = Integer.parseInt(stringInviteId);
        shouldShowOther = intent.getBooleanExtra("showOther", true);
//        mOtherLogo = intent.getStringExtra("otherLogo");
//        mMyLogo = intent.getStringExtra("myLogo");

        if (shouldShowOther)
            EventBus.getDefault().register(this);

        mUiSettings.setRotateGesturesEnabled(false);
        mUiSettings.setOverlookingGesturesEnabled(false);

        /* 初始化约会地点标记 */
        mLocationLoc = LocationToBaiduMap.toBaiduMapLocation(LocationToBaiduMap.MapType.NORMAL, storeLatitude, storeLonggitude);
        setAddressLocation(); //初始化地址的标记

        //设置 中心点 , 以约会地点为 中心
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(mLocationLoc, 17.0f);
        mBaiduMap.setMapStatus(msu);
//        MapStatus.Builder builder = new MapStatus.Builder();
//        builder.target(mLocationLoc).zoom(15.0f);
//        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        getMyLocation();

        if (shouldShowOther) {
            /* 如果可以的话初始化对方位置的标记,如果对方的位置都不为空才初始化,否则不添加 */
            if (otherLatitude != 0 && otherLonggitude != 0) {
                mOtherLoc = LocationToBaiduMap.toBaiduMapLocation(LocationToBaiduMap.MapType.NORMAL, otherLatitude, otherLonggitude);
                setOtherLocation(); //初始化对方的位置
            }
            shouldRefresh = true;
            mHandler.sendEmptyMessageDelayed(REFRESH_OTHER_LOCATION, REFRESH_OTHER_LOCTIME);
        }
    }

    private void requestOtherLocation() {
        SocketSendBean socketSendBean =
                NSMTypeUtils.getSocketSendBean(NSMTypeUtils.RequestType.TARGET,
                        myLocationLatitude, myLocationLongtitude, mInviteId);
        EventBus.getDefault().post(socketSendBean);
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                Algorithm(location);
                mMyLoc = LocationToBaiduMap.toBaiduMapLocation(
                        LocationToBaiduMap.MapType.NORMAL, location.getLatitude(), location.getLongitude());
                myLocationLatitude = location.getLatitude();
                myLocationLongtitude = location.getLongitude();
                setMyLocation();
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
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系 bd09ll
        option.setScanSpan(2000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mLocationClient.setLocOption(option);
    }

    /* 设置 约会地点 的位置 */
    private void setAddressLocation() {
        MarkerOptions ooA = new MarkerOptions().position(mLocationLoc).icon(mLocationHeader)
                .zIndex(7);
        mLocationMarker = (Marker) (mBaiduMap.addOverlay(ooA));
    }

    /* 设置 我自己 的位置 */
    private void setMyLocation() {
        int distance = (int) Math.floor(DistanceUtil.getDistance(mMyLoc, mLocationLoc));
        if (distance == -1) {
            return;
        }
        if (mMyHeaderMarker == null) {
            MarkerOptions headerPoint = new MarkerOptions()
                    .position(mMyLoc)
                    .icon(mMyHeader)
                    .anchor(0.5f, 0.5f)
                    .zIndex(9);
            mMyHeaderMarker = (Marker) (mBaiduMap.addOverlay(headerPoint));
            resetMapCenter(distance);
        } else {
            if (Math.abs(distance - mLastMyDistence) > 5) {
                mLastMyDistence = distance;
                mMyHeaderMarker.setPosition(mMyLoc);
                ALog.i("更新自己位置成功!");
            }
        }

//        if (mMyCircleMarker == null) {
//            OverlayOptions ooCircle = new CircleOptions()
//                    .fillColor(Color.parseColor(COLOR_MY_FILTER))  //填充颜色,透明
//                    .zIndex(9)              //图层
//                    .center(mLocationLoc)   //中心点
//                    .stroke(new Stroke(5, Color.parseColor(COLOR_MY_STROKE)))  //圆圈颜色
//                    .radius(distance);
//            mMyCircleMarker = (Circle) mBaiduMap.addOverlay(ooCircle);
//            resetMapCenter(distance);
//        } else {
//            if (Math.abs(distance - mLastMyDistence) > 5) {
//                mLastMyDistence = distance;
//                mMyCircleMarker.setRadius(distance);
//                if (mMyHeaderMarker != null) {
//                    mMyHeaderMarker.setPosition(mMyLoc);
//                }
//                ALog.i("更新自己位置成功!");
//            }
//        }
    }

    /* 设置 对方 的位置 */
    private void setOtherLocation() {
        int distance = (int) DistanceUtil.getDistance(mOtherLoc, mLocationLoc);
        if (distance == -1) {
            return;
        }
        if (mOtherCircleMarker == null) {
            OverlayOptions ooCircle = new CircleOptions()
                    .fillColor(Color.parseColor(COLOR_OTHER_FILTER))  //填充颜色,透明
                    .zIndex(8)              //图层
                    .center(mLocationLoc)   //中心点
                    .stroke(new Stroke(5, Color.parseColor(COLOR_OTHER_STROKE)))  //圆圈颜色
                    .radius(distance);
            mOtherCircleMarker = (Circle) mBaiduMap.addOverlay(ooCircle);
            resetMapCenter(distance);
//            mLastOtherDistence = distance;
//            if (mLastOtherDistence > mLastMyDistence) {
//                if ()
//                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo());
//            }
        } else {
            if (Math.abs(distance - mLastOtherDistence) > 5) {
                mLastOtherDistence = distance;
                mOtherCircleMarker.setRadius(distance);
                ALog.i("更新对方位置成功!");
            }
        }
    }

    //根据距离设置zoom
    private void resetMapCenter(int distance) {
        float now = mBaiduMap.getMapStatus().zoom;
        float will;
        if (distance < 800) {
            will = 16.4f;
        } else if (distance < 1400) {
            will = 15.6f;
        } else if (distance < 2200) {
            will = 15f;
        } else if (distance < 3200) {
            will = 14.4f;
        } else if (distance < 4500) {
            will = 13.9f;
        } else if (distance < 7000) {
            will = 13.3f;
        } else if (distance < 10000) {
            will = 12.8f;
        } else if (distance < 15000) {
            will = 12.3f;
        } else if (distance < 20000) {
            will = 11.8f;
        } else if (distance < 25000) {
            will = 11.5f;
        } else {
            will = 11;
        }
        if (will < now) {
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(will));
        }
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
                if (shouldShowOther)
                    setOtherLocation();
            }
        }
        if (socketResultBean.getType().equals("message") && socketResultBean.getCode() == 200) {
            if ("对方不在线".equals(socketResultBean.getMessage())) {
                if (mOtherCircleMarker != null) {
                    mOtherCircleMarker.remove();
                    mOtherCircleMarker = null;
                }
            }
        }
    }

    /***
     * 平滑策略代码实现方法，主要通过对新定位和历史定位结果进行速度评分，
     * 来判断新定位结果的抖动幅度，如果超过经验值，则判定为过大抖动，进行平滑处理,若速度过快，
     * 则推测有可能是由于运动速度本身造成的，则不进行低速平滑处理 ╭(●｀∀´●)╯
     *
     * @return Bundle
     */
    private void Algorithm(BDLocation location) {
        double curSpeed = 0;
        if (locationList.isEmpty() || locationList.size() < 2) {
            LocationEntity temp = new LocationEntity();
            temp.location = location;
            temp.time = System.currentTimeMillis();
            iscalculate = 0;
            locationList.add(temp);
        } else {
            if (locationList.size() > 5)
                locationList.removeFirst();
            double score = 0;
            for (int i = 0; i < locationList.size(); ++i) {
                LatLng lastPoint = new LatLng(locationList.get(i).location.getLatitude(),
                        locationList.get(i).location.getLongitude());
                LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
                double distance = DistanceUtil.getDistance(lastPoint, curPoint);
                curSpeed = distance / (System.currentTimeMillis() - locationList.get(i).time) / 1000;
                score += curSpeed * Utils.EARTH_WEIGHT[i];
            }
            if (score > 0.00000999 && score < 0.00005) { // 经验值,开发者可根据业务自行调整，也可以不使用这种算法
                location.setLongitude(
                        (locationList.get(locationList.size() - 1).location.getLongitude() + location.getLongitude())
                                / 2);
                location.setLatitude(
                        (locationList.get(locationList.size() - 1).location.getLatitude() + location.getLatitude())
                                / 2);
                iscalculate = 1;
            } else {
                iscalculate = 0;
            }
            LocationEntity newLocation = new LocationEntity();
            newLocation.location = location;
            newLocation.time = System.currentTimeMillis();
            locationList.add(newLocation);
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
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(mListener); //注销掉监听
        EventBus.clearCaches();
        EventBus.getDefault().unregister(this);
        mBmapView.onDestroy();
        mBmapView = null;
        super.onDestroy();
        if (mLocationHeader != null)
            mLocationHeader.recycle();
        if (mMyHeader != null)
            mMyHeader.recycle();
        if (mLocationMarker != null) {
            mLocationMarker.remove();
        }
        if (mMyHeaderMarker != null) {
            mMyHeaderMarker.remove();
        }
//        if (mMyCircleMarker != null) {
//            mMyCircleMarker.remove();
//        }
        if (mOtherCircleMarker != null) {
            mOtherCircleMarker.remove();
        }
        shouldRefresh = false;
        mHandler.removeMessages(REFRESH_OTHER_LOCATION);
    }

    /**
     * 封装定位结果和时间的实体类
     *
     * @author baidu
     */
    class LocationEntity {
        BDLocation location;
        long time;
    }

    /**
     * 开启用户详情界面方法
     *
     * @param context
     */
    public static void startSharedLocationAct(Context context, double storeLatitude, double storeLonggitude,
                                              double otherLatitude, double otherLonggitude,
                                              String otherLogo, String myLogo, String inviteId) {
        Intent intent = new Intent(context, SharedLocationV5Activity.class);
        intent.putExtra("storeLatitude", storeLatitude);
        intent.putExtra("storeLonggitude", storeLonggitude);
        intent.putExtra("otherLatitude", otherLatitude);
        intent.putExtra("otherLonggitude", otherLonggitude);
        intent.putExtra("otherLogo", otherLogo);
        intent.putExtra("myLogo", myLogo);
        intent.putExtra("inviteId", inviteId);
        context.startActivity(intent);
    }

    public static void startSharedLocationAct(Context context, double storeLatitude, double storeLonggitude,
                                              double otherLatitude, double otherLonggitude,
                                              String otherLogo, String myLogo, String inviteId,
                                              boolean showOther) {
        Intent intent = new Intent(context, SharedLocationV5Activity.class);
        intent.putExtra("storeLatitude", storeLatitude);
        intent.putExtra("storeLonggitude", storeLonggitude);
        intent.putExtra("otherLatitude", otherLatitude);
        intent.putExtra("otherLonggitude", otherLonggitude);
        intent.putExtra("otherLogo", otherLogo);
        intent.putExtra("myLogo", myLogo);
        intent.putExtra("inviteId", inviteId);
        intent.putExtra("showOther", showOther);
        context.startActivity(intent);
    }
}
