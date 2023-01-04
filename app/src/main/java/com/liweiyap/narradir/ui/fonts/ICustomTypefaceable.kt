package com.liweiyap.narradir.ui.fonts

import android.content.Context
import android.util.AttributeSet

interface ICustomTypefaceable {
    fun setCustomTypeface(context: Context, attrs: AttributeSet?)
    fun setCustomTypeface(context: Context, assetFontPath: String?)
}