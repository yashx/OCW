package com.github.yashx.mit_ocw.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.yashx.mit_ocw.adapter.DepartmentListRecyclerAdapter;
import com.github.yashx.mit_ocw.util.LoadJsonFromAsset;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class DepartmentListActivity extends CommonWithRecylerActivity {


    @Override
    protected void initRecyclerView(RecyclerView recyclerView) {
        try {
            String json = LoadJsonFromAsset.load(getApplicationContext(), "depts.json");
            JsonArray jsonArray = (new Gson()).fromJson(json, JsonArray.class);
            recyclerView.setAdapter(new DepartmentListRecyclerAdapter(jsonArray));
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getActivityTitle() {
        return "Departments";
    }
}