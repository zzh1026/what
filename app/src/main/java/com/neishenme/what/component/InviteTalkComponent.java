package com.neishenme.what.component;

import android.view.LayoutInflater;
import android.view.View;

import com.neishenme.what.R;
import com.neishenme.what.view.highlight.view.Component;

/**
 * 2017_1_6
 * 邀请详情对话元素的功能引导
 */
public class InviteTalkComponent implements Component {

    @Override
    public View getView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.show_function_invite_talk, null);
        return view;
    }

    @Override
    public int getAnchor() {
        return Component.ANCHOR_LEFT;
    }

    @Override
    public int getFitPosition() {
        return Component.FIT_END;
    }

    @Override
    public int getXOffset() {
        return -10;
    }

    @Override
    public int getYOffset() {
        return 45;
    }
}
