package com.neishenme.what.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.neishenme.what.R;
import com.neishenme.what.adapter.FocusPeopleAdapter;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.FocusPeopleListResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.view.SwLin;

import org.seny.android.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class FocusPeopleActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener, FocusPeopleAdapter.RemoveDataListener {
    public static final int SEARCH_DATA = 1;
    private TextView tvHint;
    private PullToRefreshListView lvFocus;
    private FocusPeopleAdapter focusPeopleAdapter;
    private int page = 1;
    private String userName = "";
    private FocusPeopleListResponse focusPeopleListResponse;
    private List<FocusPeopleListResponse.DataEntity.FoucsEntity> focusData;
    private int loc;
    private LinearLayout rlSearch;
    private ImageView iv_back;

    @Override
    protected int initContentView() {
        return R.layout.activity_focus_people;
    }

    @Override
    protected void initView() {
        tvHint = (TextView) findViewById(R.id.tv_hint);
        lvFocus = (PullToRefreshListView) findViewById(R.id.lv_focus);
        rlSearch = (LinearLayout) findViewById(R.id.rl_search);
        iv_back = (ImageView) findViewById(R.id.iv_focus_back);
    }

    @Override
    protected void initListener() {
        rlSearch.setOnClickListener(this);

        iv_back.setOnClickListener(this);

        lvFocus.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                HashMap params = new HashMap();
                params.put("page", page + "");
                params.put("pageSize", "20");
                params.put("userName", "");
                params.put("token", NSMTypeUtils.getMyToken());
                HttpLoader.post(ConstantsWhatNSM.URL_FOCUS_PEOPLE, params,
                        FocusPeopleListResponse.class, ConstantsWhatNSM.REQUEST_CODE_FOCUS_PEOPLE,
                        FocusPeopleActivity.this, false).setTag(this);

            }
        });


    }

    @Override
    protected void initData() {
        focusData = new ArrayList<>();
        lvFocus.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        tvHint.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        focusPeopleAdapter = null;
        HashMap params = new HashMap();
        params.put("page", page + "");
        params.put("pageSize", "20");
        params.put("userName", "");
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_FOCUS_PEOPLE, params,
                FocusPeopleListResponse.class, ConstantsWhatNSM.REQUEST_CODE_FOCUS_PEOPLE, this, false).setTag(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_search:
                ActivityUtils.startActivityForResult(this, SearchActivity.class, SEARCH_DATA);
                break;
            case R.id.iv_focus_back:
                finish();
                break;
        }
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_FOCUS_PEOPLE &&
                response instanceof FocusPeopleListResponse) {
            focusPeopleListResponse = (FocusPeopleListResponse) response;
            int code = focusPeopleListResponse.getCode();
            if (1 != code) {
                showToastInfo(focusPeopleListResponse.getMessage());
                return;
            }

            if (focusPeopleAdapter == null) {
                focusData = focusPeopleListResponse.getData().getFoucs();
                focusPeopleAdapter = new FocusPeopleAdapter(this);
                focusPeopleAdapter.bindData(focusData);

                lvFocus.setAdapter(focusPeopleAdapter);
                focusPeopleAdapter.setRemoveDataListener(this);
                lvFocus.onRefreshComplete();
            } else {
                focusData.addAll(focusPeopleListResponse.getData().getFoucs());
                focusPeopleAdapter.bindData(focusData);
                focusPeopleAdapter.notifyDataSetChanged();
                if (!focusPeopleListResponse.getData().isHasMore()) {
                    lvFocus.onRefreshComplete();
                }
            }

            lvFocus.onRefreshComplete();


        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_CANCLE_FOUCS_PEOPLE
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            int code = sendSuccessResponse.getCode();
            if (1 == code) {
                showToastSuccess("取消关注成功");
                focusData.remove(loc);
                focusPeopleAdapter.bindData(focusData);
                focusPeopleAdapter.notifyDataSetChanged();
            } else {
                showToastError("取消失败,请重试");
            }
            SwLin s = focusPeopleAdapter.mapView.get(loc);
            if (s != null && s.getCurrentScreen() == 1) {
                s.showScreen(0);//0 为主页面 1编辑删除按钮界面
                return;
            }
        }


    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        showToastError("网络访问失败了,请您检查一下网络设置吧");
        if (focusPeopleAdapter != null) {
            SwLin s = focusPeopleAdapter.mapView.get(loc);
            if (s != null && s.getCurrentScreen() == 1) {
                s.showScreen(0);//0 为主页面 1编辑删除按钮界面
                return;
            }
        }
    }

    @Override
    public void removeData(int position) {
        HashMap params = new HashMap();

        params.put("targetId", focusData.get(position).getUser_id() + "");
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_CANCLE_FOUCS_PEOPLE, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_CANCLE_FOUCS_PEOPLE, this, false).setTag(this);
        loc = position;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SEARCH_DATA:
                    ArrayList<Integer> deleteId = data.getIntegerArrayListExtra("deleteId");
                    for (int i = 0; i < focusData.size(); i++) {
                        for (int j = 0; j < deleteId.size(); j++) {
                            if (focusData.get(i).getUser_id() == deleteId.get(j)) {
                                focusData.remove(i);
                            }
                        }
                    }
                    focusPeopleAdapter.bindData(focusData);
                    focusPeopleAdapter.notifyDataSetChanged();

                    break;
            }
        }
    }
}
