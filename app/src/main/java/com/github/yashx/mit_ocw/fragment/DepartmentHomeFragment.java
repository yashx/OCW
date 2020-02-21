package com.github.yashx.mit_ocw.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.util.JsoupElementCleaner;
import com.github.yashx.mit_ocw.util.JsoupViewBuilder;
import com.github.yashx.mit_ocw.util.ViewBuilders;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class DepartmentHomeFragment extends Fragment {

    private Context context;

    public static DepartmentHomeFragment newInstance(Document doc) {
        Bundle args = new Bundle();
        //storing document instead of url to not load page twice
        args.putString("html", doc.html());
        DepartmentHomeFragment fragment = new DepartmentHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_common, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout linearLayout = view.findViewById(R.id.linearLayoutCommonFragment);
        Element eT;
        Elements eTs;
        String html = getArguments().getString("html");
        Document doc = Jsoup.parse(html);

        //getting side links and setting up chips
        eTs = doc.select("#parent-fieldname-bottom_text_1 > ul > li");
        if (eTs != null) {
            ChipGroup cp = new ChipGroup(context);
            cp.setPadding(getDps(16f), getDps(8f), getDps(16f), getDps(0f));
            for (Element e : eTs) {
                //getting absolute url (absUrl doesn't work as doc is loaded from html)
                String url = e.selectFirst("a").attr("href");
                if (!url.contains("https://"))
                    url = "https://ocw.mit.edu" + url;
                String s = e.text().trim();
                if (s.contains(">")) {
                    s = s.substring(s.lastIndexOf(">") + 1);
                }
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

        //Getting Description
        eTs = doc.select("main>p,main>.subhead");
        if (eTs != null) {
            eTs = JsoupElementCleaner.elementsCleaner(eTs);
            linearLayout.addView(ViewBuilders.SmallHeadingTextView(context, "Description"));
            ArrayList<View> vs = JsoupViewBuilder.elementsBuilder(eTs, context);
            for (View v : vs)
                linearLayout.addView(v);
        }
    }

    //pixel to dp
    int getDps(float f) {
        float s = context.getResources().getDisplayMetrics().density;
        return (int) (f * s + 0.5f);
    }
}