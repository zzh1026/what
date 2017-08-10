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
 * 其作用是 :
 */
public class TrideBean {
    public TrideBean(boolean shouldActivityPurse, int serviceId, int type, String logo, String title,
                     long time, double price, String tradeNum) {
        this.shouldActivityPurse = shouldActivityPurse;
        this.serviceId = serviceId;
        this.type = type;
        this.logo = logo;
        this.title = title;
        this.time = time;
        this.price = price;
        this.tradeNum = tradeNum;
    }

    public boolean shouldActivityPurse;
    public int type;
    public int serviceId;
    public String logo;
    public String title;
    public long time;
    public double price;
    public String tradeNum;
}
