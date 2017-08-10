package com.neishenme.what.component;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.neishenme.what.R;
import com.neishenme.what.view.highlight.view.Component;

/**
 * Created by binIoter on 16/6/17.
 */
public class SimpleComponent implements Component {

    @Override
    public View getView(LayoutInflater inflater) {
        LinearLayout ll = new LinearLayout(inflater.getContext());
        LinearLayout.LayoutParams param =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setLayoutParams(param);
        ImageView imageView = new ImageView(inflater.getContext());
        imageView.setImageResource(R.drawable.show_function_order);
        ll.removeAllViews();
        ll.addView(imageView);
        return ll;
    }

    @Override
    public int getAnchor() {
        return Component.ANCHOR_RIGHT;
    }

    @Override
    public int getFitPosition() {
        return Component.FIT_END;
    }

    @Override
    public int getXOffset() {
        return -23;
    }

    @Override
    public int getYOffset() {
        return -18;
    }
}
