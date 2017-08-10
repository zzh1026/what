package com.neishenme.what.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/3/2.
 */

public class ScrollViewNoIntercept extends ScrollView {
    public ScrollViewNoIntercept(Context context) {
        super(context);
    }

    public ScrollViewNoIntercept(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewNoIntercept(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);
        return false;
    }
}
