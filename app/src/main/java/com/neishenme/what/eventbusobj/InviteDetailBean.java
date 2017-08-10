package com.neishenme.what.eventbusobj;

/**
 * 作者：zhaozh create on 2016/5/23 15:52
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class InviteDetailBean {

    public InviteDetailBean(String inviteId, String joinerId, String targetId, String publisherId) {
        this.inviteId = inviteId;
        this.joinerId = joinerId;
        this.targetId = targetId;
        this.publisherId = publisherId;
    }

    public String inviteId;
    public String joinerId;
    public String targetId;
    public String publisherId;

}
