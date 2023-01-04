package com.liweiyap.narradir.ui.fonts

import android.content.Context
import android.util.AttributeSet

import androidx.appcompat.widget.AppCompatTextView

import com.liweiyap.narradir.util.fonts.CustomFontSetter

/**
 * https://stackoverflow.com/questions/16901930/memory-leaks-with-custom-font-for-set-custom-font/16902532#16902532
 */
open class CustomTypefaceableTextView:
    AppCompatTextView,
    ICustomTypefaceable
{
    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        setCustomTypeface(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        setCustomTypeface(context, attrs)
    }

    constructor(context: Context, assetFontPath: String?): this(context) {
        setCustomTypeface(context, assetFontPath)
    }

    final override fun setCustomTypeface(context: Context, attrs: AttributeSet?) {
        CustomFontSetter.setCustomFont(this, context, attrs)
    }

    final override fun setCustomTypeface(context: Context, assetFontPath: String?) {
        CustomFontSetter.setCustomFont(this, assetFontPath, context)
    }
}