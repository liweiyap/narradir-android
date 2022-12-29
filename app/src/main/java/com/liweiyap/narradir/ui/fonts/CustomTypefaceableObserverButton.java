package com.liweiyap.narradir.ui.fonts;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liweiyap.narradir.ui.ObserverButton;
import com.liweiyap.narradir.util.fonts.CustomFontSetter;

public class CustomTypefaceableObserverButton
    extends ObserverButton
    implements ICustomTypefaceable
{
    public CustomTypefaceableObserverButton(@NonNull Context context)
    {
        super(context);
    }

    public CustomTypefaceableObserverButton(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        setCustomTypeface(context, attrs);
    }

    public CustomTypefaceableObserverButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setCustomTypeface(context, attrs);
    }

    public CustomTypefaceableObserverButton(@NonNull Context context, final String assetFontPath)
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