package com.github.yashx.mit_ocw.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.yashx.mit_ocw.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
        if (sharedPreferences.getBoolean("firstRun", true)) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("Disclaimer")
                    .setMessage(getResources().getString(R.string.disclaimer)).setPositiveButton("Okay", null).create();
            alertDialog.show();
            sharedPreferences.edit().putBoolean("firstRun", false).apply();
        }

        findViewById(R.id.deptListCard).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), DepartmentListActivity.class);
                        startActivity(i);
                    }
                }
        );

        findViewById(R.id.podcastCard).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), ChalkRadioActivity.class);
                        startActivity(i);
                    }
                }
        );

        findViewById(R.id.popularCoursesCard).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), PopularCoursesActivity.class);
                        startActivity(i);
                    }
                }
        );

        findViewById(R.id.newCoursesCard).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), NewCoursesActivity.class);
                        startActivity(i);
                    }
                }
        );

        findViewById(R.id.scholarCoursesCard).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), ScholarCoursesActivity.class);
                        startActivity(i);
                    }
                }
        );

        findViewById(R.id.contributeCard).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), AboutActivity.class);
                        startActivity(i);
                    }
                }
        );

        findViewById(R.id.allCoursesCard).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), AllCoursesActivity.class);
                        startActivity(i);
                    }
                }
        );

        findViewById(R.id.bookmarkedCoursesCard).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), BookmarksActivity.class);
                        startActivity(i);
                    }
                }
        );
    }
}