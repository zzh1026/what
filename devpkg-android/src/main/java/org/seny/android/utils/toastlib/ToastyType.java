package org.seny.android.utils.toastlib;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/3/21.
 */

/**
 * 展示toast的样式
 */
public enum ToastyType {
    TYPE_NORMAL,    //普通的
    TYPE_INFO,      //信息
    TYPE_WARNING,   //警告
    TYPE_SUCCESS,   //成功
    TYPE_ERROR;     //错误,失败

    private ToastyType() {
    }
}
