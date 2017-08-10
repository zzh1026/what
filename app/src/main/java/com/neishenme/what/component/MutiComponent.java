package com.neishenme.what.component;

import android.view.LayoutInflater;
import android.view.View;

import com.neishenme.what.R;
import com.neishenme.what.view.highlight.view.Component;

/**
 * Created by binIoter on 16/6/17.
 */
public class MutiComponent implements Component {

    @Override
    public View getView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.show_function_who, null);
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
        return -55;
    }

    @Override
    public int getYOffset() {
        return 5;
    }
}
