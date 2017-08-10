package com.neishenme.what.eventbusobj;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/3/29.
 */

public class ActiveJoinTridBean {
    public String tradeNum;
    public String logo;
    public String title;
    public String name;
    public long time;
    public double price;

    public ActiveJoinTridBean(String tradeNum, String logo, String title, String name, long time, double price) {
        this.tradeNum = tradeNum;
        this.logo = logo;
        this.title = title;
        this.name = name;
        this.time = time;
        this.price = price;
    }
}
