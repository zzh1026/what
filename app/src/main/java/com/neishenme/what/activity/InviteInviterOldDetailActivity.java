package com.neishenme.what.activity;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.Gson;
import com.neishenme.what.R;
import com.neishenme.what.adapter.InviteInviterOldAdapter;
import com.neishenme.what.adapter.ServicePhotoAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.baidumap.NSMMapHelper;
import com.neishenme.what.baidumap.service.LocationService;
import com.neishenme.what.baidumap.ui.RestaurantHowToGoActivity;
import com.neishenme.what.baidumap.utils.LocationToBaiduMap;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.InviteDetailResponse;
import com.neishenme.what.bean.InviteSetoutResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.bean.SocketResultBean;
import com.neishenme.what.bean.SocketSendBean;
import com.neishenme.what.eventbusobj.InviteDetailBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.Gps;
import com.neishenme.what.utils.LocationUtils;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.PositionUtil;
import com.neishenme.what.utils.TimeUtils;
import com.neishenme.what.view.CustomScrollView;
import com.neishenme.what.view.IndicatorLayout;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作者：zhaozh create on 2016/5/16 11:30
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个老的发布者详情的类,如果需要用需要把旧的删掉,然后将该文件改名将Old去掉
 * .
 * 其作用是 :
 */
@Deprecated
public class InviteInviterOldDetailActivity extends BaseActivity implements HttpLoader.ResponseListener, CustomScrollView.ScrollYChangedListener {
    private String inviteId;
    private String joinerId;
    private String targetId;
    private String publisherId;

    Gson gson = null;

    private InviteInviterOldAdapter inviteInviterAdapter;
    private CustomScrollView customSV;

    private ImageView mInviteBackIv;
    private ViewPager mVpCarousel;
    private IndicatorLayout mIndicateMain;
    private TextView mInviteTitleTv;
    private TextView mInvitePriceTv;
    private TextView mInviteInfoTv;
    private TextView mInviteTimeTv;
    private TextView mInviteTimeDestenceTv;
    private TextView mInvitePlaceTv;
    private TextView mInviteAddressTv;
    private ImageView mInviteMapIv;
    private TextView mInviteJoinerNumberTv;
    private GridView mInviteJoinerGv;

    private RelativeLayout mInviteUserFaceRl;
    private Button mInviteUserGoBtn;
    private ImageView mInviteUserRefreshBtn;
    private TextView mInviteOtherDestenceTv;
    private View mTitleBar;
    private TextView mInviteDetailTitleTv;

    //邀请者邀请详情的请求结果
    private InviteDetailResponse inviteDetailResponse;
    private List<InviteDetailResponse.DataEntity.JoinersEntity> joiners;

    //侧边的按钮状态
    private static final int START_NOW = 100;
    private static final int START_ADDRESS = 200;
    private static final int START_SURE_FACE = 300;
    private static final int START_CHOOSE_AGAIN = 3;
    private int startBtnState = 0;

    private String trideTitle;
    private int tridePayType;
    private String storeName;

    private String servicesLogo;
    private int serviceId;
    private long time;

    private double latitude;
    private double longgitude;

    private double storeLatitude;
    private double storeLongitude;

    private String trideRestaurantName;
    private String trideRestaurantLogo;
    private long trideCreateTime;
    private int trideId;

    private boolean send = false;
    private LocationService locationService;

    @Override
    protected int initContentView() {
        return R.layout.activity_invite_inviter_detail;
    }

    @Override
    protected void initView() {
        customSV = (CustomScrollView) findViewById(R.id.custom_sv);

        mInviteBackIv = (ImageView) findViewById(R.id.invite_back_iv);
        mVpCarousel = (ViewPager) findViewById(R.id.vp_carousel);
        mIndicateMain = (IndicatorLayout) findViewById(R.id.indicate_main);

        mInviteTitleTv = (TextView) findViewById(R.id.invite_title_tv);
        mInvitePriceTv = (TextView) findViewById(R.id.invite_price_tv);
        mInviteInfoTv = (TextView) findViewById(R.id.invite_info_tv);
        mInviteTimeTv = (TextView) findViewById(R.id.invite_time_tv);
        mInviteTimeDestenceTv = (TextView) findViewById(R.id.invite_time_destence_tv);
        mInvitePlaceTv = (TextView) findViewById(R.id.invite_place_tv);
        mInviteAddressTv = (TextView) findViewById(R.id.invite_address_tv);

        mInviteMapIv = (ImageView) findViewById(R.id.invite_map_iv);

        mInviteJoinerNumberTv = (TextView) findViewById(R.id.invite_joiner_number_tv);
        mInviteJoinerGv = (GridView) findViewById(R.id.invite_joiner_gv);

        mInviteUserFaceRl = (RelativeLayout) findViewById(R.id.invite_user_face_rl);
        mInviteUserGoBtn = (Button) findViewById(R.id.invite_user_go_btn);
        mInviteUserRefreshBtn = (ImageView) findViewById(R.id.invite_user_refresh_btn);
        mInviteOtherDestenceTv = (TextView) findViewById(R.id.invite_other_destence_tv);

        mTitleBar = (View) findViewById(R.id.title_bar);
        mInviteDetailTitleTv = (TextView) findViewById(R.id.invite_detail_title_tv);

        locationService = NSMMapHelper.getInstance().locationService;
    }

    @Override
    protected void initListener() {
        locationService.registerListener(mListener);

        customSV.setScrollYChangedListener(this);
        mInviteBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mInviteJoinerGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int userId = joiners.get(position).getUserId();
                int newstatus = inviteDetailResponse.getData().getInvite().getNewstatus();
                boolean showBtn = false;
                //newstatus 我的状态, 会员支付或者我已经支付都是可以同意别人的,其他的情况不能同意别人
                if (50 == newstatus || 100 == newstatus) {
                    showBtn = true;
                    //去掉所有时间eventbus的
                    EventBus.getDefault().removeAllStickyEvents();
                    InviteDetailResponse.DataEntity.TradeEntity trade = inviteDetailResponse.getData().getTrade();
//                    TrideBean trideBean = new TrideBean(trideRestaurantName, trideRestaurantLogo,
//                            trideTitle, trideCreateTime, trade.getPrice(), tridePayType, trideId, trade.getTradeNum());
//                    EventBus.getDefault().postSticky(trideBean);
                }
                int inviteId = inviteDetailResponse.getData().getInvite().getId();
                UserDetailActivity.startUserDetailAct(InviteInviterOldDetailActivity.this, userId, showBtn, inviteId, newstatus);
            }
        });

        mInviteUserGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationUtils.getLocations(InviteInviterOldDetailActivity.this, new LocationUtils.GetLocations() {
                    @Override
                    public void getLocations(double latitude, double longgitude) {
                        InviteInviterOldDetailActivity.this.latitude = latitude;
                        InviteInviterOldDetailActivity.this.longgitude = longgitude;
                    }
                });
                switch (startBtnState) {
                    case START_NOW:
                        //现在出发
                    case START_ADDRESS:
                        //确认到达
                        getStartNow(startBtnState);
                        break;
                    case START_SURE_FACE:
                        //确认见面
                        InviteDetailBean inviteDetailBean = new InviteDetailBean(inviteId, joinerId, targetId, publisherId);
                        ZXingGetActivity.startZXingAct(InviteInviterOldDetailActivity.this, inviteDetailBean);
                        break;
                    case START_CHOOSE_AGAIN:
                        //重新选择
                        HashMap<String, String> params = new HashMap<>();
                        params.put("inviteId", inviteId);
                        params.put("token", NSMTypeUtils.getMyToken());
                        HttpLoader.get(ConstantsWhatNSM.URL_INVITE_RESTACCEPTUSER, params, SendSuccessResponse.class,
                                ConstantsWhatNSM.REQUEST_CODE_INVITE_RESTACCEPTUSER, InviteInviterOldDetailActivity.this).setTag(this);
                        break;
                }
            }
        });

        mInviteUserRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send = true;
                locationService.setLocationOption(LocationService.CoorType.CoorType_GCJ02, LocationService.TimeType.TIME_0);
                locationService.start();
//                InviteInviterDetailActivity.this.latitude = latitude;
//                InviteInviterDetailActivity.this.longgitude = longgitude;
//                SocketSendBean socketSendBean =
//                        NSMTypeUtils.getSocketSendBean(NSMTypeUtils.RequestType.TARGET, latitude, longgitude, Integer.parseInt(inviteId));
//                String sendBean = gson.toJson(socketSendBean);
//                ALog.i(sendBean);
//                try {
//                    App.getClientSocket().addSendMsgToQueue(sendBean);
//                } catch (Exception e) {
//                    ALog.i("失败了");
//                    e.printStackTrace();
//                }

//                LocationUtils.getLocations(InviteInviterDetailActivity.this, new LocationUtils.GetLocations() {
//                    @Override
//                    public void getLocations(double latitude, double longgitude) {
//                        ALog.i("latitude = " + latitude + " longgitude = " + longgitude);
//                        InviteInviterDetailActivity.this.latitude = latitude;
//                        InviteInviterDetailActivity.this.longgitude = longgitude;
//                        SocketSendBean socketSendBean =
//                                NSMTypeUtils.getSocketSendBean(NSMTypeUtils.RequestType.TARGET, latitude, longgitude, Integer.parseInt(inviteId));
//                        String sendBean = gson.toJson(socketSendBean);
//                        ALog.i(sendBean);
//                        try {
//                            App.getClientSocket().addSendMsgToQueue(sendBean);
//                        } catch (Exception e) {
//                            ALog.i("失败了");
//                            e.printStackTrace();
//                        }
//                    }
//                });
            }
        });

        mInviteMapIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                RestaurantHowToGoActivity.startRestHowToGoAct(
//                        InviteInviterOldDetailActivity.this, storeLatitude, storeLongitude);
            }
        });

    }

    @Override
    protected void initData() {
        loadingShow();

        gson = new Gson();
        EventBus.getDefault().register(this);

        inviteId = getIntent().getStringExtra("data");
        publisherId = NSMTypeUtils.getMyUserId();
        targetId = NSMTypeUtils.getMyUserId();

        mTitleBar.setAlpha(0);
        //addPreview();
    }

    private void addPreview() {
        HashMap<String, String> params = new HashMap<>();
        params.put("inviteId", inviteId);
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.get(ConstantsWhatNSM.URL_ADD_PREVIEW, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ADD_PREVIEW, this).setTag(this);
    }

//    public void onEventMainThread(SocketResultBean.DataEntity dataEntity) {
//        if (null != dataEntity && mInviteOtherDestenceTv.getVisibility() == View.VISIBLE) {
//            int inviteId = dataEntity.getInviteId();
//            double latitude = dataEntity.getLatitude();
//            double longitude = dataEntity.getLongitude();
//            if (inviteId != (Integer.parseInt(this.inviteId))) {
//                return;
//            }
//            final double distance = LocationToBaiduMap.getDistance(latitude, longitude, storeLatitude, storeLongitude);
//            mInviteOtherDestenceTv.setText("对方距离活动地点" + LocationToBaiduMap.getDistance(distance));
//        }
//    }

    private void getStartNow(int startBtnState) {
        mInviteUserGoBtn.setClickable(false);
        mInviteUserGoBtn.setText("正在刷新");
        HashMap<String, String> params = new HashMap<>();
        params.put("inviteId", inviteId);
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("longitude", String.valueOf(longgitude));
        params.put("latitude", String.valueOf(latitude));
        params.put("setout", String.valueOf(startBtnState));
        HttpLoader.post(ConstantsWhatNSM.URL_INVITE_SETOUT, params, InviteSetoutResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_INVITE_SETOUT, this).setTag(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIndicateMain.removeAllViewsInLayout();

//        if (!PackageVersion.isServiceRunning(UIUtils.getContext())) {
//            UIUtils.getContext().startService(new Intent(UIUtils.getContext(), SocketGetLocationService.class));
//        }

        getInviteData();
    }

    //获取邀请详情的信息
    private void getInviteData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("inviteId", inviteId);
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.get(ConstantsWhatNSM.URL_INVITE_INVETER, params, InviteDetailResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_INVITE_INVITER, this).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_INVITER
                && response instanceof InviteDetailResponse) {
            inviteDetailResponse = (InviteDetailResponse) response;
            if (1 != inviteDetailResponse.getCode()) {
                showToastInfo(inviteDetailResponse.getMessage());
                return;
            }

            int userId = inviteDetailResponse.getData().getInvite().getUserId();
            if (!NSMTypeUtils.getMyUserId().equals(String.valueOf(userId))) {
                showToastError("登录出现问题,请重新登录");
                ActivityUtils.startActivityAndFinish(this, LoginActivity.class);
                return;
            }

            if (1 == inviteDetailResponse.getData().getShowMap()) {
                mInviteMapIv.setVisibility(View.VISIBLE);
            } else {
                mInviteMapIv.setVisibility(View.INVISIBLE);
            }

            //分发界面数据
            disPathInfoData(inviteDetailResponse.getData().getInvite(), inviteDetailResponse.getData().getStore(), inviteDetailResponse.getData().getServices());

            //分发服务图片数据
            List<InviteDetailResponse.DataEntity.ServicephotosEntity> servicephotos = inviteDetailResponse.getData().getServicephotos();
            if (null != servicephotos && 0 != servicephotos.size()) {
                //服务图片
                disPathServicePhotoData(servicephotos);
            }

            //分发加入者数量数据
            joiners = inviteDetailResponse.getData().getJoiners();
            if (null != joiners && 0 != joiners.size()) {
                mInviteJoinerNumberTv.setText("(" + joiners.size() + "人已加入)");
                int joinerId = getJoinerId(joiners);
                if (0 != joinerId) {
                    this.joinerId = String.valueOf(joinerId);
                }
                disPathJoinersData(joiners);
            } else {
                mInviteJoinerNumberTv.setText("(0人已加入)");
            }

            //刷新按钮状态
            refreshMyState(inviteDetailResponse.getData().getInvite());

            loadingDismiss();
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_SETOUT
                && response instanceof InviteSetoutResponse) {
            InviteSetoutResponse inviteSetoutResponse = (InviteSetoutResponse) response;
            int code = inviteSetoutResponse.getCode();
            if (1 == code) {
                //int setout = inviteSetoutResponse.getData().getInviteInfo().getSetout();
                switch (startBtnState) {
                    case START_NOW:
                        startBtnState = START_ADDRESS;
                        break;
                    case START_ADDRESS:
                        startBtnState = START_SURE_FACE;
                        break;
                    default:
                        break;
                }
            } else {
                showToastInfo(inviteSetoutResponse.getMessage());
            }
            refreshmInviteUserGoBtnState(inviteSetoutResponse.getData());
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_RESTACCEPTUSER
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            int code = sendSuccessResponse.getCode();
            if (1 == code) {
                mIndicateMain.removeAllViewsInLayout();
                getInviteData();
            } else {
                showToastInfo(sendSuccessResponse.getMessage());
            }
        }
    }

    private int getJoinerId(List<InviteDetailResponse.DataEntity.JoinersEntity> joiners) {
        for (InviteDetailResponse.DataEntity.JoinersEntity joiner : joiners) {
            if (joiner.getNewstatus() == 150) {
                return joiner.getUserId();
            }
        }
        return 0;
    }

    private void refreshmInviteUserGoBtnState(InviteSetoutResponse.DataEntity data) {
        switch (startBtnState) {
            case START_NOW:
                mInviteUserGoBtn.setClickable(true);
                mInviteUserGoBtn.setText("立即出发");
                break;
            case START_ADDRESS:
                mInviteUserGoBtn.setClickable(true);
                mInviteUserGoBtn.setText("确认到达");
                break;
            case START_SURE_FACE:
                mInviteUserGoBtn.setClickable(true);
                mInviteUserGoBtn.setText("确认见面");
                break;
            case START_CHOOSE_AGAIN:
                mInviteUserGoBtn.setClickable(true);
                mInviteUserGoBtn.setText("重新选择");
                mInviteOtherDestenceTv.setVisibility(View.INVISIBLE);
                mInviteOtherDestenceTv.setVisibility(View.INVISIBLE);
                break;
        }
        if (null != data) {
            mInviteUserGoBtn.setClickable(false);
            if (data.getInviteInfo().getDistance() <= 5000) {
                mInviteUserGoBtn.setClickable(true);
            }
            mInviteUserRefreshBtn.setVisibility(View.VISIBLE);
            int otherSetout = data.getInviteInfo().getOtherSetout();
            if (100 == otherSetout && data.getInviteInfo().getOtherDistance() > 0) {
                mInviteOtherDestenceTv.setVisibility(View.VISIBLE);
//                mInviteOtherDestenceTv.setText("对方距离活动地点" +
//                        NSMTypeUtils.div(data.getInviteInfo().getOtherDistance(), 1000, 1) + "km");
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_INVITER) {
            loadingDismiss();
            showToastError("网络连接失败,请检查网络连接");
            finish();
        }
    }

    private void disPathInfoData(InviteDetailResponse.DataEntity.InviteEntity invite,
                                 InviteDetailResponse.DataEntity.StoreEntity store,
                                 InviteDetailResponse.DataEntity.ServicesEntity services) {
        trideTitle = invite.getTitle().trim();
        if (!NSMTypeUtils.isEmpty(trideTitle)) {
            mInviteTitleTv.setText(trideTitle);
        }
        String price = String.valueOf(services.getPrice()).trim();
        if (!NSMTypeUtils.isEmpty(price)) {
            mInvitePriceTv.setText("￥ " + price);
        }
        String target = NSMTypeUtils.getUserTarget(invite.getTarget());
        tridePayType = invite.getPayType();
        String paytype = NSMTypeUtils.getUserPayType(tridePayType);
        mInviteInfoTv.setText(paytype + target);
        time = invite.getTime();
        if (TimeUtils.isToday(time)) {
            mInviteTimeTv.setText("今天 " + TimeUtils.getTime(time));
//            mInviteTimeDestenceTv.setText("距离活动还有" + TimeUtils.overTime(time));
        } else {
            mInviteTimeTv.setText(TimeUtils.getTime(time, TimeUtils.DATE_FORMAT_NSM));
            mInviteTimeDestenceTv.setVisibility(View.INVISIBLE);
        }
        storeName = store.getName();
        if (!NSMTypeUtils.isEmpty(storeName)) {
            mInvitePlaceTv.setText(storeName);
        }
        String address = store.getAddrDetail();
        if (!NSMTypeUtils.isEmpty(address)) {
            mInviteAddressTv.setText(address);
        }

        trideRestaurantName = services.getName();
        trideRestaurantLogo = services.getLogo();
        trideId = services.getId();
        trideCreateTime = invite.getCreateTime();

        storeLongitude = store.getLatitude();
        storeLatitude = store.getLongitude();
    }

    private void disPathServicePhotoData(List<InviteDetailResponse.DataEntity.ServicephotosEntity> servicephotos) {
        mVpCarousel.setAdapter(new ServicePhotoAdapter(InviteJoinerOldDetailActivity.getServicePhotos(servicephotos)));
        mIndicateMain.setViewPage(mVpCarousel);
    }

    private void disPathJoinersData(List<InviteDetailResponse.DataEntity.JoinersEntity> joiners) {
        if (inviteInviterAdapter == null) {
            inviteInviterAdapter = new InviteInviterOldAdapter(joiners, this);
            mInviteJoinerGv.setAdapter(inviteInviterAdapter);
        } else {
            inviteInviterAdapter.setJoiners(joiners, this);
            inviteInviterAdapter.notifyDataSetChanged();
        }
    }

    private void refreshMyState(InviteDetailResponse.DataEntity.InviteEntity invite) {
        switch (invite.getNewstatus()) {
            //未支付
            case 0:
                mInviteUserFaceRl.setVisibility(View.GONE);
                break;
            //会员支付
            case 50:
                mInviteUserFaceRl.setVisibility(View.GONE);
                break;
            //已支付
            case 100:
                mInviteUserFaceRl.setVisibility(View.GONE);
                break;
            //等待对方付款
            case 120:
                mInviteUserFaceRl.setVisibility(View.VISIBLE);
                mInviteUserGoBtn.setVisibility(View.VISIBLE);
                mInviteUserGoBtn.setClickable(true);
                mInviteUserGoBtn.setText("重新选择");
                startBtnState = START_CHOOSE_AGAIN;
                mInviteUserRefreshBtn.setVisibility(View.GONE);
                mInviteOtherDestenceTv.setText("等待对方付款");
                break;
            //成单
            case 150:
                mInviteUserFaceRl.setVisibility(View.VISIBLE);
                mInviteOtherDestenceTv.setText("");
                int setout = invite.getSetout();
                if (0 == setout) {
                    mInviteUserGoBtn.setVisibility(View.VISIBLE);
                    mInviteUserGoBtn.setText("立即出发");
                    startBtnState = START_NOW;
                    mInviteUserRefreshBtn.setVisibility(View.VISIBLE);
                    mInviteOtherDestenceTv.setVisibility(View.VISIBLE);
                } else if (100 == setout) {
                    mInviteUserGoBtn.setVisibility(View.VISIBLE);
                    mInviteUserGoBtn.setText("确认到达");
                    startBtnState = START_ADDRESS;
                    mInviteUserRefreshBtn.setVisibility(View.VISIBLE);
                    mInviteOtherDestenceTv.setVisibility(View.VISIBLE);
                } else if (200 == setout) {
                    int signing = invite.getSigning();
                    if (0 == signing) {
                        mInviteUserGoBtn.setVisibility(View.VISIBLE);
                        mInviteUserGoBtn.setText("确认见面");
                        startBtnState = START_SURE_FACE;
                        mInviteUserRefreshBtn.setVisibility(View.VISIBLE);
                        mInviteOtherDestenceTv.setVisibility(View.VISIBLE);
                    } else if (1 == signing) {
                        mInviteUserGoBtn.setVisibility(View.VISIBLE);
                        mInviteUserGoBtn.setText("已见面");
                        mInviteUserGoBtn.setClickable(false);
                        mInviteUserRefreshBtn.setVisibility(View.INVISIBLE);
                        mInviteOtherDestenceTv.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            //已见面
            case 180:
                mInviteUserFaceRl.setVisibility(View.VISIBLE);
                mInviteUserGoBtn.setVisibility(View.VISIBLE);
                mInviteUserGoBtn.setClickable(false);
                mInviteUserGoBtn.setText(R.string.signed_yet);
                mInviteUserRefreshBtn.setVisibility(View.INVISIBLE);
                mInviteOtherDestenceTv.setVisibility(View.INVISIBLE);
                break;
            //已完成
            case 200:
                mInviteUserFaceRl.setVisibility(View.VISIBLE);
                mInviteUserGoBtn.setVisibility(View.VISIBLE);
                mInviteUserGoBtn.setClickable(false);
                mInviteUserGoBtn.setText("已完成");
                mInviteUserRefreshBtn.setVisibility(View.INVISIBLE);
                mInviteOtherDestenceTv.setVisibility(View.INVISIBLE);
                break;
            //穿透
            //主动取消
            case 700:
                //超时自动退款
            case 750:
                //被禁止
            case 800:
                //删除
            case 900:
            default:
                mInviteUserFaceRl.setVisibility(View.GONE);
                break;
        }
        return;
    }

    @Override
    public void scrollYChange(int y) {
        int alpha;
        //获取滚动距离设置titlebar透明度
        if (y < 0) {
            y = 0;
        }
        alpha = (int) (y * (0.005));
        mTitleBar.setAlpha(alpha >= 1 ? 1 : alpha);
        mInviteDetailTitleTv.setVisibility(alpha >= 1 ? View.VISIBLE : View.INVISIBLE);
    }

    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                if (send) {
                    send = false;
                    /**
                     * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                     * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                     */
//                    Gps gps = PositionUtil.gcj_To_Gps84(location.getLatitude(), location.getLongitude());
//                    InviteInviterOldDetailActivity.this.latitude = gps.getWgLat();
//                    InviteInviterOldDetailActivity.this.longgitude = gps.getWgLon();
//                    ALog.i("latitude = " + location.getLatitude() + " longitude = " + location.getLongitude());
//                    SocketSendBean socketSendBean =
//                            NSMTypeUtils.getSocketSendBean(NSMTypeUtils.RequestType.TARGET,
//                                    latitude, longgitude, Integer.parseInt(inviteId));
//                    String sendBean = gson.toJson(socketSendBean);
//                    ALog.i(sendBean);
                    try {
//                        App.getClientSocket().addSendMsgToQueue(sendBean);
                    } catch (Exception e) {
                        ALog.i("失败了");
                        e.printStackTrace();
                    }
                    ALog.i("latitude = " + latitude + " longitude = " + longgitude);
                }
//                locationService.stop();
            } else {
                showToastError("获取位置失败");
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    @Override
    protected void onDestroy() {
        HttpLoader.cancelRequest(this);
        EventBus.getDefault().unregister(this);
        locationService.unregisterListener(mListener); //注销掉监听
//        locationService.stop(); //停止定位服务
        super.onDestroy();
    }
}
