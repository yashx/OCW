package com.github.yashx.mit_ocw.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.util.SpannableStringMaker;
import com.github.yashx.mit_ocw.util.ViewBuilders;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class HtmlRendererCourseFragment extends Fragment {
    private LinearLayout linearLayout;
    private Context context;

    public static HtmlRendererCourseFragment newInstance(String url) {
        //stores the url of page to be loaded to be retrieved later
        Bundle args = new Bundle();
        args.putString("url", url);
        HtmlRendererCourseFragment fragment = new HtmlRendererCourseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_htmlrenderercourse, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayout = view.findViewById(R.id.linearLayoutHtmlRendererCourse);

        //url is fetched and async job is started
        String url = getArguments().getString("url");
        new HtmlAsyncTask().execute(url);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    class HtmlAsyncTask extends AsyncTask<String, String, Document> {
        @Override
        protected Document doInBackground(String... strings) {
            //Uses Jsoup to fetch web page from url
            Document doc = null;
            try {
                if (!strings[0].endsWith("/"))
                    strings[0] += "/";
                doc = Jsoup.connect(strings[0]).get();
            } catch (Exception e) {
                Log.e("TAG", "doInBackground: ", e);
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            Element eT;
            Elements eTs;

            eT = document.selectFirst("#course_nav > ul > li.selected");
            if (eT.select(".tlp_links") != null) {
                //getting sub links and setting up chips
                eTs = document.select("ul.selected li");
                if (eTs != null) {
                    ChipGroup cp = new ChipGroup(context);
                    cp.setPadding(getDps(16f), getDps(8f), getDps(16f), getDps(0f));
                    for (Element e : eTs) {
                        //getting absolute url
                        String url = "https://ocw.mit.edu/" + e.selectFirst("a").attr("href");
                        System.out.println(url);
                        String s = e.text().trim();
                        Chip chip = new Chip(context);
                        chip.setText(s);
                        chip.setTag(url);
                        chip.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse((String) v.getTag()));
                                startActivity(i);
                            }
                        });
                        cp.addView(chip);
                    }
                    linearLayout.addView(cp);
                }
            }

            //web page is received and processed
            eT = document.selectFirst("body main");
            if (eT != null) {
                //removes all images from html and replacing them with their alt text
                eTs = eT.select("img");
                if (eTs != null) {
                    for (Element el : eTs) {
                        if (el.attr("alt") != null)
                            if (el.attr("alt").contains("screen reader"))
                                el.remove();
                            else
                                el.replaceWith(new TextNode(el.attr("alt")));
                    }
                }

                //removes all scripts from html
                eTs = eT.select("script");
                if (eTs != null)
                    for (Element el : eTs)
                        el.remove();

                //removes all p style attributes from html
                eTs = eT.select("p[style]");
                if (eTs != null)
                    for (Element el : eTs)
                        el.removeAttr("style");

                //flattening html
                while (eT.selectFirst("div:not(.help)") != null) {
                    Element el = eT.selectFirst("div:not(.help)");
                    el.after(el.html());
                    el.remove();
                }

                //replaces all relative links with absolute links to open them easily
                eTs = eT.select("a");
                if (eTs != null) {
                    for (Element el : eTs) {
                        if (el.attr("href") != null)
                            el.attr("href", el.absUrl("href"));
                    }
                }

                //going through all necessary elements in html and rendering them appropriately
                eTs = eT.select(">:not(.help)");
                if (eTs != null) {
                    for (Element e : eTs) {
                        //Making headings bold
                        if (e.is("h1, h2, h3, h4, h5,h6")) {
                            linearLayout.addView(ViewBuilders.SmallHeadingTextView(context, e.text()));
                        }
                        //Making tables
                        else if (e.is(".maintabletemplate , table")) {
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
                            linearLayout.addView(s);
                        } else {
                            //converts html to spannable string that has links and no excess whitespace
                            SpannableStringBuilder formattedHtml = SpannableStringMaker.maker(e.outerHtml());
                            TextView t = ViewBuilders.SmallBodyMidTextView(context, "");
                            t.setText(formattedHtml);
                            //making textView clickable
                            t.setLinksClickable(true);
                            t.setMovementMethod(LinkMovementMethod.getInstance());
                            linearLayout.addView(t);
                        }
                    }
                }
            }
        }
    }

    int getDps(float f) {
        float s = getContext().getResources().getDisplayMetrics().density;
        return (int) (f * s + 0.5f);
    }
}