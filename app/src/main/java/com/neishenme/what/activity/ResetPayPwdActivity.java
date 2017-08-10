package com.neishenme.what.activity;

import android.os.CountDownTimer;
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
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.bean.TradeSuccessResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.DoSomethingListener;
import com.neishenme.what.receiver.SMSBroadcastReceiver;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.UpSoftInputUtils;

import org.seny.android.utils.MD5Utils;

import java.util.HashMap;

/**
 * 作者：zhaozh create on 2016/5/20 13:19
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 重置支付密码的界面
 * .
 * 其作用是 :
 */
public class ResetPayPwdActivity extends BaseActivity implements HttpLoader.ResponseListener {
    private ImageView mIvActionbarLeft;
    private TextView mTvActionbarMiddle;
    private TextView mResetPaypwdPhoneTv;
    private EditText mResetPaypwdAutocodeEt;
    private Button mResetPaypwdSendcodeBtn;
    private EditText mResetPaypwdPasswordEt;
    private Button mResetPaypwdSubmitBtn;

    private TimeCount time;

    private boolean hasAutoCode, hasPayPwd = false;

    @Override
    protected int initContentView() {
        return R.layout.activity_reset_paypwd;
    }

    @Override
    protected void initView() {
        mIvActionbarLeft = (ImageView) findViewById(R.id.iv_actionbar_left);
        mTvActionbarMiddle = (TextView) findViewById(R.id.tv_actionbar_middle);

        mResetPaypwdPhoneTv = (TextView) findViewById(R.id.reset_paypwd_phone_tv);
        mResetPaypwdAutocodeEt = (EditText) findViewById(R.id.reset_paypwd_autocode_et);
        mResetPaypwdPasswordEt = (EditText) findViewById(R.id.reset_paypwd_password_et);

        mResetPaypwdSendcodeBtn = (Button) findViewById(R.id.reset_paypwd_sendcode_btn);
        mResetPaypwdSubmitBtn = (Button) findViewById(R.id.reset_paypwd_submit_btn);

    }

    @Override
    protected void initListener() {
        mIvActionbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mResetPaypwdSendcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSendCode();
            }
        });

        mResetPaypwdSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitNewPassword();
            }
        });

        mResetPaypwdAutocodeEt.addTextChangedListener(new TextWatcher() {
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

        mResetPaypwdPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                hasPayPwd = !TextUtils.isEmpty(s.toString());
                refreshBtnState();
            }
        });
    }

    @Override
    protected void initData() {
        mTvActionbarMiddle.setVisibility(View.INVISIBLE);
        time = new TimeCount(60000, 1000);
        String phone = App.SP.getString("phone", "");
        if (!TextUtils.isEmpty(phone) && phone.length() == 11) {
            mResetPaypwdPhoneTv.setText(phone.substring(0, 3) + "****" + phone.substring(8));
        } else if (!TextUtils.isEmpty(phone) && phone.length() >= 4) {
            mResetPaypwdPhoneTv.setText("*******" + phone.substring(phone.length() - 4));
        } else {
            mResetPaypwdPhoneTv.setText("***********");
        }
        UpSoftInputUtils.goDown(mResetPaypwdPasswordEt, new DoSomethingListener() {
            @Override
            public void onDoSomethingListener() {
                submitNewPassword();
            }
        });
    }

    //点击发送验证码
    private void getSendCode() {
        registerSms();
        mResetPaypwdSendcodeBtn.setClickable(false);
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.get(ConstantsWhatNSM.URL_SEND_NUM_PAY, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_SEND_NUM_PAY, this, false).setTag(this);
    }

    private void registerSms() {
        SMSBroadcastReceiver.registerSms(this).setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void onReceived(String autoCode) {
                mResetPaypwdAutocodeEt.setText(autoCode);
            }
        });
    }

    private void refreshBtnState() {
        mResetPaypwdSubmitBtn.setEnabled(hasAutoCode && hasPayPwd);
    }

    /**
     * 提交按钮
     */
    private void submitNewPassword() {
        String authCode = mResetPaypwdAutocodeEt.getText().toString().trim();
        String password = mResetPaypwdPasswordEt.getText().toString().trim();
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(authCode)) {
            showToastInfo("请将信息填写完整再提交呗");
            return;
        }
        if (password.length() != 6) {
            showToastInfo("支付密码应该为6位哦");
            mResetPaypwdPasswordEt.setText("");
            return;
        }
        submitResetPayPassword(password, authCode);
    }

    private void submitResetPayPassword(String password, String authCode) {
        mResetPaypwdSubmitBtn.setClickable(false);
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("newPassword", MD5Utils.addToMD5(password));
        params.put("authCode", authCode);

        HttpLoader.post(ConstantsWhatNSM.URL_RESET_PAY_PASSWORD, params, TradeSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_RESET_PAY_PASSWORD, this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_SEND_NUM_PAY
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse successResponse = (SendSuccessResponse) response;
            int code = successResponse.getCode();
            if (1 == code) {
//                showToast("发送成功,请稍等");
                time.start();
            } else {
                showToastInfo(successResponse.getMessage());
                mResetPaypwdSendcodeBtn.setClickable(true);
            }
            return;
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_RESET_PAY_PASSWORD
                && response instanceof TradeSuccessResponse) {
            TradeSuccessResponse successResponse = (TradeSuccessResponse) response;
            int code = successResponse.getCode();
            if (1 == code) {
                showToastSuccess("恭喜您重置支付密码成功");
                finish();
            } else {
                showToastInfo(successResponse.getMessage());
                mResetPaypwdSubmitBtn.setClickable(true);
            }
            return;
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        resetSendAutoCode();
        mResetPaypwdSubmitBtn.setClickable(true);
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            resetSendAutoCode();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            mResetPaypwdSendcodeBtn.setText(millisUntilFinished / 1000 + " 秒");
        }
    }

    private void resetSendAutoCode() {
        mResetPaypwdSendcodeBtn.setText("重新发送");
        mResetPaypwdSendcodeBtn.setClickable(true);
    }

    @Override
    protected void onStop() {
        HttpLoader.cancelRequest(this);
        SMSBroadcastReceiver.unRegisterSms(this);
        super.onStop();
    }
}
