package com.github.yashx.mit_ocw.activity.abstracts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.fragment.ImageTextTabBarFragment;
import com.github.yashx.mit_ocw.model.TabModel;
import com.github.yashx.mit_ocw.viewmodel.CourseAndDepartmentViewModel;
import com.google.android.material.tabs.TabLayout;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public abstract class CourseAndDepartmentBaseActivity extends AppCompatActivity
        implements InternetConnectivityListener {
    private String url;
    private CourseAndDepartmentViewModel courseAndDepartmentViewModel;
    private AsyncTask asyncTask;
    private Menu menu;
    private Boolean lastState = null;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.openInBrowserMenuItem:
                //open the current course in browser
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
                break;
            case R.id.bookmarkToggleMenuItem:
                SharedPreferences preferences = getSharedPreferences(getResources()
                        .getString(R.string.app_name), MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                String key = getResources().getString(R.string.bookmarks);
                String oldB = preferences.getString(key, "");
                if (!oldB.contains(url)) {
                    editor.putString(key, oldB + url + ";").apply();
                    menu.findItem(R.id.bookmarkToggleMenuItem)
                            .setIcon(getResources().getDrawable(R.drawable.ic_bookmark_white_24dp));
                } else {
                    editor.putString(key, oldB.replace(url + ";", "")).apply();
                    menu.findItem(R.id.bookmarkToggleMenuItem)
                            .setIcon(getResources().getDrawable(R.drawable.ic_unbookmark_white_24dp));
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_activity_menu, menu);
        this.menu = menu;
        SharedPreferences preferences = getSharedPreferences(getResources()
                .getString(R.string.app_name), MODE_PRIVATE);
        String key = getResources().getString(R.string.bookmarks);
        String oldB = preferences.getString(key, "");
        if (oldB.contains(url))
            menu.findItem(R.id.bookmarkToggleMenuItem)
                    .setIcon(getResources().getDrawable(R.drawable.ic_bookmark_white_24dp));
        else
            menu.findItem(R.id.bookmarkToggleMenuItem)
                    .setIcon(getResources().getDrawable(R.drawable.ic_unbookmark_white_24dp));
        return true;
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        System.out.println(isConnected);
        if (lastState != null && (lastState == isConnected))
            return;
        lastState = isConnected;
        if (isConnected) {
            setContentView(R.layout.activity_common_showdepartment_showcourse);
            url = getIntent().getStringExtra("urlExtra");

            courseAndDepartmentViewModel = new ViewModelProvider(this)
                    .get(CourseAndDepartmentViewModel.class);

            courseAndDepartmentViewModel.getSelectedTab().observe(this, new Observer<TabLayout.Tab>() {
                @Override
                public void onChanged(TabLayout.Tab tab) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutCommonActivity
                            , onTabPressed(tab)).commit();
                }
            });

            courseAndDepartmentViewModel.getDoc().observe(this, new Observer<Document>() {
                @Override
                public void onChanged(Document document) {
                    onPageLoaded(document);
                }
            });

            Toolbar toolbar = findViewById(R.id.toolbarCommonActivity);
            setSupportActionBar(toolbar);


            ImageTextTabBarFragment imageTextTabBarFragment = new ImageTextTabBarFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayoutImageCommonActivity, imageTextTabBarFragment)
                    .commit();

            asyncTask = new CourseAndDepartmentBaseAsyncTask().execute(url);
        } else {
            if (asyncTask != null)
                asyncTask.cancel(true);
            setContentView(R.layout.layout_no_internet);
            Toolbar toolbar = findViewById(R.id.toolbarNoInternet);
            setSupportActionBar(toolbar);
        }
        ActionBar a = getSupportActionBar();
        if (a != null) {
            a.setDisplayHomeAsUpEnabled(true);
            a.setDisplayShowHomeEnabled(true);
            a.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InternetAvailabilityChecker internetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        internetAvailabilityChecker.addInternetConnectivityListener(this);
    }

    protected void setImageUrl(String u) {
        courseAndDepartmentViewModel.getUrlToImage().setValue(u);
    }

    protected void setTextTitle(String t) {
        courseAndDepartmentViewModel.getTextTitle().setValue(t);
    }

    protected void setTabs(ArrayList<TabModel> tabs) {
        courseAndDepartmentViewModel.getAllTabs().setValue(tabs);
    }


    protected abstract Fragment onTabPressed(TabLayout.Tab tab);

    protected abstract void onPageLoaded(Document doc);

    class CourseAndDepartmentBaseAsyncTask extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... strings) {
            Document doc = null;
            try {
                if (!strings[0].endsWith("/"))
                    strings[0] += "/";
                doc = Jsoup.connect(strings[0]).get();
            } catch (Exception e) {
                Log.e("TAG", "doInBackground: ", e);
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            courseAndDepartmentViewModel.getDoc().setValue(document);
        }
    }

    @Override
    protected void onDestroy() {
        InternetAvailabilityChecker.getInstance()
                .removeInternetConnectivityChangeListener(this);
        asyncTask.cancel(true);
        super.onDestroy();
    }
}