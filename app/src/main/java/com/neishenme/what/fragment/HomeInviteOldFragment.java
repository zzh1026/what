package com.neishenme.what.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.neishenme.what.R;
import com.neishenme.what.activity.ActiveActivity;
import com.neishenme.what.activity.InviteInviterDetailActivity;
import com.neishenme.what.activity.InviteJoinerDetailActivity;
import com.neishenme.what.activity.LoginActivity;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.activity.MainPushADActivity;
import com.neishenme.what.activity.PayOrderActivity;
import com.neishenme.what.activity.PublishOrderActivity;
import com.neishenme.what.activity.VIPCenterActivity;
import com.neishenme.what.adapter.HomeInviteAdapter;
import com.neishenme.what.bean.HomeFilterParams;
import com.neishenme.what.bean.HomeInviteResponse;
import com.neishenme.what.bean.HomeJoinLogoListResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.RequestJoinerResponse;
import com.neishenme.what.bean.RestaurantResponse;
import com.neishenme.what.common.CityLocationConfig;
import com.neishenme.what.component.HomeJoinComponent;
import com.neishenme.what.eventbusobj.TrideBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.LocationUtils;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.view.CarouselViewPager;
import com.neishenme.what.view.highlight.view.Guide;
import com.neishenme.what.view.highlight.view.GuideBuilder;

import org.seny.android.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * 邀请布片(新)
 *
 * @author zzh
 * @version v5.0.4
 * @time 2017/3/17修改
 * <p>
 * 对 {@link HomeInviteFragment}的备份 主要是因为这个界面对  {@link HomeInviteAdapter} 依赖比较强所以对这两个
 * 都进行了备份, 该fragment对应 {@link com.neishenme.what.adapter.HomeInviteOldAdapter}
 */

@Deprecated
public class HomeInviteOldFragment extends Fragment implements HttpLoader.ResponseListener {
    private PullToRefreshListView mLv;
    private CarouselViewPager carouselViewPager;

    private HomeInviteAdapter homeAdapter;
    private List<HomeInviteResponse.DataBean.InvitesBean> mHomeInvites;   //邀请

    private MainActivity homeActivity;
    private LayoutInflater mInflater;
    private List<RestaurantResponse.DataBean.MainpushBean> mainpushBean;    //主推banner

    private HashMap<String, String> params;
    private HomeInviteResponse.DataBean.InvitesBean joinInvite;   //加入的邀请
    private int page = 1;
    private View headerView;
    private View spaceView;

    private LinearLayout mInviteShowGuideLayout;
    private ImageView mInviteShowGuideImg;

    private LinearLayout mLlMainWorning;
    private TextView mTvMainWorning;

    private LinearLayout mInviteNoInvite;

    private RelativeLayout mHomeNoNetShow;
    private ImageView mHomeNetLoadAgain;

    private boolean canClick = true;

    private boolean hasMoreInvite = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            canClick = true;
        }
    };

    public HomeInviteOldFragment() {
    }

    public static HomeInviteOldFragment newInstance() {
        HomeInviteOldFragment fragment = new HomeInviteOldFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_invite, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {
        mLv = (PullToRefreshListView) view.findViewById(R.id.ptrl_invite);
        mLv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        mInviteShowGuideLayout = (LinearLayout) view.findViewById(R.id.invite_show_guide_layout);
        mInviteShowGuideImg = (ImageView) view.findViewById(R.id.invite_show_guide_img);

        mLlMainWorning = (LinearLayout) view.findViewById(R.id.ll_main_worning);
        mTvMainWorning = (TextView) view.findViewById(R.id.tv_main_worning);

        mInviteNoInvite = (LinearLayout) view.findViewById(R.id.invite_no_invite);

        mHomeNoNetShow = (RelativeLayout) view.findViewById(R.id.home_no_net_show);
        mHomeNetLoadAgain = (ImageView) view.findViewById(R.id.home_net_load_again);
    }

    private void initData() {
        params = new HashMap();
        homeActivity = (MainActivity) getActivity();
        mInflater = LayoutInflater.from(homeActivity);
        EventBus.getDefault().register(this);
        headerView = mInflater.inflate(R.layout.frist_view_homeinvite_banner, null);
        spaceView = mInflater.inflate(R.layout.home_act_top_write, null);

        mLv.getRefreshableView().addHeaderView(spaceView);
        mLv.getRefreshableView().addHeaderView(headerView);

        carouselViewPager = (CarouselViewPager) headerView.findViewById(R.id.vp_carousel);

        //获取banner图数据
        requestBanner();
        //获取附近邀请数据
        homeActivity.latitude = CityLocationConfig.cityLatitude;
        homeActivity.longgitude = CityLocationConfig.cityLonggitude;
        getInvite();

//        homeAdapter = new HomeInviteAdapter(this, homeActivity, null);
//        mLv.setAdapter(homeAdapter);

    }

    private void requestBanner() {
        HttpLoader.get(ConstantsWhatNSM.URL_RESTAURANT_MAINPUSH, null,
                RestaurantResponse.class, ConstantsWhatNSM.REQUEST_CODE_RESTAURANT_MAINPUSH, this, false).setTag(this);
    }

    private void requestInvite() {

        LocationUtils.getLocationByGCJ_02(new LocationUtils.OnGetLocationListener() {
            @Override
            public void onGetLocation(double latitude, double longgitude, BDLocation location) {
                homeActivity.latitude = latitude;
                homeActivity.longgitude = longgitude;
                getInvite();
            }

            @Override
            public void onGetError() {
                getInvite();
            }
        });
    }

    private void getInvite() {
        String areaId;
        try {
            areaId = String.valueOf(homeActivity.showCityAreaId);
        } catch (Exception e) {
            areaId = String.valueOf(CityLocationConfig.citySelectedId);
        }
        params.put("areaId", areaId);
        params.put("latitude", String.valueOf(homeActivity.latitude));
        params.put("longitude", String.valueOf(homeActivity.longgitude));
        params.put("page", page + "");
        params.put("pageSize", "10");
        if (NSMTypeUtils.isLogin()) {
            params.put("userId", NSMTypeUtils.getMyUserId());
        }
        //首页活动列表联网请求
        HttpLoader.post(ConstantsWhatNSM.URL_HOME, params, HomeInviteResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_HOME, this, false).setTag(this);
    }

    private void initListener() {

        mLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (params != null) {
                    params.clear();
                }
                homeAdapter = null;
                page = 1;
                requestInvite();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (hasMoreInvite) {
                    page++;
                    requestInvite();
                } else {
                    showToast("暂无更多");
                    mLv.onRefreshComplete();
                }
            }
        });

        mLv.setIsShowTitleListener(
                new PullToRefreshListView.IsShowTitleListener() {
                    @Override
                    public void isShow(boolean show) {
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
                        if (spaceView.hasWindowFocus() && spaceView.isShown()) {
                            //应该显示导航栏
                            homeActivity.showHideTitleBar(true, homeActivity.lltab, homeActivity.rlMenu);
                        }
                    }

                    @Override
                    public void onListViewStop() {
                        if (spaceView.hasWindowFocus() && spaceView.isShown()) {
                            //应该显示导航栏
                            homeActivity.showHideTitleBar(true, homeActivity.lltab, homeActivity.rlMenu);
                        }
                    }
                }

        );

        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                requestStopMediaplayer();
                if (!NSMTypeUtils.isLogin()) {
//                    homeActivity.showToast(homeActivity.getString(R.string.user_should_login_suggest));
                    ActivityUtils.startActivity(homeActivity, LoginActivity.class);
                    return;
                }
                if (position == 0 || position == 1 || position == 2) {
                    return;
                }
                HomeInviteResponse.DataBean.InvitesBean invitesBean = mHomeInvites.get(position - 3);
                String user_id = String.valueOf(invitesBean.getUser_id());
                if (NSMTypeUtils.isMyUserId(user_id)) {
                    ActivityUtils.startActivityForData(homeActivity,
                            InviteInviterDetailActivity.class, String.valueOf(invitesBean.getInvite_id()));
                } else {
                    ActivityUtils.startActivityForData(homeActivity,
                            InviteJoinerDetailActivity.class, String.valueOf(invitesBean.getInvite_id()));
                }
            }
        });

        mHomeNetLoadAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestBanner();
                requestInvite();
                if (CityLocationConfig.cityLocationId == 0) {
                    homeActivity.getLocation();
                } else {
                    homeActivity.upShowCity();
                }
            }
        });

    }

    public void onEventMainThread(HomeFilterParams filterParams) {
        params.clear();
        if (null != filterParams.getGender()) {
            params.put("gender", filterParams.getGender());
        }

        if (null != filterParams.getOrderby()) {
            params.put("orderby", filterParams.getOrderby());
        }

//        if (null != filterParams.getPublishType()) {
//            params.put("publishType", filterParams.getPublishType());
//        }

        if (NSMTypeUtils.isLogin()) {
            params.put("userId", NSMTypeUtils.getMyUserId());
        }

        homeAdapter = null;
        page = 1;
        requestInvite();
    }

    //点击加入活动
    public void tryAddInvite(int position) {
        if (!canClick) {
            return;
        }
        canClick = false;
        mHandler.sendEmptyMessageDelayed(0, 2000);
        joinInvite = mHomeInvites.get(position);
        HashMap<String, String> params = new HashMap<>();
        params.put("inviteId", joinInvite.getInvite_id() + "");
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_HOME_INVITE_LOGOLIST, params, HomeJoinLogoListResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_HOME_INVITE_LOGOLIST, this, false).setTag(this);

    }

    //点击加入成功
    public void onMoneyEdit(String money) {
        HashMap<String, String> params = new HashMap<>();
        params.put("areaId", CityLocationConfig.cityLocationId + "");
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("inviteId", joinInvite.getInvite_id() + "");
        if (TextUtils.isEmpty(money)) {
            params.put("joinPrice", "0");
        } else {
            params.put("joinPrice", money);
        }
        HttpLoader.post(ConstantsWhatNSM.URL_INVITE_JOIN, params, RequestJoinerResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_INVITE_JOIN, this, false).setTag(this);
    }

    //城市选择完毕后重新刷新数据
    public void notifyData() {
        requestInvite();
    }

    //付费加入支付成功
    public void onPaySuccess() {
        showToast("加入成功");
        joinInvite.setJoiner_newstatus(100);
        ActivityUtils.startActivityForData(homeActivity, InviteJoinerDetailActivity.class, String.valueOf(joinInvite.getInvite_id()));
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        mHomeNoNetShow.setVisibility(View.GONE);
        //主推,,上部轮播图
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
//                carouselViewPager.setBannerData(this, urlList);
            }
        }

        //邀请活动
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_HOME
                && response instanceof HomeInviteResponse) {
            HomeInviteResponse homeResponse_down = (HomeInviteResponse) response;
            if (homeResponse_down.getCode() == 1) {
                mLv.setVisibility(View.VISIBLE);
                mLlMainWorning.setVisibility(View.GONE);
                hasMoreInvite = homeResponse_down.getData().isHasMore();
                if (hasMoreInvite) {
                    mLv.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    mLv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                if (null == homeAdapter || page == 1 || mHomeInvites == null) {
                    mHomeInvites = homeResponse_down.getData().getInvites();
//                    homeAdapter = new HomeInviteAdapter(this, homeActivity, mHomeInvites);
//                    mLv.setAdapter(homeAdapter);
//                    homeActivity.tryShowGuide1();
                } else {
                    mHomeInvites.addAll(homeResponse_down.getData().getInvites());
                    homeAdapter.notifyDataSetChanged();
                }
            } else {
                mLv.setVisibility(View.INVISIBLE);
                mLlMainWorning.setVisibility(View.VISIBLE);
                mTvMainWorning.setText(homeResponse_down.getMessage());
            }

            if (mHomeInvites == null || mHomeInvites.size() == 0) {
                mInviteNoInvite.setVisibility(View.VISIBLE);
            } else {
                mInviteNoInvite.setVisibility(View.GONE);
            }

            mLv.onRefreshComplete();
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_JOIN
                && response instanceof RequestJoinerResponse) {
            RequestJoinerResponse requestJoinerResponse = (RequestJoinerResponse) response;
            int code = requestJoinerResponse.getCode();
            if (1 == code) {
                //0 说明是免费加入的单子
                if (0 == requestJoinerResponse.getData().getTrade().getPayprice() || requestJoinerResponse.getData().getJoiner().getNewstatus() == 50) {
                    showToast("加入成功");
                    joinInvite.setJoiner_newstatus(requestJoinerResponse.getData().getJoiner().getNewstatus());
                    ActivityUtils.startActivityForData(homeActivity, InviteJoinerDetailActivity.class,
                            String.valueOf(requestJoinerResponse.getData().getJoiner().getInviteId()));
                } else {
                    //付费加入
                    RequestJoinerResponse.DataBean.TradeBean trade = requestJoinerResponse.getData().getTrade();
                    payOrderForResult(trade.getPayprice(), trade.getTradeNum());
                }
            } else {
                showToast(requestJoinerResponse.getMessage());
            }
        }

        //加入之前验证
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_HOME_INVITE_LOGOLIST
                && response instanceof HomeJoinLogoListResponse) {
            HomeJoinLogoListResponse homeJoinLogoListResponse = (HomeJoinLogoListResponse) response;
            int code = homeJoinLogoListResponse.getCode();
            if (code == 1) {
                List<HomeJoinLogoListResponse.DataBean.InvitesBean> invites = homeJoinLogoListResponse.getData().getInvites();
//                HomeAddInviteDialog mHomeAddInviteDialog = new HomeAddInviteDialog(homeActivity, this, invites);
//                mHomeAddInviteDialog.show();
//                homeActivity.blurAll();
            } else if (code == -1026) {     //已经成功加入
                joinInvite.setJoiner_newstatus(100);
                showToast(homeJoinLogoListResponse.getMessage());
            } else {
                showToast(homeJoinLogoListResponse.getMessage());
            }
        }
    }

    //开始支付
    private void payOrderForResult(double price, String tradeNum) {
        TrideBean trideBean = new TrideBean(false, 0, joinInvite.getType(),
                joinInvite.getUser_thumbnailslogofile(), joinInvite.getInvite_title(), joinInvite.getInvite_time(),
                price, tradeNum);

        PayOrderActivity.startPayOrderActForResult(homeActivity, MainActivity.REQUEST_CODE_PAY_ORDER, trideBean);
    }

    public void showGuideView() {
        mInviteShowGuideLayout.setVisibility(View.VISIBLE);
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(mInviteShowGuideImg)
                .setAlpha(212)
                .setHighTargetCorner(2)
//                .setHighTargetPadding(5)
                .setOverlayTarget(false)
                .setOutsideTouchable(false)
                .setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                    @Override
                    public void onShown() {
                    }

                    @Override
                    public void onDismiss() {
                        mInviteShowGuideLayout.setVisibility(View.GONE);
                    }
                });
        builder.addComponent(new HomeJoinComponent());
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show(homeActivity);
    }

    public void dispathPageInfo(int currentPage) {
        RestaurantResponse.DataBean.MainpushBean mainpushBean = this.mainpushBean.get(currentPage);
        requestStopMediaplayer();
        int type = mainpushBean.getType();
        if (1 == type) {    //1,餐厅详情
            int serviceId = mainpushBean.getServiceId();
            PublishOrderActivity.startRestDetailAct(getContext(), serviceId);
        } else if (2 == type) { //2,广告
            String url;
            if (NSMTypeUtils.isLogin()) {
                HashMap<String, String> params = new HashMap<>();
                params.put("token", NSMTypeUtils.getMyToken());
                url = mainpushBean.getLink() + HttpLoader.buildGetParam(params, mainpushBean.getLink());
            } else {
                url = mainpushBean.getLink();
            }
            Intent intent = new Intent(homeActivity, MainPushADActivity.class);
            intent.putExtra("data", url);
            intent.putExtra("type", type);
            startActivity(intent);
        } else if (3 == type) {   //3,邀请详情
            if (!NSMTypeUtils.isLogin()) {
//                homeActivity.showToast(homeActivity.getString(R.string.user_should_login_suggest));
                ActivityUtils.startActivity(homeActivity, LoginActivity.class);
                return;
            }
            String user_id = String.valueOf(mainpushBean.getInviteUserId());
            if (NSMTypeUtils.isMyUserId(user_id)) {
                ActivityUtils.startActivityForData(homeActivity,
                        InviteInviterDetailActivity.class, String.valueOf(mainpushBean.getInviteId()));
            } else {
                ActivityUtils.startActivityForData(homeActivity,
                        InviteJoinerDetailActivity.class, String.valueOf(mainpushBean.getInviteId()));
            }
        } else if (4 == type) { //4,1元购活动
            ActivityUtils.startActivity(homeActivity, ActiveActivity.class);
        } else if (5 == type) { //会员中心
            if (!NSMTypeUtils.isLogin()) {
//                homeActivity.showToast(homeActivity.getString(R.string.user_should_login_suggest));
                ActivityUtils.startActivity(homeActivity, LoginActivity.class);
                return;
            }
            ActivityUtils.startActivity(homeActivity, VIPCenterActivity.class);
        } else if (6 == type) { //可分享的广告
            if (!NSMTypeUtils.isLogin()) {
                ActivityUtils.startActivity(homeActivity, LoginActivity.class);
                return;
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("token", NSMTypeUtils.getMyToken());
            String url = mainpushBean.getLink();
            Intent intent = new Intent(homeActivity, MainPushADActivity.class);
            intent.putExtra("bannerId", mainpushBean.getId());
            intent.putExtra("data", url);
            intent.putExtra("type", type);
            startActivity(intent);
        }
    }

//    public void onPopInfoTa(int userId) {
//        UserDetailActivity.startUserDetailAct(homeActivity, userId, false);
//    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_RESTAURANT_MAINPUSH
                || requestCode == ConstantsWhatNSM.REQUEST_CODE_HOME) {
            mHomeNoNetShow.setVisibility(View.VISIBLE);
            homeActivity.showToastError("网络访问失败了,请您检查一下网络设置吧");
        }
        mLv.onRefreshComplete();
    }

    public void requestStopMediaplayer() {
        if (homeAdapter != null) {
//            homeAdapter.onDestory();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void showToast(String str) {
        homeActivity.showToastInfo(str);
    }
}
