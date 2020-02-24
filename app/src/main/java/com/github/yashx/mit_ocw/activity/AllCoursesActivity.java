package com.github.yashx.mit_ocw.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.adapter.CourseListItemNoImageRecyclerAdapter;
import com.github.yashx.mit_ocw.model.CourseListItem;
import com.github.yashx.mit_ocw.viewmodel.CoursesFromLiTagViewModel;
import com.github.yashx.mit_ocw.viewmodel.CoursesFromLiTagViewModelFactory;
import com.github.yashx.mit_ocw.viewmodel.CoursesFromJsonViewModel;

import java.util.ArrayList;

public class AllCoursesActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_courses);

        Toolbar toolbar = findViewById(R.id.toolbarAllCourses);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        CoursesFromLiTagViewModelFactory factory = new CoursesFromLiTagViewModelFactory(
                "https://ocw.mit.edu/courses/",
                "#course_wrapper   ul > li.courseListRow"
        );

        CoursesFromLiTagViewModel coursesFromJsonViewModel = new ViewModelProvider(this,factory)
                .get(CoursesFromLiTagViewModel.class);

        final ProgressBar progressBar = findViewById(R.id.progressBarAllCourses);
        final RecyclerView recyclerView = findViewById(R.id.recyclerViewAllCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        coursesFromJsonViewModel.getCourses().observe(this, new Observer<ArrayList<CourseListItem>>() {
            @Override
            public void onChanged(ArrayList<CourseListItem> courseListItems) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(new CourseListItemNoImageRecyclerAdapter(courseListItems));
            }
        });

    }
}
