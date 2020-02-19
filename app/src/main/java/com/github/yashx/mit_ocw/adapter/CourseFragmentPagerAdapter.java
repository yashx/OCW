package com.github.yashx.mit_ocw.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.github.yashx.mit_ocw.fragment.CourseHomeFragment;

import org.jsoup.nodes.Document;

import java.io.Console;
import java.util.ArrayList;

public class CourseFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String> tabs;
    private Document doc;

    public CourseFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior, Document doc, ArrayList<String> tabs) {
        super(fm, behavior);
        this.tabs = tabs;
        this.doc = doc;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new CourseHomeFragment(doc);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position);
    }
}
