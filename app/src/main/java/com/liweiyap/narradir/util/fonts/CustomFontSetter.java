package com.liweiyap.narradir.util.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.liweiyap.narradir.R;

public final class CustomFontSetter
{
    private CustomFontSetter(){}

    /**
     * Sets a font on a TextView based on the custom assetFontPath attribute.
     * If the custom assetFontPath attribute isn't found in the attributes, nothing happens.
     * Can also be used on Buttons, because Button extends (is a subclass of) TextView.
     * Context required for getAssets() function.
     */
    public static void setCustomFont(final TextView textView, final @org.jetbrains.annotations.NotNull Context context, final AttributeSet attrs)
    {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomFont);
        String assetFontPath = typedArray.getString(R.styleable.CustomFont_assetFontPath);
        setCustomFont(textView, assetFontPath, context);
        typedArray.recycle();
    }

    /**
     * Sets a font on a TextView
     */
    public static void setCustomFont(final TextView textView, final String assetFontPath, final @org.jetbrains.annotations.NotNull Context context)
    {
        if ( (assetFontPath == null) || (textView == null) )
        {
            return;
        }

        Typeface typeface = FontCache.get(assetFontPath, context);
        if (typeface != null)
        {
            textView.setTypeface(typeface);
        }
    }
}