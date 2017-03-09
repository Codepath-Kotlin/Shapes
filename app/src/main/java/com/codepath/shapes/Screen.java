package com.codepath.shapes;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.codepath.shapes.shape.Shape;

import java.util.List;

/**
 * Created by andre on 3/7/17.
 */

public class Screen extends View {

    @Nullable
    private List<Shape> mShapeList;
    private Paint mDefaultPaint = new Paint();

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

    public void setShapeList(@Nullable List<Shape> shapeList) {
        mShapeList = shapeList;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mShapeList != null) {
            for (Shape shape : mShapeList) {
                shape.draw(canvas, mDefaultPaint);
            }
        }
    }
}
