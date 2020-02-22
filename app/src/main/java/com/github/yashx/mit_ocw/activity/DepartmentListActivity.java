package com.github.yashx.mit_ocw.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.adapter.DepartmentListRecyclerAdapter;
import com.github.yashx.mit_ocw.util.LoadJsonFromAsset;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DepartmentListActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_list);

        Toolbar toolbar = findViewById(R.id.toolbarDepartmentList);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        String json = LoadJsonFromAsset.load(this, "depts.json");
        try {
            JsonArray jsonArray = (new Gson()).fromJson(json,JsonArray.class);

            RecyclerView recyclerView = findViewById(R.id.recyclerViewDepartmentList);
            recyclerView.setAdapter(new DepartmentListRecyclerAdapter(jsonArray));
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
