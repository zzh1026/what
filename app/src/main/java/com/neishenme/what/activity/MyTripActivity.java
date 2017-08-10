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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.hedgehog.ratingbar.RatingBar;
import com.hyphenate.easeui.EaseConstant;
import com.neishenme.what.R;
import com.neishenme.what.application.App;
import com.neishenme.what.baidumap.ui.SharedLocationV5Activity;
import com.neishenme.what.baidumap.utils.LocationToBaiduMap;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.InviteDetailGetChatResponse;
import com.neishenme.what.bean.InviteSetoutResponse;
import com.neishenme.what.bean.MyTripResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.bean.SocketResultBean;
import com.neishenme.what.bean.SocketSendBean;
import com.neishenme.what.eventbusobj.ChatInfoBean;
import com.neishenme.what.eventbusobj.InviteDetailBean;
import com.neishenme.what.huanxinchat.ui.ChatActivity;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.DensityUtil;
import com.neishenme.what.utils.HuanXinUtils;
import com.neishenme.what.utils.LocationUtils;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.view.InviteTalkAnimator;
import com.neishenme.what.view.OnsizeLinearLayout;
import com.neishenme.what.view.RadiusImageViewFour;

import org.seny.android.utils.ActivityUtils;

import java.util.HashMap;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;
import de.greenrobot.event.EventBus;

import static com.daimajia.androidanimations.library.YoYo.with;
import static com.neishenme.what.utils.HuanXinUtils.MY_HX_USERNAME;

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
public class MyTripActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener {
    //标记应该更新那个时间
    private static final long TIME_ALL_TIME = 1000 * 60 * 30;  //标记对比的时间,测试时间 30分钟
    private final int SHOULD_SHOW_TALK_ANIM = 2;        //需要展示动画

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

    //地图
    private TextureMapView mMyTripMap;

    //位置
    private LinearLayout mMyTripLocationLayout;
    private TextView mMyTripLocationDistence;
    private RelativeLayout mMyTripTalkLayout;
    private ImageView mMyTripTalkImg;

    //group1, 出发
    private LinearLayout mMyTripGroup1;
    private TextView mMyTripGroup1Time;
    private TextView mMyTripGroup1Des;
    private Button mMyTripGroup1Btn;

    //group2, 到达
    private LinearLayout mMyTripGroup2;
    private TextView mMyTripGroup2Time;
    private TextView mMyTripGroup2Des;
    private Button mMyTripGroup2Btn;

    //group3, 见面
    private LinearLayout mMyTripGroup3;
    private TextView mMyTripGroup3Time;
    private TextView mMyTripGroup3Des;
    private Button mMyTripGroup3Btn;

    //group4, 完成
    private LinearLayout mMyTripGroup4;
    private TextView mMyTripGroup4Time;
    private TextView mMyTripGroup4Des;
    private Button mMyTripGroup4Btn;

    //group5, 评分
    private LinearLayout mMyTripGroup5;
    private RatingBar mTripGroup5Rating;
    private TextView mTripGroup5Des;

    private ImageView mMyTripLeftBg;
    private RelativeLayout.LayoutParams layoutParams;
    private OnsizeLinearLayout mMyTripRightLayout;

    private YoYo.AnimationComposer talkAnim;    //提示谈话的动画
    private YoYo.AnimationComposer slideInAnim;    //提示谈话的动画
    private InviteDetailGetChatResponse.DataBean talkedInfo;    //可以聊天的时候的聊天对象信息
    private boolean isTalkShowing = false;
    private long chatTime;

    //提交
    private Button mMyTripSubmit;

    private MyTripActivity INSTENCE;    //自己的引用,防止多地方持有
    private int mInviteFlag;        //标记邀请的方式, 1是自己发布的, 2是别人发布的
    private int mRatingCount = 0;   //标记用户选择星的数量

    //扫描需要的四个参数
    private String mInviteId;       //标记当前的inviteId
    private String joinerId;        //加入id
    private String targetId;        //本人id  自己
    private String publisherId;     //发布id

    private long mInviteTime;   //标记活动的时间
    private int mStateSize;     //标记当前的加入的集合数

    private double mInviteLatitude;   //商家位置 ,在查看商家位置和共享位置初始化商家位置的时候需要
    private double mInviteLongitude;
    private double otherLatitude = 0;   //对方位置 ,在共享位置初始化对方位置时候需要
    private double otherLongitude = 0;
    private String otherLogo;  //别人的头像
    private String myLogo;  //自己的头像


    //餐厅地址 和图标
    private LatLng mLocationLoc;
    private Marker mLocationMarker;
    BitmapDescriptor mLocationHeader = BitmapDescriptorFactory
            .fromResource(R.drawable.shared_loc_address);
    private BaiduMap mBaiduMap;    //地图

    private boolean isSocketSuccess = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOULD_SHOW_TALK_ANIM: //应该展示聊天动画
                    talkAnim.playOn(mMyTripTalkImg);
                    mHandler.sendEmptyMessageDelayed(SHOULD_SHOW_TALK_ANIM, 1500);
                    break;
                case SHOULD_REQUEST_OTHER_LOCATION: //应该连接socket
                    requestLoginSocket();
                    break;
            }
        }
    };

    @Override
    protected int initContentView() {
        return R.layout.activity_my_trip;
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

        mMyTripMap = (TextureMapView) findViewById(R.id.my_trip_map);

        mMyTripLocationLayout = (LinearLayout) findViewById(R.id.my_trip_location_layout);
        mMyTripLocationDistence = (TextView) findViewById(R.id.my_trip_location_distence);
        mMyTripTalkLayout = (RelativeLayout) findViewById(R.id.my_trip_talk_layout);
        mMyTripTalkImg = (ImageView) findViewById(R.id.my_trip_talk_img);

        mMyTripGroup1 = (LinearLayout) findViewById(R.id.my_trip_group_1);
        mMyTripGroup1Time = (TextView) findViewById(R.id.my_trip_group_1_time);
        mMyTripGroup1Des = (TextView) findViewById(R.id.my_trip_group_1_des);
        mMyTripGroup1Btn = (Button) findViewById(R.id.my_trip_group_1_btn);

        mMyTripGroup2 = (LinearLayout) findViewById(R.id.my_trip_group_2);
        mMyTripGroup2Time = (TextView) findViewById(R.id.my_trip_group_2_time);
        mMyTripGroup2Des = (TextView) findViewById(R.id.my_trip_group_2_des);
        mMyTripGroup2Btn = (Button) findViewById(R.id.my_trip_group_2_btn);

        mMyTripGroup3 = (LinearLayout) findViewById(R.id.my_trip_group_3);
        mMyTripGroup3Time = (TextView) findViewById(R.id.my_trip_group_3_time);
        mMyTripGroup3Des = (TextView) findViewById(R.id.my_trip_group_3_des);
        mMyTripGroup3Btn = (Button) findViewById(R.id.my_trip_group_3_btn);

        mMyTripGroup4 = (LinearLayout) findViewById(R.id.my_trip_group_4);
        mMyTripGroup4Time = (TextView) findViewById(R.id.my_trip_group_4_time);
        mMyTripGroup4Des = (TextView) findViewById(R.id.my_trip_group_4_des);
        mMyTripGroup4Btn = (Button) findViewById(R.id.my_trip_group_4_btn);

        mMyTripGroup5 = (LinearLayout) findViewById(R.id.my_trip_group_5);
        mTripGroup5Rating = (RatingBar) findViewById(R.id.trip_group_5_rating);
        mTripGroup5Des = (TextView) findViewById(R.id.trip_group_5__des);

        mMyTripSubmit = (Button) findViewById(R.id.my_trip_submit);

        mMyTripLeftBg = (ImageView) findViewById(R.id.my_trip_left_bg);
        mMyTripRightLayout = (OnsizeLinearLayout) findViewById(R.id.my_trip_right_layout);
    }

    @Override
    protected void initListener() {
        mMyTripCancel.setOnClickListener(this);
        mMyTripDetailInfo.setOnClickListener(this);
        mMyTripGroup1Btn.setOnClickListener(this);
        mMyTripGroup2Btn.setOnClickListener(this);
        mMyTripGroup3Btn.setOnClickListener(this);
        mMyTripGroup4Btn.setOnClickListener(this);
        mMyTripSubmit.setOnClickListener(this);
        mTripGroup5Rating.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float RatingCount) {
                mRatingCount = (int) RatingCount;
                mMyTripSubmit.setEnabled(true);
            }
        });
        layoutParams = (RelativeLayout.LayoutParams) mMyTripLeftBg.getLayoutParams();
        mMyTripRightLayout.addOnSizeChangedListener(new OnsizeLinearLayout.OnSizeChanged() {
            @Override
            public void onSizeChanged(int sizeHeight) {
                layoutParams.height = sizeHeight - DensityUtil.dip2px(INSTENCE, 15);
                mMyTripLeftBg.setLayoutParams(layoutParams);
            }
        });

        mMyTripTalkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == talkedInfo) {
                    return;
                }
                if (TextUtils.isEmpty(talkedInfo.getHxUserName())) {
                    return;
                }

                mHandler.removeMessages(SHOULD_SHOW_TALK_ANIM);
                NSMTypeUtils.talkedOnclick();

                ChatInfoBean chatInfoBean = new ChatInfoBean(
                        talkedInfo.getUserlogo(),
                        talkedInfo.getUserid(),
                        talkedInfo.getUsername());
                EventBus.getDefault().postSticky(chatInfoBean);
                startActivity(new Intent(INSTENCE, ChatActivity.class)
                        .putExtra(EaseConstant.EXTRA_USER_ID, talkedInfo.getHxUserName()));
            }
        });
    }

    @Override
    protected void initData() {
        //给自己赋值
        INSTENCE = this;
        mInviteId = getIntent().getStringExtra("data");
        initBaiduMap();

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

//    private void requestMyLocation() {
//        LocationUtils.getLocationByGCJ_02(new LocationUtils.OnGetLocationListener() {
//            @Override
//            public void onGetLocation(double latitude, double longgitude, BDLocation location) {
//                if (location == null || mBaiduMap == null) {
//                    return;
//                }
//                if (isFirstLoc) {
//                    isFirstLoc = false;
//                    LatLng ll = new LatLng(location.getLatitude(),
//                            location.getLongitude());
//                    MapStatus.Builder builder = new MapStatus.Builder();
//                    builder.target(ll).zoom(18.0f);
//                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//                }
//                upDateMap1(location.getLatitude(),location.getLongitude());
//            }
//
//            @Override
//            public void onGetError() {
//
//            }
//        });
//    }

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
                try {
                    double distance = LocationToBaiduMap.getDistance(
                            otherLatitude, otherLongitude, mInviteLatitude, mInviteLongitude);
                    mMyTripLocationDistence.setText("对方距离活动地点" + LocationToBaiduMap.getDistance(distance));
                } catch (Exception e) {
                    return;
                }
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
            case R.id.my_trip_group_1_btn:   //出发
                mMyTripGroup1Btn.setClickable(false);
                getStartNow(mCurrentSetOutState);
                break;
            case R.id.my_trip_group_2_btn:   //到达
                mMyTripGroup2Btn.setClickable(false);
                getStartNow(mCurrentSetOutState);
                break;
            case R.id.my_trip_group_3_btn:   //见面
                mMyTripGroup3Btn.setClickable(false);
                InviteDetailBean inviteDetailBean = new InviteDetailBean(mInviteId, joinerId, targetId, publisherId);
                ZXingGetActivity.startZXingAct(INSTENCE, inviteDetailBean);
                break;
            case R.id.my_trip_group_4_btn:   //完成
                mMyTripGroup4Btn.setClickable(false);
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
//        if (mCurrentSetOutState == TIME_REFRESH_START_GO || mCurrentSetOutState == TIME_REFRESH_START_IN) {
        boolean shouldShowOther = false;
        if (mStateSize > 0 && mStateSize <= 2) {
            shouldShowOther = true;
        }
        SharedLocationV5Activity.startSharedLocationAct(
                INSTENCE, mInviteLatitude, mInviteLongitude,
                otherLatitude, otherLongitude, otherLogo, myLogo, mInviteId, shouldShowOther);
//        }
    }

    private void getChatInfo() {
        if (!HuanXinUtils.isLoginToHX()) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("inviteId", mInviteId);
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.get(ConstantsWhatNSM.URL_INVITE_CHAT, params, InviteDetailGetChatResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_INVITE_CHAT, this).setTag(this);
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
                setResult(RESULT_OK);
                finish();
            } else {
                showToastInfo(inviteSetoutResponse.getMessage());
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_CHAT
                && response instanceof InviteDetailGetChatResponse) {
            InviteDetailGetChatResponse mChatResponse = (InviteDetailGetChatResponse) response;
            int code = mChatResponse.getCode();
            if (1 == code) {
                String hxUserName = mChatResponse.getData().getHxUserName();
                if (!TextUtils.isEmpty(hxUserName) && !hxUserName.equals(App.USERSP.getString(MY_HX_USERNAME, ""))) {
                    slideInAnim = YoYo
                            .with(Techniques.SlideInRight)
                            .duration(500);
                    talkedInfo = mChatResponse.getData();
                    mMyTripTalkLayout.setVisibility(View.VISIBLE);
                    slideInAnim.playOn(mMyTripTalkLayout);
                    if (NSMTypeUtils.shouldShowTalkAnim()) {
                        talkAnim =
                                with(new InviteTalkAnimator())
                                        .duration(600);
                        mHandler.sendEmptyMessageDelayed(SHOULD_SHOW_TALK_ANIM, 1000);
                    }
                    isTalkShowing = true;
                }
            }
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

        if (mInviteFlag == 1) { //自己的单
            targetId = publisherId;
            otherLogo = data.getJoinerUserLogo();
            myLogo = data.getInviteUserLogo();
        } else {    //别人发的单
            targetId = joinerId;
            otherLogo = data.getInviteUserLogo();
            myLogo = data.getJoinerUserLogo();
        }
        chatTime = data.getChatTime();

        disPathShowHideActInfo(data.getJourneyLists());   //控制界面group的显隐和可用状态
        disPathHeaderInfo(data);    //分发头部信息
        disPathDataActInfo(data.getJourneyLists());   //控制界面group的数据

        regestTimeTickBroadCast();      //注册时间broadCast

        upTimeProgressBar();    //更新时间的进度条
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
        setAddressLocation(); //初始化地址的标记
    }

    private void setAddressLocation() {
        if (mLocationMarker == null) {
            mLocationLoc = LocationToBaiduMap.toBaiduMapLocation(LocationToBaiduMap.MapType.NORMAL,
                    mInviteLatitude, mInviteLongitude);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(mLocationLoc).zoom(16.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            MarkerOptions ooA = new MarkerOptions().position(mLocationLoc).icon(mLocationHeader)
                    .zIndex(7);
            mLocationMarker = (Marker) (mBaiduMap.addOverlay(ooA));
        }
    }

    private void disPathShowHideActInfo(List<MyTripResponse.DataBean.JourneyListsBean> journeyLists) {
        if (journeyLists == null) {
            mStateSize = 0;
        } else {
            mStateSize = journeyLists.size();
        }
        if (mStateSize > 0 && mStateSize <= 2) {
            mMyTripLocationDistence.setVisibility(View.VISIBLE);
        } else {
            mMyTripLocationDistence.setVisibility(View.INVISIBLE);
        }
        resetUI();
        switch (mStateSize) {
            case 0:     //代表没出发
                mTimeRefreshState = TIME_REFRESH_START_GO;
                mCurrentSetOutState = TIME_REFRESH_START_GO;
                mMyTripGroup1Btn.setClickable(true);
                mMyTripGroup1Btn.setEnabled(true);
                break;
            case 1:     //已经出发 -- > 确认到达
                mTimeRefreshState = TIME_REFRESH_START_IN;
                mCurrentSetOutState = TIME_REFRESH_START_IN;
                mMyTripGroup2Btn.setEnabled(true);
                mMyTripGroup2Btn.setClickable(true);
                break;
            case 2:     //确认到达 -- > 确认见面
                mTimeRefreshState = TIME_REFRESH_START_FACE;
                mCurrentSetOutState = TIME_REFRESH_START_FACE;
                mMyTripGroup3Btn.setEnabled(true);
                mMyTripGroup3Btn.setClickable(true);
                break;
            case 3:     //确认见面 -- > 确认完成
                mTimeRefreshState = TIME_REFRESH_START_FINISH;
                mCurrentSetOutState = TIME_REFRESH_START_FINISH;
                mMyTripGroup4Btn.setEnabled(true);
                mMyTripGroup4Btn.setClickable(true);
                break;
            case 4:     //已经 确认完成
            case 5:
            default:
                mMyTripLocationLayout.setVisibility(View.INVISIBLE);
                mTimeRefreshState = TIME_REFRESH_START_NORMAL;
                mCurrentSetOutState = TIME_REFRESH_START_NORMAL;
                break;
        }
    }

    private void resetUI() {
        mMyTripLocationLayout.setVisibility(View.VISIBLE);
        mMyTripGroup5.setVisibility(View.GONE);
        mMyTripSubmit.setVisibility(View.GONE);
        mMyTripGroup1Btn.setEnabled(false);
        mMyTripGroup2Btn.setEnabled(false);
        mMyTripGroup3Btn.setEnabled(false);
        mMyTripGroup4Btn.setEnabled(false);
    }

    private void disPathDataActInfo(List<MyTripResponse.DataBean.JourneyListsBean> journeyLists) {
        if (null == journeyLists) {
            return;
        }
        if (journeyLists.size() < 4 && journeyLists.size() >= 0) {   //已经立即出发并且没有完成
            if (chatTime != 0 && chatTime < System.currentTimeMillis()) {
                if (!isTalkShowing) {
                    getChatInfo();
                }
            } else {
                isTalkShowing = false;
                mMyTripTalkLayout.setVisibility(View.INVISIBLE);
            }
        } else {
            isTalkShowing = false;
            mMyTripTalkLayout.setVisibility(View.INVISIBLE);
        }
        upTimeTick();   //更新时间
        if (journeyLists.size() < 4) {
            //这里刷新状态的显示状态
            refreshStatus(false);
        }
        if (journeyLists.size() != 0) {
            for (MyTripResponse.DataBean.JourneyListsBean journeyListsBean : journeyLists) {
                int setout_status = journeyListsBean.getSetout_status();
                long createtime = journeyListsBean.getCreatetime();
                switch (setout_status) {     //判断setout是那个, 然后分别刷新需要更新的时间
                    case TIME_REFRESH_START_GO: //setOut=100, 有一个数据;已经出发
                        setTripTime(mMyTripGroup1Time, createtime);
                        break;
                    case TIME_REFRESH_START_IN: //setOut=200,两个数据; 已到达
                        setTripTime(mMyTripGroup2Time, createtime);
                        break;
                    case TIME_REFRESH_START_FACE:   //setOut=300,三个数据;已见面
                        setTripTime(mMyTripGroup3Time, createtime);
                        break;
                    case TIME_REFRESH_START_FINISH: //setOut=400,四个数据;已完成
                        setTripTime(mMyTripGroup4Time, createtime);
                        mMyTripGroup5.setVisibility(View.VISIBLE);
                        if (journeyListsBean.getGradelevel() <= 0) {    //没有评分
                            mMyTripSubmit.setVisibility(View.VISIBLE);
                            mMyTripSubmit.setClickable(true);
                            mMyTripSubmit.setEnabled(mRatingCount != 0);
                        } else {
                            mTripGroup5Rating.setStar(journeyListsBean.getGradelevel());
                            mTripGroup5Rating.setStarCount(5 - journeyListsBean.getGradelevel());
                            mTripGroup5Rating.setmClickable(false);
                            mTripGroup5Des.setVisibility(View.GONE);
                            mMyTripSubmit.setVisibility(View.GONE);
                        }
                        refreshStatus(true);
                        break;
                }
            }
        }
    }

    //如果 为true ,表示已经评分, 所有des显示, btn隐藏, 如果为false ,表示没有评分,所有des隐藏, btn显示
    private void refreshStatus(boolean levelThanZero) {
        mMyTripGroup1Des.setVisibility(levelThanZero ? View.VISIBLE : View.INVISIBLE);
        mMyTripGroup2Des.setVisibility(levelThanZero ? View.VISIBLE : View.INVISIBLE);
        mMyTripGroup3Des.setVisibility(levelThanZero ? View.VISIBLE : View.INVISIBLE);
        mMyTripGroup4Des.setVisibility(levelThanZero ? View.VISIBLE : View.INVISIBLE);
        mMyTripGroup1Btn.setVisibility(levelThanZero ? View.INVISIBLE : View.VISIBLE);
        mMyTripGroup2Btn.setVisibility(levelThanZero ? View.INVISIBLE : View.VISIBLE);
        mMyTripGroup3Btn.setVisibility(levelThanZero ? View.INVISIBLE : View.VISIBLE);
        mMyTripGroup4Btn.setVisibility(levelThanZero ? View.INVISIBLE : View.VISIBLE);
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
                setTripTime(mMyTripGroup1Time, System.currentTimeMillis());
            case TIME_REFRESH_START_IN:
                setTripTime(mMyTripGroup2Time, System.currentTimeMillis());
            case TIME_REFRESH_START_FACE:
                setTripTime(mMyTripGroup3Time, System.currentTimeMillis());
            case TIME_REFRESH_START_FINISH:
                setTripTime(mMyTripGroup4Time, System.currentTimeMillis());
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

    private void initBaiduMap() {
        mBaiduMap = mMyTripMap.getMap();
        mBaiduMap.getUiSettings().setAllGesturesEnabled(false);
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
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

    @Override
    protected void onPause() {
        mMyTripMap.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMyTripMap.onResume();
        super.onResume();
    }

    private void recyclerBitmap() {
        if (mLocationHeader != null)
            mLocationHeader.recycle();
        if (mLocationMarker != null) {
            mLocationMarker.remove();
        }
    }

    @Override
    protected void onDestroy() {
        mMyTripMap.onDestroy();
        mMyTripMap = null;
        mBaiduMap = null;
        if (mTimeTickBroadCast != null) {
            unregisterReceiver(mTimeTickBroadCast);
        }
        EventBus.getDefault().unregister(this);
        isSocketSuccess = true;
        mHandler.removeMessages(SHOULD_REQUEST_OTHER_LOCATION);
        mHandler.removeMessages(SHOULD_SHOW_TALK_ANIM);
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
