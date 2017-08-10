package com.neishenme.what.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.neishenme.what.R;
import com.neishenme.what.activity.LoginActivity;
import com.neishenme.what.application.App;
import com.neishenme.what.bean.LoginResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.AppManager;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.PackageVersion;
import com.neishenme.what.utils.SoftInputUtils;
import com.neishenme.what.utils.UpdataPersonInfo;

import org.seny.android.utils.MD5Utils;
import org.seny.android.utils.MyToast;

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
public class LoginNormalFragment extends Fragment implements View.OnClickListener, HttpLoader.ResponseListener {
    private LoginActivity mLoginActivity;

    private EditText mLoginNormalUsername;
    private EditText mLoginNormalPwd;
    private Button mLoginBtn;
    private TextView mLoginRegestTv;
    private TextView mLoginRegestpwdTv;
    private String username;

    private boolean hasUser = false;
    private boolean hasPwd = false;

    public LoginNormalFragment() {
    }

    public static LoginNormalFragment newInstance() {
        LoginNormalFragment loginQuickFragment = new LoginNormalFragment();
        return loginQuickFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_normal, null);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initListener() {
        mLoginBtn.setOnClickListener(this);
        mLoginRegestTv.setOnClickListener(this);
        mLoginRegestpwdTv.setOnClickListener(this);

        mLoginNormalUsername.addTextChangedListener(new TextWatcher() {
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

        mLoginNormalPwd.addTextChangedListener(new TextWatcher() {
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
            mLoginNormalUsername.setText(defaultUserName);
        }
        mLoginBtn.setEnabled(false);
        mLoginActivity = (LoginActivity) getActivity();
    }

    private void initView(View view) {
        mLoginNormalUsername = (EditText) view.findViewById(R.id.login_normal_username);
        mLoginNormalPwd = (EditText) view.findViewById(R.id.login_normal_pwd);
        mLoginBtn = (Button) view.findViewById(R.id.login_btn);
        mLoginRegestTv = (TextView) view.findViewById(R.id.login_regest_tv);
        mLoginRegestpwdTv = (TextView) view.findViewById(R.id.login_regsetpwd_tv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                loginIn();
                break;
            case R.id.login_regest_tv:
                mLoginActivity.toRegestAct();
                break;
            case R.id.login_regsetpwd_tv:
                mLoginActivity.toResetPwdAct();
                break;
        }
    }

    //点击登录按钮
    private void loginIn() {
        username = mLoginNormalUsername.getText().toString().trim();
        String password = mLoginNormalPwd.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            mLoginActivity.showToastInfo(getString(R.string.login_normal_username_or_password_null));
            return;
        }
        if (username.length() != 11) {
            mLoginActivity.showToastInfo(getString(R.string.login_wrong_phone_number));
            return;
        }
        if (NSMTypeUtils.patternPhoneSuccess(username)) {
            mLoginActivity.showToastInfo(getString(R.string.login_wrong_phone_number));
            mLoginNormalUsername.setText("");
            mLoginNormalPwd.setText("");
            return;
        }
        if (password.length() < 6) {
            mLoginActivity.showToastInfo(getString(R.string.reset_password_sort_password));
        }

        loginNet(username, MD5Utils.addToMD5(password));
    }

    /**
     * 联网
     *
     * @param phone
     * @param password
     */
    private void loginNet(String phone, String password) {
        SoftInputUtils.UpSoftInputUtils();
        mLoginBtn.setClickable(false);
        mLoginBtn.setText("正在登录");
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("password", password);
        params.put("platform", "2");
        params.put("channel", AppManager.getAppMetaData(mLoginActivity));
        params.put("identifier", AppManager.getIMEI());
        params.put("version", PackageVersion.getPackageVersion(mLoginActivity));
        HttpLoader.post(ConstantsWhatNSM.URL_LOGIN, params, LoginResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_LOGIN, this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_LOGIN
                && response instanceof LoginResponse) {
            LoginResponse loginResponse = (LoginResponse) response;
            int code = loginResponse.getCode();
            if (1 != code) {
                mLoginActivity.showToastInfo(getString(R.string.login_quick_wrong_username_or_password));
                mLoginBtn.setClickable(true);
                mLoginBtn.setText("登录");
                return;
            }
            mLoginActivity.showToastSuccess("登录成功");
            mLoginBtn.setClickable(false);
            mLoginBtn.setText("登录成功");

            App.EDIT.putString("phone", username).commit();

            LoginResponse.DataEntity.UserEntity userInfo = loginResponse.getData().getUser();
            if ("1".equals(userInfo.getGender()) || "2".equals(userInfo.getGender())) {
                //将数据进行更新.
                UpdataPersonInfo.updatePersonInfoByNormal(loginResponse.getData().getUser());
                mLoginActivity.toMainAct();
            } else {
                //跳转到注册页面2;
                mLoginActivity.toRegestUserInfo(userInfo.getToken());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        HttpLoader.cancelRequest(this);
        mLoginActivity.wrong();
    }
}
