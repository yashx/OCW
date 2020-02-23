package com.github.yashx.mit_ocw.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.github.yashx.mit_ocw.R;

public abstract class CommonWithRecylerActivity extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_with_recycler);


        Toolbar toolbar = findViewById(R.id.toolbarCommonWithRecyler);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerViewCommonWithRecyler);
        initRecyclerView(recyclerView);

        TextView textView = findViewById(R.id.textViewCommonWithRecyler);
        textView.setText(getActivityTitle());
    }

    abstract protected void initRecyclerView(RecyclerView recyclerView);

    abstract protected String getActivityTitle();
}
