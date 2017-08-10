package com.neishenme.what.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.neishenme.what.R;
import com.neishenme.what.adapter.RecordListAdapter;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.RecordsListResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 作者：zhaozh create on 2016/6/2 14:59
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 账单界面
 * .
 * 其作用是 :
 */
public class BillOldActivity extends BaseActivity implements HttpLoader.ResponseListener {
    private final int START_PAGE = 1;

    private ImageView mIvBack;
    private PullToRefreshListView mPullRefreshLv;
    private RecordListAdapter mAdapter;
    List<RecordsListResponse.DataEntity.AccountsEntity> allAccounts;
    private TextView mBillTvNobill;
    int page = START_PAGE;

    @Override
    protected int initContentView() {
        return R.layout.activity_bill;
    }

    @Override
    protected void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mPullRefreshLv = (PullToRefreshListView) findViewById(R.id.pull_refresh_lv);
        mPullRefreshLv.setMode(PullToRefreshBase.Mode.BOTH);

        mBillTvNobill = (TextView) findViewById(R.id.bill_tv_nobill);
    }

    @Override
    protected void initListener() {

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPullRefreshLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mAdapter = null;
                allAccounts = null;
                page = START_PAGE;
                requestWalletExpense();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                requestWalletExpense();
            }
        });
    }

    @Override
    protected void initData() {
        page = START_PAGE;
        requestWalletExpense();
    }

    private void requestWalletExpense() {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("pageSize", "20");
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_PERSON_WALLET_EXPENSE, params, RecordsListResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_PERSON_WALLET_EXPENSE, this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PERSON_WALLET_EXPENSE
                && response instanceof RecordsListResponse) {
            mPullRefreshLv.onRefreshComplete();
            RecordsListResponse recordsListResponse = (RecordsListResponse) response;
            int code = recordsListResponse.getCode();
            if (1 == code) {
                List<RecordsListResponse.DataEntity.AccountsEntity> accounts = recordsListResponse.getData().getAccounts();
                if (accounts == null || accounts.size() == 0) {
                    showToastInfo("没有更多了");
                    if (page > 1) page--;
                    return;
                }

                if (mAdapter == null || page == START_PAGE) {
                    allAccounts = accounts;
                    mAdapter = new RecordListAdapter(this, allAccounts);
                    mPullRefreshLv.setAdapter(mAdapter);
                } else {
                    allAccounts.addAll(accounts);
                    mAdapter.setRecordsList(allAccounts);
                }

                if (allAccounts == null || allAccounts.size() == 0) {
                    mBillTvNobill.setVisibility(View.VISIBLE);
                } else {
                    mBillTvNobill.setVisibility(View.GONE);
                }
            } else {
                showToastInfo(recordsListResponse.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        mPullRefreshLv.onRefreshComplete();
        showToastInfo(getString(R.string.app_net_work_error));
    }
}
