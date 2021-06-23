package com.liweiyap.narradir.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liweiyap.narradir.R;

public class CheckableObserverImageButton
    extends ObserverImageButton
    implements Checkable
{
    public CheckableObserverImageButton(@NonNull Context context)
    {
        super(context);
        init();
    }

    public CheckableObserverImageButton(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        setDrawableIds(context, attrs);
        init();
    }

    public CheckableObserverImageButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setDrawableIds(context, attrs);
        init();
    }

    public CheckableObserverImageButton(@NonNull Context context, final int checkedDrawableId, final int uncheckedDrawableId)
    {
        super(context);
        setCheckedDrawableId(checkedDrawableId);
        setUncheckedDrawableId(uncheckedDrawableId);
        init();
    }

    @Override
    public void setDrawableIds(final Context context, final AttributeSet attrs)
    {
        if (attrs == null)
        {
            return;
        }

        @SuppressLint("CustomViewStyleable") TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckableDrawableIds);
        int checkedDrawableId = typedArray.getResourceId(R.styleable.CheckableDrawableIds_checkedDrawableId, ID_NULL);
        int uncheckedDrawableId = typedArray.getResourceId(R.styleable.CheckableDrawableIds_uncheckedDrawableId, ID_NULL);
        mIsChecked = typedArray.getBoolean(R.styleable.CheckableDrawableIds_defaultCheckedState, false);
        typedArray.recycle();

        setCheckedDrawableId(checkedDrawableId);
        setUncheckedDrawableId(uncheckedDrawableId);
    }

    @Override
    public void setCheckedDrawableId(final int drawableId)
    {
        mCheckedDrawableId = drawableId;
    }

    @Override
    public void setUncheckedDrawableId(final int drawableId)
    {
        mUncheckedDrawableId = drawableId;
    }

    @Override
    public void check()
    {
        mIsChecked = true;
        setAlpha(1.f);
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
    }

    private int mCheckedDrawableId;
    private int mUncheckedDrawableId;
    private final int ID_NULL = 0;
    private boolean mIsChecked = false;
}