package com.neishenme.what.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hyphenate.easeui.EaseConstant;
import com.neishenme.what.R;
import com.neishenme.what.adapter.HomeNewsAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.bean.HomeNewsResponse;
import com.neishenme.what.bean.MyFriendsResponse;
import com.neishenme.what.bean.NsmServerInfoResponse;
import com.neishenme.what.bean.RBResponse;
import com.neishenme.what.db.dao.HomeNewsDao;
import com.neishenme.what.eventbusobj.ChatInfoBean;
import com.neishenme.what.huanxinchat.NSMHXHelper;
import com.neishenme.what.huanxinchat.ui.ChatActivity;
import com.neishenme.what.net.HttpLoader;
import com.neishenme.what.utils.ConstantsWhatNSM;
import com.neishenme.what.utils.HuanXinUtils;
import com.neishenme.what.utils.NSMTypeUtils;

import org.seny.android.utils.ActivityUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作者：zhaozh create on 2016/6/15 13:23
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class HomeNewsActivity extends BaseActivity implements HttpLoader.ResponseListener, NSMHXHelper.OnMessageReceived {
    //    public static final String FLAG_HAVE_MESSAGE_FRIENDS = "messagefriends";    //标记是否有好友消息
//    public static final String FLAG_HAVE_MESSAGE_HELPER = "messagensm";     //标记是否有内什么小助手消息
    public static final String FLAG_HAVE_MESSAGE_FRIENDS = "messagefriendsnum";    //标记好友消息数量
    public static final String FLAG_HAVE_MESSAGE_HELPER = "messagehelpernum";     //标记内什么小助手消息数量

    private ImageView mHomeNewsBack;
    private LinearLayout mHomeNewsMessageFriends;
    private TextView mHomeNewsMessageNumberFriends;
    private LinearLayout mHomeNewsMessageHelper;
    private TextView mHomeNewsMessageNumberHelper;
    private ListView mHomeNewsMessageList;
    private ImageView mHomeNewsNoMessage;

    private LinearLayout mHomeNewsMyFriends;
    private LinearLayout mHomeNewsMyFocus;

    private HomeNewsAdapter mHomeNewsAdapter;

    private HomeNewsDao dao;

    private NsmServerInfoResponse.DataBean.NsmBean nsmServerInfo;

    @Override
    protected int initContentView() {
        return R.layout.activity_home_news;
    }

    @Override
    protected void initView() {
        mHomeNewsBack = (ImageView) findViewById(R.id.home_news_back);

        mHomeNewsMessageFriends = (LinearLayout) findViewById(R.id.home_news_message_friends);
        mHomeNewsMessageNumberFriends = (TextView) findViewById(R.id.home_news_message_number_friends);

        mHomeNewsMessageHelper = (LinearLayout) findViewById(R.id.home_news_message_helper);
        mHomeNewsMessageNumberHelper = (TextView) findViewById(R.id.home_news_message_number_helper);

        mHomeNewsMessageList = (ListView) findViewById(R.id.home_news_message_list);
        mHomeNewsNoMessage = (ImageView) findViewById(R.id.home_news_no_message);

        mHomeNewsMyFriends = (LinearLayout) findViewById(R.id.home_news_my_friends);
        mHomeNewsMyFocus = (LinearLayout) findViewById(R.id.home_news_my_focus);
    }

    @Override
    protected void onStart() {
        super.onStart();
        int friendsMessageNumber = App.SP.getInt(FLAG_HAVE_MESSAGE_FRIENDS, 0);
        if (friendsMessageNumber == 0) {
            mHomeNewsMessageNumberFriends.setVisibility(View.INVISIBLE);
            mHomeNewsMessageNumberFriends.setText("0");
        } else {
            mHomeNewsMessageNumberFriends.setVisibility(View.VISIBLE);
            mHomeNewsMessageNumberFriends.setText(String.valueOf(friendsMessageNumber));
        }

        int helperMessageNumber = App.SP.getInt(FLAG_HAVE_MESSAGE_HELPER, 0);
        if (helperMessageNumber == 0) {
            mHomeNewsMessageNumberHelper.setVisibility(View.INVISIBLE);
            mHomeNewsMessageNumberHelper.setText("0");
        } else {
            mHomeNewsMessageNumberHelper.setVisibility(View.VISIBLE);
            mHomeNewsMessageNumberHelper.setText(String.valueOf(helperMessageNumber));
        }

        if (mHomeNewsAdapter != null) {
            mHomeNewsAdapter.setHomeRes(dao.findAllHN());
        }
    }

    @Override
    protected void initListener() {
        mHomeNewsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mHomeNewsMessageFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(HomeNewsActivity.this, FriendsNewsActivity.class);
            }
        });

        mHomeNewsMessageHelper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != nsmServerInfo) {
                    if (HuanXinUtils.isLoginToHX()) {
                        ChatInfoBean chatInfoBean = new ChatInfoBean(
                                nsmServerInfo.getLogo(),
                                "0",
                                nsmServerInfo.getName());
                        EventBus.getDefault().postSticky(chatInfoBean);
                        App.EDIT.remove(FLAG_HAVE_MESSAGE_HELPER).commit();
                        startActivity(new Intent(HomeNewsActivity.this, ChatActivity.class)
                                .putExtra(EaseConstant.EXTRA_USER_ID, nsmServerInfo.getHxUserName()));
                    } else {
                        showToastError("连接异常,请退出重试");
                        HuanXinUtils.logoutToHX();
                    }
                } else {
                    showToastError("连接失败");
                }
            }
        });

        mHomeNewsMyFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ActivityUtils.startActivity(HomeNewsActivity.this, RecognizedPeopleActivity.class);
                App.EDIT.remove(FLAG_HAVE_MESSAGE_FRIENDS).commit();
                ActivityUtils.startActivity(HomeNewsActivity.this, MyFriendsActivity.class);
            }
        });

        mHomeNewsMyFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(HomeNewsActivity.this, FocusPeopleActivity.class);
            }
        });
    }

    @Override
    protected void initData() {
        dao = HomeNewsDao.getInstance(this);
        getHomeNews();
        getNsmServerInfo();

        if (!HuanXinUtils.isLoginToHX()) {
            HuanXinUtils.login();
        }

        NSMHXHelper.getInstance().regestMessageReceived(this);
    }

    private void getHomeNews() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", App.SP.getString("token", null));
        HttpLoader.post(ConstantsWhatNSM.URL_USER_HOME_NEWS, params, HomeNewsResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_USER_HOME_NEWS, this, false).setTag(this);
    }

    private void getNsmServerInfo() {
        HashMap params = new HashMap();
        params.put("token", NSMTypeUtils.getMyToken());
        HttpLoader.post(ConstantsWhatNSM.URL_GET_NSM_SERVER_INFO, params, NsmServerInfoResponse.class,
                ConstantsWhatNSM.REQUEST_CODE_GET_NSM_SERVER_INFO, this, false).setTag(this);
    }

    @Override
    public void onGetResponseSuccess(int requestCode, RBResponse response) {
        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_USER_HOME_NEWS
                && response instanceof HomeNewsResponse) {
            HomeNewsResponse homeNewsResponse = (HomeNewsResponse) response;
            if (1 == homeNewsResponse.getCode()) {
                if (homeNewsResponse.getData().getMessages() == null ||
                        homeNewsResponse.getData().getMessages().size() == 0) {
                    if (dao.hasInfo()) {
                        setInfo();
                    } else {
                        mHomeNewsNoMessage.setVisibility(View.VISIBLE);
                    }
                } else {
                    mHomeNewsNoMessage.setVisibility(View.GONE);
                    addToDao(homeNewsResponse.getData().getMessages());
                    setInfo();
                }
            }
        }

        if (requestCode == ConstantsWhatNSM.REQUEST_CODE_GET_NSM_SERVER_INFO
                && response instanceof NsmServerInfoResponse) {
            NsmServerInfoResponse myFriendsResponse = (NsmServerInfoResponse) response;
            int code = myFriendsResponse.getCode();
            if (1 == code) {
                nsmServerInfo = myFriendsResponse.getData().getNsm();
            }
        }
    }

    @Override
    public void onGetResponseError(int requestCode, VolleyError error) {

    }

    @Override
    public void onMessageFriends(final int messageNumber) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mHomeNewsMessageNumberFriends.setVisibility(View.VISIBLE);
                mHomeNewsMessageNumberFriends.setText(String.valueOf(messageNumber));
            }
        });
    }

    @Override
    public void onMessageHelper(final int messageNumber) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mHomeNewsMessageNumberHelper.setVisibility(View.VISIBLE);
                mHomeNewsMessageNumberHelper.setText(String.valueOf(messageNumber));
            }
        });
    }

    private void setInfo() {
        mHomeNewsAdapter = new HomeNewsAdapter(this, dao.findAllHN());
        mHomeNewsMessageList.setAdapter(mHomeNewsAdapter);
    }

    private void addToDao(List<HomeNewsResponse.DataEntity.MessagesEntity> messages) {
        Collections.reverse(messages);
        for (HomeNewsResponse.DataEntity.MessagesEntity message : messages) {
            dao.add(String.valueOf(message.getId()), message.getContext(),
                    String.valueOf(message.getData().getInviteId()), String.valueOf(message.getData().getJoinerId()),
                    String.valueOf(message.getData().getUserId()), String.valueOf(message.getUpdateTime()),
                    String.valueOf(message.getType()), message.getData().getLink());
        }
    }

    public void lookInfoForId(String id, int position) {
        if (dao.delete(id)) {
//            mHomeNewsAdapter.removeInfo(position);
        }
    }

    @Override
    protected void onDestroy() {
        NSMHXHelper.getInstance().unRegestMessageReceived();
        super.onDestroy();
    }
}
