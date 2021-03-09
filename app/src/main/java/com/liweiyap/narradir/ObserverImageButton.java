package com.liweiyap.narradir;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ObserverImageButton
    extends androidx.appcompat.widget.AppCompatImageButton
    implements View.OnClickListener, View.OnLongClickListener
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
        return true;  // https://stackoverflow.com/a/3756619/12367873
    }

    public void addOnClickObserver(Observer observer)
    {
        mOnClickObservers.add(observer);
    }

    private void notifyOnClickObservers()
    {
        mOnClickObservers.forEach(Observer::update);
    }

    public void addOnLongClickObserver(Observer observer)
    {
        mOnLongClickObservers.add(observer);
    }

    private void notifyOnLongClickObservers()
    {
        mOnLongClickObservers.forEach(Observer::update);
    }

    private final List<Observer> mOnClickObservers = new ArrayList<>();
    private final List<Observer> mOnLongClickObservers = new ArrayList<>();
}
