package com.liweiyap.narradir.util.fonts

import android.content.Context
import android.graphics.Typeface

import androidx.collection.ArrayMap

import kotlin.Exception

/**
 * Caches the custom Typefaces in the assets directory to:
 * - avoid memory leaks on older handsets (https://issuetracker.google.com/issues/36919609)
 * - increase execution speed at run time, since it's not super fast to read from assets all the time.
 */
object FontCache {
    private val sFontCache = ArrayMap<String, Typeface>()

    @JvmStatic
    fun get(assetFontPath: String?, context: Context): Typeface? {
        if (assetFontPath == null) {
            return null
        }

        synchronized(sFontCache) {
            var typeface: Typeface? = sFontCache[assetFontPath]

            if (typeface == null) {
                try {
                    typeface = Typeface.createFromAsset(context.assets, assetFontPath)
                } catch (e: Exception) {
                    return null
                }

                sFontCache[assetFontPath] = typeface
            }

            return typeface
        }
    }
}