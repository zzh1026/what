package com.neishenme.what.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import org.seny.android.utils.ALog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：zhaozh create on 2016/5/13 15:55
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {

    private static MessageListener mMessageListener;
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    private static SMSBroadcastReceiver mSMSBroadcastReceiver;
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";

    public SMSBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objs) {
                byte[] pdu = (byte[]) obj;
                SmsMessage smsMessage = SmsMessage.createFromPdu(pdu);
                //发送人
                String sender = smsMessage.getDisplayOriginatingAddress();
                ALog.i("短信到来了,发送人为" + sender);
                //短信内容
                String content = smsMessage.getMessageBody();
                ALog.i("短信到来了,内容为" + content);
                long date = smsMessage.getTimestampMillis();
//                Date tiemDate = new Date(date);
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String time = simpleDateFormat.format(tiemDate);

                if (!TextUtils.isEmpty(sender) && content.contains("内什么")) {
                    String code = patternCode(content);
                    if (!TextUtils.isEmpty(code)) {
                        mMessageListener.onReceived(code);
                    }
                }
            }
        }
    }

    public static SMSBroadcastReceiver registerSms(Activity mActivity) {
        if (mSMSBroadcastReceiver == null) {
            mSMSBroadcastReceiver = new SMSBroadcastReceiver();
        }
        IntentFilter intentFilter = new IntentFilter(ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        mActivity.registerReceiver(mSMSBroadcastReceiver, intentFilter);
        return mSMSBroadcastReceiver;
    }

    public static void unRegisterSms(Activity mActivity) {
        if (mSMSBroadcastReceiver != null) {
            try {
                mActivity.unregisterReceiver(mSMSBroadcastReceiver);
                ALog.i("注销成功");
            } catch (Exception e) {
                ALog.i("注销失败");
            }
        }
    }

    public interface MessageListener {
        void onReceived(String autoCode);
    }

    public void setOnReceivedMessageListener(MessageListener messageListener) {
        this.mMessageListener = messageListener;
    }

    /**
     * 匹配短信中间的6个数字（验证码等）
     *
     * @param patternContent
     * @return
     */
    private String patternCode(String patternContent) {
        if (TextUtils.isEmpty(patternContent)) {
            return null;
        }
        Pattern p = Pattern.compile(patternCoder);
        Matcher matcher = p.matcher(patternContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
