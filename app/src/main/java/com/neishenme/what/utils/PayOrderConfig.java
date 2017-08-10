package com.neishenme.what.utils;

/**
 * 这个类是支付界面的大多数配置界面, 因为支付界面东西太多,太乱,必须把代码放在多个地方来减少代码量
 * <p>
 * Created by zhaozh on 2016/12/16.
 */

public class PayOrderConfig {
    public static final int MAKE_SURE_ALI_PAY = 0;  //确认支付成功的时候阿里支付
    public static final int MAKE_SURE_TEN_PAY = 1;  //确认支付成功的时候微信支付


    public static final String BTN_STATE_CAN_PAY = "立即支付";
    public static final String BTN_STATE_IS_PAYING = "正在支付";
    public static final String BTN_STATE_INPUT_PASSWORD = "密码输入";


    public static final Boolean BTN_CAN_CLICK = true;
    public static final Boolean BTN_NO_CLICK = false;
}
