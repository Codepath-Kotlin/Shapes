package com.codepath.shapes.shape;

import android.graphics.Canvas;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

public class Circle extends Shape {

    private float mRadius;

    public Circle(float x, float y, @ColorInt int color, float radius) {
        super(x, y, color);
        this.mRadius = radius;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawCircle(getX(), getY(), getRadius(), mPaint);
    }
}
