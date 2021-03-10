package com.liweiyap.narradir.utils;

public interface ObserverListener
{
    void addOnClickObserver(Observer observer);
    void notifyOnClickObservers();
    void addOnLongClickObserver(Observer observer);
    void notifyOnLongClickObservers();
}
