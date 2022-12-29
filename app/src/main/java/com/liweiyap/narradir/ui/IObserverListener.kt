package com.liweiyap.narradir.ui

import com.liweiyap.narradir.util.IObserver

interface IObserverListener {
    fun addOnClickObserver(observer: IObserver?)
    fun notifyOnClickObservers()
    fun clearOnClickObservers()
    fun addOnLongClickObserver(observer: IObserver?)
    fun notifyOnLongClickObservers()
    fun clearOnLongClickObservers()
}