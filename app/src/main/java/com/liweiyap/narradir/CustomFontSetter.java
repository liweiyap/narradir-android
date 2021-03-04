package com.liweiyap.narradir;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomFontSetter
{
    /**
     * Sets a font on a TextView based on the custom fontName attribute.
     * If the custom fontName attribute isn't found in the attributes, nothing happens.
     * Can also be used on Buttons, because Button extends (is a subclass of) TextView.
     */
    public static void setCustomFont(TextView textView, Context context, AttributeSet attrs)
    {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomFont);
        String fontName = typedArray.getString(R.styleable.CustomFont_fontName);
        setCustomFont(textView, fontName, context);
        typedArray.recycle();
    }

    /**
     * Sets a font on a TextView
     */
    public static void setCustomFont(TextView textView, String fontName, Context context)
    {
        if (fontName == null)
        {
            return;
        }

        Typeface typeface = FontCache.get(fontName, context);

        if (typeface != null)
        {
            textView.setTypeface(typeface);
        }
    }
}