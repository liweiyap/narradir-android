package com.liweiyap.narradir.utils.fonts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liweiyap.narradir.utils.Observer;
import com.liweiyap.narradir.utils.ObserverListener;

import java.util.ArrayList;
import java.util.List;

public class CustomTypefaceableObserverButton
    extends androidx.appcompat.widget.AppCompatButton
    implements CustomTypefaceable, View.OnClickListener, View.OnLongClickListener, ObserverListener
{
    public CustomTypefaceableObserverButton(@NonNull Context context)
    {
        super(context);
        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    public CustomTypefaceableObserverButton(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        setCustomTypeface(context, attrs);
        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    public CustomTypefaceableObserverButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setCustomTypeface(context, attrs);
        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    public CustomTypefaceableObserverButton(@NonNull Context context, final String assetFontPath)
    {
        this(context);
        setCustomTypeface(context, assetFontPath);
        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    @Override
    public void setCustomTypeface(final Context context, final AttributeSet attrs)
    {
        CustomFontSetter.setCustomFont(this, context, attrs);
    }

    @Override
    public void setCustomTypeface(final Context context, final String assetFontPath)
    {
        CustomFontSetter.setCustomFont(this, assetFontPath, context);
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
