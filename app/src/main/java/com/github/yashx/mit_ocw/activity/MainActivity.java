package com.github.yashx.mit_ocw.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.github.yashx.mit_ocw.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context c = getApplicationContext();
        setContentView(R.layout.activity_home);
//        findViewById(R.id.urlButton).setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String s = ((EditText) findViewById(R.id.urlText)).getText().toString();
//                        Intent i;
////                            i = new Intent(c, CourseActivity.class);
//                            i = new Intent(c, DepartmentActivity.class);
//                        i.putExtra(c.getResources().getString(R.string.urlExtra), s);
//                        startActivity(i);
//                    }
//                }
//        );
    }
}