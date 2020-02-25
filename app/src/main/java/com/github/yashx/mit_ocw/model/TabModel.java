package com.github.yashx.mit_ocw.model;

import com.google.android.material.tabs.TabLayout;

public class TabModel {
    private String text;
    private Object tag;

    public String getText() {
        return text;
    }

    public TabModel setText(String text) {
        this.text = text;
        return this;
    }

    public Object getTag() {
        return tag;
    }

    public TabModel setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    public TabLayout.Tab tabModelToTab(TabLayout tabLayout){
        return tabLayout.newTab().setText(text).setTag(tag);
    }
}
