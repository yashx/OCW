package com.github.yashx.mit_ocw.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.activity.HtmlRendererActivity;
import com.github.yashx.mit_ocw.util.JsoupElementCleaner;
import com.github.yashx.mit_ocw.util.JsoupViewBuilder;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class HtmlRendererCourseFragment extends Fragment {
    private LinearLayout linearLayout;
    private Context context;
    ProgressBar progressBar;

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
        return inflater.inflate(R.layout.fragment_common, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayout = view.findViewById(R.id.linearLayoutCommonFragment);
        progressBar = view.findViewById(R.id.progressBarCommonFragment);

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

            progressBar.setVisibility(View.GONE);

            Element eT;
            Elements eTs;

            eT = document.selectFirst("#course_nav > ul > li.selected");
            //checking if selected link has sub links
            if (eT != null && eT.select(".tlp_links") != null) {
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
                        chip.setChipBackgroundColorResource(R.color.colorRed);
                        chip.setTextColor(Color.WHITE);
                        chip.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse((String) v.getTag()));
//                                startActivity(i);
                                Intent i = new Intent(context, HtmlRendererActivity.class);
                                i.putExtra("urlExtra", (String) v.getTag());
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
                //cleans the html to be build easily
                eT = JsoupElementCleaner.elementCleaner(eT);
                //going through all necessary elements in html and building them appropriately
                eTs = eT.select(">:not(.help)");
                ArrayList<View> vs = JsoupViewBuilder.elementsBuilder(eTs, context);
                for (View v : vs)
                    linearLayout.addView(v);
            }
        }
    }

    int getDps(float f) {
        float s = context.getResources().getDisplayMetrics().density;
        return (int) (f * s + 0.5f);
    }
}