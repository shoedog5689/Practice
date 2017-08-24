package com.android.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by hewei on 2017/8/23.
 */

public class CustomScrollRefreshLayout extends ViewGroup {
    private final String TAG = CustomScrollRefreshLayout.class.getSimpleName();

    private LinearLayout mHeaderLayout;
    private VelocityTracker mVelocityTracker;
    private float actionX, actionY;
    private int touchSlop;

    private int maxHeight;
    private int totalLayoutHeight;

    public CustomScrollRefreshLayout(Context context) {
        super(context);
    }

    public CustomScrollRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomScrollRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 处理WRAP_CONTENT
         */

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        maxHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, getContext().getResources().getDisplayMetrics());

        Log.d(TAG, "childCount:" + getChildCount());

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        /**
         * 记录如果是wrap_content时设置的宽和高
         */
        int width = 0;
        int height = 0;

        int cCount = getChildCount();

        int cWidth = 0;
        int cHeight = 0;

        //如果没有子元素，就设置宽高都为0（简化处理）
        if (getChildCount() == 0) {
            setMeasuredDimension(0, 0);
        }

        //宽和高都是AT_MOST，则设置宽度为所有子元素的宽度的和；高度设置为第一个元素的高度；
        else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            View childOne = getChildAt(0);
            int childWidth = childOne.getMeasuredWidth();
            int childHeight = childOne.getMeasuredHeight();
            setMeasuredDimension(childWidth * getChildCount(), childHeight);
        }
        //如果宽度是wrap_content，则宽度为所有子元素的宽度的和
        else if (widthMode == MeasureSpec.AT_MOST) {
            int childWidth = getChildAt(0).getMeasuredWidth();
            setMeasuredDimension(childWidth * getChildCount(), heightSize);
        }
        //如果高度是wrap_content，则高度为第一个子元素的高度
        else if (heightMode == MeasureSpec.AT_MOST) {
            int childHeight = getChildAt(0).getMeasuredHeight();
            setMeasuredDimension(widthSize, childHeight);
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int totalHeight = 0;
        View child;
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                child.layout(0, totalHeight, width, totalHeight + height);
                totalHeight += height;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent()");

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        mVelocityTracker.computeCurrentVelocity(1000);
        float xVelocity;
        float yVelocity;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent() >> ACTION_DOWN");
                actionX = event.getX();  //初始位置
                actionY = event.getY();
                if (mHeaderLayout == null) {
                    mHeaderLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.header_view, this, false);
                    ViewGroup.LayoutParams lp = mHeaderLayout.getLayoutParams();
                    if (lp == null) {
                        lp = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, maxHeight);
                    }
                    mHeaderLayout.setY(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -maxHeight, getResources().getDisplayMetrics()));  //设置初始位置，不可见
                    Log.d(TAG, "ACTION_DOWN >> addView()");
                    addView(mHeaderLayout, 0, lp);  //添加到布局中，但其实此时不可见
                    mHeaderLayout.bringToFront();  //改变在同一层级的控件中的z轴的顺序，放到最上层
                }
                break;
            case MotionEvent.ACTION_MOVE:
                xVelocity = mVelocityTracker.getXVelocity();
                yVelocity = mVelocityTracker.getYVelocity();
                Log.d(TAG, "onTouchEvent() >> ACTION_MOVE, xVelocity:" + xVelocity + ", yVelocity:" + yVelocity);

                if (xVelocity < yVelocity) {  //说明是上下滑动的手势
                    float offsetY = event.getY() - actionY;
                    if (offsetY > touchSlop) {  //超过系统设定距离，为滑动手势
                        if (mHeaderLayout != null) {
                            mHeaderLayout.setY(offsetY);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }
}
