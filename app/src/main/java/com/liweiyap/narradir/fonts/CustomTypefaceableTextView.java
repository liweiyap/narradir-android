package com.liweiyap.narradir.fonts;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * https://stackoverflow.com/questions/16901930/memory-leaks-with-custom-font-for-set-custom-font/16902532#16902532
 */
public class CustomTypefaceableTextView extends androidx.appcompat.widget.AppCompatTextView
{
    public CustomTypefaceableTextView(@NonNull Context context)
    {
        super(context);
    }

    public CustomTypefaceableTextView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        setCustomTypeface(context, attrs);
    }

    public CustomTypefaceableTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setCustomTypeface(context, attrs);
    }

    public void setCustomTypeface(final Context context, final AttributeSet attrs)
    {
        CustomFontSetter.setCustomFont(this, context, attrs);
    }

    public void setCustomTypeface(final Context context, final String fontName)
    {
        CustomFontSetter.setCustomFont(this, fontName, context);
    }
}
