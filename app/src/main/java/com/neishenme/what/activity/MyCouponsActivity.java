package com.neishenme.what.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.neishenme.what.R;
import com.neishenme.what.adapter.MyCouponsListAdapter;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.MyCouPonsResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 新版我的优惠券界面
 * 这个类的作用 :
 * <p>
 * Created by zhaozh on 2016/12/22.
 */
public class MyCouponsActivity extends BaseActivity implements HttpLoader.ResponseListener {
    public static final String SELECTED_TYPE = "type";
    public static final String SELECTED_COUPONS = "selected";

    public static final String SELECTED_COUPONS_ID = "id";
    public static final String SELECTED_COUPONS_TIME = "time";

    private ImageView mCouponsMyBack;
    private PullToRefreshGridView mCouponsMyAllcou;
    private LinearLayout mCouponsMyNoCoupons;

    private int page = 1;   //初始化页面数,第一页
    private boolean hasMoreInfo = false;    //标记是否有更多信息

    private List<MyCouPonsResponse.DataBean.CouponsBean> mCouponsBean;  //我的优惠券需要展示的信息
    private MyCouponsListAdapter mAdapter;  //界面我的优惠券的适配器

    private boolean isSelected = false; //标记是否为选择优惠券的情况
    private String time;    //时间
    private String serviceId;   //价格


    @Override
    protected int initContentView() {
        return R.layout.activity_my_coupons;
    }

    @Override
    protected void initView() {
        mCouponsMyBack = (ImageView) findViewById(R.id.coupons_my_back);
        mCouponsMyAllcou = (PullToRefreshGridView) findViewById(R.id.coupons_my_allcou);
        mCouponsMyNoCoupons = (LinearLayout) findViewById(R.id.coupons_my_no_coupons);
//        mCouponsMyAllcou.setMode(PullToRefreshBase.Mode.BOTH);
    }

    @Override
    protected void initListener() {
        mCouponsMyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mCouponsMyAllcou.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                mAdapter = null;
                page = 1;
                requestMyCoupons();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                if (hasMoreInfo) {
                    page++;
                    requestMyCoupons();
                } else {
                    mCouponsMyAllcou.onRefreshComplete();
                    showToastInfo("暂无更多信息!");
                }
            }
        });

        mCouponsMyAllcou.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isSelected) {
                    EventBus.getDefault().post(mCouponsBean.get(position));
                    finish();
                }
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String selectedType = intent.getStringExtra(SELECTED_TYPE);
        if (!TextUtils.isEmpty(selectedType) && SELECTED_COUPONS.equals(selectedType)) {
            isSelected = true;
            time = intent.getStringExtra(SELECTED_COUPONS_TIME);
            serviceId = intent.getStringExtra(SELECTED_COUPONS_ID);
        } else {
            isSelected = false;
        }

        //获取我的优惠券信息
        page = 1;
        requestMyCoupons();
    }

    private void requestMyCoupons() {
        HashMap<String, String> params = new HashMap<>();
        if (isSelected) {
            params.put("serviceId", serviceId);
            params.put("time", time);
        }
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("page", page + "");
        params.put("pageSize", "10");
        HttpLoader.post(ConstantsWhatNSM.URL_ACTIVE_GET_COUPONS, params, MyCouPonsResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_GET_COUPONS, this).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_GET_COUPONS
                && response instanceof MyCouPonsResponse) {
            mCouponsMyAllcou.onRefreshComplete();
            MyCouPonsResponse myCouPonsResponse = (MyCouPonsResponse) response;
            if (myCouPonsResponse.getCode() == 1) {
                hasMoreInfo = myCouPonsResponse.getData().isHasMore();
                mCouponsMyAllcou.setMode(PullToRefreshBase.Mode.BOTH);
                if (mAdapter == null || page == 1) {
                    mCouponsBean = myCouPonsResponse.getData().getCoupons();
                    if (null == mCouponsBean || mCouponsBean.size() == 0) {
                        mCouponsMyNoCoupons.setVisibility(View.VISIBLE);
                        return;
                    }
                    mCouponsMyNoCoupons.setVisibility(View.GONE);
                    mAdapter = new MyCouponsListAdapter(this, mCouponsBean);
                    mCouponsMyAllcou.setAdapter(mAdapter);
                } else {
                    mCouponsBean.addAll(myCouPonsResponse.getData().getCoupons());
                    mAdapter.notifyDataSetChanged();
                }
            } else {
                showToastInfo(myCouPonsResponse.getMessage());
                mCouponsMyNoCoupons.setVisibility(View.VISIBLE);
                mCouponsMyAllcou.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        mCouponsMyAllcou.onRefreshComplete();
        showToastError("网络获取失败,请重试!");
    }
}
