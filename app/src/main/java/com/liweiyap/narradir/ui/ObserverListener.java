package com.liweiyap.narradir.ui;

import com.liweiyap.narradir.util.Observer;

public interface ObserverListener
{
    void addOnClickObserver(final Observer observer);
    void notifyOnClickObservers();
    void clearOnClickObservers();
    void addOnLongClickObserver(final Observer observer);
    void notifyOnLongClickObservers();
    void clearOnLongClickObservers();
}