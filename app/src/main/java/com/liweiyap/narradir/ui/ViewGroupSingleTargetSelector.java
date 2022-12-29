package com.liweiyap.narradir.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

public final class ViewGroupSingleTargetSelector
{
    private ViewGroupSingleTargetSelector(){}

    public static void addSingleTargetSelection(final ViewGroup viewGroup) throws RuntimeException
    {
        if (viewGroup == null)
        {
            return;
        }

        for (int childIdx = 0; childIdx < viewGroup.getChildCount(); ++childIdx)
        {
            View view = viewGroup.getChildAt(childIdx);

            if (!(view instanceof Checkable))
            {
                throw new RuntimeException(
                    "ViewGroupSingleTargetSelector::addSingleTargetSelection(): " +
                        "Programming error: View is not Checkable."
                );
            }

            if (!(view instanceof IObserverListener))
            {
                throw new RuntimeException(
                    "ViewGroupSingleTargetSelector::addSingleTargetSelection(): " +
                        "Programming error: View is not ObserverListener."
                );
            }

            int i = childIdx;
            ((IObserverListener) view).addOnClickObserver(() -> {
                ((Checkable) view).setChecked(true);
                for (int j = 0; j < viewGroup.getChildCount(); ++j)
                {
                    if (i != j)
                    {
                        View tmp = viewGroup.getChildAt(j);
                        ((Checkable) tmp).setChecked(false);
                    }
                }
            });
        }
    }
}