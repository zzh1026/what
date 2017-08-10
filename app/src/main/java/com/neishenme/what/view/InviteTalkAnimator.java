package com.neishenme.what.view;

import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.neishenme.what.R;
import com.nineoldandroids.animation.ObjectAnimator;

import org.seny.android.utils.ALog;

/**
 * 这个类的作用是:  邀请详情的可以聊天的 动画
 * <p>
 * Created by zhaozh on 2017/1/9.
 */

public class InviteTalkAnimator extends BaseViewAnimator {

    @Override
    protected void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "rotation", 0, 30, -30, 21, -21, 8, -8, 3, -3, 0)
        );
    }
}
