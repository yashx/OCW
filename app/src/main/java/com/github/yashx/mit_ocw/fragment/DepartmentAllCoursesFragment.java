package com.github.yashx.mit_ocw.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.adapter.CourseListItemNoImageRecyclerAdapter;
import com.github.yashx.mit_ocw.model.CourseListItem;
import com.github.yashx.mit_ocw.viewmodel.CourseAndDepartmentViewModel;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class DepartmentAllCoursesFragment extends Fragment {

    private Context context;
    private LinearLayout linearLayout;


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
        final ProgressBar progressBar = view.findViewById(R.id.progressBarCommonFragment);


        CourseAndDepartmentViewModel courseAndDepartmentViewModel =
                new ViewModelProvider(requireActivity()).get(CourseAndDepartmentViewModel.class);
        courseAndDepartmentViewModel.getDoc().observe(this, new Observer<Document>() {
            @Override
            public void onChanged(Document doc) {
                progressBar.setVisibility(View.GONE);
                ArrayList<CourseListItem> courseListItemArrayList = null;
                //getting All Courses
                Elements eTs = doc.select("#global_inner > div.courseListDiv > ul > li:not(.courseListHeaderRow)");
                if (eTs != null) {
                    courseListItemArrayList = new ArrayList<>(eTs.size());
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
                        boolean hasVideos = (e.attr("data-other_video")
                                .equalsIgnoreCase("true")
                                || e.attr("data-complete_video")
                                .equalsIgnoreCase("true"));

                        courseListItem = new CourseListItem(
                                e.attr("data-title"),
                                e.attr("data-courseno"),
                                e.attr("data-semester"),
                                hasVideos,
                                href
                        );

                        courseListItemArrayList.add(courseListItem);
                    }

                }

                //setting up recyclerView
                RecyclerView recyclerView = new RecyclerView(context);
                recyclerView.setLayoutParams(new LinearLayout
                        .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT));
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(new CourseListItemNoImageRecyclerAdapter(courseListItemArrayList));
                linearLayout.addView(recyclerView);
            }
        });

    }
}