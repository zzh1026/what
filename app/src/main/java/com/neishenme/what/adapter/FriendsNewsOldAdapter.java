package com.neishenme.what.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.neishenme.what.R;
import com.neishenme.what.application.App;
import com.neishenme.what.huanxinchat.domain.Constant;
import com.neishenme.what.net.HttpLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/5/19.
 *
 * 弃用的好友消息适配器,老的,作为备份存在
 */
@Deprecated
public class FriendsNewsOldAdapter extends AddHobbySelectableAdapter {
    private Context mContext;
    private List<EMConversation> conversationList;

//    private String myHxUserName = App.USERSP.getString("hxUserName", "");

    public FriendsNewsOldAdapter(Context mContext, List<EMConversation> mConversationList) {
        this.mContext = mContext;
//        if (TextUtils.isEmpty(myHxUserName)) {
//            myHxUserName = App.USERSP.getString("hxUserName", "");
//        }
//        this.conversationList = new ArrayList<>();
//        for (EMConversation emConversation : mConversationList) {
//            for (EMMessage message : emConversation.getAllMessages()) {
//                if (message.getTo().equals(myHxUserName)) {   //给我的
//                    if (!message.getFrom().equalsIgnoreCase(Constant.SEND_USER_NSM_SERVER)) //来自内什么小助手
//                        this.conversationList.add(emConversation);
//                    break;
//                }
//            }
//        }
//        if (this.conversationList == null || this.conversationList.size() == 0) {
//            MyFriendsActivity myFriendsActivity = (MyFriendsActivity) mContext;
//            myFriendsActivity.showContentFragment();
//        }
        this.conversationList = mConversationList;
    }


    public void bindData(List<EMConversation> conversationList) {
        this.conversationList = conversationList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (conversationList != null)
            return conversationList.size();
        return 0;
    }

    @Override
    public EMConversation getItem(int position) {
        return conversationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_friends_news, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        EMConversation item = getItem(position);
        int unreadMsgCount = item.getUnreadMsgCount();
        if (unreadMsgCount == 0) {
            holder.mRriendsNewsNumber.setVisibility(View.INVISIBLE);
            holder.mRriendsNewsNumber.setText("0");
        } else {
            holder.mRriendsNewsNumber.setVisibility(View.VISIBLE);
            holder.mRriendsNewsNumber.setText(String.valueOf(unreadMsgCount));
        }

        EMMessage emMessageText = item.getLastMessage();
        EMMessage emMessageInfo = null;
        for (EMMessage message : item.getAllMessages()) {
            if (message.getTo().equals(App.USERSP.getString("hxUserName", ""))) {
                emMessageInfo = message;
                break;
            }
        }
        String ticker = EaseCommonUtils.getMessageDigest(emMessageText, mContext);
        if (emMessageText.getType() == EMMessage.Type.TXT) {
            ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
        }
        holder.mTvLastTolk.setText(ticker);
        String userName = emMessageInfo.getStringAttribute(Constant.SEND_USER_NAME, "");
        if (!TextUtils.isEmpty(userName)) {
            holder.mTvUserName.setText(userName);
        } else {
            if (emMessageInfo.getFrom().equalsIgnoreCase(Constant.SEND_USER_NSM_SERVER)) {
                holder.mTvUserName.setText(Constant.SEND_USER_NSM_NAME);
            } else {
                holder.mTvUserName.setText("未知好友");
            }
        }
        String StringUserLogo = emMessageInfo.getStringAttribute(Constant.SEND_USER_LOGO, "");
        if (!TextUtils.isEmpty(StringUserLogo)) {
            HttpLoader.getImageLoader().get(StringUserLogo,
                    ImageLoader.getImageListener(holder.mIvUserLogo, R.drawable.picture_moren, R.drawable.picture_moren));
        } else {
            holder.mIvUserLogo.setImageResource(R.drawable.push_actionbar);
        }

        return convertView;
    }

    class ViewHolder {
        private ImageView mIvUserLogo;
        private TextView mTvLastTolk;
        private TextView mTvUserName;
        private TextView mRriendsNewsNumber;

        public ViewHolder(View view) {
            mIvUserLogo = (ImageView) view.findViewById(R.id.iv_user_logo);
            mTvLastTolk = (TextView) view.findViewById(R.id.tv_last_tolk);
            mTvUserName = (TextView) view.findViewById(R.id.tv_user_name);
            mRriendsNewsNumber = (TextView) view.findViewById(R.id.friends_news_number);
        }
    }


}
