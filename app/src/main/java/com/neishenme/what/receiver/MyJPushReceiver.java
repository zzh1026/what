package com.neishenme.what.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.neishenme.what.activity.InviteInviterDetailActivity;
import com.neishenme.what.activity.InviteJoinerDetailActivity;
import com.neishenme.what.activity.MainActivity;
import com.neishenme.what.activity.MyTripActivity;
import com.neishenme.what.activity.PublishOrderActivity;
import com.neishenme.what.activity.UserDetailActivity;
import com.neishenme.what.application.App;
import com.neishenme.what.bean.JPushResponse;
import com.neishenme.what.eventbusobj.HomeNewsNumber;
import com.neishenme.what.utils.NSMTypeUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.seny.android.utils.ALog;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

/**
 * 作者：zhaozh create on 2016/4/14 17:06
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class MyJPushReceiver extends BroadcastReceiver {
    private final Gson gson = new Gson();
    private static final String TAG = "JPush";
    private int newNumber = 0;
    private HomeNewsNumber homeNewsNumber;

    public static final String MSG_ORDER_INVITE_ID = "orderInviteId";
    private static OnMessageReceived mOnMessageReceived;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        ALog.i("[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            ALog.i("JPush用户注册成功 : " + regId);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            //自定义消息
            ALog.i("JPush接收到推送下来的自定义消息 : " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
//            ALog.i(" title : " + title);
//            String message = bundle.getString(JPushInterface.EXTRA_ALERT);
//            ALog.i("message : " + message);
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            ALog.i("extras : " + extras);

            JPushResponse result = null;
            try {
                String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
                result = gson.fromJson(json, JPushResponse.class);    //解析json
            } catch (JsonSyntaxException e) {

            }
            if (null != result && "order".equals(result.getType())
                    && !TextUtils.isEmpty(result.getInviteId())) {
                if (mOnMessageReceived != null) {
                    mOnMessageReceived.onMessageReceived(result.getInviteId());
                } else {
                    App.EDIT.putString(MSG_ORDER_INVITE_ID, result.getInviteId()).commit();
                }
            } else {
                ALog.i("JPush 接收到推送下来的通知");
                String s = App.SP.getString("newsNumber", "").trim();
                if (TextUtils.isEmpty(s)) {
                    newNumber = 1;
                } else {
                    newNumber = Integer.valueOf(s) + 1;
                }
                App.EDIT.putString("newsNumber", String.valueOf(newNumber)).commit();
                homeNewsNumber = new HomeNewsNumber();
                homeNewsNumber.num = newNumber;
                EventBus.getDefault().post(homeNewsNumber);
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            if (!NSMTypeUtils.isLogin()) {
                toHomeAct(context);
                return;
            }
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            JPushResponse result = null;
            try {
                String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
                result = gson.fromJson(json, JPushResponse.class);    //解析json
                ALog.i("result 的值为" + json);
            } catch (JsonSyntaxException e) {

            }
            if (null != result) {
                String type = result.getType();
                //跳转 邀请详情
                if ("invite".equals(type) || "joinInvite".equals(type)) {
                    try {
                        Intent i;
                        if (NSMTypeUtils.isMyUserId(result.getUserId())) {
                            i = new Intent(context, InviteInviterDetailActivity.class);
                        } else {
                            i = new Intent(context, InviteJoinerDetailActivity.class);
                        }
                        i.putExtra("data", result.getInviteId());
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    } catch (Exception e) {
                        toHomeAct(context);
                    }
                    // 跳转网页
                } else if ("ad".equals(type)) {
                    try {
//                        Intent i = new Intent(context, MainActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        i.putExtra("isShowAd", result.getUrl());
//                        newNumber = 0;
//                        context.startActivity(i);
                        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getUrl()));
                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(it);
                    } catch (Exception e) {
                        toHomeAct(context);
                    }
                    //跳转用户详情界面
                } else if ("user".equals(type)) {
                    try {
                        Intent it = new Intent(context, UserDetailActivity.class);
                        it.putExtra("userId", Integer.parseInt(result.getUserId()));
                        it.putExtra("showButton", false);
                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(it);
                    } catch (Exception e) {
                        toHomeAct(context);
                    }
                    //跳转服务
                } else if ("server".equals(type)) {
                    try {
                        Intent it = new Intent(context, PublishOrderActivity.class);
                        it.putExtra("serviceId", Integer.parseInt(result.getServerId()));
                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(it);
                    } catch (Exception e) {
                        toHomeAct(context);
                    }

                } else if ("order".equals(type)) {
                    try {
                        Intent i;
                        if (NSMTypeUtils.isMyUserId(result.getUserId())) {
                            i = new Intent(context, InviteInviterDetailActivity.class);
                        } else {
                            i = new Intent(context, InviteJoinerDetailActivity.class);
                        }
                        i.putExtra("data", result.getInviteId());
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    } catch (Exception e) {
                        toHomeAct(context);
                    }
//                    try {
//                        Intent i = new Intent(context, MyTripActivity.class);
//                        i.putExtra("data", result.getInviteId());
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        context.startActivity(i);
//                    } catch (Exception e) {
//                        toHomeAct(context);
//                    }
                } else {
                    toHomeAct(context);
                }
            } else {
                toHomeAct(context);
            }

//            //打开自定义的Activity
//            Intent i = new Intent(context, MainActivity.class);
//            i.putExtras(bundle);
//            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(i);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction()))

        {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction()))

        {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else

        {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private void toHomeAct(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newNumber = 0;
        context.startActivity(i);
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    public static void addOnMessageReceivedListener(OnMessageReceived onMessageReceived) {
        mOnMessageReceived = onMessageReceived;
    }

    public static void removeOnMessageReceivedListener() {
        mOnMessageReceived = null;
    }

    public interface OnMessageReceived {
        void onMessageReceived(String inviteId);
    }
}
