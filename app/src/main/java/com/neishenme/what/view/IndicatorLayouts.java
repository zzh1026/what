package com.neishenme.what.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.neishenme.what.R;
import com.neishenme.what.application.App;

/**
 * Created by gengxin on 2016/5/3.
 */
public class IndicatorLayouts extends LinearLayout implements ViewPager.OnPageChangeListener {
    private Context context;
    private int mCount;

    public IndicatorLayouts(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    public void setViewPage(ViewPager viewPager, int count) {
        if (count != 0) {
            mCount = count;
            for (int i = 0; i < count; i++) {
                View view = LayoutInflater.from(App.getApplication()).inflate(R.layout.indicator_button, null);
                this.addView(view);
            }
            this.getChildAt(0).setSelected(true);
            viewPager.setOnPageChangeListener(this);
        }


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        int selectedPosition = position % mCount;
        for (int i = 0; i < mCount; i++) {
            if (i == selectedPosition) {
                this.getChildAt(i).setSelected(true);
            } else {
                this.getChildAt(i).setSelected(false);
            }
        }


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
