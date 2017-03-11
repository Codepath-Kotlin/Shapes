package com.codepath.shapes.shape;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

public class Rectangle extends Shape {

    private float mWidth;
    private float mHeight;

    // Recycled RectF for drawing
    private RectF mRectF = new RectF();

    public Rectangle(float x, float y, @ColorInt int color, float width, float height) {
        super(x, y, color);
        mWidth = width;
        mHeight = height;
    }

    public float getWidth() {
        return mWidth;
    }

    public void setWidth(Float mWidth) {
        this.mWidth = mWidth;
    }

    public float getHeight() {
        return mHeight;
    }

    public void setHeight(Float mHeight) {
        this.mHeight = mHeight;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        //TODO: Extension to set a ReactF using center point, width and height.
        final float left = getX() - mWidth / 2f;
        final float top = getY() - mHeight / 2f;
        final float right = getX() + mWidth / 2f;
        final float bottom = getY() + mHeight / 2f;

        mRectF.set(left, top, right, bottom);
        canvas.drawRect(mRectF, mPaint);
    }
}
