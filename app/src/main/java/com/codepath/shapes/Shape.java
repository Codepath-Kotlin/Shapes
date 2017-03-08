package com.codepath.shapes;

import android.graphics.Canvas;

/**
 * Created by JaneChung on 3/7/17.
 */

public abstract class Shape {

    private Float mXPosition;
    private Float mYPosition;

    abstract void draw(Canvas canvas);

    public void setX(Float x) {
        mXPosition = x;
    }

    public void setY(Float y) {
        mYPosition = y;
    }

}
