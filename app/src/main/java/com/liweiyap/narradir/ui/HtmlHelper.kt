package com.liweiyap.narradir.ui

import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView

object HtmlHelper {
    @JvmStatic
    fun setText(htmlTextView: TextView?, htmlString: String?) {
        setText(htmlTextView, htmlString, Html.FROM_HTML_MODE_COMPACT)
    }

    @JvmStatic
    fun setText(htmlTextView: TextView?, htmlString: String?, htmlFlags: Int) {
        htmlTextView?.let {
            it.text = Html.fromHtml(htmlString, htmlFlags)

            if (it.linksClickable) {
                it.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }
}