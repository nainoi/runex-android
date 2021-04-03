package com.think.runex.util.extension

import android.os.Build
import android.text.Html
import android.widget.TextView

fun TextView.setTextHtmlFormat(text: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT))
    } else {
        setText(Html.fromHtml(text))
    }
}