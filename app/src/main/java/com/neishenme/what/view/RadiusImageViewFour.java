package com.neishenme.what.view;

import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 这个类的作用是:
 * <p>
 * Created by zhaozh on 2016/11/23.
 */

public class RadiusImageViewFour extends ImageView {
    private float xRadius = 10;
    private float yRadius = 10;

    public RadiusImageViewFour(Context context) {
        this(context, null);
    }

    public RadiusImageViewFour(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadiusImageViewFour(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);
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

    @Override
    protected void onDraw(Canvas canvas) {
        BitmapShader shader;
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getDrawable();
        shader = new BitmapShader(bitmapDrawable.getBitmap(),
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //设置映射否则图片显示不全
        RectF rect = new RectF(0.0f, 0.0f, getWidth(), getHeight());
        int width = bitmapDrawable.getBitmap().getWidth();
        int height = bitmapDrawable.getBitmap().getHeight();
        RectF src = new RectF(0.0f, 0.0f, width, height);
        Matrix matrix = new Matrix();
        matrix.setRectToRect(src, rect, Matrix.ScaleToFit.CENTER);
        shader.setLocalMatrix(matrix);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        canvas.drawRoundRect(rect, xRadius, yRadius, paint);
    }
}
