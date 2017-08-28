package com.android.demo.view.custom;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by hewei on 2017/8/28.
 */

public class CircleDrawable extends Drawable {
    private static final String TAG = CircleDrawable.class.getSimpleName();

    private Paint mPaint;
    private float mRadius;
    private RectF mRectF;
    private int mOffset;

    public CircleDrawable() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        RectF tempRectF = new RectF(mRectF.left + mOffset, mRectF.top, mRectF.right - mOffset, mRectF.bottom);
        canvas.drawRoundRect(tempRectF, mRadius, mRadius, mPaint);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom)
    {
        super.setBounds(left, top, right, bottom);
        mRectF = new RectF(left, top, right, bottom);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setOffset(int offset) {
        this.mOffset = offset;
        invalidateSelf();
    }

    public void setRadius(float radius) {
        this.mRadius = radius;
        invalidateSelf();
    }
}
