package com.neishenme.what.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.neishenme.what.R;
import com.neishenme.what.adapter.AddHobbyAdapter;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.HobbyListResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.bean.SendSuccessResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;

import org.seny.android.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/5/19.
 * <p>
 * 这个界面是  个人信息编辑界面 点击添加兴趣爱好后添加新的兴趣界面
 */
public class AddHobbyActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener {
    private static final int ADD_HOBBY = 1;
    public static final int ADD_HOBBY_NUMBER = 7;
    private TextView tvBack;
    private TextView tvTitle;
    private PullToRefreshListView mLvData;
    private AddHobbyAdapter addHobbyAdapter;
    private RelativeLayout rlAdd;
    private TextView mTvSaveHobby;
    private TextView tvHobby;
    private String titleStr;
    private int page = 1;
    private List<String> interestedData;
    private List<String> loadData;
    private String chooseHobby;
    private String type = null;
    private StringBuilder postStrBuilder;
    private boolean haveHobby = false;


    @Override
    protected int initContentView() {
        return R.layout.activity_add_hobby;
    }

    @Override
    protected void initView() {
        rlAdd = (RelativeLayout) findViewById(R.id.rl_add);
        tvHobby = (TextView) findViewById(R.id.tv_hobby);
        tvBack = (TextView) findViewById(R.id.tv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        mLvData = (PullToRefreshListView) findViewById(R.id.lv_data);
        mTvSaveHobby = (TextView) findViewById(R.id.tv_save_hobby);
    }


    @Override
    protected void initListener() {
        tvBack.setOnClickListener(this);
        rlAdd.setOnClickListener(this);
        mLvData.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                addHobbyAdapter = null;
                getInterestInfo();
            }
        });

        mTvSaveHobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postHobby();
            }
        });
    }

    @Override
    protected void initData() {
        mLvData.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        loadData = new ArrayList<>();

        //获取到已经传入进来的标题和爱好信息
        titleStr = getIntent().getStringExtra("data");
        chooseHobby = getIntent().getStringExtra("extra");

        String[] arrayStr;
        if (!TextUtils.isEmpty(chooseHobby)) {
            haveHobby = true;
            arrayStr = chooseHobby.split(";");
            interestedData = java.util.Arrays.asList(arrayStr);
            loadData.addAll(interestedData);
        }

        tvTitle.setText(titleStr);
        tvHobby.setHint("添加自己喜欢的" + titleStr);

        sendConnRequest();
    }

    public void sendConnRequest() {

        switch (titleStr) {
            case "电影":
                type = "movie_name";
                break;
            case "美食":
                type = "food_name";
                break;
            case "旅途":
                type = "trip_name";
                break;
        }

        getInterestInfo();
    }

    private void getInterestInfo() {
        HashMap params = new HashMap();
        params.put("interestype", type);
        params.put("page", page + "");
        params.put("pageSize", "20");
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_INTEREST_LIST, params, HobbyListResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_INTEREST_LIST, this, false).setTag(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_add:
                if (addHobbyAdapter.getSelectedItemCount() >= ADD_HOBBY_NUMBER) {
                    showToastInfo("最多只能选择" + ADD_HOBBY_NUMBER + "项");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("title", titleStr);
                bundle.putString("type", type);
//                bundle.putInt("selectedCount", addHobbyAdapter.getSelectedItemCount());
                ActivityUtils.startActivityForResultBundle(this, AlterInfoActivity.class, bundle, ADD_HOBBY);
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }


    public void postHobby() {
        postStrBuilder = new StringBuilder();
        for (int i = 0; i < addHobbyAdapter.getSelectedList().size(); i++) {
            if (i != addHobbyAdapter.getSelectedList().size() - 1) {
                postStrBuilder.append(addHobbyAdapter.getSelectedList().get(i) + ";");
            } else {
                postStrBuilder.append(addHobbyAdapter.getSelectedList().get(i));
            }
        }
        if (addHobbyAdapter.getSelectedList().size() == 0) {
            upDateHobby();
        } else {
            checkSentive();
        }

    }

    public void resultHobby() {
        Intent intent = new Intent();
        intent.putExtra("type", type);
        intent.putExtra("str", postStrBuilder.toString());
        setResult(RESULT_OK, intent);
    }

    private void checkSentive() {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", postStrBuilder.toString());
        HttpLoader.post(ConstantsWhatNSM.URL_PERSON_EDIT_SENSITIVE_CHECK, params, SendSuccessResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_PERSON_EDIT_SENSITIVE_CHECK, this).setTag(this);
    }

    private void upDateHobby() {
        HashMap params = new HashMap();
        params.put("key", type);
        params.put("content", addHobbyAdapter.getSelectedList().size() == 0 ? "" : postStrBuilder.toString());
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_POST_INTERSTED, params,
                SendSuccessResponse.class, ConstantsWhatNSM.REQUEST_CODE_POST_INTERSTED, this, false).setTag(this);
    }


    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        mLvData.onRefreshComplete();
        if (addHobbyAdapter == null) {
            addHobbyAdapter = new AddHobbyAdapter(this);
        }
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_INTEREST_LIST
                && response instanceof HobbyListResponse) {
            HobbyListResponse hobbyListResponse = (HobbyListResponse) response;
            if (1 != hobbyListResponse.getCode()) {
                showToastInfo(hobbyListResponse.getMessage());
                return;
            }

            List<HobbyListResponse.DataEntity.InterestBaseInfosEntity> loadInterestedEntity =
                    hobbyListResponse.getData().getInterestBaseInfos();
            if (loadInterestedEntity == null || loadInterestedEntity.size() == 0) {
                showToastInfo("已无更多");
                page--;
                return;
            }
            if (haveHobby) {
                for (int i = 0; i < interestedData.size(); i++) {
                    for (int j = 0; j < loadInterestedEntity.size(); j++) {
                        if (loadInterestedEntity.get(j).getContent().equals(interestedData.get(i))) {
                            loadInterestedEntity.remove(j);
                            j--;
                        }
                    }
                }
                for (int i = 0; i < loadInterestedEntity.size(); i++) {
                    loadData.add(loadInterestedEntity.get(i).getContent());
                }

                addHobbyAdapter.setSelectedList(interestedData);
            } else {
                for (int i = 0; i < loadInterestedEntity.size(); i++) {
                    loadData.add(loadInterestedEntity.get(i).getContent());
                }
            }
            addHobbyAdapter.bindData(loadData);
            mLvData.setAdapter(addHobbyAdapter);
            mLvData.getRefreshableView().setSelection(page == 1 ? 0 : loadData.size() - 20);
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_PERSON_EDIT_SENSITIVE_CHECK
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            if (sendSuccessResponse.getCode() == 1) {
                upDateHobby();
            } else {
                showToastInfo(sendSuccessResponse.getMessage());
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_POST_INTERSTED
                && response instanceof SendSuccessResponse) {
            SendSuccessResponse sendSuccessResponse = (SendSuccessResponse) response;
            if (1 == sendSuccessResponse.getCode()) {
                resultHobby();
                finish();
            } else {
                showToastInfo(sendSuccessResponse.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        mLvData.onRefreshComplete();
        showToastError("连接服务器失败");
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_HOBBY && resultCode == RESULT_OK) {
            String str = data.getStringExtra("result");
            if (str != null) {
                loadData.add(0, str);
                addHobbyAdapter.setSelected(str);
                addHobbyAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpLoader.cancelRequest(this);
    }
}
