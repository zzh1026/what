package com.neishenme.what.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.neishenme.what.R;
import com.neishenme.what.activity.ActiveActivity;
import com.neishenme.what.activity.MyActiveActivity;
import com.neishenme.what.adapter.ActiveAddFragAdapter;
import com.neishenme.what.bean.ActiveMyTakeResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.NSMTypeUtils;
import com.neishenme.what.view.ListViewAdjustHeight;

import org.seny.android.utils.ActivityUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 我的活动我参与的
 */
public class ActiveMyAddFragment extends Fragment implements HttpLoader.ResponseListener {
    private static ActiveMyAddFragment myAddFragment;
    public static Object object = new Object();
    private MyActiveActivity myActiveActivity;

    private int page = 1;       //标记当前请求页面
    private boolean hsmMoreInfo = false;    //标记是否有更多信息

    private ImageView mActiveFragUserlogo;
    private TextView mActiveFragJoinTicketNum;
    private ImageView mActiveFragUserGender;
    private TextView mActiveFragUsername;
    private TextView mActiveFragJoinNum;
    private TextView mActiveFragTicketNum;
    private ListViewAdjustHeight mActiveFragListview;
    private LinearLayout mActiveJoinLl;
    private PullToRefreshScrollView activeMyAddScroll;
    private LinearLayout mActiveMyAddLl;

    private ActiveMyTakeResponse.DataBean myTakeResponseData;    //返回的值
    private ActiveAddFragAdapter mActiveAddFragAdapter;         //我参与的列表适配器
    private List<ActiveMyTakeResponse.DataBean.ListBean> myTakeResponseDataList;    //我参与的列表所有数据

    public ActiveMyAddFragment() {
    }

    //获取对象
    public static ActiveMyAddFragment newInstance() {
        if (myAddFragment == null) {
            synchronized (object) {
                if (myAddFragment == null) {
                    myAddFragment = new ActiveMyAddFragment();
                }
            }
        }
        return myAddFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_active_add, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {
        mActiveFragUserlogo = (ImageView) view.findViewById(R.id.active_frag_userlogo);
        mActiveFragJoinTicketNum = (TextView) view.findViewById(R.id.active_frag_join_ticket_num);
        mActiveFragUserGender = (ImageView) view.findViewById(R.id.active_frag_user_gender);
        mActiveFragUsername = (TextView) view.findViewById(R.id.active_frag_username);
        mActiveFragJoinNum = (TextView) view.findViewById(R.id.active_frag_join_num);
        mActiveFragTicketNum = (TextView) view.findViewById(R.id.active_frag_ticket_num);
        mActiveFragListview = (ListViewAdjustHeight) view.findViewById(R.id.active_frag_listview);
        mActiveJoinLl = (LinearLayout) view.findViewById(R.id.active_join_ll);
        activeMyAddScroll = (PullToRefreshScrollView) view.findViewById(R.id.active_my_add_scroll);
        activeMyAddScroll.setMode(PullToRefreshBase.Mode.BOTH);

        mActiveMyAddLl = (LinearLayout) view.findViewById(R.id.active_my_add_ll);

    }

    private void initData() {
        myActiveActivity = (MyActiveActivity) getActivity();
        requestMyTake();
    }

    private void initListener() {
        activeMyAddScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                requestMyTake();
            }
        });

        activeMyAddScroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 1;
                mActiveAddFragAdapter = null;
                requestMyTake();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (hsmMoreInfo) {
                    page++;
                    requestMyTake();
                } else {
                    showToast("暂无更多信息!");
                    activeMyAddScroll.onRefreshComplete();
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

    private void requestMyTake() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", NSMTypeUtils.getMyToken());
        params.put("page", page + "");
        params.put("pageSize", "20");
        HttpLoader.post(ConstantsWhatNSM.URL_ACTIVE_MY_TAKE, params, ActiveMyTakeResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_ACTIVE_MY_TAKE, this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_ACTIVE_MY_TAKE
                && response instanceof ActiveMyTakeResponse) {
            activeMyAddScroll.onRefreshComplete();
            ActiveMyTakeResponse myTakeResponse = (ActiveMyTakeResponse) response;
            if (myTakeResponse.getCode() == 1) {
                hsmMoreInfo = myTakeResponse.getData().isHasMore();
                if (myTakeResponse.getData().getMytakemeout() == 1) {
                    mActiveMyAddLl.setVisibility(View.VISIBLE);
                    mActiveJoinLl.setVisibility(View.INVISIBLE);
                    if (mActiveAddFragAdapter == null) {
                        myTakeResponseData = myTakeResponse.getData();
                        myTakeResponseDataList = myTakeResponseData.getList();
                        dispathUserInfo();
                        dispathJoinerInfo();
                    } else {
                        myTakeResponseDataList.addAll(myTakeResponse.getData().getList());
                        mActiveAddFragAdapter.notifyDataSetChanged();
                    }
                } else {
                    mActiveJoinLl.setVisibility(View.VISIBLE);
                    mActiveMyAddLl.setVisibility(View.INVISIBLE);
                }
            } else {
                mActiveJoinLl.setVisibility(View.VISIBLE);
                mActiveMyAddLl.setVisibility(View.INVISIBLE);
                myActiveActivity.showToastInfo(myTakeResponse.getMessage());
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {
        activeMyAddScroll.onRefreshComplete();
        showToast("网络获取失败,请重试!");
    }

    private void showToast(String message) {
        myActiveActivity.showToastInfo(message);
    }

    private void dispathUserInfo() {
        String userLogo = myTakeResponseData.getThumbnailslogo();
        if (TextUtils.isEmpty(userLogo)) {
            mActiveFragUserlogo.setImageResource(R.drawable.picture_moren);
        } else {
            HttpLoader.getImageLoader().get(userLogo,
                    ImageLoader.getImageListener(mActiveFragUserlogo, R.drawable.picture_moren, R.drawable.picture_moren));
        }

        mActiveFragJoinTicketNum.setText(myTakeResponseData.getTickets() + "");
        mActiveFragUserGender.setImageResource(myTakeResponseData.getGender() == 1 ? R.drawable.man_icon : R.drawable.woman_icon);
        mActiveFragUsername.setText(myTakeResponseData.getName());
        mActiveFragJoinNum.setText(myTakeResponseData.getUsers() + "人已约");
        String userId = myTakeResponseData.getId();
        if (userId.length() == 1) {
            mActiveFragTicketNum.setText("00" + myTakeResponseData.getId());
        } else if (userId.length() == 2) {
            mActiveFragTicketNum.setText("0" + myTakeResponseData.getId());
        } else {
            mActiveFragTicketNum.setText(myTakeResponseData.getId());
        }
    }

    private void dispathJoinerInfo() {
        mActiveAddFragAdapter = new ActiveAddFragAdapter(myActiveActivity, myTakeResponseDataList);
        mActiveFragListview.setAdapter(mActiveAddFragAdapter);
    }

    @Override
    public void onDestroy() {
        myAddFragment = null;
        super.onDestroy();
    }
}
