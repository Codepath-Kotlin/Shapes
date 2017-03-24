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
import com.codepath.shapes.io.ShapeList
import com.codepath.shapes.shape.Shape
import java.util.*

class Screen : View {
    var tapListener: ((x: Float, y: Float) -> Unit)? = null
    private val internalShapeList = mutableListOf<Shape>()
    private var mGestureDetector: GestureDetectorCompat

    var shapeList: ShapeList
        get() = Collections.unmodifiableList(internalShapeList)
        set(newList) {
            internalShapeList.run {
                clear()
                addAll(newList)
            }
            invalidate()
        }

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
        internalShapeList.add(shape)
        invalidate()
    }

    fun clear() {
        internalShapeList.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        internalShapeList.forEach { it.draw(canvas) }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mGestureDetector.onTouchEvent(event)
        return true
    }
}
