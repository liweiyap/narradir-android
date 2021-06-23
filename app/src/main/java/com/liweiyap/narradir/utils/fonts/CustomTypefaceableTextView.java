package com.liweiyap.narradir.utils.fonts;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * https://stackoverflow.com/questions/16901930/memory-leaks-with-custom-font-for-set-custom-font/16902532#16902532
 */
public class CustomTypefaceableTextView
    extends androidx.appcompat.widget.AppCompatTextView
    implements CustomTypefaceable
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

    public CustomTypefaceableTextView(@NonNull Context context, final String assetFontPath)
    {
        this(context);
        setCustomTypeface(context, assetFontPath);
    }

    @Override
    public void setCustomTypeface(final Context context, final AttributeSet attrs)
    {
        CustomFontSetter.setCustomFont(this, context, attrs);
    }

    @Override
    public void setCustomTypeface(final Context context, final String assetFontPath)
    {
        CustomFontSetter.setCustomFont(this, assetFontPath, context);
    }
}