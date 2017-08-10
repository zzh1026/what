package com.neishenme.what.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 *
 * 查找界面 ,我关注的人查找界面
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener, FocusPeopleAdapter.RemoveDataListener {
    private PullToRefreshListView lvSearch;
    private EditText etSearch;
    private ImageView ivSearch, ivBack;
    private int page = 1;
    private String userName;
    private int loc;
    private ArrayList<Integer> deleteId;

    private FocusPeopleAdapter focusPeopleAdapter;
    private FocusPeopleListResponse focusPeopleListResponse;
    private List<FocusPeopleListResponse.DataEntity.FoucsEntity> focusData;

    @Override
    protected int initContentView() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_focus_back);
        lvSearch = (PullToRefreshListView) findViewById(R.id.lv_search);
        etSearch = (EditText) findViewById(R.id.et_search);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
    }

    @Override
    protected void initListener() {
        ivSearch.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //清除ListView中的数据
                if (focusPeopleAdapter != null) {
                    focusData.clear();
                    focusPeopleAdapter.bindData(focusData);
                    focusPeopleAdapter.notifyDataSetChanged();
                }
                userName = etSearch.getText().toString().trim();
                String trim = s.toString().trim();
                if (!TextUtils.isEmpty(userName)) {
                    HashMap params = new HashMap();
                    params.put("page", page + "");
                    params.put("pageSize", "10");
                    params.put("userName", userName);
                    params.put("token", NSMTypeUtils.getMyToken());
                    HttpLoader.post(ConstantsWhatNSM.URL_FOCUS_PEOPLE, params, FocusPeopleListResponse.class, ConstantsWhatNSM.REQUEST_CODE_FOCUS_PEOPLE, SearchActivity.this, false).setTag(this);
                }
            }
        });


        lvSearch.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                HashMap params = new HashMap();
                params.put("page", page + "");
                params.put("pageSize", "20");
                params.put("userName", userName);
                params.put("token", NSMTypeUtils.getMyToken());
                HttpLoader.post(ConstantsWhatNSM.URL_FOCUS_PEOPLE, params, FocusPeopleListResponse.class, ConstantsWhatNSM.REQUEST_CODE_FOCUS_PEOPLE, SearchActivity.this, false).setTag(this);
            }
        });

    }

    @Override
    protected void initData() {
        deleteId = new ArrayList<>();
        lvSearch.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                userName = etSearch.getText().toString();
                if (!TextUtils.isEmpty(userName)) {
                    HashMap params = new HashMap();
                    params.put("page", page + "");
                    params.put("pageSize", "10");
                    params.put("userName", userName);
                    params.put("token", NSMTypeUtils.getMyToken());
                    HttpLoader.post(ConstantsWhatNSM.URL_FOCUS_PEOPLE, params, FocusPeopleListResponse.class, ConstantsWhatNSM.REQUEST_CODE_FOCUS_PEOPLE, SearchActivity.this, false).setTag(this);
                }
                break;
            case R.id.iv_focus_back:
                returnResult();
                break;
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
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_FOCUS_PEOPLE &&
                response instanceof FocusPeopleListResponse) {
            focusPeopleListResponse = (FocusPeopleListResponse) response;
            if (focusPeopleListResponse.getData().getFoucs().size() != 0) {
                if (focusPeopleAdapter == null) {
                    focusData = focusPeopleListResponse.getData().getFoucs();
                    focusPeopleAdapter = new FocusPeopleAdapter(this);
                    focusPeopleAdapter.bindData(focusData);
                    lvSearch.setAdapter(focusPeopleAdapter);
                    focusPeopleAdapter.setRemoveDataListener(this);
                    lvSearch.onRefreshComplete();
                } else {
                    focusData.addAll(focusPeopleListResponse.getData().getFoucs());
                    focusPeopleAdapter.bindData(focusData);
                    focusPeopleAdapter.notifyDataSetChanged();
                    if (!focusPeopleListResponse.getData().isHasMore()) {
                        lvSearch.onRefreshComplete();
                    }
                }
                lvSearch.onRefreshComplete();
            } else {
                showToastInfo("暂未找到该关注用户");
            }


        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_CANCLE_FOUCS_PEOPLE && response
                instanceof SendSuccessResponse) {


            deleteId.add(focusData.get(loc).getUser_id());
            focusData.remove(loc);
            focusPeopleAdapter.bindData(focusData);
            focusPeopleAdapter.notifyDataSetChanged();
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
        SwLin s = focusPeopleAdapter.mapView.get(loc);
        if (s != null && s.getCurrentScreen() == 1) {
            s.showScreen(0);//0 为主页面 1编辑删除按钮界面
            return;
        }

    }

    @Override
    public void onBackPressed() {
        returnResult();
        super.onBackPressed();

    }


    public void returnResult() {
        if (deleteId.size() != 0) {
            Intent intent = new Intent();
            intent.putIntegerArrayListExtra("deleteId", deleteId);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            finish();
        }

    }
}
