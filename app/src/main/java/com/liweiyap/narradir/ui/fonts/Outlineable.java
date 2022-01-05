package com.liweiyap.narradir.ui.fonts;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;

public interface Outlineable
{
    void setStroke(final Context context, final AttributeSet attrs);
    void setStrokeColor(final @ColorInt int color);
    void setStrokeWidth(final float width);
    void setStrokeWidth(final int unit, final float width);
}