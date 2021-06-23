package com.liweiyap.narradir.utils;

public interface ObserverListener
{
    void addOnClickObserver(final Observer observer);
    void notifyOnClickObservers();
    void addOnLongClickObserver(final Observer observer);
    void notifyOnLongClickObservers();
}