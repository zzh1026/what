package com.neishenme.what.baidumap.ui;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.neishenme.what.R;
import com.neishenme.what.adapter.SelectedPOIAdapter;
import com.neishenme.what.baidumap.commonmap.PlaceCityInfoConfig;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.common.CityLocationConfig;
import com.neishenme.what.utils.Gps;
import com.neishenme.what.utils.LocationUtils;
import com.neishenme.what.utils.PositionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类的作用是选择地址的地图界面
 * <p>
 * Created by zhaozh on 2016/12/13.
 */

public class SelectedPlaceActivity extends BaseActivity implements OnGetPoiSearchResultListener, OnGetGeoCoderResultListener {
    private ImageView mSelectedPlaceBack;
    private EditText mSelectedPlaceSearch;
    private ImageView mSelectedPlaceSearchClear;
    private ListView mSelectedPlacePoi;
    private ListView mSelectedPlaceSearchResult;

    //地图和标注物相关
    private MapView mBmapView;
    private BaiduMap mBaiduMap;
    private UiSettings mUiSettings;
    private Marker mMarker;

    //检索相关
    private PoiSearch mPoiSearch = null;
    //    private SuggestionSearch mSuggestionSearch = null;
    private GeoCoder mSearch = null; // 地址编译相关
    private PoiInfo firstInfo = new PoiInfo();
    private List<PoiInfo> poiLists;     //地图移动的poi
    private List<PoiInfo> allPoiInfos;  //搜索的poi
    private String nowCity = CityLocationConfig.cityLocation + "市";
    private BDLocation location;    //标记定位的地址location

    BitmapDescriptor bd = BitmapDescriptorFactory
            .fromResource(R.drawable.selected_place_maker);

    private SelectedPOIAdapter mSelectedPOIAdapter; //移动地图时候周边结果的适配器
    private SelectedPOIAdapter mSearchPOIAdapter; //输入地点查询结果的适配器
    private LatLng myLatLng;    //我的位置

    // 天安门坐标
    LatLng defaultLatLng = new LatLng(39.915291, 116.403857);

    @Override
    protected int initContentView() {
        return R.layout.activity_selected_place;
    }

    @Override
    protected void initView() {
        mSelectedPlaceBack = (ImageView) findViewById(R.id.selected_place_back);
        mSelectedPlaceSearch = (EditText) findViewById(R.id.selected_place_search);
        mSelectedPlaceSearchClear = (ImageView) findViewById(R.id.selected_place_search_clear);
        mSelectedPlaceSearchResult = (ListView) findViewById(R.id.selected_place_search_result);
        mBmapView = (MapView) findViewById(R.id.bmapView);
        mSelectedPlacePoi = (ListView) findViewById(R.id.selected_place_poi);
        mBaiduMap = mBmapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();
    }

    @Override
    protected void initListener() {
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                if (mMarker != null) {
                    LatLng target = mapStatus.target;
                    mMarker.setPosition(target);
                }
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                LatLng target = mapStatus.target;
                //反编译
                reverseGeoCode(target);
            }
        });

        mSelectedPlaceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSelectedPlaceSearchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedPlaceSearch.setText("");
                mSelectedPlaceSearchResult.setVisibility(View.GONE);
                mSelectedPlaceSearchClear.setVisibility(View.GONE);
            }
        });

        mSelectedPlacePoi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (poiLists != null) {
                    PoiInfo poiInfo = poiLists.get(position);
                    setResultInfo(poiInfo);
                }
            }
        });

        mSelectedPlaceSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (allPoiInfos != null) {
                    PoiInfo poiInfo = allPoiInfos.get(position);
                    String city = poiInfo.city;//获取搜索选择的城市
                    //当前城市与搜索城市比较
                    if (nowCity.equals(city)) {
                        setResultInfo(poiInfo);
                    } else {
                        showToastWarning("异地不能发单");
                    }

                }
            }
        });

        mSelectedPlaceSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString().trim();
                if (TextUtils.isEmpty(keyword)) {
                    mSelectedPlaceSearchClear.setVisibility(View.GONE);
                    mSelectedPlaceSearchResult.setVisibility(View.GONE);
                } else {
                    mSelectedPlaceSearchClear.setVisibility(View.VISIBLE);
                    mSelectedPlaceSearchResult.setVisibility(View.VISIBLE);
                    mPoiSearch.searchInCity((new PoiCitySearchOption())
                            .city(nowCity).keyword(keyword).pageNum(0).pageCapacity(20));
                }
            }
        });
    }

    @Override
    protected void initData() {
        poiLists = new ArrayList<>();

        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(defaultLatLng, 12f));

        mBaiduMap.setMyLocationEnabled(true);
        getMyLocation();

        mUiSettings.setRotateGesturesEnabled(false);
        mUiSettings.setOverlookingGesturesEnabled(false);

        //初始化搜索模块
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
//        //  初始化建议搜索模块，注册建议搜索事件监听
//        mSuggestionSearch = SuggestionSearch.newInstance();
//        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
    }

    private void getMyLocation() {
        LocationUtils.getLocationByBD0911Once(new LocationUtils.OnGetLocationListener() {
            @Override
            public void onGetLocation(double latitude, double longgitude, BDLocation location) {
                if (location == null || mBaiduMap == null) {
                    return;
                }

                SelectedPlaceActivity.this.location = location;
                if (!TextUtils.isEmpty(location.getCity()))
                    nowCity = location.getCity();

                myLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                setMyLocation(location);

                setMapStatus();
//                mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
//                        MyLocationConfiguration.LocationMode.COMPASS, true, null));
            }

            @Override
            public void onGetError() {

            }
        });
    }

    //设置地图中心位置
    private void setMapStatus() {
        if (location == null) {
            return;
        }
        LatLng ll = new LatLng(location.getLatitude(),
                location.getLongitude());
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(16.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        reverseGeoCode(ll);
        initOverlay(ll);
    }

    private void setMyLocation(BDLocation location) {
        MyLocationData locData = new MyLocationData.Builder()
//                .accuracy(location.getRadius())
//                .direction(location.getDirection())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);
    }

    private void reverseGeoCode(LatLng target) {
        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(target));
    }

    private void initOverlay(LatLng ll) {
        if (mMarker == null) {
            MarkerOptions ooA = new MarkerOptions().position(ll).icon(bd)
                    .zIndex(5).draggable(false);
            ooA.animateType(MarkerOptions.MarkerAnimateType.grow);
            mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
        }
    }

    private void setResultInfo(PoiInfo poiInfo) {
//        Gps gps = PositionUtil.bd09_To_Gps84(poiInfo.location.latitude, poiInfo.location.longitude);
        //这里同一修改成了高德地图的坐标(gcj)()
        Gps gps = PositionUtil.bd09_To_Gcj02(poiInfo.location.latitude, poiInfo.location.longitude);
        String address = poiInfo.address;
        if (TextUtils.isEmpty(address) || TextUtils.isEmpty(address.trim())) {
            address = poiInfo.name;
        }
        String showAddress;
        if (address.contains("路")) {
            showAddress = address.substring(0, address.indexOf("路") + 1);
        } else if (address.contains("街")) {
            showAddress = address.substring(0, address.indexOf("街") + 1);
        } else {
            showAddress = address;
        }
        Intent intent = new Intent();
        intent.putExtra(PlaceCityInfoConfig.PLACE_NAME, poiInfo.name);
        intent.putExtra(PlaceCityInfoConfig.PLACE_SHOW_ADDRESS, showAddress);
        intent.putExtra(PlaceCityInfoConfig.PLACE_ADDRESS, address);
        intent.putExtra(PlaceCityInfoConfig.PLACE_CITY, nowCity);
        intent.putExtra(PlaceCityInfoConfig.PLACE_LATITUDE, String.valueOf(gps.getWgLat()));
        intent.putExtra(PlaceCityInfoConfig.PLACE_LONGITUDE, String.valueOf(gps.getWgLon()));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            showToastInfo("未找到结果");
            return;
        }
        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
            //这个代表在本市没有找到在其他市找到了,因为异地不能发单所以不行
//                || poiResult.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD
            allPoiInfos = poiResult.getAllPoi();
            if (null == allPoiInfos || allPoiInfos.size() == 0) {
                return;
            }
            if (mSearchPOIAdapter == null) {
                mSearchPOIAdapter = new SelectedPOIAdapter(this, allPoiInfos);
                mSelectedPlaceSearchResult.setAdapter(mSearchPOIAdapter);
            } else {
                mSearchPOIAdapter.updatePOIInfos(allPoiInfos);
                mSelectedPlacePoi.setSelection(0);
            }
        } else if (poiResult.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            showToastWarning("抱歉,异地暂时支持发单,您查找的位置不在本市,请换个地点吧");
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    //地址编译成坐标
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            showToastWarning("未能识别该地址,请重试");
            return;
        }
        if (reverseGeoCodeResult.getAddressDetail() == null) {
            showToastWarning("获取不到该地址,请重试");
            return;
        }
        if (!nowCity.equals(reverseGeoCodeResult.getAddressDetail().city)) {
            showToastWarning("异地不能发单");
            setMapStatus();
            return;
        }
        poiLists.clear();

        firstInfo.name = reverseGeoCodeResult.getAddressDetail().street;
        firstInfo.address = reverseGeoCodeResult.getAddress();
        firstInfo.location = reverseGeoCodeResult.getLocation();
        firstInfo.city = nowCity;
        poiLists.add(firstInfo);

        List<PoiInfo> poiListPOI = reverseGeoCodeResult.getPoiList();
        if (null != poiListPOI && poiListPOI.size() != 0) {
            poiLists.addAll(poiListPOI);
        }

        if (mSelectedPOIAdapter == null) {
            mSelectedPOIAdapter = new SelectedPOIAdapter(this, poiLists);
            mSelectedPlacePoi.setAdapter(mSelectedPOIAdapter);
        } else {
            mSelectedPOIAdapter.updatePOIInfos(poiLists);
            mSelectedPlacePoi.setSelection(0);
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
        if (null != myLatLng) {
            reverseGeoCode(myLatLng);
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mBaiduMap.setMyLocationEnabled(false);
        mBmapView.onDestroy();
        mBmapView = null;
        mPoiSearch.destroy();
        mPoiSearch = null;
//        mSuggestionSearch.destroy();
//        mSuggestionSearch = null;
        mSearch.destroy();
        mSearch = null;
        super.onDestroy();
        bd.recycle();
        bd = null;
    }
}
