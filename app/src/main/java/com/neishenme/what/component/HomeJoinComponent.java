package com.neishenme.what.component;

import android.view.LayoutInflater;
import android.view.View;

import com.neishenme.what.R;
import com.neishenme.what.view.highlight.view.Component;

/**
 * 2017_1_6
 * 主界面加入的功能引导元素
 */
public class HomeJoinComponent implements Component {

    @Override
    public View getView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.show_function_home_join, null);
        return view;
    }

    @Override
    public int getAnchor() {
        return Component.ANCHOR_LEFT;
    }

    @Override
    public int getFitPosition() {
        return Component.FIT_CENTER;
    }

    @Override
    public int getXOffset() {
        return -20;
    }

    @Override
    public int getYOffset() {
        return 43;
    }
}
