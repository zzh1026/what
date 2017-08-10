package com.neishenme.what.activity;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.AliPaySignResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.TradeSuccessResponse;
import com.neishenme.what.bean.WxCreateTenpay;
import com.neishenme.what.bean.WxPayResult;
import com.neishenme.what.eventbusobj.ActivePayTrideBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.AppManager;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.PayOrderConfig;
import com.neishenme.what.utils.PayResult;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.seny.android.utils.ALog;
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
 * 这是一个 加入活动后的支付界面, 在一元购活动和网约女明星使用到了
 * .
 * 其作用是 :
 */
public class ActiveJoinPayActivity extends BaseActivity implements HttpLoader.ResponseListener {
    private ImageView mIvBack;
    private TextView mActivePayActivename;
    private ImageView mActivePayUserlogo;
    private TextView mActivePayActiveTitle;
    private TextView mActivePayTicketNum;
    private TextView mActivePayPrice;
    private RelativeLayout mRlPayZhifubao;
    private CheckBox mCbPayZhifibao;
    private RelativeLayout mRlPayWeixin;
    private CheckBox mCbPayWeixin;
    private TextView mOrderTotalPriceTv;
    private Button mOrderPayNowBtn;

    String imei;
    private WxCreateTenpay wxCreateTenpay;

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
                    if (TextUtils.equals(resultStatus, "9000") || TextUtils.equals(resultStatus, "8000")) {
                        surePaySuccess(PayOrderConfig.MAKE_SURE_ALI_PAY);
                    } else {
                        ALog.i("错误原因是 : " + resultStatus);
                        showToastError("支付失败");
                        mOrderPayNowBtn.setClickable(true);
                        mOrderPayNowBtn.setText("立即支付");
                        setAllButtonEnable(true);
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
    private ActivePayTrideBean activePayTrideBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected int initContentView() {
        return R.layout.activity_active_join_pay;
    }

    @Override
    protected void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);

        mActivePayActivename = (TextView) findViewById(R.id.active_pay_activename);

        mActivePayUserlogo = (ImageView) findViewById(R.id.active_pay_userlogo);
        mActivePayActiveTitle = (TextView) findViewById(R.id.active_pay_active_title);
        mActivePayTicketNum = (TextView) findViewById(R.id.active_pay_ticket_num);
        mActivePayPrice = (TextView) findViewById(R.id.active_pay_price);

        mRlPayZhifubao = (RelativeLayout) findViewById(R.id.rl_pay_zhifubao);
        mCbPayZhifibao = (CheckBox) findViewById(R.id.cb_pay_zhifibao);
        mRlPayWeixin = (RelativeLayout) findViewById(R.id.rl_pay_weixin);
        mCbPayWeixin = (CheckBox) findViewById(R.id.cb_pay_weixin);

        mOrderTotalPriceTv = (TextView) findViewById(R.id.order_total_price_tv);
        mOrderPayNowBtn = (Button) findViewById(R.id.order_pay_now_btn);
    }

    @Override
    protected void initListener() {
        mRlPayWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCbPayWeixin.setChecked(!mCbPayWeixin.isChecked());
                mCbPayZhifibao.setChecked(false);
            }
        });
        mRlPayZhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCbPayZhifibao.setChecked(!mCbPayZhifibao.isChecked());
                mCbPayWeixin.setChecked(false);
            }
        });

        mOrderPayNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay();
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
        EventBus.getDefault().register(this);
        activePayTrideBean = EventBus.getDefault().getStickyEvent(ActivePayTrideBean.class);
        //分发界面基础信息
        disPathBaseInfo();
    }

    private void disPathBaseInfo() {
        mActivePayActivename.setText(activePayTrideBean.activeName);
        if (TextUtils.isEmpty(activePayTrideBean.userLogo)) {
            mActivePayUserlogo.setImageResource(R.drawable.picture_moren);
        } else {
            HttpLoader.getImageLoader().get(activePayTrideBean.userLogo, ImageLoader.getImageListener(
                    mActivePayUserlogo, R.drawable.picture_moren, R.drawable.picture_moren));
        }
        mActivePayTicketNum.setText(activePayTrideBean.activeTicketNum + "");
        mActivePayActiveTitle.setText("为" + activePayTrideBean.activeUserName + "投票");
        mActivePayPrice.setText((int) (activePayTrideBean.activePrice) + "");
        mOrderTotalPriceTv.setText("共计: " + (int) (activePayTrideBean.activePrice) + "元");
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ZHIFUBAO_PAY
                && response instanceof AliPaySignResponse) {
            AliPaySignResponse aliPaySignResponse = (AliPaySignResponse) response;
            if (1 == aliPaySignResponse.getCode()) {
                AliPaySignResponse.DataEntity.SignEntity signs = aliPaySignResponse.getData().getSign();
                String sign = signs.getSign();
                if (TextUtils.isEmpty(sign)) {
                    showToastWarning("支付出错,请重新支付");
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
                        PayTask alipay = new PayTask(ActiveJoinPayActivity.this);
                        String result = alipay.pay(payInfo, true);
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            } else {
                mOrderPayNowBtn.setText("立即支付");
                mOrderPayNowBtn.setClickable(true);
                showToastInfo(aliPaySignResponse.getMessage());
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_WEIXIN_PAY
                && response instanceof WxCreateTenpay) {
            wxCreateTenpay = (WxCreateTenpay) response;
            int code = wxCreateTenpay.getCode();
            if (1 == code) {
                sendPayReq(wxCreateTenpay);
            } else {
                showToastError(wxCreateTenpay.getMessage());
                mOrderPayNowBtn.setText("立即支付");
                mOrderPayNowBtn.setClickable(true);
                setAllButtonEnable(true);
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PAY_TRADE_SUCCESS
                && response instanceof TradeSuccessResponse) {
            TradeSuccessResponse requestResponse = (TradeSuccessResponse) response;
            int code = requestResponse.getCode();
            if (1 == code) {
                onPayResult(true);
                finish();
            } else {
                showToastError(requestResponse.getMessage());
                onPayResult(false);
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PAY_TRADE_SUCCESS) {
            showToastWarning("网络连接失败");
            onPayResult(false);
        }
    }

    private void onPayResult(boolean isPaySuccess) {
        if (isPaySuccess) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    private void pay() {
        setAllButtonEnable(false);
        mOrderPayNowBtn.setText("正在支付");
        mOrderPayNowBtn.setClickable(false);
        if (mCbPayZhifibao.isChecked()) {
            //支付宝支付
            payForZhiFuBao();
        } else if (mCbPayWeixin.isChecked()) {
            payForWeixin();
        } else {
            showToastWarning("您尚未选择任何支付方式");
            setAllButtonEnable(true);
            mOrderPayNowBtn.setClickable(true);
            mOrderPayNowBtn.setText("立即支付");
        }
    }

    private void payForZhiFuBao() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("out_trade_no", activePayTrideBean.activeTradeNum);
        params.put("subject", activePayTrideBean.activeName);
        params.put("body", "为" + activePayTrideBean.activeUserName + "投票");
        params.put("total_fee", String.valueOf(activePayTrideBean.activePrice));
        params.put("service", "mobile.securitypay.pay");
        params.put("payment_type", "1");
        params.put("_input_charset", "utf-8");
        params.put("it_b_pay", "30m");
        HttpLoader.post(ConstantsWhatNSM.URL_ZHIFUBAO_PAY, params, AliPaySignResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ZHIFUBAO_PAY, this).setTag(this);
    }

    private void payForWeixin() {
        HashMap params = new HashMap();
        imei = AppManager.getIMEI();
        params.put("tradeNum", activePayTrideBean.activeTradeNum);
        params.put("deviceInfo", imei);
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_WEIXIN_PAY, params, WxCreateTenpay.class,
                ConstantsWhatNSM.REQUEST_CODE_WEIXIN_PAY, this).setTag(this);
    }

    //支付宝和微信支付成功调用
    private void surePaySuccess(int payType) {
        HashMap<String, String> params = new HashMap<>();
        params.put("tradeNum", activePayTrideBean.activeTradeNum);
        params.put("token", NSMTypeUtils.getMyToken());
        if (payType == PayOrderConfig.MAKE_SURE_ALI_PAY) {
            params.put("payType", "alipay");
        } else {
            params.put("payType", "tenpay");
        }
        HttpLoader.post(ConstantsWhatNSM.URL_PAY_TRADE_SUCCESS, params, TradeSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_PAY_TRADE_SUCCESS, this, false).setTag(this);
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
        mHandler.sendEmptyMessageDelayed(2, 2000);
    }

    private void setAllButtonEnable(boolean b) {
        mCbPayZhifibao.setEnabled(b);
        mCbPayWeixin.setEnabled(b);
    }

    public void onEventMainThread(WxPayResult event) {
        if (event.isResult() == true) {
            surePaySuccess(PayOrderConfig.MAKE_SURE_TEN_PAY);
        } else {
            showToastError("支付失败");
            setAllButtonEnable(true);
            mOrderPayNowBtn.setClickable(true);
            mOrderPayNowBtn.setText("立即支付");
        }
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
     * @param context   一个可以开启界面的上下文
     * @param trideBean 一个活动类,里面涵盖了所有支付界面需要必要的参数
     */
    public static void startActiveJoinPayAct(Context context, ActivePayTrideBean trideBean) {
        Intent intent = new Intent(context, ActiveJoinPayActivity.class);
        EventBus.getDefault().postSticky(trideBean);
        context.startActivity(intent);
    }

    /**
     * 开启确认活动界面的方法
     *
     * @param context   一个可以开启界面的上下文
     * @param trideBean 一个活动类,里面涵盖了所有支付界面需要必要的参数
     */
    public static void startActivePayForResult(Activity context, ActivePayTrideBean trideBean, int requestCode) {
        Intent intent = new Intent(context, ActiveJoinPayActivity.class);
        EventBus.getDefault().postSticky(trideBean);
        context.startActivityForResult(intent, requestCode);
    }
}
