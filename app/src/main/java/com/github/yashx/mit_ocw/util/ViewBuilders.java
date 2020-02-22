package com.github.yashx.mit_ocw.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.github.yashx.mit_ocw.R;

public class ViewBuilders {
    private static Context context;

    public static TextView BigHeadingTextView(Context c, String s) {
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

    public static TextView SmallHeadingTextView(Context c, String s) {
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
        return textView;

    }

    public static LinearLayout SmallHeadingTextViewWithDecorator(Context c, String s) {
        return SmallHeadingTextViewWithDecorator(c, s, true);
    }

    public static LinearLayout SmallHeadingTextViewWithDecorator(Context c, String s, Boolean textFirst) {
        context = c;
        TextView textView = SmallHeadingTextView(c, s);

        CardView cardView = new CardView(c);
        cardView.setRadius(getDps(16));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, getDps(5), 1);
        layoutParams.setMargins(0, getDps(4), 0, 0);
        cardView.setLayoutParams(layoutParams);
        cardView.setCardBackgroundColor(c.getResources().getColor(R.color.colorAfricanViolet));

        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        LinearLayout linearLayout = new LinearLayout(c);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        if (textFirst) {
            linearLayout.addView(textView);
            linearLayout.addView(cardView);
        } else {
            linearLayout.addView(cardView);
            linearLayout.addView(textView);
        }


        return linearLayout;
    }

    public static TextView SmallBodyEndTextView(Context c, String s) {
        context = c;

        TextView textView = new TextView(c);
        textView.setPadding(getDps(16f), getDps(4f), getDps(16f), getDps(8f));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setText(s);

        return textView;
    }

    public static TextView SmallBodyMidTextView(Context c, String s) {
        context = c;

        TextView textView = new TextView(c);
        textView.setPadding(getDps(16f), getDps(4f), getDps(16f), getDps(4f));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setText(s);

        return textView;
    }

    static int getDps(float f) {
        float s = context.getResources().getDisplayMetrics().density;
        return (int) (f * s + 0.5f);
    }
}
