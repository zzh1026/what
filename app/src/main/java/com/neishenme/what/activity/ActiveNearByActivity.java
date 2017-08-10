package com.neishenme.what.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.neishenme.what.R;
import com.neishenme.what.adapter.ActiveNearByAdapter;
import com.neishenme.what.baidumap.service.LocationService;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.ActiveCurrentSharedInfos;
import com.neishenme.what.bean.ActiveListResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.dialog.ActiveTicketNearBySubDialog;
import com.neishenme.what.eventbusobj.ActiveSharedBean;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.LocationUtils;
import com.neishenme.what.utils.NSMTypeUtils;

import org.seny.android.utils.ActivityUtils;

import java.util.HashMap;
import java.util.List;

import jp.wasabeef.blurry.Blurry;

/**
 * 附近的活动, 已经废弃了  2017/4/20
 */

@Deprecated
public class ActiveNearByActivity extends BaseActivity implements HttpLoader.ResponseListener {

    private ImageView mIvBack;
    private PullToRefreshGridView mActiveDetailPgv;
    private RelativeLayout mRlContener;

    private int page = 1;

    private boolean isCanJoin = false;    //标记是否可以加入
    private boolean hasMoreInfo = false;   //是否有更多
    private double latitude;    //经纬度
    private double longgitude;

    private ActiveSharedBean mActiveSharedBean = null;

    private ActiveNearByAdapter mActiveNearByAdapter;
    private List<ActiveListResponse.DataBean.TakemeoutBean> takemeoutAll; //所有需要用到的数据

    @Override
    protected int initContentView() {
        return R.layout.activity_activite_nearby;
    }

    @Override
    protected void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mRlContener = (RelativeLayout) findViewById(R.id.rl_contener);
        mActiveDetailPgv = (PullToRefreshGridView) findViewById(R.id.active_detail_pgv);
        mActiveDetailPgv.setMode(PullToRefreshBase.Mode.BOTH);
    }

    @Override
    protected void initListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mActiveDetailPgv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                mActiveNearByAdapter = null;
                page = 1;
                getPlease();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                if (hasMoreInfo) {
                    page++;
                    getPlease();
                } else {
                    showToastInfo("暂无更多信息!");
                    mActiveDetailPgv.onRefreshComplete();
                }
            }
        });

        mActiveDetailPgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!NSMTypeUtils.isLogin()) {
                    toLogin();
                    return;
                }
                int userId = 0;
                ActiveListResponse.DataBean.TakemeoutBean takemeoutBean = takemeoutAll.get(position);
                if (takemeoutBean != null) {
                    userId = takemeoutBean.getUserId();
                }
                if (NSMTypeUtils.isMyUserId(String.valueOf(userId))) {
                    ActivityUtils.startActivity(ActiveNearByActivity.this, MyActiveActivity.class);
                } else if (userId != 0) {
                    UserActiveDetailActivity.startUserDetailAct(ActiveNearByActivity.this, userId, Integer.parseInt(takemeoutBean.getId()));
                } else {
                    showToastInfo("获取用户失败,请退出重试!");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mActiveNearByAdapter = null;
        page = 1;
        getPlease();
    }

    @Override
    protected void initData() {
        getAllActive();
        getCurrentSharedInfo();
    }

    private void getCurrentSharedInfo() {
        HashMap<String, String> paerm = new HashMap<>();
        paerm.put("type", "takemeout");
        HttpLoader.post(ConstantsWhatNSM.URL_ACTIVE_GET_CURRENT_SHARED, paerm, ActiveCurrentSharedInfos.class,
                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_GET_CURRENT_SHARED, this, false).setTag(this);
    }

    public void toLogin() {
        showToastWarning("请先登录帐号!");
        ActivityUtils.startActivity(this, LoginActivity.class);
    }

    private void getPlease() {
        LocationUtils.getLocation(new LocationUtils.OnGetLocationListener() {
            @Override
            public void onGetLocation(double latitude, double longgitude, BDLocation location) {
                ActiveNearByActivity.this.latitude = latitude;
                ActiveNearByActivity.this.longgitude = longgitude;
                getAllActive();
            }

            @Override
            public void onGetError() {
                getAllActive();
            }
        }, LocationService.CoorType.CoorType_GCJ02);
    }

    //获取所有的活动信息
    private void getAllActive() {
        HashMap params = new HashMap();
        params.put("page", page + "");
        params.put("pageSize", "20");
        params.put("orderby", "near");
        params.put("longitude", longgitude + "");
        params.put("latitude", latitude + "");
        HttpLoader.post(ConstantsWhatNSM.URL_ACTIVE_TACKMEOUT_LIST, params, ActiveListResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_TACKMEOUT_LIST, this, false).setTag(this);
    }

    public void ticketSub(int ticketSubPosition) {
        if (!isCanJoin) {
            showToastInfo("现在不是投票时间,不能投票");
            return;
        }
        if (!NSMTypeUtils.isLogin()) {
            toLogin();
            return;
        }
        ActiveTicketNearBySubDialog mActiveTicketSubDialog = new ActiveTicketNearBySubDialog(this, takemeoutAll.get(ticketSubPosition));
        mActiveTicketSubDialog.show();
        Blurry.with(ActiveNearByActivity.this).radius(10).sampling(2).async().onto(mRlContener);
    }

    public void dismissDialog() {
        Blurry.delete(mRlContener);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        mActiveDetailPgv.onRefreshComplete();
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_TACKMEOUT_LIST
                && response instanceof ActiveListResponse) {
            ActiveListResponse activeListResponse = (ActiveListResponse) response;
            if (activeListResponse.getCode() == 1) {
                ActiveListResponse.DataBean data = activeListResponse.getData();
                hasMoreInfo = activeListResponse.getData().isHasMore();
                if (mActiveNearByAdapter == null || page == 1) {
                    takemeoutAll = data.getTakemeout();
                    mActiveNearByAdapter = new ActiveNearByAdapter(this, takemeoutAll);
                    mActiveDetailPgv.setAdapter(mActiveNearByAdapter);
                } else {
                    takemeoutAll.addAll(data.getTakemeout());
                    mActiveNearByAdapter.notifyDataSetChanged();
                }

                if (null == takemeoutAll || takemeoutAll.size() == 0) {
                    return;
                }
            } else {
                showToastInfo(activeListResponse.getMessage());
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_GET_CURRENT_SHARED
                && response instanceof ActiveCurrentSharedInfos) {
            ActiveCurrentSharedInfos mActiveCurrentSharedInfos = (ActiveCurrentSharedInfos) response;
            if (mActiveCurrentSharedInfos.getCode() == 1) {
                ActiveCurrentSharedInfos.DataBean.ActiveBean activeBean = mActiveCurrentSharedInfos.getData().getActive();
                mActiveSharedBean = new ActiveSharedBean(activeBean.getShareTitle(), activeBean.getShareLink(), activeBean.getShareDescribe(), activeBean.getShareImage());

                if (activeBean.getStartJoinTime() < System.currentTimeMillis() && System.currentTimeMillis() < activeBean.getEndJoinTime()) {
                    isCanJoin = true;
                } else {
                    isCanJoin = false;
                }
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        mActiveDetailPgv.onRefreshComplete();
        showToastError("网络获取失败,请重试");
    }

    @Override
    protected void onDestroy() {
        HttpLoader.cancelRequest(this);
        super.onDestroy();
    }
}
