package com.liweiyap.narradir.ui;

import android.content.Context;
import android.util.AttributeSet;

public interface Checkable
{
    void check();
    void uncheck();
    void toggle();

    boolean isChecked();
}