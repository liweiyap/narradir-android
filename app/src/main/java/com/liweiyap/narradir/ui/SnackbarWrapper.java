package com.liweiyap.narradir.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.liweiyap.narradir.R;
import com.liweiyap.narradir.util.Observer;
import com.liweiyap.narradir.util.SnackbarBuilderFlag;

import java.util.EnumSet;

public class SnackbarWrapper
{
    public SnackbarWrapper(final @NonNull Context context)
    {
        mMaxInlineActionWidth = context.getResources().getDimensionPixelSize(R.dimen.narradir_design_snackbar_action_inline_max_width);
    }

    public void show(final @NonNull View view, final @NonNull String message, final int duration, final @Nullable String actionMessage, final @Nullable Observer actionCallback, final @NonNull EnumSet<SnackbarBuilderFlag> flags)
    {
        if (!isValidSnackbarDuration(duration))
        {
            throw new RuntimeException(
                "SnackbarWrapper::show()" +
                    ": Programming Error. Value for duration (" + duration + ") not recognised.");
        }

        dismissOldSnackbar();

        if (!isValidSnackbar(view, message, flags))
        {
            return;
        }

        showNewSnackbar(view, message, duration, actionMessage, actionCallback, flags);
    }

    private void showNewSnackbar(final @NonNull View view, final @NonNull String message, final int duration, final @Nullable String actionMessage, final @Nullable Observer actionCallback, final @NonNull EnumSet<SnackbarBuilderFlag> flags)
    {
        mSnackbar = Snackbar.make(view, message, duration);

        if (flags.contains(SnackbarBuilderFlag.SHOW_ABOVE_XY))
        {
            mSnackbar.setAnchorView(view);
        }

        if (flags.contains(SnackbarBuilderFlag.ACTION_DISMISSABLE))
        {
            mSnackbar.setMaxInlineActionWidth(mMaxInlineActionWidth);
            if (actionMessage != null)
            {
                mSnackbar.setAction(actionMessage, v -> {
                    dismissOldSnackbar();
                    if (actionCallback != null)
                    {
                        actionCallback.update();
                    }
                });
            }
        }

        mSnackbar.show();
    }

    private void dismissOldSnackbar()
    {
        if (mSnackbar == null)
        {
            return;
        }

        mSnackbar.dismiss();
    }

    private boolean isValidSnackbarDuration(final int duration)
    {
        return ((duration == BaseTransientBottomBar.LENGTH_SHORT) || (duration == BaseTransientBottomBar.LENGTH_LONG) || (duration == BaseTransientBottomBar.LENGTH_INDEFINITE));
    }

    private boolean isValidSnackbar(final View view, final String message, final EnumSet<SnackbarBuilderFlag> flags)
    {
        return ((view != null) && (!TextUtils.isEmpty(message)) && (flags != null));
    }

    private Snackbar mSnackbar;
    private final int mMaxInlineActionWidth;
}