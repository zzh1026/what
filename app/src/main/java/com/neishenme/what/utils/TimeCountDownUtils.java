package com.neishenme.what.utils;

import android.os.CountDownTimer;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/3/21.
 * <p>
 * 这个事倒计时 的工具类, 用来全局获取一个 倒计时, 这样就可以在不同的界面实现同一个倒计时了 使用的是 CountDownTimer
 */

/**
 * 该工具类的普通使用方法是:
 *
 * 1,在 initdata 中使用 {@link TimeCountDownUtils#canStart()} 来判断是否需要显示,来初始化显示.
 * 同时使用{@link TimeCountDownUtils#regest(TimeCallInterface)}来进行注册回调
 * 该方法调用一次即可
 *
 * 2,通过回调方法来刷新ui处理, 当倒计时完毕后 time会置空, 此时 canstart会返回ture,当需要倒计时的时候调用
 * {@link TimeCountDownUtils#start()} 方法来开始倒计时,并且通过 onTick和 onFinish回调来处理显示
 *
 * 3,在onDestory 中调用 {@link TimeCountDownUtils#unRegest()} 来取消注册,这样可以释放内存取消对其他数据的引用减少内存泄漏
 *
 */
public class TimeCountDownUtils {

    private static TimeCount time;
    private static TimeCallInterface timeCallInterface;

    static class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            if (timeCallInterface != null) {
                timeCallInterface.onFinish();
            }
            time = null;
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            if (timeCallInterface != null) {
                timeCallInterface.onTick(millisUntilFinished);
            }
        }
    }

    public static interface TimeCallInterface {
        void onFinish();

        void onTick(long millisUntilFinished);
    }

    public static void regest(TimeCallInterface timeCallInterface) {
        if (TimeCountDownUtils.timeCallInterface != null) {
            TimeCountDownUtils.timeCallInterface = null;
        }
        TimeCountDownUtils.timeCallInterface = timeCallInterface;
    }

    public static boolean canStart() {
        if (time == null) {
            return true;
        }
        return false;
    }

    public static void start() {
        if (time == null) {
            time = new TimeCount(60000, 1000);
            time.start();
        }
    }

    public static void unRegest() {
        timeCallInterface = null;
    }
}
