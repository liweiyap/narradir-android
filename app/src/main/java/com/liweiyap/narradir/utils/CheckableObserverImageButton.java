package com.liweiyap.narradir.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.liweiyap.narradir.R;

import java.net.IDN;

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
        setDrawables(context, attrs);
        init();
    }

    public CheckableObserverImageButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setDrawables(context, attrs);
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
    public void setDrawables(final Context context, final AttributeSet attrs)
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
            setImageResource(mCheckedDrawableId);
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
            setImageResource(mUncheckedDrawableId);
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
    private final int IDNOTFOUND = -1;
    private boolean mIsChecked = false;
}
