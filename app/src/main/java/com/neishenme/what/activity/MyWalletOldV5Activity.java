package com.neishenme.what.activity;

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
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.view.ListViewAdjustHeight;

import org.seny.android.utils.ActivityUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 新版我的钱包界面
 * 这个类的作用 :
 * <p>
 * Created by zhaozh on 2016/12/16.
 *
 * v5版本过时的钱包, 2017/5/16
 */

@Deprecated
public class MyWalletOldV5Activity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener {
    private ImageView mWalletMyBack;
    private TextView mWalletMyBill;

    private TextView mWalletMyBalance;
    private TextView mWalletMyCashBalance;
    private TextView mWalletMyActiveBalance;

    private ListViewAdjustHeight mWalletMyRecordsLv;
    private LinearLayout mWalletMyNoBill;

    private LinearLayout mWalletMyCoupons;
    private LinearLayout mWalletMyVip;
    private LinearLayout mWalletMyBackMoney;

    private WalletRecordsListAdapter mAdapter;  //界面消费记录的适配器


    @Override
    protected int initContentView() {
        return R.layout.activity_my_wallet_old_v5;
    }

    @Override
    protected void initView() {
        mWalletMyBack = (ImageView) findViewById(R.id.wallet_my_back);
        mWalletMyBill = (TextView) findViewById(R.id.wallet_my_bill);

        mWalletMyBalance = (TextView) findViewById(R.id.wallet_my_balance);
        mWalletMyCashBalance = (TextView) findViewById(R.id.wallet_my_cash_balance);
        mWalletMyActiveBalance = (TextView) findViewById(R.id.wallet_my_active_balance);

        mWalletMyRecordsLv = (ListViewAdjustHeight) findViewById(R.id.wallet_my_records_lv);
        mWalletMyNoBill = (LinearLayout) findViewById(R.id.wallet_my_no_bill);


        mWalletMyCoupons = (LinearLayout) findViewById(R.id.wallet_my_coupons);
        mWalletMyVip = (LinearLayout) findViewById(R.id.wallet_my_vip);
        mWalletMyBackMoney = (LinearLayout) findViewById(R.id.wallet_my_back_money);
    }

    @Override
    protected void initListener() {
        mWalletMyBack.setOnClickListener(this);
        mWalletMyBill.setOnClickListener(this);
        mWalletMyCoupons.setOnClickListener(this);
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
//                mWalletMyBalance.setText(data.getAllPurse() + "");
                mWalletMyBalance.setText(NSMTypeUtils.getGreatPrice(data.getAllPurse()));
                mWalletMyCashBalance.setText(data.getPurse() + "");
                mWalletMyActiveBalance.setText(data.getActivitypurse() + "");
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PERSON_WALLET_EXPENSE
                && response instanceof RecordsListResponse) {
            RecordsListResponse recordsListResponse = (RecordsListResponse) response;
            int code = recordsListResponse.getCode();
            if (1 == code) {
                List<RecordsListResponse.DataEntity.AccountsEntity> accounts = recordsListResponse.getData().getAccounts();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wallet_my_back:   //返回
                finish();
                break;
            case R.id.wallet_my_bill:   //账单
                ActivityUtils.startActivity(this, BillOldActivity.class);
                break;
            case R.id.wallet_my_coupons:    //我的优惠券
                ActivityUtils.startActivity(this, MyCouponsActivity.class);
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
