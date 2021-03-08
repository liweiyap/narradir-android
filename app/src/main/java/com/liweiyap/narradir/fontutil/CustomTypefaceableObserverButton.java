package com.liweiyap.narradir.fontutil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liweiyap.narradir.Observer;

import java.util.ArrayList;
import java.util.List;

public class CustomTypefaceableObserverButton
    extends androidx.appcompat.widget.AppCompatButton
    implements CustomTypefaceable, View.OnClickListener
{
    public CustomTypefaceableObserverButton(@NonNull Context context)
    {
        super(context);
        setOnClickListener(this);
    }

    public CustomTypefaceableObserverButton(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        setCustomTypeface(context, attrs);
        setOnClickListener(this);
    }

    public CustomTypefaceableObserverButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setCustomTypeface(context, attrs);
        setOnClickListener(this);
    }

    public CustomTypefaceableObserverButton(@NonNull Context context, final String assetFontPath)
    {
        this(context);
        setCustomTypeface(context, assetFontPath);
        setOnClickListener(this);
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
        notifyObservers();
    }

    public void addObserver(Observer observer)
    {
        mObservers.add(observer);
    }

    private void notifyObservers()
    {
        mObservers.forEach(Observer::update);
    }

    private final List<Observer> mObservers = new ArrayList<>();
}
