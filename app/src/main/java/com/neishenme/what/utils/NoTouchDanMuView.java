package com.neishenme.what.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.anbetter.danmuku.DanMuView;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/4/5.
 */

public class NoTouchDanMuView extends DanMuView {
    public NoTouchDanMuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }
}
