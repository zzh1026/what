package com.neishenme.what.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hyphenate.easeui.EaseConstant;
import com.neishenme.what.R;
import com.neishenme.what.activity.LoginActivity;
import com.neishenme.what.activity.MyFriendsActivity;
import com.neishenme.what.activity.RecognizedPeopleActivity;
import com.neishenme.what.adapter.RecognizedAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.bean.LoginResponse;
import com.neishenme.what.bean.MyFriendsResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.eventbusobj.ChatInfoBean;
import com.neishenme.what.huanxinchat.ui.ChatActivity;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.AppManager;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.HuanXinUtils;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.utils.PackageVersion;
import com.neishenme.what.utils.SoftInputUtils;
import com.neishenme.what.utils.UpdataPersonInfo;

import org.seny.android.utils.MD5Utils;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.neishenme.what.R.id.mLv;
import static com.neishenme.what.R.id.view;

/**
 * 作者：zhaozh create on 2016/5/11 17:14
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 我认识的人 联系人模块
 * .
 * 其作用是 :
 */
public class MyFriendsContentFragment extends Fragment implements View.OnClickListener, HttpLoader.ResponseListener {
    private MyFriendsActivity myFriendsActivity;

    private PullToRefreshListView mFragmentMyFriendsContentList;

    private int page = 0;
    private List<MyFriendsResponse.DataEntity.FriendsEntity> allFriends;
    private RecognizedAdapter adapter;

    private boolean hasMoreFriends = false;

    public MyFriendsContentFragment() {
    }

    public static MyFriendsContentFragment newInstance() {
        MyFriendsContentFragment myFriendsContentFragment = new MyFriendsContentFragment();
        return myFriendsContentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_friends_content, null);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {
        mFragmentMyFriendsContentList = (PullToRefreshListView) view.findViewById(R.id.fragment_my_friends_content_list);

    }

    private void initListener() {
        mFragmentMyFriendsContentList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                adapter = null;
                page = 0;
                requestMyFriends();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                requestMyFriends();
            }
        });

        mFragmentMyFriendsContentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (HuanXinUtils.isLoginToHX()) {
                    MyFriendsResponse.DataEntity.FriendsEntity friendsEntity =
                            allFriends.get(position - 1);
                    ChatInfoBean chatInfoBean = new ChatInfoBean(
                            friendsEntity.getLogo(),
                            String.valueOf(friendsEntity.getId()),
                            friendsEntity.getName());
                    EventBus.getDefault().postSticky(chatInfoBean);
                    App.EDIT.remove(friendsEntity.getHxUserName()).commit();
                    startActivity(new Intent(myFriendsActivity, ChatActivity.class)
                            .putExtra(EaseConstant.EXTRA_USER_ID, friendsEntity.getHxUserName()));
                } else {
                    HuanXinUtils.logoutToHX();
                    myFriendsActivity.showToastError("聊天服务器异常,请返回重试");
                    return;
                }
            }
        });
    }

    private void initData() {
        myFriendsActivity = (MyFriendsActivity) getActivity();

        mFragmentMyFriendsContentList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        requestMyFriends();
    }


    @Override
    public void onClick(View v) {

    }

    private void requestMyFriends() {
        HashMap params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("page", page + "");
        params.put("pageSize", "10");
        HttpLoader.post(ConstantsWhatNSM.URL_RECOGNIZED_PEOPLE, params, MyFriendsResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_RECOGNIZED_PEOPLE, this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_RECOGNIZED_PEOPLE
                && response instanceof MyFriendsResponse) {
            mFragmentMyFriendsContentList.onRefreshComplete();
            MyFriendsResponse myFriendsResponse = (MyFriendsResponse) response;
            if (myFriendsResponse.getCode() == 1) {
                List<MyFriendsResponse.DataEntity.FriendsEntity> friends = myFriendsResponse.getData().getFriends();
                if (friends == null || friends.size() == 0) {
                    hasMoreFriends = false;
                    mFragmentMyFriendsContentList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                } else {
                    hasMoreFriends = true;
                    mFragmentMyFriendsContentList.setMode(PullToRefreshBase.Mode.BOTH);
                }
                if (null == adapter || page == 0) {
                    allFriends = friends;
                    adapter = new RecognizedAdapter(myFriendsActivity, allFriends, true);
                    mFragmentMyFriendsContentList.setAdapter(adapter);
                } else {
                    allFriends.addAll(friends);
                    adapter.notifyDataSetChanged();
                }

                adapter.setOnScreenFriend(new RecognizedAdapter.OnScreenFriend() {
                    @Override
                    public void onScreenFriend(int position) {
                        allFriends.clear();
                        adapter = null;
                        page = 0;
                        requestMyFriends();
                    }
                });
            } else {
                myFriendsActivity.showToastInfo(myFriendsResponse.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        HttpLoader.cancelRequest(this);
    }
}
