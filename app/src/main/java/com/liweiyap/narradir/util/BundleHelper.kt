package com.liweiyap.narradir.util

import android.os.Build
import android.os.Bundle
import java.io.Serializable
import kotlin.jvm.java

object BundleHelper {
    inline fun <reified T : Serializable> getSerializable(args: Bundle, key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            args.getSerializable(key, T::class.java)
        }
        else {
            @Suppress("DEPRECATION")
            args.getSerializable(key) as? T
        }
    }
}