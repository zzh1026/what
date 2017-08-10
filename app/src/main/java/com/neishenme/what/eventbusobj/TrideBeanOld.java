package com.neishenme.what.eventbusobj;

/**
 * 作者：zhaozh create on 2016/5/19 14:31
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是
 *  旧的活动model , 已弃用,新的活动model见 @see {@link TrideBean}
 */
public class TrideBeanOld {
    public TrideBeanOld(String restrantantName, String restaurantLogo, String title,
                        long time, double price, int payType, int serviceId, String tradeNum) {
        this.restrantantName = restrantantName;
        this.restaurantLogo = restaurantLogo;
        this.title = title;
        this.time = time;
        this.price = price;
        this.payType = payType;
        this.serviceId = serviceId;
        this.tradeNum = tradeNum;
    }

    public String restrantantName;
    public String restaurantLogo;
    public String title;
    public long time;
    public double price;
    public int payType;
    public int serviceId;
    public String tradeNum;
}
