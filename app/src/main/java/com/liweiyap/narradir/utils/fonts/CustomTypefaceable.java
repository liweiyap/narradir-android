package com.liweiyap.narradir.utils.fonts;

import android.content.Context;
import android.util.AttributeSet;

public interface CustomTypefaceable {
    void setCustomTypeface(final Context context, final AttributeSet attrs);
    void setCustomTypeface(final Context context, final String assetFontPath);
}
