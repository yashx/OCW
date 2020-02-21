package com.github.yashx.mit_ocw.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.model.CourseListItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CourseListItemRecyclerAdapter extends RecyclerView.Adapter<CourseListItemRecyclerAdapter.CourseListItemViewHolder> {
    ArrayList<CourseListItem> courses;

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

    public CourseListItemRecyclerAdapter(ArrayList<CourseListItem> courses) {
        this.courses = courses;
    }

    @NonNull
    @Override
    public CourseListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_course, parent, false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
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
        if (!TextUtils.isEmpty(courseListItem.getThumb()))
            Picasso.get().load(courseListItem.getThumb()).into(holder.courseThumbImage);
    }
}
