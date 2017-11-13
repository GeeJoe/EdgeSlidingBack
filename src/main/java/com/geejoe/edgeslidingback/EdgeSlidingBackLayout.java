package com.geejoe.edgeslidingback;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by JoeLee on 2017/6/3 0003 09:00.
 */

public class EdgeSlidingBackLayout extends FrameLayout {

    private EdgeSlidingBackActivity mActivity;
    private EdgeSlidingBackFragment mFragment;

    private Scroller mScroller;

    private int mScreenWidth;
    //可滑动返回的操作区域宽度
    private int mTouchAreaWidth;
    //有效滑动距离
    private int mTouchSlope;

    //解决滑动冲突
    private int mLastX;
    private int mLastY;
    private int mActionDownX;

    private VelocityTracker vt;
    private int mPointedId;
    private float mVelocityX;
    private float mMinVelocity;

    private Drawable mShadow;
    private int mShadowWidth;

    public EdgeSlidingBackLayout(Context context) {
        this(context, null, 0);
    }

    public EdgeSlidingBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EdgeSlidingBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mMinVelocity = (5000 * 440) / dm.densityDpi;
        mScreenWidth = dm.widthPixels;
        //屏幕宽度的十二分之一作为可滑动返回的操作区域宽度
        mTouchAreaWidth = mScreenWidth / 12;
        mTouchSlope = ViewConfiguration.get(context).getScaledTouchSlop();
        mShadow = context.getResources().getDrawable(R.drawable.left_shadow);
        mShadowWidth = (int) context.getResources().getDisplayMetrics().density * 16;
    }

    public void bindActivity(EdgeSlidingBackActivity activity) {
        this.mActivity = activity;
        //将EdgeSlidingBackLayout作为DecorView的唯一子布局
        Window window = mActivity.getWindow();
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View rootView = decorView.getChildAt(0);
        decorView.removeView(rootView);
        addView(rootView);
        decorView.addView(this);
    }

    public void bindFrgment(EdgeSlidingBackFragment fragment) {
        this.mFragment = fragment;
        // TODO
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mActionDownX = (int) ev.getX();
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (mActionDownX < mTouchAreaWidth && deltaX > mTouchSlope
                        && Math.abs(deltaX) > Math.abs(deltaY)) {
                    intercept = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (vt == null) {
            vt = VelocityTracker.obtain();
        }
        vt.addMovement(event);
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPointedId = event.getPointerId(0);
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                vt.computeCurrentVelocity(1000);
                mVelocityX = VelocityTrackerCompat.getXVelocity(vt, mPointedId);
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (mActionDownX < mTouchAreaWidth
                        && Math.abs(deltaX) > Math.abs(deltaY)) {
                    int moveX = -deltaX;
                    if (-getScrollX() < 0) {
                        scrollTo(0, 0);
                    } else {
                        scrollBy(moveX, 0);
                    }
                }
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                Log.d("EdgeSlidingBackLayout", "onTouchEvent()@EdgeSlidingBackLayout.java:144-->>" + "mVelocityX：" + mVelocityX);
                if (mVelocityX > mMinVelocity || -getScrollX() > mScreenWidth / 3) {
                    scrollRightOut();
                } else {
                    scrollResume();
                }
                break;
        }
        return true;
    }

    private void scrollResume() {
        int startX = getScrollX();
        int distance = -getScrollX();
        mScroller.startScroll(startX, 0, distance, 0, 300);
        invalidate();
    }

    private void scrollRightOut() {
        int startX = getScrollX();
        int distance = -mScreenWidth - startX;
        mScroller.startScroll(startX, 0, distance, 0, 300);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        } else if (-getScrollX() >= getWidth()) {
            //
            mActivity.onSlidingBack();
            mActivity.finish();
            mActivity.overridePendingTransition(R.anim.anim_slide_in, R.anim.anim_slide_out);
            Log.d("EdgeSlidingBackLayout", "computeScroll()@EdgeSlidingBackLayout.java:161-->>" + mActivity + "销毁");
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawShadow(canvas);
    }

    private void drawShadow(Canvas canvas) {
        mShadow.setBounds(0, 0, mShadowWidth, getHeight());
        canvas.save();
        canvas.translate(-mShadowWidth, 0);
        mShadow.draw(canvas);
        canvas.restore();
    }

    interface OnSlidingBackListener {
        void onSlidingBack();
    }
}