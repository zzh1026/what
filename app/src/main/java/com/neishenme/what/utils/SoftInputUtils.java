package com.neishenme.what.utils;

import android.content.Context;
import android.view.KeyEvent;
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
 * 这是一个软键盘操作的类
 * .
 * 其作用是 :
 */
public class SoftInputUtils {
    public static void UpSoftInputUtils() {
        EditText view = new EditText(App.getApplication());
        InputMethodManager imm = (InputMethodManager) App.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

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

}
