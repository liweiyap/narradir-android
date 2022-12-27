package com.liweiyap.narradir.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liweiyap.narradir.util.Observer;

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

    public void destroy()
    {
        clearOnClickObservers();
        clearOnLongClickObservers();

        mOnClickObservers = null;
        mOnLongClickObservers = null;

        setOnClickListener(null);
        setOnLongClickListener(null);
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
        if (mOnClickObservers == null)
        {
            return;
        }

        mOnClickObservers.add(observer);
    }

    @Override
    public void notifyOnClickObservers()
    {
        if (mOnClickObservers == null)
        {
            return;
        }

        mOnClickObservers.forEach(Observer::update);
    }

    @Override
    public void clearOnClickObservers()
    {
        if (mOnClickObservers == null)
        {
            return;
        }

        mOnClickObservers.clear();
    }

    @Override
    public void addOnLongClickObserver(final Observer observer)
    {
        if (mOnLongClickObservers == null)
        {
            return;
        }

        mOnLongClickObservers.add(observer);
    }

    @Override
    public void notifyOnLongClickObservers()
    {
        if (mOnLongClickObservers == null)
        {
            return;
        }

        mOnLongClickObservers.forEach(Observer::update);
    }

    @Override
    public void clearOnLongClickObservers()
    {
        if (mOnLongClickObservers == null)
        {
            return;
        }

        mOnLongClickObservers.clear();
    }

    protected List<Observer> mOnClickObservers = new ArrayList<>();
    protected List<Observer> mOnLongClickObservers = new ArrayList<>();
}