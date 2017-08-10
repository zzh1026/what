package com.neishenme.what.activity;

import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.neishenme.what.R;
import com.neishenme.what.application.App;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.RegestResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.common.CityLocationConfig;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.receiver.SMSBroadcastReceiver;
import com.neishenme.what.utils.AppManager;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.PackageVersion;
import com.neishenme.what.utils.TimeCountDownUtils;

import org.seny.android.utils.ActivityUtils;

import java.util.HashMap;

/**
 * 作者：zhaozh create on 2016/5/12 19:07
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 注册界面, 用来进行帐号的注册,注册完毕后进入注册个人信息界面来初始化个人信息
 * .
 * 其作用是 :
 */
public class RegestActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener {
    private static final String REGEST_USER_XIEYI = "http://www.neishenme.com/agree_new.html";
    private ImageView mIvActionbarLeft;
    private TextView mTvActionbarMiddle;
    private EditText mRegestUsernameEt;
    private EditText mRegestAutocodeEt;
    private Button mRegestSendcodeBtn;
    private EditText mRegestPasswordEt;
    private Button mRegestBtn;
    private TextView mRegestUserTv;

    private boolean hasUsername, hasAutoCode, hasPassword = false;

    @Override
    protected int initContentView() {
        return R.layout.activity_regest;
    }

    @Override
    protected void initView() {
        mIvActionbarLeft = (ImageView) findViewById(R.id.iv_actionbar_left);
        mTvActionbarMiddle = (TextView) findViewById(R.id.tv_actionbar_middle);

        mRegestUsernameEt = (EditText) findViewById(R.id.regest_username_et);
        mRegestAutocodeEt = (EditText) findViewById(R.id.regest_autocode_et);
        mRegestPasswordEt = (EditText) findViewById(R.id.regest_password_et);

        mRegestSendcodeBtn = (Button) findViewById(R.id.regest_sendcode_btn);
        mRegestBtn = (Button) findViewById(R.id.regest_btn);
        mRegestUserTv = (TextView) findViewById(R.id.regest_user_tv);
    }

    @Override
    protected void initListener() {
        mIvActionbarLeft.setOnClickListener(this);
        mRegestSendcodeBtn.setOnClickListener(this);
        mRegestBtn.setOnClickListener(this);
        mRegestUserTv.setOnClickListener(this);

        mRegestUsernameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                hasUsername = !TextUtils.isEmpty(s.toString());
                refreshBtnState();
            }
        });

        mRegestAutocodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                hasAutoCode = !TextUtils.isEmpty(s.toString());
                refreshBtnState();
            }
        });

        mRegestPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                hasPassword = !TextUtils.isEmpty(s.toString());
                refreshBtnState();
            }
        });
    }

    private void refreshBtnState() {
        mRegestBtn.setEnabled(hasUsername && hasAutoCode && hasPassword);
    }

    @Override
    protected void initData() {
        mTvActionbarMiddle.setText("注册");

        if (TimeCountDownUtils.canStart()) {
            mRegestSendcodeBtn.setEnabled(true);
        } else {
            mRegestSendcodeBtn.setEnabled(false);
        }
        TimeCountDownUtils.regest(new TimeCountDownUtils.TimeCallInterface() {
            @Override
            public void onFinish() {
                resetAutoCodeBtn();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                mRegestSendcodeBtn.setText(millisUntilFinished / 1000 + " 秒");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regest_sendcode_btn:
                getSendCode();
                break;
            case R.id.regest_btn:
                regest();
                break;
            case R.id.iv_actionbar_left:
                finish();
                break;
            case R.id.regest_user_tv:
                Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(REGEST_USER_XIEYI));
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.getApplication().startActivity(it);
                break;
        }
    }

    private void regest() {
        String phone = mRegestUsernameEt.getText().toString().trim();
        String authCode = mRegestAutocodeEt.getText().toString().trim();
        String password = mRegestPasswordEt.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(authCode) || TextUtils.isEmpty(password)) {
            showToastWarning(getString(R.string.reset_password_username_or_password_null));
            return;
        }
        if (password.length() < 6) {
            showToastWarning(getString(R.string.reset_password_sort_password));
            mRegestPasswordEt.setText("");
            return;
        }
        if (NSMTypeUtils.patternPhoneSuccess(phone)) {
            showToastWarning(getString(R.string.login_wrong_phone_number));
            mRegestUsernameEt.setText("");
            return;
        }
        regestUserNet(phone, authCode, password);
    }

    /**
     * 注册提交
     *
     * @param phone
     * @param authCode
     * @param password
     */
    private void regestUserNet(String phone, String authCode, String password) {
        mRegestBtn.setClickable(false);
        mRegestBtn.setText("正在注册");
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("areaId", String.valueOf(CityLocationConfig.cityLocationId));
        params.put("password", password);
        params.put("authCode", authCode);
        params.put("platform", "2");
        params.put("channel", AppManager.getAppMetaData(this));
        params.put("identifier", AppManager.getIMEI());
        params.put("version", PackageVersion.getPackageVersion(this));

        HttpLoader.post(ConstantsWhatNSM.URL_REGEST, params, RegestResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_REGEST, this, false).setTag(this);
    }

    //点击发送验证码
    private void getSendCode() {
        String phone = mRegestUsernameEt.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            showToastInfo(getString(R.string.regest_user_username_null));
            return;
        }
        if (NSMTypeUtils.patternPhoneSuccess(phone)) {
            showToastInfo(getString(R.string.login_wrong_phone_number));
            mRegestUsernameEt.setText("");
            mRegestAutocodeEt.setText("");
            mRegestPasswordEt.setText("");
            return;
        }
        registerSms();
        regestSendCodeNet(phone);
    }

    private void registerSms() {
        SMSBroadcastReceiver.registerSms(this).setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void onReceived(String autoCode) {
                mRegestAutocodeEt.setText(autoCode);
            }
        });
    }

    private void regestSendCodeNet(String phone) {
        mRegestSendcodeBtn.setClickable(false);
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("type", "USER_REG");
        HttpLoader.get(ConstantsWhatNSM.URL_SEND_NUM, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_SEND_NUM, this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_SEND_NUM
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse successResponse = (SendSuccessResponse) response;
            int code = successResponse.getCode();
            if (1 == code) {
//                showToast(getString(R.string.regest_user_phone_autocode_send_success));
                startTimeCount();
            } else {
                showToastInfo(successResponse.getMessage());
                resetAutoCodeBtn();
            }
            return;
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_REGEST
                && response instanceof RegestResponse) {
            RegestResponse regestResponse = (RegestResponse) response;
            int code = regestResponse.getCode();
            if (1 == code) {
                //跳转注册界面2
                ActivityUtils.startActivityForDataAndFinish(this, RegestUserInfoActivity.class,
                        regestResponse.getData().getUser().getToken());
            } else {
                mRegestBtn.setClickable(true);
                mRegestBtn.setText("注册");
                showToastInfo(regestResponse.getMessage());
            }
            return;
        }
    }

    private void startTimeCount() {
        if (TimeCountDownUtils.canStart()) {
            mRegestSendcodeBtn.setEnabled(false);
            TimeCountDownUtils.start();
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        HttpLoader.cancelRequest(this);
        resetAutoCodeBtn();
        mRegestBtn.setClickable(true);
        mRegestBtn.setText("注册");
    }

    private void resetAutoCodeBtn() {
        mRegestSendcodeBtn.setEnabled(true);
        mRegestSendcodeBtn.setText(getString(R.string.send_autocode_again));
        mRegestSendcodeBtn.setClickable(true);
    }

    @Override
    protected void onDestroy() {
        HttpLoader.cancelRequest(this);
        SMSBroadcastReceiver.unRegisterSms(this);
        TimeCountDownUtils.unRegest();
        super.onDestroy();
    }
}
