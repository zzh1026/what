package com.neishenme.what.activity;

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
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.receiver.SMSBroadcastReceiver;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.TimeCountDownUtils;

import org.seny.android.utils.MD5Utils;

import java.util.HashMap;

/**
 * 作者：zhaozh create on 2016/5/12 20:32
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 重置登录密码的界面
 * .
 * 其作用是 :
 */
public class ResetPwdActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener {
    private ImageView mIvActionbarLeft;
    private TextView mTvActionbarMiddle;
    private EditText mResetUsernameEt;
    private EditText mResetAutocodeEt;
    private Button mResetSendcodeBtn;
    private EditText mResetPasswordEt;
    private Button mResetBtn;

    private boolean hasUsername, hasAutoCode, hasNewPassword = false;

    @Override
    protected int initContentView() {
        return R.layout.activity_resetpwd;
    }

    @Override
    protected void initView() {
        mIvActionbarLeft = (ImageView) findViewById(R.id.iv_actionbar_left);
        mTvActionbarMiddle = (TextView) findViewById(R.id.tv_actionbar_middle);

        mResetUsernameEt = (EditText) findViewById(R.id.reset_username_et);
        mResetAutocodeEt = (EditText) findViewById(R.id.reset_autocode_et);
        mResetPasswordEt = (EditText) findViewById(R.id.reset_password_et);

        mResetSendcodeBtn = (Button) findViewById(R.id.reset_sendcode_btn);
        mResetBtn = (Button) findViewById(R.id.reset_btn);
    }

    @Override
    protected void initListener() {
        mIvActionbarLeft.setOnClickListener(this);
        mResetSendcodeBtn.setOnClickListener(this);
        mResetBtn.setOnClickListener(this);

        mResetUsernameEt.addTextChangedListener(new TextWatcher() {
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

        mResetAutocodeEt.addTextChangedListener(new TextWatcher() {
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

        mResetPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                hasNewPassword = !TextUtils.isEmpty(s.toString());
                refreshBtnState();
            }
        });
    }

    @Override
    protected void initData() {
        mTvActionbarMiddle.setText("忘记密码");
        if (TimeCountDownUtils.canStart()) {
            mResetSendcodeBtn.setEnabled(true);
        } else {
            mResetSendcodeBtn.setEnabled(false);
        }

        TimeCountDownUtils.regest(new TimeCountDownUtils.TimeCallInterface() {
            @Override
            public void onFinish() {
                resetAutoCodeBtn();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                mResetSendcodeBtn.setText(millisUntilFinished / 1000 + " 秒");
            }
        });
    }

    private void refreshBtnState() {
        mResetBtn.setEnabled(hasUsername && hasAutoCode && hasNewPassword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_sendcode_btn:
                getSendCode();
                break;
            case R.id.reset_btn:
                reset();
                break;
            case R.id.iv_actionbar_left:
                finish();
                break;
        }
    }

    private void reset() {
        String phone = mResetUsernameEt.getText().toString().trim();
        String authCode = mResetAutocodeEt.getText().toString().trim();
        String password = mResetPasswordEt.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(authCode) || TextUtils.isEmpty(password)) {
            showToastInfo(getString(R.string.reset_password_username_or_password_null));
            return;
        }
        if (password.length() < 6) {
            showToastInfo(getString(R.string.reset_password_sort_password));
            mResetUsernameEt.setText("");
            return;
        }

        if (NSMTypeUtils.patternPhoneSuccess(phone)) {
            showToastInfo(getString(R.string.reset_password_username_or_password_null));
            mResetUsernameEt.setText("");
            return;
        }
        regestUserNet(phone, authCode, MD5Utils.addToMD5(password));
    }

    /**
     * 注册提交
     *
     * @param phone
     * @param authCode
     * @param password
     */
    private void regestUserNet(String phone, String authCode, String password) {
        mResetBtn.setClickable(false);
        mResetBtn.setText("正在修改");
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("password", password);
        params.put("authCode", authCode);

        HttpLoader.post(ConstantsWhatNSM.URL_RESET_PASSWORD, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_RESET_PASSWORD, this, false).setTag(this);
    }

    //点击发送验证码
    private void getSendCode() {
        String phone = mResetUsernameEt.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            showToastInfo(getString(R.string.reset_password_username_null));
            return;
        }
        if (NSMTypeUtils.patternPhoneSuccess(phone)) {
            showToastInfo("亲,认真点,这个不是手机号呀");
            mResetUsernameEt.setText("");
            mResetAutocodeEt.setText("");
            mResetPasswordEt.setText("");
            return;
        }
        registerSms();
        regestSendCodeNet(phone);
    }

    private void registerSms() {
        SMSBroadcastReceiver.registerSms(this).setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void onReceived(String autoCode) {
                mResetAutocodeEt.setText(autoCode);
            }
        });
    }

    private void regestSendCodeNet(String phone) {
        mResetSendcodeBtn.setClickable(false);
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("type", "USER_RESET_PWD");
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
//                showToast(getString(R.string.reset_password_sendautocode_success));
                startTimeCount();
            } else {
                showToastInfo(successResponse.getMessage());
                mResetSendcodeBtn.setClickable(true);
            }
            return;
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_RESET_PASSWORD
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse resetResponse = (SendSuccessResponse) response;
            int code = resetResponse.getCode();
            if (1 == code) {
                showToastSuccess("重置密码成功,快去登录吧");
                finish();
            } else {
                mResetBtn.setClickable(true);
                mResetBtn.setText("确定");
                showToastInfo(resetResponse.getMessage());
            }
            return;
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        mResetBtn.setClickable(true);
        mResetBtn.setText("确定");
        resetAutoCodeBtn();
    }

    private void startTimeCount() {
        if (TimeCountDownUtils.canStart()) {
            mResetSendcodeBtn.setEnabled(false);
            TimeCountDownUtils.start();
        }
    }

    //重置按钮的状态
    private void resetAutoCodeBtn() {
        mResetSendcodeBtn.setEnabled(true);
        mResetSendcodeBtn.setText(getString(R.string.send_autocode_again));
        mResetSendcodeBtn.setClickable(true);
    }

    @Override
    protected void onStop() {
        HttpLoader.cancelRequest(this);
        SMSBroadcastReceiver.unRegisterSms(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        TimeCountDownUtils.unRegest();
        super.onDestroy();
    }
}
