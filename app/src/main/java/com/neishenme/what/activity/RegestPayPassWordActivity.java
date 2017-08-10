package com.neishenme.what.activity;

import android.text.TextUtils;
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
import com.neishenme.what.bean.TradeSuccessResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.DoSomethingListener;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.UpSoftInputUtils;

import org.seny.android.utils.MD5Utils;
import org.seny.android.utils.MyToast;

import java.util.HashMap;

/**
 * 作者：zhaozh create on 2016/5/20 12:32
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 初始化支付密码界面,如果需要使用 现金余额或者活动余额, 则需要验证是否已经设置支付密码,如果没有进入该界面
 * .
 * 其作用是 :
 */
public class RegestPayPassWordActivity extends BaseActivity implements HttpLoader.ResponseListener {
    private ImageView mIvActionbarLeft;
    private TextView mTvActionbarMiddle;
    private EditText mRegestPayPwdFirstEt;
    private EditText mRegestPayPwdSecondEt;
    private Button mRegestPayPwdBtn;

    @Override
    protected int initContentView() {
        return R.layout.activity_regest_paypassword;
    }

    @Override
    protected void initView() {
        mIvActionbarLeft = (ImageView) findViewById(R.id.iv_actionbar_left);
        mTvActionbarMiddle = (TextView) findViewById(R.id.tv_actionbar_middle);
        mRegestPayPwdFirstEt = (EditText) findViewById(R.id.regest_pay_pwd_first_et);
        mRegestPayPwdSecondEt = (EditText) findViewById(R.id.regest_pay_pwd_second_et);
        mRegestPayPwdBtn = (Button) findViewById(R.id.regest_pay_pwd_btn);
    }

    @Override
    protected void initListener() {
        mIvActionbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRegestPayPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regestPayPassword();
            }
        });
    }

    @Override
    protected void initData() {
        mTvActionbarMiddle.setVisibility(View.INVISIBLE);

        UpSoftInputUtils.goDown(mRegestPayPwdSecondEt, new DoSomethingListener() {
            @Override
            public void onDoSomethingListener() {
                regestPayPassword();
            }
        });
    }

    private void regestPayPassword() {
        String pwd = mRegestPayPwdFirstEt.getText().toString().trim();
        String againPwd = mRegestPayPwdSecondEt.getText().toString().trim();
        if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(againPwd)) {
            MyToast.show(App.getApplication(), "请将密码信息补充完整再确定吧");
            mRegestPayPwdFirstEt.setText("");
            mRegestPayPwdSecondEt.setText("");
            return;
        }
        if (!pwd.equals(againPwd)) {
            MyToast.show(App.getApplication(),"两次输入的密码不一样,请重新输入");
            mRegestPayPwdFirstEt.setText("");
            mRegestPayPwdSecondEt.setText("");
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("newPassword", MD5Utils.addToMD5(pwd));
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_USER_REGEST_PAY_PWD, params, TradeSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_USER_REGEST_PAY_PWD, this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_USER_REGEST_PAY_PWD
                && response instanceof TradeSuccessResponse) {
            TradeSuccessResponse walletMaxPrice = (TradeSuccessResponse) response;
            int code = walletMaxPrice.getCode();
            if (1 == code) {
                MyToast.show(this, "密码设置成功");
                finish();
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpLoader.cancelRequest(this);
    }
}
