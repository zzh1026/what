package com.neishenme.what.activity;

import android.Manifest;
import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.baidu.location.BDLocation;
import com.neishenme.what.R;
import com.neishenme.what.adapter.MainAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.ActiveMainShowDialog;
import com.neishenme.what.bean.HomeFilterParams;
import com.neishenme.what.bean.MainMenuResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.common.ActiveSPConfig;
import com.neishenme.what.common.AppSharePreConfig;
import com.neishenme.what.common.CityLocationConfig;
import com.neishenme.what.common.ShowGuideConfig;
import com.neishenme.what.component.MutiComponent;
import com.neishenme.what.component.SimpleComponent;
import com.neishenme.what.dialog.ActiveShowDialog;
import com.neishenme.what.dialog.HomeMenuFilter;
import com.neishenme.what.dialog.MenuFiltrateDialog;
import com.neishenme.what.eventbusobj.HomeNewsNumber;
import com.neishenme.what.fragment.HomeInviteFragment;
import com.neishenme.what.fragment.HomeTrivelFragment;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.OnAudioPermissionListener;
import com.neishenme.what.receiver.MyJPushReceiver;
import com.neishenme.what.service.SocketGetLocationService;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.DensityUtil;
import com.neishenme.what.utils.HuanXinUtils;
import com.neishenme.what.utils.LocationUtils;
import com.neishenme.what.utils.MPermissionManager;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.PackageVersion;
import com.neishenme.what.utils.StatusBarUtil;
import com.neishenme.what.utils.UIUtils;
import com.neishenme.what.utils.UpdataPersonInfo;
import com.neishenme.what.view.CircleImageView;
import com.neishenme.what.view.NoScrollViewPager;
import com.neishenme.what.view.highlight.view.Guide;
import com.neishenme.what.view.highlight.view.GuideBuilder;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;

import java.util.List;

import de.greenrobot.event.EventBus;
import jp.wasabeef.blurry.Blurry;

/**
 * Created by Administrator on 2016/5/12.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, HomeMenuFilter.OnFiltrateSelectedListener, MenuFiltrateDialog.OnFiltrateSelectedListener, HttpLoader.ResponseListener {

    private static final int MESSAGE_CAN_EXIT = 1;
    private static final int MESSAGE_LOGOUT_REFRESH = 2;    //退出登录后刷新数据
    private static final int MESSAGE_REFRESH_NOTE_COLOR = 3;    //点击按钮闪动的时候1秒后重置字体颜色

    private static final int TIME_REFRESH_NOTE_COLOR = 100;    //点击按钮闪动的时候1秒后重置字体颜色的时间

    public static final int REQUEST_CODE_JOIN_INVITE_PRICE = 12;  //付费加入活动的金额请求码

    //发布等花钱的时候请求码, 因为需要根据不同的情况回调到不同的fragment,所以需要不同的请求码
    public static final int REQUEST_CODE_PAY_ORDER = 14;   //付费加入请求码
    public static final int REQUEST_CODE_PICK_CITY = 18;   //主界面城市选择,(左上角的切换城市)请求码

    private String mOrderInviteId;  //标记通知栏到达的order标记

    private NoScrollViewPager vpHome;

    private TextView tvHomeInvite, tvHomeWho;
    private ImageView btnBell;
    public View indicatorLine;
    public LinearLayout rlMenu;
    public ImageView ivHomeMenuFilter, ivHomeMenuSearch, ivHomeMenuSetting;
    private ImageView ivBack;
    private CircleImageView ivUserLogo;
    private LinearLayout llPersonalCenter;
    private LinearLayout llVipCenter;
    private LinearLayout llWallet;
    private LinearLayout llSetUp;
    private LinearLayout llMyActive;
    private LinearLayout llSignOut;

    private View mViewShowLine;

    private RelativeLayout mHomeActiveLayout;
    private RelativeLayout mHomeTripLayout;
    private TextView mHomeTripInfoTip;

    private TextView mMain11;
    private ImageView mMain12;
    private TextView mMain13;
    private TextView mMain21;
    private ImageView mMain22;
    private TextView mMain23;
    private TextView mMain31;
    private ImageView mMain32;
    private TextView mMain33;
    private TextView mMain41;
    private ImageView mMain42;
    private TextView mMain43;
    private TextView mMain51;
    private ImageView mMain52;
    private TextView mMain53;
    private TextView mMain61;
    private ImageView mMain62;
    private TextView mMain63;

    private LinearLayout mMainPlaceSelect;  //点击选择位置
    private TextView mMainPlaceInfo;        //显示已经选择的位置
    public String showCityInvite = CityLocationConfig.cityLocation;     //要展示的城市名字,默认为定位城市
    public int showCityAreaId = CityLocationConfig.cityLocationId;  //展示的ID ，areaId

    public double latitude = CityLocationConfig.cityLatitude;     //当前城市的经纬度
    public double longgitude = CityLocationConfig.cityLonggitude;

    private OnAudioPermissionListener mOnAudioPermissionListener = null;

    private int offset;     //标记scrollview的滑动高度
    private float everyDistence;    //标记主界面下部横线的滑动距离

    private TextView mHomeNewsNumber;

    private float leftBtnX;
    private float leftBtnWidth;
    private float rightBtnX;
    private float originalIndicatorLineLeftMargin;

    private RelativeLayout.LayoutParams indicatorLineLayoutParams;
    private ImageView mUserHeaderBlurLogo;

    private RelativeLayout rlSignZero;
    private ScrollView rlSignZeroZero;

    private boolean isTop = false;//scrollView 已经在顶部了
    private boolean canExit = false;//scrollView 已经在顶部了
    private boolean isLoadSuccess = false;//标记是头像的背景图否已经加载过了,,,,尽量每次启动只加载一次blur头像
    private boolean shouldShowGuide1 = false;    //标记是否应该展示加入的功能引导
    private boolean shouldShowGuide2 = false;    //标记是否应该展示发布的功能引导

    public boolean isShouldNotRefresh = false;  //标记是否进入邀请详情界面, 进入详情界面回来是不刷新的

    private MainAdapter mainAdapter;
    public RelativeLayout lltab;
    int viewPage = 0;

    private boolean isFirstShow = false;    //标记是否是第一次进入,如果是第一次进入会对行程进行处理

    public boolean mTitleBarOnShowing = false;

    private RelativeLayout mRlConteners;

    private HomeInviteFragment homeInviteFragment;
    private HomeTrivelFragment homeTrivelFragment;

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_CAN_EXIT:
                    canExit = false;
                    break;
                case MESSAGE_LOGOUT_REFRESH:
                    showAnim();
                    resetOrderInviteId(mOrderInviteId);
                    UpdataPersonInfo.logoutPersonInfo();
                    break;
                case MESSAGE_REFRESH_NOTE_COLOR:
                    setChangeText(0);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentStatus(this, true);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        rlSignZeroZero = (ScrollView) findViewById(R.id.rl_sign_zero_zero);
        lltab = (RelativeLayout) findViewById(R.id.ll_tab);
        mRlConteners = (RelativeLayout) findViewById(R.id.rl_conteners);
        rlMenu = (LinearLayout) findViewById(R.id.rl_menu);

        vpHome = (NoScrollViewPager) findViewById(R.id.vp_home);

        tvHomeInvite = (TextView) findViewById(R.id.tv_home_invite);
        tvHomeWho = (TextView) findViewById(R.id.tv_home_who);

        btnBell = (ImageView) findViewById(R.id.btn_bell);
        indicatorLine = findViewById(R.id.line_indicator);

        rlSignZero = (RelativeLayout) findViewById(R.id.rl_sign_zero);
        mUserHeaderBlurLogo = (ImageView) findViewById(R.id.user_header_blur_logo);

        ivHomeMenuFilter = (ImageView) findViewById(R.id.iv_home_menu_filter);
        ivHomeMenuSearch = (ImageView) findViewById(R.id.iv_home_menu_search);
        ivHomeMenuSetting = (ImageView) findViewById(R.id.iv_home_menu_setting);

        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivUserLogo = (CircleImageView) findViewById(R.id.iv_user_logo);
        llPersonalCenter = (LinearLayout) findViewById(R.id.ll_personal_center);
        llVipCenter = (LinearLayout) findViewById(R.id.ll_vip_center);
        llWallet = (LinearLayout) findViewById(R.id.ll_wallet);
        llSetUp = (LinearLayout) findViewById(R.id.ll_set_up);
        llSignOut = (LinearLayout) findViewById(R.id.ll_sign_out);
        mHomeNewsNumber = (TextView) findViewById(R.id.home_news_number);

        llMyActive = (LinearLayout) findViewById(R.id.ll_my_active);
        mViewShowLine = findViewById(R.id.view_show_line);

        mHomeActiveLayout = (RelativeLayout) findViewById(R.id.home_active_layout);
        mHomeTripLayout = (RelativeLayout) findViewById(R.id.home_trip_layout);
        mHomeTripInfoTip = (TextView) findViewById(R.id.home_trip_info_tip);

        mMain11 = (TextView) findViewById(R.id.main_1_1);
        mMain12 = (ImageView) findViewById(R.id.main_1_2);
        mMain13 = (TextView) findViewById(R.id.main_1_3);
        mMain21 = (TextView) findViewById(R.id.main_2_1);
        mMain22 = (ImageView) findViewById(R.id.main_2_2);
        mMain23 = (TextView) findViewById(R.id.main_2_3);
        mMain31 = (TextView) findViewById(R.id.main_3_1);
        mMain32 = (ImageView) findViewById(R.id.main_3_2);
        mMain33 = (TextView) findViewById(R.id.main_3_3);
        mMain41 = (TextView) findViewById(R.id.main_4_1);
        mMain42 = (ImageView) findViewById(R.id.main_4_2);
        mMain43 = (TextView) findViewById(R.id.main_4_3);
        mMain51 = (TextView) findViewById(R.id.main_5_1);
        mMain52 = (ImageView) findViewById(R.id.main_5_2);
        mMain53 = (TextView) findViewById(R.id.main_5_3);
        mMain61 = (TextView) findViewById(R.id.main_6_1);
        mMain62 = (ImageView) findViewById(R.id.main_6_2);
        mMain63 = (TextView) findViewById(R.id.main_6_3);

        mMainPlaceSelect = (LinearLayout) findViewById(R.id.main_place_select);
        mMainPlaceInfo = (TextView) findViewById(R.id.main_place_info);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();

        //初始化上部的高
        int stateHeight = getStatusBarHeight();
        ViewGroup.LayoutParams params = rlSignZero.getLayoutParams();
        offset = height - DensityUtil.dip2px(this, 53) - stateHeight;
        params.height = offset;
        rlSignZero.setLayoutParams(params);

        //初始化主页的高
        ViewGroup.LayoutParams params1 = vpHome.getLayoutParams();
        params1.height = height - stateHeight;
        vpHome.setLayoutParams(params1);
    }

    @Override
    protected void initListener() {
        vpHome.addOnPageChangeListener(this);
        mHomeActiveLayout.setOnClickListener(this);
        mHomeTripLayout.setOnClickListener(this);
        ivHomeMenuFilter.setOnClickListener(this);
        ivHomeMenuSearch.setOnClickListener(this);
        ivHomeMenuSetting.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        llVipCenter.setOnClickListener(this);
        llWallet.setOnClickListener(this);
        llPersonalCenter.setOnClickListener(this);
        llSetUp.setOnClickListener(this);
        llMyActive.setOnClickListener(this);
        llSignOut.setOnClickListener(this);
        btnBell.setOnClickListener(this);
        mMainPlaceSelect.setOnClickListener(this);

        rlSignZeroZero.post(new Runnable() {
            public void run() {
                rlSignZeroZero.scrollTo(0, offset);
            }
        });
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);

        //启动服务
        if (NSMTypeUtils.isLogin() && !PackageVersion.isServiceRunning(this)) {
            startService(new Intent(this, SocketGetLocationService.class));
        }

        initIndicatorLinePosition();

        tvHomeInvite.setTextColor(getResources().getColor(R.color.Whitec2c2c2select));
        tvHomeWho.setTextColor(getResources().getColor(R.color.Whitec2c2c2));

        mainAdapter = new MainAdapter(getSupportFragmentManager());
        vpHome.setAdapter(mainAdapter);

        homeInviteFragment = (HomeInviteFragment) mainAdapter.getItem(0);
        homeTrivelFragment = (HomeTrivelFragment) mainAdapter.getItem(1);

//        if (ShowGuideConfig.shouldShowRelease() && !isTop) {
//            shouldShowGuide2 = true;
//        }
//
//        if (ShowGuideConfig.shouldShowJoin() && !isTop) {
//            shouldShowGuide1 = true;
//        } else {
//            getActiveDialog();
//        }
        getActiveDialog();

        upShowCity();

        Intent intent = getIntent();
        String showAd = intent.getStringExtra("isShowAd");
        if (!TextUtils.isEmpty(showAd)) {
            Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(showAd));
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
        }

        isFirstShow = true;

        mOrderInviteId = App.SP.getString(MyJPushReceiver.MSG_ORDER_INVITE_ID, null);
        if (TextUtils.isEmpty(mOrderInviteId)) {
            mHomeTripInfoTip.setVisibility(View.INVISIBLE);
        } else {
            mHomeTripInfoTip.setVisibility(View.VISIBLE);
        }

        MyJPushReceiver.addOnMessageReceivedListener(new MyJPushReceiver.OnMessageReceived() {
            @Override
            public void onMessageReceived(final String inviteId) {
                if (UIUtils.isRunInMainThread()) {
                    mOrderInviteId = inviteId;
                    if (!TextUtils.isEmpty(mOrderInviteId)) {
                        mHomeTripInfoTip.setVisibility(View.VISIBLE);
                        if (homeTrivelFragment != null) {
                            homeTrivelFragment.setOrderInviteId(mOrderInviteId);
                        }
                    }
                } else {
                    UIUtils.runInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mOrderInviteId = inviteId;
                            if (!TextUtils.isEmpty(mOrderInviteId)) {
                                mHomeTripInfoTip.setVisibility(View.VISIBLE);
                                if (homeTrivelFragment != null) {
                                    homeTrivelFragment.setOrderInviteId(mOrderInviteId);
                                }
                            }
                        }
                    });
                }
            }
        });

        loginToHuanXin();
    }

    public void resetOrderInviteId(String inviteId) {
        if (!TextUtils.isEmpty(mOrderInviteId) && mOrderInviteId.equals(inviteId)) {
            mOrderInviteId = null;
            mHomeTripInfoTip.setVisibility(View.INVISIBLE);
            App.EDIT.remove(MyJPushReceiver.MSG_ORDER_INVITE_ID).commit();
            if (homeTrivelFragment != null) {
                homeTrivelFragment.setOrderInviteId(mOrderInviteId);
            }
        }
    }

    @Override
    protected void onStart() {
//        ActivityUtils.startActivity(this, ActiveSignUpActivity.class);

        if (!AndPermission.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                !AndPermission.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestLocationPermission();
        }

        showHideTitleBar(true, lltab, rlMenu);

        //判断是否登录情况下进入发布界面
        tryToReleaseFragment();

        //获取背景图
        if (NSMTypeUtils.isLogin()) {
            if (!isLoadSuccess) //没有加载成功
                getBackGround();
        }

        if (viewPage == 0) {
            ivHomeMenuFilter.startAnimation(bottomAnimation(200, ivHomeMenuFilter));
            ivHomeMenuSearch.startAnimation(bottomAnimation(400, ivHomeMenuSearch));
            ivHomeMenuSetting.startAnimation(bottomAnimation(600, ivHomeMenuSetting));
        } else {
            ivHomeMenuFilter.setVisibility(View.INVISIBLE);
            ivHomeMenuSearch.startAnimation(bottomAnimation(200, ivHomeMenuSearch));
            ivHomeMenuSetting.startAnimation(bottomAnimation(400, ivHomeMenuSetting));
        }

        super.onStart();
        //清空之前的所有activity
        App.removeAllActivity();

        EventBus.getDefault().removeAllStickyEvents();
        EventBus.clearCaches();

        //被T;
        if (App.SP.getBoolean(AppSharePreConfig.USER_LOGIN_BE_T, false)) {
            showLoginBeT();
        }

        String newsNumber = App.SP.getString("newsNumber", "");
        if (!TextUtils.isEmpty(newsNumber)) {
            mHomeNewsNumber.setVisibility(View.VISIBLE);
            mHomeNewsNumber.setText(newsNumber);
        }

        //每次进入该界面会通过该接口去请求活动来动态设置一个menu的条目, 现在已经去掉了 2017/4/19
//        getActiveInfo();

        updateLocation();

        showCityInvite = CityLocationConfig.citySelected;
        if (getString(R.string.city_location_wrong).equals(mMainPlaceInfo.getText().toString().trim())) {
            if (TextUtils.isEmpty(showCityInvite) || showCityAreaId == 0) {
                getLocation();
            } else {
                mMainPlaceInfo.setText(showCityInvite);
                showCityAreaId = CityLocationConfig.cityLocationId;
                latitude = CityLocationConfig.cityLatitude;
                longgitude = CityLocationConfig.cityLonggitude;
            }
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void updateLocation() {
        LocationUtils.getLocationByGCJ_02(new LocationUtils.OnGetLocationListener() {
            @Override
            public void onGetLocation(double latitude, double longgitude, BDLocation location) {
                MainActivity.this.latitude = latitude;
                MainActivity.this.longgitude = longgitude;
            }

            @Override
            public void onGetError() {
            }
        });
    }

    private void getActiveInfo() {
        HttpLoader.get(ConstantsWhatNSM.URL_ACTIVE_MENU_LIST, null, MainMenuResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_MENU_LIST, this, false).setTag(this);
    }

    private void showLoginBeT() {
        //将被T的情况 删除;.
        App.EDIT.remove(AppSharePreConfig.USER_LOGIN_BE_T).commit();

        App.EDIT.putString("newsNumber", "").commit();

        new AlertDialog.Builder(this).setCancelable(false).setTitle("帐号下线").setMessage("您的帐号在其他设备登录,您被迫下线")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UpdataPersonInfo.logoutPersonInfo();
                        resetOrderInviteId(mOrderInviteId);
                        return;
                    }
                }).show();
    }

    private void tryToReleaseFragment() {
        if (!NSMTypeUtils.isLogin()) {
            if (viewPage == 1) {
                vpHome.setCurrentItem(0, false);
                viewPage = 0;
                ActivityUtils.startActivity(this, LoginActivity.class);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void blurAll() {
//        Blurry.with(MainActivity.this).radius(10).async().onto(mRlConteners);
    }

    public void blurCancel() {
//        Blurry.delete(mRlConteners);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        MyJPushReceiver.removeOnMessageReceivedListener();
        super.onDestroy();
    }

    private void getBackGround() {
        String logo = App.USERSP.getString("thumbnailslogo", "");
        if (!TextUtils.isEmpty(logo)) {
            HttpLoader.getImageLoader().get(logo, ImageLoader.getImageListener(
                    ivUserLogo, R.drawable.picture_moren, R.drawable.picture_moren, new ImageLoader.BitmapListener() {
                        @Override
                        public void onResponse(final ImageLoader.ImageContainer response) {
                            Blurry.with(MainActivity.this).radius(15).sampling(2).async().from(response.getBitmap()).into(mUserHeaderBlurLogo);
                            isLoadSuccess = true;
                        }
                    }));
        }
    }

    //获取状态栏高度
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        tryRefreshFragmentData();
    }

    private void tryRefreshFragmentData() {
        if (viewPage == 0) {
            if (homeInviteFragment != null && !isShouldNotRefresh) {
                homeInviteFragment.notifyData();
            } else {
                isShouldNotRefresh = false;
            }
        } else {
            if (homeTrivelFragment != null) {
                homeTrivelFragment.notifyData();
            }
        }
    }

    public void upShowCity() {
        showCityInvite = CityLocationConfig.citySelected;
        showCityAreaId = CityLocationConfig.citySelectedId;
        if (!TextUtils.isEmpty(showCityInvite)) {
            mMainPlaceInfo.setText(showCityInvite);
        } else {
            mMainPlaceInfo.setText(getString(R.string.city_location_wrong));
        }
    }

    private void getActiveDialog() {
        HttpLoader.get(ConstantsWhatNSM.URL_ACTIVE_SHOW_DIALOG, null, ActiveMainShowDialog.class,
                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_SHOW_DIALOG, this, false).setTag(this);
    }

    //初始化线的位置
    private void initIndicatorLinePosition() {
        indicatorLineLayoutParams = (RelativeLayout.LayoutParams) indicatorLine.getLayoutParams();

        tvHomeInvite.post(new Runnable() {
            @Override
            public void run() {
                tvHomeInvite.measure(0, 0);
                leftBtnX = tvHomeInvite.getLeft() + mHomeActiveLayout.getLeft();//邀请据左边距离
                leftBtnWidth = tvHomeInvite.getMeasuredWidth();//邀请的宽度
                tvHomeWho.measure(0, 0);
                rightBtnX = tvHomeWho.getLeft() + mHomeTripLayout.getLeft();
                everyDistence = rightBtnX - leftBtnX;
            }
        });


        indicatorLine.post(new Runnable() {
            @Override
            public void run() {
                indicatorLine.measure(0, 0);
                originalIndicatorLineLeftMargin = leftBtnX +
                        leftBtnWidth / 2 - DensityUtil.dip2px(MainActivity.this, 50) / 2;
                indicatorLineLayoutParams.leftMargin = (int) originalIndicatorLineLeftMargin;
                indicatorLine.setLayoutParams(indicatorLineLayoutParams);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isTop) {
            scrollUI(true);
            showAnim();
            return;
        }
        if (!canExit) {
            showToastInfo("再按一次退出");
            canExit = true;
            mHandler.sendEmptyMessageDelayed(MESSAGE_CAN_EXIT, 3000);
            return;
        }
        EventBus.getDefault().removeAllStickyEvents();
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            switch (requestCode) {
                case MPermissionManager.REQUEST_CODE_LOCATION:
                    if (TextUtils.isEmpty(CityLocationConfig.cityLocation)) {
                        getLocation();
                    }
                    break;
                case MPermissionManager.REQUEST_CODE_AUDIO:
                    if (mOnAudioPermissionListener != null) {
                        mOnAudioPermissionListener.onAudioPermissionSuccess();
                    }
                    break;
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            switch (requestCode) {
                case MPermissionManager.REQUEST_CODE_LOCATION:
                    MPermissionManager.showToSetting(MainActivity.this,
                            MPermissionManager.REQUEST_CODE_LOCATION, new MPermissionManager.OnNegativeListner() {
                                @Override
                                public void onNegative() {
                                    finish();
                                }
                            });
                    break;
                case MPermissionManager.REQUEST_CODE_AUDIO:
                    MPermissionManager.showToSetting(MainActivity.this,
                            MPermissionManager.REQUEST_CODE_AUDIO, new MPermissionManager.OnNegativeListner() {
                                @Override
                                public void onNegative() {
                                    if (mOnAudioPermissionListener != null) {
                                        mOnAudioPermissionListener.onAudioPermissionFiler();
                                    }
                                }
                            });
                    break;
            }
        }
    };

    private void requestLocationPermission() {
        AndPermission.with(this)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .rationale(mRationaleListener)
                .send();
    }

    public void requestAudioPermission(OnAudioPermissionListener mOnAudioPermissionListener) {
        this.mOnAudioPermissionListener = mOnAudioPermissionListener;
        AndPermission.with(this)
                .requestCode(MPermissionManager.REQUEST_CODE_AUDIO)
                .permission(Manifest.permission.RECORD_AUDIO)
                .rationale(mRationaleListener)
                .send();
    }

    private RationaleListener mRationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            String message = "";
            switch (requestCode) {
                case MPermissionManager.REQUEST_CODE_LOCATION:
                    message = MPermissionManager.REQUEST_LOCATION_DISCRIBE;
                    break;
                case MPermissionManager.REQUEST_CODE_AUDIO:
                    message = MPermissionManager.REQUEST_AUDIO_DISCRIBE;
                    break;
            }
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(MPermissionManager.REQUEST_TITLE)
                    .setMessage(message)
                    .setPositiveButton(MPermissionManager.REQUEST_POSITIVE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            rationale.resume();// 用户同意继续申请。
                        }
                    })
                    .setNegativeButton(MPermissionManager.REQUEST_NEGATIVE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            rationale.cancel(); // 用户拒绝申请。
                        }
                    }).show();
        }
    };

    public void getLocation() {
        CityLocationConfig.getLocationUtil(new CityLocationConfig.OnCitySuccess() {
            @Override
            public void onCitySuccess() {
                showCityInvite = CityLocationConfig.citySelected;
                showCityAreaId = CityLocationConfig.citySelectedId;
                if (!TextUtils.isEmpty(showCityInvite)) {
                    mMainPlaceInfo.setText(showCityInvite);
                    homeInviteFragment.notifyData();
                } else {
                    mMainPlaceInfo.setText(getString(R.string.city_location_wrong));
                }
            }

            @Override
            public void onCityError() {

            }
        });
    }

    //登录到环信
    private void loginToHuanXin() {
        if (NSMTypeUtils.isLogin()) {
            if (!HuanXinUtils.isLoginToHX()) {
                HuanXinUtils.login();
            }
        }
    }

    public void onEventMainThread(HomeNewsNumber event) {
        if (0 != event.num) {
            mHomeNewsNumber.setVisibility(View.VISIBLE);
            mHomeNewsNumber.setText(String.valueOf(event.num));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //三大界面**********************************************************************
            //活动
            case R.id.home_active_layout:
                if (vpHome.getCurrentItem() == 0) {
                    return;
                }
                vpHome.setCurrentItem(0);
                if (isTop) {
                    scrollUI(true);
                }
                break;
            //行程
            case R.id.home_trip_layout:
                if (vpHome.getCurrentItem() == 1) {
                    return;
                }
                vpHome.setCurrentItem(1);
                if (isTop) {
                    scrollUI(true);
                }
                break;
            //城市选择
            case R.id.main_place_select:
                startActivityForResult(new Intent(this, CityPickerActivity.class), REQUEST_CODE_PICK_CITY);
                break;
            //三大界面**********************************************************************

            //菜单选择**********************************************************************
            //筛选
            case R.id.iv_home_menu_filter:
                if (viewPage == 0) {
                    HomeMenuFilter filter = new HomeMenuFilter(this, this);
                    filter.show();
                }
                break;
            //发布
            case R.id.iv_home_menu_search:
                if (!NSMTypeUtils.isLogin()) {
                    ActivityUtils.startActivity(this, LoginActivity.class);
                    return;
                }
                toReleaseAct();
                break;
            //进入设置
            case R.id.iv_home_menu_setting:
                if (!NSMTypeUtils.isLogin()) {
                    ActivityUtils.startActivity(this, LoginActivity.class);
                    return;
                }
                scrollUI(false);
                break;
            //菜单选择**********************************************************************

            //设置*********************************************************************************
            //跳回主界面
            case R.id.iv_back:
                showAnim();
                scrollUI(true);
                break;
            //界面铃铛---信息
            case R.id.btn_bell:
                if (NSMTypeUtils.isLogin()) {
                    mHomeNewsNumber.setText("");
                    mHomeNewsNumber.setVisibility(View.INVISIBLE);
                    App.EDIT.putString("newsNumber", "").commit();
                    ActivityUtils.startActivity(this, HomeNewsActivity.class);
                } else {
                    ActivityUtils.startActivity(this, LoginActivity.class);
                }
                break;
            //会员中心
            case R.id.ll_vip_center:
                setChangeText(2);
                ActivityUtils.startActivity(this, VIPCenterActivity.class);
                break;
            //我的钱包
            case R.id.ll_wallet:
                setChangeText(3);
                ActivityUtils.startActivity(this, MyWalletActivity.class);
                break;
            //设置
            case R.id.ll_set_up:
                setChangeText(4);
                ActivityUtils.startActivity(this, SettingActivity.class);
                break;
            //我的活动
            case R.id.ll_my_active:
                setChangeText(6);
                ActivityUtils.startActivity(this, MyActiveActivity.class);
                break;
            //退出
            case R.id.ll_sign_out:
                setChangeText(5);
                showLoginOutDialog();
                break;
            //个人中心
            case R.id.ll_personal_center:
                isLoadSuccess = false;
                setChangeText(1);
                UserDetailActivity.startUserDetailAct(this, Integer.parseInt(NSMTypeUtils.getMyUserId()), false);
                break;
            //设置完毕*********************************************************************************
        }
    }

    public void toReleaseAct() {
        ActivityUtils.startActivity(this, ReleaseQuickActivity.class);
    }

    private void scrollUI(boolean isShowMain) {
        if (isShowMain) {
            rlSignZeroZero.smoothScrollBy(0, offset);
            isTop = false;
            tryToReleaseFragment();
            tryRefreshFragmentData();
        } else {
            rlSignZeroZero.smoothScrollBy(0, -offset);
            isTop = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_JOIN_INVITE_PRICE:    //付费加入输入金额
                if (resultCode == RESULT_OK) {
                    homeInviteFragment.onMoneyEdit(data.getStringExtra(EditPayMoneyActivity.PAY_MONEY_BACK_DATA));
                }
                break;
            case REQUEST_CODE_PAY_ORDER:    //付费加入支付
                if (resultCode == RESULT_OK) {
                    homeInviteFragment.onPaySuccess();
                } else {
                    showToastError("支付失败");
                }
                break;
            case REQUEST_CODE_PICK_CITY:  //更换城市
                if (resultCode == RESULT_OK) {
                    upShowCity();
                    if (homeInviteFragment != null) {
                        homeInviteFragment.notifyData();
                    }
                }
                break;
        }
    }

    public void expVip() {
        if (vpHome.getCurrentItem() == 2) {
            return;
        }
        vpHome.setCurrentItem(2);
    }

    private void showLoginOutDialog() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(getString(R.string.main_act_sure_login))
                .setPositiveButton("继续退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isLoadSuccess = false;
                        isFirstShow = true;
                        if (vpHome.getCurrentItem() == 1) {
                            vpHome.setCurrentItem(0);
                        }
                        scrollUI(true);
                        mHandler.sendEmptyMessageDelayed(MESSAGE_LOGOUT_REFRESH, 200);
                    }
                })
                .setNegativeButton("取消退出", null).show();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //position :当前页面，及你点击滑动的页面 positionOffset:当前页面偏移的百分比 positionOffsetPixels:当前页面偏移的像素位置
        indicatorLineLayoutParams.leftMargin = (int)
                (originalIndicatorLineLeftMargin + everyDistence * (positionOffset + position));
        indicatorLine.setLayoutParams(indicatorLineLayoutParams);
        lltab.setVisibility(View.VISIBLE);

        if (shouldShowGuide2 && viewPage == 2 && positionOffset == 0) {
            shouldShowGuide2 = false;
            ShowGuideConfig.shownReleaseed();
        }
    }

    @Override
    public void onPageSelected(int position) {
        viewPage = position;
        if (position == 0) {
            tvHomeInvite.setTextColor(getResources().getColor(R.color.Whitec2c2c2select));
            tvHomeWho.setTextColor(getResources().getColor(R.color.Whitec2c2c2));
            ivHomeMenuFilter.setVisibility(View.VISIBLE);
            ivHomeMenuSearch.setVisibility(View.VISIBLE);
            ivHomeMenuSetting.setVisibility(View.VISIBLE);
        } else {
            tvHomeInvite.setTextColor(getResources().getColor(R.color.Whitec2c2c2));
            tvHomeWho.setTextColor(getResources().getColor(R.color.Whitec2c2c2select));
            ivHomeMenuFilter.setVisibility(View.INVISIBLE);
            ivHomeMenuSearch.setVisibility(View.VISIBLE);
            ivHomeMenuSetting.setVisibility(View.VISIBLE);
        }

        showHideTitleBar(true, lltab, rlMenu);

        tryToReleaseFragment();
        tryRefreshFragmentData();
    }

    public void setPagingEnabled(boolean b) {
        vpHome.setPagingEnabled(b);
    }

    public void tryShowGuide1() {
        if (shouldShowGuide1 && viewPage == 0) {
            shouldShowGuide1 = false;
            ShowGuideConfig.shownJoined();
            homeInviteFragment.showGuideView();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        lltab.setVisibility(View.VISIBLE);
    }

    private Animator mAnimatorTitle;
    private Animator mAnimatorBottomMenu;

    public void showHideTitleBar(boolean tag, View titleBar, View bottomMenu) {
//        if (isTop) {
//            return;
//        }
//
//        if (mAnimatorTitle != null && mAnimatorTitle.isRunning()) {
//            mAnimatorTitle.cancel();
//        }
//
//        if (mAnimatorBottomMenu != null && mAnimatorBottomMenu.isRunning()) {
//            mAnimatorBottomMenu.cancel();
//        }
//        if (tag) {
//            //显示titleBar
//            mAnimatorTitle = ObjectAnimator.ofFloat(titleBar, "translationY", titleBar.getTranslationY(), 0);
//            mAnimatorBottomMenu = ObjectAnimator.ofFloat(bottomMenu, "translationY", bottomMenu.getTranslationY(), 0f);
//            btnBell.setBackgroundResource(R.drawable.small_bell);
//        } else {
//            //隐藏titlBar
//            mAnimatorTitle = ObjectAnimator.ofFloat(titleBar, "translationY", titleBar.getTranslationY(), -titleBar.getHeight());
//            mAnimatorBottomMenu = ObjectAnimator.ofFloat(bottomMenu, "translationY", bottomMenu.getTranslationY(), bottomMenu.getHeight() + DensityUtil.dip2px(MainActivity.this, 18));
//            btnBell.setBackgroundResource(R.drawable.small_bell_black);
//        }
//        mAnimatorTitle.start();
//        mAnimatorBottomMenu.start();
//        mAnimatorTitle.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                mTitleBarOnShowing = false;
//                super.onAnimationEnd(animation);
//            }
//        });
    }

    private void showAnim() {
        if (viewPage == 0 && vpHome.getCurrentItem() == 0) {
            ivHomeMenuFilter.startAnimation(bottomAnimation(200, ivHomeMenuFilter));
            ivHomeMenuSearch.startAnimation(bottomAnimation(400, ivHomeMenuSearch));
            ivHomeMenuSetting.startAnimation(bottomAnimation(600, ivHomeMenuSetting));
        } else if (viewPage == 1 && vpHome.getCurrentItem() == 1) {
            ivHomeMenuSearch.startAnimation(bottomAnimation(400, ivHomeMenuSearch));
            ivHomeMenuSetting.startAnimation(bottomAnimation(600, ivHomeMenuSetting));
        }
    }

    public void tryShowMyTrip() {
        if (isFirstShow) {
            isFirstShow = false;
            vpHome.setCurrentItem(1, true);
        }
    }

    public AnimationSet bottomAnimation(final long l, final ImageView iv) {
        AnimationSet animationSet = new AnimationSet(true);
        RotateAnimation rotateAnimation = new RotateAnimation(0, -15,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f);
        rotateAnimation.setDuration(200);
        animationSet.addAnimation(rotateAnimation);
        animationSet.setStartOffset(l);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationSet animationSet2 = new AnimationSet(true);
                RotateAnimation rotateAnimation = new RotateAnimation(-15, 0,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 1f);
                rotateAnimation.setDuration(200);
                animationSet2.addAnimation(rotateAnimation);
                iv.startAnimation(animationSet2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animationSet;
    }

    @Override
    public void filtrateSelected(String str) {
        if ("".equals(str)) {
            return;
        }
        HomeFilterParams params = new HomeFilterParams();
        String[] s = str.split("");
        for (int i = 0; i < str.length() + 1; i++) {
            switch (s[i]) {
                case 0 + "":
                    params.setGender("1");//性别筛选的参数0不限 1男2女
                    break;
                case 1 + "":
                    params.setGender("2");
                    break;
                case 2 + "":
                    params.setGender("0");
                    break;
                case 3 + "":
                    params.setOrderby("time"); //排序方式
                    break;
                case 4 + "":
                    params.setOrderby("distance");
                    break;
                case 5 + "":
                    params.setOrderby("price");
                    break;
                case 6 + "":
                    params.setShowVip("1"); //1,只看会员 , 0,不限
                    break;
            }
        }
        EventBus.getDefault().post(params);
    }

    public void setChangeText(int num) {
        mMain11.setTextColor(getResources().getColor(R.color.White_aaa8a8));
        mMain12.setImageResource(R.drawable.gray_circle);
        mMain13.setTextColor(getResources().getColor(R.color.White_aaa8a8));
        mMain21.setTextColor(getResources().getColor(R.color.White_aaa8a8));
        mMain22.setImageResource(R.drawable.gray_circle);
        mMain23.setTextColor(getResources().getColor(R.color.White_aaa8a8));
        mMain31.setTextColor(getResources().getColor(R.color.White_aaa8a8));
        mMain32.setImageResource(R.drawable.gray_circle);
        mMain33.setTextColor(getResources().getColor(R.color.White_aaa8a8));
        mMain41.setTextColor(getResources().getColor(R.color.White_aaa8a8));
        mMain42.setImageResource(R.drawable.gray_circle);
        mMain43.setTextColor(getResources().getColor(R.color.White_aaa8a8));
        mMain51.setTextColor(getResources().getColor(R.color.White_aaa8a8));
        mMain52.setImageResource(R.drawable.gray_circle);
        mMain53.setTextColor(getResources().getColor(R.color.White_aaa8a8));
        mMain61.setTextColor(getResources().getColor(R.color.White_aaa8a8));
        mMain62.setImageResource(R.drawable.gray_circle);
        mMain63.setTextColor(getResources().getColor(R.color.White_aaa8a8));

        switch (num) {
            case 1:
                mMain11.setTextColor(getResources().getColor(R.color.Whiteffffff));
                mMain12.setImageResource(R.drawable.white_circle);
                mMain13.setTextColor(getResources().getColor(R.color.Whiteffffff));
                mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH_NOTE_COLOR, TIME_REFRESH_NOTE_COLOR);
                break;
            case 2:
                mMain21.setTextColor(getResources().getColor(R.color.Whiteffffff));
                mMain22.setImageResource(R.drawable.white_circle);
                mMain23.setTextColor(getResources().getColor(R.color.Whiteffffff));
                mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH_NOTE_COLOR, TIME_REFRESH_NOTE_COLOR);
                break;
            case 3:
                mMain31.setTextColor(getResources().getColor(R.color.Whiteffffff));
                mMain32.setImageResource(R.drawable.white_circle);
                mMain33.setTextColor(getResources().getColor(R.color.Whiteffffff));
                mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH_NOTE_COLOR, TIME_REFRESH_NOTE_COLOR);
                break;
            case 4:
                mMain41.setTextColor(getResources().getColor(R.color.Whiteffffff));
                mMain42.setImageResource(R.drawable.white_circle);
                mMain43.setTextColor(getResources().getColor(R.color.Whiteffffff));
                mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH_NOTE_COLOR, TIME_REFRESH_NOTE_COLOR);
                break;
            case 5:
                mMain51.setTextColor(getResources().getColor(R.color.Whiteffffff));
                mMain52.setImageResource(R.drawable.white_circle);
                mMain53.setTextColor(getResources().getColor(R.color.Whiteffffff));
                mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH_NOTE_COLOR, TIME_REFRESH_NOTE_COLOR);
                break;
            case 6:
                mMain61.setTextColor(getResources().getColor(R.color.Whiteffffff));
                mMain62.setImageResource(R.drawable.white_circle);
                mMain63.setTextColor(getResources().getColor(R.color.Whiteffffff));
                mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH_NOTE_COLOR, TIME_REFRESH_NOTE_COLOR);
            default:
                break;
        }
    }

    public void showGuideView() {

        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(ivHomeMenuFilter)
                .setAlpha(220)
                .setHighTargetCorner(2)
                .setHighTargetPadding(2)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {

            }

            @Override
            public void onDismiss() {
                showGuideView2();
            }
        });

        builder.addComponent(new SimpleComponent());
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show(this);
    }

    public void showGuideView2() {
        tvHomeInvite.setTextColor(getResources().getColor(R.color.Whitec2c2c2));
        tvHomeWho.setTextColor(getResources().getColor(R.color.Whiteffffff));
        final GuideBuilder builder1 = new GuideBuilder();
        builder1.setTargetView(tvHomeWho)
                .setAlpha(220)
                .setHighTargetCorner(2)
                .setHighTargetPadding(2)
                .setOverlayTarget(false)
                .setExitAnimationId(android.R.anim.fade_out)
                .setOutsideTouchable(false);
        builder1.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
                tvHomeWho.setTextColor(getResources().getColor(R.color.Whitec2c2c2));
                tvHomeInvite.setTextColor(getResources().getColor(R.color.Whitec2c2c2select));

                getActiveDialog();
            }
        });

        builder1.addComponent(new MutiComponent());
        Guide guide = builder1.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_SHOW_DIALOG
                && response instanceof ActiveMainShowDialog) {
            ActiveMainShowDialog activeMainShowDialog = (ActiveMainShowDialog) response;
            if (activeMainShowDialog.getCode() == 1) {
                ActiveMainShowDialog.DataBean data = activeMainShowDialog.getData();
                if (data.getIsdialog() == 1) {
                    disPathDialogData(data.getDialog());
                }
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_MENU_LIST
                && response instanceof MainMenuResponse) {
            MainMenuResponse mainMenuResponse = (MainMenuResponse) response;
            if (mainMenuResponse.getCode() == 1) {
                MainMenuResponse.DataBean data = mainMenuResponse.getData();
                if (data != null && data.getList() != null && data.getList().size() != 0) {
                    MainMenuResponse.DataBean.ListBean listBean = data.getList().get(0);
                    if (!TextUtils.isEmpty(listBean.getTitle()) && !TextUtils.isEmpty(listBean.getCtitle())) {
                        llMyActive.setVisibility(View.VISIBLE);
                        mViewShowLine.setVisibility(View.VISIBLE);
                        mMain61.setText(listBean.getTitle());
                        mMain63.setText(listBean.getCtitle());
                    } else {
                        hideActive();
                    }
                } else {
                    hideActive();
                }
            } else {
                hideActive();
            }
        }
    }

    private void hideActive() {
        llMyActive.setVisibility(View.GONE);
        mViewShowLine.setVisibility(View.GONE);
    }

    private void disPathDialogData(ActiveMainShowDialog.DataBean.DialogBean infos) {
        switch (infos.getType()) {
            case ActiveSPConfig.SHOW_DIALOG_ONCE:
                if (!ActiveSPConfig.getVersion().equals(infos.getVersion())) {
                    ActiveShowDialog activeShowDialog = new ActiveShowDialog(this, infos);
                    activeShowDialog.show();
                    ActiveSPConfig.updateVersion(infos.getVersion());
                }
                break;
            case ActiveSPConfig.SHOW_DIALOG_EVERYDAY:
                if (!ActiveSPConfig.getVersion().equals(infos.getVersion()) || !ActiveSPConfig.isTodayAlreadyShow()) {
                    ActiveShowDialog activeShowDialog = new ActiveShowDialog(this, infos);
                    activeShowDialog.show();
                    ActiveSPConfig.updateVersionAndShowday(infos.getVersion());
                }
                break;
            case ActiveSPConfig.SHOW_DIALOG_EVERYTIME:
                ActiveShowDialog activeShowDialog = new ActiveShowDialog(this, infos);
                activeShowDialog.show();
                break;
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_SHOW_DIALOG) {
            ALog.i("获取失败");
        } else if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_MENU_LIST) {
            hideActive();
        }
    }
}

