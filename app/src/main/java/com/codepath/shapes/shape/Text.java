package com.codepath.shapes.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

/**
 * Created by JaneChung on 3/7/17.
 */

public class Text extends Shape {

    @NonNull
    private String mText;

    public Text(float x, float y, @NonNull String text) {
        super(x, y);
        mText = text;
    }

    public void setText(@NonNull String text) {
        mText = text;
    }

    @NonNull
    public String getText() {
        return mText;
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint) {
        canvas.drawText(mText, 0, mText.length(), getX(), getY(), paint);
    }
}
