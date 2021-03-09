package com.liweiyap.narradir.utils;

import android.content.Context;
import android.util.AttributeSet;

public interface Checkable
{
    void setDrawables(final Context context, final AttributeSet attrs);

    void setCheckedDrawable(final Context context, final int drawableId);
    void setUncheckedDrawable(final Context context, final int drawableId);

    void check();
    void uncheck();

    boolean isChecked();
}
