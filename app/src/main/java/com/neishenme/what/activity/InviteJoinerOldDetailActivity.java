package com.neishenme.what.activity;

import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.Gson;
import com.neishenme.what.R;
import com.neishenme.what.adapter.InviteJoinerOldAdapter;
import com.neishenme.what.adapter.ServicePhotoAdapter;
import com.neishenme.what.adapter.UserDetailPhotosAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.baidumap.NSMMapHelper;
import com.neishenme.what.baidumap.service.LocationService;
import com.neishenme.what.baidumap.ui.RestaurantHowToGoActivity;
import com.neishenme.what.baidumap.utils.LocationToBaiduMap;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.InviteDetailResponse;
import com.neishenme.what.bean.InviteSetoutResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.RequestJoinResponse;
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
import com.neishenme.what.view.CircleImageView;
import com.neishenme.what.view.CustomScrollView;
import com.neishenme.what.view.GridViewAdjustHeight;
import com.neishenme.what.view.IndicatorLayout;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.MyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作者：zhaozh create on 2016/5/16 11:34
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个老的加入者详情的类,如果需要用需要把旧的删掉,然后将该文件改名将Old去掉
 * 
 * 其作用是 :
 */
@Deprecated
public class InviteJoinerOldDetailActivity extends BaseActivity implements HttpLoader.ResponseListener, CustomScrollView.ScrollYChangedListener {

    private String inviteId;
    private String joinerId;
    private String targetId;
    private String publisherId;

    private InviteJoinerOldAdapter inviteJoinerAdapter;

    Gson gson = null;

    private CustomScrollView customSV;

    private ImageView mInviteBackIv;
    private ViewPager mVpCarousel;
    private IndicatorLayout mIndicateMain;
    private TextView mInviteTitleTv;
    private TextView mInvitePriceTv;
    private TextView mInviteInfoTv;
    private CircleImageView mInviteUserLogoIv;
    private TextView mInviteUserNameTv;
    private ImageView mInviteUserGenderIv;
    private TextView mInviteUserAgeTv;
    private RelativeLayout mInviteUserMoreinfoTv;
    private TextView mInviteUserSignTv;
    private HorizontalScrollView mInviteUserPhotoTv;
    private TextView mInviteTimeTv;
    private TextView mInviteTimeDestenceTv;
    private TextView mInvitePlaceTv;
    private TextView mInviteAddressTv;
    private ImageView mInviteMapIv;
    private TextView mInviteJoinerNumberTv;
    private GridViewAdjustHeight mInviteJoinerGv;
    private Button mInviteJoinBtn;
    private RelativeLayout mInviteUserFaceRl;
    private Button mInviteUserGoBtn;
    private ImageView mInviteUserRefreshBtn;
    private TextView mInviteOtherDestenceTv;
    private View mTitleBar;
    private TextView mInviteDetailTitleTv;

    //邀请者邀请详情的请求结果
    private InviteDetailResponse inviteDetailResponse;

    private LinearLayout mGallery;
    private LayoutInflater mInflater;
    private GridView mInviteUserPhotosGv;
    private UserDetailPhotosAdapter adapter;

    //按钮状态
    private static final int TO_PAY = 0;
    private static final int TO_ADD = 1;
    private int joinBtnState = 1;

    //侧边的按钮状态
    private static final int START_NOW = 100;
    private static final int START_ADDRESS = 200;
    private static final int START_SURE_FACE = 300;
    private int startBtnState = START_NOW;
    private int userId;

    private String title;
    private String storeName;
    private String servicesLogo;
    private long time;
    private int payType;
    private int serviceId;

    private List<InviteDetailResponse.DataEntity.UserphonesEntity> userphones;
    private double latitude;
    private double longgitude;

    private ArrayList<String> userPhotos;
    private ArrayList<String> userHeaderPhoto;
    private double storeLatitude;
    private double storeLongitude;

    private boolean send = false;
    private LocationService locationService;

    @Override
    protected int initContentView() {
        return R.layout.activity_invite_joiner_detail;
    }

    @Override
    protected void initView() {
        customSV = (CustomScrollView) findViewById(R.id.custom_sv);
        //返回
        mInviteBackIv = (ImageView) findViewById(R.id.invite_back_iv);
        mVpCarousel = (ViewPager) findViewById(R.id.vp_carousel);
        mIndicateMain = (IndicatorLayout) findViewById(R.id.indicate_main);
        //标题
        mInviteTitleTv = (TextView) findViewById(R.id.invite_title_tv);
        mInvitePriceTv = (TextView) findViewById(R.id.invite_price_tv);
        mInviteInfoTv = (TextView) findViewById(R.id.invite_info_tv);
        mInviteTimeTv = (TextView) findViewById(R.id.invite_time_tv);
        mInviteTimeDestenceTv = (TextView) findViewById(R.id.invite_time_destence_tv);
        mInvitePlaceTv = (TextView) findViewById(R.id.invite_place_tv);
        mInviteAddressTv = (TextView) findViewById(R.id.invite_address_tv);

        mInviteUserLogoIv = (CircleImageView) findViewById(R.id.invite_user_logo_iv);
        mInviteUserNameTv = (TextView) findViewById(R.id.invite_user_name_tv);
        mInviteUserGenderIv = (ImageView) findViewById(R.id.invite_user_gender_iv);
        mInviteUserAgeTv = (TextView) findViewById(R.id.invite_user_age_tv);
        mInviteUserMoreinfoTv = (RelativeLayout) findViewById(R.id.invite_user_moreinfo_tv);
        mInviteUserSignTv = (TextView) findViewById(R.id.invite_user_sign_tv);
        mInviteUserPhotoTv = (HorizontalScrollView) findViewById(R.id.invite_user_photo_tv);

        mInviteMapIv = (ImageView) findViewById(R.id.invite_map_iv);

        mInviteUserPhotosGv = (GridView) findViewById(R.id.gridview);
        mGallery = (LinearLayout) findViewById(R.id.id_gallery);
        mInflater = LayoutInflater.from(this);

        mInviteJoinerNumberTv = (TextView) findViewById(R.id.invite_joiner_number_tv);
        mInviteJoinerGv = (GridViewAdjustHeight) findViewById(R.id.invite_joiner_gv);

        mInviteJoinBtn = (Button) findViewById(R.id.invite_join_btn);
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

        mInviteUserPhotosGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (userPhotos != null) {
                    ActivityUtils.startActivityForListData(InviteJoinerOldDetailActivity.this,
                            PhotoViewActivity.class, userPhotos, position);
                }
            }
        });

        mInviteJoinerGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ALog.i("点击的条目数的position为" + position);
            }
        });

        mInviteUserLogoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivityForListData(InviteJoinerOldDetailActivity.this,
                        PhotoViewActivity.class, userHeaderPhoto, 0);
            }
        });

        mInviteUserGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationUtils.getLocations(InviteJoinerOldDetailActivity.this, new LocationUtils.GetLocations() {
                    @Override
                    public void getLocations(double latitude, double longgitude) {
                        InviteJoinerOldDetailActivity.this.latitude = latitude;
                        InviteJoinerOldDetailActivity.this.longgitude = longgitude;
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
                        ZXingGetActivity.startZXingAct(InviteJoinerOldDetailActivity.this, inviteDetailBean);
                        break;
                }
            }
        });

        mInviteJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (joinBtnState) {
                    case TO_PAY:
                        //去支付,会员支付被同意才会有这个按钮
                        toPayForVip(inviteDetailResponse.getData().getTrade());
                        break;
                    case TO_ADD:
                        //申请加入
                        requestJoin();
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
//                LocationUtils.getLocations(InviteJoinerDetailActivity.this, new LocationUtils.GetLocations() {
//                    @Override
//                    public void getLocations(double latitude, double longgitude) {
//                        InviteJoinerDetailActivity.this.latitude = latitude;
//                        InviteJoinerDetailActivity.this.longgitude = longgitude;
//                    }
//                });
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
            }
        });

        mInviteUserMoreinfoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到用户详情界面
                UserDetailActivity.startUserDetailAct(InviteJoinerOldDetailActivity.this, userId, false);
            }
        });

        mInviteMapIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                RestaurantHowToGoActivity.startRestHowToGoAct(
//                        InviteJoinerOldDetailActivity.this, storeLatitude, storeLongitude);
            }
        });
    }

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
    protected void initData() {
        loadingShow();

        gson = new Gson();
        EventBus.getDefault().register(this);

        inviteId = getIntent().getStringExtra("data");
        joinerId = NSMTypeUtils.getMyUserId();
        targetId = NSMTypeUtils.getMyUserId();

        mTitleBar.setAlpha(0);
        getInviteData();
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

    @Override
    protected void onStart() {
        super.onStart();
        if (mIndicateMain != null) {
            mIndicateMain.removeAllViewsInLayout();
        }

//        if (!PackageVersion.isServiceRunning(UIUtils.getContext())) {
//            UIUtils.getContext().startService(new Intent(UIUtils.getContext(), SocketGetLocationService.class));
//        } else {
//            ALog.i("已经运行了 ,,, ");
//        }

        getInviteData();
    }

    //获取邀请详情的信息
    private void getInviteData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("inviteId", inviteId);
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.get(ConstantsWhatNSM.URL_INVITE_JOINER, params, InviteDetailResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_INVITE_JOINER, this).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_JOINER
                && response instanceof InviteDetailResponse) {
            inviteDetailResponse = (InviteDetailResponse) response;
            if (1 != inviteDetailResponse.getCode()) {
                showToastInfo(inviteDetailResponse.getMessage());
                return;
            }

            userId = inviteDetailResponse.getData().getInvite().getUserId();
            if (NSMTypeUtils.getMyUserId().equals(String.valueOf(userId))) {
                showToastInfo("登录出现问题,请重新登录");
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

            //分发用户信息数据
            disPathUserData(inviteDetailResponse.getData().getUser());

            //分发用户照片数据
            userphones = inviteDetailResponse.getData().getUserphones();
            if (null != userphones && 0 != userphones.size()) {
                disPathUserPhotoData(userphones);
            } else {
                mInviteUserPhotosGv.setVisibility(View.INVISIBLE);
            }

            //分发服务图片数据
            List<InviteDetailResponse.DataEntity.ServicephotosEntity> servicephotos = inviteDetailResponse.getData().getServicephotos();
            if (null != servicephotos && 0 != servicephotos.size()) {
                //服务图片
                disPathServicePhotoData(servicephotos);
            }

            //分发加入者数量数据
            List<InviteDetailResponse.DataEntity.JoinersEntity> joiners = inviteDetailResponse.getData().getJoiners();
            if (null != joiners && 0 != joiners.size()) {
                mInviteJoinerNumberTv.setText("(" + joiners.size() + "人已加入)");
                disPathJoinersData(joiners);
            } else {
                mInviteJoinerNumberTv.setText("(0人已加入)");
            }

            //刷新按钮状态
            int newstatus = inviteDetailResponse.getData().getInvite().getNewstatus();
            long time = inviteDetailResponse.getData().getInvite().getTime();
            if (50 == newstatus || 100 == newstatus || 120 == newstatus || 150 == newstatus ||
                    180 == newstatus || 200 == newstatus && (time > System.currentTimeMillis())) {
                refreshMyState(joiners);
            } else {
                mInviteJoinBtn.setVisibility(View.GONE);
                mInviteUserFaceRl.setVisibility(View.INVISIBLE);
            }

            loadingDismiss();
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_JOIN
                && response instanceof RequestJoinResponse) {
            RequestJoinResponse requestJoinResponse = (RequestJoinResponse) response;
            int code = requestJoinResponse.getCode();
            if (1 == code) {
                mInviteJoinBtn.setVisibility(View.INVISIBLE);
                //0 说明是加入别人请客的单子,或者是会员加入了
                if (0 == requestJoinResponse.getData().getTrade().getPrice() || requestJoinResponse.getData().getJoiner().getNewstatus() == 50) {
                    mInviteJoinBtn.setVisibility(View.INVISIBLE);
                    showToastSuccess("加入成功");
                    if (mIndicateMain != null) {
                        mIndicateMain.removeAllViewsInLayout();
                    }
                    getInviteData();
                } else {
//                    RequestJoinResponse.DataEntity.TradeEntity trade = requestJoinResponse.getData().getTrade();
//                    TrideBean trideBean = new TrideBean(storeName, servicesLogo, title, time,
//                            trade.getPrice(), payType, serviceId, trade.getTradeNum());
//                    PayOrderActivity.startPayOrderActForResult(InviteJoinerOldDetailActivity.this, trideBean);
                }
            } else if (-1210 == code) {
                MyToast.showConterToast(this, requestJoinResponse.getMessage());
                mInviteJoinBtn.setClickable(true);
                mInviteJoinBtn.setText("申请加入");
            } else {
                MyToast.showConterToast(this, requestJoinResponse.getMessage());
                mInviteJoinBtn.setClickable(true);
                mInviteJoinBtn.setText("申请加入");
            }
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
        }

        mInviteUserGoBtn.setClickable(false);
        if (null != data) {

            if (data.getInviteInfo().getDistance() <= 5000) {
                mInviteUserGoBtn.setClickable(true);
            }
            mInviteUserRefreshBtn.setVisibility(View.VISIBLE);
            int otherSetout = data.getInviteInfo().getOtherSetout();
            if (100 == otherSetout && data.getInviteInfo().getOtherDistance() > 0) {
                mInviteOtherDestenceTv.setVisibility(View.VISIBLE);
                mInviteOtherDestenceTv.setText("对方距离目的地还有" +
                        NSMTypeUtils.div(data.getInviteInfo().getOtherDistance(), 1000, 1) + "km");
            }
        }

    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INVITE_JOINER) {
            loadingDismiss();
            showToastError("网络连接失败,请检查网络连接");
            finish();
        }
    }

    private void toPayForVip(InviteDetailResponse.DataEntity.TradeEntity trade) {
//        TrideBean trideBean = new TrideBean(storeName, servicesLogo, title, time,
//                trade.getPrice(), payType, serviceId, trade.getTradeNum());
//        PayOrderActivity.startPayOrderActForResult(InviteJoinerOldDetailActivity.this, trideBean);
    }

    private void disPathInfoData(InviteDetailResponse.DataEntity.InviteEntity invite,
                                 InviteDetailResponse.DataEntity.StoreEntity store,
                                 InviteDetailResponse.DataEntity.ServicesEntity services) {
        title = invite.getTitle().trim();
        if (!NSMTypeUtils.isEmpty(title)) {
            mInviteTitleTv.setText(title);
        }
        String price = String.valueOf(services.getPrice()).trim();
        if (!NSMTypeUtils.isEmpty(price)) {
            mInvitePriceTv.setText("￥ " + price);
        }
        String target = NSMTypeUtils.getUserTarget(invite.getTarget());
        payType = invite.getPayType();
        String paytype = NSMTypeUtils.getUserPayType(payType);
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

        servicesLogo = services.getLogo();
        serviceId = invite.getServiceId();

        storeLongitude = store.getLatitude();
        storeLatitude = store.getLongitude();
    }

    private void disPathJoinersData(List<InviteDetailResponse.DataEntity.JoinersEntity> joiners) {
        if (inviteJoinerAdapter == null) {
            inviteJoinerAdapter = new InviteJoinerOldAdapter(joiners, this);
            mInviteJoinerGv.setAdapter(inviteJoinerAdapter);
        } else {
            inviteJoinerAdapter.setJoiners(joiners, this);
            inviteJoinerAdapter.notifyDataSetChanged();
        }
    }

    private void disPathUserData(InviteDetailResponse.DataEntity.UserEntity user) {
        //用户Id
        publisherId = String.valueOf(user.getId());

        String name = user.getName();
        if (!NSMTypeUtils.isEmpty(name)) {
            mInviteUserNameTv.setText(name);
        }
        mInviteUserGenderIv.setImageResource(1 == user.getGender() ? R.drawable.man_icon : R.drawable.user_female);
        mInviteUserAgeTv.setText(TimeUtils.getAge(user.getBirthday()) + " 岁");
        mInviteUserSignTv.setText(user.getSign());
        String userLogo = user.getThumbnailslogo();
        userHeaderPhoto = new ArrayList<>();
        userHeaderPhoto.add(user.getLogo());
        if (!NSMTypeUtils.isEmpty(userLogo)) {
            HttpLoader.getImageLoader().get(userLogo,
                    ImageLoader.getImageListener(mInviteUserLogoIv, R.drawable.picture_moren, R.drawable.picture_moren));
        } else {
            mInviteUserLogoIv.setImageResource(R.drawable.picture_moren);
        }
    }

    private void disPathUserPhotoData(List<InviteDetailResponse.DataEntity.UserphonesEntity> userphones) {

        userPhotos = new ArrayList<>();

        adapter = new UserDetailPhotosAdapter(this, getUserPhotos(userphones), mGallery, mInflater);
        mInviteUserPhotosGv.setAdapter(adapter);

        int size = userphones.size();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        int columnsNum = size;
        int allWidth = (int) (86 * columnsNum * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                allWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        mInviteUserPhotosGv.setLayoutParams(params);
        mInviteUserPhotosGv.setStretchMode(GridView.NO_STRETCH);
        mInviteUserPhotosGv.setNumColumns(columnsNum);
    }

    private void disPathServicePhotoData(List<InviteDetailResponse.DataEntity.ServicephotosEntity> servicephotos) {
        mVpCarousel.setAdapter(new ServicePhotoAdapter(getServicePhotos(servicephotos)));
        mIndicateMain.setViewPage(mVpCarousel);
    }

    private List<String> getUserPhotos(List<InviteDetailResponse.DataEntity.UserphonesEntity> userphones) {
        List<String> lists = new ArrayList<>();
        for (InviteDetailResponse.DataEntity.UserphonesEntity photosEntity : userphones) {
            lists.add(photosEntity.getThumbnailsPhoto());
            userPhotos.add(photosEntity.getPhoto());
        }

//        for (InviteDetailResponse.DataEntity.UserphonesEntity userphonesEntity : userphones) {
//            String photo = userphonesEntity.getThumbnails();
//            if (!TextUtils.isEmpty(photo)) {
//            }
//        }
        return lists;
    }

    public static List<String> getServicePhotos(List<InviteDetailResponse.DataEntity.ServicephotosEntity> servicephotos) {
        List<String> lists = new ArrayList<>();
        for (InviteDetailResponse.DataEntity.ServicephotosEntity photosEntity : servicephotos) {
            lists.add(photosEntity.getPhoto());
        }
        return lists;
    }

    private void requestJoin() {
        mInviteJoinBtn.setClickable(false);
        mInviteJoinBtn.setText("正在加入");
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("inviteId", inviteId);
        HttpLoader.post(ConstantsWhatNSM.URL_INVITE_JOIN, params, RequestJoinResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_INVITE_JOIN, this, false).setTag(this);
    }

    private void refreshMyState(List<InviteDetailResponse.DataEntity.JoinersEntity> joiners) {
        if (joiners != null) {
            //用户自己的Id
            String myUserId = NSMTypeUtils.getMyUserId();

            //邀请者该单的状态
            int newstatus = inviteDetailResponse.getData().getInvite().getNewstatus();
            if (TextUtils.isEmpty(myUserId)) {
                return;
            }
            //初始化状态
            mInviteJoinBtn.setVisibility(View.GONE);
            mInviteUserFaceRl.setVisibility(View.INVISIBLE);
            for (InviteDetailResponse.DataEntity.JoinersEntity joiner : joiners) {
                if (myUserId.equals(String.valueOf(joiner.getUserId()))) {
                    //说明自己已经加入了,判断加入状态.
                    switch (joiner.getNewstatus()) {
                        //未支付
                        case 0:
                            if (50 == newstatus || 100 == newstatus) {
                                showToastInfo("您尚未支付该活动");
                                toPayForVip(inviteDetailResponse.getData().getTrade());
                            }
//                            mInviteJoinBtn.setVisibility(View.GONE);
//                            mInviteUserFaceRl.setVisibility(View.INVISIBLE);
//                            joinBtnState = TO_PAY;
//                            break;
                            //会员支付
                        case 50:
//                            mInviteJoinBtn.setVisibility(View.GONE);
//                            mInviteUserFaceRl.setVisibility(View.INVISIBLE);
//                            break;
                            //已支付
                        case 100:
                            mInviteJoinBtn.setVisibility(View.GONE);
                            mInviteUserFaceRl.setVisibility(View.INVISIBLE);
                            break;
                        //去支付
                        case 120:
                            if (120 == newstatus) {
                                mInviteJoinBtn.setVisibility(View.VISIBLE);
                                mInviteJoinBtn.setText("立即付款");
                                mInviteUserFaceRl.setVisibility(View.INVISIBLE);
                                joinBtnState = TO_PAY;
                            } else {
                                mInviteJoinBtn.setVisibility(View.GONE);
                                mInviteUserFaceRl.setVisibility(View.INVISIBLE);
                            }
                            break;
                        //成单
                        case 150:
                            mInviteJoinBtn.setVisibility(View.INVISIBLE);
                            mInviteUserFaceRl.setVisibility(View.VISIBLE);
                            int setout = joiner.getSetout();
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
                                int signing = joiner.getSigning();
                                if (0 == signing) {
                                    mInviteUserGoBtn.setVisibility(View.VISIBLE);
                                    mInviteUserGoBtn.setText("确认见面");
                                    startBtnState = START_SURE_FACE;
                                    mInviteUserRefreshBtn.setVisibility(View.VISIBLE);
                                    mInviteOtherDestenceTv.setVisibility(View.VISIBLE);
                                } else if (1 == signing) {
                                    mInviteUserGoBtn.setVisibility(View.VISIBLE);
                                    mInviteUserGoBtn.setText(R.string.signed_yet);
                                    mInviteUserGoBtn.setClickable(false);
                                    mInviteUserRefreshBtn.setVisibility(View.INVISIBLE);
                                    mInviteOtherDestenceTv.setVisibility(View.INVISIBLE);
                                }
                            }
                            break;
                        //已见面
                        case 180:
                            mInviteJoinBtn.setVisibility(View.INVISIBLE);
                            mInviteUserFaceRl.setVisibility(View.VISIBLE);
                            mInviteUserGoBtn.setVisibility(View.VISIBLE);
                            mInviteUserGoBtn.setClickable(false);
                            mInviteUserGoBtn.setText(R.string.signed_yet);
                            mInviteUserRefreshBtn.setVisibility(View.INVISIBLE);
                            mInviteOtherDestenceTv.setVisibility(View.INVISIBLE);
                            break;
                        //已完成
                        case 200:
                            mInviteJoinBtn.setVisibility(View.INVISIBLE);
                            mInviteUserFaceRl.setVisibility(View.VISIBLE);
                            mInviteUserGoBtn.setVisibility(View.VISIBLE);
                            mInviteUserGoBtn.setClickable(false);
                            mInviteUserGoBtn.setText("已完成");
                            mInviteUserRefreshBtn.setVisibility(View.INVISIBLE);
                            mInviteOtherDestenceTv.setVisibility(View.INVISIBLE);
                            break;
                        //邀请者同意其他人,自己被拒绝
                        //穿透
                        case 650:
                            //邀请者主动取消
                        case 700:
                            //参与者主动取消
                        case 710:
                            //超时自动退款
                        case 750:
                            //删除
                        case 900:
                        default:
                            mInviteJoinBtn.setVisibility(View.GONE);
                            mInviteUserFaceRl.setVisibility(View.INVISIBLE);
                            break;
                    }
                    return;
                }
            }
        }
        mInviteJoinBtn.setVisibility(View.VISIBLE);
        mInviteJoinBtn.setText("申请加入");
        mInviteJoinBtn.setClickable(true);
        joinBtnState = TO_ADD;
        mInviteUserFaceRl.setVisibility(View.INVISIBLE);
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
//                    InviteJoinerOldDetailActivity.this.latitude = gps.getWgLat();
//                    InviteJoinerOldDetailActivity.this.longgitude = gps.getWgLon();
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

                    StringBuffer sb = new StringBuffer(256);
                    sb.append("time : ");
                    /**
                     * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                     * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                     */
                    sb.append(location.getTime());
                    sb.append("\nlocType : ");// 定位类型
                    sb.append(location.getLocType());
                    sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                    Log.i("hehe", "sb.tostring=" + sb.toString());
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
