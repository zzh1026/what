package com.neishenme.what.view;

import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.neishenme.what.R;
import com.neishenme.what.application.App;
import com.neishenme.what.utils.DensityUtil;
import com.nineoldandroids.animation.ObjectAnimator;

import org.seny.android.utils.ALog;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/12/21.
 */

public class InviteJoinDealogAnimator extends BaseViewAnimator {
    private boolean isLeft = true;  //是否是向左边的动画

    public InviteJoinDealogAnimator(boolean isLeft) {
        this.isLeft = isLeft;
    }

    @Override
    protected void prepare(View target) {
        ViewGroup parent = (ViewGroup) target.getParent();
        int yDistance = (int) (parent.getHeight() - target.getTop() - target.getResources().getDimension(R.dimen.margin_100));
        int xDistance;
        if (isLeft)
            xDistance = (parent.getWidth() / 2 - target.getLeft() - target.getWidth() / 2);
        else
            xDistance = -(target.getRight() - parent.getWidth() / 2 - target.getWidth() / 2);
        ALog.i("xdis的值为:" + xDistance);
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 0, 0.8f, 1),
                ObjectAnimator.ofFloat(target, "scaleX", 0.1f, 0.75f, 1),
                ObjectAnimator.ofFloat(target, "scaleY", 0.1f, 0.75f, 1),
                ObjectAnimator.ofFloat(target, "rotation", -200, -50, 0),
                Glider.glide(Skill.BackEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "translationY", yDistance, 0)),
                Glider.glide(Skill.BackEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "translationX", xDistance, 0))
        );
    }
}
