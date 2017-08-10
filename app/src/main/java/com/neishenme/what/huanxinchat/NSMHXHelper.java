package com.neishenme.what.huanxinchat;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.model.EaseNotifier;
import com.neishenme.what.R;
import com.neishenme.what.activity.HomeNewsActivity;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.application.App;
import com.neishenme.what.common.AppSharePreConfig;
import com.neishenme.what.eventbusobj.ChatInfoBean;
import com.neishenme.what.eventbusobj.HomeNewsNumber;
import com.neishenme.what.huanxinchat.domain.Constant;
import com.neishenme.what.huanxinchat.ui.ChatActivity;
import com.neishenme.what.utils.UpdataPersonInfo;

import org.seny.android.utils.ALog;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作者：zhaozh create on 2016/5/30 14:27
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class NSMHXHelper {

    private static final int NOTIFICATION_FLAG = 1;

    private Context mContext;

    private OnMessageReceived messageReceived;
    private OnMessageRefreshListener onMessageRefreshListener;

    // 单例对象
    private static NSMHXHelper instance = null;

    private EMConnectionListener connectionListener;

    // EaseUI的实例
    private EaseUI mEaseUI;

    private HomeNewsNumber homeNewsNumber;

    private NSMHXHelper() {
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public synchronized static NSMHXHelper getInstance() {
        if (instance == null) {
            instance = new NSMHXHelper();
        }
        return instance;
    }

    /**
     * 初始化方法，调用EaseUI的初始化，
     *
     * @param context
     */
    public void init(Context context) {
        EMOptions options = initChatOptions();
        if (EaseUI.getInstance().init(context, options)) {
            mContext = context;

            // 获取EaseUI单例对象，为后边设置做准备
            mEaseUI = EaseUI.getInstance();

            EMClient.getInstance().setDebugMode(false);

            // 初始化设置EaseUI定义的一些提供者
            setEaseUIProviders();

            setGlobalListeners();

            // 全局监听消息方法
            registerEventListener();
        }
    }

    private EMOptions initChatOptions() {
        ALog.i("初始化options成功");

        // 获取到EMChatOptions对象
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 设置是否需要已读回执
        options.setRequireAck(false);
        // 设置是否需要已送达回执
        options.setRequireDeliveryAck(false);

        return options;
    }

    /**
     * 设置EaseUI的提供者
     */
    public void setEaseUIProviders() {
        //set emoji icon provider
//        easeUI.setEmojiconInfoProvider(new EaseUI.EaseEmojiconInfoProvider() {
//
//            @Override
//            public EaseEmojicon getEmojiconInfo(String emojiconIdentityCode) {
//                EaseEmojiconGroupEntity data = EmojiconExampleGroupData.getData();
//                for(EaseEmojicon emojicon : data.getEmojiconList()){
//                    if(emojicon.getIdentityCode().equals(emojiconIdentityCode)){
//                        return emojicon;
//                    }
//                }
//                return null;
//            }
//
//            @Override
//            public Map<String, Object> getTextEmojiconMapping() {
//                return null;
//            }
//        });

        /**
         * 设置通知消息提供者
         */
        mEaseUI.getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {
            @Override
            public String getTitle(EMMessage message) {
                String stringAttribute = message.getStringAttribute(Constant.SEND_USER_NAME, "");
                if (TextUtils.isEmpty(stringAttribute)) {
                    return null;
                }
                return stringAttribute;
            }

            /**
             * 设置通知栏小图标，规定要求大小为24dp
             * @param message
             * @return
             */
            @Override
            public int getSmallIcon(EMMessage message) {
                return R.mipmap.lable_logo;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // 设置状态栏的消息提示，可以根据message的类型做相应提示
//                String ticker = EaseCommonUtils.getMessageDigest(message, mContext);
//                if (message.getType() == EMMessage.Type.TXT) {
//                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
//                }
//                //消息来自nsmserver
//                if (message.getFrom().equals("nsmServer")) {
//                    return "客服: " + ticker;
//                } else {
//                    return "您的好友: " + ticker;
//                }
                String chatName = message.getFrom();
                if (!TextUtils.isEmpty(chatName)) {
                    App.EDIT.putBoolean(chatName, true).commit();
                }
                return null;
            }

            /**
             * 根据消息条数来判断如果显示
             * @param message
             *            接收到的消息
             * @param fromUsersNum
             *            发送人的数量
             * @param messageNum
             *            消息数量
             * @return
             */
            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                // 当只有一个人，发来一条消息时，显示消息内容 TODO 表情符显示为图片
                //return fromUsersNum + " 个联系人，发来 " + messageNum + " 条消息";
                return null;
            }

            /**
             * 通知栏点击跳转设置，这里因为只有客服，所以没有其他判断，直接跳转到聊天界面，
             * 把客服username传递过去即可
             * @param message
             *            显示在notification上最近的一条消息
             * @return
             */
            @Override
            public Intent getLaunchIntent(EMMessage message) {
                Intent intent = new Intent();
                //intent.setClass(mContext, MainActivity.class);
//                intent.putExtra(EaseConstant.EXTRA_USER_ID, message.getFrom());
                intent.setClass(mContext, ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, message.getFrom());

                String sendUserId = message.getStringAttribute(Constant.SEND_USER_ID, "");

                //sendUserId有值并且 消息来源不是nsmserver
                if (TextUtils.isEmpty(sendUserId) && !message.getFrom().equalsIgnoreCase(Constant.SEND_USER_NSM_SERVER)) {
                    sendUserId = message.getFrom().substring(3);
                    //来自内什么小助手
                } else if (message.getFrom().equalsIgnoreCase(Constant.SEND_USER_NSM_SERVER)) {
                    sendUserId = "0";
                }

                if (TextUtils.isEmpty(sendUserId)) {
                    return null;
                }

                String sendUserName = message.getStringAttribute(Constant.SEND_USER_NAME, "").trim();
                if (TextUtils.isEmpty(sendUserName) && message.getFrom().equalsIgnoreCase(Constant.SEND_USER_NSM_SERVER)) {
                    sendUserName = Constant.SEND_USER_NSM_NAME;
                }

                ChatInfoBean chatInfoBean = new ChatInfoBean(
                        message.getStringAttribute(Constant.SEND_USER_LOGO, ""),
                        sendUserId, sendUserName);
                EventBus.getDefault().postSticky(chatInfoBean);
                return intent;
            }
        });
    }

    private void setGlobalListeners() {
        //设置全局的连接监听, 是否连接.
        // create the global connection listener
        connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                if (error == EMError.USER_REMOVED) {
                    onCurrentAccountRemoved();
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    onConnectionConflict();
                }
            }

            @Override
            public void onConnected() {

            }
        };

        //注册连接监听
        EMClient.getInstance().addConnectionListener(connectionListener);
    }

    /**
     * 账号被移除
     */
    protected void onCurrentAccountRemoved() {
        ALog.i("帐号被移除登录状态");
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 账号在别的设备登录
     */
    protected void onConnectionConflict() {
        App.EDIT.putBoolean(AppSharePreConfig.USER_LOGIN_BE_T, true).commit();
        UpdataPersonInfo.logoutPersonInfo();

        // 判断如果在app中就理消息通知
        if (isRunningForeground(mContext)) {
            Intent i = new Intent(mContext, MainActivity.class);
            //FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(i);
        } else {
            // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
            NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                    new Intent(mContext, MainActivity.class), 0);
            Notification notify = new Notification.Builder(mContext)
                    .setSmallIcon(R.drawable.push_actionbar)
                    .setTicker("内什么 : 下线通知")
                    .setContentTitle("内什么")
                    .setContentText("您的帐号在其他设备登录,您被迫下线")
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .build();
            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            manager.notify(NOTIFICATION_FLAG, notify);
        }
    }

    private boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        ALog.i("currentPackageName 的值为 :　" + cn.getPackageName());
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(getPackageName())) {
            return true;
        }

        return false;
    }

    private String getPackageName() {
        ALog.i("getPackagename 的值为 :　" + App.getApplication().getPackageName());
        return App.getApplication().getPackageName();
    }

    /**
     * 全局的消息监听事件，这里的监听会一直执行，所以要判断一下程序是否在前台运行阶段，
     * 如果在前台就让前台界面的监听去处理消息（比如MainActivity、ChatActivity里的onEvent）
     */
    private void registerEventListener() {
        EMMessageListener messageListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                ALog.i("接收到了环信消息");
                for (EMMessage message : messages) {
                    if (message.getFrom().equalsIgnoreCase(Constant.SEND_USER_NSM_SERVER)) {    //内什么小助手
                        int helperMessageNumber = App.SP.getInt(HomeNewsActivity.FLAG_HAVE_MESSAGE_HELPER, 0);
                        if (messageReceived != null) {
                            messageReceived.onMessageHelper(helperMessageNumber + 1);

                        }
                        App.EDIT.putInt(HomeNewsActivity.FLAG_HAVE_MESSAGE_HELPER, helperMessageNumber + 1).commit();
                    } else {  //朋友
                        if (onMessageRefreshListener != null) {
                            onMessageRefreshListener.onMessageRefreshListener();
                        } else {
                            int friendsMessageNumber = App.SP.getInt(HomeNewsActivity.FLAG_HAVE_MESSAGE_FRIENDS, 0);
                            if (messageReceived != null) {
                                messageReceived.onMessageFriends(friendsMessageNumber + 1);
                            }
                            App.EDIT.putInt(HomeNewsActivity.FLAG_HAVE_MESSAGE_FRIENDS, friendsMessageNumber + 1).commit();
                        }
                    }

                    //应用在后台，不需要刷新UI,通知栏提示新消息
                    if (!mEaseUI.hasForegroundActivies()) {
                        String s = App.SP.getString("newsNumber", "");
                        if (TextUtils.isEmpty(s)) {
                            App.EDIT.putString("newsNumber", "1").commit();
                            homeNewsNumber = new HomeNewsNumber();
                            homeNewsNumber.num = 1;
                            EventBus.getDefault().post(homeNewsNumber);
                        }
                        getNotifier().onNewMsg(message);
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {

                }
            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }
        };
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    /**
     * 获取EaseUI定义的通知对象，用来发送通知
     *
     * @return
     */

    public EaseNotifier getNotifier() {
        return mEaseUI.getNotifier();
    }

    public static interface OnMessageReceived {
        void onMessageFriends(int messageNumber);

        void onMessageHelper(int messageNumber);
    }

    public void regestMessageReceived(OnMessageReceived onMessageReceived) {
        this.messageReceived = onMessageReceived;
    }

    public void unRegestMessageReceived() {
        this.messageReceived = null;
    }

    public static interface OnMessageRefreshListener {
        void onMessageRefreshListener();
    }

    public void setOnMessageRefreshListener(OnMessageRefreshListener messageRefreshListener) {
        this.onMessageRefreshListener = messageRefreshListener;
    }

    public void removeMessageRefreshListener() {
        this.onMessageRefreshListener = null;
    }

}
