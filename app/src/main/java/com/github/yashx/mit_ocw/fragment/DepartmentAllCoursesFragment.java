package com.github.yashx.mit_ocw.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.adapter.AllCourseListItemRecyclerAdapter;
import com.github.yashx.mit_ocw.model.CourseListItem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class DepartmentAllCoursesFragment extends Fragment {

    private Context context;
    private LinearLayout linearLayout;

    public static DepartmentAllCoursesFragment newInstance(Document doc) {
        Bundle args = new Bundle();
        //storing document instead of url to not load page twice
        args.putString("html", doc.html());
        DepartmentAllCoursesFragment fragment = new DepartmentAllCoursesFragment();
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
        linearLayout = view.findViewById(R.id.linearLayoutCommonFragment);
        Elements eTs;
        String html = getArguments().getString("html");
        Document doc = Jsoup.parse(html);

        int i = 0;
        //getting All Courses
        eTs = doc.select("#global_inner > div.courseListDiv > ul > li:not(.courseListHeaderRow)");
        if (eTs != null) {
            ArrayList<CourseListItem> courseListItems = new ArrayList<>();
            for (Element e : eTs) {
                String href;
                final CourseListItem courseListItem;
                //getting absolute url (absUrl doesn't work as doc is loaded from html)
                String url = e.selectFirst("a").attr("href");

                if (!url.contains("https://"))
                    url = "https://ocw.mit.edu" + url;
                if (!url.endsWith("/"))
                    url += "/";
                href = url;

                courseListItem = new CourseListItem(
                        e.attr("data-title"),
                        e.attr("data-courseno"),
                        e.attr("data-semester"),
                        href
                );

                courseListItems.add(courseListItem);
             }
            RecyclerView recyclerView = new RecyclerView(context);
            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new AllCourseListItemRecyclerAdapter(courseListItems));
            linearLayout.addView(recyclerView);
        }
    }
}