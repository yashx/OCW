package com.github.yashx.mit_ocw.activity.abstracts;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.github.yashx.mit_ocw.R;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

public abstract class CommonWithRecyclerActivity extends AppCompatActivity
        implements InternetConnectivityListener {

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
            setContentView(R.layout.activity_common_with_recycler);

            ProgressBar progressBar = findViewById(R.id.progressBarCommonWithRecycler);

            Toolbar toolbar = findViewById(R.id.toolbarCommonWithRecycler);
            setSupportActionBar(toolbar);

            RecyclerView recyclerView = findViewById(R.id.recyclerViewCommonWithRecycler);
            initRecyclerView(recyclerView, progressBar);

            TextView textView = findViewById(R.id.textViewCommonWithRecycler);
            textView.setText(getActivityTitle());
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

    abstract protected void initRecyclerView(RecyclerView recyclerView, ProgressBar progressBar);

    abstract protected String getActivityTitle();
}