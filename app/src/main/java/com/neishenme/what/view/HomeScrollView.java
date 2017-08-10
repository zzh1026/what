package com.neishenme.what.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/5/13.
 */
public class HomeScrollView extends ScrollView {
    private ScrollYChangedListener scrollYChangedListener;


    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;


    public HomeScrollView(Context context) {
        super(context);
    }

    public HomeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                xDistance = yDistance = 0f;
//                xLast = ev.getX();
//                yLast = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                final float curX = ev.getX();
//                final float curY = ev.getY();
//
//                xDistance += Math.abs(curX - xLast);
//                yDistance += Math.abs(curY - yLast);
//                xLast = curX;
//                yLast = curY;
//
//                if (xDistance > yDistance) {
//                    return false;
//                }
//        }
        // super.onInterceptTouchEvent(ev);

        return false;
    }

    public interface ScrollYChangedListener {
        void scrollYChange(int y);
    }

    @Override
    protected int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollYChangedListener != null) {
            scrollYChangedListener.scrollYChange(t);
            //System.out.println("=======t======" + t);
        }


    }

    public void setScrollYChangedListener(ScrollYChangedListener scrollYChangedListener) {
        System.out.println("=======y======");
        this.scrollYChangedListener = scrollYChangedListener;
    }
}
