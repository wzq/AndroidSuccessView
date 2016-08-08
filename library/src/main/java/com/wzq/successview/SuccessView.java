package com.wzq.successview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by wzq on 15/9/2.
 */
public class SuccessView extends View {
    private float mDensity = -1;

    private Paint mPaint, nPaint;

    private float minWidth;

    private float minHeight;

    private float angle, startAngle = -90;

    private final float CONST_RADIUS = dip2px(1.2f);
    private final float CONST_RECT_WEIGHT = dip2px(3);
    private final float CONST_LEFT_RECT_W = dip2px(15);
    private final float CONST_RIGHT_RECT_W = dip2px(25);

    private float mLeftRectWidth = 0;
    private float mRightRectWidth = 0;

    public SuccessView(Context context) {
        this(context, null);
    }

    public SuccessView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuccessView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SuccessView);
        int pColor = typedArray.getColor(R.styleable.SuccessView_svStrokeColor, 0xffA5DC86);
        float strokeWidth = typedArray.getFloat(R.styleable.SuccessView_svStrokeWidth, 2.5f);
        typedArray.recycle(); //should recycle

        minWidth = dip2px(50);
        minHeight = dip2px(50);
        mPaint = new Paint();
        mPaint.setColor(pColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(0.8f);
        mPaint.setAntiAlias(true);

        nPaint = new Paint();
        nPaint.setAntiAlias(true);
        nPaint.setStyle(Paint.Style.STROKE);
        nPaint.setStrokeWidth(dip2px(strokeWidth));
        nPaint.setColor(pColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        Rect bounds = canvas.getClipBounds();
        float left, right, top, bottom;
        if (bounds.width() > bounds.height()) {
            float distance = (bounds.width() / 2 - bounds.height() / 2);
            left = bounds.left + distance;
            right = bounds.right - distance;
            top = bounds.top;
            bottom = bounds.bottom;
        } else if (bounds.width() < bounds.height()) {
            float distance = (bounds.height() / 2 - bounds.width() / 2);
            top = bounds.top + distance;
            bottom = bounds.bottom - distance;
            left = bounds.left;
            right = bounds.right;
        } else {
            left = bounds.left;
            right = bounds.right;
            top = bounds.top;
            bottom = bounds.bottom;
        }
        RectF oval = new RectF(left + dip2px(2f), top + dip2px(2f), right - dip2px(2f), bottom - dip2px(2f));
        canvas.drawArc(oval, startAngle, angle, false, nPaint);


        int totalW = getWidth();
        int totalH = getHeight();

        canvas.rotate(45, totalW / 2, totalH / 2);

        totalW /= 1.2;
        totalH /= 1.4;

        RectF leftRect = new RectF();

        if (mLeftRectWidth > 0) {
            leftRect.left = (totalW - CONST_LEFT_RECT_W) / 2 + CONST_RECT_WEIGHT;
            leftRect.right = leftRect.left + dip2px(mLeftRectWidth);
            leftRect.top = (totalH + CONST_RIGHT_RECT_W) / 2;
            leftRect.bottom = leftRect.top + CONST_RECT_WEIGHT;
            canvas.drawRoundRect(leftRect, CONST_RADIUS, CONST_RADIUS, mPaint);
        }
        if (mRightRectWidth > 0) {
            RectF rightRect = new RectF();
            rightRect.bottom = (totalH + CONST_RIGHT_RECT_W) / 2 + CONST_RECT_WEIGHT - 1;
            rightRect.left = (totalW + CONST_LEFT_RECT_W) / 2;
            rightRect.right = rightRect.left + CONST_RECT_WEIGHT;
            rightRect.top = rightRect.bottom - dip2px(mRightRectWidth);
            canvas.drawRoundRect(rightRect, CONST_RADIUS, CONST_RADIUS, mPaint);
        }

        super.onDraw(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = (int) (getPaddingLeft() + minWidth + getPaddingRight());
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = (int) (getPaddingTop() + minHeight + getPaddingBottom());
        }

        setMeasuredDimension(width, height);
    }

    public float dip2px(float dpValue) {
        if (mDensity == -1) {
            mDensity = getResources().getDisplayMetrics().density;
        }
        return dpValue * mDensity + 0.5f;
    }


    public void startAnim(int startDelay) {
        clearAnimation();
        ValueAnimator animator = ValueAnimator.ofFloat(0, 60f, 120f, 180f, 240f, 300f, 360f, 375f, 400f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                angle = -value;
                if (value>360 && value<=375){
                    mLeftRectWidth = value - 360;
                }else if(value>375){
                    mRightRectWidth = value - 375;
                }
                invalidate();
            }
        });

        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setStartDelay(startDelay);
        animator.start();
    }
}
