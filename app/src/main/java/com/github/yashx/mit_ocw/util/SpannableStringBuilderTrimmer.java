package com.github.yashx.mit_ocw.util;

import android.text.SpannableStringBuilder;

import static androidx.core.util.Preconditions.checkNotNull;

public class SpannableStringBuilderTrimmer {
    public static SpannableStringBuilder trimmer(SpannableStringBuilder spannable){
        checkNotNull(spannable);
        int trimStart = 0;
        int trimEnd = 0;

        String text = spannable.toString();

        while (text.length() > 0 && (text.startsWith("\n")||text.startsWith(" "))) {
            text = text.substring(1);
            trimStart += 1;
        }

        while (text.length() > 0 && (text.endsWith("\n")||text.endsWith(" "))) {
            text = text.substring(0, text.length() - 1);
            trimEnd += 1;
        }

        return spannable.delete(0, trimStart).delete(spannable.length() - trimEnd, spannable.length());
    }
}
