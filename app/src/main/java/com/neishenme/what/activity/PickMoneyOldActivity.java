package com.neishenme.what.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.neishenme.what.R;
import com.neishenme.what.application.App;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.RefundsStates;
import com.neishenme.what.bean.TradeSuccessResponse;
import com.neishenme.what.bean.WalletMaxPrice;
import com.neishenme.what.dialog.PayOrderDialog;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.PayOrderInputListener;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.MD5Utils;

import java.util.HashMap;

/**
 * 旧版的 提现界面 的备份
 * 已过时,   2017/5/22
 */
@Deprecated
public class PickMoneyOldActivity extends BaseActivity implements HttpLoader.ResponseListener {

    private ImageView ivBack;
    private TextView tvBill;
    private TextView tvCashLeave;
    private TextView tvSignOne;
    private EditText etSum;
    private Button btnPickUp; //申请退款 / 查询进度
    private double priceMax;
    private String drawPrice;

    private TextView pickMoneyForgetPaypassword;
    private String currentStatus; //当前的状态 -1为没有申请记录   如果是其它值说明是正在申请
    private RefundsStates statusMap;//返回来的状态对象

    @Override
    protected int initContentView() {
        return R.layout.activity_pick_money_old;
    }

    @Override
    protected void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvBill = (TextView) findViewById(R.id.tv_bill);
        tvCashLeave = (TextView) findViewById(R.id.tv_cash_leave);
        etSum = (EditText) findViewById(R.id.et_sum);
        btnPickUp = (Button) findViewById(R.id.btn_pick_up);
        pickMoneyForgetPaypassword = (TextView) findViewById(R.id.pick_money_forget_paypassword);
    }

    @Override
    protected void initListener() {
        tvBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PickMoneyOldActivity.this, BillOldActivity.class);
                startActivity(intent);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 申请退款 / 查询进度 监听事件
         */
        btnPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStatus.equals("-1")) {  //没有申请记录，点击为 申请退款逻辑
                    drawMoney();
                } else {             //查询进度逻辑
                    Intent intent = new Intent(PickMoneyOldActivity.this, RefundsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("states", statusMap);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        pickMoneyForgetPaypassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(PickMoneyOldActivity.this, ResetPayPwdActivity.class);
            }
        });

        /**
         * Edittext监听事件处理
         */
        etSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!currentStatus.equals("-1")) {
                    showToastInfo("您有活动正在处理");
                    etSum.setFocusable(false);
                    //强制关闭软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
    }

    @Override
    protected void initData() {
        //获取可以提取的金额
        getCanDrawMoney();
    }

    @Override
    protected void onResume() {
        super.onResume();
        etSum.setFocusable(true);
        etSum.setFocusableInTouchMode(true);
        etSum.requestFocus();
        //请求进度查看接口，查询状态
        initRefundsRequestDate();
    }

    /**
     * 请求进度查看接口，查询状态
     */
    private void initRefundsRequestDate() {
        //验证是否有支付密码
        HashMap<String, String> params = new HashMap<>();
        params.put("token", App.SP.getString("token", null));
        HttpLoader.post(ConstantsWhatNSM.URL_APPLYY_MONEY, params, TradeSuccessResponse.class,
                ConstantsWhatNSM.URL_APPLYY_MONEY_STATE, this, false).setTag(this);
    }

    private void getCanDrawMoney() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_PERSON_WALLET_MAXPRICE, params, WalletMaxPrice.class,
                ConstantsWhatNSM.REQUEST_CODE_PERSON_WALLET_MAXPRICE, this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PERSON_WALLET_MAXPRICE   //请求现金余额的业务逻辑处理
                && response instanceof WalletMaxPrice) {
            WalletMaxPrice walletMaxPrice = (WalletMaxPrice) response;
            int code = walletMaxPrice.getCode();
            ALog.i("code 的值为:" + code);
            if (1 == code) {
                priceMax = walletMaxPrice.getData().getPriceMax();
                tvCashLeave.setText("￥ " + priceMax);
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_USER_PAY_PASSWORD  //输入密码的业务逻辑处理
                && response instanceof TradeSuccessResponse) {
            TradeSuccessResponse walletMaxPrice = (TradeSuccessResponse) response;
            int code = walletMaxPrice.getCode();
            if (1 == code) {
                PayOrderDialog.showInputPwdDialog(this, new PayOrderInputListener() {
                    @Override
                    public void inputErrorListener() {
                    }

                    @Override
                    public void inputSuccessListener(String input) { //登录密码输入成功后。
                        //密码输入正确，调用申请退款借口
                        payMoneyTry(input);
                    }

                });
            } else {
                //跳转密码设置界面 (支付)
                ActivityUtils.startActivity(this, RegestPayPassWordActivity.class);
            }
        }
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_AREFUNDS           //申请退款接口业务逻辑处理
                && response instanceof TradeSuccessResponse) {
            TradeSuccessResponse walletMaxPrice = (TradeSuccessResponse) response;
            int code = walletMaxPrice.getCode();
            if (1 == code) {
                ActivityUtils.startActivityForData(this, RefundsActivity.class, walletMaxPrice.getData().getApplyTime());
                etSum.setText("");
            } else {
                showToastInfo(walletMaxPrice.getMessage());
            }
        }
        if (requestCode == ConstantsWhatNSM.URL_APPLYY_MONEY_STATE           //查询进度接口业务逻辑处理
                && response instanceof TradeSuccessResponse) {
            TradeSuccessResponse walletMaxPrice = (TradeSuccessResponse) response;
            int code = walletMaxPrice.getCode();
            if (1 == code) {
                currentStatus = walletMaxPrice.getData().getCurrentStatus(); //当前状态
                statusMap = walletMaxPrice.getData().getStatusMap();//返回来的状态对象
                if(currentStatus.equals("-1")) {
                   btnPickUp.setText("申请退款");
                    etSum.setFocusable(true);
                    etSum.setCursorVisible(true); //光标显示
                }else {
                    btnPickUp.setText("进度查询");
                    etSum.setCursorVisible(false);//光标隐藏
                }
            } else {
                showToastInfo(walletMaxPrice.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    private void drawMoney() {
        drawPrice = etSum.getText().toString().trim();
        if (TextUtils.isEmpty(drawPrice)) {
            showToastInfo(getString(R.string.pick_money_money_number_numm));
            return;
        }
        double tryDraw = Double.parseDouble(drawPrice);
        if (priceMax < tryDraw) {
            showToastWarning(getString(R.string.pick_money_more_than_max_money));
            return;
        }
        if (tryDraw == 0) {
            showToastWarning("请输入有效金额");
            return;
        }

        //验证是否有支付密码
        HashMap<String, String> params = new HashMap<>();
        params.put("token", App.SP.getString("token", null));
        HttpLoader.post(ConstantsWhatNSM.URL_USER_PAY_PASSWORD, params, TradeSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_USER_PAY_PASSWORD, this, false).setTag(this);
    }

    /**
     * 密码输入正确后，调用申请退款接口
     *
     * @param input
     */
    private void payMoneyTry(String input) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("payPassword", MD5Utils.addToMD5(input));
        params.put("price", drawPrice);
        HttpLoader.post(ConstantsWhatNSM.URL_REFUNDS, params, TradeSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_AREFUNDS, this, false).setTag(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        HttpLoader.cancelRequest(this);
        getCanDrawMoney();
    }

}
