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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class DepartmentFeaturedCoursesFragment extends Fragment {

    private Context context;

    public static DepartmentFeaturedCoursesFragment newInstance(Document doc) {
        Bundle args = new Bundle();
        //storing document instead of url to not load page twice
        args.putString("html", doc.html());
        DepartmentFeaturedCoursesFragment fragment = new DepartmentFeaturedCoursesFragment();
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

        //getting Featured Courses
        ArrayList<String> urls = new ArrayList<>();
        eTs = doc.select("#carousel_ul .item");
        if (eTs != null) {
            for (Element e : eTs) {
                //getting absolute url (absUrl doesn't work as doc is loaded from html)
                String url = e.selectFirst("a").attr("href");
                if (!url.contains("https://"))
                    url = "https://ocw.mit.edu" + url;
                int l = url.lastIndexOf("index.htm");
                if (l != -1) {
                    url = url.substring(0,l) + "index.json";
                }
                urls.add(url);
            }
            linearLayout.addView(ViewBuilders.SmallBodyMidTextView(context,urls.toString()));
        }
    }

    //pixel to dp
    int getDps(float f) {
        float s = context.getResources().getDisplayMetrics().density;
        return (int) (f * s + 0.5f);
    }
}
