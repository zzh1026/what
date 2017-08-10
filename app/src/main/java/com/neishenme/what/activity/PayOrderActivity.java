package com.neishenme.what.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
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
import com.neishenme.what.nsminterface.PayCancelListener;
import com.neishenme.what.nsminterface.PayOrderInputListener;
import com.neishenme.what.utils.AppManager;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.PayOrderConfig;
import com.neishenme.what.utils.PayResult;
import com.neishenme.what.utils.TimeUtils;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.MD5Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * 作者：zhaozh create on 2016/12/16 14:14
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是  新的支付界面
 * .
 * 其作用是 :
 */
public class PayOrderActivity extends BaseActivity implements HttpLoader.ResponseListener, View.OnClickListener {
    /**
     * 这个是建议的开启支付界面的请求码,但不限制,使用这个可以方便一点,比如一个界面只有一个startactivityforresult
     *
     * @see InviteJoinerDetailActivity ,加入界面申请加入只有一个为了结果请求的不会发生冲突,建议用该请求码
     * @see UserDetailActivity ,用户中心同意TA的时候也只有一个为了结果请求的 ,所以也不会冲突,建议用该请求码
     * @see MainActivity ,主界面在急速发布或者普通发布的时候有两种情况,并且主界面可以自己付费加入,有三个进入该界面的入口,为了
     * 不产生冲突,就建议主界面自己确定请求码
     */
    public static final int REQUEST_CODE_START_PAY = 0;

    private ImageView mPayOrderCancel;
    private TextView mPayOrderInviteType;
    private ImageView mPayOrderLogo;
    private TextView mPayOrderTitle;
    private TextView mPayOrderTime;
    private TextView mPayOrderPrice;
    private LinearLayout mPayOrderZhifubao;
    private CheckBox mPayOrderZhifubaoCheck;
    private LinearLayout mPayOrderWeixin;
    private CheckBox mPayOrderWeixinCheck;
    private LinearLayout mPayOrderPure;
    private ImageView mPayOrderPurseCheck;
    private TextView mPayOrderForgetPayPasswrod;
    private TextView mPayOrderTotalPrice;
    private Button mPayOrderStartPay;

    private LinearLayout mPayOrderPurseActive;

    private LinearLayout mPayOrderPayCash;
    private TextView mTvPayOrderPayCash;
    private CheckBox mPayOrderCashCheck;

    private LinearLayout payOrderActiveLayout;
    private LinearLayout mPayOrderPayActive;
    private TextView mTvPayOrderPayActive;
    private CheckBox mPayOrderActiveCheck;

    private boolean isShowPurse = false;    //标记是否展示了余额支付

    private boolean canCheckClick = true;    //标记checkBox是否可以点击,当点击支付后就不能点击了

    private TrideBean trideBean;

    //第三方应该支付的钱数量
//    private double actualPurse = 0;

    private double casePrice = 0;   //标记现金余额的钱
    private double activePrice = 0;   //标记活动余额的钱


    private WxCreateTenpay wxCreateTenpay;

//    Dialog dialog;

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
                        surePaySuccess(PayOrderConfig.MAKE_SURE_ALI_PAY);
                    } else {
                        if (TextUtils.equals(resultStatus, "8000")) {
                            showToastInfo("支付结果确认中");
                            surePaySuccess(PayOrderConfig.MAKE_SURE_ALI_PAY);
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ALog.i("错误原因是 : " + resultStatus);
                            showToastError("支付失败");
                            setAliPayError();
                        }
                    }
                    break;
                }
                case 2:
                    setBtnState(PayOrderConfig.BTN_STATE_CAN_PAY, PayOrderConfig.BTN_CAN_CLICK);
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_pay_order;
    }

    @Override
    protected void initView() {
        mPayOrderCancel = (ImageView) findViewById(R.id.pay_order_cancel);

        mPayOrderInviteType = (TextView) findViewById(R.id.pay_order_invite_type);
        mPayOrderLogo = (ImageView) findViewById(R.id.pay_order_logo);
        mPayOrderTitle = (TextView) findViewById(R.id.pay_order_title);
        mPayOrderTime = (TextView) findViewById(R.id.pay_order_time);
        mPayOrderPrice = (TextView) findViewById(R.id.pay_order_price);

        mPayOrderZhifubao = (LinearLayout) findViewById(R.id.pay_order_zhifubao);
        mPayOrderZhifubaoCheck = (CheckBox) findViewById(R.id.pay_order_zhifubao_check);

        mPayOrderWeixin = (LinearLayout) findViewById(R.id.pay_order_weixin);
        mPayOrderWeixinCheck = (CheckBox) findViewById(R.id.pay_order_weixin_check);

        mPayOrderPure = (LinearLayout) findViewById(R.id.pay_order_pure);
        mPayOrderPurseCheck = (ImageView) findViewById(R.id.pay_order_purse_check);

        mPayOrderForgetPayPasswrod = (TextView) findViewById(R.id.pay_order_forget_pay_passwrod);

        mPayOrderTotalPrice = (TextView) findViewById(R.id.pay_order_total_price);
        mPayOrderStartPay = (Button) findViewById(R.id.pay_order_start_pay);

        mPayOrderPurseActive = (LinearLayout) findViewById(R.id.pay_order_purse_active);
        mPayOrderPayCash = (LinearLayout) findViewById(R.id.pay_order_pay_cash);
        mPayOrderCashCheck = (CheckBox) findViewById(R.id.pay_order_cash_check);
        mTvPayOrderPayCash = (TextView) findViewById(R.id.tv_pay_order_pay_cash);

        payOrderActiveLayout = (LinearLayout) findViewById(R.id.pay_order_active_layout);
        mPayOrderPayActive = (LinearLayout) findViewById(R.id.pay_order_pay_active);
        mPayOrderActiveCheck = (CheckBox) findViewById(R.id.pay_order_active_check);
        mTvPayOrderPayActive = (TextView) findViewById(R.id.tv_pay_order_pay_active);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_order_cancel:     //返回
//                new AlertDialog.Builder(this).setCancelable(false).setMessage("确定离开么?")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                setResult(false);
//                            }
//                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).show();
                PayOrderDialog.showCancel(this, new PayCancelListener() {
                    @Override
                    public void ok() {
                        setResult(false);
                    }

                    @Override
                    public void cancel() {
                    }
                });
                break;
            case R.id.pay_order_zhifubao:   //支付宝
                if (canCheckClick) {
                    mPayOrderZhifubaoCheck.setChecked(!mPayOrderZhifubaoCheck.isChecked());
                    mPayOrderWeixinCheck.setChecked(false);
                    if (purseCanPay()) {
                        mPayOrderCashCheck.setChecked(false);
                        if (activeCanPay()) {
                            mPayOrderActiveCheck.setChecked(false);
                        }
                    }
                }
                break;
            case R.id.pay_order_weixin:     //微信
                if (canCheckClick) {
                    mPayOrderWeixinCheck.setChecked(!mPayOrderWeixinCheck.isChecked());
                    mPayOrderZhifubaoCheck.setChecked(false);
                    if (purseCanPay()) {
                        mPayOrderCashCheck.setChecked(false);
                        if (activeCanPay()) {
                            mPayOrderActiveCheck.setChecked(false);
                        }
                    }
                }
                break;
            case R.id.pay_order_pure:       //余额支付
                if (canCheckClick) {
                    if (isShowPurse) {  //正在展示
                        mPayOrderPurseActive.setVisibility(View.GONE);
                        isShowPurse = false;
                        mPayOrderPurseCheck.setImageResource(R.drawable.pay_order_purse_bg);
                    } else {
                        mPayOrderPurseActive.setVisibility(View.VISIBLE);
                        isShowPurse = true;
                        mPayOrderPurseCheck.setImageResource(R.drawable.pay_order_purse2_bg);
                    }
                }
                break;
            case R.id.pay_order_pay_cash:     //现金余额
                if (canCheckClick) {
                    mPayOrderCashCheck.setChecked(!mPayOrderCashCheck.isChecked());
                    if (purseCanPay()) {
                        mPayOrderWeixinCheck.setChecked(false);
                        mPayOrderZhifubaoCheck.setChecked(false);
                        if (activeCanPay()) {
                            mPayOrderActiveCheck.setChecked(false);
                        }
                    }
                }
                break;
            case R.id.pay_order_pay_active:     //活动余额
                if (canCheckClick) {
                    mPayOrderActiveCheck.setChecked(!mPayOrderActiveCheck.isChecked());
                    if (activeCanPay()) {   //活动余额不够
                        mPayOrderWeixinCheck.setChecked(false);
                        mPayOrderZhifubaoCheck.setChecked(false);
                        mPayOrderCashCheck.setChecked(false);
                    } else if (purseCanPay()) { //活动余额不够但是加上这个够
                        mPayOrderWeixinCheck.setChecked(false);
                        mPayOrderZhifubaoCheck.setChecked(false);
                    }
                }
                break;
            case R.id.pay_order_forget_pay_passwrod:    //忘记密码
                ActivityUtils.startActivity(this, ResetPayPwdActivity.class);
                break;
            case R.id.pay_order_start_pay:  //立即支付
                pay();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initListener() {
        mPayOrderCancel.setOnClickListener(this);
        mPayOrderZhifubao.setOnClickListener(this);
        mPayOrderWeixin.setOnClickListener(this);
        mPayOrderPure.setOnClickListener(this);
        mPayOrderForgetPayPasswrod.setOnClickListener(this);
        mPayOrderStartPay.setOnClickListener(this);
        mPayOrderPayCash.setOnClickListener(this);
        mPayOrderPayActive.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        trideBean = EventBus.getDefault().getStickyEvent(TrideBean.class);
        //分发界面基础信息
        disPathBaseInfo();

        //获取现金余额
        getCasePayPurse();

        if (trideBean.shouldActivityPurse && trideBean.type == 0) {
            //获取活动最大可用余额信息
            getActivePayPurse();
            payOrderActiveLayout.setVisibility(View.VISIBLE);
        } else {
            payOrderActiveLayout.setVisibility(View.GONE);
            mPayOrderActiveCheck.setChecked(false);
        }
    }

    private void disPathBaseInfo() {
//        mPayOrderInviteType.setText(trideBean.type == 1 ? "极速发布" : "专属发布");
        mPayOrderInviteType.setText("发布活动");
        if (TextUtils.isEmpty(trideBean.logo)) {
            mPayOrderLogo.setImageResource(R.drawable.picture_moren);
        } else {
            HttpLoader.getImageLoader().get(trideBean.logo,
                    ImageLoader.getImageListener(mPayOrderLogo, R.drawable.picture_moren, R.drawable.picture_moren));
        }
        mPayOrderTitle.setText(trideBean.title);
        mPayOrderTime.setText("今天 " + TimeUtils.getTime(trideBean.time));
        mPayOrderPrice.setText("" + (trideBean.price));
        mPayOrderTotalPrice.setText("共计: " + (trideBean.price) + "元");
    }

    private void getCasePayPurse() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_PERSON_WALLET, params, MyWalletResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_PERSON_WALLET, this, false).setTag(this);
    }

    private void getActivePayPurse() {
        if (trideBean.serviceId == 0) {
            payOrderActiveLayout.setVisibility(View.GONE);
            mPayOrderActiveCheck.setChecked(false);
            return;
        }
        HashMap<String, String> param1 = new HashMap<>();
        param1.put("token", NSMTypeUtils.getMyToken());
        param1.put("serviceId", String.valueOf(trideBean.serviceId));
//        param1.put("payType", "1");
        HttpLoader.post(ConstantsWhatNSM.URL_MAX_SERVICE_PRICE, param1, MaxServicePriceResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_SERVICE_PRICE, this, false).setTag(this);
    }

    //判断余额支付是否可以支付
    private boolean purseCanPay() {
        if (mPayOrderActiveCheck.isChecked()) {
            return trideBean.price - casePrice - activePrice <= 0;
        } else {
            return trideBean.price - casePrice <= 0;
        }
    }

    //判断现金余额是否够支付
    private boolean caseCanPay() {
        return mPayOrderCashCheck.isChecked() && trideBean.price <= casePrice;
    }

    //判断活动余额是否够支付
    private boolean activeCanPay() {
        return mPayOrderActiveCheck.isChecked() && trideBean.price <= activePrice;
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PERSON_WALLET
                && response instanceof MyWalletResponse) {
            MyWalletResponse myWalletResponse = (MyWalletResponse) response;
            int code = myWalletResponse.getCode();
            //获取现金余额
            if (1 == code) {
                casePrice = myWalletResponse.getData().getPurse();
                mTvPayOrderPayCash.setText("￥ " + casePrice);
                if (casePrice == 0) {
                    mPayOrderPayCash.setClickable(false);
                    mTvPayOrderPayCash.setText("￥ 0.00");
                    mPayOrderCashCheck.setChecked(false);
                } else {
                    mPayOrderPayCash.setClickable(true);
                }
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_SERVICE_PRICE
                && response instanceof MaxServicePriceResponse) {
            MaxServicePriceResponse maxServicePriceResponse = (MaxServicePriceResponse) response;
            int code = maxServicePriceResponse.getCode();
            //获取最大可支付活动余额;
            if (1 == code) {
                activePrice = maxServicePriceResponse.getData().getMaxPrice();
                mTvPayOrderPayActive.setText("￥ " + activePrice);
                if (activePrice == 0) {
                    mPayOrderPayActive.setClickable(false);
                    mPayOrderActiveCheck.setChecked(false);
                    mTvPayOrderPayActive.setText("￥ 0.00");
                } else {
                    mPayOrderPayActive.setClickable(true);
                }
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_USER_PAY_PASSWORD
                && response instanceof TradeSuccessResponse) {
            TradeSuccessResponse walletMaxPrice = (TradeSuccessResponse) response;
            int code = walletMaxPrice.getCode();
            if (1 == code) {
                setBtnState(PayOrderConfig.BTN_STATE_INPUT_PASSWORD, PayOrderConfig.BTN_NO_CLICK);
                PayOrderDialog.showInputPwdDialog(this, new PayOrderInputListener() {
                    @Override
                    public void inputErrorListener() {
                        setBtnState(PayOrderConfig.BTN_STATE_CAN_PAY, PayOrderConfig.BTN_CAN_CLICK);
                    }

                    @Override
                    public void inputSuccessListener(String input) {
                        setBtnState(PayOrderConfig.BTN_STATE_IS_PAYING, PayOrderConfig.BTN_NO_CLICK);
                        payMoneyTry(input);
                    }
                });
            } else {
                showToastInfo("系统监测到您尚未设置支付密码,请设置后再来");
                setBtnState(PayOrderConfig.BTN_STATE_CAN_PAY, PayOrderConfig.BTN_CAN_CLICK);
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
                    setAliPayError();
                    return;
                }
                try {
                    sign = URLEncoder.encode(sign, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                final String payInfo = signs.getOrderInfo();
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(PayOrderActivity.this);
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
                setTenPayError();
            }
        }

        //微信和支付宝验证是否支付成功的接口
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PAY_TRADE_SUCCESS
                && response instanceof TradeSuccessResponse) {
            TradeSuccessResponse requestResponse = (TradeSuccessResponse) response;
            int code = requestResponse.getCode();
            if (1 == code) {
                setResult(true);
            } else {
                showToastInfo(requestResponse.getMessage());
                setAliPayError();
//                if (dialog != null && dialog.isShowing())
//                    dialog.dismiss();
                setResult(false);
            }
        }

        //余额支付
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVITY_PRICE
                && response instanceof ActivtyPrice) {
            ActivtyPrice activtyPrice = (ActivtyPrice) response;
            int code = activtyPrice.getCode();
            if (1 == code) {
                double actualPurse = activtyPrice.getData().getActualPurse();
                if (actualPurse <= 0) {
                    setResult(true);
                } else {
                    if (mPayOrderZhifubaoCheck.isChecked()) {
                        payForZhiFuBao();
                    } else {
                        payForWeixin();
                    }
                }
            } else {
                showToastInfo(activtyPrice.getMessage());
                setBtnState(PayOrderConfig.BTN_STATE_CAN_PAY, PayOrderConfig.BTN_CAN_CLICK);
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PAY_TRADE_SUCCESS) {
            showToastWarning("确认支付时服务器异常,支付的钱会退回钱包");
            setResult(false);
        }
    }

    private void pay() {
        setBtnState(PayOrderConfig.BTN_STATE_IS_PAYING, PayOrderConfig.BTN_NO_CLICK);
        if ((mPayOrderCashCheck.isChecked() || mPayOrderActiveCheck.isChecked())) {
            if (mPayOrderZhifubaoCheck.isChecked() || mPayOrderWeixinCheck.isChecked()) {
                //验证是否有支付密码
                payForPuarse();
            } else {    //只选中了活动余额或者现金余额
                if (purseCanPay()) {    //这里不再限制活动余额不能唯一支付
//                    if (!mPayOrderCashCheck.isChecked()) {  //只选中了活动余额
//                        showToast("活动金额不能唯一支付");
//                        setBtnState(PayOrderConfig.BTN_STATE_CAN_PAY, PayOrderConfig.BTN_CAN_CLICK);
//                    } else {
                    //余额足够,验证支付密码
                    payForPuarse();
//                    }
                } else {
                    showToastInfo("余额不足");
                    setBtnState(PayOrderConfig.BTN_STATE_CAN_PAY, PayOrderConfig.BTN_CAN_CLICK);
                }
            }
        } else {
            //没有选择余额支付或者活动支付,即只选择了支付宝或者微信支付
            if (mPayOrderZhifubaoCheck.isChecked()) {
                //支付宝支付
                payForZhiFuBao();
            } else if (mPayOrderWeixinCheck.isChecked()) {
                payForWeixin();
            } else {
                showToastInfo("您尚未选择任何支付方式");
                setBtnState(PayOrderConfig.BTN_STATE_CAN_PAY, PayOrderConfig.BTN_CAN_CLICK);
            }
        }
    }

    private void setBtnState(String btnState, boolean canClick) {
        canCheckClick = canClick;   //三个选择框的选择与下方的点击支付的按钮是同步的
        mPayOrderStartPay.setText(btnState);
        mPayOrderStartPay.setClickable(canClick);
    }

    //验证是否有密码
    private void payForPuarse() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_USER_PAY_PASSWORD, params, TradeSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_USER_PAY_PASSWORD, this, false).setTag(this);
    }

    //余额支付
    private void payMoneyTry(String input) {
        HashMap params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("tradeNum", trideBean.tradeNum);
        params.put("paypassword", MD5Utils.addToMD5(input));

        if (mPayOrderActiveCheck.isChecked()) {
            params.put("activitypurse", activeCanPay() ? trideBean.price + "" : activePrice + "");
        } else {
            params.put("activitypurse", "0.0");
        }

        if (mPayOrderCashCheck.isChecked()) {
            String price;
            if (purseCanPay()) {
                if (activeCanPay()) {   //活动余额支付为主
                    price = "0.0";
                } else {
                    if (mPayOrderActiveCheck.isChecked()) {
                        price = String.valueOf(trideBean.price - activePrice);
                    } else {
                        price = trideBean.price + "";
                    }
                }
            } else {
                price = casePrice + "";
            }
            params.put("userpurse", price);
        } else {
            params.put("userpurse", "0.0");
        }
        HttpLoader.post(ConstantsWhatNSM.URL_ACTIVITY_PRICE, params, ActivtyPrice.class,
                ConstantsWhatNSM.REQUEST_CODE_ACTIVITY_PRICE, this, false).setTag(this);
    }

    //支付宝支付
    private void payForZhiFuBao() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("out_trade_no", trideBean.tradeNum);     //活动号
//        params.put("subject", trideBean.type == 1 ? "极速发布" : "专属发布");   //服务名称
//        params.put("body", trideBean.title);    //服务介绍
//        if (actualPurse != 0) {     //支付金额,price
//            params.put("total_fee", String.valueOf(actualPurse));
//        } else {
//            params.put("total_fee", String.valueOf(trideBean.price));
//        }
//        params.put("service", "mobile.securitypay.pay");    //服务地址
//        params.put("payment_type", "1");    //支付方式
//        params.put("_input_charset", "utf-8");  //编码方式
//        params.put("it_b_pay", "30m");  //最迟支付时间
        HttpLoader.post(ConstantsWhatNSM.URL_ZHIFUBAO_PAY, params, AliPaySignResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ZHIFUBAO_PAY, this).setTag(this);
    }

    //微信支付
    private void payForWeixin() {
        HashMap params = new HashMap();
        params.put("tradeNum", trideBean.tradeNum);
        params.put("deviceInfo", AppManager.getIMEI());
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_WEIXIN_PAY, params, WxCreateTenpay.class,
                ConstantsWhatNSM.REQUEST_CODE_WEIXIN_PAY, this).setTag(this);
    }

    //支付宝和微信支付成功调用
    private void surePaySuccess(int payType) {
        HashMap<String, String> params = new HashMap<>();
        if (payType == PayOrderConfig.MAKE_SURE_ALI_PAY) {
            params.put("payType", "alipay");
        } else {
//            params.put("deviceInfo", AppManager.getIMEI());
            params.put("payType", "tenpay");
        }
        params.put("tradeNum", trideBean.tradeNum);
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_PAY_TRADE_SUCCESS, params, TradeSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_PAY_TRADE_SUCCESS, this, false).setTag(this);
    }

    //支付宝支付失败调用
    private void setAliPayError() {
        HashMap params = new HashMap();
        params.put("tradeNum", trideBean.tradeNum);
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_ALIPAY_SIGN_ERROR, params, ActivtyPrice.class,
                ConstantsWhatNSM.REQUEST_CODE_ALIPAY_SIGN_ERROR, this, false).setTag(this);
        setBtnState(PayOrderConfig.BTN_STATE_CAN_PAY, PayOrderConfig.BTN_CAN_CLICK);
    }

    //微信支付失败调用
    private void setTenPayError() {
        HashMap params = new HashMap();
        params.put("tradeNum", trideBean.tradeNum);
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_TENPAY_ERROR, params, ActivtyPrice.class,
                ConstantsWhatNSM.REQUEST_CODE_TENPAY_ERROR, this, false).setTag(this);
        setBtnState(PayOrderConfig.BTN_STATE_CAN_PAY, PayOrderConfig.BTN_CAN_CLICK);
    }

    private void sendPayReq(WxCreateTenpay wxCreateTenpay) {
        api = WXAPIFactory.createWXAPI(this, wxCreateTenpay.getData().getAppid(), false);
        // 将该app注册到微信
        api.registerApp(wxCreateTenpay.getData().getAppid());
        if (!api.isWXAppInstalled()) {
            showToastWarning("未安装微信，请安装后重试");
            setBtnState(PayOrderConfig.BTN_STATE_CAN_PAY, PayOrderConfig.BTN_CAN_CLICK);
            return;
        } else {
            boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
            if (isPaySupported == false) {
                showToastWarning("微信版本过低，请更新后重试");
                setBtnState(PayOrderConfig.BTN_STATE_CAN_PAY, PayOrderConfig.BTN_CAN_CLICK);
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

    public void onEventMainThread(WxPayResult event) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("提示");
        if (event.isResult() == true) {
//            builder.setMessage("微信支付确认中");
//            dialog = builder.show();
            surePaySuccess(PayOrderConfig.MAKE_SURE_TEN_PAY);
        } else {
//            builder.setMessage("微信支付失败");
//            builder.show();
            showToastError("支付失败");
            setTenPayError();
        }
    }

    /**
     * 通过该方法来设置返回的结果
     *
     * @param success 支付是否成功
     */
    private void setResult(boolean success) {
        setResult(success ? RESULT_OK : RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpLoader.cancelRequest(this);
        EventBus.getDefault().unregister(this);
    }

    /**
     * 开启确认活动界面的方法
     *
     * @param requestCode 为了更加方便,支付界面以后同一使用startforresult来开启,只需要将支付是否成功传出即可
     * @param context     一个可以开启界面的上下文
     * @param trideBean   一个活动类,里面涵盖了所有支付界面需要必要的参数
     */
    public static void startPayOrderActForResult(Activity context, int requestCode, TrideBean trideBean) {
        Intent intent = new Intent(context, PayOrderActivity.class);
        EventBus.getDefault().postSticky(trideBean);
        context.startActivityForResult(intent, requestCode);
    }

//    public static void startPayOrderActForResult(Context context, TrideBean trideBean) {
//        Intent intent = new Intent(context, PayOrderActivity.class);
//        EventBus.getDefault().postSticky(trideBean);
//        context.startActivity(intent);
//    }
}
