package com.neishenme.what.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.neishenme.what.R;
import com.neishenme.what.adapter.FriendsNewsAdapter;
import com.neishenme.what.application.App;
import com.neishenme.what.base.BaseActivity;
import com.neishenme.what.eventbusobj.ChatInfoBean;
import com.neishenme.what.huanxinchat.domain.Constant;
import com.neishenme.what.huanxinchat.ui.ChatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 作者：zhaozh create on 2016/5/11 15:31
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class FriendsNewsActivity extends BaseActivity {

    private ImageView mIvFocusBack;
    private ListView mLvFocus;
    private String hxUserName;
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    private FriendsNewsAdapter adapter;


    @Override
    protected int initContentView() {
        return R.layout.activity_friends_news;
    }

    @Override
    protected void initView() {
        mIvFocusBack = (ImageView) findViewById(R.id.iv_focus_back);
        mLvFocus = (ListView) findViewById(R.id.lv_focus);
    }

    @Override
    protected void initListener() {
        mIvFocusBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLvFocus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation emConversation = conversationList.get(position);

                EMMessage emMessageInfo = null;
                for (EMMessage message : emConversation.getAllMessages()) {
                    if (message.getTo().equals(App.USERSP.getString("hxUserName", ""))) {
                        emMessageInfo = message;
                    }
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
                    showToastError("连接不到服务器");
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
                startActivity(new Intent(FriendsNewsActivity.this, ChatActivity.class)
                        .putExtra(EaseConstant.EXTRA_USER_ID, emMessageInfo.getFrom()));
            }
        });
    }

    @Override
    protected void initData() {
        hxUserName = App.USERSP.getString("hxUserName", "");
        if (TextUtils.isEmpty(hxUserName)) {
            showToastInfo("连接不到聊天服务器");
            finish();
            return;
        }

        conversationList.addAll(loadConversationList());
        if (conversationList != null && conversationList.size() != 0) {
//            adapter = new FriendsNewsAdapter(this, conversationList);
//            mLvFocus.setAdapter(adapter);
        }
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

    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (EMConversation emConversation : conversationList) {
            emConversation.markAllMessagesAsRead();
            EMMessage emMessageInfo = null;
            for (EMMessage message : emConversation.getAllMessages()) {
                if (!message.getFrom().equals(App.USERSP.getString("hxUserName", ""))) {
                    emMessageInfo = message;
                    break;
                }
            }
            if (emMessageInfo != null)
                EMClient.getInstance().chatManager().deleteConversation(emMessageInfo.getFrom(), false);
        }
    }
}
