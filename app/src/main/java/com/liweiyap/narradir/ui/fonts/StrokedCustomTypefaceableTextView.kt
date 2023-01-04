package com.liweiyap.narradir.ui.fonts

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue

import androidx.annotation.ColorInt

import com.liweiyap.narradir.R

open class StrokedCustomTypefaceableTextView:
    CustomTypefaceableTextView,
    IOutlineable
{
    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        setStroke(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        setStroke(context, attrs)
    }

    constructor(context: Context, assetFontPath: String?): super(context, assetFontPath)

    constructor(context: Context, @ColorInt strokeColour: Int, strokeWidth: Float): super(context) {
        setStrokeColor(strokeColour)
        setStrokeWidth(strokeWidth)
    }

    constructor(context: Context, assetFontPath: String?, @ColorInt strokeColour: Int, strokeWidth: Float): super(context, assetFontPath) {
        setStrokeColor(strokeColour)
        setStrokeWidth(strokeWidth)
    }

    override fun onDraw(canvas: Canvas) {
        if (mStrokeWidth <= 0) {
            super.onDraw(canvas)
            return
        }

        mIsDrawing = true

        val p: Paint = paint
        p.style = Paint.Style.FILL
        super.onDraw(canvas)

        @ColorInt val currentTextColor: Int = currentTextColor
        p.style = Paint.Style.STROKE
        p.strokeWidth = mStrokeWidth
        setTextColor(mStrokeColor)
        super.onDraw(canvas)

        setTextColor(currentTextColor)
        mIsDrawing = false
    }

    override fun invalidate() {
        // Ignore invalidate() calls when isDrawing == true
        // (These calls are triggered by calls to setTextColor(color) calls, which would create an infinite loop)
        if (mIsDrawing) {
            return
        }

        super.invalidate()
    }

    final override fun setStroke(context: Context, attrs: AttributeSet?) {
        attrs?.let {
            @SuppressLint("CustomViewStyleable") val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.StrokedTextAttrs)
            mStrokeColor = typedArray.getColor(R.styleable.StrokedTextAttrs_textStrokeColor, mStrokeColor)
            mStrokeWidth = typedArray.getFloat(R.styleable.StrokedTextAttrs_textStrokeWidth, mStrokeWidth)
            typedArray.recycle()

            setStrokeColor(mStrokeColor)
            setStrokeWidth(mStrokeWidth)
        }
    }

    final override fun setStrokeColor(@ColorInt color: Int) {
        mStrokeColor = color
    }

    final override fun setStrokeWidth(width: Float) {
        // convert values specified in dp in XML layout to px,
        // otherwise stroke width would appear different on different screens
        mStrokeWidth = dpToPx(context = context, dp = width).toFloat()
    }

    final override fun setStrokeWidth(unit: Int, width: Float) {
        mStrokeWidth = TypedValue.applyDimension(unit, width, context.resources.displayMetrics)
    }

    /**
     * Convenience method to convert density independent pixel(dp) value
     * into device display specific pixel value.
     *
     * @param context Context to access device specific display metrics
     * @param dp density independent pixel value
     * @return device specific pixel value.
     */
    private fun dpToPx(context: Context, dp: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dp * scale + 0.5F).toInt()
    }

    // should be safe to be stored as an int; it represents a packed color int, AARRGGBB. If applied to an int array, every element in the array represents a color integer.
    // Hence, it's not a resource ID (R.color.); it's already been converted from said resource ID. We even use TypedArray::getColor() instead of TypedArray::getInt()
    @ColorInt private var mStrokeColor: Int = currentTextColor
    private var mStrokeWidth: Float = 0F
    private var mIsDrawing: Boolean = false
}