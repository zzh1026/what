package com.neishenme.what.view.refreshview.loadingview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;
import com.neishenme.what.R;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * 这个类的作用是:  自定义的刷新 headerview
 * <p>
 * Created by zhaozh on 2017/4/13.
 */

public class CustomRefreshHeaderView extends FrameLayout implements IHeaderView {
    private ImageView loadingView;

    public CustomRefreshHeaderView(Context context) {
        this(context, null);
    }

    public CustomRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = View.inflate(getContext(), R.layout.view_custom_refresh_header, null);
        loadingView = (ImageView) rootView.findViewById(R.id.iv_loading);
        loadingView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.rocket, null));
        addView(rootView);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {

    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {

    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        AnimationDrawable drawable = (AnimationDrawable) loadingView.getDrawable();
        if (null != drawable) {
            drawable.start();
        }
    }

    @Override
    public void onFinish(OnAnimEndListener animEndListener) {
        animEndListener.onAnimEnd();
    }

    @Override
    public void reset() {
        AnimationDrawable drawable = (AnimationDrawable) loadingView.getDrawable();
        if (null != drawable && drawable.isRunning()) {
            drawable.stop();
        }
    }
}
