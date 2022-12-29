package com.liweiyap.narradir.util.fonts

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

import com.liweiyap.narradir.R

object CustomFontSetter {
    /**
     * Sets a font on a TextView based on the custom assetFontPath attribute.
     * If the custom assetFontPath attribute isn't found in the attributes, nothing happens.
     * Can also be used on Buttons, because Button extends (is a subclass of) TextView.
     * Context required for getAssets() function.
     */
    @JvmStatic
    fun setCustomFont(textView: TextView?, context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }

        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomFont)
        val assetFontPath: String? = typedArray.getString(R.styleable.CustomFont_assetFontPath)
        setCustomFont(textView, assetFontPath, context)
        typedArray.recycle()
    }

    /**
     * Sets a font on a TextView
     */
    @JvmStatic
    fun setCustomFont(textView: TextView?, assetFontPath: String?, context: Context) {
        if ( (assetFontPath == null) || (textView == null) ) {
            return
        }

        val typeface: Typeface? = FontCache.get(assetFontPath, context)
        if (typeface != null) {
            textView.typeface = typeface
        }
    }
}