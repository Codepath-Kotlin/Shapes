package com.codepath.shapes.shape;

import android.graphics.Canvas;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * Created by JaneChung on 3/7/17.
 */

public class Text extends Shape {

    @NonNull
    private String mText;

    public Text(float x, float y, @ColorInt int color, float textSize, @NonNull String text) {
        super(x, y, color);
        mText = text;
        mPaint.setTextSize(textSize);
    }

    @NonNull
    public String getText() {
        return mText;
    }

    public void setText(@NonNull String text) {
        mText = text;
    }

    public float getFontSize() {
        return mPaint.getTextSize();
    }

    public void setFontSize(float size) {
        mPaint.setTextSize(size);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawText(mText, 0, mText.length(), getX(), getY(), mPaint);
    }
}
