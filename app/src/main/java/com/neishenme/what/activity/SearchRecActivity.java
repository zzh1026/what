package com.neishenme.what.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.neishenme.what.R;
import com.neishenme.what.adapter.RecognizedAdapter;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.MyFriendsResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 *
 * 这个是我的好友界面的搜索好友界面, 由我的好友界面进入
 */
public class SearchRecActivity extends BaseActivity implements View.OnClickListener, HttpLoader.ResponseListener {
    private PullToRefreshListView lvSearch;
    private EditText etSearch;
    private ImageView ivSearch, ivBack;
    private String userName;

    private RecognizedAdapter recognizedAdapter;
    private List<MyFriendsResponse.DataEntity.FriendsEntity> myAllFriendsData;
    private List<MyFriendsResponse.DataEntity.FriendsEntity> myFriendsData;

    @Override
    protected int initContentView() {
        return R.layout.activity_search_rec;
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
                userName = etSearch.getText().toString().trim();
                getFriendsInfo(userName);
            }
        });
    }

    @Override
    protected void initData() {
        lvSearch.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        myFriendsData = new ArrayList<>();
        HashMap params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("page", "0");
        params.put("pageSize", "150");
        HttpLoader.post(ConstantsWhatNSM.URL_RECOGNIZED_PEOPLE, params, MyFriendsResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_RECOGNIZED_PEOPLE, this, false).setTag(this);

    }

    private void getFriendsInfo(String userName) {
        if (TextUtils.isEmpty(userName))
            return;

        if (myAllFriendsData == null || myAllFriendsData.size() == 0) {
            showToastInfo("暂未找到该好友");
            return;
        }
        myFriendsData.clear();
        for (int i = 0; i < myAllFriendsData.size(); i++) {
            MyFriendsResponse.DataEntity.FriendsEntity friendsEntity = myAllFriendsData.get(i);
            if (friendsEntity.getName().contains(userName)) {
                myFriendsData.add(friendsEntity);
            }
        }
        if (recognizedAdapter != null) {
            recognizedAdapter.setData(myFriendsData);
            recognizedAdapter.notifyDataSetChanged();
        } else {
            recognizedAdapter = new RecognizedAdapter(this, myFriendsData, false);
            lvSearch.setAdapter(recognizedAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                userName = etSearch.getText().toString();
                getFriendsInfo(userName);
                break;
            case R.id.iv_focus_back:
                setResult(RecognizedPeopleActivity.RESULT_SEARCH_DATA);
                finish();
                break;
        }
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_RECOGNIZED_PEOPLE
                && response instanceof MyFriendsResponse) {
            MyFriendsResponse myFriendsResponse = (MyFriendsResponse) response;
            int code = myFriendsResponse.getCode();
            if (1 == code) {
                if (myFriendsResponse.getData().getFriends().size() > 0) {
                    myFriendsResponse.getData().getFriends().remove(0);
                }
                myAllFriendsData = myFriendsResponse.getData().getFriends();
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        showToastError("网络访问失败了,请您检查一下网络设置吧");
    }
}
