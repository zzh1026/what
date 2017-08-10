package com.neishenme.what.bean;

/**
 * 作者：zhaozh on 2016/3/8 17:51 create
 *
 * 版权: 内什么
 *
 * =====================================
 *
 *
 * 这是一个网络请求失败调用的类
 *
 * 其作用是 :
 *   网络请求失败的时候进行的处理
 */
public class ErrorResponse extends RBResponse {
    /**
     * text : 用户名不存在
     */

    private ErrorEntity error;

    public void setError(ErrorEntity error) {
        this.error = error;
    }

    public ErrorEntity getError() {
        return error;
    }

    public static class ErrorEntity {
        private String text;

        public void setText(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
