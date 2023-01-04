package com.liweiyap.narradir.ui.fonts

import android.content.Context
import android.util.AttributeSet

import androidx.annotation.ColorInt

interface IOutlineable {
    fun setStroke(context: Context, attrs: AttributeSet?)
    fun setStrokeColor(@ColorInt color: Int)
    fun setStrokeWidth(width: Float)
    fun setStrokeWidth(unit: Int, width: Float)
}