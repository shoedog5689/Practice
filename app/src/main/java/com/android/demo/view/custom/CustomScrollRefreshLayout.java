package com.android.demo.view.custom;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

import com.android.demo.R;

/**
 * Created by hewei on 2017/8/23.
 */

public class CustomScrollRefreshLayout extends ViewGroup {
    private final String TAG = CustomScrollRefreshLayout.class.getSimpleName();

    private LinearLayout mHeaderLayout;
    private VelocityTracker mVelocityTracker;
//    private int touchSlop;

    private float headerViewHeight;
    private float yOffset; //Y轴偏移量
    float actionX = 0;
    float actionY = 0;

    public CustomScrollRefreshLayout(Context context) {
        super(context);
        addHeaderView();
    }

    public CustomScrollRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        addHeaderView();
    }

    public CustomScrollRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addHeaderView();
    }

    private void addHeaderView() {
        //初始化顶部不可见的HeaderView
        headerViewHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, getContext().getResources().getDisplayMetrics());  //默认100dp高度
        if (mHeaderLayout == null) {
            mHeaderLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.header_view, this, false);
            ViewGroup.LayoutParams lp = mHeaderLayout.getLayoutParams();
            if (lp == null) {
                lp = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, (int) headerViewHeight);
            }
//            mHeaderLayout.setY(-headerViewHeight);  //设置初始位置，不可见
            addView(mHeaderLayout, 0, lp);  //添加到布局中，但其实此时不可见
            mHeaderLayout.bringToFront();  //改变在同一层级的控件中的z轴的顺序，放到最上层
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        Log.e(TAG, "onMeasure()");

        /**
         * 处理WRAP_CONTENT
         */

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

//        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

//        Log.d(TAG, "childCount:" + getChildCount());

        measureChildren(widthMeasureSpec, heightMeasureSpec);

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
//        Log.d(TAG, "onLayout() >> childCount:" + childCount);
        float totalVisibleHeight = 0; //除了headerView外的可视View的高度
        View child;
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                int width = child.getMeasuredWidth();
                if (mHeaderLayout == null) {  //Check again!
                    addHeaderView();
                }
                if (child.getId() != mHeaderLayout.getId()) {
                    int height = child.getMeasuredHeight();
                    //可视高度加上Y轴偏移量来确定ViewGroup的位置
//                    Log.d(TAG, "onLayout() >> totalVisibleHeight:" + totalVisibleHeight + ", yOffset:" + yOffset);
                    child.layout(0, (int) (totalVisibleHeight + yOffset), width, (int) (totalVisibleHeight + height + yOffset));
                    totalVisibleHeight += height;
                }else {
//                    Log.d(TAG, "onLayout() >> header view measured height:" + mHeaderLayout.getMeasuredHeight());
//                    child.layout(0, 0, width, (int) yOffset);  //仅布局一部分的方式
                    child.layout(0, (int) (yOffset - mHeaderLayout.getMeasuredHeight()), width, (int) yOffset);  //初始布局在顶部，随手势下移
                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.d(TAG, "onInterceptTouchEvent()");

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        mVelocityTracker.computeCurrentVelocity(1000);
        float xVelocity;
        float yVelocity;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onInterceptTouchEvent() >> ACTION_DOWN");
                actionX = event.getX();  //初始位置
                actionY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onInterceptTouchEvent() >> ACTION_MOVE");

                xVelocity = mVelocityTracker.getXVelocity();
                yVelocity = mVelocityTracker.getYVelocity();
                Log.d(TAG, "onTouchEvent() >> ACTION_MOVE, xVelocity:" + xVelocity + ", yVelocity:" + yVelocity);

                if (Math.abs(xVelocity) < Math.abs(yVelocity)) {  //说明是上下滑动的手势
                    yOffset = event.getY() - actionY;
                }

                Log.e(TAG, "yOffset:" + yOffset);
//                if () {
//
//                }
                if (yOffset > 0) {  //向下滑动，拦截交给自己处理
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onInterceptTouchEvent() >> ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "onInterceptTouchEvent() >> ACTION_CANCEL");
                break;
        }

        return false;
    }

    /**
     * 拦截了ACTION_MOVE之后，会走到onTouchEvent()中，这时需要重新计算move的距离然后重新布局才会生效，因为如果只在Interception中计算的话，
     * 后续如果拦截了是不会再走到Intercepterion()中的
     *
     * @param event
     * @return
     */
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
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent() >> ACTION_MOVE");
                xVelocity = mVelocityTracker.getXVelocity();
                yVelocity = mVelocityTracker.getYVelocity();
                Log.d(TAG, "onTouchEvent() >> ACTION_MOVE, xVelocity:" + xVelocity + ", yVelocity:" + yVelocity);

                if (Math.abs(xVelocity) < Math.abs(yVelocity)) {  //说明是上下滑动的手势
                    yOffset = event.getY() - actionY;
                    if (mHeaderLayout != null && yOffset > 0) {
                        requestLayout();  //刷新
                        invalidate();
                    }
                }
                break; //消费
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent() >> ACTION_UP");
                //松开手指后

                if (yOffset > 0) {  //当向下滑动时
                    resetViewToTop();
                    return true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "onTouchEvent() >> ACTION_CANCEL");
                break;
        }

        return true;
    }

    private void resetViewToTop() {
        Log.e(TAG, "get back to top");
        //平滑的回到初始位置，有两种方法

        //Method 1:属性动画
        ValueAnimator objectAnimator = ValueAnimator.ofFloat(yOffset, 0f);
        objectAnimator.setTarget(this);
        objectAnimator.setDuration(500).start();
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                yOffset = (float) animation.getAnimatedValue();
                requestLayout();
                invalidate();
            }
        });

        //Method 2:Scroller
    }
}
