package com.neishenme.what.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.neishenme.what.R;
import com.neishenme.what.adapter.ActiveAdapter;
import com.neishenme.what.baidumap.NSMMapHelper;
import com.neishenme.what.baidumap.service.LocationService;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.ActiveCurrentSharedInfos;
import com.neishenme.what.bean.ActiveListResponse;
import com.neishenme.what.bean.ActiveTitleResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.dialog.ActiveTicketSubDialog;
import com.neishenme.what.eventbusobj.ActiveSharedBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.DensityUtil;
import com.neishenme.what.utils.NSMTypeUtils;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import jp.wasabeef.blurry.Blurry;

/**
 * 一元购 的活动主界面..
 */
public class ActiveActivity extends BaseActivity implements HttpLoader.ResponseListener {
    public static final int MESSAGE_START_SHOW_ANIMATION = 0;   //开始展现
    public static final int MESSAGE_START_HIDE_ANIMATION = 1;   //开始隐藏

    public static final int JOIN_PAY_REQUEST_CODE = 10;   //开始隐藏

    private ImageView mIvBack;
    private TextView mTvNear;
    private PullToRefreshGridView mActiveDetailPgv;
    private LinearLayout mActiveMenuLl;
    private RelativeLayout mRlContener;
    private TextView mActiveTvTime;
    private TextView mActiveTvPersonNum;
    private TextView mActiveTvTickets;
    private TextView mActiveTvPairing;
    private TextView mActiveTvAllTicket;
    private TextView mActiveDetailSignUp;       //我要报名

    private int page = 1;
    private boolean isShowMenu = true;  //是否正在显示最上部主题
    private boolean isCanJoin = false;   //标记是否可以加入

    private boolean send = false;   //是否获取位置
    private boolean hasMoreInfo = false;   //是否有更多
    private LocationService locationService;
    private double latitude = 0.0;    //经纬度
    private double longgitude = 0.0;

    private boolean isFromUrl = false;  //标记是否是从分享的网页跳转过来的

    private ActiveAdapter mActiveAdapter;

    private Animator mAnimatorTitle;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_START_SHOW_ANIMATION:
                    if (isShowMenu) {
                        return;
                    }
                    startTitleAnimation();
                    mHandler.removeMessages(MESSAGE_START_SHOW_ANIMATION);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_START_HIDE_ANIMATION, 4000);
                    break;
                case MESSAGE_START_HIDE_ANIMATION:
                    if (!isShowMenu) {
                        return;
                    }
                    mHandler.removeMessages(MESSAGE_START_HIDE_ANIMATION);
                    startTitleAnimation();
                    break;
                default:
                    break;
            }
        }
    };
    private List<ActiveListResponse.DataBean.TakemeoutBean> takemeoutAll;
    private ActiveSharedBean mActiveSharedBean = null;

    @Override
    protected void onStart() {
        super.onStart();
        if (isShowMenu) { //如果在展示,则4秒以后消失
            mHandler.sendEmptyMessageDelayed(MESSAGE_START_HIDE_ANIMATION, 4000);
        } else {
            mHandler.sendEmptyMessage(MESSAGE_START_SHOW_ANIMATION);
        }

        mActiveDetailPgv.setOnGridTouchListener(new PullToRefreshGridView.OnGridTouchListener() {
            @Override
            public void setOnGridTouchListener() {
                mActiveDetailPgv.removeOnGridTouchListener();
                mHandler.sendEmptyMessage(MESSAGE_START_HIDE_ANIMATION);
            }
        });

        getTitleInfos();

        mActiveAdapter = null;
        page = 1;
        getPlease();
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_activite;
    }

    @Override
    protected void initView() {
        mRlContener = (RelativeLayout) findViewById(R.id.rl_contener);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvNear = (TextView) findViewById(R.id.tv_near);
        mActiveDetailPgv = (PullToRefreshGridView) findViewById(R.id.active_detail_pgv);
        mActiveDetailPgv.setMode(PullToRefreshBase.Mode.BOTH);
        mActiveMenuLl = (LinearLayout) findViewById(R.id.active_menu_ll);
        mActiveDetailSignUp = (TextView) findViewById(R.id.active_detail_sign_up);

        mActiveTvTime = (TextView) findViewById(R.id.active_tv_time);
        mActiveTvPersonNum = (TextView) findViewById(R.id.active_tv_person_num);
        mActiveTvTickets = (TextView) findViewById(R.id.active_tv_tickets);
        mActiveTvPairing = (TextView) findViewById(R.id.active_tv_pairing);
        mActiveTvAllTicket = (TextView) findViewById(R.id.active_tv_all_ticket);

        locationService = NSMMapHelper.getInstance().locationService;
        locationService.registerListener(mListener);
    }

    @Override
    protected void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFromUrl) {
                    isFromUrl();
                } else {
                    finish();
                }
            }
        });

        mActiveDetailSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NSMTypeUtils.isLogin()) {
                    toLogin();
                    return;
                }
                if (mActiveSharedBean != null) {
                    EventBus.getDefault().postSticky(mActiveSharedBean);
                }
                ActivityUtils.startActivity(ActiveActivity.this, ActiveSignUpActivity.class);
            }
        });

        mTvNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ActivityUtils.startActivity(ActiveActivity.this, ActiveNearByActivity.class);
                ActivityUtils.startActivity(ActiveActivity.this, ActiveRuleActivity.class);
            }
        });

        mActiveDetailPgv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                mActiveAdapter = null;
                page = 1;
                getPlease();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                if (hasMoreInfo) {
                    page++;
                    getPlease();
                } else {
                    showToastInfo("暂无更多信息!");
                    mActiveDetailPgv.onRefreshComplete();
                }
            }
        });

        mActiveDetailPgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!NSMTypeUtils.isLogin()) {
                    toLogin();
                    return;
                }
                int userId = 0;
                ActiveListResponse.DataBean.TakemeoutBean takemeoutBean = takemeoutAll.get(position);
                if (takemeoutBean != null) {
                    userId = takemeoutBean.getUserId();
                }
                if (NSMTypeUtils.isMyUserId(String.valueOf(userId))) {
                    ActivityUtils.startActivity(ActiveActivity.this, MyActiveActivity.class);
                } else if (userId != 0) {
                    UserActiveDetailActivity.startUserDetailAct(ActiveActivity.this, userId, Integer.parseInt(takemeoutBean.getId()));
                } else {
                    showToastInfo("获取用户失败,请退出重试!");
                }
            }
        });

        mActiveMenuLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowMenu) {
                    mHandler.sendEmptyMessage(MESSAGE_START_SHOW_ANIMATION);
                    getTitleInfos();
                    mActiveDetailPgv.setOnGridTouchListener(new PullToRefreshGridView.OnGridTouchListener() {
                        @Override
                        public void setOnGridTouchListener() {
                            mActiveDetailPgv.removeOnGridTouchListener();
                            mHandler.sendEmptyMessage(MESSAGE_START_HIDE_ANIMATION);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isFromUrl) {
            isFromUrl();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void initData() {
        getAllActive();
        getCurrentSharedInfo();
        Uri uri = getIntent().getData();
        if (uri != null) {
            isFromUrl = true;
        } else {
            isFromUrl = false;
        }
    }

    private void getCurrentSharedInfo() {
        HashMap<String, String> paerm = new HashMap<>();
        paerm.put("type", "takemeout");
        HttpLoader.post(ConstantsWhatNSM.URL_ACTIVE_GET_CURRENT_SHARED, paerm, ActiveCurrentSharedInfos.class,
                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_GET_CURRENT_SHARED, this, false).setTag(this);
    }

    private void getTitleInfos() {
        HttpLoader.post(ConstantsWhatNSM.URL_ACTIVE_ACTIVE_INFOS, null, ActiveTitleResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_ACTIVE_INFOS, this, false).setTag(this);
    }

    public void toLogin() {
        showToastInfo("请先登录帐号!");
        ActivityUtils.startActivity(this, LoginActivity.class);
    }

    private void getPlease() {
        send = true;
        locationService.setLocationOption(LocationService.CoorType.CoorType_GCJ02, LocationService.TimeType.TIME_0);
        locationService.start();
    }

    //获取所有的活动信息
    private void getAllActive() {
        HashMap params = new HashMap();
        params.put("page", page + "");
        params.put("pageSize", "20");
        params.put("longitude", longgitude + "");
        params.put("latitude", latitude + "");
        HttpLoader.post(ConstantsWhatNSM.URL_ACTIVE_TACKMEOUT_LIST, params, ActiveListResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_TACKMEOUT_LIST, this, false).setTag(this);
    }

    private void isFromUrl() {
        ActivityUtils.startActivityAndFinish(ActiveActivity.this, MainActivity.class);
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (send) {
                send = false;
                if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                    /**
                     * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                     * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                     */
                    latitude = location.getLatitude();
                    longgitude = location.getLongitude();
                    ALog.i("位置转换成功:latitude = " + latitude + " longitude = " + longgitude);
                }
                getAllActive();
                locationService.stop();
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    //开启动画
    private void startTitleAnimation() {
        if (mAnimatorTitle != null && mAnimatorTitle.isRunning()) {
            mAnimatorTitle.cancel();
        }
        if (isShowMenu) { //如果显示,就隐藏
            mAnimatorTitle = ObjectAnimator.ofFloat(mActiveMenuLl, "translationX", mActiveMenuLl.getTranslationX(), DensityUtil.dip2px(this, 310));
        } else { //如果隐藏,就显示
            mAnimatorTitle = ObjectAnimator.ofFloat(mActiveMenuLl, "translationX", mActiveMenuLl.getTranslationX(), 0);
        }
        mAnimatorTitle.start();
        mAnimatorTitle.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isShowMenu = !isShowMenu;
            }
        });
    }

    public void ticketSub(int ticketSubPosition) {
        if (!isCanJoin) {
            showToastInfo("现在不是投票时间,不能投票");
            return;
        }
        if (!NSMTypeUtils.isLogin()) {
            toLogin();
            return;
        }
        ActiveTicketSubDialog mActiveTicketSubDialog = new ActiveTicketSubDialog(this, takemeoutAll.get(ticketSubPosition));
        mActiveTicketSubDialog.show();
        Blurry.with(ActiveActivity.this).radius(10).sampling(2).async().onto(mRlContener);
    }

    //当弹框消失的时候调用
    public void dismissDialog() {
        Blurry.delete(mRlContener);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        mActiveDetailPgv.onRefreshComplete();
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_TACKMEOUT_LIST
                && response instanceof ActiveListResponse) {
            ActiveListResponse activeListResponse = (ActiveListResponse) response;
            if (activeListResponse.getCode() == 1) {
                ActiveListResponse.DataBean data = activeListResponse.getData();
                hasMoreInfo = activeListResponse.getData().isHasMore();
                if (mActiveAdapter == null || page == 1) {
                    takemeoutAll = data.getTakemeout();
                    mActiveAdapter = new ActiveAdapter(this, takemeoutAll);
                    mActiveDetailPgv.setAdapter(mActiveAdapter);
                } else {
                    takemeoutAll.addAll(data.getTakemeout());
                    mActiveAdapter.notifyDataSetChanged();
                }

                if (null == takemeoutAll || takemeoutAll.size() == 0) {
                    return;
                }
            } else {
                showToastInfo(activeListResponse.getMessage());
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_ACTIVE_INFOS
                && response instanceof ActiveTitleResponse) {
            ActiveTitleResponse mActiveTitleResponse = (ActiveTitleResponse) response;
            if (mActiveTitleResponse.getCode() == 1) {
                ActiveTitleResponse.DataBean.ListBean listBean = mActiveTitleResponse.getData().getList();
                mActiveTvPersonNum.setText(listBean.getApplication() + "");
                mActiveTvTickets.setText(listBean.getVoters() + "");
                mActiveTvPairing.setText(listBean.getTwinning() + "");
                mActiveTvAllTicket.setText(listBean.getVotes() + "");
                mActiveTvTime.setText(NSMTypeUtils.getDealDays(listBean.getCountdown()) + "天");
            } else {
                ALog.i(mActiveTitleResponse.getMessage());
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_GET_CURRENT_SHARED
                && response instanceof ActiveCurrentSharedInfos) {
            ActiveCurrentSharedInfos mActiveCurrentSharedInfos = (ActiveCurrentSharedInfos) response;
            if (mActiveCurrentSharedInfos.getCode() == 1) {
                ActiveCurrentSharedInfos.DataBean.ActiveBean activeBean = mActiveCurrentSharedInfos.getData().getActive();
                mActiveSharedBean = new ActiveSharedBean(activeBean.getShareTitle(), activeBean.getShareLink(), activeBean.getShareDescribe(), activeBean.getShareImage());

                if (activeBean.getStartJoinTime() < System.currentTimeMillis() && System.currentTimeMillis() < activeBean.getEndJoinTime()) {
                    mActiveDetailSignUp.setVisibility(View.VISIBLE);
                    isCanJoin = true;
                } else {
                    mActiveDetailSignUp.setVisibility(View.GONE);
                    isCanJoin = false;
                }
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        mActiveDetailPgv.onRefreshComplete();
        if (requestCode != ConstantsWhatNSM.REQUEST_CODE_ACTIVE_ACTIVE_INFOS) {
            showToastError("网络获取失败,请重试");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == JOIN_PAY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                showToastSuccess("投票成功");
            }
        }
    }

    @Override
    protected void onDestroy() {
        HttpLoader.cancelRequest(this);
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onDestroy();
    }
}
