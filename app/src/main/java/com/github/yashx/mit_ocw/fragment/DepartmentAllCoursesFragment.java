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

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.model.CourseListItem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
            for (Element e : eTs) {
                String id;
                final CourseListItem courseListItem;
                //getting absolute url (absUrl doesn't work as doc is loaded from html)
                String url = e.selectFirst("a").attr("href");
                if (url.endsWith("/"))
                    url = url.substring(0, url.lastIndexOf("/"));

                id = url.substring(url.lastIndexOf("/") + 1);
                if (!url.contains("https://"))
                    url = "https://ocw.mit.edu" + url;
                if (!url.endsWith("/"))
                    url += "/";
                if(url.contains("index")) {
                    int l = url.lastIndexOf("index.htm");
                    if (l != -1) {
                        url = url.substring(0, l) + "index.json";
                    }
                }
                else
                    url = url + "index.json";

                courseListItem = new CourseListItem(
                        e.attr("data-title"),
                        e.attr("data-courseno"),
                        e.attr("data-semester"),
                        id
                );

                View v = LayoutInflater.from(context).inflate(R.layout.listitem_course, linearLayout, false);
                ((TextView) v.findViewById(R.id.titleTextViewCourseListItem)).setText(courseListItem.getTitle());
                ((TextView) v.findViewById(R.id.subTitleTextViewCourseListItem)).setText(courseListItem.getSubtitle());
                (v.findViewById(R.id.imageViewCourseListItem)).setMinimumHeight((int) (Resources.getSystem().getDisplayMetrics().heightPixels * 0.2));
                (v.findViewById(R.id.imageViewCourseListItem)).setMinimumWidth((int) (Resources.getSystem().getDisplayMetrics().heightPixels * 0.2));
                linearLayout.addView(v);


                if (i < 2) {
                    i++;
                    new ImageFetcherAsync(((ImageView) v.findViewById(R.id.imageViewCourseListItem)), url).execute();
                }
            }
        }
    }


    class ImageFetcherAsync extends AsyncTask<Void, String, String> {
        ImageView imageView;
        String urlToJson;

        public ImageFetcherAsync(ImageView imageView, String urlToJson) {
            this.imageView = imageView;
            this.urlToJson = urlToJson;
        }

        @Override
        protected String doInBackground(Void... url) {
            String json = "";
            try {
                json = (Jsoup.connect(urlToJson).ignoreContentType(true).execute().body());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            JsonObject jsonObject = (new Gson()).fromJson(json, JsonObject.class);
            String s = jsonObject.get("thumb").toString();
            s = s.replace("\"","");
            if (!s.contains("https://"))
                s = "https://ocw.mit.edu" + s;

            System.out.println(s);
            Picasso.get().load(s).error(android.R.drawable.stat_notify_error).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}