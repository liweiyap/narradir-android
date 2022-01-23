package com.liweiyap.narradir.ui.fonts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liweiyap.narradir.R;

public class StrokedCustomTypefaceableTextView
    extends CustomTypefaceableTextView
    implements Outlineable
{
    public StrokedCustomTypefaceableTextView(@NonNull Context context)
    {
        super(context);
    }

    public StrokedCustomTypefaceableTextView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        setStroke(context, attrs);
    }

    public StrokedCustomTypefaceableTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setStroke(context, attrs);
    }

    public StrokedCustomTypefaceableTextView(@NonNull Context context, final String assetFontPath)
    {
        super(context, assetFontPath);
    }

    public StrokedCustomTypefaceableTextView(@NonNull Context context, final @ColorInt int strokeColour, final float strokeWidth)
    {
        super(context);
        setStrokeColor(strokeColour);
        setStrokeWidth(strokeWidth);
    }

    public StrokedCustomTypefaceableTextView(@NonNull Context context, final String assetFontPath, final @ColorInt int strokeColour, final float strokeWidth)
    {
        super(context, assetFontPath);
        setStrokeColor(strokeColour);
        setStrokeWidth(strokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (mStrokeWidth <= 0)
        {
            super.onDraw(canvas);
            return;
        }

        mIsDrawing = true;

        Paint p = getPaint();
        p.setStyle(Paint.Style.FILL);
        super.onDraw(canvas);

        int currentTextColor = getCurrentTextColor();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(mStrokeWidth);
        setTextColor(mStrokeColor);
        super.onDraw(canvas);

        setTextColor(currentTextColor);
        mIsDrawing = false;
    }

    @Override
    public void invalidate()
    {
        // Ignore invalidate() calls when isDrawing == true
        // (These calls are triggered by calls to setTextColor(color) calls, which would create an infinite loop)
        if (mIsDrawing)
        {
            return;
        }

        super.invalidate();
    }

    @Override
    public void setStroke(final Context context, final AttributeSet attrs)
    {
        if (attrs == null)
        {
            return;
        }

        @SuppressLint("CustomViewStyleable") TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StrokedTextAttrs);
        mStrokeColor = typedArray.getColor(R.styleable.StrokedTextAttrs_textStrokeColor, mStrokeColor);
        mStrokeWidth = typedArray.getFloat(R.styleable.StrokedTextAttrs_textStrokeWidth, mStrokeWidth);
        typedArray.recycle();

        setStrokeColor(mStrokeColor);
        setStrokeWidth(mStrokeWidth);
    }

    @Override
    public void setStrokeColor(final @ColorInt int color)
    {
        mStrokeColor = color;
    }

    @Override
    public void setStrokeWidth(final float width)
    {
        // convert values specified in dp in XML layout to px,
        // otherwise stroke width would appear different on different screens
        mStrokeWidth = dpToPx(getContext(), width);
    }

    @Override
    public void setStrokeWidth(final int unit, final float width)
    {
        mStrokeWidth = TypedValue.applyDimension(unit, width, getContext().getResources().getDisplayMetrics());
    }

    /**
     * Convenience method to convert density independent pixel(dp) value
     * into device display specific pixel value.
     *
     * @param context Context to access device specific display metrics
     * @param dp density independent pixel value
     * @return device specific pixel value.
     */
    private int dpToPx(final @NonNull Context context, final float dp)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    // should be safe to be stored as an int; it represents a packed color int, AARRGGBB. If applied to an int array, every element in the array represents a color integer.
    // Hence, it's not a resource ID (R.color.); it's already been converted from said resource ID. We even use TypedArray::getColor() instead of TypedArray::getInt()
    private @ColorInt int mStrokeColor = getCurrentTextColor();
    private float mStrokeWidth = 0;
    private boolean mIsDrawing;
}