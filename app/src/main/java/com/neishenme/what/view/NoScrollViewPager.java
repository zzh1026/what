package com.neishenme.what.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/1/17.
 */

public class NoScrollViewPager extends ViewPager {
    private boolean isPagingEnabled = true;
    private int x1;
    private int y1;

    private int x2;
    private int y2;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return this.isPagingEnabled && super.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
//        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
        if (getCurrentItem() == getChildCount() - 1) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x1 = (int) event.getX();
                    y1 = (int) event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    x2 = (int) event.getX();
                    y2 = (int) event.getY();
                    if (x2 > x1 && Math.abs(x1 - x2) > Math.abs(y2 - y1)) {
                        return true;
                    } else {
                        return false;
                    }
            }
        }
        return super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}
