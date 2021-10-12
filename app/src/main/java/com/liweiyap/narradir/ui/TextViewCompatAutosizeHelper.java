package com.liweiyap.narradir.ui;

import android.util.TypedValue;
import android.widget.TextView;

import androidx.core.widget.TextViewCompat;

public final class TextViewCompatAutosizeHelper
{
    private TextViewCompatAutosizeHelper(){}

    /**
     * Especially useful if textSize happens to be equal to autoSizeMinTextSize (12 sp at the time of writing)
     */
    public static void minimiseAutoSizeTextSizeRange(final TextView textView)
    {
        if (textView == null)
        {
            return;
        }

        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
            textView,
            TextViewCompat.getAutoSizeMinTextSize(textView),
            TextViewCompat.getAutoSizeMinTextSize(textView) + 1,
            TextViewCompat.getAutoSizeStepGranularity(textView),
            TypedValue.COMPLEX_UNIT_PX
        );
    }
}