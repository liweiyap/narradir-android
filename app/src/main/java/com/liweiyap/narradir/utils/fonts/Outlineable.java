package com.liweiyap.narradir.utils.fonts;

import android.content.Context;
import android.util.AttributeSet;

public interface Outlineable
{
    void setStroke(final Context context, final AttributeSet attrs);
    void setStrokeColor(final int color);
    void setStrokeWidth(final float width);
    void setStrokeWidth(final int unit, final float width);
}
