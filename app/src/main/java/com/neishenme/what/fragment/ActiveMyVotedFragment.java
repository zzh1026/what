package com.neishenme.what.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.neishenme.what.R;
import com.neishenme.what.activity.ActiveActivity;
import com.neishenme.what.activity.MyActiveActivity;
import com.neishenme.what.adapter.ActiveVotedFragAdapter;
import com.neishenme.what.bean.ActiveMyJoinResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;

import org.seny.android.utils.ALog;
import org.seny.android.utils.ActivityUtils;

import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class ActiveMyVotedFragment extends Fragment implements HttpLoader.ResponseListener {

    private static ActiveMyVotedFragment myVotedFragment;
    public static Object object = new Object();
    private MyActiveActivity myActiveActivity;

    private PullToRefreshGridView mActiveMyFragPrlv;
    private LinearLayout mActiveJoinLl;

    private boolean hasMoreInfo = false;   //是否有更多
    private int page = 1;
    private ActiveVotedFragAdapter activeVotedFragAdapter;  //适配器
    private List<ActiveMyJoinResponse.DataBean.ListBean> joinListInfos; //所展示的所有数据信息

    public ActiveMyVotedFragment() {
    }

    //获取对象
    public static ActiveMyVotedFragment newInstance() {
        if (myVotedFragment == null) {
            synchronized (object) {
                if (myVotedFragment == null) {
                    myVotedFragment = new ActiveMyVotedFragment();
                }
            }
        }
        return myVotedFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_active_voted, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {
        mActiveJoinLl = (LinearLayout) view.findViewById(R.id.active_join_ll);
        mActiveMyFragPrlv = (PullToRefreshGridView) view.findViewById(R.id.active_my_frag_prlv);
        mActiveMyFragPrlv.setMode(PullToRefreshBase.Mode.BOTH);
    }

    private void initData() {
        myActiveActivity = (MyActiveActivity) getActivity();
        page = 1;
        requestMyJoinList();
    }

    private void initListener() {
//        mActiveMyFragPrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                activeVotedFragAdapter = null;
//                page = 1;
//                requestMyJoinList();
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                showToast("加载更多!");
//                mActiveMyFragPrlv.onRefreshComplete();
//                if (hasMoreInfo) {
//                    page++;
//                    requestMyJoinList();
//                } else {
//                    mActiveMyFragPrlv.onRefreshComplete();
//                    showToast("暂无更多信息!");
//                }
//            }
//        });
        mActiveMyFragPrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                activeVotedFragAdapter = null;
                page = 1;
                requestMyJoinList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                if (hasMoreInfo) {
                    page++;
                    requestMyJoinList();
                } else {
                    mActiveMyFragPrlv.onRefreshComplete();
                    showToast("暂无更多信息!");
                }
            }
        });

        mActiveJoinLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(myActiveActivity, ActiveActivity.class);
            }
        });
    }

    private void requestMyJoinList() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("page", page + "");
        params.put("pageSize", "20");
        HttpLoader.post(ConstantsWhatNSM.URL_ACTIVE_MY_JOIN_LIST, params, ActiveMyJoinResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_MY_JOINLIST, this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_MY_JOINLIST
                && response instanceof ActiveMyJoinResponse) {
            mActiveMyFragPrlv.onRefreshComplete();
            ActiveMyJoinResponse myJoinResponse = (ActiveMyJoinResponse) response;
            if (myJoinResponse.getCode() == 1) {
                hasMoreInfo = myJoinResponse.getData().isHasMore();
                if (activeVotedFragAdapter == null) {
                    joinListInfos = myJoinResponse.getData().getList();
                    activeVotedFragAdapter = new ActiveVotedFragAdapter(myActiveActivity, joinListInfos);
                    mActiveMyFragPrlv.setAdapter(activeVotedFragAdapter);
                } else {
                    joinListInfos.addAll(myJoinResponse.getData().getList());
                    activeVotedFragAdapter.notifyDataSetChanged();
                }

                if (joinListInfos == null || joinListInfos.size() == 0) {
                    mActiveJoinLl.setVisibility(View.VISIBLE);
                    mActiveMyFragPrlv.setVisibility(View.INVISIBLE);
                } else {
                    mActiveJoinLl.setVisibility(View.INVISIBLE);
                    mActiveMyFragPrlv.setVisibility(View.VISIBLE);
                }
            } else {
                myActiveActivity.showToastInfo(myJoinResponse.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        mActiveMyFragPrlv.onRefreshComplete();
        showToast("网络获取失败,请重试!");
    }

    private void showToast(String message) {
        myActiveActivity.showToastInfo(message);
    }

    @Override
    public void onDestroy() {
        myVotedFragment = null;
        super.onDestroy();
    }
}
