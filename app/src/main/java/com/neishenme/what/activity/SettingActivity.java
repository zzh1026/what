package com.neishenme.what.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.VolleyError;
import com.hyphenate.easeui.EaseConstant;
import com.neishenme.what.R;
import com.neishenme.what.application.App;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.NsmServerInfoResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.eventbusobj.ChatInfoBean;
import com.neishenme.what.huanxinchat.ui.ChatActivity;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.PackageVersion;

import org.seny.android.utils.ActivityUtils;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * 作者：zhaozh create on 2016/6/2 15:38
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 设置界面 ,由 主界面菜单栏点击设置进入, 主要功能是修改支付密码等功能
 * .
 * 其作用是 :
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener {
    private static final String ABOUT_US_URL = "http://www.neishenme.com/about/about.html";

    private ImageView mIvBack;
    private RelativeLayout mSettingInformationAlarm;
    private ToggleButton mTbInformationAlarm;
    private RelativeLayout mRlContactUs;
    private RelativeLayout mRlAboutUs;
    private RelativeLayout mRlResetPassword;
    private TextView mSettingVersion;
    private NsmServerInfoResponse.DataBean.NsmBean nsmServerInfo;

    @Override
    protected int initContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mSettingInformationAlarm = (RelativeLayout) findViewById(R.id.setting_information_alarm);
        mTbInformationAlarm = (ToggleButton) findViewById(R.id.tb_information_alarm);
        mRlContactUs = (RelativeLayout) findViewById(R.id.rl_contact_us);
        mRlAboutUs = (RelativeLayout) findViewById(R.id.rl_about_us);
        mRlResetPassword = (RelativeLayout) findViewById(R.id.rl_reset_password);
        mSettingVersion = (TextView) findViewById(R.id.setting_version);
    }

    @Override
    protected void initListener() {
        mIvBack.setOnClickListener(this);
        mSettingInformationAlarm.setOnClickListener(this);
        //mTbInformationAlarm.setOnClickListener(this);
        mRlContactUs.setOnClickListener(this);
        mRlAboutUs.setOnClickListener(this);
        mRlResetPassword.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mTbInformationAlarm.setChecked(App.SP.getBoolean("informationAlarm", false));
        getNsmServerInfo();

        mSettingVersion.setText(PackageVersion.getPackageVersion(this));
    }

    private void getNsmServerInfo() {
        HashMap params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_GET_NSM_SERVER_INFO, params, NsmServerInfoResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_GET_NSM_SERVER_INFO, this, false).setTag(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                App.EDIT.putBoolean("informationAlarm", mTbInformationAlarm.isChecked()).commit();
                finish();
                break;
            case R.id.setting_information_alarm:
                mTbInformationAlarm.setChecked(!mTbInformationAlarm.isChecked());
                break;
//            case R.id.tb_information_alarm:
//                mTbInformationAlarm.setChecked(!mTbInformationAlarm.isChecked());
//                break;
            case R.id.rl_contact_us:
                if (null != nsmServerInfo) {
                    ChatInfoBean chatInfoBean = new ChatInfoBean(
                            nsmServerInfo.getLogo(),
                            "0",
                            nsmServerInfo.getName());
                    EventBus.getDefault().postSticky(chatInfoBean);
                    startActivity(new Intent(this, ChatActivity.class)
                            .putExtra(EaseConstant.EXTRA_USER_ID, nsmServerInfo.getHxUserName()));
                } else {
                    showToastError("连接失败");
                }
                break;
            case R.id.rl_about_us:
//                Intent intent = new Intent(this, MainPushADActivity.class);
//                startActivity(intent);
                Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(ABOUT_US_URL));
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.getApplication().startActivity(it);
                break;
            case R.id.rl_reset_password:
                ActivityUtils.startActivity(this, ResetPwdActivity.class);
                break;
        }
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_GET_NSM_SERVER_INFO
                && response instanceof NsmServerInfoResponse) {
            NsmServerInfoResponse myFriendsResponse = (NsmServerInfoResponse) response;
            int code = myFriendsResponse.getCode();
            if (1 == code) {
                nsmServerInfo = myFriendsResponse.getData().getNsm();
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }
}
