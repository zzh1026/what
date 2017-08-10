package com.neishenme.what.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.neishenme.what.R;
import com.neishenme.what.activity.LoginActivity;
import com.neishenme.what.application.App;
import com.neishenme.what.bean.LoginQuickResponse;
import com.neishenme.what.bean.LoginQuickSuccessResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.common.CityLocationConfig;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.receiver.SMSBroadcastReceiver;
import com.neishenme.what.utils.AppManager;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.PackageVersion;
import com.neishenme.what.utils.SoftInputUtils;
import com.neishenme.what.utils.TimeCountDownUtils;
import com.neishenme.what.utils.UpdataPersonInfo;

import java.util.HashMap;

/**
 * 作者：zhaozh create on 2016/5/11 17:14
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class LoginQuickFragment extends Fragment implements View.OnClickListener, HttpLoader.ResponseListener {
    private LoginActivity mLoginActivity;

    private EditText mLoginUsernameEt;
    private EditText mLoginAutocodeEt;
    private Button mLoginSendcodeBtn;
    private Button mLoginBtn;

    private String token;
    private String phone;

    private boolean hasUser = false;
    private boolean hasPwd = false;

    public LoginQuickFragment() {
    }

    public static LoginQuickFragment newInstance() {
        LoginQuickFragment loginQuickFragment = new LoginQuickFragment();
        return loginQuickFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_quick, null);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initListener() {
        mLoginSendcodeBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);

        mLoginUsernameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hasUser = s.toString().length() != 0;
                mLoginBtn.setEnabled(hasUser && hasPwd);
            }
        });

        mLoginAutocodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hasPwd = s.toString().length() != 0;
                mLoginBtn.setEnabled(hasUser && hasPwd);
            }
        });
    }

    private void initData() {
        String defaultUserName = App.SP.getString("phone", "");
        if (TextUtils.isEmpty(defaultUserName)) {
            hasUser = false;
        } else {
            hasUser = true;
            mLoginUsernameEt.setText(defaultUserName);
        }
        mLoginBtn.setEnabled(false);
        mLoginActivity = (LoginActivity) getActivity();

        if (TimeCountDownUtils.canStart()) {
            mLoginSendcodeBtn.setEnabled(true);
        } else {
            mLoginSendcodeBtn.setEnabled(false);
        }
        TimeCountDownUtils.regest(new TimeCountDownUtils.TimeCallInterface() {
            @Override
            public void onFinish() {
                resetSendAutoCode();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                mLoginSendcodeBtn.setText(millisUntilFinished / 1000 + " 秒");
            }
        });
    }

    private void initView(View view) {
        mLoginUsernameEt = (EditText) view.findViewById(R.id.login_username_et);
        mLoginAutocodeEt = (EditText) view.findViewById(R.id.login_autocode_et);
        mLoginSendcodeBtn = (Button) view.findViewById(R.id.login_sendcode_btn);
        mLoginBtn = (Button) view.findViewById(R.id.login_btn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_sendcode_btn:
                getSendCode();
                break;
            case R.id.login_btn:
                login();
                break;
        }
    }

    private void getSendCode() {
        phone = mLoginUsernameEt.getText().toString().trim();
        if (NSMTypeUtils.isEmpty(phone)) {
            mLoginActivity.showToastInfo(getString(R.string.login_quick_username_is_null));
            return;
        }
        if (phone.length() != 11) {
            mLoginActivity.showToastInfo(getString(R.string.login_wrong_phone_number));
            mLoginUsernameEt.setText("");
            mLoginAutocodeEt.setText("");
            return;
        }
        if (NSMTypeUtils.patternPhoneSuccess(phone)) {
            mLoginActivity.showToastInfo(getString(R.string.login_wrong_phone_number));
            mLoginUsernameEt.setText("");
            mLoginAutocodeEt.setText("");
            return;
        }
        registerSms();
        regestSendCodeNet(phone);
    }

    private void login() {
        String username = mLoginUsernameEt.getText().toString().trim();
        String autoCode = mLoginAutocodeEt.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(autoCode)) {
            mLoginActivity.showToastInfo(getString(R.string.login_quick_username_null));
            return;
        }
        if (NSMTypeUtils.patternPhoneSuccess(username)) {
            mLoginActivity.showToastInfo(getString(R.string.login_wrong_phone_number));
            mLoginUsernameEt.setText("");
            return;
        }

        loginNet(username, autoCode);
    }

    private void loginNet(String username, String autoCode) {
        SoftInputUtils.UpSoftInputUtils();
        mLoginBtn.setClickable(false);
        mLoginBtn.setText("正在登录");
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", username);
        params.put("identifier", AppManager.getIMEI());
        params.put("channel", AppManager.getAppMetaData(mLoginActivity));
        params.put("authCode", autoCode);
        params.put("areaId", String.valueOf(CityLocationConfig.cityLocationId));
        HttpLoader.post(ConstantsWhatNSM.URL_LOGIN_QUICK, params, LoginQuickResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_LOGIN_QUICK, this, false).setTag(this);
    }

    /**
     * @param phone
     */
    private void regestSendCodeNet(String phone) {
        mLoginSendcodeBtn.setClickable(false);
//        USER_LOGIN　快速登录.　USER_REG　注册；USER_RESET_PWD　重置
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("type", "USER_LOGIN");
        HttpLoader.get(ConstantsWhatNSM.URL_SEND_NUM, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_SEND_NUM, this, false).setTag(this);
    }

    private void registerSms() {
        SMSBroadcastReceiver.registerSms(mLoginActivity).setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void onReceived(String autoCode) {
                mLoginAutocodeEt.setText(autoCode);
                String username = mLoginUsernameEt.getText().toString().trim();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(autoCode)) {
                    return;
                }
                loginNet(username, autoCode);
            }
        });
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_SEND_NUM
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse successResponse = (SendSuccessResponse) response;
            int code = successResponse.getCode();
            if (1 == code) {
//                mLoginActivity.showToast("发送成功");
                startTimeCount();
            } else {
                resetSendAutoCode();
                mLoginActivity.showToastInfo(successResponse.getMessage());
            }
            return;
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_LOGIN_QUICK
                && response instanceof LoginQuickResponse) {
            LoginQuickResponse loginQuickResponse = (LoginQuickResponse) response;
            int code = loginQuickResponse.getCode();
            if (1 != code) {
                mLoginActivity.showToastInfo(getString(R.string.login_quick_wrong_username_or_password));
                mLoginBtn.setClickable(true);
                mLoginBtn.setText("登录");
                return;
            }
            mLoginBtn.setClickable(false);
            mLoginBtn.setText("初始化中");

            App.EDIT.putString("phone", phone).commit();

            if (loginQuickResponse.getData().isNewX()) {
                //跳转到注册页面2;
                mLoginActivity.toRegestUserInfo(loginQuickResponse.getData().getToken());
            } else {
                //通过token获取数据
                token = loginQuickResponse.getData().getToken();
                getUserInfo();
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_LOGIN_BYTOKEN
                && response instanceof LoginQuickSuccessResponse) {
            LoginQuickSuccessResponse loginResponse = (LoginQuickSuccessResponse) response;
            int code = loginResponse.getCode();
            if (1 == code) {
                LoginQuickSuccessResponse.DataEntity.UserEntity user = loginResponse.getData().getUser();
                if ("1".equals(user.getGender()) || "2".equals(user.getGender())) {
                    user.setToken(token);
                    //将数据进行更新.
                    mLoginActivity.showToastSuccess("登录成功");
                    UpdataPersonInfo.updatePersonInfoByToken(loginResponse.getData());
                    mLoginActivity.toMainAct();
                    mLoginBtn.setText("登录成功");
                } else {
                    //跳转到注册页面2;
                    mLoginActivity.toRegestUserInfo(token);
                }
            } else {
                mLoginActivity.showToastError("登录失败,请重试");
                mLoginBtn.setClickable(true);
                mLoginBtn.setText("立即登录");
            }
        }
    }

    private void startTimeCount() {
        if (TimeCountDownUtils.canStart()) {
            mLoginSendcodeBtn.setEnabled(false);
            TimeCountDownUtils.start();
        }
    }

    private void getUserInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", token);
        params.put("platform", "2");
        params.put("identifier", AppManager.getIMEI());
        params.put("version", PackageVersion.getPackageVersion(mLoginActivity));
        HttpLoader.get(ConstantsWhatNSM.URL_LOGIN_BYTOKEN, params, LoginQuickSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_LOGIN_BYTOKEN, this, false).setTag(this);
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        HttpLoader.cancelRequest(this);
        mLoginActivity.wrong();
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_SEND_NUM) {
            resetSendAutoCode();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TimeCountDownUtils.unRegest();
    }

    private void resetSendAutoCode() {
        mLoginSendcodeBtn.setEnabled(true);
        mLoginSendcodeBtn.setText(getString(R.string.send_autocode_again));
        mLoginSendcodeBtn.setClickable(true);
    }
}
