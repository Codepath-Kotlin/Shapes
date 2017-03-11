package com.codepath.shapes;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.codepath.shapes.shape.Shape;

import java.util.LinkedList;
import java.util.List;

public class Screen extends View {

    @NonNull
    private List<Shape> mShapeList = new LinkedList<>();

    @Nullable
    private OnTapListener mOnTapListener;

    private GestureDetectorCompat mGestureDetector;

    public Screen(Context context) {
        super(context);
    }

    public Screen(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Screen(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Screen(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void addShape(@Nullable Shape shape) {
        mShapeList.add(shape);
        invalidate();
    }

    public void clear() {
        mShapeList.clear();
        invalidate();
    }

    public void setOnTapListener(@Nullable OnTapListener onTapListener) {
        mOnTapListener = onTapListener;
    }

    public void init() {
        GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (mOnTapListener != null) {
                    mOnTapListener.onTap(e.getX(), e.getY());
                }
                return true;
            }
        };
        //TODO: Convert to single line using apply
        mGestureDetector = new GestureDetectorCompat(getContext(), simpleOnGestureListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Shape shape : mShapeList) {
            shape.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            mGestureDetector.onTouchEvent(event);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public interface OnTapListener {
        void onTap(float x, float y);
    }
}
