package com.neishenme.what.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.neishenme.what.R;
import com.neishenme.what.adapter.RecordListAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.MyWalletResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.RecordsListResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;

import org.seny.android.utils.ActivityUtils;

import java.util.HashMap;
import java.util.List;

/**
 *
 *  旧的 钱包界面,已弃用,详情查看新的钱包界面
 *  @see MyWalletActivity
 */
@Deprecated
public class MyWalletOldActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener {
    private ImageView ivBack;
    private TextView tvBill;
    private ImageView ivSignOne;
    private TextView tvPrePrice;
    private TextView tvBehindPrice;
    private ImageView btnPickUp;
    private TextView tvCashLeave;
    private TextView tvActivityLeave;
    private ListView mWalletRecordsLv;
    private RecordListAdapter mAdapter;
    private TextView mTvWalletNorecords;


    @Override
    protected int initContentView() {
        return R.layout.activity_my_old_wallet;
    }

    @Override
    protected void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvBill = (TextView) findViewById(R.id.tv_bill);

        tvPrePrice = (TextView) findViewById(R.id.tv_pre_price);
        tvBehindPrice = (TextView) findViewById(R.id.tv_behind_price);
        btnPickUp = (ImageView) findViewById(R.id.btn_pick_up);

        tvCashLeave = (TextView) findViewById(R.id.tv_cash_leave);
        tvActivityLeave = (TextView) findViewById(R.id.tv_activity_leave);

        mWalletRecordsLv = (ListView) findViewById(R.id.wallet_records_lv);
        mTvWalletNorecords = (TextView) findViewById(R.id.tv_wallet_norecords);

    }

    @Override
    protected void initListener() {
        tvBill.setOnClickListener(this);
        btnPickUp.setOnClickListener(this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onStart() {
        //获取钱包信息
        getPersonWallet();

        //获取消费记录信息
        getRecordsList();
        super.onStart();
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
        params.put("pageSize", "10");
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
                setData(myWalletResponse.getData());
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PERSON_WALLET_EXPENSE
                && response instanceof RecordsListResponse) {
            RecordsListResponse recordsListResponse = (RecordsListResponse) response;
            int code = recordsListResponse.getCode();
            if (1 == code) {
                List<RecordsListResponse.DataEntity.AccountsEntity> accounts = recordsListResponse.getData().getAccounts();
                if (accounts == null || accounts.size() == 0) {
                    mTvWalletNorecords.setVisibility(View.VISIBLE);
                    return;
                }
                if (mAdapter == null) {
                    mAdapter = new RecordListAdapter(App.getApplication(), accounts);
                    mWalletRecordsLv.setAdapter(mAdapter);
                } else {
                    mAdapter.setRecordsList(accounts);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    private void setData(MyWalletResponse.DataEntity data) {
        double purseMoney = data.getPurse();
        double activitypurseMoney = data.getActivitypurse();
        double purse = purseMoney + activitypurseMoney;

        String[] purseMoneys = splitPoint(purseMoney);
        if (purseMoneys.length == 2) {
            tvCashLeave.setText("￥" + String.valueOf(purseMoneys[0] + "." + purseMoneys[1]));
        } else if (purseMoneys.length == 1) {
            tvCashLeave.setText("￥" + purseMoney + ".00");
        } else {
            tvCashLeave.setText("￥" + "0.00");
        }

        String[] purseActivitys = splitPoint(activitypurseMoney);
        if (purseActivitys.length == 2) {
            tvActivityLeave.setText("￥" + purseActivitys[0] + "." + purseActivitys[1]);
        } else if (purseActivitys.length == 1) {
            tvActivityLeave.setText("￥" + activitypurseMoney + ".00");
        } else {
            tvActivityLeave.setText("￥" + "0.00");
        }

        String[] purses = splitPoint(purse);
        if (purses.length == 2) {
            tvPrePrice.setText(purses[0] + ".");
            tvBehindPrice.setText(purses[1]);
        } else if (purses.length == 1) {
            tvPrePrice.setText(purses[0]);
            tvBehindPrice.setText(".00");
        } else {
            tvPrePrice.setText("0");
            tvBehindPrice.setText(".00");
        }
    }

    private String[] splitPoint(double purse) {
        String[] split = String.valueOf(purse).split("\\.");
        if (split.length == 2 && split[1].length() == 1) {
            split[1] = split[1].concat("0");
        }
        return split;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pick_up:
                ActivityUtils.startActivity(this, PickMoneyActivity.class);
                break;
            case R.id.tv_bill:
                ActivityUtils.startActivity(this, BillOldActivity.class);
                break;
        }
    }
}
