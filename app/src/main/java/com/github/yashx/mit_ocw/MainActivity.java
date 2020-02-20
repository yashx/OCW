package com.github.yashx.mit_ocw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.github.yashx.mit_ocw.activity.CourseActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context c = getApplicationContext();
        setContentView(R.layout.activity_main);
        findViewById(R.id.urlButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(c, CourseActivity.class);
                        i.putExtra(c.getResources().getString(R.string.urlExtra), ((EditText) findViewById(R.id.urlText)).getText().toString());
                        startActivity(i);
                    }
                }
        );
    }
}