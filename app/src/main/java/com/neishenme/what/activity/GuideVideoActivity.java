package com.neishenme.what.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.VideoView;

import com.neishenme.what.R;
import com.neishenme.what.application.App;
import com.neishenme.what.common.AppSharePreConfig;
import com.neishenme.what.utils.UpdataPersonInfo;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;

/**
 * 作者：zhaozh create on 2016/5/11 12:46
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :   欢迎界面更改 该视频欢迎界面需要留下,但是应该不会用
 */
public class GuideVideoActivity extends Activity implements View.OnClickListener {
    private final int RG_0 = 0;
    private final int RG_1 = 1;
    private final int RG_2 = 2;
    private final int RG_3 = 3;

    private VideoView vv;
    private Button mBtnEnterRegest;
    private Button mBtnEnterLogin;
    private Button mBtnEnterHome;
    private String stringPath;
    private RadioGroup mGuideVideoRg;
    private int videoDuration;  //视频间隔时长

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RG_0:
                    mGuideVideoRg.check(R.id.rb_point_0);
                    mHandler.sendEmptyMessageDelayed(RG_1, videoDuration);
                    break;
                case RG_1:
                    mGuideVideoRg.check(R.id.rb_point_1);
                    mHandler.sendEmptyMessageDelayed(RG_2, videoDuration);
                    break;
                case RG_2:
                    mGuideVideoRg.check(R.id.rb_point_2);
                    mHandler.sendEmptyMessageDelayed(RG_3, videoDuration);
                    break;
                case RG_3:
                    mGuideVideoRg.check(R.id.rb_point_3);
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawable(null);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_guide_video);

        mBtnEnterRegest = (Button) findViewById(R.id.btn_enter_regest);
        mBtnEnterLogin = (Button) findViewById(R.id.btn_enter_login);
        mBtnEnterHome = (Button) findViewById(R.id.btn_enter_home);
        mGuideVideoRg = (RadioGroup) findViewById(R.id.guide_video_rg);


        mBtnEnterRegest.setOnClickListener(this);
        mBtnEnterLogin.setOnClickListener(this);
        mBtnEnterHome.setOnClickListener(this);

        stringPath = getIntent().getStringExtra("data");
        ALog.i("网络地址为:" + stringPath);
    }

    @Override
    protected void onStart() {
        super.onStart();
        vv = (VideoView) this.findViewById(R.id.videoView);
//        String uri = "android.resource://" + getPackageName() + "/" + R.raw.play;
        vv.setVideoURI(Uri.parse(stringPath));
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
                removeAllMessage();
                mHandler.sendEmptyMessage(RG_0);
            }
        });
        vv.start();
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoDuration = vv.getDuration() / 4;
                removeAllMessage();
                mHandler.sendEmptyMessage(RG_0);
            }
        });

        //被T;
        if (App.SP.getBoolean(AppSharePreConfig.USER_LOGIN_BE_T, false)) {
            showLoginBeT();
        }
    }

    private void removeAllMessage() {
        mHandler.removeMessages(RG_1);
        mHandler.removeMessages(RG_2);
        mHandler.removeMessages(RG_3);
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
                        return;
                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        if (null != vv) {
            vv = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //点击注册
            case R.id.btn_enter_regest:
                toRegestAct();
                break;
            //点击登录
            case R.id.btn_enter_login:
                toLoginAct();
                break;
            //点击逛逛
            case R.id.btn_enter_home:
                toHomeAct();
                break;
        }
    }

    private void toRegestAct() {
        App.addActivity(GuideVideoActivity.this);
        ActivityUtils.startActivity(this, RegestActivity.class);
    }

    private void toHomeAct() {
        ActivityUtils.startActivityAndFinish(this, MainActivity.class);
    }

    private void toLoginAct() {
        App.addActivity(GuideVideoActivity.this);
        ActivityUtils.startActivity(this, LoginActivity.class);
    }
}
