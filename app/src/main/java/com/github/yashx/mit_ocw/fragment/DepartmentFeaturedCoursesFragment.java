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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.model.CourseListItem;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;

import java.util.ArrayList;

public class DepartmentFeaturedCoursesFragment extends Fragment {

    private Context context;
    private LinearLayout linearLayout;

    public static DepartmentFeaturedCoursesFragment newInstance(ArrayList<String> urlList) {
        Bundle args = new Bundle();
        args.putStringArrayList("html", urlList);
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
        linearLayout = view.findViewById(R.id.linearLayoutCommonFragment);

        ArrayList<String> urlList = getArguments().getStringArrayList("html");
        for (String url : urlList)
            new JsonFetcherAsync().execute(url);

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
            CourseListItem courseListItem;
            courseListItem = CourseListItem.fromJson(json);

            //inflating listitem_course and adding it to fragment
            View v = LayoutInflater.from(context).inflate(R.layout.listitem_course, linearLayout, false);
            ((TextView) v.findViewById(R.id.titleTextViewCourseListItem)).setText(courseListItem.getTitle());
            ((TextView) v.findViewById(R.id.subTitleTextViewCourseListItem)).setText(courseListItem.getSubtitle());
            Picasso.get().load(courseListItem.getThumb()).into(((ImageView) v.findViewById(R.id.imageViewCourseListItem)));
            (v.findViewById(R.id.imageViewCourseListItem)).setMinimumHeight((int) (Resources.getSystem().getDisplayMetrics().heightPixels * 0.2));
            (v.findViewById(R.id.imageViewCourseListItem)).setMinimumWidth((int) (Resources.getSystem().getDisplayMetrics().heightPixels * 0.2));
            linearLayout.addView(v);
        }
    }
}