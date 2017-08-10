package com.neishenme.what.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.neishenme.what.R;

/**
 * Created by Administrator on 2016/5/5.
 */
public class BaseDialog extends Dialog {

    //界面默认显示在底部
    protected static final int DEFAULT_GRAVITY = Gravity.BOTTOM;

    //默认动画从底部向上
    protected static final AnimationDirection DEFAULT_ANIMATION_DIRECTION = AnimationDirection.VERTICLE;

    public enum AnimationDirection {
        HORIZONTAL, VERTICLE ,GROW
    }

    protected BaseDialog(Context context, int gravity, float marginVerticle, AnimationDirection animationDirection, boolean backCancelable, boolean outsideCancelable) {
        super(context, R.style.BaseDialog);
        init(gravity, marginVerticle, animationDirection, backCancelable, outsideCancelable);
    }

    protected void init(int gravity, float marginVerticle, AnimationDirection animationDirection, boolean backCancelable, boolean outsideCancelable) {
        this.setCancelable(backCancelable);
        this.setCanceledOnTouchOutside(outsideCancelable);
        Window dialogWindow = this.getWindow();
        if (animationDirection == AnimationDirection.VERTICLE) {
            dialogWindow.setWindowAnimations(R.style.DialogVerticleWindowAnim);
        } else if (animationDirection == AnimationDirection.HORIZONTAL) {
            dialogWindow.setWindowAnimations(R.style.DialogRightHorizontalWindowAnim);
        } else if (animationDirection == AnimationDirection.GROW) {
            dialogWindow.setWindowAnimations(R.style.DialogGrowWindowAnim);
        }
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = gravity;
        layoutParams.verticalMargin = marginVerticle;
        dialogWindow.setAttributes(layoutParams);
    }


    public static class Builder {
        protected Context context;
        protected int gravity = DEFAULT_GRAVITY;
        protected AnimationDirection animationDirection = DEFAULT_ANIMATION_DIRECTION;
        protected float marginVerticle;
        protected boolean backCancelable;
        protected boolean outsideCancelable;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;

        }

        public Builder setAnimationDirection(AnimationDirection animationDirection) {
            this.animationDirection = animationDirection;
            return this;
        }

        public Builder setMarginVerticle(float marginVerticle) {
            this.marginVerticle = marginVerticle;
            return this;
        }

        public Builder setBackCancelable(boolean backCancelable) {
            this.backCancelable = backCancelable;
            return this;
        }

        public Builder setOutsideCancelable(boolean outsideCancelable) {
            this.outsideCancelable = outsideCancelable;
            return this;
        }

        public BaseDialog build() {
            return new BaseDialog(context, gravity, marginVerticle,
                    animationDirection, backCancelable, outsideCancelable);
        }

    }
}
