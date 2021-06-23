package com.liweiyap.narradir.ui;

import android.content.Context;
import android.util.AttributeSet;

public interface Checkable
{
    void setDrawableIds(final Context context, final AttributeSet attrs);

    void setCheckedDrawableId(final int drawableId);
    void setUncheckedDrawableId(final int drawableId);

    void check();
    void uncheck();
    void toggle();

    boolean isChecked();
}