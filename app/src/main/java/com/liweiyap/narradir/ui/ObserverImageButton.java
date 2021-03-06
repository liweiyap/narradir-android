package com.liweiyap.narradir.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liweiyap.narradir.util.Observer;

import java.util.ArrayList;
import java.util.List;

public class ObserverImageButton
    extends androidx.appcompat.widget.AppCompatImageButton
    implements View.OnClickListener, View.OnLongClickListener, ObserverListener
{
    public ObserverImageButton(@NonNull Context context)
    {
        super(context);
        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    public ObserverImageButton(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    public ObserverImageButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
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
    public void addOnClickObserver(final Observer observer)
    {
        mOnClickObservers.add(observer);
    }

    @Override
    public void notifyOnClickObservers()
    {
        mOnClickObservers.forEach(Observer::update);
    }

    @Override
    public void addOnLongClickObserver(final Observer observer)
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