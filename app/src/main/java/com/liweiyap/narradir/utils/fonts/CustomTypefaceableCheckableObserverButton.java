package com.liweiyap.narradir.utils.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.liweiyap.narradir.R;
import com.liweiyap.narradir.utils.Checkable;

public class CustomTypefaceableCheckableObserverButton
    extends CustomTypefaceableObserverButton
    implements Checkable
{
    public CustomTypefaceableCheckableObserverButton(@NonNull Context context)
    {
        super(context);
        init();
    }

    public CustomTypefaceableCheckableObserverButton(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        setDrawables(context, attrs);
        init();
    }

    public CustomTypefaceableCheckableObserverButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setDrawables(context, attrs);
        init();
    }

    public CustomTypefaceableCheckableObserverButton(@NonNull Context context, final String assetFontPath)
    {
        super(context, assetFontPath);
        init();
    }

    public CustomTypefaceableCheckableObserverButton(@NonNull Context context, final int checkedDrawableId, final int uncheckedDrawableId)
    {
        super(context);
        setCheckedDrawable(context, checkedDrawableId);
        setUncheckedDrawable(context, uncheckedDrawableId);
        init();
    }

    public CustomTypefaceableCheckableObserverButton(@NonNull Context context, final String assetFontPath, final int checkedDrawableId, final int uncheckedDrawableId)
    {
        super(context, assetFontPath);
        setCheckedDrawable(context, checkedDrawableId);
        setUncheckedDrawable(context, uncheckedDrawableId);
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
        int checkedDrawableId = typedArray.getResourceId(R.styleable.CheckableDrawableIds_checkedDrawableId, -1);
        int uncheckedDrawableId = typedArray.getResourceId(R.styleable.CheckableDrawableIds_uncheckedDrawableId, -1);
        mIsChecked = typedArray.getBoolean(R.styleable.CheckableDrawableIds_defaultCheckedState, false);
        typedArray.recycle();
        setCheckedDrawable(context, checkedDrawableId);
        setUncheckedDrawable(context, uncheckedDrawableId);
    }

    @Override
    public void setCheckedDrawable(final Context context, final int drawableId)
    {
        try
        {
            mCheckedDrawable = ContextCompat.getDrawable(context, drawableId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void setUncheckedDrawable(final Context context, final int drawableId)
    {
        try
        {
            mUncheckedDrawable = ContextCompat.getDrawable(context, drawableId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void check()
    {
        mIsChecked = true;
        setAlpha(1.f);
        if (mCheckedDrawable != null)
        {
            setBackgroundDrawable(mCheckedDrawable);
        }
    }

    @Override
    public void uncheck()
    {
        mIsChecked = false;
        setAlpha(0.5f);
        if (mUncheckedDrawable != null)
        {
            setBackgroundDrawable(mUncheckedDrawable);
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

    private Drawable mCheckedDrawable;
    private Drawable mUncheckedDrawable;
    private boolean mIsChecked = false;
}
