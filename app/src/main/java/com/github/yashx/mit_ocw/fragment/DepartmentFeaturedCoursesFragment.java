package com.github.yashx.mit_ocw.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.activity.ShowCourseActivity;
import com.github.yashx.mit_ocw.model.CourseListItem;
import com.github.yashx.mit_ocw.viewmodel.CourseAndDepartmentViewModel;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class DepartmentFeaturedCoursesFragment extends Fragment {

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
                //getting Featured Courses
                Elements eTs = doc.select("#carousel_ul .item");
                ArrayList<String> urlList = null;
                if (eTs != null) {
                    urlList = new ArrayList<>(eTs.size());
                    for (Element e : eTs) {
                        //getting absolute url (absUrl doesn't work as doc is loaded from html)
                        String url = e.selectFirst("a").attr("href");
                        if (!url.contains("https://"))
                            url = "https://ocw.mit.edu" + url;
                        if (!url.endsWith("/"))
                            url += "/";
                        int l = url.lastIndexOf("index.htm");
                        if (l != -1) {
                            url = url.substring(0, l) + "index.json";
                        }
                        urlList.add(url);
                    }
                }
                for (String url : urlList)
                    new JsonFetcherAsync().execute(url);
            }
        });

    }

    class JsonFetcherAsync extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... url) {
            //getting json for course
            String s = url[0];
            String json = "";
            try {
                json = (Jsoup.connect(s).ignoreContentType(true).execute().body());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);

            //object from model class
            final CourseListItem courseListItem;
            courseListItem = CourseListItem.fromCourseJson(json);

            //inflating listitem_course and adding it to fragment
            View v = LayoutInflater.from(context).inflate(R.layout.listitem_course, linearLayout, false);
            ((TextView) v.findViewById(R.id.titleTextViewCourseListItem)).setText(courseListItem.getTitle());
            ((TextView) v.findViewById(R.id.subTitleTextViewCourseListItem)).setText(courseListItem.getSubtitle());
            Picasso.get().load(courseListItem.getThumb()).into(((ImageView) v.findViewById(R.id.imageViewCourseListItem)));
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), ShowCourseActivity.class);
                    i.putExtra(v.getContext().getResources().getString(R.string.urlExtra), courseListItem.getHref());
                    v.getContext().startActivity(i);
                }
            });
            (v.findViewById(R.id.imageViewCourseListItem)).setMinimumHeight((int) (Resources.getSystem().getDisplayMetrics().heightPixels * 0.2));
            (v.findViewById(R.id.imageViewCourseListItem)).setMinimumWidth((int) (Resources.getSystem().getDisplayMetrics().heightPixels * 0.2));
            linearLayout.addView(v);
        }
    }
}