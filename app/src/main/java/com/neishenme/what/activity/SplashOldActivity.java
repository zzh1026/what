package com.neishenme.what.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.application.App;
import com.neishenme.what.bean.AdResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.TradeSuccessResponse;
import com.neishenme.what.bean.UpdateVersionResponse;
import com.neishenme.what.common.AppSharePreConfig;
import com.neishenme.what.common.CityLocationConfig;
import com.neishenme.what.db.HomeNewsOpenHelper;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.APKDownloader;
import com.neishenme.what.utils.AppManager;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.HuanXinUtils;
import com.neishenme.what.utils.MPermissionManager;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.PackageVersion;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.DateUtils;

import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者：zhaozh create on 2016/6/15 17:47
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 旧的欢迎界面 ,现在需要重新重构一下了
 * .
 * 其作用是 :
 */
@Deprecated
public class SplashOldActivity extends Activity implements HttpLoader.ResponseListener {
    private long startTime;
    private String today;
    private String newToday;
    private String everyToday;
    private String localversion;

    private ImageView mSplashLogoNsm;
    private TextView mAdJump;

    private String message;
    private boolean isShowVideo = false;

    public final int MSG_FINISH_LAUNCHERACTIVITY = 500;
    private AdResponse adResponse = null;
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            toHomeActi();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawable(null);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_splash);

        initView();

        initListener();

        //初始化数据
        initData();

        if (App.SP.getBoolean(AppSharePreConfig.IS_FIRST_ENTER_APP, true)) {
            HashMap<String, String> params = new HashMap<>();
            params.put("channel", AppManager.getAppMetaData(this));
            params.put("version", localversion);
            params.put("identifier", AppManager.getIMEI());
            params.put("modelNumber", android.os.Build.MODEL);
            //获取服务器版本
            HttpLoader.post(ConstantsWhatNSM.URL_DOWN_QUDAO, params, TradeSuccessResponse.class,
                    ConstantsWhatNSM.REQUEST_CODE_DOWN_QUDAO, this, false).setTag(this);
            App.EDIT.putBoolean(AppSharePreConfig.IS_FIRST_ENTER_APP, false).commit();
        }

        loginToHuanXin();

        //获取用户的定位信息
        CityLocationConfig.getLocationUtil();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (AndPermission.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                && AndPermission.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && AndPermission.hasPermission(this, Manifest.permission.READ_PHONE_STATE)) {
            startSplash();
        } else {
            requestLocationPermission();
        }
    }

    private void startSplash() {
        startTime = System.currentTimeMillis();

        //获取网络版本
        getNetVorsion();
    }

    @Override
    protected void onDestroy() {
        HttpLoader.cancelRequest(this);
        super.onDestroy();
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
                    ALog.i("获取位置权限成功");
                    startSplash();
                    break;
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> grantedPermissions) {
            switch (requestCode) {
                case MPermissionManager.REQUEST_CODE_LOCATION:
                    MPermissionManager.showToSetting(SplashOldActivity.this,
                            MPermissionManager.REQUEST_CODE_LOCATION, new MPermissionManager.OnNegativeListner() {
                                @Override
                                public void onNegative() {
                                    finish();
                                }
                            });
                    break;
            }
        }
    };

    private void requestLocationPermission() {
        AndPermission.with(this)
                .requestCode(MPermissionManager.REQUEST_CODE_LOCATION)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.READ_PHONE_STATE
                )
                .rationale(mRationaleListener)
                .send();
    }

    private RationaleListener mRationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            switch (requestCode) {
                case MPermissionManager.REQUEST_CODE_LOCATION:
                    message = MPermissionManager.REQUEST_LOCATION_DISCRIBE;
                    break;
            }
            new AlertDialog.Builder(SplashOldActivity.this)
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

    public void initView() {
        mSplashLogoNsm = (ImageView) findViewById(R.id.splash_logo_nsm);
        mAdJump = (TextView) findViewById(R.id.ad_jump);
    }

    public void initListener() {
        mAdJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toHomeActi();
            }
        });
        mSplashLogoNsm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != adResponse && !TextUtils.isEmpty(adResponse.getData().getLink())) {
                    Uri uri;
                    if (NSMTypeUtils.isLogin()) {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("token", NSMTypeUtils.getMyToken());
                        String link = adResponse.getData().getLink();
                        uri = Uri.parse(link + HttpLoader.buildGetParam(params, link));
                    } else {
                        uri = Uri.parse(adResponse.getData().getLink());
                    }
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    SplashOldActivity.this.startActivity(it);
                }
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

    private void initData() {
        localversion = PackageVersion.getPackageVersion(this);

        today = String.valueOf(DateUtils.formatDay(System.currentTimeMillis()));
        newToday = App.SP.getString("today", "");
        everyToday = App.SP.getString("everytoday", "");
        if (TextUtils.isEmpty(everyToday) || !everyToday.equals(today)) {
            deleteDatabase(HomeNewsOpenHelper.CREATE_TABLE_NAME);
            App.EDIT.putString("everytoday", today).commit();
        }
    }

    private void getNetVorsion() {
        //准备请求参数
        HashMap<String, String> params = new HashMap<>();
        params.put("localVersion", localversion);
        params.put("channel", AppManager.getAppMetaData(this));

        //获取服务器版本
        HttpLoader.get(ConstantsWhatNSM.URL_UPDATE_VERSION, params, UpdateVersionResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_UPDATE_VERSION, this, false).setTag(this);
    }

    private void getLocation() {
//        LocationUtils.getLocationByGCJ_02(new LocationUtils.OnGetLocationListener() {
//            @Override
//            public void onGetLocation(double latitude, double longgitude, BDLocation location) {
//                requestLocation(location.getProvince(), location.getCity(), latitude, longgitude);
//            }
//
//            @Override
//            public void onGetError() {
//                requestLocation("", "", 0, 0);
//            }
//        });
    }

    private void requestLocation(String province, String citydouble, double latitude, double longgitude) {
//        HashMap<String, String> params = new HashMap<>();
//        params.put("province", province);
//        params.put("city", citydouble);
//        params.put("latitude", String.valueOf(latitude));
//        params.put("longitude", String.valueOf(longgitude));
//        String city = citydouble.endsWith("市") ? citydouble.substring(0, citydouble.length() - 1) : citydouble;
//        CityLocationConfig.cityLocation = city;
//        CityLocationConfig.citySelected = city;
//        CityLocationConfig.cityLatitude = latitude;
//        CityLocationConfig.cityLonggitude = longgitude;
//        HttpLoader.get(ConstantsWhatNSM.URL_ACTIVE_GET_CITY_LOCATION, params, CityLocationResponse.class,
//                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_GET_CITY_LOCAGION, SplashActivity.this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_UPDATE_VERSION
                && response instanceof UpdateVersionResponse) {
            UpdateVersionResponse updateVersionResponse = (UpdateVersionResponse) response;
            int code = updateVersionResponse.getCode();
            if (1 == code) {
                String version = updateVersionResponse.getData().getVersion().getAndroidVersion();
                int androidAd = updateVersionResponse.getData().getVersion().getAndroidad();
                if (!TextUtils.isEmpty(version)) {
                    String appMetaVersion = "v" + AppManager.getAppMetaVersion(this);
                    if (version.equals(localversion) || version.equals(appMetaVersion)) {
                        waitTime();
                        //2 , 如果是 1 ,则判断有没有广告
//                        if (isFirstEnter(version)) {
//                            ShowGuideConfig.shouldShownAll();
//                        }

                        if (isFirstEnter(version)) {
                            ActivityUtils.startActivityAndFinish(this, GuideImageActivity.class);
                        } else {
                            if (androidAd == 1) {
                                showAD();
                            } else {
                                toHomeActi();
                            }
                        }
//                        addAnim();
                    } else {
                        App.EDIT.putBoolean(AppSharePreConfig.IS_FIRST_ENTER, true).commit();
                        if (!TextUtils.isEmpty(newToday) && newToday.equals(today)) {
                            waitTime();
                            // 3 , 如果为1则去广告页面;
                            if (androidAd == 1) {
                                showAD();
                            } else {
                                toHomeActi();
                            }
//                            addAnim();
                        } else {
                            showUpdateDialog(updateVersionResponse.getData().getVersion().getAndroidUrl());
                        }
                    }
                } else {
                    waitTime();
                    ActivityUtils.startActivityAndFinish(this, MainActivity.class);
                }
            } else {
                waitTime();
                ActivityUtils.startActivityAndFinish(this, MainActivity.class);
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_AD
                && response instanceof AdResponse) {
            mAdJump.setVisibility(View.VISIBLE);
            adResponse = (AdResponse) response;
            if (1 == adResponse.getCode()) {
                isShowVideo = adResponse.getData().getGuide().getType().equals("1");

                if (!TextUtils.isEmpty(adResponse.getData().getImage())) {
                    HttpLoader.getImageLoader().get(adResponse.getData().getImage(),
                            ImageLoader.getImageListener(mSplashLogoNsm, R.drawable.splash_nsm, R.drawable.splash_nsm));
                    mSplashLogoNsm.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                long endTime = System.currentTimeMillis();
                long dTime = endTime - startTime;

                if (dTime < 3000) {
                    mHandler.sendEmptyMessageDelayed(MSG_FINISH_LAUNCHERACTIVITY, 3000 - dTime);
                } else {
                    toHomeActi();
                }
            } else {
                waitTimes();
            }
        }

//        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_GET_CITY_LOCAGION
//                && response instanceof CityLocationResponse) {
//            CityLocationResponse cityLocationResponse = (CityLocationResponse) response;
//            if (1 == cityLocationResponse.getCode()) {
//                CityLocationResponse.DataBean.ResultdataBean resultdataBean =
//                        cityLocationResponse.getData().getResultdata().get(0);
//                CityLocationConfig.cityLocation = resultdataBean.getName();
//                CityLocationConfig.citySelected = resultdataBean.getName();
//                CityLocationConfig.cityLocationId = resultdataBean.getAreaId();
//                CityLocationConfig.citySelectedId = resultdataBean.getAreaId();
//            }
//        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        waitTime();
        mAdJump.setVisibility(View.VISIBLE);
        ActivityUtils.startActivityAndFinish(this, MainActivity.class);
    }

    public void showAD() {
        startTime = System.currentTimeMillis();
        HttpLoader.get(ConstantsWhatNSM.URL_AD, null, AdResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_AD, this, false).setTag(this);

        waitTimes();
    }

    private void waitTimes() {
        long endTime = System.currentTimeMillis();
        long dTime = endTime - startTime;
        if (dTime < 3000) {
            mHandler.sendEmptyMessageDelayed(MSG_FINISH_LAUNCHERACTIVITY, 3000 - dTime);
        } else {
            toHomeActi();
        }
    }

    private void waitTime() {
        long endTime = System.currentTimeMillis();
        long dTime = endTime - startTime;
        if (dTime < 1500) {
            SystemClock.sleep(2000 - dTime);
        }
    }

    private void addAnim() {
        overridePendingTransition(R.anim.page_showad_in, R.anim.page_showad_out);
    }

    private boolean isFirstEnter(String version) {
        return App.SP.getBoolean(AppSharePreConfig.IS_FIRST_ENTER, true) && version.equals(localversion);
    }

    private void showUpdateDialog(final String url) {
        new AlertDialog.Builder(SplashOldActivity.this)
                .setCancelable(false)
                .setTitle("更新提示")
                .setMessage("发现新版本，是否更新?")
                .setPositiveButton("马上更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread() {
                            @Override
                            public void run() {
                                APKDownloader.getInstance().downloadAPK(url, "内什么.apk");
                            }
                        }.start();
                        ActivityUtils.startActivityAndFinish(SplashOldActivity.this,
                                MainActivity.class);
                    }
                })
                .setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.EDIT.putString("today", today).commit();
                        ActivityUtils.startActivityAndFinish(SplashOldActivity.this,
                                MainActivity.class);
                    }
                }).show();
    }

    private void toHomeActi() {
        if (NSMTypeUtils.isLogin()) {
            ActivityUtils.startActivityAndFinish(this, MainActivity.class);
//            ActivityUtils.startActivityAndFinish(this, GuideImageActivity.class);
        } else {
            if (adResponse != null && adResponse.getData() != null) {
                AdResponse.DataBean.GuideBean guideBean = adResponse.getData().getGuide();
                if (guideBean.getType().equals("1") && !TextUtils.isEmpty(guideBean.getVedio())) {
                    ActivityUtils.startActivityForDataAndFinish(this, GuideVideoActivity.class, adResponse.getData().getGuide().getVedio());
                } else {
                    ActivityUtils.startActivityAndFinish(this, MainActivity.class);
                }
            } else {
                ActivityUtils.startActivityAndFinish(this, MainActivity.class);
            }
        }

    }
}
