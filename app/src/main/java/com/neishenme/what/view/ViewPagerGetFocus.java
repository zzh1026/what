package com.neishenme.what.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.seny.android.utils.ALog;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/12/14.
 */

public class ViewPagerGetFocus extends ViewPager {
    /**
     * 触摸时按下的点
     **/
    PointF downP = new PointF();
    /**
     * 触摸时当前的点
     **/
    PointF curP = new PointF();
    OnSingleTouchListener onSingleTouchListener;
    OnScrollTouchListener mOnScrollTouchListener;

    public ViewPagerGetFocus(Context context) {
        this(context, null);
    }

    public ViewPagerGetFocus(Context context, AttributeSet attrs) {
        super(context, attrs);
//        initView();
    }

    private void initView() {
        setFocusable(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setWillNotDraw(false);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                return true;
//            case MotionEvent.ACTION_MOVE:
//                return true;
//        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //每次进行onTouch事件都记录当前的按下的坐标
        if (getChildCount() <= 1) {
            return super.onTouchEvent(ev);
        }
        curP.x = ev.getX();
        curP.y = ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录按下时候的坐标
                //切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
                downP.x = ev.getX();
                downP.y = ev.getY();
                //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
                getParent().requestDisallowInterceptTouchEvent(true);
                if (mOnScrollTouchListener != null) {
                    mOnScrollTouchListener.onScrollTouchDown();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //在up时判断是否按下和松手的坐标为一个点
                //如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
                getParent().requestDisallowInterceptTouchEvent(true);
                if (mOnScrollTouchListener != null) {
                    mOnScrollTouchListener.onScrollTouchUp();
                }
                if (downP.x == curP.x && downP.y == curP.y) {
                    onSingleTouch(this);
                    return true;
                }
                break;
        }
        super.onTouchEvent(ev); //注意这句不能 return super.onTouchEvent(arg0); 否则触发parent滑动
        return true;
    }

    public void onSingleTouch(View v) {
        if (onSingleTouchListener != null) {
            onSingleTouchListener.onSingleTouch(v);
        }
    }

    public interface OnSingleTouchListener {
        public void onSingleTouch(View v);
    }

    public interface OnScrollTouchListener {
        public void onScrollTouchDown();

        public void onScrollTouchUp();
    }

    public void setOnSingleTouchListener(
            OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }

    public void setOnScrollTouchListener(OnScrollTouchListener mOnScrollTouchListener) {
        this.mOnScrollTouchListener = mOnScrollTouchListener;
    }
}
