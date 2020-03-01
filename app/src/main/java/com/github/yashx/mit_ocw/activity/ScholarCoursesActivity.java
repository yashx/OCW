package com.github.yashx.mit_ocw.activity;

import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.yashx.mit_ocw.activity.abstracts.CommonWithRecyclerActivity;
import com.github.yashx.mit_ocw.adapter.CourseListItemRecyclerAdapter;
import com.github.yashx.mit_ocw.model.CourseListItem;
import com.github.yashx.mit_ocw.viewmodel.CoursesFromCourseJsonViewModelFactory;
import com.github.yashx.mit_ocw.viewmodel.CoursesFromCourseJsonViewModel;

import java.util.ArrayList;

public class ScholarCoursesActivity extends CommonWithRecyclerActivity {

    @Override
    protected void initRecyclerView(final RecyclerView recyclerView, final ProgressBar progressBar) {
        CoursesFromCourseJsonViewModelFactory factory =
                new CoursesFromCourseJsonViewModelFactory("https://ocw.mit.edu/courses/ocw-scholar/",
                        "div.scmod> a");
        CoursesFromCourseJsonViewModel coursesFromCourseJsonViewModel = new ViewModelProvider(this, factory)
                .get(CoursesFromCourseJsonViewModel.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        coursesFromCourseJsonViewModel.getCourses().observe(this, new Observer<ArrayList<CourseListItem>>() {
            @Override
            public void onChanged(ArrayList<CourseListItem> courseListItems) {
                recyclerView.setAdapter(new CourseListItemRecyclerAdapter(courseListItems));
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected String getActivityTitle() {
        return "Scholar Courses";
    }
}
