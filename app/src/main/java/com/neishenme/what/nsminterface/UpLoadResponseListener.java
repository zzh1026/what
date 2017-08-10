package com.neishenme.what.nsminterface;

/**
 * 作者：zhaozh create on 2016/5/14 17:41
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public interface UpLoadResponseListener {
    void onResponseSuccess(String requestString);
    void onResponseError(Exception e);
}
