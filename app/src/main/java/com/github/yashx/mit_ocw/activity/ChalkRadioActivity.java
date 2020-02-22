package com.github.yashx.mit_ocw.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.adapter.ChalkRadioLinksAdapter;

import java.util.ArrayList;

public class ChalkRadioActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chalk_radio);

        Toolbar toolbar = findViewById(R.id.toolbarChalkRadio);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        String[] platforms = getResources().getStringArray(R.array.platforms);
        String[] platformUrls = getResources().getStringArray(R.array.platformUrls);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewChalkRadio);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),4));
        recyclerView.setAdapter(new ChalkRadioLinksAdapter(platforms,platformUrls));
    }
}
