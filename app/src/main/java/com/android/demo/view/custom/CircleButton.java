package com.android.demo.view.custom;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by hewei on 2017/8/28.
 */

public class CircleButton extends android.support.v7.widget.AppCompatButton {
    private static final String TAG = CircleButton.class.getSimpleName();

    private Paint mPaint;
    private CircleDrawable mCircleDrawable;
    private AnimatorSet mAnimatorSet;
    private ValueAnimator cornerAnimator;
    private ValueAnimator scaledAnimator;

    public CircleButton(Context context) {
        super(context);

        init();
    }

    public CircleButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public CircleButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        Log.d(TAG, "init()");

        mPaint = new Paint();
        mCircleDrawable = new CircleDrawable();
        mAnimatorSet = new AnimatorSet();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mCircleDrawable.setBounds(0, 0, getWidth(), getHeight());
        Log.d(TAG, "onLayout() >> getWidth:" + getWidth() + ", getHeight():" + getHeight());

        setCornerAnimation();
        setScaledAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d(TAG, "onDraw()");

        mCircleDrawable.draw(canvas);
        setBackground(mCircleDrawable);
    }

    public void startAnimationInOrder() {
        mAnimatorSet
                .play(cornerAnimator)
                .after(scaledAnimator);

        mAnimatorSet.start();
    }

    private void setCornerAnimation() {
        Log.d(TAG, "setCornerAnimation()");

        cornerAnimator = ObjectAnimator.ofFloat(0, getHeight()/2);
        cornerAnimator.setDuration(1000);
        cornerAnimator.setRepeatCount(0);
        cornerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float radius = (float) animation.getAnimatedValue();
                Log.d(TAG, "onAnimationUpdate() >> radius:" + radius);

                updateRadius(radius);
            }
        });
    }

    private void setScaledAnimation() {
        Log.d(TAG, "setScaledAnimation()");

        scaledAnimator = ObjectAnimator.ofInt(0, (getWidth() - getHeight())/2);
        scaledAnimator.setDuration(1000);
        scaledAnimator.setRepeatCount(0);
        scaledAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int offset = (int) animation.getAnimatedValue();
                Log.d(TAG, "onAnimationUpdate() >> offset:" + offset);

                updateOffset(offset);
            }
        });
    }

    private void updateOffset(int offset) {
        mCircleDrawable.setOffset(offset);
    }

    private void updateRadius(float radius) {
        mCircleDrawable.setRadius(radius);
    }
}
