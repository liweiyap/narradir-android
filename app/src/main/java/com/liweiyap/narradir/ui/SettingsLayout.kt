package com.liweiyap.narradir.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

import com.liweiyap.narradir.R
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableObserverButton
import com.liweiyap.narradir.ui.fonts.CustomTypefaceableTextView

class SettingsLayout: LinearLayout {
    constructor(context: Context?): super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    /**
     * Assumes that SettingsLayout has been initialised in an XML file, and that the same XML file also
     * initialises a CustomTypefaceableTextView that is a child view of this object and that serves as the title
     *
     * @param key String that serves as the new title
     */
    fun setKey(key: String?) {
        key?.let {
            val keyTextView = findViewById<CustomTypefaceableTextView>(R.id.keyTextView)
                ?: return
            keyTextView.text = it
        }
    }

    /**
     * Assumes that SettingsLayout has been initialised in an XML file, and that the same XML file also
     * initialises a CustomTypefaceableTextView that is a child view of this object and that serves as the subheading
     *
     * @param value String that serves as the new subheading
     */
    fun setValue(value: String?) {
        value?.let {
            val valueTextView = findViewById<CustomTypefaceableTextView>(R.id.valueTextView)
                ?: return
            valueTextView.text = it
        }
    }

    /**
     * Assumes that SettingsLayout has been initialised in an XML file, and that the same XML file also
     * initialises a CustomTypefaceableObserverButton that is a child view of this object and that serves as the edit button
     */
    val editButton: CustomTypefaceableObserverButton
        get() = findViewById(R.id.editButton)
}