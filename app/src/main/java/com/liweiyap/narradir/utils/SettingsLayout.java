package com.liweiyap.narradir.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liweiyap.narradir.R;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableObserverButton;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableTextView;

public class SettingsLayout extends LinearLayout
{
    public SettingsLayout(Context context)
    {
        super(context);
    }

    public SettingsLayout(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SettingsLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Assumes that SettingsLayout has been initialised in an XML file, and that the same XML file also
     * initialises a CustomTypefaceableTextView that is a child view of this object and that serves as the title
     *
     * @param key String that serves as the new title
     */
    public void setKey(String key)
    {
        CustomTypefaceableTextView keyTextView = findViewById(R.id.keyTextView);
        if (keyTextView != null)
        {
            keyTextView.setText(key);
        }
    }

    /**
     * Assumes that SettingsLayout has been initialised in an XML file, and that the same XML file also
     * initialises a CustomTypefaceableTextView that is a child view of this object and that serves as the subheading
     *
     * @param value String that serves as the new subheading
     */
    public void setValue(String value)
    {
        CustomTypefaceableTextView valueTextView = findViewById(R.id.valueTextView);
        if (valueTextView != null)
        {
            valueTextView.setText(value);
        }
    }

    /**
     * Assumes that SettingsLayout has been initialised in an XML file, and that the same XML file also
     * initialises a CustomTypefaceableObserverButton that is a child view of this object and that serves as the edit button
     */
    public CustomTypefaceableObserverButton getEditButton()
    {
        return findViewById(R.id.editButton);
    }
}