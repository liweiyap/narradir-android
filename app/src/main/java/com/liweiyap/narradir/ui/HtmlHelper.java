package com.liweiyap.narradir.ui;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public final class HtmlHelper
{
    private HtmlHelper(){}

    public static void setText(final TextView htmlTextView, final String htmlString)
    {
        setText(htmlTextView, htmlString, Html.FROM_HTML_MODE_COMPACT);
    }

    public static void setText(final TextView htmlTextView, final String htmlString, final int htmlFlags)
    {
        if (htmlTextView == null)
        {
            return;
        }

        htmlTextView.setText(Html.fromHtml(htmlString, htmlFlags));

        if (htmlTextView.getLinksClickable())
        {
            htmlTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}