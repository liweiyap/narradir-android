package com.liweiyap.narradir.ui.fonts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Checkable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liweiyap.narradir.R;

/**
 * See: https://stackoverflow.com/a/34074176/12367873
 */
public class CustomTypefaceableCheckableObserverButton
    extends CustomTypefaceableObserverButton
    implements Checkable
{
    public CustomTypefaceableCheckableObserverButton(@NonNull Context context)
    {
        super(context);
        init(context, null);
    }

    public CustomTypefaceableCheckableObserverButton(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomTypefaceableCheckableObserverButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CustomTypefaceableCheckableObserverButton(@NonNull Context context, final String assetFontPath)
    {
        super(context, assetFontPath);
        init(context, null);
    }

    /**
     * Toggles background based on the selector that we set in XML
     */
    @Override
    public int[] onCreateDrawableState(int extraSpace)
    {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked())
        {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public void setChecked(boolean checked)
    {
        setAlpha(checked ? 1.f : 0.5f);

        if (mIsChecked == checked)
        {
            return;
        }

        mIsChecked = checked;
        refreshDrawableState();
    }

    @Override
    public void toggle()
    {
        setChecked(!mIsChecked);
    }

    @Override
    public boolean isChecked()
    {
        return mIsChecked;
    }

    private void init(final Context context, final AttributeSet attrs)
    {
        if (attrs != null)
        {
            @SuppressLint("CustomViewStyleable") TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckableDrawableIds);
            mIsChecked = typedArray.getBoolean(R.styleable.CheckableDrawableIds_defaultCheckedState, false);
            typedArray.recycle();
        }

        setChecked(mIsChecked);
    }

    private boolean mIsChecked = false;

    private static final int[] CHECKED_STATE_SET = {
        android.R.attr.state_checked
    };
}