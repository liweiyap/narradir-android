package com.liweiyap.narradir.ui

interface IObserverListener {
    fun addOnClickObserver(observer: () -> Unit)
    fun notifyOnClickObservers()
    fun clearOnClickObservers()
    fun addOnLongClickObserver(observer: () -> Unit)
    fun notifyOnLongClickObservers()
    fun clearOnLongClickObservers()
}