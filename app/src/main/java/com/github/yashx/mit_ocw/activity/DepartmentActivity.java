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
import com.github.yashx.mit_ocw.fragment.DepartmentFeaturedCoursesFragment;
import com.github.yashx.mit_ocw.fragment.DepartmentHomeFragment;
import com.github.yashx.mit_ocw.fragment.ImageTextTabBarFragment;
import com.github.yashx.mit_ocw.model.CourseListItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

//see course activity for explanations not given as most code is same
public class DepartmentActivity extends AppCompatActivity implements ImageTextTabBarFragment.Callbacks {

    private String url;
    private ImageTextTabBarFragment.Callbacks callbacks;
    private Document doc;
    private ArrayList<CourseListItem> courseListItemArrayList;
    private ArrayList<String> urlList;
    private DepartmentHomeFragment departmentHomeFragment;
    private DepartmentFeaturedCoursesFragment departmentFeaturedCoursesFragment;
    private DepartmentAllCoursesFragment departmentAllCoursesFragment;

    @Override
    public void onTabPressed(Object tabTag) {
        Fragment currentFragment;
        switch ((String) tabTag) {
            default:
            case "home":
                if (departmentHomeFragment == null)
                    departmentHomeFragment = DepartmentHomeFragment.newInstance(doc);
                currentFragment = departmentHomeFragment;
                break;
            case "Featured Courses":
                if (departmentFeaturedCoursesFragment == null)
                    departmentFeaturedCoursesFragment = DepartmentFeaturedCoursesFragment.newInstance(urlList);
                currentFragment = departmentFeaturedCoursesFragment;
                break;
            case "All Courses":
                if (departmentAllCoursesFragment == null)
                    departmentAllCoursesFragment = DepartmentAllCoursesFragment.newInstance(courseListItemArrayList);
                currentFragment = departmentAllCoursesFragment;
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutCommonActivity, currentFragment).commit();
    }

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

    class JsoupDocumentAsyncLoader extends AsyncTask<String, String, Document> {
        ArrayList<CourseListItem> courseListItems;
        ArrayList<String> urls;

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);

            courseListItemArrayList = courseListItems;
            urlList = urls;

            doc = document;
            //finds department pic absolute url
            String u = document.select("#global_inner > img").first().absUrl("src");
            //finds department name
            String t = document.select("#parent-fieldname-title").first().text();
            ArrayList<String> tabNames = new ArrayList<>();
            ArrayList<String> tabTags = new ArrayList<>();

            //for tabs (nothing needs to be loaded as everything is on doc)
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

            //getting All Courses
            Elements eTs = doc.select("#global_inner > div.courseListDiv > ul > li:not(.courseListHeaderRow)");
            if (eTs != null) {
                courseListItems = new ArrayList<>();
                for (Element e : eTs) {
                    String href;
                    final CourseListItem courseListItem;
                    //getting absolute url (absUrl doesn't work as doc is loaded from html)
                    String url = e.selectFirst("a").attr("href");

                    if (!url.contains("https://"))
                        url = "https://ocw.mit.edu" + url;
                    if (!url.endsWith("/"))
                        url += "/";
                    href = url;

                    courseListItem = new CourseListItem(
                            e.attr("data-title"),
                            e.attr("data-courseno"),
                            e.attr("data-semester"),
                            href
                    );

                    courseListItems.add(courseListItem);
                }

            }

            //getting Featured Courses
            urls = new ArrayList<>();
            eTs = doc.select("#carousel_ul .item");
            if (eTs != null) {
                for (Element e : eTs) {
                    //getting absolute url (absUrl doesn't work as doc is loaded from html)
                    String url = e.selectFirst("a").attr("href");
                    if (!url.contains("https://"))
                        url = "https://ocw.mit.edu" + url;
                    if (!url.endsWith("/"))
                        url += "/";
                    int l = url.lastIndexOf("index.htm");
                    if (l != -1) {
                        url = url.substring(0, l) + "index.json";
                    }
                    urls.add(url);
                }
            }

            return doc;
        }
    }
}