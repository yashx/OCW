package com.github.yashx.mit_ocw.activity;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.activity.abstracts.CommonWithRecylerActivity;
import com.github.yashx.mit_ocw.adapter.CourseListItemRecyclerAdapter;
import com.github.yashx.mit_ocw.model.CourseListItem;
import com.github.yashx.mit_ocw.viewmodel.CoursesFromUrlViewModel;

import java.util.ArrayList;

public class BookmarksActivity extends CommonWithRecylerActivity {
    SharedPreferences sharedPreferences;
    String urls;
    CoursesFromUrlViewModel coursesFromUrlViewModel;

    @Override
    protected String getActivityTitle() {
        return "Bookmarks";
    }

    @Override
    protected void initRecyclerView(final RecyclerView recyclerView, final ProgressBar progressBar) {
        sharedPreferences = getSharedPreferences(getResources()
                .getString(R.string.app_name), MODE_PRIVATE);
        urls = sharedPreferences.getString(getResources()
                .getString(R.string.bookmarks), "");

        coursesFromUrlViewModel = new ViewModelProvider(this)
                .get(CoursesFromUrlViewModel.class);

        String[] urlsArray;
        if (urls.isEmpty())
            urlsArray = new String[0];
        else
            urlsArray = urls.split(";");

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        coursesFromUrlViewModel.getCourses().observe(this, new Observer<ArrayList<CourseListItem>>() {
            @Override
            public void onChanged(ArrayList<CourseListItem> courseListItems) {
                recyclerView.setAdapter(new CourseListItemRecyclerAdapter(courseListItems));
                progressBar.setVisibility(View.GONE);
            }
        });
        coursesFromUrlViewModel.getUrlArr().setValue(urlsArray);
    }

    @Override
    protected void onResume() {
        String newUrls = sharedPreferences.getString(getResources()
                .getString(R.string.bookmarks), "");
        if (!urls.equals(newUrls)) {
            String[] urlsArray;
            if (urls.isEmpty())
                urlsArray = new String[0];
            else
                urlsArray = newUrls.split(";");
            coursesFromUrlViewModel.getUrlArr().setValue(urlsArray);
        }
        super.onResume();
    }
}
