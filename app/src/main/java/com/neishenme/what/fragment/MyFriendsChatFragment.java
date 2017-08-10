package com.neishenme.what.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.neishenme.what.R;
import com.neishenme.what.activity.MyFriendsActivity;
import com.neishenme.what.adapter.FriendsNewsAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.eventbusobj.ChatInfoBean;
import com.neishenme.what.huanxinchat.NSMHXHelper;
import com.neishenme.what.huanxinchat.domain.Constant;
import com.neishenme.what.huanxinchat.ui.ChatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

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
public class MyFriendsChatFragment extends Fragment {
    private MyFriendsActivity myFriendsActivity;
    private ListView mFragmentMyFriendsChatList;

    private String hxUserName;  //自己的环信用户名
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    private String myHxUserName = App.USERSP.getString("hxUserName", "");
    private FriendsNewsAdapter adapter;

    public MyFriendsChatFragment() {
    }

    public static MyFriendsChatFragment newInstance() {
        MyFriendsChatFragment myFriendsChatFragment = new MyFriendsChatFragment();
        return myFriendsChatFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_friends_chat, null);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {
        mFragmentMyFriendsChatList = (ListView) view.findViewById(R.id.fragment_my_friends_chat_list);
    }

    private void initListener() {
        mFragmentMyFriendsChatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation emConversation = conversationList.get(position);

                EMMessage emMessageInfo = null;
                for (EMMessage message : emConversation.getAllMessages()) {
                    if (message.getTo().equals(App.USERSP.getString("hxUserName", ""))) {
                        emMessageInfo = message;
                        break;
                    }
                }
                if (emMessageInfo == null) {
                    return;
                }
                String sendUserId = emMessageInfo.getStringAttribute(Constant.SEND_USER_ID, "");

                //sendUserId有值并且 消息来源不是nsmserver
                if (TextUtils.isEmpty(sendUserId) && !emMessageInfo.getFrom().equalsIgnoreCase(Constant.SEND_USER_NSM_SERVER)) {
                    sendUserId = emMessageInfo.getFrom().substring(3);
                    //来自内什么小助手
                } else if (emMessageInfo.getFrom().equalsIgnoreCase(Constant.SEND_USER_NSM_SERVER)) {
                    sendUserId = "0";
                }

                if (TextUtils.isEmpty(sendUserId)) {
                    myFriendsActivity.showToastError("连接不到服务器");
                    return;
                }

                String sendUserName = emMessageInfo.getStringAttribute(Constant.SEND_USER_NAME, "").trim();
                if (TextUtils.isEmpty(sendUserName) && emMessageInfo.getFrom().equalsIgnoreCase(Constant.SEND_USER_NSM_SERVER)) {
                    sendUserName = Constant.SEND_USER_NSM_NAME;
                }

                ChatInfoBean chatInfoBean = new ChatInfoBean(
                        emMessageInfo.getStringAttribute(Constant.SEND_USER_LOGO, ""),
                        sendUserId, sendUserName);

                EventBus.getDefault().postSticky(chatInfoBean);
                startActivity(new Intent(myFriendsActivity, ChatActivity.class)
                        .putExtra(EaseConstant.EXTRA_USER_ID, emMessageInfo.getFrom()));
            }
        });
    }

    private void initData() {
        myFriendsActivity = (MyFriendsActivity) getActivity();
        hxUserName = App.USERSP.getString("hxUserName", "");
        if (TextUtils.isEmpty(hxUserName)) {
            myFriendsActivity.showToastInfo("连接不到聊天服务器");
            myFriendsActivity.finish();
            return;
        }

        conversationList.addAll(loadConversationList());
        if (conversationList != null && conversationList.size() != 0) {
            adapter = new FriendsNewsAdapter(myFriendsActivity, conversationList);
            mFragmentMyFriendsChatList.setAdapter(adapter);
        } else {
            myFriendsActivity.showContentFragment();
        }
    }

    protected List<EMConversation> loadConversationList() {
        // 获取所有会话，包括陌生人
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(
                            new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }

        List<EMConversation> allConversation = new ArrayList<EMConversation>();
        if (TextUtils.isEmpty(myHxUserName)) {
            myHxUserName = App.USERSP.getString("hxUserName", "");
        }
        for (EMConversation emConversation : list) {
            if (!emConversation.conversationId().equalsIgnoreCase(Constant.SEND_USER_NSM_SERVER)) {
                for (EMMessage message : emConversation.getAllMessages()) {
                    if (message.getTo().equals(myHxUserName)) {     //给我的
                        allConversation.add(emConversation);
                        break;
                    }
                }
            }
        }
        return allConversation;
    }

    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    public void refresh() {
        conversationList.clear();
        conversationList.addAll(loadConversationList());
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }
}
