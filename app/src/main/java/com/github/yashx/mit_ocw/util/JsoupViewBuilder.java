package com.github.yashx.mit_ocw.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class JsoupViewBuilder {
    public static ArrayList<View> elementsBuilder(Elements eTs, Context context) {
        ArrayList<View> v = new ArrayList<>();
        for (Element e : eTs) {
            //for all heading build small heading
            if (e.is("h1, h2, h3, h4, h5,h6")) {
                v.add(smallHeadingWithDecoratorFromElementBuilder(e, context));
            }
            //for these elements building table
            else if (e.is(".maintabletemplate , table")) {
                v.add(tableFromElementBuilder(e, context));
            }
            //for anything else
            else v.add(bodyTextViewFromElementBuilder(e, context));
        }
        return v;
    }

    //builds small Heading
    public static View smallHeadingFromElementBuilder(Element e, Context context) {
        return ViewBuilders.SmallHeadingTextView(context, e.text().trim());
    }

    public static View smallHeadingWithDecoratorFromElementBuilder(Element e, Context context) {
        return ViewBuilders.SmallHeadingTextViewWithDecorator(context, e.text().trim());
    }

    public static View smallHeadingWithDecoratorFromElementBuilder(Element e, Context context,Boolean textFirst) {
        return ViewBuilders.SmallHeadingTextViewWithDecorator(context, e.text().trim(),textFirst);
    }


    //builds table
    public static View tableFromElementBuilder(Element e, Context context) {
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        TableLayout tableLayout = new TableLayout(context);
        tableLayout.setLayoutParams(new HorizontalScrollView.LayoutParams(HorizontalScrollView.LayoutParams.WRAP_CONTENT, HorizontalScrollView.LayoutParams.WRAP_CONTENT));

        TableRow tableRow = new TableRow(context);
        tableRow.setLayoutParams(tableParams);

        TextView t;
        for (Element el : e.select("th")) {
            t = ViewBuilders.SmallHeadingTextView(context, el.text());
            t.setLayoutParams(rowParams);
            t.setBackgroundColor(Color.parseColor("#212121"));
            tableRow.addView(t);
        }
        tableLayout.addView(tableRow);

        for (Element tr : e.select("tbody tr")) {
            tableRow = new TableRow(context);
            tableRow.setLayoutParams(tableParams);
            for (Element td : tr.select("td")) {
                //Explained below
                t = ViewBuilders.SmallBodyMidTextView(context, "");
                SpannableStringBuilder formattedHtml = SpannableStringMaker.maker(td.html());
                t.setLayoutParams(rowParams);
                t.setMaxWidth(Resources.getSystem().getDisplayMetrics().widthPixels);
                t.setText(formattedHtml);
                t.setLinksClickable(true);
                t.setMovementMethod(LinkMovementMethod.getInstance());
                tableRow.addView(t);
            }
            tableLayout.addView(tableRow);
        }
        HorizontalScrollView s = new HorizontalScrollView(context);
        s.addView(tableLayout);
        return s;
    }

    //converts html to spannable string that has links and no excess whitespace
    public static View bodyTextViewFromElementBuilder(Element e, Context context) {
        SpannableStringBuilder formattedHtml = SpannableStringMaker.maker(e.outerHtml());
        TextView t = ViewBuilders.SmallBodyMidTextView(context, "");
        t.setText(formattedHtml);
        //making textView clickable
        t.setLinksClickable(true);
        t.setMovementMethod(LinkMovementMethod.getInstance());
        return t;
    }
}
