package com.neishenme.what.base;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.android.volley.VolleyError;
import com.neishenme.what.bean.ActivtyPrice;
import com.neishenme.what.bean.AliPaySignResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.TradeSuccessResponse;
import com.neishenme.what.bean.WxCreateTenpay;
import com.neishenme.what.bean.WxPayResult;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/3/27.
 * <p>
 * 支付宝和 微信支付的基类
 *
 * 为了方便未来查找, 其他用到支付的地方有
 *
 * 1,{@link com.neishenme.what.base.BasePayActivity} ,本支付基类界面
 * 2,{@link com.neishenme.what.activity.ActiveJoinPayActivity} ,1元购活动支付界面
 * 3,{@link com.neishenme.what.activity.BuyVipActivity} ,会员购买/续费界面
 * 4,{@link com.neishenme.what.activity.PayOrderActivity} ,活动支付界面
 *
 * 5,{@link com.neishenme.what.activity.PayOrderOldActivity} ,活动支付界面备份 ,该界面已经丢弃了.
 *
 *
 * 1,包含了{@link com.neishenme.what.activity.PayActiveActivity} ,活动支付界面
 * 2,包含了{@link com.neishenme.what.activity.ConvertVipActivity},兑换会员界面
 */

public abstract class BasePayActivity extends BaseActivity implements HttpLoader.ResponseListener {

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
                        showToastError("支付失败");
                        resetState();
                    }
                    break;
                }
                case 2:
                    resetState();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
    }

    protected void payForZhiFuBao() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("out_trade_no", getTradeNumber());   //活动号
        HttpLoader.post(ConstantsWhatNSM.URL_ZHIFUBAO_PAY, params, AliPaySignResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ZHIFUBAO_PAY, this).setTag(this);
    }

    protected void payForWeixin() {
        HashMap params = new HashMap();
        params.put("tradeNum", getTradeNumber());
        params.put("deviceInfo", AppManager.getIMEI());
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_WEIXIN_PAY, params, WxCreateTenpay.class,
                ConstantsWhatNSM.REQUEST_CODE_WEIXIN_PAY, this).setTag(this);
    }

    //支付宝和微信支付成功调用
    protected void surePaySuccess(int payType) {
        HashMap<String, String> params = new HashMap<>();
        params.put("tradeNum", getTradeNumber());
        params.put("token", NSMTypeUtils.getMyToken());
        if (payType == PayOrderConfig.MAKE_SURE_ALI_PAY) {
            params.put("payType", "alipay");
        } else {
            params.put("payType", "tenpay");
        }
        HttpLoader.post(ConstantsWhatNSM.URL_PAY_TRADE_SUCCESS, params, TradeSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_PAY_TRADE_SUCCESS, this, false).setTag(this);
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
                    showToastError("支付出错,请重新支付");
                    resetState();
                    return;
                }
                aliPayForResult(signs);
            } else {
                showToastError(aliPaySignResponse.getMessage());
                resetState();
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_WEIXIN_PAY
                && response instanceof WxCreateTenpay) {
            WxCreateTenpay wxCreateTenpay = (WxCreateTenpay) response;
            int code = wxCreateTenpay.getCode();
            if (1 == code) {
                sendPayReq(wxCreateTenpay);
            } else {
                showToastError("支付出错,请重新支付");
                resetState();
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PAY_TRADE_SUCCESS
                && response instanceof TradeSuccessResponse) {
            TradeSuccessResponse requestResponse = (TradeSuccessResponse) response;
            int code = requestResponse.getCode();
            if (1 == code) {
                onPayResult(true);
            } else {
                onPaySuccessError();
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PAY_TRADE_SUCCESS) {
            onPaySuccessError();
        }
        resetState();
    }

    private void onPaySuccessError() {
        showToastWarning("确认支付时服务器异常,已经支付的钱会退回钱包");
        onPayResult(false);
    }

    private void aliPayForResult(AliPaySignResponse.DataEntity.SignEntity signs) {
        String sign = signs.getSign();
        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String payInfo = signs.getOrderInfo();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(BasePayActivity.this);
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

    //微信支付
    private void sendPayReq(WxCreateTenpay wxCreateTenpay) {
        IWXAPI api = WXAPIFactory.createWXAPI(this, wxCreateTenpay.getData().getAppid(), false);
        // 将该app注册到微信
        api.registerApp(wxCreateTenpay.getData().getAppid());
        if (!api.isWXAppInstalled()) {
            showToastWarning("未安装微信，请安装后重试");
            resetState();
            return;
        } else {
            if (api.getWXAppSupportAPI() < Build.PAY_SUPPORTED_SDK_INT) {
                showToastWarning("微信版本过低，请更新后重试");
                resetState();
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

    public void onEventMainThread(WxPayResult event) {
        if (event.isResult()) {
            surePaySuccess(PayOrderConfig.MAKE_SURE_TEN_PAY);
        } else {
            showToastError("支付失败");
            resetState();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpLoader.cancelRequest(this);
        EventBus.clearCaches();
        EventBus.getDefault().unregister(this);
    }

    //获取订单号
    protected abstract String getTradeNumber();

    //重置状态
    protected abstract void resetState();

    //设置支付结果
    protected abstract void onPayResult(boolean payResult);
}
