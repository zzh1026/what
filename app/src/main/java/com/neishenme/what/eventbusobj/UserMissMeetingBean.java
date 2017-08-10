package com.neishenme.what.eventbusobj;

/**
 * 这个类 是用户爽约后的处理
 * <p>
 * Created by zhaozh on 2017/1/12.
 */

public class UserMissMeetingBean {
    public int inviteId;
    public int userId;

    public UserMissMeetingBean(int inviteId, int userId) {
        this.inviteId = inviteId;
        this.userId = userId;
    }
}
