package com.liweiyap.narradir.ui

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

object TextViewSpanHelper {
    fun setClickableSpan(textView: TextView?, text: CharSequence?, spanStart: Int, spanEnd: Int, onClickCallback: () -> Unit) {
        if ( (textView == null ) || (TextUtils.isEmpty(text)) ) {
            return
        }

        val spannableString = SpannableString(text)
        spannableString.setSpan(getClickableSpan(onClickCallback), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = Color.TRANSPARENT
    }

    private fun getClickableSpan(onClickCallback: () -> Unit): ClickableSpan {
        return object: ClickableSpan() {
            override fun onClick(textView: View) {
                onClickCallback()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }
    }
}