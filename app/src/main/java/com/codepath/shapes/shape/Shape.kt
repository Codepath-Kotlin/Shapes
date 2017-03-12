package com.codepath.shapes.shape

import android.graphics.Canvas
import android.graphics.Color.BLACK
import android.graphics.Paint
import android.graphics.RectF
import android.support.annotation.ColorInt
import com.codepath.shapes.util.setCenterAndDimensions

sealed class Shape(var x: Float = 0f, var y: Float = 0f, @ColorInt color: Int = BLACK) {
    protected var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        mPaint.color = color
    }

    var color: Int
        get() = mPaint.color
        set(@ColorInt color) {
            mPaint.color = color
        }

    abstract fun draw(canvas: Canvas)
}

class Rectangle(x: Float, y: Float, @ColorInt color: Int, var width: Float, var height: Float) : Shape(x, y, color) {
    // Recycled RectF for drawing
    private val mRectF = RectF()

    override fun draw(canvas: Canvas) {
        mRectF.setCenterAndDimensions(x, y, width, height)
        canvas.drawRect(mRectF, mPaint)
    }
}

class Circle(x: Float, y: Float, @ColorInt color: Int, var radius: Float) : Shape(x, y, color) {
    override fun draw(canvas: Canvas) = canvas.drawCircle(x, y, radius, mPaint)
}

class Text(x: Float, y: Float, @ColorInt color: Int, textSize: Float, var text: String) : Shape(x, y, color) {
    init {
        mPaint.textSize = textSize
    }

    var fontSize: Float
        get() = mPaint.textSize
        set(size) {
            mPaint.textSize = size
        }

    override fun draw(canvas: Canvas) = canvas.drawText(text, 0, text.length, x, y, mPaint)
}
