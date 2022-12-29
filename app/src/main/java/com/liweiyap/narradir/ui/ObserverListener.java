package com.liweiyap.narradir.ui;

import com.liweiyap.narradir.util.IObserver;

public interface ObserverListener
{
    void addOnClickObserver(final IObserver observer);
    void notifyOnClickObservers();
    void clearOnClickObservers();
    void addOnLongClickObserver(final IObserver observer);
    void notifyOnLongClickObservers();
    void clearOnLongClickObservers();
}