package com.neishenme.what.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.neishenme.what.R;
import com.neishenme.what.base.BasePayActivity;
import com.neishenme.what.dialog.PayOrderDialog;
import com.neishenme.what.eventbusobj.ActiveJoinTridBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.PayCancelListener;
import com.neishenme.what.utils.PayOrderConfig;
import com.neishenme.what.utils.TimeUtils;

import de.greenrobot.event.EventBus;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/3/29.
 *
 *  支付界面,主要用于 活动加入订单或者活动发布订单后的支付界面
 */

public class PayActiveActivity extends BasePayActivity implements View.OnClickListener {

    private ImageView mPayActiveBack;
    private TextView mPayActiveType;
    private ImageView mPayActiveLogo;
    private TextView mPayActiveTitle;
    private TextView mPayActiveTime;
    private TextView mPayActivePrice;
    private LinearLayout mPayActiveAlipayLayout;
    private CheckBox mPayActiveAlipayCheck;
    private LinearLayout mPayActiveTenpayLayout;
    private CheckBox mPayActiveTenpayCheck;
    private TextView mPayActiveTotalPrice;
    private Button mPayActivePay;

    private ActiveJoinTridBean mActiveJoinTridBean;

    private boolean canCheckClick = true;    //标记checkBox是否可以点击,当点击支付后就不能点击了

    @Override
    protected int initContentView() {
        return R.layout.activity_pay_active;
    }

    @Override
    protected void initView() {
        mPayActiveBack = (ImageView) findViewById(R.id.pay_active_back);

        mPayActiveType = (TextView) findViewById(R.id.pay_active_type);
        mPayActiveLogo = (ImageView) findViewById(R.id.pay_active_logo);
        mPayActiveTitle = (TextView) findViewById(R.id.pay_active_title);
        mPayActiveTime = (TextView) findViewById(R.id.pay_active_time);
        mPayActivePrice = (TextView) findViewById(R.id.pay_active_price);

        mPayActiveAlipayLayout = (LinearLayout) findViewById(R.id.pay_active_alipay_layout);
        mPayActiveAlipayCheck = (CheckBox) findViewById(R.id.pay_active_alipay_check);
        mPayActiveTenpayLayout = (LinearLayout) findViewById(R.id.pay_active_tenpay_layout);
        mPayActiveTenpayCheck = (CheckBox) findViewById(R.id.pay_active_tenpay_check);

        mPayActiveTotalPrice = (TextView) findViewById(R.id.pay_active_total_price);
        mPayActivePay = (Button) findViewById(R.id.pay_active_pay);
    }

    @Override
    protected void initListener() {
        mPayActiveBack.setOnClickListener(this);
        mPayActiveAlipayLayout.setOnClickListener(this);
        mPayActiveTenpayLayout.setOnClickListener(this);
        mPayActivePay.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();

        mActiveJoinTridBean = EventBus.getDefault().getStickyEvent(ActiveJoinTridBean.class);

        if (mActiveJoinTridBean == null) {
            finish();
        }

        disPathBaseInfo();

    }

    private void disPathBaseInfo() {
        mPayActiveType.setText(mActiveJoinTridBean.name);
        if (TextUtils.isEmpty(mActiveJoinTridBean.logo)) {
            mPayActiveLogo.setImageResource(R.drawable.picture_moren);
        } else {
            HttpLoader.getImageLoader().get(mActiveJoinTridBean.logo,
                    ImageLoader.getImageListener(mPayActiveLogo, R.drawable.picture_moren, R.drawable.picture_moren));
        }
        mPayActiveTitle.setText(mActiveJoinTridBean.title);
        if (TimeUtils.isToday(mActiveJoinTridBean.time) || TimeUtils.isTomorrow(mActiveJoinTridBean.time)) {
            if (TimeUtils.isToday(mActiveJoinTridBean.time)) {
                mPayActiveTime.setText("今天 " + TimeUtils.getTime(mActiveJoinTridBean.time));
            } else {
                mPayActiveTime.setText("明天 " + TimeUtils.getTime(mActiveJoinTridBean.time));
            }
        } else {
            mPayActiveTime.setText(TimeUtils.getTime(mActiveJoinTridBean.time, TimeUtils.DATE_FORMAT_NSM));
        }

        mPayActivePrice.setText("" + (mActiveJoinTridBean.price));
        mPayActiveTotalPrice.setText("共计: " + (mActiveJoinTridBean.price) + "元");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_active_back:     //返回
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
            case R.id.pay_active_alipay_layout:   //支付宝
                if (canCheckClick) {
                    mPayActiveAlipayCheck.setChecked(!mPayActiveAlipayCheck.isChecked());
                    mPayActiveTenpayCheck.setChecked(false);
                }
                break;
            case R.id.pay_active_tenpay_layout:   //支付宝
                if (canCheckClick) {
                    mPayActiveTenpayCheck.setChecked(!mPayActiveTenpayCheck.isChecked());
                    mPayActiveAlipayCheck.setChecked(false);
                }
                break;
            case R.id.pay_active_pay:  //立即支付
                pay();
                break;
            default:
                break;
        }
    }

    private void pay() {
        setBtnState(PayOrderConfig.BTN_STATE_IS_PAYING, PayOrderConfig.BTN_NO_CLICK);
        if (mPayActiveAlipayCheck.isChecked()) {
            payForZhiFuBao();
        } else if (mPayActiveTenpayCheck.isChecked()) {
            payForWeixin();
        } else {
            showToastInfo("您尚未选择任何支付方式");
            setBtnState(PayOrderConfig.BTN_STATE_CAN_PAY, PayOrderConfig.BTN_CAN_CLICK);
        }
    }

    private void setBtnState(String btnState, boolean canClick) {
        canCheckClick = canClick;   //三个选择框的选择与下方的点击支付的按钮是同步的
        mPayActivePay.setText(btnState);
        mPayActivePay.setClickable(canClick);
    }

    @Override
    protected void onPayResult(boolean payResult) {
        setResult(payResult);
    }

    @Override
    protected String getTradeNumber() {
        return mActiveJoinTridBean.tradeNum;
    }

    @Override
    protected void resetState() {
        setBtnState(PayOrderConfig.BTN_STATE_CAN_PAY, PayOrderConfig.BTN_CAN_CLICK);
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
     * @param trideBean   一个活动类,里面涵盖了所有支付界面需要必要的参数
     */
    public static void startPayActiveActForResult(Activity context, int requestCode, ActiveJoinTridBean trideBean) {
        Intent intent = new Intent(context, PayActiveActivity.class);
        EventBus.getDefault().postSticky(trideBean);
        context.startActivityForResult(intent, requestCode);
    }
}
