package com.neishenme.what.eventbusobj;

/**
 * 作者：zhaozh create on 2016/11/23 16:31
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class ActivePayTrideBean {
    public ActivePayTrideBean(String activeName, String userLogo, String activeUserName,
                              double activePrice, int activePayType, String activeTradeNum, int activeTicketNum) {
        this.activeName = activeName;
        this.userLogo = userLogo;
        this.activeUserName = activeUserName;
        this.activePrice = activePrice;
        this.activePayType = activePayType;
        this.activeTradeNum = activeTradeNum;
        this.activeTicketNum = activeTicketNum;
    }

    public String activeName;
    public String userLogo;
    public String activeUserName;
    public double activePrice;
    public int activePayType;
    public String activeTradeNum;
    public int activeTicketNum;
}
