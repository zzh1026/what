package com.neishenme.what.component;

import android.view.LayoutInflater;
import android.view.View;

import com.neishenme.what.R;
import com.neishenme.what.view.highlight.view.Component;

/**
 * 2017_1_6
 * 主界面发布元素的功能引导
 */
public class HomeReleaseComponent implements Component {

    @Override
    public View getView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.show_function_home_release, null);
        return view;
    }

    @Override
    public int getAnchor() {
        return Component.ANCHOR_BOTTOM;
    }

    @Override
    public int getFitPosition() {
        return Component.FIT_CENTER;
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return 0;
    }
}
