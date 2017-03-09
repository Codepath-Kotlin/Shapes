package com.codepath.shapes.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * Created by JaneChung on 3/7/17.
 */

public class Rectangle extends Shape {

    private float mWidth;
    private float mHeight;

    // Recycled RectF for drawing
    private RectF mRectF = new RectF();

    public Rectangle(float x, float y, float width, float height) {
        super(x, y);
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
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint) {
        //TODO: Extension to set a ReactF using center point, width and height.
        final float left = getX() - mWidth / 2f;
        final float top = getX() - mHeight / 2f;
        final float right = getX() + mWidth / 2f;
        final float bottom = getX() - mHeight / 2f;

        mRectF.set(left, top, right, bottom);
        canvas.drawRect(mRectF, paint);
    }
}
