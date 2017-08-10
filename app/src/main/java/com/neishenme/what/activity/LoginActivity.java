package com.neishenme.what.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.neishenme.what.R;
import com.neishenme.what.application.App;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.HomeFilterParams;
import com.neishenme.what.fragment.LoginNormalFragment;
import com.neishenme.what.fragment.LoginQuickFragment;
import com.neishenme.what.receiver.SMSBroadcastReceiver;

import org.seny.android.utils.ActivityUtils;

import de.greenrobot.event.EventBus;

/**
 * 作者：zhaozh create on 2016/5/11 15:31
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class LoginActivity extends BaseActivity {
    private ImageView mIvActionbarLeft;
    private TextView mTvActionbarMiddle;
    private RadioGroup mRgMyLogin;
    private RadioButton mRbLoginQuick;
    private RadioButton mRbLoginNormal;
    private LinearLayout mUnderLineContainer;
    private FrameLayout mFragmentContainer;

    private int width;
    private int height;
    private RadioButton[] RadioButtonArray = new RadioButton[2];

    private FragmentTransaction transaction;
    private LoginQuickFragment mLoginQuickFragment;
    private LoginNormalFragment mLoginNormalFragment;

    private int fromX = 0;
    private View view = null;


    @Override
    protected int initContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mIvActionbarLeft = (ImageView) findViewById(R.id.iv_actionbar_left);
        mTvActionbarMiddle = (TextView) findViewById(R.id.tv_actionbar_middle);

        mRgMyLogin = (RadioGroup) findViewById(R.id.rg_my_login);
        mRbLoginQuick = (RadioButton) findViewById(R.id.rb_login_quick);
        mRbLoginNormal = (RadioButton) findViewById(R.id.rb_login_normal);

        mUnderLineContainer = (LinearLayout) findViewById(R.id.underLine_container);
        mFragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);


        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
        width = outMetrics.widthPixels;
        height = outMetrics.heightPixels;
    }

    @Override
    protected void initListener() {
        mIvActionbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRgMyLogin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                transaction = getFragmentManager().beginTransaction();
                hideFragment(transaction);
                switch (checkedId) {
                    case R.id.rb_login_quick:
                        if (null != mLoginQuickFragment && null != mLoginNormalFragment) {
                            transaction.show(mLoginQuickFragment).hide(mLoginNormalFragment).commit();
                        }
                        startUnderlineAnimation(0);
                        RadioButtonArray[0].setTextColor(getResources().getColor(R.color._343434));
                        RadioButtonArray[1].setTextColor(getResources().getColor(R.color._8f8f8f));
                        break;

                    case R.id.rb_login_normal:
                        if (null != mLoginNormalFragment) {
                            transaction.show(mLoginNormalFragment).hide(mLoginQuickFragment).commit();
                        }
                        startUnderlineAnimation(1);
                        RadioButtonArray[0].setTextColor(getResources().getColor(R.color._8f8f8f));
                        RadioButtonArray[1].setTextColor(getResources().getColor(R.color._343434));
                        break;
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void initData() {
        mTvActionbarMiddle.setText("登录");
        for (int i = 0; i < mRgMyLogin.getChildCount(); i++) {
            RadioButtonArray[i] = (RadioButton) mRgMyLogin.getChildAt(i);
        }

        view = new View(this);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width / 2, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(param);
        view.setBackgroundColor(Color.BLACK);
        mUnderLineContainer.addView(view);

        transaction = getFragmentManager().beginTransaction();
        if (null == mLoginQuickFragment) {
            mLoginQuickFragment = LoginQuickFragment.newInstance();
        }
        if (null == mLoginNormalFragment) {
            mLoginNormalFragment = LoginNormalFragment.newInstance();
        }
        transaction.add(R.id.fragment_container, mLoginQuickFragment).add(R.id.fragment_container, mLoginNormalFragment)
                .show(mLoginQuickFragment).hide(mLoginNormalFragment).commit();
    }

    public void hideFragment(FragmentTransaction ftransaction) {
        if (mLoginQuickFragment != null) {
            ftransaction.hide(mLoginQuickFragment);
        }
        if (mLoginNormalFragment != null) {
            ftransaction.hide(mLoginNormalFragment);
        }
    }


    private void startUnderlineAnimation(int j) {
        j = width / 2 * j;
        TranslateAnimation animation = new TranslateAnimation(fromX, j, 0, 0);
        animation.setDuration(200);
        animation.setFillAfter(true);
        fromX = j;
        view.startAnimation(animation);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    public void toRegestUserInfo(String token) {
        //跳转注册界面2
        ActivityUtils.startActivityForDataAndFinish(this, RegestUserInfoActivity.class, token);
    }

    public void toRegestAct() {
        //跳转到注册界面;
        App.addActivity(this);
        ActivityUtils.startActivity(this, RegestActivity.class);
    }

    public void toResetPwdAct() {
        //跳转到忘记密码界面;
        ActivityUtils.startActivity(this, ResetPwdActivity.class);
    }

    public void toMainAct() {
        //跳转到主界面
        EventBus.getDefault().post(new HomeFilterParams());
        ActivityUtils.startActivityAndFinish(this, MainActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSBroadcastReceiver.unRegisterSms(this);
    }

    public void wrong() {
        showToastError("网络连接失败");
        finish();
    }

}
