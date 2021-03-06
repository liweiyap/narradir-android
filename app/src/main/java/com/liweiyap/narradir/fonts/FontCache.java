package com.liweiyap.narradir.fonts;

import android.content.Context;
import android.graphics.Typeface;

import androidx.collection.ArrayMap;

/**
 * Caches the custom Typefaces in the assets directory to:
 *  - avoid memory leaks on older handsets (https://issuetracker.google.com/issues/36919609)
 *  - increase execution speed at run time, since it's not super fast to read from assets all the time.
 */
public class FontCache
{
    private static final ArrayMap<String, Typeface> fontCache = new ArrayMap<>();

    public static Typeface get(String assetFontPath, Context context)
    {
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
