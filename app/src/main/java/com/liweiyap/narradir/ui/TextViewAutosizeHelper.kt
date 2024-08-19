package com.liweiyap.narradir.ui

import android.util.TypedValue
import android.widget.TextView

import androidx.core.widget.TextViewCompat

object TextViewAutosizeHelper {
    /**
     * Programmatically sets the autoSizeMaxTextSize of a TextView to prevent it from getting too big when auto-sized according to user preferences,
     * thereby fixing the autoSizeTextSize (or minimising its range).
     * Especially useful if textSize happens to be equal to or even less than autoSizeMinTextSize (12 sp at the time of writing) because in most of our styles,
     * we set only autoSizeMaxTextSize, but then this will throw an IllegalArgumentException if we do not first set autoSizeMinTextSize to be small enough,
     * since autoSizeMaxTextSize cannot be smaller than autoSizeMinTextSize.
     * E.g. in style resource XML, we could do something like:
     * <item name="android:autoSizeMinTextSize">10 or 12 sp</item>
     * <item name="android:autoSizeMaxTextSize">10.5 or 12.5sp</item>
     * Here, because we don't know the px to sp ratio, we set autoSizeMaxTextSize just high enough so that it is clearly not less than or equal to autoSizeMinTextSize and should thus hopefully not throw another type of IllegalArgumentException.
     * But it's not that accurate because autoSizeMaxTextSize shouldn't simply be 10.5 sp; it should instead be as close as possible to textSize (or autoSizeMinTextSize, for that matter) without throwing the latter IllegalArgumentException.
     */
    fun minimiseAutoSizeTextSizeRange(textView: TextView?) {
        if (textView == null) {
            return
        }

        if (TextViewCompat.getAutoSizeTextType(textView) != TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM) {
            return
        }

        val minTextSize: Int = TextViewCompat.getAutoSizeMinTextSize(textView)

        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
            textView,
            minTextSize,
            minTextSize + 1,
            TextViewCompat.getAutoSizeStepGranularity(textView),
            TypedValue.COMPLEX_UNIT_PX)
    }
}