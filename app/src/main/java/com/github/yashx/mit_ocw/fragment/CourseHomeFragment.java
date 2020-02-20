package com.github.yashx.mit_ocw.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.util.ViewBuilders;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class CourseHomeFragment extends Fragment {

    public Document doc;

    public static CourseHomeFragment newInstance(Document doc) {
        CourseHomeFragment fragment = new CourseHomeFragment();
        fragment.doc = doc;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coursehome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Element eT;
        Elements eTs;
        Context c = getContext();
        LinearLayout linearLayout = view.findViewById(R.id.LLfragCourseHome);
        eTs = doc.select("#related > div > ul > li");
        if (eTs != null) {
            ArrayList<String> arr = new ArrayList<>();
            for (Element e : eTs) {
                String s = e.text().trim();
                if(s.contains(">")){
                    s = s.substring(s.lastIndexOf(">")+1);
                }
                arr.add(s);
            }
            ChipGroup cp = new ChipGroup(c);
            cp.setPadding(getDps(16f), getDps(8f), getDps(16f), getDps(0f));
            for(String s:arr){
                Chip chip = new Chip(c);
                chip.setText(s);
                cp.addView(chip);
            }
            linearLayout.addView(cp);
        }
        eT = doc.selectFirst("#description > div > p");
        if (eT != null) {
            linearLayout.addView(ViewBuilders.SmallHeadingTextView(c, "Description"));
            linearLayout.addView(ViewBuilders.SmallBodyEndTextView(c, eT.text().trim()));
        }
        eTs = doc.select("p.ins");
        if (eTs != null) {
            linearLayout.addView(ViewBuilders.SmallHeadingTextView(c, eTs.size() != 1 ? "Instructors" : "Instructor"));
            StringBuilder sb = new StringBuilder();
            for (Element e : eTs) {
                sb.append(e.text().trim()).append("\n");
            }
            linearLayout.addView(ViewBuilders.SmallBodyEndTextView(c, sb.toString().trim()));
        }

        eT = doc.selectFirst("p[itemprop=\"startDate\"]");
        if (eT != null) {
            linearLayout.addView(ViewBuilders.SmallHeadingTextView(c, "As Taught In"));
            linearLayout.addView(ViewBuilders.SmallBodyEndTextView(c, eT.text().trim()));
        }

        eT = doc.selectFirst("p[itemprop=\"typicalAgeRange\"]");
        if (eT != null) {
            linearLayout.addView(ViewBuilders.SmallHeadingTextView(c, "Level"));
            linearLayout.addView(ViewBuilders.SmallBodyEndTextView(c, eT.text().trim()));
        }

    }
    int getDps(float f) {
        float s = getContext().getResources().getDisplayMetrics().density;
        return (int) (f * s + 0.5f);
    }
}
