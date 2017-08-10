package com.neishenme.what.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.neishenme.what.R;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.adapter.HomeMyTripAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.bean.HistoryOrderResponse;
import com.neishenme.what.bean.MyOrderListResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.receiver.MyJPushReceiver;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 主界面的 行程 fragment
 * v5.0.5
 * 2017/3/16
 */
public class HomeTrivelFragment extends Fragment implements HttpLoader.ResponseListener {
    private MainActivity homeActivity;

    private LinearLayout mLlMainWorning;
    private TextView mTvMainWorning;

    private PullToRefreshListView mHomeTrivelRefresh;
    private HomeMyTripAdapter mTripAdapter;

    private LinearLayout mHomeTripNoTrip;
    private ImageView mHomeTripToRelease;

    private List<MyOrderListResponse.DataBean.AllJourneyBean> mAllJourneyList;  //所有需要展示的行程数据

    private int mCurrentPage = 1; //标记当前展示的页数
    private boolean hasMorePage = false; //标记当前是否有更多页,即上拉加载更多是否有效
    private View spaceView;     //上部空白
    private String mOrderInviteId;  //标记通知栏到达的order标记

    public HomeTrivelFragment() {
    }

    public static HomeTrivelFragment newInstance() {
        HomeTrivelFragment fragment = new HomeTrivelFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_trivel, container, false);
        initView(view);
        initListener();
        initData();
        return view;
    }

    private void initView(View view) {
        mHomeTrivelRefresh = (PullToRefreshListView) view.findViewById(R.id.home_trivel_refresh);
        mHomeTrivelRefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        mLlMainWorning = (LinearLayout) view.findViewById(R.id.ll_main_worning);
        mTvMainWorning = (TextView) view.findViewById(R.id.tv_main_worning);

        mHomeTripNoTrip = (LinearLayout) view.findViewById(R.id.home_trip_no_trip);
        mHomeTripToRelease = (ImageView) view.findViewById(R.id.home_trip_to_release);
    }

    private void initData() {
        homeActivity = (MainActivity) getActivity();
        LayoutInflater inflater = LayoutInflater.from(homeActivity);
        spaceView = inflater.inflate(R.layout.home_act_top_write, null);
//        mHomeTrivelRefresh.getRefreshableView().addHeaderView(spaceView);
        mOrderInviteId = App.SP.getString(MyJPushReceiver.MSG_ORDER_INVITE_ID, null);
        getMyTripList();
    }

    public void notifyData() {
        mCurrentPage = 1;
        mTripAdapter = null;
        mAllJourneyList = null;
//        getMyTripList();

        mHomeTrivelRefresh.setRefreshing();
    }

    private void initListener() {
        mHomeTripToRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.toReleaseAct();
            }
        });

        mHomeTrivelRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mCurrentPage = 1;
                mTripAdapter = null;
                mAllJourneyList = null;
                getMyTripList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (hasMorePage) {
                    mCurrentPage++;
                    getMyTripList();
                } else {
                    showToast("暂无更多");
                    mHomeTrivelRefresh.onRefreshComplete();
                }
            }
        });

        mHomeTrivelRefresh.setIsShowTitleListener(
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
    }

    private void getMyTripList() {
        if (!NSMTypeUtils.isLogin()) {
            return;
        }
        HashMap<String, String> params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("page", mCurrentPage + "");
        params.put("pageSize", "10");
        HttpLoader.get(ConstantsWhatNSM.URL_MY_ORDER_LIST, params, MyOrderListResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_MY_ORDER_LIST, this).setTag(this);
    }

    public void showToast(String str) {
        homeActivity.showToastInfo(str);
    }

    public void refreshDate() {
        mHomeTrivelRefresh.setRefreshing();
    }

    public void setOrderInviteId(String mOrderInviteId) {
        this.mOrderInviteId = mOrderInviteId;
        if (mTripAdapter != null) {
            mTripAdapter.setOrderInviteId(mOrderInviteId);
        }
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_MY_ORDER_LIST
                && response instanceof MyOrderListResponse) {
            mHomeTrivelRefresh.onRefreshComplete();
            MyOrderListResponse myOrderListResponse = (MyOrderListResponse) response;
            if (myOrderListResponse.getCode() == 1) {
                mHomeTrivelRefresh.setVisibility(View.VISIBLE);
                mLlMainWorning.setVisibility(View.GONE);
                hasMorePage = myOrderListResponse.getData().isHasMore();
                if (!hasMorePage) {
                    mHomeTrivelRefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                } else {
                    mHomeTrivelRefresh.setMode(PullToRefreshBase.Mode.BOTH);
                }
                if (mCurrentPage == 1 || null == mTripAdapter || null == mAllJourneyList) {
                    mAllJourneyList = myOrderListResponse.getData().getAllJourney();
                    mTripAdapter = new HomeMyTripAdapter(homeActivity, this, mAllJourneyList, mOrderInviteId);
                    mHomeTrivelRefresh.setAdapter(mTripAdapter);
                } else {
                    mAllJourneyList.addAll(myOrderListResponse.getData().getAllJourney());
                    mTripAdapter.notifyDataSetChanged();
                }
                if (mAllJourneyList == null || mAllJourneyList.size() == 0) {
                    mHomeTripNoTrip.setVisibility(View.VISIBLE);
                } else {
                    mHomeTripNoTrip.setVisibility(View.GONE);
                }
            } else {
                showToast(myOrderListResponse.getMessage());
                mHomeTrivelRefresh.setVisibility(View.INVISIBLE);
                mLlMainWorning.setVisibility(View.VISIBLE);
                mTvMainWorning.setText(myOrderListResponse.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        mHomeTrivelRefresh.onRefreshComplete();
    }
}
