package com.neishenme.what.view;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.neishenme.what.R;
import com.neishenme.what.application.App;

/**
 * view上面的小点点
 */
public class IndicatorLayout extends LinearLayout implements OnPageChangeListener {
    Context context;
    int count;

    public IndicatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setViewPage(ViewPager viewPager) {
        count = viewPager.getAdapter().getCount();

        if (count != 0) {
            for (int i = 0; i < count; i++) {
//			View view = LayoutInflater.from(context).inflate(R.layout.indicate_image, null);
                View view = View.inflate(App.getApplication(), R.layout.indicate_image, null);
                this.addView(view);
            }
            this.getChildAt(0).setSelected(true);
            viewPager.addOnPageChangeListener(this);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int arg0) {
        for (int i = 0; i < count; i++) {
            if (i == arg0) {
                this.getChildAt(i).setSelected(true);
            } else {
                this.getChildAt(i).setSelected(false);
            }
        }

    }
}
