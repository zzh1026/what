package com.neishenme.what.bean;

/**
 * 作者：zhaozh create on 2016/3/8 17:56
 * 版权: 内什么
 * =====================================
 * 这是一个网络请求结果的类
 * 其作用是 :
 *
 *      相当于网络请求结果的基类,用于封装服务器返回的json信息。
 */
public class RBResponse {

    /**
     * response的类型.
     */
    protected String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
