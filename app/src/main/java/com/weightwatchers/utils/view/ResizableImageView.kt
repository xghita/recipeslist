package com.weightwatchers.utils.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.ceil

open class ResizableImageView(context: Context?, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val d = drawable
        if (d != null) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = ceil((width.toFloat() * d.intrinsicHeight.toFloat() / d.intrinsicWidth.toFloat()).toDouble()).toInt()
            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}