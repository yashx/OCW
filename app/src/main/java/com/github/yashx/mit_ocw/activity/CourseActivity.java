package com.github.yashx.mit_ocw.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.adapter.CourseFragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity {

    Context c;
    Toolbar toolbar;
    RelativeLayout r;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        c = this;

        tabLayout = findViewById(R.id.tabLayoutCourseActivity);
        r = findViewById(R.id.relativeLayoutCourseActivity);
        toolbar = findViewById(R.id.toolbarCourseActivity);
        viewPager = findViewById(R.id.viewPagerCourseActivity);

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
        protected void onPostExecute(Document document) {
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

            TextView textView = new TextView(c);
            textView.setTextColor(Color.WHITE);
            textView.setShadowLayer(getDps(12.0f), getDps(0.5f), 0, Color.BLACK);
            textView.setPadding(getDps(16f), getDps(16f), getDps(16f), getDps(16f));
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            textView.setText(t);
            lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            textView.setLayoutParams(lp);

            r.addView(imageView, 0);
            r.addView(textView, 1);

            ArrayList<String> tabs = new ArrayList<>();
            for (Element e : document.select("#course_nav > ul > li")) {
                if (e.selectFirst("a") != null && !e.selectFirst("a").text().isEmpty()) {
                    tabs.add(e.selectFirst("a").text().trim());
                }
            }
            tabs.remove(tabs.size()-1);
            viewPager.setAdapter(new CourseFragmentPagerAdapter(getSupportFragmentManager(),
                    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, document, tabs));
            tabLayout.setupWithViewPager(viewPager);
        }

        @Override
        protected Document doInBackground(String... strings) {
            //getting doc with jsoup
            Document doc = null;
            try {
                doc = Jsoup.connect(strings[0]).get();
            } catch (Exception e) {
                Log.e("TAG", "doInBackground: ", e);
            }
            return doc;
        }
    }

    int getDps(float f) {
        float s = c.getResources().getDisplayMetrics().density;
        return (int) (f * s + 0.5f);
    }

}
