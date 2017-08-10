package com.neishenme.what.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hyphenate.easeui.EaseConstant;
import com.neishenme.what.R;
import com.neishenme.what.adapter.RecognizedAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.MyFriendsResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.eventbusobj.ChatInfoBean;
import com.neishenme.what.huanxinchat.ui.ChatActivity;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.HuanXinUtils;
import com.neishenme.what.utils.NSMTypeUtils;

import org.seny.android.utils.ActivityUtils;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 我认识的人界面
 */
public class RecognizedPeopleActivity extends BaseActivity implements HttpLoader.ResponseListener {
    public static final int SEARCH_DATA = 2;
    public static final int RESULT_SEARCH_DATA = 3;
    private ImageView ivBack;
    private PullToRefreshListView mLv;
    private RecognizedAdapter adapter;
    private LinearLayout mRlSearch;
    private TextView mTvHint;

    private int page = 0;
    private List<MyFriendsResponse.DataEntity.FriendsEntity> friends;
    private List<MyFriendsResponse.DataEntity.FriendsEntity> newFriends;

    @Override
    protected int initContentView() {
        return R.layout.activity_recognized_people;
    }

    @Override
    protected void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        mLv = (PullToRefreshListView) findViewById(R.id.mLv);
        mRlSearch = (LinearLayout) findViewById(R.id.rl_search);
        mTvHint = (TextView) findViewById(R.id.tv_hint);

    }

    @Override
    protected void initListener() {
        mRlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivityForResult(RecognizedPeopleActivity.this, SearchRecActivity.class, SEARCH_DATA);
            }
        });

        mLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                adapter = null;
                page = 0;
                HashMap params = new HashMap();
                params.put("token", NSMTypeUtils.getMyToken());
                params.put("page", "0");
                params.put("pageSize", "10");
                HttpLoader.post(ConstantsWhatNSM.URL_RECOGNIZED_PEOPLE, params, MyFriendsResponse.class,
                        ConstantsWhatNSM.REQUEST_CODE_RECOGNIZED_PEOPLE, RecognizedPeopleActivity.this, false).setTag(this);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                HashMap params = new HashMap();
                params.put("token", NSMTypeUtils.getMyToken());
                params.put("page", page + "");
                params.put("pageSize", "10");
                HttpLoader.post(ConstantsWhatNSM.URL_RECOGNIZED_PEOPLE, params, MyFriendsResponse.class,
                        ConstantsWhatNSM.REQUEST_CODE_RECOGNIZED_PEOPLE, RecognizedPeopleActivity.this, false).setTag(this);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (HuanXinUtils.isLoginToHX()) {
                    MyFriendsResponse.DataEntity.FriendsEntity friendsEntity =
                            friends.get(position - 1);
                    ChatInfoBean chatInfoBean = new ChatInfoBean(
                            friendsEntity.getLogo(),
                            String.valueOf(friendsEntity.getId()),
                            friendsEntity.getName());
                    EventBus.getDefault().postSticky(chatInfoBean);
                    App.EDIT.remove(friendsEntity.getHxUserName()).commit();
                    startActivity(new Intent(RecognizedPeopleActivity.this, ChatActivity.class)
                            .putExtra(EaseConstant.EXTRA_USER_ID, friendsEntity.getHxUserName()));
                } else {
                    showToastError("聊天服务器异常,请返回重试");
                    return;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void initData() {
        mLv.setMode(PullToRefreshBase.Mode.BOTH);
        getFriends();
        HuanXinUtils.login();
    }

    private void getFriends() {
        HashMap params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("page", "0");
        params.put("pageSize", "10");
        HttpLoader.post(ConstantsWhatNSM.URL_RECOGNIZED_PEOPLE, params, MyFriendsResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_RECOGNIZED_PEOPLE, this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_RECOGNIZED_PEOPLE
                && response instanceof MyFriendsResponse) {
            mLv.onRefreshComplete();
            MyFriendsResponse myFriendsResponse = (MyFriendsResponse) response;
            int code = myFriendsResponse.getCode();
            if (1 != code) {
                showToastInfo(myFriendsResponse.getMessage());
                return;
            } else {
                if (null == myFriendsResponse || null == myFriendsResponse.getData() || null == myFriendsResponse.getData().getFriends()
                        || myFriendsResponse.getData().getFriends().size() == 0) {
                    showToastInfo("没有更多了");
                    page--;
                    return;
                }
                newFriends = myFriendsResponse.getData().getFriends();
                if (null == adapter) {
                    this.friends = newFriends;
                    adapter = new RecognizedAdapter(this, this.friends, true);
                    mLv.setAdapter(adapter);
                } else {
                    this.friends.addAll(newFriends);
                    adapter.setData(this.friends);
                    adapter.notifyDataSetChanged();
                }
                adapter.setOnScreenFriend(new RecognizedAdapter.OnScreenFriend() {
                    @Override
                    public void onScreenFriend(int position) {
//                        MyFriendsResponse.DataEntity.FriendsEntity friendsEntity = friends.get(position);
//                        int relation = friendsEntity.getRelation();
//                        if (1 == relation) {
//                            friendsEntity.setRelation(2);
//                        } else {
//                            friendsEntity.setRelation(1);
//                        }
                        RecognizedPeopleActivity.this.friends.clear();
                        page = 0;
                        getFriends();
                    }
                });
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_DATA && resultCode == RESULT_SEARCH_DATA) {
            adapter = null;
            page = 0;
            HashMap params = new HashMap();
            params.put("token", NSMTypeUtils.getMyToken());
            params.put("page", "0");
            params.put("pageSize", "50");
            HttpLoader.post(ConstantsWhatNSM.URL_RECOGNIZED_PEOPLE, params, MyFriendsResponse.class,
                    ConstantsWhatNSM.REQUEST_CODE_RECOGNIZED_PEOPLE, RecognizedPeopleActivity.this, false).setTag(this);
        }
    }
}
