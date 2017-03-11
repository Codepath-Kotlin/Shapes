package com.codepath.shapes.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * Created by JaneChung on 3/7/17.
 */

public abstract class Shape {
    protected Paint mPaint;
    private float mXPosition;
    private float mYPosition;
    @ColorInt
    private int mColor;

    public Shape(float x, float y, @ColorInt int color) {
        mXPosition = x;
        mYPosition = y;
        mColor = color;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColor);

    }

    public float getX() {
        return mXPosition;
    }

    public void setX(float x) {
        mXPosition = x;
    }

    public float getY() {
        return mYPosition;
    }

    public void setY(float y) {
        mYPosition = y;
    }

    @ColorInt
    public int getColor() {
        return mColor;
    }

    public void setColor(@ColorInt int color) {
        mColor = color;
        mPaint.setColor(mColor);
    }

    public abstract void draw(@NonNull Canvas canvas);

    //serialize method ?

}
