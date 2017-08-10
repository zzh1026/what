package com.neishenme.what.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.neishenme.what.R;
import com.neishenme.what.application.App;
import com.neishenme.what.common.AppSharePreConfig;
import com.neishenme.what.utils.UpdataPersonInfo;

import org.seny.android.utils.ActivityUtils;

/**
 * 作者：zhaozh create on 2016/3/11 18:10
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个  老的登录指引界面,,已弃用,不用了,唉.....
 * .
 * 其作用是 :
 */
@Deprecated
public class GuideStartActivity extends Activity {
    //声明进入主界面控件
    private Button enterHome;
    private Button mBtGuideEnterLogin;
    private Button mBtGuideEnterRegest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawable(null);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_guide_start);

        enterHome = (Button) findViewById(R.id.bt_guide_enter_home);
        enterHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivityAndFinish(GuideStartActivity.this, MainActivity.class);
            }
        });


        mBtGuideEnterLogin = (Button) findViewById(R.id.bt_guide_enter_login);
        mBtGuideEnterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.addActivity(GuideStartActivity.this);
                ActivityUtils.startActivity(GuideStartActivity.this, LoginActivity.class);
            }
        });


        mBtGuideEnterRegest = (Button) findViewById(R.id.bt_guide_enter_regest);
        mBtGuideEnterRegest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.addActivity(GuideStartActivity.this);
                ActivityUtils.startActivity(GuideStartActivity.this, RegestActivity.class);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //被T;
        if (App.SP.getBoolean(AppSharePreConfig.USER_LOGIN_BE_T, false)) {
            showLoginBeT();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
}
