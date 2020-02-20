package com.github.yashx.mit_ocw.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.github.yashx.mit_ocw.R;

public class ViewBuilders {
    private static Context context;

    public static TextView BigHeadingTextView(Context c,String s){
        context = c;

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        TypedArray arr =
                context.obtainStyledAttributes(typedValue.data, new int[]{
                        android.R.attr.textColorPrimary});
        int primaryColor = arr.getColor(0, -1);
        arr.recycle();

        TextView textView = new TextView(c);
        textView.setTextColor(primaryColor);
        textView.setShadowLayer(getDps(12.0f), getDps(0.5f), 0, Color.BLACK);
        textView.setPadding(getDps(16f), getDps(16f), getDps(16f), getDps(16f));
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        textView.setText(s);
        context = null;
        return textView;
    }
    public static TextView SmallHeadingTextView(Context c,String s){
        context = c;

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        TypedArray arr =
                context.obtainStyledAttributes(typedValue.data, new int[]{
                        android.R.attr.textColorPrimary});
        int primaryColor = arr.getColor(0, -1);
        arr.recycle();

        TextView textView = new TextView(c);
        textView.setTextColor(primaryColor);
        textView.setPadding(getDps(16f), getDps(8f), getDps(16f), getDps(4f));
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        textView.setText(s);
        context = null;
        return textView;
    }
    public static TextView SmallBodyEndTextView(Context c, String s){
        context = c;

        TextView textView = new TextView(c);
        textView.setPadding(getDps(16f), getDps(4f), getDps(16f), getDps(8f));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setText(s);
        context = null;
        return textView;
    }

    public static TextView SmallBodyMidTextView(Context c, String s){
        context = c;

        TextView textView = new TextView(c);
        textView.setPadding(getDps(16f), getDps(4f), getDps(16f), getDps(4f));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setText(s);
        context = null;
        return textView;
    }

    static int getDps(float f) {
        float s = context.getResources().getDisplayMetrics().density;
        return (int) (f * s + 0.5f);
    }
}
