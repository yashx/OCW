package com.github.yashx.mit_ocw.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.util.JsoupElementCleaner;
import com.github.yashx.mit_ocw.util.JsoupViewBuilder;
import com.github.yashx.mit_ocw.util.ViewBuilders;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class DepartmentAllCoursesFragment extends Fragment {
    private LinearLayout linearLayout;
    private Context context;

    public static DepartmentAllCoursesFragment newInstance(String url) {
        //stores the url of page to be loaded to be retrieved later
        Bundle args = new Bundle();
        args.putString("url", url);
        DepartmentAllCoursesFragment fragment = new DepartmentAllCoursesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_common, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayout = view.findViewById(R.id.linearLayoutCommonFragment);

        //url is fetched and async job is started
        String url = getArguments().getString("url");
        new HtmlAsyncTask().execute(url);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    class HtmlAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            //Uses Jsoup to get Json
            String json = null;
            try {
                if (!strings[0].endsWith("/"))
                    strings[0] += "/";
                if (strings[0].contains("index.htm"))
                    strings[0] = strings[0].replace("index.htm", "index.json");
                else
                    strings[0] += "index.json";
                json = Jsoup.connect(strings[0]).ignoreContentType(true).execute().body();
            } catch (Exception e) {
                Log.e("TAG", "doInBackground: ", e);
            }
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            linearLayout.addView(ViewBuilders.SmallBodyMidTextView(context, s));
        }
    }

    int getDps(float f) {
        float s = context.getResources().getDisplayMetrics().density;
        return (int) (f * s + 0.5f);
    }
}