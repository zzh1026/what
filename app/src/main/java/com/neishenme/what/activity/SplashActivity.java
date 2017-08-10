package com.neishenme.what.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
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
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.APKDownloader;
import com.neishenme.what.utils.AppManager;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.MPermissionManager;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.PackageVersion;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

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
 * 这是一个 欢迎界面 splash , 主要功能是  显示欢迎和 广告界面,原来的广告界面已经删除了
 * .
 * 其作用是 :
 */
public class SplashActivity extends Activity implements HttpLoader.ResponseListener {
    private static final String FLAG_TODAY = "today";
    private static final String FLAG_EVERY_DAY = "everyday";

    private String mToday;       //获取今天的日期
    private String mSpSaveToday;    //获取新的日期
    private String mEveryday;  //获取每一天的日期
    private String localversion;    //本地的version

    private ImageView mSplashLogoNsm;   //背景图
    private TextView mAdJump;           //调过广告

    private String message;     //需要展示的

    public final int MSG_FINISH_LAUNCHERACTIVITY = 500;
    public final int MSG_SHOW_AD = 600;

    private AdResponse adResponse = null;   //广告的结果

    private AlertDialog mUpdateDialog;          //更新dialog
    private AlertDialog mPermissionSuggestPermission;   //权限提示dialog
    private Dialog mPermissionDialog;   //权限警告弹窗

    private boolean isShowAd;   //是否展示ad广告页面

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MSG_SHOW_AD) {
                if (isShowAd) {
                    waitADImg();
                    showAD();
                } else {
                    mHandler.sendEmptyMessage(MSG_FINISH_LAUNCHERACTIVITY);
                }
                mHandler.removeMessages(MSG_SHOW_AD);
            } else {
                if (mUpdateDialog != null && mUpdateDialog.isShowing()) {
                    return;
                }
                if (mPermissionSuggestPermission != null && mPermissionSuggestPermission.isShowing()) {
                    return;
                }
                if (mPermissionDialog != null && mPermissionDialog.isShowing()) {
                    return;
                }
                mHandler.removeMessages(MSG_FINISH_LAUNCHERACTIVITY);
                toHomeActi();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        initView();
        initListener();

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

        //获取用户的定位信息
        CityLocationConfig.getLocationUtil();

        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (AndPermission.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                && AndPermission.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && AndPermission.hasPermission(this, Manifest.permission.READ_PHONE_STATE)) {
            startSplash();
        } else {
            finish();
        }
    }

    private void startSplash() {
        waitSplashImg();
        //获取网络版本
        getNetVorsion();
    }

    @Override
    protected void onStop() {
        super.onStop();
        HttpLoader.cancelRequest(this);
        mHandler.removeMessages(MSG_FINISH_LAUNCHERACTIVITY);
        mHandler.removeMessages(MSG_SHOW_AD);
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
                    startSplash();
                    break;
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> grantedPermissions) {
            switch (requestCode) {
                case MPermissionManager.REQUEST_CODE_LOCATION:
                    mPermissionDialog = MPermissionManager.showToSetting(SplashActivity.this,
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
            // 用户同意继续申请。
            // 用户拒绝申请。
            mPermissionSuggestPermission = new AlertDialog.Builder(SplashActivity.this)
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
        mAdJump = (TextView) findViewById(R.id.splash_ad_jump);
    }

    public void initListener() {
        mAdJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(MSG_FINISH_LAUNCHERACTIVITY);
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
                    SplashActivity.this.startActivity(it);
                }
            }
        });
    }

    private void initData() {
        localversion = PackageVersion.getPackageVersion(this);

        if (!NSMTypeUtils.isLogin()) {
            if (!JPushInterface.isPushStopped(App.getApplication())) {
                JPushInterface.stopPush(App.getApplication());
            }
        }

        mToday = String.valueOf(DateUtils.formatDay(System.currentTimeMillis()));

        mSpSaveToday = App.SP.getString(FLAG_TODAY, "");    //获取保存的表示今天的日期值

        //标记今天, 把
        mEveryday = App.SP.getString(FLAG_EVERY_DAY, "");
        if (TextUtils.isEmpty(mEveryday) || !mEveryday.equals(mToday)) {
            App.EDIT.putString(FLAG_EVERY_DAY, mToday).commit();
        }

        if (AndPermission.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                && AndPermission.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && AndPermission.hasPermission(this, Manifest.permission.READ_PHONE_STATE)) {
            startSplash();
        } else {
            requestLocationPermission();
        }
    }

    private void getNetVorsion() {
        HashMap<String, String> params = new HashMap<>();
        params.put("localVersion", localversion);
        params.put("channel", AppManager.getAppMetaData(this));
        HttpLoader.get(ConstantsWhatNSM.URL_UPDATE_VERSION, params, UpdateVersionResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_UPDATE_VERSION, this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_UPDATE_VERSION
                && response instanceof UpdateVersionResponse) {
            UpdateVersionResponse updateVersionResponse = (UpdateVersionResponse) response;
            int code = updateVersionResponse.getCode();
            if (1 == code) {
                String version = updateVersionResponse.getData().getVersion().getAndroidVersion();
                isShowAd = updateVersionResponse.getData().getVersion().getAndroidad() == 1;
                if (!TextUtils.isEmpty(version)) {
                    String appMetaVersion = "v" + AppManager.getAppMetaVersion(this);
                    if (version.equals(localversion) || version.equals(appMetaVersion)) {
                        if (isFirstEnter(version)) {
                            ActivityUtils.startActivityAndFinish(this, GuideImageActivity.class);
                        }
                    } else {
                        App.EDIT.putBoolean(AppSharePreConfig.IS_FIRST_ENTER, true).commit();
                        if (TextUtils.isEmpty(mSpSaveToday) || !mSpSaveToday.equals(mToday)) {
                            showUpdateDialog(updateVersionResponse.getData().getVersion().getAndroidUrl());
                        }
                    }
                }
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_AD
                && response instanceof AdResponse) {
            mAdJump.setVisibility(View.VISIBLE);
            adResponse = (AdResponse) response;
            if (1 == adResponse.getCode()) {
                String imageUrl = adResponse.getData().getImage();
                if (!TextUtils.isEmpty(imageUrl)) {
                    HttpLoader.getImageLoader().get(imageUrl,
                            ImageLoader.getImageListener(mSplashLogoNsm, R.drawable.splash_nsm, R.drawable.splash_nsm));
                }
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
    }

    public void showAD() {
        HttpLoader.get(ConstantsWhatNSM.URL_AD, null, AdResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_AD, this, false).setTag(this);
    }

    private void waitSplashImg() {
        mHandler.sendEmptyMessageDelayed(MSG_SHOW_AD, 1500);
    }

    private void waitADImg() {
        mHandler.sendEmptyMessageDelayed(MSG_FINISH_LAUNCHERACTIVITY, 2300);
    }

    private boolean isFirstEnter(String version) {
        return App.SP.getBoolean(AppSharePreConfig.IS_FIRST_ENTER, true) && version.equals(localversion);
    }

    private void showUpdateDialog(final String url) {
        mUpdateDialog = new AlertDialog.Builder(SplashActivity.this)
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
                        mHandler.sendEmptyMessageDelayed(MSG_FINISH_LAUNCHERACTIVITY, 1000);
                    }
                })
                .setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.EDIT.putString(FLAG_TODAY, mToday).commit();
                        mHandler.sendEmptyMessageDelayed(MSG_FINISH_LAUNCHERACTIVITY, 1000);
                    }
                }).show();
    }

    /**
     * 直接跳转进行后续处理的方法
     */
    private void toHomeActi() {
        if (NSMTypeUtils.isLogin()) {
            ActivityUtils.startActivityAndFinish(this, MainActivity.class);
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
