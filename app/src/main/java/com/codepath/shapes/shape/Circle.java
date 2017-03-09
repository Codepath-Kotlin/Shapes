package com.codepath.shapes.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

/**
 * Created by JaneChung on 3/7/17.
 */

public class Circle extends Shape {

    private float mRadius;

    public Circle(float x, float y, float radius) {
        super(x, y);
        this.mRadius = radius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }

    public float getRadius() {
        return mRadius;
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint) {
        canvas.drawCircle(getX(), getY(), getRadius(), paint);
    }
}
