package com.neishenme.what.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.neishenme.what.R;
import com.neishenme.what.adapter.BuyVipAdapter;
import com.neishenme.what.adapter.ConverVipAdapter;
import com.neishenme.what.base.BasePayActivity;
import com.neishenme.what.bean.BuyVipResponse;
import com.neishenme.what.bean.ConvertVipCardResponse;
import com.neishenme.what.bean.ConvertVipConvertedResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.VipTrade;
import com.neishenme.what.dialog.PayOrderDialog;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.PayCancelListener;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.view.ListViewAdjustHeight;

import java.util.HashMap;

/**
 * 这个类的作用是: 兑换会员 界面, 因为需要支付所以继承了支付的基类
 * <p>
 * Created by zhaozh on 2017/5/17.
 */

public class ConvertVipActivity extends BasePayActivity implements View.OnClickListener {
    private ImageView mConvertVipCancel;
    private ListViewAdjustHeight mConvertVipList;
    private LinearLayout mConvertVipZhifubaoLayout;
    private CheckBox mConvertVipZhifubaoCb;
    private LinearLayout mConvertVipWeixinLayout;
    private CheckBox mConvertVipWeixinCb;
    private Button mConvertVipConvert;

    private String mTradeNumber;
    private ConverVipAdapter mAdapter;
    private boolean canClick = true;
    private double mConvertPrice = 0;      //折扣金额


    @Override
    protected int initContentView() {
        return R.layout.activity_convert_vip;
    }

    @Override
    protected void initView() {
        mConvertVipCancel = (ImageView) findViewById(R.id.convert_vip_cancel);
        mConvertVipList = (ListViewAdjustHeight) findViewById(R.id.convert_vip_list);
        mConvertVipZhifubaoLayout = (LinearLayout) findViewById(R.id.convert_vip_zhifubao_layout);
        mConvertVipZhifubaoCb = (CheckBox) findViewById(R.id.convert_vip_zhifubao_cb);
        mConvertVipWeixinLayout = (LinearLayout) findViewById(R.id.convert_vip_weixin_layout);
        mConvertVipWeixinCb = (CheckBox) findViewById(R.id.convert_vip_weixin_cb);
        mConvertVipConvert = (Button) findViewById(R.id.convert_vip_convert);
    }

    @Override
    protected void initListener() {
        mConvertVipCancel.setOnClickListener(this);
        mConvertVipZhifubaoLayout.setOnClickListener(this);
        mConvertVipWeixinLayout.setOnClickListener(this);
        mConvertVipConvert.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        getConvertVipInfo();
    }

    private void getConvertVipInfo() {
        HashMap params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_CONVERT_VIP_VIPCARD, params, ConvertVipCardResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_CONVERT_VIP_VIPCARD, this, false).setTag(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.convert_vip_cancel:     //返回
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
            case R.id.convert_vip_zhifubao_layout:     //返回
                if (!canClick) {
                    return;
                }
                mConvertVipZhifubaoCb.setChecked(true);
                mConvertVipWeixinCb.setChecked(false);
                break;
            case R.id.convert_vip_weixin_layout:     //返回
                if (!canClick) {
                    return;
                }
                mConvertVipZhifubaoCb.setChecked(false);
                mConvertVipWeixinCb.setChecked(true);
                break;
            case R.id.convert_vip_convert:  //兑换
                if (!canClick) {
                    return;
                }
                convert();
                break;
            default:
                break;
        }
    }

    private void convert() {
        if (mAdapter.getSelectedPosition() >= 0) {
            ConvertVipCardResponse.DataBean.VipCardsBean vipCardsBean = mAdapter.getItem(mAdapter.getSelectedPosition());
            if (mConvertPrice < vipCardsBean.getPrice()) {
                if (!mConvertVipWeixinCb.isChecked() && !mConvertVipZhifubaoCb.isChecked()) {
                    showToastWarning("你还未选中任何支付方式");
                    return;
                }
            }
            setBtnState("正在兑换", false);
            HashMap params = new HashMap();
            params.put("vipCardId", vipCardsBean.getId() + "");
            params.put("token", NSMTypeUtils.getMyToken());
            params.put("deduction", String.valueOf(mConvertPrice));
            HttpLoader.post(ConstantsWhatNSM.URL_CONVERT_VIP_TRADE, params, ConvertVipConvertedResponse.class,
                    ConstantsWhatNSM.REQUEST_CODE_CONVERT_VIP_TRADE, this, false).setTag(this);
        } else {
            showToastInfo("请选择兑换内容");
        }
    }

    @Override
    protected String getTradeNumber() {
        return mTradeNumber;
    }

    @Override
    protected void resetState() {
        setBtnState("立即兑换", true);
    }

    @Override
    protected void onPayResult(boolean payResult) {
        setResult(payResult);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        super.onGetResponseSuccess(requestCode, response);
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_CONVERT_VIP_VIPCARD
                && response instanceof ConvertVipCardResponse) {
            ConvertVipCardResponse convertVipCardResponse = (ConvertVipCardResponse) response;
            if (convertVipCardResponse.getCode() == 1) {
                ConvertVipCardResponse.DataBean data = convertVipCardResponse.getData();
                mConvertPrice = data.getDeduction();
                mAdapter = new ConverVipAdapter(this, data.getVipCards(), mConvertPrice);
                mConvertVipList.setAdapter(mAdapter);
            } else {
                showToastWarning(convertVipCardResponse.getMessage());
                resetState();
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_CONVERT_VIP_TRADE
                && response instanceof ConvertVipConvertedResponse) {
            ConvertVipConvertedResponse vipTrade = (ConvertVipConvertedResponse) response;
            if (vipTrade.getCode() == 1) {
                if (vipTrade.getData().getTrade().getPayprice() > 0) {
                    mTradeNumber = vipTrade.getData().getTrade().getTradeNum();
                    if (mConvertVipZhifubaoCb.isChecked()) {
                        payForZhiFuBao();
                    } else {
                        payForWeixin();
                    }
                } else {
                    showToastSuccess("兑换成功");
                    setResult(true);
                }
            } else {
                resetState();
                showToastError(vipTrade.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        super.onGetResponseError(requestCode, error);
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_CONVERT_VIP_VIPCARD
                || requestCode == ConstantsWhatNSM.REQUEST_CODE_CONVERT_VIP_TRADE) {
            resetState();
        }
    }

    private void setBtnState(String btnState, boolean canClick) {
        this.canClick = canClick;   //三个选择框的选择与下方的点击支付的按钮是同步的
        mConvertVipConvert.setText(btnState);
        mConvertVipConvert.setClickable(canClick);
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

    /**
     * 开启活动支付活动界面的方法
     *
     * @param requestCode 为了更加方便,支付界面以后同一使用startforresult来开启,只需要将支付是否成功传出即可
     * @param context     一个可以开启界面的上下文
     */
    public static void startConvertVipActForResult(Activity context, int requestCode) {
        Intent intent = new Intent(context, ConvertVipActivity.class);
        context.startActivityForResult(intent, requestCode);
    }
}
