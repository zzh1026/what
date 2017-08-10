package com.neishenme.what.huanxinchat.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.EaseChatInputMenu;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.neishenme.what.R;
import com.neishenme.what.application.App;
import com.neishenme.what.bean.ChatInfoTypeBean;
import com.neishenme.what.huanxinchat.domain.Constant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zzh
 * 自定义ChatFragment类
 */
public class ChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {

    private ChatActivity mActivity;
    private Gson gson;
    private ChatInfoTypeBean chatInfoTypeBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (ChatActivity) getActivity();
        return inflater.inflate(R.layout.ease_fragment_chat, container, false);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void setUpView() {
        setChatFragmentListener(this);
        gson = new Gson();
        super.setUpView();
        //((EaseEmojiconMenu) inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());
    }

    @Override
    protected void registerExtendMenuItem() {
        super.registerExtendMenuItem();
    }

    /**
     * 设置消息扩展属性
     */
    @Override
    public void onSetMessageAttributes(EMMessage message) {
        String userName = mActivity.getSendUserName();
        String userLogo = mActivity.getSendUserLogo();
        String userId = mActivity.getSendUserId();

        chatInfoTypeBean = new ChatInfoTypeBean();
        ChatInfoTypeBean.VisitorEntity visitorEntity = new ChatInfoTypeBean.VisitorEntity();
        String phones = App.USERSP.getString("phones", "");
        if (!TextUtils.isEmpty(phones)) {
            visitorEntity.setPhone(phones);
        }
        visitorEntity.setTrueName(userName);
        chatInfoTypeBean.setVisitor(visitorEntity);

        message.setAttribute(Constant.SEND_USER_NAME, userName);
        message.setAttribute(Constant.SEND_USER_LOGO, userLogo);
        try {
            message.setAttribute(Constant.SEND_USER_TYPE, new JSONObject(gson.toJson(chatInfoTypeBean)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message.setAttribute(Constant.SEND_USER_ID, userId);
    }

    public EaseChatInputMenu getEaseChatInputMenu() {
        return inputMenu;
    }

    /**
     * 进入会话详情
     */
    @Override
    public void onEnterToChatDetails() {

    }

    /**
     * 用户头像点击事件
     *
     * @param username
     */
    @Override
    public void onAvatarClick(String username) {

    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    /**
     * 消息气泡框点击事件
     */
    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    /**
     * 消息气泡框长按事件
     */
    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    /**
     * 扩展输入栏item点击事件,如果要覆盖EaseChatFragment已有的点击事件，return true
     *
     * @param view
     * @param itemId
     * @return boolean
     */
    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    /**
     * 设置自定义chatrow提供者
     *
     * @return
     */
    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }
}
