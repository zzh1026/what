package com.neishenme.what.eventbusobj;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/1/14.
 */

public class UserMeetingSuccess {
    private int inviteId;

    public UserMeetingSuccess(int inviteId) {
        this.inviteId = inviteId;
    }

    public int getInviteId() {
        return inviteId;
    }

    public void setInviteId(int inviteId) {
        this.inviteId = inviteId;
    }
}
