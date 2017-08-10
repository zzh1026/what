package com.neishenme.what.view;

import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import org.seny.android.utils.ALog;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/11/23.
 */

public class RadiusImageViewTwo extends ImageView {
    private float xRadius = 10;
    private float yRadius = 10;

    /**
     * 触摸时按下的点
     **/
    PointF downP = new PointF();
    /**
     * 触摸时当前的点
     **/
    PointF curP = new PointF();

    public RadiusImageViewTwo(Context context) {
        this(context, null);
    }

    public RadiusImageViewTwo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadiusImageViewTwo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public float getxRadius() {
        return xRadius;
    }

    public void setxRadius(float xRadius) {
        this.xRadius = xRadius;
    }

    public float getyRadius() {
        return yRadius;
    }

    public void setyRadius(float yRadius) {
        this.yRadius = yRadius;
    }

    public void setRadius(float radius) {
        this.xRadius = radius;
        this.yRadius = radius;
    }

    public interface OnSingleTouchListener {
        public void onSingleTouch(View v);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        BitmapShader shader;
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getDrawable();
        shader = new BitmapShader(bitmapDrawable.getBitmap(),
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //设置映射否则图片显示不全
        RectF rect = new RectF(0.0f, 0.0f, getWidth(), getHeight() + yRadius);
        int width = bitmapDrawable.getBitmap().getWidth();
        int height = bitmapDrawable.getBitmap().getHeight() + (int) yRadius;
        RectF src = new RectF(0.0f, 0.0f, width, height);
        Matrix matrix = new Matrix();
        matrix.setRectToRect(src, rect, Matrix.ScaleToFit.CENTER);
        shader.setLocalMatrix(matrix);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        canvas.clipRect(new Rect(0, 0, (int) (rect.right), (int) (rect.bottom - yRadius)));
        canvas.drawRoundRect(rect, xRadius, yRadius, paint);
    }
}
