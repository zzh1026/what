package com.neishenme.what.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.seny.android.utils.ALog;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2017/5/15.
 */

public class OnsizeLinearLayout extends LinearLayout {
    private OnSizeChanged mOnSizeChangedListener;

    public OnsizeLinearLayout(Context context) {
        super(context);
    }

    public OnsizeLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OnsizeLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mOnSizeChangedListener != null) {
            mOnSizeChangedListener.onSizeChanged(h);
        }
    }

    public void addOnSizeChangedListener(OnSizeChanged mOnSizeChangedListener) {
        this.mOnSizeChangedListener = mOnSizeChangedListener;
    }

    public static interface OnSizeChanged{
        void onSizeChanged(int sizeHeight);
    }
}
