package com.neishenme.what.view.refreshview.loadingview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lcodecore.tkrefreshlayout.IBottomView;
import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;
import com.neishenme.what.R;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * 这个类的作用是:  自定义的刷新 的尾部
 * <p>
 * Created by zhaozh on 2017/4/13.
 */

public class CustomRefreshFooterView extends ImageView implements IBottomView {

    public CustomRefreshFooterView(Context context) {
        this(context, null);
    }

    public CustomRefreshFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRefreshFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        FrameLayout.LayoutParams params = new
                FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        setLayoutParams(params);
        setImageDrawable(getContext().getResources().getDrawable(R.drawable.rocket, null));
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingUp(float fraction, float maxBottomHeight, float bottomHeight) {

    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        AnimationDrawable drawable = (AnimationDrawable) getDrawable();
        if (null != drawable) {
            drawable.start();
        }
    }

    @Override
    public void reset() {
        AnimationDrawable drawable = (AnimationDrawable) getDrawable();
        if (null != drawable && drawable.isRunning()) {
            drawable.stop();
        }
    }
}
