package com.neishenme.what.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.neishenme.what.R;
import com.neishenme.what.adapter.WalletRecordsListAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.MyWalletResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.RecordsListResponse;
import com.neishenme.what.dialog.ConvertVipSureDialog;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.view.ListViewAdjustHeight;

import org.seny.android.utils.ActivityUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 新版我的钱包界面 ,主要作用是展示自己的余额,功能上可以使用活动余额进行兑换会员,退款
 * 这个类的作用 :
 * <p>
 * Created by zhaozh on 2016/12/16.
 */
public class MyWalletActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener {
    public static final int REQUEST_CODE_CONVERT_VIP = 1;

    private ImageView mWalletMyBack;
    private TextView mWalletMyBill;

    private TextView mWalletMyBalance;

    private ListViewAdjustHeight mWalletMyRecordsLv;
    private LinearLayout mWalletMyNoBill;

    private LinearLayout mWalletMyVip;
    private LinearLayout mWalletMyBackMoney;
    private LinearLayout mMyWalletBuyVipLayout;
    private TextView mMyWalletActivePause;
    private ImageView mMyWalletActiveBuyVip;

    private WalletRecordsListAdapter mAdapter;  //界面消费记录的适配器


    @Override
    protected int initContentView() {
        return R.layout.activity_my_wallet;
    }

    @Override
    protected void initView() {
        mWalletMyBack = (ImageView) findViewById(R.id.wallet_my_back);
        mWalletMyBill = (TextView) findViewById(R.id.wallet_my_bill);

        mWalletMyBalance = (TextView) findViewById(R.id.wallet_my_balance);

        mWalletMyRecordsLv = (ListViewAdjustHeight) findViewById(R.id.wallet_my_records_lv);
        mWalletMyNoBill = (LinearLayout) findViewById(R.id.wallet_my_no_bill);

        mMyWalletActivePause = (TextView) findViewById(R.id.my_wallet_active_pause);
        mMyWalletActiveBuyVip = (ImageView) findViewById(R.id.my_wallet_active_buy_vip);
        mWalletMyVip = (LinearLayout) findViewById(R.id.wallet_my_vip);
        mWalletMyBackMoney = (LinearLayout) findViewById(R.id.wallet_my_back_money);
        mMyWalletBuyVipLayout = (LinearLayout) findViewById(R.id.my_wallet_buy_vip_layout);
    }

    @Override
    protected void initListener() {
        mWalletMyBack.setOnClickListener(this);
        mWalletMyBill.setOnClickListener(this);
        mMyWalletActiveBuyVip.setOnClickListener(this);
        mWalletMyVip.setOnClickListener(this);
        mWalletMyBackMoney.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        //获取钱包信息
        getPersonWallet();

        //获取消费记录信息
        getRecordsList();
    }

    private void getPersonWallet() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", App.SP.getString("token", null));
        HttpLoader.post(ConstantsWhatNSM.URL_PERSON_WALLET, params, MyWalletResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_PERSON_WALLET, this, false).setTag(this);
    }

    private void getRecordsList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", "0");
        params.put("pageSize", "5");
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_PERSON_WALLET_EXPENSE, params, RecordsListResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_PERSON_WALLET_EXPENSE, this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PERSON_WALLET
                && response instanceof MyWalletResponse) {
            MyWalletResponse myWalletResponse = (MyWalletResponse) response;
            int code = myWalletResponse.getCode();
            if (1 == code) {
                MyWalletResponse.DataEntity data = myWalletResponse.getData();
                mWalletMyBalance.setText(NSMTypeUtils.getGreatPrice(data.getPurse()));
                if (data.getActivitypurse() == 0) {
                    mMyWalletBuyVipLayout.setVisibility(View.GONE);
                } else {
                    mMyWalletBuyVipLayout.setVisibility(View.VISIBLE);
                    mMyWalletActivePause.setText("活动余额: " +
                            NSMTypeUtils.getGreatPrice(data.getActivitypurse()) + " (元)");
                }
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PERSON_WALLET_EXPENSE
                && response instanceof RecordsListResponse) {
            RecordsListResponse recordsListResponse = (RecordsListResponse) response;
            int code = recordsListResponse.getCode();
            if (1 == code) {
                List<RecordsListResponse.DataEntity.AccountsEntity> accounts
                        = recordsListResponse.getData().getAccounts();
                if (accounts == null || accounts.size() == 0) {
                    mWalletMyNoBill.setVisibility(View.VISIBLE);
                    mWalletMyRecordsLv.setVisibility(View.GONE);
                    return;
                }
                mWalletMyNoBill.setVisibility(View.GONE);
                mWalletMyRecordsLv.setVisibility(View.VISIBLE);
                if (mAdapter == null) {
                    mAdapter = new WalletRecordsListAdapter(this, accounts);
                    mWalletMyRecordsLv.setAdapter(mAdapter);
                } else {
                    mAdapter.setRecordsList(accounts);
                }
            } else {
                mWalletMyNoBill.setVisibility(View.VISIBLE);
                mWalletMyRecordsLv.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONVERT_VIP) {
            if (resultCode == RESULT_OK) {
                showToastSuccess("兑换成功");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wallet_my_back:   //返回
                finish();
                break;
            case R.id.wallet_my_bill:   //账单
                ActivityUtils.startActivity(this, BillOldActivity.class);
                break;
            case R.id.my_wallet_active_buy_vip:    //兑换会员
                ConvertVipSureDialog dialog = new ConvertVipSureDialog(this);
                dialog.show();
                break;
            case R.id.wallet_my_vip:    //vip专享服务
                ActivityUtils.startActivity(this, VipServiceActivity.class);
                break;
            case R.id.wallet_my_back_money: //退款,提现
                ActivityUtils.startActivity(this, PickMoneyActivity.class);
                break;
            default:
                break;
        }
    }
}
