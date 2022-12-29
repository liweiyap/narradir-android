package com.liweiyap.narradir.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liweiyap.narradir.util.IObserver;

import java.util.ArrayList;
import java.util.List;

public class ObserverImageButton
    extends androidx.appcompat.widget.AppCompatImageButton
    implements View.OnClickListener, View.OnLongClickListener, IObserverListener
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
    public void addOnClickObserver(final IObserver observer)
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

        mOnClickObservers.forEach(IObserver::update);
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
    public void addOnLongClickObserver(final IObserver observer)
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

        mOnLongClickObservers.forEach(IObserver::update);
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

    protected List<IObserver> mOnClickObservers = new ArrayList<>();
    protected List<IObserver> mOnLongClickObservers = new ArrayList<>();
}