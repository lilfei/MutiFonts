package com.llf.fonttest;

import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View {
    private Paint mPaint;
    private Point mPoint;
    private int mColor;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public MyView(Context context) {
        super(context);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(0xFFF00000);
        mPaint.setAntiAlias(true); // 抗锯齿
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mPoint.x, mPoint.y, 60, mPaint);
    }

    public void start() {
        final ValueAnimator animator = ValueAnimator.ofObject(new PointEvaluator(), new Point(60, 60), new Point(990, 1050));
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });

        ValueAnimator animator1;
        if (Build.VERSION.SDK_INT < 21) {
            animator1 = ValueAnimator.ofObject(new TextArgbEvaluator(), 0xFFF00000, 0xFFFFFF00);
        } else {
            animator1 = ValueAnimator.ofArgb(0xFFF00000, 0xFFFFFF00);
        }
        animator1.setRepeatCount(ValueAnimator.INFINITE);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mColor = (int) animation.getAnimatedValue();
                mPaint.setColor(mColor);
            }
        });
        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setDuration(3000);
        animationSet.setInterpolator(new LgDecelerateInterpolator());
        animationSet.play(animator).with(animator1);
        animationSet.start();
    }

    class PointEvaluator implements TypeEvaluator {
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Point startPoint = (Point) startValue;
            Point endPoint = (Point) endValue;
            int x = (int) (startPoint.x + fraction * (endPoint.x - startPoint.x));
            int y = (int) (startPoint.y + fraction * (endPoint.y - startPoint.y));
            return new Point(x, y);
        }
    }

    class LgDecelerateInterpolator implements TimeInterpolator {
        private float background;

        public LgDecelerateInterpolator() {
            background = 10;
        }

        @Override
        public float getInterpolation(float input) {
            return (1 - (float) Math.pow(background, -input));
        }
    }

    private class TextArgbEvaluator implements TypeEvaluator {
        //这段代码是从源码中抠出来的
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            int startInt = (Integer) startValue;
            int startA = (startInt >> 24) & 0xff;
            int startR = (startInt >> 16) & 0xff;
            int startG = (startInt >> 8) & 0xff;
            int startB = startInt & 0xff;

            int endInt = (Integer) endValue;
            int endA = (endInt >> 24) & 0xff;
            int endR = (endInt >> 16) & 0xff;
            int endG = (endInt >> 8) & 0xff;
            int endB = endInt & 0xff;

            return (int) ((startA + (int) (fraction * (endA - startA))) << 24) |
                    (int) ((startR + (int) (fraction * (endR - startR))) << 16) |
                    (int) ((startG + (int) (fraction * (endG - startG))) << 8) |
                    (int) ((startB + (int) (fraction * (endB - startB))));
        }
    }
}
