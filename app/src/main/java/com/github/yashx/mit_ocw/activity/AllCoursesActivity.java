package com.github.yashx.mit_ocw.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

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
import com.github.yashx.mit_ocw.viewmodel.CoursesFromLiTagViewModelWithSearchFactory;
import com.github.yashx.mit_ocw.viewmodel.CoursesFromLiTagWithSearchViewModel;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import java.util.ArrayList;

public class AllCoursesActivity extends AppCompatActivity implements InternetConnectivityListener {

    Boolean lastState = null;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        System.out.println("isConnected " + isConnected);
        if (lastState != null && (lastState == isConnected))
            return;
        lastState = isConnected;
        if (isConnected) {
            setContentView(R.layout.activity_all_courses);

            Toolbar toolbar = findViewById(R.id.toolbarAllCourses);
            setSupportActionBar(toolbar);

            CoursesFromLiTagViewModelWithSearchFactory factory = new CoursesFromLiTagViewModelWithSearchFactory(
                    "https://ocw.mit.edu/courses/",
                    "#course_wrapper   ul > li.courseListRow"
            );

            final CoursesFromLiTagWithSearchViewModel coursesFromJsonViewModel =
                    new ViewModelProvider(this, factory)
                            .get(CoursesFromLiTagWithSearchViewModel.class);

            final ProgressBar progressBar = findViewById(R.id.progressBarAllCourses);
            final RecyclerView recyclerView = findViewById(R.id.recyclerViewAllCourses);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            coursesFromJsonViewModel.getFilteredCourses().observe(this, new Observer<ArrayList<CourseListItem>>() {
                @Override
                public void onChanged(ArrayList<CourseListItem> courseListItems) {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setAdapter(new CourseListItemNoImageRecyclerAdapter(courseListItems));
                }
            });

            SearchView searchView = findViewById(R.id.searchViewAllCourses);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    coursesFromJsonViewModel.getTextQuery().setValue(newText);
                    return true;
                }
            });
        } else {
            setContentView(R.layout.layout_no_internet);
            Toolbar toolbar = findViewById(R.id.toolbarNoInternet);
            setSupportActionBar(toolbar);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InternetAvailabilityChecker internetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        internetAvailabilityChecker.addInternetConnectivityListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        InternetAvailabilityChecker.getInstance()
                .removeInternetConnectivityChangeListener(this);
    }
}