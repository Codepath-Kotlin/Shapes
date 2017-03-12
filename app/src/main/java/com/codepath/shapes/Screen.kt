package com.codepath.shapes

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.support.v4.view.GestureDetectorCompat
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.codepath.shapes.shape.Shape

class Screen : View {
    var tapListener: ((x: Float, y: Float) -> Unit)? = null
    private val shapeList = mutableListOf<Shape>()
    private var mGestureDetector: GestureDetectorCompat

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        val simpleOnGestureListener = object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                tapListener?.invoke(e.x, e.y)
                return true
            }
        }
        mGestureDetector = GestureDetectorCompat(context, simpleOnGestureListener)
    }

    fun addShape(shape: Shape) {
        shapeList.add(shape)
        invalidate()
    }

    fun clear() {
        shapeList.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        shapeList.forEach { it.draw(canvas) }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mGestureDetector.onTouchEvent(event)
        return true
    }
}
