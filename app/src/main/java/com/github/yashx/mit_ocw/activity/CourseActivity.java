package com.github.yashx.mit_ocw.activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.fragment.CourseHomeFragment;
import com.github.yashx.mit_ocw.fragment.HtmlRendererCourseFragment;
import com.github.yashx.mit_ocw.util.ViewBuilders;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CourseActivity extends AppCompatActivity {

    Context c;
    Toolbar toolbar;
    RelativeLayout r;
    TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        c = this;

        tabLayout = findViewById(R.id.tabLayoutCourseActivity);
        r = findViewById(R.id.relativeLayoutCourseActivity);
        toolbar = findViewById(R.id.toolbarCourseActivity);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //getting url from intent
        String url = getIntent().getStringExtra(c.getResources().getString(R.string.urlExtra));
        new Loader().execute(url);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class Loader extends AsyncTask<String, String, Document> {
        @Override
        protected void onPostExecute(final Document document) {
            super.onPostExecute(document);

            //getting courseImage url and loading it
            String u = document.select("#chpImage > div.image > img").first().absUrl("src");


            ImageView imageView = new ImageView(c);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setMinimumHeight((int) (Resources.getSystem().getDisplayMetrics().heightPixels * 0.4));
            imageView.setMinimumWidth(Resources.getSystem().getDisplayMetrics().widthPixels);
            Picasso.get().load(u).into(imageView);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            imageView.setLayoutParams(lp);

            //getting course title
            String t = document.select("#course_title > h1").first().text();

            TextView textView = ViewBuilders.BigHeadingTextView(c, t);
            lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            textView.setLayoutParams(lp);

            r.addView(imageView, 0);
            r.addView(textView, 1);

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutCourseActivity, CourseHomeFragment.newInstance(document)).commit();

            int i = 0;
            for (Element e : document.select("#course_nav > ul > li")) {
                if (e.selectFirst("a") != null && !e.selectFirst("a").text().isEmpty()) {
                    if (i == 0) {
                        i++;
                        tabLayout.addTab(tabLayout.newTab().setText(e.selectFirst("a").text().trim())
                                .setTag("home"));
                    } else {
                        String s = e.selectFirst("a").text().trim().toLowerCase();
                        if (!(s.contains("insight") || s.contains("download")))
                            tabLayout.addTab(tabLayout.newTab().setText(s)
                                    .setTag(e.selectFirst("a").absUrl("href")));
                    }
                }
            }
//            tabs.remove(tabs.size()-1);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                @Override
                public void onTabSelected(TabLayout.Tab tab) {
//                    Toast.makeText(c, tab.getTag().toString(), Toast.LENGTH_LONG).show();
                    Fragment currentFragment;
                    if (tab.getTag().toString().equals("home"))
                        currentFragment = CourseHomeFragment.newInstance(document);
                    else
                        currentFragment = HtmlRendererCourseFragment.newInstance(tab.getTag().toString());
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutCourseActivity, currentFragment).commit();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
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
