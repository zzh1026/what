package com.neishenme.what.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.VolleyError;
import com.neishenme.what.R;
import com.neishenme.what.application.App;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.ActivtyPrice;
import com.neishenme.what.bean.AliPaySignResponse;
import com.neishenme.what.bean.MaxServicePriceResponse;
import com.neishenme.what.bean.MyWalletResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.TradeSuccessResponse;
import com.neishenme.what.bean.WxCreateTenpay;
import com.neishenme.what.bean.WxPayResult;
import com.neishenme.what.dialog.PayOrderDialog;
import com.neishenme.what.eventbusobj.TrideBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.PayOrderInputListener;
import com.neishenme.what.utils.AppManager;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.PayResult;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.MD5Utils;
import org.seny.android.utils.MyToast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * 作者：zhaozh create on 2016/5/19 14:14
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 *
 * 旧的支付界面,已弃用
 * @see PayOrderActivity
 */
@Deprecated
public class PayOrderOldActivity extends BaseActivity implements HttpLoader.ResponseListener {
    private ImageView mIvBack;
    private TextView mTvRestrantantName;
    private ImageView mIvRestaurantLogo;
    private TextView mTvTitle;
    private TextView mTvTime;
    private TextView mTvPrice;
    private RelativeLayout mRlPayCash;
    private CheckBox mCbPayCash;
    private TextView mTvCashLeave;
    private RelativeLayout mRlPayActivity;
    private CheckBox mCbPayActivity;
    private TextView mTvActivityLeave;
    private RelativeLayout mRlPayZhifubao;
    private CheckBox mCbPayZhifibao;
    private RelativeLayout mRlPayWeixin;
    private CheckBox mCbPayWeixin;
    private TextView mTvForgetPassword;
    private TextView mOrderTotalPriceTv;
    private Button mOrderPayNowBtn;
    private TrideBean trideBean;

    private double mPurse = 0;
    private double maxPrice = 0;
    //第三方应该支付的钱数量
    private double actualPurse = 0;

    String imei;
    String tradeNum;
    private WxCreateTenpay wxCreateTenpay;

    Dialog dialog;

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    private static final int SDK_PAY_FLAG = 1;

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        //Toast.makeText(PayOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        surePaySuccess();
                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayOrderOldActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                            surePaySuccess();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ALog.i("错误原因是 : " + resultStatus);
                            Toast.makeText(PayOrderOldActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                            HashMap params = new HashMap();
                            params.put("tradeNum", trideBean.tradeNum);
                            params.put("token", App.SP.getString("token", null));
                            HttpLoader.post(ConstantsWhatNSM.URL_ALIPAY_SIGN_ERROR, params, ActivtyPrice.class,
                                    ConstantsWhatNSM.REQUEST_CODE_ALIPAY_SIGN_ERROR, PayOrderOldActivity.this, false).setTag(this);
                            mOrderPayNowBtn.setClickable(true);
                            mOrderPayNowBtn.setText("立即支付");
                            setAllButtonEnable(true);
                        }
                    }
                    break;
                }
                case 2:
                    setAllButtonEnable(true);
                    mOrderPayNowBtn.setText("立即支付");
                    mOrderPayNowBtn.setClickable(true);
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private boolean isUserAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_pay_order_old;
    }

    @Override
    protected void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);

        mTvRestrantantName = (TextView) findViewById(R.id.tv_restrantant_name);
        mIvRestaurantLogo = (ImageView) findViewById(R.id.iv_restaurant_logo);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvPrice = (TextView) findViewById(R.id.tv_price);

        mRlPayCash = (RelativeLayout) findViewById(R.id.rl_pay_cash);
        mCbPayCash = (CheckBox) findViewById(R.id.cb_pay_cash);
        mTvCashLeave = (TextView) findViewById(R.id.tv_cash_leave);

        mRlPayActivity = (RelativeLayout) findViewById(R.id.rl_pay_activity);
        mCbPayActivity = (CheckBox) findViewById(R.id.cb_pay_activity);
        mTvActivityLeave = (TextView) findViewById(R.id.tv_activity_leave);

        mRlPayZhifubao = (RelativeLayout) findViewById(R.id.rl_pay_zhifubao);
        mCbPayZhifibao = (CheckBox) findViewById(R.id.cb_pay_zhifibao);

        mRlPayWeixin = (RelativeLayout) findViewById(R.id.rl_pay_weixin);
        mCbPayWeixin = (CheckBox) findViewById(R.id.cb_pay_weixin);

        mTvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);

        mOrderTotalPriceTv = (TextView) findViewById(R.id.order_total_price_tv);
        mOrderPayNowBtn = (Button) findViewById(R.id.order_pay_now_btn);
    }

    @Override
    protected void initListener() {
        mRlPayCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPurse == 0) {
                    return;
                }
                mCbPayCash.setChecked(!mCbPayCash.isChecked());
            }
        });
        mRlPayActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maxPrice == 0) {
                    return;
                }
                mCbPayActivity.setChecked(!mCbPayActivity.isChecked());
            }
        });
        mRlPayWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPurse + maxPrice > trideBean.price) {
                    return;
                }
                mCbPayWeixin.setChecked(!mCbPayWeixin.isChecked());
                mCbPayZhifibao.setChecked(false);
            }
        });
        mRlPayZhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPurse + maxPrice > trideBean.price) {
                    return;
                }
                mCbPayZhifibao.setChecked(!mCbPayZhifibao.isChecked());
                mCbPayWeixin.setChecked(false);
            }
        });

        mOrderPayNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //支付
                pay();
            }
        });
        mTvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayOrderOldActivity.this, ResetPayPwdActivity.class);
                startActivity(intent);
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        isUserAgree = getIntent().getBooleanExtra("isAgreeTa", false);

//        EventBus.getDefault().register(this);
//        trideBean = EventBus.getDefault().getStickyEvent(TrideBean.class);
        trideBean = null;

        //分发界面基础信息
        disPathBaseInfo();

        //获取可用额度信息
        getPayPurse();
    }

    private void disPathBaseInfo() {
//        mTvRestrantantName.setText(trideBean.restrantantName);
//        HttpLoader.getImageLoader().get(trideBean.restaurantLogo,
//                ImageLoader.getImageListener(mIvRestaurantLogo, R.drawable.picture_moren, R.drawable.picture_moren));
//        mTvTitle.setText(trideBean.title);
//        mTvTime.setText("今天 " + TimeUtils.getTime(trideBean.time));
//        if (trideBean.payType == 3) {
//            mTvPrice.setText("" + (int) (trideBean.price * 2));
//        } else {
//            mTvPrice.setText("" + (int) (trideBean.price));
//        }
//        mOrderTotalPriceTv.setText("共计: " + (int) (trideBean.price) + "元");
    }

    private void getPayPurse() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_PERSON_WALLET, params, MyWalletResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_PERSON_WALLET, this, false).setTag(this);
        HashMap<String, String> param1 = new HashMap<>();
        param1.put("token", NSMTypeUtils.getMyToken());
//        param1.put("serviceId", String.valueOf(trideBean.serviceId));
//        param1.put("payType", String.valueOf(trideBean.payType));
        HttpLoader.post(ConstantsWhatNSM.URL_MAX_SERVICE_PRICE, param1, MaxServicePriceResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_SERVICE_PRICE, this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PERSON_WALLET
                && response instanceof MyWalletResponse) {
            MyWalletResponse myWalletResponse = (MyWalletResponse) response;
            int code = myWalletResponse.getCode();
            if (1 == code) {
                mPurse = myWalletResponse.getData().getPurse();
                mTvCashLeave.setText("￥ " + mPurse);
                if (mPurse == 0) {
                    mCbPayCash.setVisibility(View.VISIBLE);
                    mCbPayCash.setClickable(false);
                    mCbPayCash.setChecked(false);
                    mTvCashLeave.setText("￥ 0.00");
                } else {
                    mCbPayCash.setChecked(true);
                    mCbPayZhifibao.setChecked(false);
                    mCbPayWeixin.setChecked(false);
                    if (mPurse > trideBean.price) {
                        mCbPayZhifibao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                mCbPayZhifibao.setChecked(false);
                            }
                        });
                        mCbPayWeixin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                mCbPayWeixin.setChecked(false);
                            }
                        });
                    }
                }
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_SERVICE_PRICE
                && response instanceof MaxServicePriceResponse) {
            MaxServicePriceResponse maxServicePriceResponse = (MaxServicePriceResponse) response;
            int code = maxServicePriceResponse.getCode();
            if (1 == code) {
                maxPrice = maxServicePriceResponse.getData().getMaxPrice();
                mTvActivityLeave.setText("￥ " + maxPrice);
                if (maxPrice == 0) {
                    mCbPayActivity.setVisibility(View.VISIBLE);
                    mCbPayActivity.setClickable(false);
                    mCbPayActivity.setChecked(false);
                    mTvActivityLeave.setText("￥ 0.00");
                } else {
                    mCbPayActivity.setChecked(true);
                    mCbPayZhifibao.setChecked(false);
                    mCbPayWeixin.setChecked(false);
                }
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_USER_PAY_PASSWORD
                && response instanceof TradeSuccessResponse) {
            TradeSuccessResponse walletMaxPrice = (TradeSuccessResponse) response;
            int code = walletMaxPrice.getCode();
            if (1 == code) {
                mOrderPayNowBtn.setText("密码输入");
                mOrderPayNowBtn.setClickable(false);
                PayOrderDialog.showInputPwdDialog(this, new PayOrderInputListener() {
                    @Override
                    public void inputErrorListener() {
                        mOrderPayNowBtn.setText("立即支付");
                        mOrderPayNowBtn.setClickable(true);
                        setAllButtonEnable(true);
                    }

                    @Override
                    public void inputSuccessListener(String input) {
                        mOrderPayNowBtn.setText("正在支付");
                        mOrderPayNowBtn.setClickable(false);
                        setAllButtonEnable(true);
                        payMoneyTry(input);
                    }
                });
            } else {
                MyToast.showConterToast(this, "系统监测到您尚未设置支付密码,请设置后再来");
                mOrderPayNowBtn.setText("立即支付");
                mOrderPayNowBtn.setClickable(true);
                //跳转密码设置界面 (支付)
                ActivityUtils.startActivity(this, RegestPayPassWordActivity.class);
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ZHIFUBAO_PAY
                && response instanceof AliPaySignResponse) {
            AliPaySignResponse aliPaySignResponse = (AliPaySignResponse) response;
            if (1 == aliPaySignResponse.getCode()) {
                AliPaySignResponse.DataEntity.SignEntity signs = aliPaySignResponse.getData().getSign();
                String sign = signs.getSign();

                if (TextUtils.isEmpty(sign)) {
                    showToastError("支付出错,请重新支付");
                    HashMap params = new HashMap();
                    params.put("tradeNum", trideBean.tradeNum);
                    params.put("token", NSMTypeUtils.getMyToken());
                    HttpLoader.post(ConstantsWhatNSM.URL_ALIPAY_SIGN_ERROR, params, ActivtyPrice.class,
                            ConstantsWhatNSM.REQUEST_CODE_ALIPAY_SIGN_ERROR, this, false).setTag(this);
                    return;
                }
                try {
                    sign = URLEncoder.encode(sign, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                final String payInfo = signs.getOrderInfo();
                ALog.i("支付信息的 值 为: " + payInfo);
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(PayOrderOldActivity.this);
                        String result = alipay.pay(payInfo, true);
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_WEIXIN_PAY
                && response instanceof WxCreateTenpay) {
            wxCreateTenpay = (WxCreateTenpay) response;
            int code = wxCreateTenpay.getCode();
            if (1 == code) {
                sendPayReq(wxCreateTenpay);
            } else {
                showToastError("支付出错请重新支付");
                mOrderPayNowBtn.setText("立即支付");
                mOrderPayNowBtn.setClickable(true);
                setAllButtonEnable(true);
                HashMap params = new HashMap();
                params.put("tradeNum", trideBean.tradeNum);
                params.put("token", NSMTypeUtils.getMyToken());
                HttpLoader.post(ConstantsWhatNSM.URL_TENPAY_ERROR, params, ActivtyPrice.class,
                        ConstantsWhatNSM.REQUEST_CODE_TENPAY_ERROR, this, false).setTag(this);

            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PAY_TRADE_SUCCESS
                && response instanceof TradeSuccessResponse) {
            TradeSuccessResponse requestResponse = (TradeSuccessResponse) response;
            int code = requestResponse.getCode();
            if (1 == code) {
                if (isUserAgree) {
                    setResultAgree(true);
                } else {
                    MyToast.showConterToast(this, "支付成功");
//                    ActivityUtils.startActivityAndFinish(this, MainActivity.class);
                    finish();
                }
            } else {
                showToastInfo(requestResponse.getMessage());
                mOrderPayNowBtn.setText("立即支付");
                mOrderPayNowBtn.setClickable(true);
                setAllButtonEnable(true);
                HashMap params = new HashMap();
                params.put("tradeNum", trideBean.tradeNum);
                HttpLoader.post(ConstantsWhatNSM.URL_ALIPAY_SIGN_ERROR, params, ActivtyPrice.class,
                        ConstantsWhatNSM.REQUEST_CODE_ALIPAY_SIGN_ERROR, this, false).setTag(this);
                dialog.dismiss();
            }
        }

//        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_USER_PAY_PASSWORD_RIGHT
//                && response instanceof TradeSuccessResponse) {
//            TradeSuccessResponse requestResponse = (TradeSuccessResponse) response;
//            int code = requestResponse.getCode();
//            if (1 == code) {
//                successPay();
//            } else {
//                MyToast.show(this, "您输入的支付密码是错误的,请重新输入");
//                mOrderPayNowBtn.setText("立即支付");
//                mOrderPayNowBtn.setClickable(true);
//                setAllButtonEnable(true);
//            }
//        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVITY_PRICE
                && response instanceof ActivtyPrice) {
            ActivtyPrice activtyPrice = (ActivtyPrice) response;
            int code = activtyPrice.getCode();
            if (1 == code) {
                actualPurse = activtyPrice.getData().getActualPurse();
                if (actualPurse <= 0) {
                    if (isUserAgree) {
                        setResultAgree(true);
                    } else {
                        MyToast.show(this, "支付成功");
                        finish();
                    }
                } else {
                    if (mCbPayWeixin.isChecked()) {
                        payForWeixin();
                    } else {
                        payForZhiFuBao();
                    }
                }
            } else {
                showToastInfo(activtyPrice.getMessage());
                mOrderPayNowBtn.setText("立即支付");
                mOrderPayNowBtn.setClickable(true);
                setAllButtonEnable(true);
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    private void pay() {
        setAllButtonEnable(false);
        mOrderPayNowBtn.setText("正在支付");
        mOrderPayNowBtn.setClickable(false);
        if ((mCbPayCash.isChecked() || mCbPayActivity.isChecked())) {
            if (mCbPayZhifibao.isChecked() || mCbPayWeixin.isChecked()) {
                //验证是否有支付密码
                payForPuarse();
            } else {
                if (maxPrice + mPurse >= trideBean.price) {
                    //余额足够
                    payForPuarse();
                } else {
                    showToastWarning("余额不足");
                    setAllButtonEnable(true);
                    mOrderPayNowBtn.setClickable(true);
                    mOrderPayNowBtn.setText("立即支付");
                }
            }
        } else {
            //没有选择余额支付或者活动支付,即只选择了支付宝或者微信支付
            if (mCbPayZhifibao.isChecked()) {
                //支付宝支付
                payForZhiFuBao();
            } else if (mCbPayWeixin.isChecked()) {
                payForWeixin();
            } else {
                showToastInfo("您尚未选择任何支付方式");
                setAllButtonEnable(true);
                mOrderPayNowBtn.setClickable(true);
                mOrderPayNowBtn.setText("立即支付");
            }
        }
    }

    private void payForPuarse() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_USER_PAY_PASSWORD, params, TradeSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_USER_PAY_PASSWORD, this, false).setTag(this);
    }

    private void payMoneyTry(String input) {
        if (mCbPayActivity.isChecked() && !mCbPayCash.isChecked() && !mCbPayZhifibao.isChecked() && !mCbPayWeixin.isChecked()) {
            showToastInfo("活动金额不能唯一支付");
        } else {
            HashMap params = new HashMap();
            params.put("token", NSMTypeUtils.getMyToken());
            params.put("tradeNum", trideBean.tradeNum);
            params.put("paypassword", MD5Utils.addToMD5(input));
            if (mCbPayCash.isChecked()) {
                params.put("userpurse", trideBean.price - maxPrice + "");
            } else {
                params.put("userpurse", "0.0");
            }
            if (mCbPayActivity.isChecked()) {
                params.put("activitypurse", maxPrice + "");
            } else {
                params.put("activitypurse", "0.0");
            }
            HttpLoader.post(ConstantsWhatNSM.URL_ACTIVITY_PRICE, params, ActivtyPrice.class,
                    ConstantsWhatNSM.REQUEST_CODE_ACTIVITY_PRICE, this, false).setTag(this);
        }
    }

    private void payForZhiFuBao() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        //活动号
        params.put("out_trade_no", trideBean.tradeNum);
        //服务名称
//        params.put("subject", trideBean.restrantantName);
        //服务介绍
        params.put("body", trideBean.title);
        //支付金额,price
        if (actualPurse != 0) {
            params.put("total_fee", String.valueOf(actualPurse));
        } else {
            params.put("total_fee", String.valueOf(trideBean.price));
        }

        //服务地址
        params.put("service", "mobile.securitypay.pay");
        //支付方式
        params.put("payment_type", "1");
        //编码方式
        params.put("_input_charset", "utf-8");
        //最迟支付时间
        params.put("it_b_pay", "30m");

        HttpLoader.post(ConstantsWhatNSM.URL_ZHIFUBAO_PAY, params, AliPaySignResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ZHIFUBAO_PAY, this).setTag(this);
    }

    private void surePaySuccess() {
//        HashMap<String, String> params = new HashMap<>();
//        params.put("tradeNum", trideBean.tradeNum);
//        params.put("token", NSMTypeUtils.getMyToken());
//        params.put("payType", "alipay");
//        HttpLoader.post(ConstantsWhatNSM.URL_PAY_TRADE_SUCCESS, params, TradeSuccessResponse.class,
//                ConstantsWhatNSM.REQUEST_CODE_PAY_TRADE_SUCCESS, this, false).setTag(this);
    }

    private void payForWeixin() {
//        HashMap params = new HashMap();
//        imei = AppManager.getIMEI();
//        tradeNum = trideBean.tradeNum;
//        params.put("tradeNum", tradeNum);
//        params.put("deviceInfo", imei);
//        params.put("token", NSMTypeUtils.getMyToken());
//        HttpLoader.post(ConstantsWhatNSM.URL_WEIXIN_PAY, params, WxCreateTenpay.class,
//                ConstantsWhatNSM.REQUEST_CODE_WEIXIN_PAY, this).setTag(this);
    }

    private void sendPayReq(WxCreateTenpay wxCreateTenpay) {
        api = WXAPIFactory.createWXAPI(this, wxCreateTenpay.getData().getAppid(), false);
        // 将该app注册到微信
        api.registerApp(wxCreateTenpay.getData().getAppid());
        if (!api.isWXAppInstalled()) {
            MyToast.showConterToast(this, "未安装微信，请安装后重试");
            setAllButtonEnable(true);
            mOrderPayNowBtn.setText("立即支付");
            mOrderPayNowBtn.setClickable(true);
            return;
        } else {
            boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
            if (isPaySupported == false) {
                MyToast.showConterToast(this, "微信版本过低，请更新后重试");
                setAllButtonEnable(true);
                mOrderPayNowBtn.setText("立即支付");
                mOrderPayNowBtn.setClickable(true);
                return;
            }
        }
        PayReq req = new PayReq();
        req.appId = wxCreateTenpay.getData().getAppid();
        req.partnerId = wxCreateTenpay.getData().getPartnerid();
        req.prepayId = wxCreateTenpay.getData().getPrepayid();
        req.nonceStr = wxCreateTenpay.getData().getNoncestr();
        req.timeStamp = wxCreateTenpay.getData().getTimestamp() + "";
        req.packageValue = wxCreateTenpay.getData().getPackageX();
        req.sign = wxCreateTenpay.getData().getSign();
        api.sendReq(req);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(2000);
                    mHandler.sendEmptyMessage(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

//    private void successPay() {
//        HashMap<String, String> params = new HashMap<>();
//        params.put("tradeNum", trideBean.tradeNum);
//        params.put("payType", "pursepay");
//        params.put("token", App.SP.getString("token", null));
//        mOrderPayNowBtn.setText("正在完成");
//        mOrderPayNowBtn.setClickable(false);
//        HttpLoader.post(ConstantsWhatNSM.URL_PAY_TRADE_SUCCESS, params, TradeSuccessResponse.class,
//                ConstantsWhatNSM.REQUEST_CODE_PAY_TRADE_SUCCESS, this, false).setTag(this);
//    }

    private void setAllButtonEnable(boolean b) {
        mCbPayCash.setEnabled(b);
        mCbPayActivity.setEnabled(b);
        mCbPayZhifibao.setEnabled(b);
        mCbPayWeixin.setEnabled(b);
    }

    public void onEventMainThread(WxPayResult event) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("提示");
//        if (event.isResult() == true) {
//            builder.setMessage("微信支付确认中");
//            dialog = builder.show();
//            HashMap params = new HashMap();
//            params.put("deviceInfo", imei);
//            params.put("tradeNum", tradeNum);
//            params.put("payType", "tenpay");
//            params.put("token", NSMTypeUtils.getMyToken());
//            HttpLoader.post(ConstantsWhatNSM.URL_PAY_TRADE_SUCCESS, params, TradeSuccessResponse.class,
//                    ConstantsWhatNSM.REQUEST_CODE_PAY_TRADE_SUCCESS, this, false).setTag(this);
//        } else {
//            builder.setMessage("微信支付失败");
//            builder.show();
//            HashMap params = new HashMap();
//            params.put("tradeNum", this.trideBean.tradeNum);
//            params.put("token", App.SP.getString("token", null));
//            HttpLoader.post(ConstantsWhatNSM.URL_TENPAY_ERROR, params, ActivtyPrice.class,
//                    ConstantsWhatNSM.REQUEST_CODE_TENPAY_ERROR, this, false).setTag(this);
//            setAllButtonEnable(true);
//            mOrderPayNowBtn.setClickable(true);
//            mOrderPayNowBtn.setText("立即支付");
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpLoader.cancelRequest(this);
        EventBus.getDefault().unregister(this);
    }

    private void setResultAgree(boolean b) {
        setResult(b ? RESULT_OK : RESULT_CANCELED);
        finish();
    }

    /**
     * 开启确认活动界面的方法
     *
     * @param context   一个可以开启界面的上下文
     * @param trideBean 一个活动类,里面涵盖了所有支付界面需要必要的参数
     */
    public static void startPayOrderAct(Context context, TrideBean trideBean) {
        Intent intent = new Intent(context, PayOrderOldActivity.class);
        EventBus.getDefault().postSticky(trideBean);
        context.startActivity(intent);
    }
}
