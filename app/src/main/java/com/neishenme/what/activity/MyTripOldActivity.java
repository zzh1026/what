package com.neishenme.what.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.hedgehog.ratingbar.RatingBar;
import com.neishenme.what.R;
import com.neishenme.what.baidumap.ui.SharedLocationV5Activity;
import com.neishenme.what.baidumap.utils.LocationToBaiduMap;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.InviteReasonListResponse;
import com.neishenme.what.bean.InviteSetoutResponse;
import com.neishenme.what.bean.MyTripResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.bean.SocketResultBean;
import com.neishenme.what.bean.SocketSendBean;
import com.neishenme.what.dialog.InviteTimeOutDialog;
import com.neishenme.what.eventbusobj.InviteDetailBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.LocationUtils;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.view.RadiusImageViewFour;

import org.seny.android.utils.ActivityUtils;

import java.util.HashMap;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;
import de.greenrobot.event.EventBus;

/**
 * 作者：zhaozh create on 2017/3/1 15:00
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 我的行程 界面 ,进来方式是从我的活动中 正在进行中的活动和历史活动的条目点击而来
 * .
 * 其作用是 :
 */

/**
 * 由于v5.1.8的改版 已经过时了, 改版需求是: 删除1个地图, 修改主体ui , 修改部分逻辑
 *  2017/5/12
 */
@Deprecated
public class MyTripOldActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener {
    //标记应该更新那个时间
//    private static final long TIME_ALL_TIME = 1000 * 60 * 60 * 4;  //标记对比的时间,来显示还剩余时间的百分比,4个小时
    private static final long TIME_ALL_TIME = 1000 * 60 * 30;  //标记对比的时间,测试时间 30分钟
    private static final int TIME_REFRESH_START_NORMAL = 0;       //已经确认完成,不需更新任何数据
    private static final int TIME_REFRESH_START_GO = 100;         //立即出发的时间
    private static final int TIME_REFRESH_START_IN = 200;         //确认到达
    private static final int TIME_REFRESH_START_FACE = 300;       //确认见面
    private static final int TIME_REFRESH_START_FINISH = 400;     //确认完成
    private TimeTickBroadCast mTimeTickBroadCast;       //接收时间变化的广播
    private int mTimeRefreshState = TIME_REFRESH_START_GO;      //标记应该刷新那个时间
    private int mCurrentSetOutState = TIME_REFRESH_START_GO;      //标记现在的setoutState状态

    private final int SHOULD_REQUEST_OTHER_LOCATION = 1;
    private final int REQUEST_OTHER_LOCATION_TIME = 1000 * 5;

    //头部取消和查看更多
    private ImageView mMyTripCancel;
    private TextView mMyTripDetailInfo;

    //邀请详情的信息头部
    private RadiusImageViewFour mMyTripMylogo;
    private TextView mMyTripTitle;
    private TextView mMyTripTime;
    private CircleProgressBar mMyTripProgressTime;
    private TextView mMyTripContentTime;
    private CountdownView mMyTripNumberTime;

    //group1, 永远显示的立即出发
    private TextView mTripGroup1Time;
    private TextView mTripGroup1Discribe;
    private ImageView mTripGroup1Start;

    //group2, 第一张地图
    private LinearLayout mTripGroup2;
    private TextureMapView mTripGroup2Map;

    //group3, 确认到达
    private LinearLayout mTripGroup3;
    private TextView mTripGroup3Time;
    private TextView mTripGroup3Discribe;
    private ImageView mTripGroup3Start;

    //group4, 第二张地图
    private LinearLayout mTripGroup4;
    private TextureMapView mTripGroup4Map;

    //group5, 确认见面
    private LinearLayout mTripGroup5;
    private TextView mTripGroup5Time;
    private TextView mTripGroup5Describe;
    private ImageView mTripGroup5Start;

    //group6, 用户见面信息
    private LinearLayout mTripGroup6;
    private RadiusImageViewFour mTripGroup6Mylogo;
    private RadiusImageViewFour mTripGroup6Otherlogo;

    //group7, 确认完成
    private LinearLayout mTripGroup7;
    private TextView mTripGroup7Time;
    private TextView mTripGroup7Discribe;
    private ImageView mTripGroup7Start;

    //group8, 选择并打分
    private LinearLayout mTripGroup8;
    private RatingBar mTripGroup8Rating;
    private TextView my_trip_rating_des;

    //提交按钮
    private Button mMyTripSubmit;

    private MyTripOldActivity INSTENCE;    //自己的引用,防止多地方持有
    private int mInviteFlag;        //标记邀请的方式, 1是自己发布的, 2是别人发布的
    private int mRatingCount = 0;   //标记用户选择星的数量

    //扫描需要的四个参数
    private String mInviteId;       //标记当前的inviteId
    private String joinerId;        //加入id
    private String targetId;        //本人id  自己
    private String publisherId;     //发布id

    private String mCancelJoinerId;

    private long mInviteTime;   //标记活动的时间

    private double mInviteLatitude;   //商家位置 ,在查看商家位置和共享位置初始化商家位置的时候需要
    private double mInviteLongitude;
    private double otherLatitude = 0;   //对方位置 ,在共享位置初始化对方位置时候需要
    private double otherLongitude = 0;
    private String otherLogo;  //别人的头像
    private String myLogo;  //自己的头像

    //用户用于地图上的头像信息
    BitmapDescriptor myLocation = BitmapDescriptorFactory
            .fromResource(R.drawable.shared_loc_user_header_logo);
    private BaiduMap mMap1;     //地图1
    private BaiduMap mMap2;     //地图2
    private Marker mMap1Marker; //地图1的marker
    private Marker mMap2Marker; //地图2的marker
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private boolean isFirstLoc = true;

    private boolean isSocketSuccess = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOULD_REQUEST_OTHER_LOCATION: //应该连接socket
                    requestLoginSocket();
                    break;
            }
        }
    };

    @Override
    protected int initContentView() {
        return R.layout.activity_my_old_trip;
    }

    @Override
    protected void initView() {
        mMyTripCancel = (ImageView) findViewById(R.id.my_trip_cancel);
        mMyTripDetailInfo = (TextView) findViewById(R.id.my_trip_detail_info);

        mMyTripMylogo = (RadiusImageViewFour) findViewById(R.id.my_trip_mylogo);
        mMyTripTitle = (TextView) findViewById(R.id.my_trip_title);
        mMyTripTime = (TextView) findViewById(R.id.my_trip_time);
        mMyTripProgressTime = (CircleProgressBar) findViewById(R.id.my_trip_progress_time);
        mMyTripContentTime = (TextView) findViewById(R.id.my_trip_content_time);
        mMyTripNumberTime = (CountdownView) findViewById(R.id.my_trip_number_time);

        mTripGroup1Time = (TextView) findViewById(R.id.trip_group_1_time);
        mTripGroup1Discribe = (TextView) findViewById(R.id.trip_group_1_discribe);
        mTripGroup1Start = (ImageView) findViewById(R.id.trip_group_1_start);

        mTripGroup2 = (LinearLayout) findViewById(R.id.trip_group_2);
        mTripGroup2Map = (TextureMapView) findViewById(R.id.trip_group_2_map);

        mTripGroup3 = (LinearLayout) findViewById(R.id.trip_group_3);
        mTripGroup3Time = (TextView) findViewById(R.id.trip_group_3_time);
        mTripGroup3Discribe = (TextView) findViewById(R.id.trip_group_3_discribe);
        mTripGroup3Start = (ImageView) findViewById(R.id.trip_group_3_start);

        mTripGroup4 = (LinearLayout) findViewById(R.id.trip_group_4);
        mTripGroup4Map = (TextureMapView) findViewById(R.id.trip_group_4_map);

        mTripGroup5 = (LinearLayout) findViewById(R.id.trip_group_5);
        mTripGroup5Time = (TextView) findViewById(R.id.trip_group_5_time);
        mTripGroup5Describe = (TextView) findViewById(R.id.trip_group_5_describe);
        mTripGroup5Start = (ImageView) findViewById(R.id.trip_group_5_start);

        mTripGroup6 = (LinearLayout) findViewById(R.id.trip_group_6);
        mTripGroup6Mylogo = (RadiusImageViewFour) findViewById(R.id.trip_group_6_mylogo);
        mTripGroup6Otherlogo = (RadiusImageViewFour) findViewById(R.id.trip_group_6_otherlogo);

        mTripGroup7 = (LinearLayout) findViewById(R.id.trip_group_7);
        mTripGroup7Time = (TextView) findViewById(R.id.trip_group_7_time);
        mTripGroup7Discribe = (TextView) findViewById(R.id.trip_group_7_discribe);
        mTripGroup7Start = (ImageView) findViewById(R.id.trip_group_7_start);

        mTripGroup8 = (LinearLayout) findViewById(R.id.trip_group_8);
        mTripGroup8Rating = (RatingBar) findViewById(R.id.trip_group_8_rating);
        my_trip_rating_des = (TextView) findViewById(R.id.my_trip_rating_des);

        mMyTripSubmit = (Button) findViewById(R.id.my_trip_submit);
    }

    @Override
    protected void initListener() {
        mMyTripCancel.setOnClickListener(this);
        mMyTripDetailInfo.setOnClickListener(this);
        mTripGroup1Start.setOnClickListener(this);
        mTripGroup3Start.setOnClickListener(this);
        mTripGroup4Map.setOnClickListener(this);
        mTripGroup5Start.setOnClickListener(this);
        mTripGroup7Start.setOnClickListener(this);
        mMyTripSubmit.setOnClickListener(this);
        mTripGroup8Rating.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float RatingCount) {
                mRatingCount = (int) RatingCount;
                mMyTripSubmit.setEnabled(true);
            }
        });
    }

    @Override
    protected void initData() {
        //给自己赋值
        INSTENCE = this;
        mInviteId = getIntent().getStringExtra("data");
        initMap1();
        initMap2();

        EventBus.getDefault().register(this);
        if (!"200".equals(getIntent().getStringExtra("status"))) {
            requestOtherLoc();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestData();
    }

    //请求登录socket
    private void requestLoginSocket() {
        if (!isSocketSuccess) {  //未成功,继续调用
            requestOtherLoc();
        }
    }

    private void requestOtherLoc() {
        if (isSocketSuccess) {
            return;
        }
        mHandler.sendEmptyMessageDelayed(SHOULD_REQUEST_OTHER_LOCATION, REQUEST_OTHER_LOCATION_TIME);
        LocationUtils.getLocationByGCJ_02(new LocationUtils.OnGetLocationListener() {
            @Override
            public void onGetLocation(double latitude, double longgitude, BDLocation location) {
                SocketSendBean socketSendBean =
                        NSMTypeUtils.getSocketSendBean(NSMTypeUtils.RequestType.TARGET,
                                location.getLatitude(), location.getLongitude(), Integer.parseInt(mInviteId));
                EventBus.getDefault().post(socketSendBean);
            }

            @Override
            public void onGetError() {

            }
        });
    }

    public void onEventMainThread(SocketResultBean socketResultBean) {
        if (socketResultBean.getType().equals("pushlocation")) {    //推送位置
            SocketResultBean.DataEntity dataEntity = socketResultBean.getData();
            if (null != dataEntity) {
                otherLatitude = dataEntity.getLatitude();
                otherLongitude = dataEntity.getLongitude();
                isSocketSuccess = true;
                mHandler.removeMessages(SHOULD_REQUEST_OTHER_LOCATION);
            }
        }
        if (socketResultBean.getType().equals("message") && socketResultBean.getCode() == 200) {    //推送位置
            isSocketSuccess = true;
            mHandler.removeMessages(SHOULD_REQUEST_OTHER_LOCATION);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_trip_cancel:
                finish();
                break;
            case R.id.my_trip_detail_info:  //进入邀请详情界面
                toInviteDetailAct();
                break;
            case R.id.trip_group_4_map:     //进入位置共享界面
//                toSharedLocationAct();
                break;
            case R.id.trip_group_1_start:   //立即出发
                mTripGroup1Start.setClickable(false);
                getStartNow(mCurrentSetOutState);
                break;
            case R.id.trip_group_3_start:   //确认到达
                mTripGroup3Start.setClickable(false);
                getStartNow(mCurrentSetOutState);
                break;
            case R.id.trip_group_5_start:   //确认见面
                mTripGroup5Start.setClickable(false);
                InviteDetailBean inviteDetailBean = new InviteDetailBean(mInviteId, joinerId, targetId, publisherId);
                ZXingGetActivity.startZXingAct(INSTENCE, inviteDetailBean);
                break;
            case R.id.trip_group_7_start:   //确认完成
                mTripGroup7Start.setClickable(false);
                getStartNow(mCurrentSetOutState);
                break;
            case R.id.my_trip_submit:       //提交
                mMyTripSubmit.setClickable(false);
                submitStar();
                break;
            default:
                break;
        }
    }

    private void requestData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("inviteId", mInviteId);
        HttpLoader.post(ConstantsWhatNSM.URL_MY_TRIP, params, MyTripResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_MY_TRIP, INSTENCE, false).setTag(INSTENCE);
    }

    private void getStartNow(int setOutState) {
        HashMap<String, String> params = new HashMap<>();
        params.put("inviteId", mInviteId);
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("setout", String.valueOf(setOutState));
        HttpLoader.post(ConstantsWhatNSM.URL_INVITE_SETOUT, params, InviteSetoutResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_INVITE_SETOUT, INSTENCE).setTag(INSTENCE);
    }

    private void submitStar() {
        if (mRatingCount == 0) {
            showToastWarning("请选择评分");
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("inviteId", mInviteId);
        params.put("gradeLevel", String.valueOf(mRatingCount));
        HttpLoader.post(ConstantsWhatNSM.URL_SUBMIT_GRADELEVEL, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_SUBMIT_GRADELEVEL, INSTENCE, false).setTag(INSTENCE);
    }

    private void toInviteDetailAct() {
        if (mInviteFlag == 1) {
            ActivityUtils.startActivityForData(
                    INSTENCE, InviteInviterDetailActivity.class, mInviteId);
        } else {
            ActivityUtils.startActivityForData(
                    INSTENCE, InviteJoinerDetailActivity.class, mInviteId);
        }
    }

    private void toSharedLocationAct() {
        if (mCurrentSetOutState == TIME_REFRESH_START_GO || mCurrentSetOutState == TIME_REFRESH_START_IN) {
            SharedLocationV5Activity.startSharedLocationAct(
                    INSTENCE, mInviteLatitude, mInviteLongitude,
                    otherLatitude, otherLongitude, otherLogo, myLogo, mInviteId);
        }
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_MY_TRIP
                && response instanceof MyTripResponse) {
            MyTripResponse myTripResponse = (MyTripResponse) response;
            int code = myTripResponse.getCode();
            if (1 == code) {
                dispathInfoData(myTripResponse.getData());
            } else {
                showToastInfo(myTripResponse.getMessage());
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_SETOUT
                && response instanceof InviteSetoutResponse) {
            InviteSetoutResponse inviteSetoutResponse = (InviteSetoutResponse) response;
            int code = inviteSetoutResponse.getCode();
            if (1 == code) {
                requestData();
            } else {
                showToastInfo(inviteSetoutResponse.getMessage());
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_SUBMIT_GRADELEVEL
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse inviteSetoutResponse = (SendSuccessResponse) response;
            int code = inviteSetoutResponse.getCode();
            if (1 == code) {
//                requestData();
                setResult(RESULT_OK);
                finish();
            } else {
                showToastInfo(inviteSetoutResponse.getMessage());
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_REASON_LIST
                && response instanceof InviteReasonListResponse) {
            InviteReasonListResponse inviteReasonListResponse = (InviteReasonListResponse) response;
            int code = inviteReasonListResponse.getCode();
            if (1 == code) {
                if (inviteReasonListResponse.getData().getReasons() != null
                        && inviteReasonListResponse.getData().getReasons().size() != 0) {
//                    InviteTimeOutDialog inviteTimeOutDialog = new InviteTimeOutDialog(this,
//                            inviteReasonListResponse.getData().getReasons(),
//                            new InviteTimeOutDialog.OnInviteTimeOutCallback() {
//                                @Override
//                                public void onInviteTimeOutCall(int type, String reason) {
//                                    inviteTimeOutReason(type, reason);
//                                }
//                            });
//                    inviteTimeOutDialog.show();
                }
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_TIMEOUT_INVITE
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            int code = sendSuccessResponse.getCode();
            if (1 == code) {
                finish();
            } else {
                showToastError(sendSuccessResponse.getMessage());
            }
        }
    }

    private void inviteTimeOutReason(int type, String reason) {
        HashMap<String, String> pamers = new HashMap<>();
        pamers.put("token", NSMTypeUtils.getMyToken());
        pamers.put("cancelReason", reason == null ? "" : reason);
        pamers.put("operateStatus", String.valueOf(type));
        if (mInviteFlag == 1) { //自己的单
            pamers.put("inviteId", mInviteId);
            HttpLoader.post(ConstantsWhatNSM.URL_INVITE_TIMEOUT_INVITE, pamers, SendSuccessResponse.class,
                    ConstantsWhatNSM.REQUEST_CODE_INVITE_TIMEOUT_INVITE, this, false).setTag(this);
        } else {    //别人发的单
            pamers.put("joinerId", mCancelJoinerId);
            HttpLoader.post(ConstantsWhatNSM.URL_INVITE_TIMEOUT_JOIN, pamers, SendSuccessResponse.class,
                    ConstantsWhatNSM.REQUEST_CODE_INVITE_TIMEOUT_INVITE, this, false).setTag(this);
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_SETOUT) {
            showToastError("网络连接失败");
            requestData();
        }
    }

    private void dispathInfoData(MyTripResponse.DataBean data) {
        if (data == null) {
            return;
        }
        mInviteFlag = data.getFlag();
        publisherId = String.valueOf(data.getInviteUserId());
        joinerId = String.valueOf(data.getJoinerUserId());

        mCancelJoinerId = data.getJoinerId();
        if (mInviteFlag == 1) { //自己的单
            targetId = publisherId;
            otherLogo = data.getJoinerUserLogo();
            myLogo = data.getInviteUserLogo();
        } else {    //别人发的单
            targetId = joinerId;
            otherLogo = data.getInviteUserLogo();
            myLogo = data.getJoinerUserLogo();
        }
        disPathShowHideActInfo(data.getJourneyLists());   //控制界面group的显隐
        disPathHeaderInfo(data);    //分发头部信息
        disPathDataActInfo(data.getJourneyLists());   //控制界面group的数据

        regestTimeTickBroadCast();      //注册时间broadCast

        upTimeProgressBar();    //更新时间的进度条

//        if (data.getOperatestatus() == 0) {
//            getTimeOutInfo();   //获取超时30分钟弹窗信息
//        }

    }

    private void disPathHeaderInfo(MyTripResponse.DataBean data) {
        if (!TextUtils.isEmpty(myLogo)) {
            HttpLoader.getImageLoader().get(myLogo,
                    ImageLoader.getImageListener(mMyTripMylogo, R.drawable.picture_moren, R.drawable.picture_moren));
        }
        mInviteTime = data.getInvite_time();
        if (TimeUtils.isToday(mInviteTime)) {
            mMyTripTime.setText("活动时间: " + TimeUtils.getTime(mInviteTime));
        } else {
            mMyTripTime.setText("活动时间: " + TimeUtils.getTime(mInviteTime, TimeUtils.DATE_FORMAT_NSM));
        }
        mMyTripTitle.setText(data.getInvite_title());
        if (data.getJourneyLists() != null && data.getJourneyLists().size() == 4) {
            mMyTripContentTime.setText("已完成");
            mMyTripNumberTime.setVisibility(View.GONE);
            mMyTripProgressTime.setProgress(100);
        } else {
            if (mInviteTime > System.currentTimeMillis()) {
                mMyTripContentTime.setText("剩余");
                mMyTripNumberTime.setVisibility(View.VISIBLE);
                mMyTripNumberTime.start(mInviteTime - System.currentTimeMillis() - 200);
                mMyTripNumberTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        mMyTripContentTime.setText("已超时");
                        mMyTripNumberTime.setVisibility(View.GONE);
                        mMyTripProgressTime.setProgress(0);
                    }
                });
            } else {
                mMyTripContentTime.setText("已超时");
                mMyTripNumberTime.setVisibility(View.GONE);
                mMyTripProgressTime.setProgress(0);
            }
        }

        mInviteLatitude = data.getInvite_latitude();
        mInviteLongitude = data.getInvite_longitude();

    }

    private void disPathShowHideActInfo(List<MyTripResponse.DataBean.JourneyListsBean> journeyLists) {
        int size;
        if (journeyLists == null) {
            size = 0;
        } else {
            size = journeyLists.size();
        }
        switch (size) {
            case 5:
            case 4:
                mTripGroup7Start.setVisibility(View.INVISIBLE);
                mTripGroup7Discribe.setVisibility(View.VISIBLE);
            case 3:
                mTripGroup5Start.setVisibility(View.INVISIBLE);
                mTripGroup5Describe.setVisibility(View.VISIBLE);
            case 2:
                mTripGroup3Start.setVisibility(View.INVISIBLE);
                mTripGroup3Discribe.setVisibility(View.VISIBLE);
            case 1:
                mTripGroup1Start.setVisibility(View.INVISIBLE);
                mTripGroup1Discribe.setVisibility(View.VISIBLE);
                break;
        }
        switch (size) {
            case 0:     //代表没出发
                mTimeRefreshState = TIME_REFRESH_START_GO;
                mCurrentSetOutState = TIME_REFRESH_START_GO;
                mTripGroup2.setVisibility(View.GONE);
                mTripGroup3.setVisibility(View.GONE);
                mTripGroup4.setVisibility(View.GONE);
                mTripGroup5.setVisibility(View.GONE);
                mTripGroup6.setVisibility(View.GONE);
                mTripGroup7.setVisibility(View.GONE);
                mTripGroup8.setVisibility(View.GONE);
                mMyTripSubmit.setVisibility(View.GONE);
                mTripGroup1Start.setVisibility(View.VISIBLE);
                mTripGroup1Start.setClickable(true);
                break;
            case 1:     //已经立即出发
                mTimeRefreshState = TIME_REFRESH_START_IN;
                mCurrentSetOutState = TIME_REFRESH_START_IN;
                mTripGroup2.setVisibility(View.VISIBLE);
                mTripGroup3.setVisibility(View.VISIBLE);
                mTripGroup4.setVisibility(View.VISIBLE);
                mTripGroup5.setVisibility(View.GONE);
                mTripGroup6.setVisibility(View.GONE);
                mTripGroup7.setVisibility(View.GONE);
                mTripGroup8.setVisibility(View.GONE);
                mMyTripSubmit.setVisibility(View.GONE);
                mTripGroup3Start.setVisibility(View.VISIBLE);
                mTripGroup3Start.setClickable(true);
                break;
            case 2:     //已经确认到达
                mTimeRefreshState = TIME_REFRESH_START_FACE;
                mCurrentSetOutState = TIME_REFRESH_START_FACE;
                mTripGroup2.setVisibility(View.VISIBLE);
                mTripGroup3.setVisibility(View.VISIBLE);
                mTripGroup4.setVisibility(View.VISIBLE);
                mTripGroup5.setVisibility(View.VISIBLE);
                mTripGroup6.setVisibility(View.GONE);
                mTripGroup7.setVisibility(View.GONE);
                mTripGroup8.setVisibility(View.GONE);
                mMyTripSubmit.setVisibility(View.GONE);
                mTripGroup5Start.setVisibility(View.VISIBLE);
                mTripGroup5Start.setClickable(true);
                break;
            case 3:     //已经确认见面
                mTimeRefreshState = TIME_REFRESH_START_FINISH;
                mCurrentSetOutState = TIME_REFRESH_START_FINISH;
                mTripGroup2.setVisibility(View.VISIBLE);
                mTripGroup3.setVisibility(View.VISIBLE);
                mTripGroup4.setVisibility(View.VISIBLE);
                mTripGroup5.setVisibility(View.VISIBLE);
                mTripGroup6.setVisibility(View.VISIBLE);
                mTripGroup7.setVisibility(View.VISIBLE);
                mTripGroup8.setVisibility(View.GONE);
                mMyTripSubmit.setVisibility(View.GONE);
                mTripGroup7Start.setVisibility(View.VISIBLE);
                mTripGroup7Start.setClickable(true);
                break;
            case 4:     //已经确认完成
            case 5:
            default:
                mTimeRefreshState = TIME_REFRESH_START_NORMAL;
                mCurrentSetOutState = TIME_REFRESH_START_NORMAL;
                mTripGroup2.setVisibility(View.VISIBLE);
                mTripGroup3.setVisibility(View.VISIBLE);
                mTripGroup4.setVisibility(View.VISIBLE);
                mTripGroup5.setVisibility(View.VISIBLE);
                mTripGroup6.setVisibility(View.VISIBLE);
                mTripGroup7.setVisibility(View.VISIBLE);
                mTripGroup8.setVisibility(View.GONE);
                mMyTripSubmit.setVisibility(View.GONE);
                break;
        }
    }

    private void disPathDataActInfo(List<MyTripResponse.DataBean.JourneyListsBean> journeyLists) {
        if (null == journeyLists) {
            return;
        }
        //标记map2的状态,如果size为0和1说明地图是自动更新的,如果大于等于2说明是按死的,就不用管了
        boolean isUpMap2 = true;
        if (journeyLists.size() >= 2) {
            isUpMap2 = false;
        }
        upTimeTick();
        if (journeyLists.size() != 0) {
            for (MyTripResponse.DataBean.JourneyListsBean journeyListsBean : journeyLists) {
                int setout_status = journeyListsBean.getSetout_status();
                long createtime = journeyListsBean.getCreatetime();
                switch (setout_status) {
                    case TIME_REFRESH_START_GO: //已经出发,更新group1和group2
                        setTripTime(mTripGroup1Time, createtime);
                        upDateMap1(journeyListsBean.getLatitude(), journeyListsBean.getLongitude());   //更新map1的状态
                        if (isUpMap2) {
                            openMyLocationOnMap2();  //此时说明是自动更新的,所以在map2中开启自己的位置
                        }
                        break;
                    case TIME_REFRESH_START_IN: //已经确认到达,更新group3和group4
                        setTripTime(mTripGroup3Time, createtime);
                        upDateMap2(journeyListsBean.getLatitude(), journeyListsBean.getLongitude());   //更新map2的状态
                        break;
                    case TIME_REFRESH_START_FACE:   //已经确认见面,更新group5和group6
                        setTripTime(mTripGroup5Time, createtime);
                        disPathUserHeader();
                        break;
                    case TIME_REFRESH_START_FINISH: //已经完成
                        setTripTime(mTripGroup7Time, createtime);
                        if (journeyListsBean.getGradelevel() <= 0) {    //如果没有评分
                            mTripGroup8.setVisibility(View.VISIBLE);
                            mMyTripSubmit.setVisibility(View.VISIBLE);
                            mMyTripSubmit.setClickable(true);
                            mMyTripSubmit.setEnabled(mRatingCount != 0);
                        } else {
                            mTripGroup8.setVisibility(View.VISIBLE);
                            mTripGroup8Rating.setVisibility(View.VISIBLE);
                            mTripGroup8Rating.setStar(journeyListsBean.getGradelevel());
                            mTripGroup8Rating.setStarCount(5 - journeyListsBean.getGradelevel());
                            mTripGroup8Rating.setmClickable(false);
                            my_trip_rating_des.setVisibility(View.GONE);
                            mMyTripSubmit.setVisibility(View.GONE);
                        }
                        break;
                }
            }
        }
    }

    private void getTimeOutInfo() {
        //未扫码完成
        if (mTimeRefreshState == TIME_REFRESH_START_GO || mTimeRefreshState == TIME_REFRESH_START_IN
                || mTimeRefreshState == TIME_REFRESH_START_FACE) {
            //超时30分钟
            if (mInviteTime < System.currentTimeMillis()
                    && (System.currentTimeMillis() - mInviteTime) >= (30 * 60 * 1000)) {
                HttpLoader.get(ConstantsWhatNSM.URL_INVITE_REASON_LIST, null, InviteReasonListResponse.class,
                        ConstantsWhatNSM.REQUEST_CODE_INVITE_REASON_LIST, this, false).setTag(this);
            }
        }
    }

    //注册时间监听
    private void regestTimeTickBroadCast() {
        mTimeTickBroadCast = new TimeTickBroadCast();
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(mTimeTickBroadCast, filter);
    }

    private void upTimeTick() {
        switch (mTimeRefreshState) {
            case TIME_REFRESH_START_GO:
                setTripTime(mTripGroup1Time, System.currentTimeMillis());
                break;
            case TIME_REFRESH_START_IN:
                setTripTime(mTripGroup3Time, System.currentTimeMillis());
                break;
            case TIME_REFRESH_START_FACE:
                setTripTime(mTripGroup5Time, System.currentTimeMillis());
                break;
            case TIME_REFRESH_START_FINISH:
                setTripTime(mTripGroup7Time, System.currentTimeMillis());
                break;
            default:
                break;
        }
    }

    private void upTimeProgressBar() {
        if (mTimeRefreshState == TIME_REFRESH_START_NORMAL) {
            return;
        }
        long time = mInviteTime - System.currentTimeMillis();
        if (time > 0) {
            if (time > TIME_ALL_TIME) {
                mMyTripProgressTime.setProgress(100);
            } else {
                mMyTripProgressTime.setProgress((int) (time * 100 / TIME_ALL_TIME));
            }
        } else {        //说明已经超时了
            mMyTripProgressTime.setProgress(0);
        }
    }

    //为目标textview更新时间
    private void setTripTime(TextView mTripTimeTarget, long createTime) {
        if (TimeUtils.isToday(createTime)) {
            mTripTimeTarget.setText("今天 " + TimeUtils.getTime(createTime));
        } else {
            mTripTimeTarget.setText(TimeUtils.getTime(createTime, TimeUtils.DATE_FORMAT_NSM));
        }
    }

    private void initMap1() {
        mMap1 = mTripGroup2Map.getMap();
        mMap1.getUiSettings().setAllGesturesEnabled(false);
    }

    private void initMap2() {
        mMap2 = mTripGroup4Map.getMap();
        mMap2.getUiSettings().setAllGesturesEnabled(false);
        mMap2.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                toSharedLocationAct();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                toSharedLocationAct();
                return false;
            }
        });
    }

    private void upDateMap1(double latitude, double longitude) {
        LatLng latLng = LocationToBaiduMap.toBaiduMapLocation(LocationToBaiduMap.MapType.NORMAL, latitude, longitude);
        if (mMap1Marker == null) {
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(latLng).zoom(16.0f);
            mMap1.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            MarkerOptions ooA = new MarkerOptions().position(latLng).icon(myLocation)
                    .zIndex(5).draggable(false);
            ooA.animateType(MarkerOptions.MarkerAnimateType.grow);
            mMap1Marker = (Marker) mMap1.addOverlay(ooA);
        } else {
            mMap1Marker.setPosition(latLng);
        }
    }

    private void upDateMap2(double latitude, double longitude) {
        stopLocClient();
        mMap2.setMyLocationEnabled(false);
        LatLng latLng = LocationToBaiduMap.toBaiduMapLocation(LocationToBaiduMap.MapType.NORMAL, latitude, longitude);
        if (mMap2Marker == null) {
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(latLng).zoom(16.0f);
            mMap2.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            MarkerOptions ooA = new MarkerOptions().position(latLng).icon(myLocation)
                    .zIndex(5).draggable(false);
            ooA.animateType(MarkerOptions.MarkerAnimateType.grow);
            mMap2Marker = (Marker) mMap2.addOverlay(ooA);
        } else {
            mMap2Marker.setPosition(latLng);
        }
    }

    private void openMyLocationOnMap2() {
        // 开启定位图层
        mMap2.setMyLocationEnabled(true);
        mLocClient = new LocationClient(INSTENCE);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setNeedDeviceDirect(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    private void disPathUserHeader() {
        if (!TextUtils.isEmpty(myLogo)) {
            //活动详情中自己头像显示
            HttpLoader.getImageLoader().get(myLogo,
                    ImageLoader.getImageListener(mTripGroup6Mylogo, R.drawable.picture_moren, R.drawable.picture_moren));
        }
        if (!TextUtils.isEmpty(otherLogo)) {
            //活动详情中对方的头像
            HttpLoader.getImageLoader().get(otherLogo,
                    ImageLoader.getImageListener(mTripGroup6Otherlogo, R.drawable.picture_moren, R.drawable.picture_moren));
        }
    }

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
//                    .accuracy(location.getRadius())
//                    .direction(location.getDirection())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mMap2.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mMap2.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    @Override
    protected void onPause() {
        mTripGroup2Map.onPause();
        mTripGroup4Map.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mTripGroup2Map.onResume();
        mTripGroup4Map.onResume();
        super.onResume();
    }

    private void recyclerBitmap() {
        if (myLocation != null) {
            myLocation.recycle();
        }
    }

    private void stopLocClient() {
        if (mLocClient != null) {
            mLocClient.unRegisterLocationListener(myListener);
            myListener = null;
            mLocClient.stop();
            mLocClient = null;
        }
    }

    @Override
    protected void onDestroy() {
        stopLocClient();
        mMap2.setMyLocationEnabled(false);
        mTripGroup2Map.onDestroy();
        mTripGroup4Map.onDestroy();
        mTripGroup2Map = null;
        mTripGroup4Map = null;
        if (mTimeTickBroadCast != null) {
            unregisterReceiver(mTimeTickBroadCast);
        }
        EventBus.getDefault().unregister(this);
        isSocketSuccess = true;
        mHandler.removeMessages(SHOULD_REQUEST_OTHER_LOCATION);
        super.onDestroy();
        recyclerBitmap();
    }

    public class TimeTickBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    upTimeTick();
                    upTimeProgressBar();
                }
            });
        }
    }
}
