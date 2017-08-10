package com.neishenme.what.utils;

import android.app.Activity;
import android.app.Service;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.neishenme.what.application.App;
import com.neishenme.what.nsminterface.DoSomethingListener;

/**
 * 作者：zhaozh create on 2016/4/5 16:02
 * .
 * 版权: 内什么（北京）信息技术有限公司
 * .
 * =====================================
 * .
 * 这是一个 的类
 * .
 * 其作用是 :
 */
public class UpSoftInputUtils {

    /**
     * 隐藏键盘
     */
    public static void UpSoftInputUtils(Activity activity) {
        EditText view = new EditText(App.getApplication());
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void closeKeyBoard(Activity activity) {

        try {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            ((InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自动登录使用
     *
     * @param editText
     * @param doSomethingListener
     */
    public static void goDown(EditText editText, final DoSomethingListener doSomethingListener) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    doSomethingListener.onDoSomethingListener();
                }
                return false;
            }
        });
    }

    /**
     * 展示键盘
     *
     */
    public static void upInput() {
        EditText view = new EditText(App.getApplication());
        InputMethodManager imm = (InputMethodManager) App.getApplication().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);

//        ((InputMethodManager) activity.getSystemService(Service.INPUT_METHOD_SERVICE)).
//                hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void openKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
