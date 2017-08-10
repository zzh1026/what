package com.neishenme.what.nsminterface;

/**
 * 作者：zhaozh create on 2016/4/5 20:44
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public interface ScreenListener {
    boolean startTouch(String tag);

    void canTouch(boolean flag);

    void changeScreen(int screen, String tag);
}
