package com.liweiyap.narradir.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View

import androidx.appcompat.widget.AppCompatImageButton

open class ObserverImageButton:
    AppCompatImageButton,
    View.OnClickListener, View.OnLongClickListener, IObserverListener
{
    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        setOnClickListener(this)
        setOnLongClickListener(this)
    }

    fun destroy() {
        clearOnClickObservers()
        clearOnLongClickObservers()

        mOnClickObservers = null
        mOnLongClickObservers = null

        setOnClickListener(null)
        setOnLongClickListener(null)
    }

    override fun onClick(view: View) {
        notifyOnClickObservers()
    }

    override fun onLongClick(view: View): Boolean {
        notifyOnLongClickObservers()
        return true  // https://stackoverflow.com/a/3756619/12367873; https://stackoverflow.com/questions/4402740/android-long-click-on-a-button-perform-actions
    }

    override fun addOnClickObserver(observer: () -> Unit) {
        mOnClickObservers?.add(observer)
    }

    override fun notifyOnClickObservers() {
        mOnClickObservers?.forEach { observer: () -> Unit ->
            observer()
        }
    }

    override fun clearOnClickObservers() {
        mOnClickObservers?.clear()
    }

    override fun addOnLongClickObserver(observer: () -> Unit) {
        mOnLongClickObservers?.add(observer)
    }

    override fun notifyOnLongClickObservers() {
        mOnLongClickObservers?.forEach { observer: () -> Unit ->
            observer()
        }
    }

    override fun clearOnLongClickObservers() {
        mOnLongClickObservers?.clear()
    }

    private var mOnClickObservers: MutableList<() -> Unit>? = ArrayList()
    private var mOnLongClickObservers: MutableList<() -> Unit>? = ArrayList()
}