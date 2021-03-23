package com.liweiyap.narradir.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liweiyap.narradir.R;
import com.liweiyap.narradir.utils.fonts.CustomTypefaceableTextView;

import java.util.ArrayList;
import java.util.List;

public class SettingsLayout
    extends LinearLayout
    implements Checkable, View.OnClickListener, View.OnLongClickListener, ObserverListener
{
    public SettingsLayout(Context context)
    {
        super(context);
        init();
    }

    public SettingsLayout(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        setDrawableIds(context, attrs);
        init();
    }

    public SettingsLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setDrawableIds(context, attrs);
        init();
    }

    @Override
    public void setDrawableIds(Context context, AttributeSet attrs)
    {
        if (attrs == null)
        {
            return;
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckableDrawableIds);
        int checkedDrawableId = typedArray.getResourceId(R.styleable.CheckableDrawableIds_checkedDrawableId, IDNOTFOUND);
        int uncheckedDrawableId = typedArray.getResourceId(R.styleable.CheckableDrawableIds_uncheckedDrawableId, IDNOTFOUND);
        mIsChecked = typedArray.getBoolean(R.styleable.CheckableDrawableIds_defaultCheckedState, false);
        typedArray.recycle();

        setCheckedDrawableId(checkedDrawableId);
        setUncheckedDrawableId(uncheckedDrawableId);
    }

    @Override
    public void setCheckedDrawableId(int drawableId)
    {
        mCheckedDrawableId = drawableId;
    }

    @Override
    public void setUncheckedDrawableId(int drawableId)
    {
        mUncheckedDrawableId = drawableId;
    }

    @Override
    public void check()
    {
        mIsChecked = true;
        setAlpha(1f);
        try
        {
            setBackgroundResource(mCheckedDrawableId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void uncheck()
    {
        mIsChecked = false;
        setAlpha(0.5f);
        try
        {
            setBackgroundResource(mUncheckedDrawableId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void toggle()
    {
        if (mIsChecked)
        {
            uncheck();
        }
        else
        {
            check();
        }
    }

    @Override
    public boolean isChecked()
    {
        return mIsChecked;
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

    private void init()
    {
        if (mIsChecked)
        {
            check();
        }
        else
        {
            uncheck();
        }

        addOnClickObserver(this::toggle);

        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    /**
     * Assumes that SettingsLayout has been initialised in an XML file, and that the same XML file also
     * initialises a CustomTypefaceableTextView that is a child view of this object and that serves as the title
     *
     * @param key String that serves as the new title
     */
    public void setKey(String key)
    {
        CustomTypefaceableTextView keyTextView = findViewById(R.id.keyTextView);
        if (keyTextView != null)
        {
            keyTextView.setText(key);
        }
    }

    /**
     * Assumes that SettingsLayout has been initialised in an XML file, and that the same XML file also
     * initialises a CustomTypefaceableTextView that is a child view of this object and that serves as the subheading
     *
     * @param value String that serves as the new subheading
     */
    public void setValue(String value)
    {
        CustomTypefaceableTextView valueTextView = findViewById(R.id.valueTextView);
        if (valueTextView != null)
        {
            valueTextView.setText(value);
        }
    }

    private int mCheckedDrawableId;
    private int mUncheckedDrawableId;
    private final int IDNOTFOUND = -1;
    private boolean mIsChecked = false;

    private final List<Observer> mOnClickObservers = new ArrayList<>();
    private final List<Observer> mOnLongClickObservers = new ArrayList<>();
}