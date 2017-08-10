package com.neishenme.what.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.neishenme.what.R;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.activity.PublishOrderActivity;
import com.neishenme.what.adapter.RecommedRestaurantAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.baidumap.NSMMapHelper;
import com.neishenme.what.baidumap.service.LocationService;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.RestaurantListResponse;
import com.neishenme.what.bean.RestaurantResponse;
import com.neishenme.what.dialog.MenuFiltrateDialog;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.Gps;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.PositionUtil;
import com.neishenme.what.view.CarouselViewPager;

import org.seny.android.utils.ALog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gengxin on 2016/5/3.
 */
@Deprecated
public class RestaurantFragment extends Fragment implements HttpLoader.ResponseListener {

    private PullToRefreshListView lvRecommend;
    private CarouselViewPager carouselViewPager;
    private RecommedRestaurantAdapter recommedRestaurantAdapter;
    private int page = 1;
    private View headerView;
    private MainActivity homeActivity;
    public static boolean isShowCurr = true;
    private boolean isGetLocation = false;


    private static final String ARG_PARAM1 = "param1";
    private String params1;

    private List<RestaurantResponse.DataBean.MainpushBean> mainpushBean;
    private List<RestaurantListResponse.DataEntity.ServiceEntity> serviceBeans;

    private View spaceView;

    private LocationService locationService;

    private String requestPageNum;
    private String requestPageSize;
    private double storeLatitude;
    private double storeLonggitude;

    private LinearLayout mLlMainWorning;
    private TextView mTvMainWorning;

    public static RestaurantFragment newInstance(String params) {
        RestaurantFragment fragment = new RestaurantFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            params1 = getArguments().getString(ARG_PARAM1);
        }
        HttpLoader.get(ConstantsWhatNSM.URL_RESTAURANT_MAINPUSH, null,
                RestaurantResponse.class, ConstantsWhatNSM.REQUEST_CODE_RESTAURANT_MAINPUSH, this, false).setTag(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        initView(view);
        initData();
        initListener();
        requestRestaurant("1", "20");
        return view;
    }

    private void requestRestaurant(String page, String pageSize) {
        requestPageNum = page;
        requestPageSize = pageSize;
        isGetLocation = true;
        locationService.setLocationOption(LocationService.CoorType.CoorType_GCJ02, LocationService.TimeType.TIME_0);
        locationService.start();
    }


    public void initView(View view) {
        lvRecommend = (PullToRefreshListView) view.findViewById(R.id.lv_recommend);
        lvRecommend.setMode(PullToRefreshBase.Mode.BOTH);

        mLlMainWorning = (LinearLayout) view.findViewById(R.id.ll_main_worning);
        mTvMainWorning = (TextView) view.findViewById(R.id.tv_main_worning);
    }

    public void initData() {
        homeActivity = (MainActivity) getActivity();
        headerView = LayoutInflater.from(homeActivity).inflate(R.layout.frist_view_carousel_view_pager, null);
        spaceView = LayoutInflater.from(homeActivity).inflate(R.layout.home_act_top_write, null);

        lvRecommend.getRefreshableView().addHeaderView(spaceView);
        lvRecommend.getRefreshableView().addHeaderView(headerView);

        carouselViewPager = (CarouselViewPager) headerView.findViewById(R.id.vp_carousel);

        locationService = NSMMapHelper.getInstance().locationService;
        locationService.registerListener(mListener);
    }

    public void initListener() {

        lvRecommend.setIsShowTitleListener(new PullToRefreshListView.IsShowTitleListener() {
            @Override
            public void isShow(boolean show) {
                isShowCurr = show;
                if (show) {
                    //应该显示导航栏
                    if (!homeActivity.mTitleBarOnShowing) {
                        homeActivity.mTitleBarOnShowing = true;
                        homeActivity.showHideTitleBar(true, homeActivity.lltab, homeActivity.rlMenu);
                    }
                } else {
                    //不应该显示导航栏
                    if (!homeActivity.mTitleBarOnShowing) {
                        homeActivity.mTitleBarOnShowing = true;
                        homeActivity.showHideTitleBar(false, homeActivity.lltab, homeActivity.rlMenu);
                    }
                }
            }

            @Override
            public void onUp() {
                if (spaceView.isShown()) {
                    //应该显示导航栏
                    homeActivity.showHideTitleBar(true, homeActivity.lltab, homeActivity.rlMenu);
                }
            }

            @Override
            public void onListViewStop() {
                if (spaceView.isShown()) {
                    //应该显示导航栏
                    homeActivity.showHideTitleBar(true, homeActivity.lltab, homeActivity.rlMenu);
                }
            }
        });

        lvRecommend.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                recommedRestaurantAdapter = null;
                page = 1;
                requestRestaurant("1", "20");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                requestRestaurant(page + "", "5");
            }
        });

        lvRecommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (serviceBeans != null) {
//                    RestaurantListResponse.DataBean.ServiceBean serviceBean = restaurantListResponse.getData().getService().get((position - 2));
//                    EventBus.getDefault().postSticky(serviceBean);
//                    ActivityUtils.startActivity(getActivity(), PublishOrderActivity.class);
                    if (position == 0 || position == 1 || position == 2) {
                        return;
                    }
                    int services_id = serviceBeans.get(position - 3).getServices_id();
                    PublishOrderActivity.startRestDetailAct(getContext(), services_id);
                }


            }
        });
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                if (isGetLocation) {
                    isGetLocation = false;
//                    Gps gps = PositionUtil.gcj_To_Gps84(location.getLatitude(), location.getLongitude());
//                    storeLatitude = gps.getWgLat();
//                    storeLonggitude = gps.getWgLon();
//
//                    HashMap<String, String> params = new HashMap();
//                    params.put("page", requestPageNum);
//                    params.put("pageSize", requestPageSize);
//                    params.put("latitude", String.valueOf(storeLatitude));
//                    params.put("longitude", String.valueOf(storeLonggitude));
//                    HttpLoader.get(ConstantsWhatNSM.URL_RESTAURANT, params, RestaurantListResponse.class,
//                            ConstantsWhatNSM.REQUEST_CODE_RESTAURANT, RestaurantFragment.this, false).setTag(this);
                }

//                locationService.stop();
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };


    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_RESTAURANT_MAINPUSH
                && response instanceof RestaurantResponse) {
            RestaurantResponse restaurantResponse = (RestaurantResponse) response;
            if (restaurantResponse.getCode() == 1) {
                mainpushBean = restaurantResponse.getData().getMainpush();
                if (null == mainpushBean || mainpushBean.size() == 0) {
                    return;
                }
                List<String> urlList = new ArrayList<>();
                for (int i = 0; i < mainpushBean.size(); i++) {
                    String imagePath = mainpushBean.get(i).getImage();
                    if (!TextUtils.isEmpty(imagePath)) {
                        urlList.add(imagePath);
                    }
                }
                //carouselViewPager.setBannerData(this, urlList);
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_RESTAURANT
                && response instanceof RestaurantListResponse) {
            lvRecommend.onRefreshComplete();
            RestaurantListResponse updateResponse = (RestaurantListResponse) response;
            if (updateResponse.getCode() == 1) {
                lvRecommend.setVisibility(View.VISIBLE);
                mLlMainWorning.setVisibility(View.GONE);
                if (recommedRestaurantAdapter == null) {
                    serviceBeans = updateResponse.getData().getService();
                    recommedRestaurantAdapter = new RecommedRestaurantAdapter(getActivity(), serviceBeans);
                    lvRecommend.setAdapter(recommedRestaurantAdapter);
                } else {
                    serviceBeans.addAll(updateResponse.getData().getService());
                    recommedRestaurantAdapter.setRecommedRestaurant(serviceBeans);
                    recommedRestaurantAdapter.notifyDataSetChanged();
                }
            } else {
                lvRecommend.setVisibility(View.INVISIBLE);
                mLlMainWorning.setVisibility(View.VISIBLE);
                mTvMainWorning.setText(updateResponse.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_RESTAURANT)
            lvRecommend.onRefreshComplete();
        homeActivity.showToastError("网络访问失败了,请您检查一下网络设置吧");
    }

    public void dispathPageInfo(int currentPage) {
        RestaurantResponse.DataBean.MainpushBean mainpushBean = this.mainpushBean.get(currentPage);
        int type = mainpushBean.getType();
        if (1 == type) {
            int serviceId = mainpushBean.getServiceId();
            PublishOrderActivity.startRestDetailAct(getContext(), serviceId);
        } else if (2 == type) {
            Uri uri;
            if (NSMTypeUtils.isLogin()) {
                HashMap<String, String> params = new HashMap<>();
                params.put("token", NSMTypeUtils.getMyToken());
                uri = Uri.parse(mainpushBean.getLink() + HttpLoader.buildGetParam(params, mainpushBean.getLink()));
            } else {
                uri = Uri.parse(mainpushBean.getLink());
            }
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            App.getApplication().startActivity(it);
        }
    }

    public void requestFilterSort(int mCurrentSelected) {
        page = 1;
        recommedRestaurantAdapter = null;
        HashMap<String, String> param = new HashMap();
        param.put("page", "1");
        param.put("pageSize", "20");
        switch (mCurrentSelected) {
            case MenuFiltrateDialog.SORT_BY_BOY_LOVE:
                param.put("orderby", "schoolboy");
                break;
            case MenuFiltrateDialog.SORT_BY_GIRL_LOVE:
                param.put("orderby", "schoolgirl");
                break;
            case MenuFiltrateDialog.SORT_BY_PRICE:
                param.put("orderby", "pricelow");
                break;
            case MenuFiltrateDialog.SORT_BY_TIME:
                param.put("orderby", "time");
                break;
            case MenuFiltrateDialog.SORT_BY_DISTENCE:
                param.put("orderby", "distance");
                break;
            default:
                break;
        }
        param.put("latitude", String.valueOf(storeLatitude));
        param.put("longitude", String.valueOf(storeLonggitude));
        HttpLoader.get(ConstantsWhatNSM.URL_RESTAURANT, param, RestaurantListResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_RESTAURANT, this, false).setTag(this);
    }

    @Override
    public void onDestroyView() {
        locationService.unregisterListener(mListener); //注销掉监听
//        locationService.stop(); //停止定位服务
        super.onDestroyView();
    }
}
