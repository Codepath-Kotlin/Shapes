package com.codepath.shapes.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

/**
 * Created by JaneChung on 3/7/17.
 */

public abstract class Shape {
    private float mXPosition;
    private float mYPosition;

    public Shape(float x, float y) {
        mXPosition = x;
        mYPosition = y;
    }

    public void setX(float x) {
        mXPosition = x;
    }

    public void setY(float y) {
        mYPosition = y;
    }

    public float getX() {
        return mXPosition;
    }

    public float getY() {
        return mYPosition;
    }

    public abstract void draw(@NonNull Canvas canvas, @NonNull Paint paint);

    //serialize method ?

}
