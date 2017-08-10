package com.neishenme.what.eventbusobj;

/**
 * 作者：zhaozh create on 2016/8/19 22:57
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class ChatInfoBean {
    public ChatInfoBean(String userLogo, String userId, String userName) {
        this.userLogo = userLogo;
        this.userId = userId;
        this.userName = userName;
    }

    public String userLogo;
    public String userId;
    public String userName;
}
