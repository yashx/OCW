package com.github.yashx.mit_ocw.adapter;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.model.CourseListItem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;

import java.util.ArrayList;

public class AllCourseListItemRecyclerAdapter extends RecyclerView.Adapter<AllCourseListItemRecyclerAdapter.CourseListItemViewHolder> {
    private ArrayList<CourseListItem> courses;

    class CourseListItemViewHolder extends RecyclerView.ViewHolder {
        ImageView courseThumbImage;
        TextView courseTitle;
        TextView courseSubtitle;

        CourseListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            courseThumbImage = itemView.findViewById(R.id.imageViewCourseListItem);
            courseTitle = itemView.findViewById(R.id.titleTextViewCourseListItem);
            courseSubtitle = itemView.findViewById(R.id.subTitleTextViewCourseListItem);
        }
    }

    public AllCourseListItemRecyclerAdapter(ArrayList<CourseListItem> courses) {
        this.courses = courses;
    }

    @NonNull
    @Override
    public CourseListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_course, parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(layoutParams);
        return new CourseListItemViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }


    @Override
    public void onBindViewHolder(@NonNull CourseListItemViewHolder holder, int position) {
        CourseListItem courseListItem = courses.get(position);
        holder.courseTitle.setText(courseListItem.getTitle());
        holder.courseSubtitle.setText(courseListItem.getSubtitle());
        (holder.courseThumbImage).setMinimumHeight((int) (Resources.getSystem().getDisplayMetrics().heightPixels * 0.2));
        (holder.courseThumbImage).setMinimumWidth((int) (Resources.getSystem().getDisplayMetrics().heightPixels * 0.2));

        String url = courseListItem.getHref();

        if (url.contains("index")) {
            int l = url.lastIndexOf("index.htm");
            if (l != -1) {
                url = url.substring(0, l) + "index.json";
            }
        } else
            url = url + "index.json";

        //using href to get json and from it load image
        new ImageFetcherAsync(holder.courseThumbImage, url).execute();

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
            s = s.replace("\"", "");
            if (!s.contains("https://"))
                s = "https://ocw.mit.edu" + s;

            Picasso.get().load(s).error(android.R.drawable.stat_notify_error).into(imageView);
        }
    }
}
