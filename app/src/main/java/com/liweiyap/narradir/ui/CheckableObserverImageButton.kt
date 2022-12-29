package com.liweiyap.narradir.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.Checkable

import com.liweiyap.narradir.R

/**
 * See: https://stackoverflow.com/a/34074176/12367873
 */
open class CheckableObserverImageButton:
    ObserverImageButton,
    Checkable
{
    constructor(context: Context): super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    /**
     * Toggles background based on the selector that we set in XML
     */
    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        }
        return drawableState
    }

    override fun setChecked(checked: Boolean) {
        alpha = if (checked) 1f else mAlphaUnchecked

        if (mIsChecked == checked) {
            return
        }

        mIsChecked = checked
        refreshDrawableState()
    }

    override fun toggle() {
        isChecked = !mIsChecked
    }

    override fun isChecked(): Boolean {
        return mIsChecked
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        attrs?.let {
            @SuppressLint("CustomViewStyleable") val typedArray: TypedArray = context.obtainStyledAttributes(it, R.styleable.CheckableInitHelper)
            mIsChecked = typedArray.getBoolean(R.styleable.CheckableInitHelper_defaultCheckedState, mIsChecked)
            mAlphaUnchecked = typedArray.getFloat(R.styleable.CheckableInitHelper_alphaUnchecked, mAlphaUnchecked)
            typedArray.recycle()
        }

        isChecked = mIsChecked
    }

    private var mIsChecked: Boolean = false
    private var mAlphaUnchecked: Float = 0.5f

    companion object {
        private val CHECKED_STATE_SET = intArrayOf(
            android.R.attr.state_checked
        )
    }
}