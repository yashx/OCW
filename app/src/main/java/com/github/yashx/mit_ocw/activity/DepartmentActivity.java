package com.github.yashx.mit_ocw.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.fragment.DepartmentAllCoursesFragment;
import com.github.yashx.mit_ocw.fragment.DepartmentHomeFragment;
import com.github.yashx.mit_ocw.fragment.DepartmentFeaturedCoursesFragment;
import com.github.yashx.mit_ocw.fragment.ImageTextTabBarFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class DepartmentActivity extends AppCompatActivity implements ImageTextTabBarFragment.Callbacks {

    private String url;
    private ImageTextTabBarFragment.Callbacks callbacks;
    private Document doc;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.openInBrowserMenuItem:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_activity_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);


        Toolbar toolbar = findViewById(R.id.toolbarCommonActivity);
        callbacks = this;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        url = getIntent().getStringExtra("urlExtra");
        new DepartmentActivity.JsoupDocumentAsyncLoader().execute(url);
    }

    @Override
    public void onTabPressed(Object tabTag) {
        Fragment currentFragment;
        switch ((String) tabTag) {
            default:
            case "home":
                currentFragment = DepartmentHomeFragment.newInstance(doc);
                break;
            case "Featured Courses":
                currentFragment = DepartmentFeaturedCoursesFragment.newInstance(doc);
                break;
            case "All Courses":
                currentFragment = DepartmentAllCoursesFragment.newInstance(url);
                break;

        }
//        else
//            currentFragment = HtmlRendererCourseFragment.newInstance((String) tabTag);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutCommonActivity, currentFragment).commit();
    }

    class JsoupDocumentAsyncLoader extends AsyncTask<String, String, Document> {
        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);

            doc = document;
            String u = document.select("#global_inner > img").first().absUrl("src");
            String t = document.select("#parent-fieldname-title").first().text();
            ArrayList<String> tabNames = new ArrayList<>();
            ArrayList<String> tabTags = new ArrayList<>();

            tabNames.add("Home");
            tabNames.add("Featured Courses");
            tabNames.add("All Courses");
            tabTags.add("Home");
            tabTags.add("Featured Courses");
            tabTags.add("All Courses");

            ImageTextTabBarFragment imageTextTabBarFragment = ImageTextTabBarFragment.newInstance(u, t, tabNames, tabTags);
            imageTextTabBarFragment.setCallbacks(callbacks);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayoutImageCommonActivity, imageTextTabBarFragment)
                    .replace(R.id.frameLayoutCommonActivity, DepartmentHomeFragment.newInstance(document))
                    .commit();

        }

        @Override
        protected Document doInBackground(String... strings) {
            //getting doc with jsoup
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
    }
}