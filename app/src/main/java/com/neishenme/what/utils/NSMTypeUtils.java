package com.neishenme.what.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.neishenme.what.application.App;
import com.neishenme.what.bean.SocketLoginBean;
import com.neishenme.what.bean.SocketSendBean;
import com.neishenme.what.common.CityLocationConfig;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：zhaozh create on 2016/3/11 13:46
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个关于获取Type类型的类
 * .
 * 其作用是 :
 * 可以通过一个字符串或int数据来返回不同的类型的字符串用于设置进textview中
 */
public class NSMTypeUtils {
    private static final String TALK_CLICK_TIME = "talkclick";  //标记邀请详情界面用户点击谈话的时间

    /**
     * 获取用户支付类型
     * <p/>
     * Home 主界面
     * 1,我买单
     * 2,求请客
     * 3,AA制
     *
     * @param payType 传入一个int类型的值
     * @return 具体string
     */
    public static String getUserPayType(int payType) {
        if (payType == 1) {
            return "我请客 ";
        } else if (payType == 2) {
            return "求请客 ";
        } else {
            return "AA制 ";
        }
    }

    /**
     * 判断是否为空.
     *
     * @param o
     * @return
     */
    public static boolean isEmpty(Object o) {
        if (null == o) {
            return true;
        } else {
            if (o instanceof String) {
                String s = ((String) o).trim();
                if (null != s && !TextUtils.isEmpty(s)) {
                    return false;
                } else {
                    return true;
                }
            } else {
                //这里可添加
                return true;
            }
        }
    }

    /**
     * 判断是否登录的方法,判断方法为判断token和性别
     * <p/>
     * 返回是否处于登录状态, true : 登录, false :　未登录；
     *
     * @return
     */
    public static boolean isLogin() {
        if (isEmpty(getMyToken())) {
            return false;
        }
        String gender = App.USERSP.getString("gender", "").trim();
        if (isEmpty(gender)) {
            return false;
        }
        if ("1".equals(gender) || "2".equals(gender)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取自己的性别信息, 1男 2女
     *
     * @return
     */
    public static int getMyGender() {
        return Integer.parseInt(App.USERSP.getString("gender", "0").trim());
    }

    /**
     * 判断要判断的用户id是不是自己的用户id
     *
     * @param userId
     * @return
     */
    public static boolean isMyUserId(String userId) {
        return userId.equals(App.USERSP.getString("userId", "").trim());
    }

    /**
     * 获取登录情况下该用户USERSP中保存的userId
     *
     * @return
     */
    public static String getMyUserId() {
        return App.USERSP.getString("userId", "").trim();
    }

    /**
     * 获取登录情况下该用户SP中保存的token
     *
     * @return
     */
    public static String getMyToken() {
        return App.SP.getString("token", "").trim();
    }

    /**
     * 验证手机号
     *
     * @param username
     * @return
     */
    public static boolean patternPhoneSuccess(String username) {
        //String telRegex = "[1][358]\\d{9}";
        String telRegex = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\\\d{8}$";
        return username.matches(telRegex);
    }

    /**
     * 根据一个long类型的时间间隔获取是多少天
     *
     * @param dealTimes
     * @return
     */
    public static int getDealDays(long dealTimes) {
        int days = (int) (dealTimes / 1000 / 60 / 60 / 24);
        return days == 0 ? 1 : days;
    }

    /**
     * 验证是否是手机号
     *
     * @param phoneNumbers 要验证的手机号
     * @return 是否为手机号
     * @see NSMTypeUtils patternPhoneSuccess
     */
    public static boolean patternPhoneNumbers(String phoneNumbers) {
        String pattern = "(13\\d|14[57]|15[^4,\\D]|17[678]|18\\d)\\d{8}|170[059]\\d{7}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(phoneNumbers);
        return m.matches();
    }

    /**
     * 获取用户邀请对象类型
     * <p/>
     * Home主界面
     * <p/>
     * 1,男,
     * 0,不限
     * 2,女
     *
     * @param target
     * @return
     */
    public static String getUserTarget(int target) {
        String s = "/ Girl";
        switch (target) {
            case 1:
                s = "/ Boy";
                break;
            case 0:
                s = "/ 不限";
                break;
        }
        return s;
    }

    //主界面的获取用户的邀请对象, 因为主界面修改了,但是其他界面尚不需要修改
    public static String getUserTargetWithHome(int target) {
        String s = "女";
        switch (target) {
            case 1:
                s = "男";
                break;
            case 0:
                s = "不限";
                break;
        }
        return s;
    }

    /**
     * 获取 , 将某几个字修改成某种颜色
     *
     * @param changeColor
     * @param chchangeText
     * @return
     */
    public static SpannableStringBuilder getDifColorText(int changeColor, String chchangeText) {
        return getDifColorText(changeColor, chchangeText, null, null);
    }

    /**
     * 获取, 将某几个字转换成其他颜色的string
     *
     * @param changeColor  要修改字体的颜色
     * @param chchangeText 要修改的字符
     * @param beforeText   该字符前面的字符
     * @param afterText    该字符后面的字符
     * @return
     */
    public static SpannableStringBuilder getDifColorText(
            int changeColor, String chchangeText, String beforeText, String afterText) {
        SpannableStringBuilder builder = new SpannableStringBuilder(chchangeText);
        ForegroundColorSpan whiteSpan = new ForegroundColorSpan(changeColor);
        builder.setSpan(whiteSpan, 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (!TextUtils.isEmpty(beforeText)) {
            builder.insert(0, beforeText);
        }
        if (!TextUtils.isEmpty(afterText)) {
            builder.append(afterText);
        }
        return builder;
    }

    /**
     * 将double或者string类型的price转换成合适的price  如果小数点后面是00 会转成整数类型
     *
     * @param price
     * @return
     */
    public static String getGreatPrice(double price) {
        return getGreatPrice(String.valueOf(price));
    }

    public static String getGreatPrice(String price) {
        String[] split = price.split("\\.");
        if (split.length == 1) {    //说明是整数
            return price;
        }
        String newPrice;
        if (split[1].length() > 2) {    //说明很长
            newPrice = split[1].substring(0, 2);
        } else {
            newPrice = split[1];
        }
        if (Integer.parseInt(newPrice) == 0) {
            return split[0];
        } else {
            return split[0] + "." + newPrice;
        }
    }

    public static String getPublicType(int publicType) {
        String s = "专属发布 ";
        switch (publicType) {
            case 1:
                s = "极速发布 ";
                break;
            default:
                break;
        }
        return s;
    }

    /**
     * 获取我的账单的类型 ,两个地方用到
     * {@link com.neishenme.what.adapter.WalletRecordsListAdapter }我的钱包账单
     * {@link com.neishenme.what.adapter.RecordListAdapter} //我的账单界面
     *
     * @param type
     * @return
     */
    public static String getRocordOrigin(int type) {
        switch (type) {
            case 110:
                return "加入活动";
            case 120:
                return "发布活动";
            case 130:
                return "提现";
            case 140:
                return "会员购买";
            case 150:
                return "活动补偿";
            case 210:
                return "活动失效";
            case 220:
                return "付款失效";
            case 230:
                return "系统赠送";
            case 240:
                return "完成退款";
            case 250:
                return "活动爽约";
            case 260:
                return "迟到扣款";
            default:
                return "";
        }
    }

    public static String getOrderTarget(int target) {
        if (1 == target) {
            return "男";
        } else if (2 == target) {
            return "女";
        } else {
            return "不限";
        }
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 邀请详情界面判断是否需要展示对话的动画
     * 具体判断的方式为 ,获取  {{@link #talkedOnclick()}}   存入的时间,默认为0
     * 如果结果为0 说明没有存过, 则应该显示,
     * 如果不为0, 判断该时间与当前时间的大小,如果超过点击时间则说明活动已经完毕,
     * 肯定不显示了,说明该时间不是当前活动的,所以清空当前值并返回ture
     * 如果比当前时间小并且在 20分钟以内,(正常就应该是这样的),则说明是这个单,但是已经显示过了,则不显示
     *
     * @return
     */
    public static boolean shouldShowTalkAnim() {
        long clickTime = App.SP.getLong(TALK_CLICK_TIME, 0);
        if (0 == clickTime) {   //如果是0 ,没存过,第一次,返回true
            return true;
        }
        long l = (System.currentTimeMillis() - clickTime) / 1000 / 60;
        if (l <= 20) {   //已经点击过了
            return false;
        } else {    //已经是上一个活动的了, 和这个活动没关系所以返回true并且关闭该单的记录时间
            App.EDIT.remove(TALK_CLICK_TIME).commit();
            return true;
        }
    }

    /**
     * 邀请详情页来标记用户点击了 谈话按钮
     * 逻辑:  将点击的时间进行保存;
     */
    public static void talkedOnclick() {
        App.EDIT.putLong(TALK_CLICK_TIME, System.currentTimeMillis()).commit();
    }

    public static SocketSendBean getSocketSendBean(RequestType type, double latitude, double longitude, int inviteId) {
        SocketSendBean socketSendBean = new SocketSendBean();
        socketSendBean.setUserToken(getMyToken());
        if (type == RequestType.LOCATION) {
            socketSendBean.setServer("location");
        } else {
            socketSendBean.setServer("target");
        }
        SocketSendBean.ParametersEntity entity = new SocketSendBean.ParametersEntity();
        entity.setInviteId(inviteId);
        entity.setLatitude(latitude);
        entity.setLongitude(longitude);
        socketSendBean.setParameters(entity);
        return socketSendBean;
    }

    public static SocketLoginBean getSocketLoginBean(double latitude, double longitude) {
        SocketLoginBean socketSendBean = new SocketLoginBean();
        socketSendBean.setUserToken(getMyToken());
        socketSendBean.setServer("location");
        SocketLoginBean.ParametersEntity entity = new SocketLoginBean.ParametersEntity();
        entity.setLatitude(latitude);
        entity.setLongitude(longitude);
        entity.setAreaId(CityLocationConfig.cityLocationId);
        socketSendBean.setParameters(entity);
        return socketSendBean;
    }

    /**
     * takemeout如果不满三位数需要在前面补0;
     *
     * @param takeMeOutId
     * @return
     */
    public static String getTakeMeOutId(String takeMeOutId) {
        if (takeMeOutId.length() == 1) {
            return "00" + takeMeOutId;
        } else if (takeMeOutId.length() == 2) {
            return "0" + takeMeOutId;
        } else {
            return takeMeOutId;
        }
    }

    public enum RequestType {
        LOCATION,
        TARGET;
    }
}
