package com.github.yashx.mit_ocw.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.fragment.HtmlRendererCourseFragment;

public class HtmlRendererActivity extends AppCompatActivity {

    String url;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (android.R.id.home):
                finish();
                break;
            case R.id.openInBrowserMenuItem:
                //open the current course in browser
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_activity_menu, menu);
        menu.findItem(R.id.bookmarkToggleMenuItem).setVisible(false);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_renderer);

        Toolbar toolbar = findViewById(R.id.toolbarHtmlRenderer);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        url = getIntent().getStringExtra("urlExtra");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolderHtmlRenderer
                , HtmlRendererCourseFragment.newInstance(url)).commit();
    }
}
