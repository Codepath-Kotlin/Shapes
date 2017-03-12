package com.codepath.shapes.util

import android.graphics.RectF

fun RectF.setCenterAndDimensions(centerX: Float, centerY: Float, width: Float, height: Float) {
    val left = centerX - width / 2f
    val top = centerY - height / 2f
    val right = centerX + width / 2f
    val bottom = centerY + height / 2f

    set(left, top, right, bottom)
}