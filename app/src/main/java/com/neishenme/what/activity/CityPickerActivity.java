package com.neishenme.what.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.neishenme.what.R;
import com.neishenme.what.adapter.CityPickerAllAdapter;
import com.neishenme.what.adapter.CityPickerHotAdapter;
import com.neishenme.what.adapter.CityPickerSearchAdapter;
import com.neishenme.what.baidumap.service.LocationService;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.CityPickerResponse;
import com.neishenme.what.bean.CitySearchResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.common.CityLocationConfig;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.LocationUtils;
import com.neishenme.what.view.GridViewAdjustHeight;
import com.neishenme.what.view.ListViewAdjustHeight;

import java.util.HashMap;
import java.util.List;

/**
 * 作者：zhaozh create
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个主界面点击选择城市进入城市选择的界面
 * .
 * 其作用是 :
 */
public class CityPickerActivity extends BaseActivity implements HttpLoader.ResponseListener, View.OnClickListener {
    public static final int GET_LOCATION_SUCCESS = 0;
    public static final int GET_LOCATION_ERROR = 1;
    public static final int GET_LOCATIONING = 2;
    private int getLocationState = GET_LOCATIONING;


    private ImageView mCityPickBack;
    private EditText mCityPickSearchText;
    private ImageView mCityPickSearchClear;
    private ScrollView mCityPickAllCityScroll;
    private TextView mCityPickCityNow;
    private TextView mCityPickCityLocation;
    private GridViewAdjustHeight mCityPickHotCity;
    private ListViewAdjustHeight mCityPickAllCity;
    private ListView mCityPickSearchCity;
    private LinearLayout mCityPickNoSearchCity;

    private List<CitySearchResponse.DataBean.CitylistBean> mSearchResultLists;  //搜索结果数据
    private List<CityPickerResponse.DataBean.HotCitylistBean> mHotCityLists;    //热门城市数据
    private CityPickerSearchAdapter mCityPickerSearchAdapter;   //搜索结果适配器

    @Override
    protected int initContentView() {
        return R.layout.activity_pick_city;
    }

    @Override
    protected void initView() {
        mCityPickBack = (ImageView) findViewById(R.id.city_pick_back);
        mCityPickSearchText = (EditText) findViewById(R.id.city_pick_search_text);
        mCityPickSearchClear = (ImageView) findViewById(R.id.city_pick_search_clear);
        mCityPickAllCityScroll = (ScrollView) findViewById(R.id.city_pick_all_city_scroll);
        mCityPickCityNow = (TextView) findViewById(R.id.city_pick_city_now);
        mCityPickCityLocation = (TextView) findViewById(R.id.city_pick_city_location);
        mCityPickHotCity = (GridViewAdjustHeight) findViewById(R.id.city_pick_hot_city);
        mCityPickAllCity = (ListViewAdjustHeight) findViewById(R.id.city_pick_all_city);
        mCityPickSearchCity = (ListView) findViewById(R.id.city_pick_search_city);
        mCityPickNoSearchCity = (LinearLayout) findViewById(R.id.city_pick_no_search_city);
    }

    @Override
    protected void initListener() {

        mCityPickBack.setOnClickListener(this);
        mCityPickSearchClear.setOnClickListener(this);
        mCityPickCityLocation.setOnClickListener(this);

        mCityPickHotCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CityPickerResponse.DataBean.HotCitylistBean hotCitylistBean = mHotCityLists.get(position);
                setLocation(hotCitylistBean.getId(), hotCitylistBean.getName());
            }
        });

        mCityPickSearchCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CitySearchResponse.DataBean.CitylistBean citylistBean = mSearchResultLists.get(position);
                setLocation(citylistBean.getId(), citylistBean.getName());
            }
        });

        mCityPickSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().trim();
                if (TextUtils.isEmpty(searchText)) {
                    mCityPickSearchClear.setVisibility(View.GONE);
                    mCityPickNoSearchCity.setVisibility(View.GONE);
                    mCityPickSearchCity.setVisibility(View.GONE);
                } else {
                    mCityPickSearchClear.setVisibility(View.VISIBLE);
                    mCityPickSearchCity.setVisibility(View.VISIBLE);
                    searchText(searchText);
                }
            }
        });
    }

    private void searchText(String searchText) {
        HashMap<String, String> params = new HashMap<>();
        params.put("search", searchText);
        HttpLoader.post(ConstantsWhatNSM.URL_ACTIVE_GET_CITY_SEARCH, params, CitySearchResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_GET_CITY_SEARCH, this, false).setTag(this);
    }

    @Override
    protected void initData() {
        mCityPickCityNow.setText(CityLocationConfig.citySelected);

        refreshLocation();
//        HashMap<String, String> params = new HashMap<>();
//        params.put("page", "0");
//        params.put("pageSize", "10");
//        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_ACTIVE_GET_CITY_LIST, null, CityPickerResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_GET_CITY_LIST, this, false).setTag(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city_pick_back:
                finish();
                break;
            case R.id.city_pick_search_clear:
                mCityPickSearchText.setText("");
                break;
            case R.id.city_pick_city_location:
                switch (getLocationState) {
                    case GET_LOCATION_ERROR:
                        refreshLocation();  //刷新定位位置
                        break;
                    case GET_LOCATION_SUCCESS:
                        setLocation(CityLocationConfig.cityLocationId, CityLocationConfig.cityLocation);
                        break;
                }
                break;
        }
    }

    private void refreshLocation() {
        mCityPickCityLocation.setText("正在定位");
        getLocationState = GET_LOCATIONING;
        LocationUtils.getLocation(new LocationUtils.OnGetLocationListener() {
            @Override
            public void onGetLocation(double latitude, double longgitude, BDLocation location) {
                if (TextUtils.isEmpty(location.getCity())) {
                    mCityPickCityLocation.setText("定位失败");
                    getLocationState = GET_LOCATION_ERROR;
                } else {
                    mCityPickCityLocation.setText(location.getCity());
                    getLocationState = GET_LOCATION_SUCCESS;
                }
            }

            @Override
            public void onGetError() {
                mCityPickCityLocation.setText("定位失败");
                getLocationState = GET_LOCATION_ERROR;
            }
        }, LocationService.CoorType.CoorType_BD09LL);
    }

    public void setLocation(int areaId, String cityName) {
        if (CityLocationConfig.citySelectedId == areaId && CityLocationConfig.citySelected.equals(cityName)) {
            finish();
        } else {
            CityLocationConfig.citySelectedId = areaId;
            CityLocationConfig.citySelected = cityName;
            setResult(RESULT_OK);
            finish();
        }

    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_GET_CITY_LIST
                && response instanceof CityPickerResponse) {
            CityPickerResponse mCityPickerResponse = (CityPickerResponse) response;
            int code = mCityPickerResponse.getCode();
            if (1 == code) {
                CityPickerResponse.DataBean mDatas = mCityPickerResponse.getData();
                if (mDatas == null) {
                    return;
                }
                mHotCityLists = mDatas.getHotCitylist();
                if (mHotCityLists != null && mHotCityLists.size() != 0) {
                    dispatchHotInfos();
                }
                List<CityPickerResponse.DataBean.AllCityListBean> allCityList = mDatas.getAllCityList();
                if (allCityList != null && allCityList.size() != 0) {
                    dispatchCityInfos(allCityList);
                }
                mCityPickAllCityScroll.setVisibility(View.VISIBLE);
            } else {
                showToastInfo(getString(R.string.get_city_filer));
                finish();
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_GET_CITY_SEARCH
                && response instanceof CitySearchResponse) {
            CitySearchResponse mCitySearchResponse = (CitySearchResponse) response;
            int code = mCitySearchResponse.getCode();
            if (1 == code) {
                mSearchResultLists = mCitySearchResponse.getData().getCitylist();
                if (mSearchResultLists == null || mSearchResultLists.size() == 0) {
                    mCityPickNoSearchCity.setVisibility(View.VISIBLE);
                } else {
                    mCityPickNoSearchCity.setVisibility(View.GONE);
                    if (mCityPickerSearchAdapter == null) {
                        mCityPickerSearchAdapter = new CityPickerSearchAdapter(this, mSearchResultLists);
                        mCityPickSearchCity.setAdapter(mCityPickerSearchAdapter);
                    } else {
                        mCityPickerSearchAdapter.changeData(mSearchResultLists);
                    }
                }
            } else {
                mCityPickNoSearchCity.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    private void dispatchCityInfos(List<CityPickerResponse.DataBean.AllCityListBean> allCityList) {
        CityPickerAllAdapter mCityPickerAllAdapter = new CityPickerAllAdapter(this, allCityList);
        mCityPickAllCity.setAdapter(mCityPickerAllAdapter);
    }

    private void dispatchHotInfos() {
        CityPickerHotAdapter cityPickerHotAdapter = new CityPickerHotAdapter(this, mHotCityLists);
        mCityPickHotCity.setAdapter(cityPickerHotAdapter);
    }
}
