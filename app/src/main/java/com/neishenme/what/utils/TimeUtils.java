package com.neishenme.what.utils;

import android.content.Context;

import com.neishenme.what.dialog.MyDatePickerDialog;
import com.neishenme.what.nsminterface.OnBirthdayTimeSelect;

import org.seny.android.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 作者：zhaozh create on 2016/3/21 16:29
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT_TIME = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT_OVER_TIME = new SimpleDateFormat("HH个小时mm分钟");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATE_FORMAT_NSM = new SimpleDateFormat("yyyy年MM月dd日");
    public static final SimpleDateFormat DATE_FORMAT_PICK_MONEY = new SimpleDateFormat("yyyy.MM.dd");
    public static final SimpleDateFormat DATE_FORMAT_HOME_NEWS = new SimpleDateFormat("MM月dd日");

    //我的优惠券 优惠券的游戏其的timlete
    public static final SimpleDateFormat DATE_FORMAT_MYCOUPONS = new SimpleDateFormat("yy-MM-dd");

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * long 转字符串
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT_TIME);
    }


//    public static String overTime(long timeInMillis) {
//        StringBuilder sb = new StringBuilder();
//        long time = timeInMillis - System.currentTimeMillis();
//        if (time >= 0) {
//            long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时
//            if (hour == 1) {
//                hour = 0;
//            } else {
//                hour--;
//            }
//            long minute = (long) Math.ceil((time - hour * 60 * 60 * 1000) / 1000 / 60);// 分钟
//            if (hour != 0) {
//                sb.append(hour + "小时");
//            }
//            if (minute == 60) {
//                sb.append("59分钟");
//            } else {
//                sb.append(minute + "分钟");
//            }
//            return sb.toString();
//        } else {
//            long overTime = System.currentTimeMillis() - timeInMillis;
//            long hour = (long) Math.ceil(overTime / 60 / 60 / 1000.0f);// 小时
//            if (hour == 1) {
//                hour = 0;
//            } else {
//                hour--;
//            }
//            long minute = (long) Math.ceil((overTime - hour * 60 * 60 * 1000) / 1000 / 60);// 分钟
//            if (hour != 0) {
//                sb.append(hour + "小时");
//            }
//            if (minute == 60) {
//                sb.append("59分钟");
//            } else {
//                sb.append(minute + "分钟");
//            }
//            return sb.toString();
//        }
//    }

    public static String overTime(long timeInMillis) {
        StringBuilder sb = new StringBuilder();
        long time = Math.abs(timeInMillis - System.currentTimeMillis());
        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时
        if (hour == 1) {
            hour = 0;
        } else {
            hour--;
        }
        long minute = (long) Math.ceil((time - hour * 60 * 60 * 1000) / 1000 / 60);// 分钟
        if (hour != 0) {
            sb.append(hour + "小时");
        }
        if (minute == 60) {
            sb.append("59分钟");
        } else {
            sb.append(minute + "分钟");
        }
        return sb.toString();
    }

    /**
     * 是否应该显示谈话框 (邀请详情中 距离活动20分钟内应该 显示谈话框)
     *
     * @param timeInMillis
     * @return
     */
    public static boolean isShouldShowTalk(long timeInMillis) {
        long time = timeInMillis - System.currentTimeMillis();
        if (time > 0) {
            long minute = (long) Math.floor(time / 1000 / 60);
            return minute <= 20;
        } else {
            return false;
        }
    }

    /**
     * 通过一个 long 类型的字段转换成距离现在多少的时间 天,或者小时, 内谁界面来决定用户最后上线的时间
     *
     * @param lastTime 最后一次上线时间
     * @return
     */
    public static String getLastTimeBefore(long lastTime) {
        long timeDistance = (TimeUtils.getCurrentTimeInLong() - lastTime) / 1000;
        int day = (int) (timeDistance / 86400);
        if (day == 0) {
            int hour = (int) Math.floor(timeDistance % 86400 / 3600);
            if (hour == 1 || hour == 0)
                return hour + " hour ago";
            return hour + " hours ago";
        } else {
            if (day == 1)
                return day + " day ago";
            return day + " days ago";
        }
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }


    /**
     * 判断一个时间是否是今天
     *
     * @param timeInMillis
     * @return
     */
    public static boolean isToday(long timeInMillis) {
        String time = getTime(timeInMillis, DATE_FORMAT_DATE);
        String time1 = getTime(System.currentTimeMillis(), DATE_FORMAT_DATE);
        return time.equals(time1);
    }

    /**
     * 判断一个时间是否是明天
     *
     * @param timeInMillis
     * @return
     */
    public static boolean isTomorrow(long timeInMillis) {
        String time = getTime(timeInMillis, DATE_FORMAT_DATE);
        String time1 = getTime(System.currentTimeMillis() + 1000 * 60 * 60 * 24, DATE_FORMAT_DATE);
        return time.equals(time1);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    public static int getAge(long birthday) {
        Date now = new Date();
        long days = (now.getTime() - birthday) / (1000 * 60 * 60 * 24);//得到总天数
        int years = (int) (days / 365);//计算出年数
        if (years > 0) {
            return years;
        } else {
            return 0;
        }
    }

    public static void setBirthdayTime(Context context, long birthday, final OnBirthdayTimeSelect onBirthdayTimeSelect) {
        final int startYear = 1900;
        ArrayList<String> items = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        for (int i = startYear; i <= thisYear; i++) {
            items.add(i + "");
        }
        String birthdayStr = DateUtils.formatDate(birthday);

        String[] split = birthdayStr.split("-");
        MyDatePickerDialog myDatePickerDialog = new MyDatePickerDialog(context, items, "出生日期");
        if (split.length == 3) {
            int year = Integer.parseInt(split[0].replace(" ", ""));
            int month = Integer.parseInt(split[1].replace(" ", ""));
            int day = Integer.parseInt(split[2].replace(" ", ""));
            if (year >= startYear && year < thisYear) {
                myDatePickerDialog = new MyDatePickerDialog(context, items, "生日", year, month, day);
            }

        }

        final MyDatePickerDialog finalMyDatePickerDialog = myDatePickerDialog;
        myDatePickerDialog.setOnConfirmListeners(new MyDatePickerDialog.OnConfirmListeners() {

            @Override
            public void onConfirmClicked(int yearSelectedIndex, int monthSelectedIndex, int daySelectedIndex) {
                long l = DateUtils.formatToLong((startYear + yearSelectedIndex) + "-" + (monthSelectedIndex + 1) + "-" + (daySelectedIndex + 1), "yyyy-MM-dd");
                finalMyDatePickerDialog.dismiss();
                onBirthdayTimeSelect.onTimeSelect(l);
            }
        });
        myDatePickerDialog.show();
    }
}
