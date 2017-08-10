package com.neishenme.what.fragment;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.neishenme.what.R;
import com.neishenme.what.activity.InviteInviterDetailActivity;
import com.neishenme.what.activity.InviteJoinerDetailActivity;
import com.neishenme.what.activity.LoginActivity;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.activity.PayOrderActivity;
import com.neishenme.what.activity.UserDetailActivity;
import com.neishenme.what.adapter.HomeAdapter;
import com.neishenme.what.bean.HomeFilterParams;
import com.neishenme.what.bean.HomeResponse;
import com.neishenme.what.bean.InviteDetailResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.RequestJoinResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.dialog.InviteMenuDialog;
import com.neishenme.what.eventbusobj.TrideBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;

import java.util.HashMap;

import de.greenrobot.event.EventBus;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InviteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@Deprecated
public class InviteFragment extends Fragment implements HttpLoader.ResponseListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PullToRefreshListView mLv;
    private ImageView ivWait;
    private RelativeLayout rlWait;


    String storeName;
    String servicesLogo;
    String title;
    long time;
    int payType;
    int serviceId;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private HomeAdapter homeAdapter;
    private HomeResponse homeResponse;
    int touchSlop = 10;
    private MainActivity homeActivity;
    private View mMainToolbar = null;
    private int lastVisibleItemPosition = 0;
    private boolean titleVisible = true;
    private HashMap params;
    private int page = 1;
    private InviteDetailResponse inviteDetailResponse;
    private int joinPosition = -1;
    AnimationDrawable animationDrawable;
    private View headerView;
    private View spaceView;

    private LinearLayout mLlMainWorning;
    private TextView mTvMainWorning;

    public InviteFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static InviteFragment newInstance(String param1, String param2) {
        InviteFragment fragment = new InviteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        params = new HashMap();
        params.put("page", "1");
        params.put("pageSize", "20");
        if (NSMTypeUtils.isLogin()) {
            params.put("userId", NSMTypeUtils.getMyUserId());
        }
        HttpLoader.post(ConstantsWhatNSM.URL_HOME, params, HomeResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_HOME, this, false).setTag(this);
        touchSlop = (int) (ViewConfiguration.get(getActivity()).getScaledTouchSlop() * 0.9);//滚动过多少距离后才开始计算是否隐藏/显示头尾元素。这里用了默认touchslop的0.9倍。
        homeActivity = (MainActivity) getActivity();
        EventBus.getDefault().register(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite, container, false);
        mLv = (PullToRefreshListView) view.findViewById(R.id.lv_invite);
        mLv.setMode(PullToRefreshBase.Mode.BOTH);

        mLlMainWorning = (LinearLayout) view.findViewById(R.id.ll_main_worning);
        mTvMainWorning = (TextView) view.findViewById(R.id.tv_main_worning);

        // homeActivity.showHideTitleBar(true, homeActivity.lltab, homeActivity.rlMenu);
        initData();
        initListener();
        ivWait = (ImageView) view.findViewById(R.id.iv_wait);
        rlWait = (RelativeLayout) view.findViewById(R.id.rl_wait);
        rlWait.setVisibility(View.INVISIBLE);
        return view;
    }

    private void initData() {
        homeActivity = (MainActivity) getActivity();
        headerView = LayoutInflater.from(homeActivity).inflate(R.layout.home_act_top_write, null);
        spaceView = headerView.findViewById(R.id.item_frist);
        mLv.getRefreshableView().addHeaderView(headerView);
    }

    public void refreshData() {
        homeAdapter = null;
        page = 1;
        params.put("page", "1");
        params.put("pageSize", "20");
        if (NSMTypeUtils.isLogin()) {
            params.put("userId", NSMTypeUtils.getMyUserId());
        }
        HttpLoader.post(ConstantsWhatNSM.URL_HOME, params, HomeResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_HOME, InviteFragment.this, false).setTag(this);
    }

    private void initListener() {

        mLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                homeAdapter = null;
                page = 1;
                params.put("page", "1");
                params.put("pageSize", "20");
                if (NSMTypeUtils.isLogin()) {
                    params.put("userId", NSMTypeUtils.getMyUserId());
                }
                HttpLoader.post(ConstantsWhatNSM.URL_HOME, params, HomeResponse.class,
                        ConstantsWhatNSM.REQUEST_CODE_HOME, InviteFragment.this, false).setTag(this);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                params.put("page", page + "");
                params.put("pageSize", "20");
                if (NSMTypeUtils.isLogin()) {
                    params.put("userId", NSMTypeUtils.getMyUserId());
                }
                HttpLoader.post(ConstantsWhatNSM.URL_HOME, params, HomeResponse.class,
                        ConstantsWhatNSM.REQUEST_CODE_HOME, InviteFragment.this, false).setTag(this);
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
                    homeActivity.showToastInfo("您尚未登录,请登录后再进行操作");
                    ActivityUtils.startActivity(homeActivity, LoginActivity.class);
                    return;
                }
                if (position == 0 || position == 1) {
                    return;
                }
                HomeResponse.DataBean.InvitesBean invitesBean = homeResponse.getData().getInvites().get(position - 2);
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


    }

    public void onEventMainThread(HomeFilterParams filterParams) {
        params = new HashMap();
        if (null != filterParams.getGender()) {
            params.put("gender", filterParams.getGender());
        }

//        if (null != filterParams.getPublishType()) {
//            params.put("payType", filterParams.getPublishType());
//        }
//
//        if (null != filterParams.getOrderby()) {
//            params.put("orderby", filterParams.getOrderby());
//        }
//
//        if (NSMTypeUtils.isLogin()) {
//            params.put("userId", NSMTypeUtils.getMyUserId());
//        }

        homeAdapter = null;
        HttpLoader.post(ConstantsWhatNSM.URL_HOME, params, HomeResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_HOME, this, false).setTag(this);
    }


    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_HOME
                && response instanceof HomeResponse) {
            HomeResponse homeResponse_down = (HomeResponse) response;
            if (homeResponse_down.getCode() == 1) {
                mLv.setVisibility(View.VISIBLE);
                mLlMainWorning.setVisibility(View.GONE);
                if (null == homeAdapter) {
                    homeResponse = homeResponse_down;
                    homeAdapter = new HomeAdapter(getActivity(), homeResponse);
                    mLv.setAdapter(homeAdapter);
                    homeAdapter.setOnItemPop(new HomeAdapter.itemPopCallBack() {
                        @Override
                        public void setOnitem(int i) {
                            joinPosition = i;
                            showPopWindow(i);
                            Log.d("popnum", i + "item");
                        }
                    });

                } else {
                    homeResponse.getData().getInvites().addAll(homeResponse_down.getData().getInvites());
                }
                homeAdapter.notifyDataSetChanged();
            } else {
                mLv.setVisibility(View.INVISIBLE);
                mLlMainWorning.setVisibility(View.VISIBLE);
                mTvMainWorning.setText(homeResponse_down.getMessage());
            }
            mLv.onRefreshComplete();
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ADDFOCUS
                && response instanceof SendSuccessResponse) {
            if (((SendSuccessResponse) response).getCode() == 1) {
                homeActivity.showToastSuccess("添加关注成功");
                homeAdapter.addFocus(joinPosition);
            } else {
                showToast(((SendSuccessResponse) response).getMessage());
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_CANCLE_FOUCS_PEOPLE
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            if (sendSuccessResponse.getCode() == 1) {
                homeActivity.showToastSuccess("取消关注成功");
                homeAdapter.cancleFocus(joinPosition);
            } else {
                showToast(sendSuccessResponse.getMessage());
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ADD_SHIELD
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            if (sendSuccessResponse.getCode() == 1) {
                homeActivity.showToastSuccess("屏蔽成功");
                homeAdapter.shieldPeople(joinPosition);
            } else {
                showToast(sendSuccessResponse.getMessage());
            }
        }

//        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_JOINER
//                && response instanceof InviteDetailResponse) {
//            inviteDetailResponse = (InviteDetailResponse) response;
//            if (1 != inviteDetailResponse.getCode()) {
//                homeActivity.showToast(inviteDetailResponse.getMessage());
//                animationDrawable.stop();
//                rlWait.setVisibility(View.INVISIBLE);
//                return;
//            }
//            int userId = inviteDetailResponse.getData().getInvite().getUserId();
//            if (NSMTypeUtils.getMyUserId().equals(String.valueOf(userId))) {
//                homeActivity.showToast("登录出现问题,请重新登录");
//                ActivityUtils.startActivity(getActivity(), LoginActivity.class);
//                animationDrawable.stop();
//                rlWait.setVisibility(View.INVISIBLE);
//                return;
//            }
//            storeName = ((InviteDetailResponse) response).getData().getStore().getName();
//            servicesLogo = ((InviteDetailResponse) response).getData().getServices().getLogo();
//            title = ((InviteDetailResponse) response).getData().getInvite().getTitle().trim();
//            time = ((InviteDetailResponse) response).getData().getInvite().getTime();
//            payType = ((InviteDetailResponse) response).getData().getInvite().getPayType();
//            serviceId = ((InviteDetailResponse) response).getData().getInvite().getServiceId();
//
//            requestJoin();
//        }
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_JOIN
                && response instanceof RequestJoinResponse) {
            RequestJoinResponse requestJoinResponse = (RequestJoinResponse) response;
            int code = requestJoinResponse.getCode();
            if (1 == code) {

                //0 说明是加入别人请客的单子,一个女的加入了一个男的胆子,直接显示成功,并刷新 ,或者是会员加入了
                if (0 == requestJoinResponse.getData().getTrade().getPrice() || requestJoinResponse.getData().getJoiner().getNewstatus() == 50) {
                    HomeResponse.DataBean.InvitesBean invitesBean = homeResponse.getData().getInvites().get(joinPosition);
                    String user_id = String.valueOf(invitesBean.getUser_id());
                    showToast("加入成功");
                    if (NSMTypeUtils.isMyUserId(user_id)) {
                        ActivityUtils.startActivityForData(homeActivity,
                                InviteInviterDetailActivity.class, String.valueOf(invitesBean.getInvite_id()));
                    } else {
                        ActivityUtils.startActivityForData(homeActivity,
                                InviteJoinerDetailActivity.class, String.valueOf(invitesBean.getInvite_id()));
                    }
                } else {
//                    RequestJoinResponse.DataEntity.TradeEntity trade = requestJoinResponse.getData().getTrade();
//                    TrideBean trideBean = new TrideBean(storeName, servicesLogo, title, time,
//                            trade.getPrice(), payType, serviceId, trade.getTradeNum());
////                    PayOrderActivity.startPayOrderActForResult(getActivity(), trideBean);
                }
                refreshData();
            } else if (-1210 == code) {
                homeActivity.showToastInfo(requestJoinResponse.getMessage());
            } else {
                homeActivity.showToastInfo(requestJoinResponse.getMessage());

            }
            rlWait.setVisibility(View.INVISIBLE);
            animationDrawable.stop();
        }
    }

    private void requestJoin() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("inviteId", homeResponse.getData().getInvites().get(joinPosition).getInvite_id() + "");
        HttpLoader.post(ConstantsWhatNSM.URL_INVITE_JOIN, params, RequestJoinResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_INVITE_JOIN, this, false).setTag(this);
    }

    private TextView tvFocus;
    private TextView tvJoinDate;
    private TextView tvInfoTa;
    private TextView tvCancel;
    private PopupWindow popupWindow;

    private void showPopWindow(final int i) {

        HomeResponse.DataBean.InvitesBean invitesBean = homeResponse.getData().getInvites().get(joinPosition);
        InviteMenuDialog inviteMenuDialog = new InviteMenuDialog(homeActivity, this, invitesBean);
        inviteMenuDialog.show();

//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_main, null);
//        tvJoinDate = (TextView) view.findViewById(R.id.tv_join_date);
//        tvJoinDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rlWait.setVisibility(View.VISIBLE);
//                animationDrawable = (AnimationDrawable) ivWait.getDrawable();
//                animationDrawable.start();
//                Log.d("popnum", i + "popitem");
//                joinDate(joinPosition);
//                popupWindow.dismiss();
//            }
//        });
//        tvInfoTa = (TextView) view.findViewById(R.id.tv_info_ta);
//        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
//        tvInfoTa.setOnClickListener(this);
//        tvCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//            }
//        });
    }

    public void onPopFocusClick(HomeResponse.DataBean.InvitesBean invitesBean) {
        HashMap params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("targetId", invitesBean.getUser_id() + "");
        if (invitesBean.getUserfoucs_state() == 0) {
            HttpLoader.post(ConstantsWhatNSM.URL_ADDFOCUS, params, SendSuccessResponse.class,
                    ConstantsWhatNSM.REQUEST_CODE_ADDFOCUS, InviteFragment.this, false).setTag(this);
        } else {
            HttpLoader.post(ConstantsWhatNSM.URL_CANCLE_FOUCS_PEOPLE, params, SendSuccessResponse.class,
                    ConstantsWhatNSM.REQUEST_CODE_CANCLE_FOUCS_PEOPLE, InviteFragment.this, false).setTag(this);
        }
    }

    public void onPopJoinDateClick(boolean isInviteDetail) {
        if (isInviteDetail) {
            HomeResponse.DataBean.InvitesBean invitesBean = homeResponse.getData().getInvites().get(joinPosition);
            String user_id = String.valueOf(invitesBean.getUser_id());
            if (NSMTypeUtils.isMyUserId(user_id)) {
                ActivityUtils.startActivityForData(homeActivity,
                        InviteInviterDetailActivity.class, String.valueOf(invitesBean.getInvite_id()));
            } else {
                ActivityUtils.startActivityForData(homeActivity,
                        InviteJoinerDetailActivity.class, String.valueOf(invitesBean.getInvite_id()));
            }
        } else {
            rlWait.setVisibility(View.VISIBLE);
            animationDrawable = (AnimationDrawable) ivWait.getDrawable();
            animationDrawable.start();
            joinDate(joinPosition);
        }
    }

    public void onPopInfoTa(int userId) {
        UserDetailActivity.startUserDetailAct(homeActivity, userId, false);
    }

    public void onPopPinbbiTa(int inviteId) {
        HashMap params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("inviteId", inviteId + "");
        HttpLoader.post(ConstantsWhatNSM.URL_ADD_SHIELD, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ADD_SHIELD, InviteFragment.this, false).setTag(this);
    }

    private void joinDate(int i) {
        HomeResponse.DataBean.InvitesBean invitesBean = homeResponse.getData().getInvites().get(i);
        String user_id = String.valueOf(invitesBean.getUser_id());
        if (NSMTypeUtils.isMyUserId(String.valueOf(user_id))) {
            ALog.i("是自己的,跳入发单者的邀请详情界面");
            homeActivity.showToastInfo("不能加入自己的发单");
        } else {
            storeName = invitesBean.getStores_name();
            servicesLogo = invitesBean.getServices_logofile();
            title = invitesBean.getInvite_title();
            time = invitesBean.getInvite_time();
            payType = invitesBean.getInvite_payType();
            serviceId = invitesBean.getServices_id();

            requestJoin();

//            ALog.i("不是自己的,获得邀请基本信息");
//            HashMap<String, String> params = new HashMap<>();
//            params.put("inviteId", invitesBean.getInvite_id() + "");
//            params.put("token", NSMTypeUtils.getMyToken());
//            HttpLoader.get(ConstantsWhatNSM.URL_INVITE_JOINER, params, InviteDetailResponse.class,
//                    ConstantsWhatNSM.REQUEST_CODE_INVITE_JOINER, this).setTag(this);
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        ALog.d(requestCode + error.getMessage());

        homeActivity.showToastError("网络访问失败了,请您检查一下网络设置吧");
        mLv.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != homeAdapter && null != homeAdapter.getMp()) {
            try {
                homeAdapter.getMp().stop();
                homeAdapter.getMp().release();
            } catch (Exception e) {

            }
        }
    }

    public void requestStopMediaplayer() {
        if (homeAdapter != null) {
            homeAdapter.onDestory();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void showToast(String str) {
        homeActivity.showToastInfo(str);
    }

}
