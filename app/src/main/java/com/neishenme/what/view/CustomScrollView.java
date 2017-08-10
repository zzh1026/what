package com.neishenme.what.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/5/5.
 * 获取到内容滑动距离的ScrollView
 */
public class CustomScrollView extends ScrollView {

    private ScrollYChangedListener scrollYChangedListener;



    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        }
    }

    public void setScrollYChangedListener(ScrollYChangedListener scrollYChangedListener) {
        this.scrollYChangedListener = scrollYChangedListener;
    }
}
