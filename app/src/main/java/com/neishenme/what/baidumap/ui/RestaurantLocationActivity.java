package com.neishenme.what.baidumap.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.neishenme.what.R;
import com.neishenme.what.baidumap.utils.LocationToBaiduMap;
import com.neishenme.what.base.BaseActivity;

/**
 * 作者：zhaozh create on 2016/5/26 17:18
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class RestaurantLocationActivity extends BaseActivity {

    // 天安门坐标
    double mLat1 = 39.915291;
    double mLon1 = 116.403857;

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Marker mMarker;
    private InfoWindow mInfoWindow;

    private ImageView mIvBack;

    BitmapDescriptor bd = BitmapDescriptorFactory
            .fromResource(R.drawable.rest_location);
    private LatLng desLatLng;
    private String resteName;

    @Override
    protected int initContentView() {
        return R.layout.activity_restaurant_location;
    }

    @Override
    protected void initView() {
        mMapView = (MapView) findViewById(R.id.bmapView);

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

        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("longgitude", mLat1);
        double longgitude = intent.getDoubleExtra("latitude", mLon1);
        resteName = intent.getStringExtra("resteName");

        desLatLng = LocationToBaiduMap.toBaiduMapLocation(LocationToBaiduMap.MapType.NORMAL, latitude, longgitude);

        mBaiduMap = mMapView.getMap();

        //设置中心位置
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(desLatLng, 15.0f);
        mBaiduMap.setMapStatus(msu);

        initOverlay();

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                Button button = new Button(getApplicationContext());
                button.setBackgroundResource(R.drawable.popup);
                if (marker == mMarker) {
                    button.setText(resteName);
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            mBaiduMap.hideInfoWindow();
                        }
                    });
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(button, ll, -47);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                }
                return true;
            }
        });
    }

    private void initOverlay() {
        MarkerOptions ooA = new MarkerOptions().position(desLatLng).icon(bd)
                .zIndex(5).draggable(false);
        ooA.animateType(MarkerOptions.MarkerAnimateType.grow);
        mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity 销毁时同时销毁地图控件
        mMapView.onDestroy();
        bd.recycle();
    }

    /**
     * 开启用户详情界面方法
     *
     * @param context
     * @param latitude
     * @param longgitude
     */
    public static void startRestLocationAct(Context context, double latitude, double longgitude, String resteName) {
        Intent intent = new Intent(context, RestaurantLocationActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longgitude", longgitude);
        intent.putExtra("resteName", resteName);
        context.startActivity(intent);
    }
}
