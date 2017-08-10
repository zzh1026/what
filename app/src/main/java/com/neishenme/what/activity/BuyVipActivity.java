package com.neishenme.what.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.VolleyError;
import com.neishenme.what.R;
import com.neishenme.what.adapter.BuyVipAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.AliPaySignResponse;
import com.neishenme.what.bean.BuyVipResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.TradeSuccessResponse;
import com.neishenme.what.bean.VipTrade;
import com.neishenme.what.bean.WxCreateTenpay;
import com.neishenme.what.bean.WxPayResult;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.AppManager;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
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
 * 这个界面是 主界面设置里面点击会员中心后购买会员的界面
 */
public class BuyVipActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener {
    private ImageView ivBack;
    private ListView mLv;
    private RelativeLayout rlPayZhifubao;
    private CheckBox cbPayZhifibao;
    private RelativeLayout rlPayWeixin;
    private CheckBox cbPayWeixin;
    private Button btnBuyVip;
    private int vipCardId;
    private int preCLickPositon = -1;
    private BuyVipAdapter adapter;
    private static final int SDK_PAY_FLAG = 1;
    private String out_trade_no;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(BuyVipActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        //支付成功后调用本地确认的方法.
                        surePaySuccess();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            surePaySuccess();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            showToastError("支付失败");
                            btnBuyVip.setText("确认支付");
                            setAllButtonEnable(true);
                        }
                    }
                    break;
                }
                case 2:
                    break;
                default:
                    break;

            }
        }

        ;
    };

    @Override
    protected int initContentView() {
        return R.layout.activity_buy_vip;
    }

    @Override
    protected void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        mLv = (ListView) findViewById(R.id.mLv);
        rlPayZhifubao = (RelativeLayout) findViewById(R.id.rl_pay_zhifubao);
        cbPayZhifibao = (CheckBox) findViewById(R.id.cb_pay_zhifibao);
        rlPayWeixin = (RelativeLayout) findViewById(R.id.rl_pay_weixin);
        cbPayWeixin = (CheckBox) findViewById(R.id.cb_pay_weixin);
        btnBuyVip = (Button) findViewById(R.id.btn_buy_vip);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(this);
        rlPayZhifubao.setOnClickListener(this);
        rlPayWeixin.setOnClickListener(this);
        btnBuyVip.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        HashMap params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_VIP_SCHEME, params, BuyVipResponse.class,
                ConstantsWhatNSM.REQUEST_VIP_SCHEME, BuyVipActivity.this, false).setTag(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_pay_zhifubao:
                cbPayZhifibao.setChecked(true);
                cbPayZhifibao.setBackground(getResources().getDrawable(R.drawable.pay_gou_select));
                cbPayWeixin.setChecked(false);
                cbPayWeixin.setBackground(getResources().getDrawable(R.drawable.buy_vip_normal));
                break;
            case R.id.rl_pay_weixin:
                cbPayZhifibao.setChecked(false);
                cbPayZhifibao.setBackground(getResources().getDrawable(R.drawable.buy_vip_normal));
                cbPayWeixin.setChecked(true);
                cbPayWeixin.setBackground(getResources().getDrawable(R.drawable.pay_gou_select));
                break;
            case R.id.btn_buy_vip:
                if (adapter.getSelectedPosition() >= 0) {
                    if (cbPayZhifibao.isChecked() || cbPayWeixin.isChecked()) {
                        HashMap params = new HashMap();
                        BuyVipResponse.DataBean.VipCardsBean vipCardsBean = adapter.getItem(adapter.getSelectedPosition());
                        params.put("vipCardId", vipCardsBean.getId() + "");
                        params.put("token", NSMTypeUtils.getMyToken());
                        HttpLoader.post(ConstantsWhatNSM.URL_VIP_TRADE, params, VipTrade.class,
                                ConstantsWhatNSM.REQUEST_CODE_VIP_TRADE, BuyVipActivity.this, false).setTag(this);
                        setAllButtonEnable(false);
                        btnBuyVip.setText("支付中");
                    } else {
                        showToastInfo("你还未选中任何支付方式");
                    }
                } else {
                    showToastInfo("请选择购买内容");
                }
                break;
        }

    }

    private void setAllButtonEnable(boolean b) {
        rlPayWeixin.setEnabled(b);
        rlPayZhifubao.setEnabled(b);
        cbPayWeixin.setEnabled(b);
        cbPayZhifibao.setEnabled(b);
        mLv.setEnabled(b);
    }


    private void payForAli(VipTrade response) {
        HashMap<String, String> params = new HashMap<>();

        params.put("token", NSMTypeUtils.getMyToken());
        //活动号
        params.put("out_trade_no", response.getData().getTrade().getTradeNum());
        //服务名称
        params.put("subject", App.SP.getString("tvRestautantNameTag", "huiyuan"));
        //服务介绍
        params.put("body", App.SP.getString("tvRestautantName", "huiyuan"));
        //支付金额,price
        params.put("total_fee", String.valueOf(response.getData().getTrade().getPrice()));
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

    //当支付宝显示支付成功的时候会调用该方法
    private void surePaySuccess() {
        HashMap<String, String> params = new HashMap<>();
        params.put("tradeNum", out_trade_no);
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("payType", "alipay");
        HttpLoader.post(ConstantsWhatNSM.URL_PAY_TRADE_SUCCESS, params, TradeSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_PAY_TRADE_SUCCESS, this, false).setTag(this);
    }

    private void sendPayReq(WxCreateTenpay wxCreateTenpay) {
        //通过WXAPIFactory工厂，获取IWXAPI的实例
        IWXAPI api = WXAPIFactory.createWXAPI(this, wxCreateTenpay.getData().getAppid(), false);
        // 将该app注册到微信
        api.registerApp(wxCreateTenpay.getData().getAppid());
        if (!api.isWXAppInstalled()) {
            MyToast.showConterToast(this, "未安装微信，请安装后重试");
            setAllButtonEnable(true);
            btnBuyVip.setText("确认支付");
            return;
        } else {
            boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
            //Toast.makeText(PayActivity.this, String.valueOf(isPaySupported), Toast.LENGTH_SHORT).show();
            if (isPaySupported == false) {
                MyToast.showConterToast(this, "微信版本过低，请更新后重试");
                setAllButtonEnable(true);
                btnBuyVip.setText("确认支付");
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
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
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
            HashMap params = new HashMap();
            params.put("tradeNum", out_trade_no);
            params.put("payType", "tenpay");
            params.put("token", NSMTypeUtils.getMyToken());
            HttpLoader.post(ConstantsWhatNSM.URL_PAY_TRADE_SUCCESS, params, TradeSuccessResponse.class,
                    ConstantsWhatNSM.REQUEST_CODE_PAY_TRADE_SUCCESS, this, false).setTag(this);
        } else {
//            builder.setMessage("微信支付失败");
//            builder.show();
            showToastError("支付失败");
            setAllButtonEnable(true);
            btnBuyVip.setText("确认支付");
        }

    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_VIP_SCHEME
                && response instanceof BuyVipResponse) {

            if (((BuyVipResponse) response).getCode() == 1) {
                adapter = new BuyVipAdapter(this, (BuyVipResponse) response);
                mLv.setAdapter(adapter);
            }

        }
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_VIP_TRADE && response instanceof VipTrade) {
            int m = ((VipTrade) response).getCode();

            if (((VipTrade) response).getCode() == 1) {
                out_trade_no = ((VipTrade) response).getData().getTrade().getTradeNum();
                if (cbPayWeixin.isChecked()) {
                    cbPayByWeixin((VipTrade) response);
                } else {
                    payForAli((VipTrade) response);
                }
            } else {
                setAllButtonEnable(true);
                showToastInfo(((VipTrade) response).getMessage());
            }
        }
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ZHIFUBAO_PAY
                && response instanceof AliPaySignResponse) {
            AliPaySignResponse aliPaySignResponse = (AliPaySignResponse) response;
            if (1 == aliPaySignResponse.getCode()) {

                /**
                 * 完整的符合支付宝参数规范的活动信息
                 */
                AliPaySignResponse.DataEntity.SignEntity signs = aliPaySignResponse.getData().getSign();
                String sign = signs.getSign();

                if (TextUtils.isEmpty(sign)) {
                    btnBuyVip.setText("确认支付");
                    setAllButtonEnable(true);
                    return;
                }
                try {
                    /**
                     * 仅需对sign 做URL编码
                     */
                    sign = URLEncoder.encode(sign, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
//                final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
                final String payInfo = signs.getOrderInfo();
                ALog.i(payInfo);
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(BuyVipActivity.this);
                        // 调用支付接口，获取支付结果
                        String result = alipay.pay(payInfo, true);
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }
        }
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_WEIXIN_PAY
                && response instanceof WxCreateTenpay) {
            WxCreateTenpay wxCreateTenpay = (WxCreateTenpay) response;
            ALog.d("sign:" + wxCreateTenpay.getData().getSign());
            ALog.d("package:" + wxCreateTenpay.getData().getPackageX());
            int code = wxCreateTenpay.getCode();
            if (1 == code) {
                sendPayReq(wxCreateTenpay);
            } else {
                showToastError("支付出错请重新支付");
//                btnSubmit.setText("支付");
//                btnSubmit.setClickable(true);
                setAllButtonEnable(true);
                btnBuyVip.setText("确认支付");


            }
        }
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PAY_TRADE_SUCCESS
                && response instanceof TradeSuccessResponse) {
            TradeSuccessResponse requestResponse = (TradeSuccessResponse) response;
            int code = requestResponse.getCode();
            if (1 == code) {
                showToastSuccess("支付成功,快快体验会员新功能吧");
                finish();
                setAllButtonEnable(true);
                btnBuyVip.setText("确认支付");
            } else {
                showToastError("支付失败");
                setAllButtonEnable(true);
                btnBuyVip.setText("确认支付");
            }
        }
    }

    private void cbPayByWeixin(VipTrade response) {

        HashMap params = new HashMap();
        String imei = AppManager.getIMEI();
        String tradeNum = out_trade_no;
        params.put("tradeNum", tradeNum);
        params.put("deviceInfo", imei);
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_WEIXIN_PAY, params, WxCreateTenpay.class,
                ConstantsWhatNSM.REQUEST_CODE_WEIXIN_PAY, this).setTag(this);
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        String s = error.getMessage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
