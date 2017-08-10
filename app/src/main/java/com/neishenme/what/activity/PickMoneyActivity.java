package com.neishenme.what.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.neishenme.what.R;
import com.neishenme.what.adapter.PickMoneyAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.PickMoneyInfoResponse;
import com.neishenme.what.bean.PickMoneyListResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.RefundsStates;
import com.neishenme.what.bean.TradeSuccessResponse;
import com.neishenme.what.dialog.PayOrderDialog;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.nsminterface.PayOrderInputListener;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.view.ListViewAdjustHeight;
import com.neishenme.what.view.refreshview.CustomRefreshLayout;

import org.seny.android.utils.ActivityUtils;
import org.seny.android.utils.MD5Utils;

import java.util.HashMap;
import java.util.List;

/**
 * 提现界面, 进入方式是  由 我的钱包界面点击退款界面进入
 */
public class PickMoneyActivity extends BaseActivity implements HttpLoader.ResponseListener, View.OnClickListener {

    private ImageView mPickMoneyCalcel;
    private TextView mPickMoneySelectAll;
    private TextView mPickMoneyMyPrice;
    //    private RecyclerView mPickMoneyListRecycle;
    private ListViewAdjustHeight mListViewAdjustHeight;
    private Button mPickMoneyPick;
    private TextView mPickMoneyForgetPaypassword;

    private CustomRefreshLayout mPickMoneyRefreshLayout;
    private LinearLayout mPickMoneyListLayout;
    private LinearLayout mPickMoneyNoMoney;

    //    private PickMoneyListAdapter mAdapter;
    private PickMoneyAdapter mAdapter;

    private double mPriceMax;   //最大可提现金额
    private int mCurrentStatus; //当前的状态 -1为没有申请记录   如果是其它值说明是正在申请
    private boolean mIsVip;     //1 - 是vip , 其他 不是vip

    private String mSelectedIds;    //选中的id集合
    private RefundsStates statusMap;//返回来的状态对象

    @Override
    protected int initContentView() {
        return R.layout.activity_pick_money;
    }

    @Override
    protected void initView() {
        mPickMoneyCalcel = (ImageView) findViewById(R.id.pick_money_calcel);
        mPickMoneySelectAll = (TextView) findViewById(R.id.pick_money_select_all);
        mPickMoneyPick = (Button) findViewById(R.id.pick_money_pick);
        mPickMoneyForgetPaypassword = (TextView) findViewById(R.id.pick_money_forget_paypassword);
        mPickMoneyMyPrice = (TextView) findViewById(R.id.pick_money_my_price);
//        mPickMoneyListRecycle = (RecyclerView) findViewById(R.id.pick_money_list_recycle);
        mListViewAdjustHeight = (ListViewAdjustHeight) findViewById(R.id.pick_money_list_listview);

        mPickMoneyRefreshLayout = (CustomRefreshLayout) findViewById(R.id.pick_money_refresh_layout);
        mPickMoneyListLayout = (LinearLayout) findViewById(R.id.pick_money_list_layout);
        mPickMoneyNoMoney = (LinearLayout) findViewById(R.id.pick_money_no_money);

    }

    @Override
    protected void initListener() {
        mPickMoneyCalcel.setOnClickListener(this);
        mPickMoneySelectAll.setOnClickListener(this);
        mPickMoneyPick.setOnClickListener(this);
        mPickMoneyForgetPaypassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pick_money_calcel:
                finish();
                break;
            case R.id.pick_money_select_all:    //全选
                if (mAdapter != null && !mAdapter.isSelectedAll()) {
                    mAdapter.changeAllInfo(true);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.pick_money_pick:    //提现
                if (mCurrentStatus == -1) {  //没有申请记录，点击为 申请退款逻辑
                    drawMoney();
                } else {             //查询进度逻辑
                    if (statusMap == null) {
                        return;
                    }
                    Intent intent = new Intent(PickMoneyActivity.this, RefundsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("states", statusMap);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.pick_money_forget_paypassword:    //忘记密码
                ActivityUtils.startActivity(PickMoneyActivity.this, ResetPayPwdActivity.class);
                break;
        }
    }

    @Override
    protected void initData() {
//        mAdapter = new PickMoneyListAdapter(this, data);
//        mPickMoneyListRecycle.setLayoutManager(new LinearLayoutManager(this));
//        mPickMoneyListRecycle.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //获取提现界面的信息
        getPickMoneyInfo();
    }

    private void getPickMoneyInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_PICK_MONEY_INFO, params, PickMoneyInfoResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_PICK_MONEY_INFO, this, false).setTag(this);
    }

    private void getPickMoneyScheme() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_PICK_MONEY_SCHEME, params, PickMoneyListResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_PICK_MONEY_SCHEME, this, false).setTag(this);
    }

    /**
     * 请求进度查看接口，查询状态
     */
    private void getRefundsRequestDate() {
        //验证是否有支付密码
        HashMap<String, String> params = new HashMap<>();
        params.put("token", App.SP.getString("token", null));
        HttpLoader.post(ConstantsWhatNSM.URL_APPLYY_MONEY, params, TradeSuccessResponse.class,
                ConstantsWhatNSM.URL_APPLYY_MONEY_STATE, this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PICK_MONEY_INFO   //提现界面的具体信息
                && response instanceof PickMoneyInfoResponse) {
            PickMoneyInfoResponse pickMoneyInfoResponse = (PickMoneyInfoResponse) response;
            int code = pickMoneyInfoResponse.getCode();
            if (1 == code) {
                PickMoneyInfoResponse.DataBean dataBean = pickMoneyInfoResponse.getData();
                mPriceMax = dataBean.getPriceMax();
                mCurrentStatus = dataBean.getCurrentStatus();
                mIsVip = dataBean.getIsvip() == 1;
                if (mPriceMax <= 0 && mCurrentStatus == -1) {
                    mPickMoneyRefreshLayout.setVisibility(View.GONE);
                    mPickMoneyNoMoney.setVisibility(View.VISIBLE);
                    mPickMoneySelectAll.setVisibility(View.INVISIBLE);
                    return;
                }
                mPickMoneyRefreshLayout.setVisibility(View.VISIBLE);
                mPickMoneyNoMoney.setVisibility(View.GONE);
                mPickMoneyMyPrice.setText("￥ " + mPriceMax);

                if (mIsVip && mCurrentStatus == -1) {
                    mPickMoneySelectAll.setVisibility(View.VISIBLE);
                } else {
                    mPickMoneySelectAll.setVisibility(View.INVISIBLE);
                }

                if (mCurrentStatus == -1) {
                    mPickMoneyPick.setText("申请退款");
                    mPickMoneyListLayout.setVisibility(View.VISIBLE);
                    //获取退款的方案
                    getPickMoneyScheme();
                } else {
                    mPickMoneyPick.setText("进度查询");
                    //获取进度信息
                    getRefundsRequestDate();
                    mPickMoneyListLayout.setVisibility(View.GONE);
                }
            } else {
                showToastWarning(pickMoneyInfoResponse.getMessage());
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PICK_MONEY_SCHEME
                && response instanceof PickMoneyListResponse) {
            PickMoneyListResponse pickMoneyListResponse = (PickMoneyListResponse) response;
            if (pickMoneyListResponse.getCode() == 1) {
                List<PickMoneyListResponse.DataBean.ListBean> list = pickMoneyListResponse.getData().getList();
                if (list != null && list.size() != 0) {
                    mAdapter = new PickMoneyAdapter(this, list, mIsVip);
                    mListViewAdjustHeight.setAdapter(mAdapter);
                }
            } else {
                showToastWarning(pickMoneyListResponse.getMessage());
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
                        setBtnState(true);
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    }

                    @Override
                    public void inputSuccessListener(String input) { //登录密码输入成功后。
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        //密码输入正确，调用申请退款借口
                        payMoneyTry(input);
                    }

                });
            } else {
                //跳转密码设置界面 (支付)
                ActivityUtils.startActivity(this, RegestPayPassWordActivity.class);
                setBtnState(true);
            }
        }
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PICK_MONEY_REFUND    //申请退款接口业务逻辑处理
                && response instanceof TradeSuccessResponse) {
            TradeSuccessResponse walletMaxPrice = (TradeSuccessResponse) response;
            int code = walletMaxPrice.getCode();
            if (1 == code) {
                ActivityUtils.startActivityForData(this, RefundsActivity.class, walletMaxPrice.getData().getApplyTime());
            } else {
                showToastInfo(walletMaxPrice.getMessage());
            }
            setBtnState(true);
        }
        if (requestCode == ConstantsWhatNSM.URL_APPLYY_MONEY_STATE           //查询进度接口业务逻辑处理
                && response instanceof TradeSuccessResponse) {
            TradeSuccessResponse walletMaxPrice = (TradeSuccessResponse) response;
            int code = walletMaxPrice.getCode();
            if (1 == code) {
                statusMap = walletMaxPrice.getData().getStatusMap();//返回来的状态对象
            } else {
                showToastInfo(walletMaxPrice.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        setBtnState(true);
    }

    private void setBtnState(boolean canClick) {
        mPickMoneyPick.setClickable(canClick);
    }

    private void drawMoney() {
        mSelectedIds = mAdapter.getSelectedIds();
        if (TextUtils.isEmpty(mSelectedIds)) {
            showToastInfo("请选择提现金额");
            return;
        }
        setBtnState(false);
        //验证是否有支付密码
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_USER_PAY_PASSWORD, params, TradeSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_USER_PAY_PASSWORD, this, false).setTag(this);
    }

    /**
     * 密码输入正确后，调用申请退款接口
     *
     * @param input
     */
    private void payMoneyTry(String input) {
        double selectedMoneys = mAdapter.getSelectedMoneys();
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("payPassword", MD5Utils.addToMD5(input));
        params.put("amount", String.valueOf(selectedMoneys > mPriceMax ? mPriceMax : selectedMoneys));
        params.put("tradeDetailId", mSelectedIds);
        HttpLoader.post(ConstantsWhatNSM.URL_PICK_MONEY_REFUND, params, TradeSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_PICK_MONEY_REFUND, this, false).setTag(this);
    }
}
