package com.github.yashx.mit_ocw.activity;

import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.yashx.mit_ocw.activity.abstracts.CommonWithRecyclerActivity;
import com.github.yashx.mit_ocw.adapter.DepartmentListRecyclerAdapter;
import com.github.yashx.mit_ocw.util.LoadJsonFromAsset;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class DepartmentListActivity extends CommonWithRecyclerActivity {


    @Override
    protected void initRecyclerView(RecyclerView recyclerView, ProgressBar progressBar) {
        try {
            String json = LoadJsonFromAsset.load(getApplicationContext(), "depts.json");
            JsonArray jsonArray = (new Gson()).fromJson(json, JsonArray.class);
            recyclerView.setAdapter(new DepartmentListRecyclerAdapter(jsonArray));
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            progressBar.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getActivityTitle() {
        return "Departments";
    }
}