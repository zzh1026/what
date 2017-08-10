package com.neishenme.what.baidumap.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.neishenme.what.R;
import com.neishenme.what.baidumap.NSMMapHelper;
import com.neishenme.what.baidumap.service.LocationService;
import com.neishenme.what.baidumap.utils.LocationToBaiduMap;
import com.neishenme.what.baidumap.utils.OverlayManager;
import com.neishenme.what.baidumap.utils.TransitRouteOverlay;
import com.neishenme.what.baidumap.utils.WalkingRouteOverlay;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.utils.LocationUtils;

import org.seny.android.utils.ALog;

import static com.neishenme.what.activity.ZXingGetRichActivity.result;

/**
 * 作者：zhaozh create on 2016/5/26 17:19
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class RestaurantHowToGoActivity extends BaseActivity implements OnGetRoutePlanResultListener {
    private LatLng startPlace;
    boolean isFirstLoc = false;

    RouteLine route = null;

    MapView mMapView = null;    // 地图View
    BaiduMap mBaidumap = null;

    // 搜索相关
    RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    OverlayManager routeOverlay = null;

    // 天安门坐标
    double mLat1 = 39.915291;
    double mLon1 = 116.403857;

    private String city;
    private LatLng endPlace;

    private ImageView mIvBack;

    @Override
    protected int initContentView() {
        return R.layout.activity_restaurant_howtogo;
    }

    @Override
    protected void initView() {
        // 初始化地图
        mMapView = (MapView) findViewById(R.id.map);
        mBaidumap = mMapView.getMap();

        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);

        mIvBack = (ImageView) findViewById(R.id.iv_back);
    }

    @Override
    protected void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        isFirstLoc = true;
        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude", mLat1);
        double longgitude = intent.getDoubleExtra("longgitude", mLon1);
        endPlace = LocationToBaiduMap.toBaiduMapLocation(LocationToBaiduMap.MapType.NORMAL, latitude, longgitude);

        LocationUtils.getLocationByBD0911Once(new LocationUtils.OnGetLocationListener() {
            @Override
            public void onGetLocation(double latitude, double longgitude, BDLocation location) {
                startPlace = new LatLng(location.getLatitude(), location.getLongitude());
                //规划路径
                city = location.getCity();
                makeSureRote();

                if (isFirstLoc) {
                    isFirstLoc = false;
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(startPlace).zoom(19.0f);
                    mBaidumap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            }

            @Override
            public void onGetError() {
                showToastError("获取位置失败");
            }
        });
    }

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    private void makeSureRote() {
        mBaidumap.clear();
        PlanNode stNode = PlanNode.withLocation(startPlace);
        PlanNode enNode = PlanNode.withLocation(endPlace);

        mSearch.walkingSearch((new WalkingRoutePlanOption())
                .from(stNode).to(enNode));
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {

            if (result.getRouteLines().size() >= 1) {
                // 直接显示
                route = result.getRouteLines().get(0);
                WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
                mBaidumap.setOnMarkerClickListener(overlay);
                routeOverlay = overlay;
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

            } else {
                Log.d("route result", "结果数<0");
                return;
            }

        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            showToastError("抱歉，未找到结果");
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            route = result.getRouteLines().get(0);
            TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
            mBaidumap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.route_start);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.route_end);
        }
    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.route_start);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.route_end);
        }
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mSearch.destroy();
        mMapView.onDestroy();
        super.onDestroy();
    }

    /**
     * 开启用户详情界面方法
     *
     * @param context
     * @param latitude
     * @param longgitude
     */
    public static void startRestHowToGoAct(Context context, double latitude, double longgitude) {
        Intent intent = new Intent(context, RestaurantHowToGoActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longgitude", longgitude);
        context.startActivity(intent);
    }
}
