package com.liweiyap.narradir.util.fonts;

import android.content.Context;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;

import org.jetbrains.annotations.Nullable;

/**
 * Caches the custom Typefaces in the assets directory to:
 *  - avoid memory leaks on older handsets (https://issuetracker.google.com/issues/36919609)
 *  - increase execution speed at run time, since it's not super fast to read from assets all the time.
 */
public final class FontCache
{
    private FontCache(){}

    private static final ArrayMap<String, Typeface> fontCache = new ArrayMap<>();

    public static @Nullable Typeface get(final String assetFontPath, final @NonNull Context context)
    {
        if (assetFontPath == null)
        {
            return null;
        }

        Typeface typeface = fontCache.get(assetFontPath);
        
        if (typeface == null)
        {
            try
            {
                typeface = Typeface.createFromAsset(context.getAssets(), assetFontPath);
            }
            catch (Exception e)
            {
                return null;
            }
            
            fontCache.put(assetFontPath, typeface);
        }
        
        return typeface;
    }
}