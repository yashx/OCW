package com.github.yashx.mit_ocw.adapter;

import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.activity.ShowCourseActivity;
import com.github.yashx.mit_ocw.model.CourseListItem;

import java.util.ArrayList;

public class CourseListItemNoImageRecyclerAdapter extends RecyclerView.Adapter<CourseListItemNoImageRecyclerAdapter.CourseListItemNoImageViewHolder> {
    private ArrayList<CourseListItem> courses;

    class CourseListItemNoImageViewHolder extends RecyclerView.ViewHolder {
        TextView courseTitle;
        TextView courseSubtitle;
        TextView courseCode;

        CourseListItemNoImageViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.titleTextViewCourseListItemNoImage);
            courseSubtitle = itemView.findViewById(R.id.subTitleTextViewCourseListItemNoImage);
            courseCode = itemView.findViewById(R.id.codeTextViewCourseListItemNoImage);
        }
    }

    public CourseListItemNoImageRecyclerAdapter(ArrayList<CourseListItem> courses) {
        this.courses = courses;
    }

    @NonNull
    @Override
    public CourseListItemNoImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_course_no_image, parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(layoutParams);
        return new CourseListItemNoImageViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }


    @Override
    public void onBindViewHolder(@NonNull CourseListItemNoImageViewHolder holder, int position) {
        final CourseListItem courseListItem = courses.get(position);
        holder.courseTitle.setText(courseListItem.getTitle());
        holder.courseSubtitle.setText(courseListItem.getSubtitle());
        (holder.courseCode).setMinimumWidth((int) (Resources.getSystem().getDisplayMetrics().heightPixels * 0.2));
        (holder.courseCode).setText(courseListItem.getMcn());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ShowCourseActivity.class);
                i.putExtra(v.getContext().getResources().getString(R.string.urlExtra), courseListItem.getHref());
                v.getContext().startActivity(i);
            }
        });

    }
}
