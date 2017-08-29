package com.android.demo.view.custom;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;

import com.android.demo.R;

/**
 * Created by hewei on 2017/8/28.
 */

public class CircleButton extends android.support.v7.widget.AppCompatButton {
    private static final String TAG = CircleButton.class.getSimpleName();

    private CircleDrawable mCircleDrawable;
    private ValueAnimator cornerAnimator;
    private ValueAnimator scaledAnimator;
    private int initialWidth, initialHeight;

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

        mCircleDrawable = new CircleDrawable(ContextCompat.getColor(getContext(), R.color.colorAccent));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        initialWidth = getWidth();
        initialHeight = getHeight();

        //首先初始化drawable初始形状
        mCircleDrawable.setBounds(0, 0, initialWidth, initialHeight);
        Log.d(TAG, "onLayout() >> getWidth:" + getWidth() + ", getHeight():" + getHeight());

        //初始化动画，要在drawable初始化之后再初始化动画，否则位置不准确
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
        //首先初始化drawable初始形状
        updateOffset(0);
        updateRadius(0);

        AnimatorSet AnimatorSet = new AnimatorSet();
        AnimatorSet
                .play(scaledAnimator)
                .after(cornerAnimator);

        AnimatorSet.start();
    }

    /**
     * 初始化圆角动画
     */
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

    /**
     * 初始化长度动画
     */
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

    /**
     * 刷新drawable的长度
     * @param offset
     */
    private void updateOffset(int offset) {
        mCircleDrawable.setOffset(offset);
    }

    /**
     * 刷新drawable的圆角
     * @param radius
     */
    private void updateRadius(float radius) {
        mCircleDrawable.setRadius(radius);
    }
}
