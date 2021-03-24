package com.liweiyap.narradir.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ObserverButton
    extends androidx.appcompat.widget.AppCompatButton
    implements View.OnClickListener, View.OnLongClickListener, ObserverListener
{
    public ObserverButton(@NonNull Context context)
    {
        super(context);
        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    public ObserverButton(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    public ObserverButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        notifyOnClickObservers();
    }

    @Override
    public boolean onLongClick(View view)
    {
        notifyOnLongClickObservers();
        return true;  // https://stackoverflow.com/a/3756619/12367873; https://stackoverflow.com/questions/4402740/android-long-click-on-a-button-perform-actions
    }

    @Override
    public void addOnClickObserver(Observer observer)
    {
        mOnClickObservers.add(observer);
    }

    @Override
    public void notifyOnClickObservers()
    {
        mOnClickObservers.forEach(Observer::update);
    }

    @Override
    public void addOnLongClickObserver(Observer observer)
    {
        mOnLongClickObservers.add(observer);
    }

    @Override
    public void notifyOnLongClickObservers()
    {
        mOnLongClickObservers.forEach(Observer::update);
    }

    protected final List<Observer> mOnClickObservers = new ArrayList<>();
    protected final List<Observer> mOnLongClickObservers = new ArrayList<>();
}