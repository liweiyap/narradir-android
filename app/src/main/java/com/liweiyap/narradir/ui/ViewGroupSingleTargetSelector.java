package com.liweiyap.narradir.ui;

import android.view.View;
import android.view.ViewGroup;

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

            if (!(view instanceof ObserverListener))
            {
                throw new RuntimeException(
                    "ViewGroupSingleTargetSelector::addSingleTargetSelection(): " +
                        "Programming error: View is not ObserverListener."
                );
            }

            int i = childIdx;
            ((ObserverListener) view).addOnClickObserver(() -> {
                ((Checkable) view).check();
                for (int j = 0; j < viewGroup.getChildCount(); ++j)
                {
                    if (i != j)
                    {
                        View tmp = viewGroup.getChildAt(j);
                        ((Checkable) tmp).uncheck();
                    }
                }
            });
        }
    }
}