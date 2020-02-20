package com.github.yashx.mit_ocw.util;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.URLSpan;
import android.text.util.Linkify;

import androidx.core.text.HtmlCompat;

public class SpannableStringMaker {
    public static SpannableStringBuilder maker(String html) {
        SpannableStringBuilder formattedHtml =
                (SpannableStringBuilder) HtmlCompat.fromHtml(html,
                        HtmlCompat.FROM_HTML_MODE_COMPACT);
        formattedHtml = SpannableStringBuilderTrimmer.trimmer(formattedHtml);
        URLSpan[] currentSpans = formattedHtml.getSpans(0, formattedHtml.length(), URLSpan.class);

        SpannableString buffer = new SpannableString(formattedHtml);
        Linkify.addLinks(buffer, Linkify.WEB_URLS);

        for (URLSpan span : currentSpans) {
            int end = formattedHtml.getSpanEnd(span);
            int start = formattedHtml.getSpanStart(span);
            buffer.setSpan(span, start, end, 0);
        }
        return formattedHtml;
    }
}
