package com.github.yashx.mit_ocw.model;

import com.google.gson.Gson;

public class CourseListItem {
    String title;
    String thumb;
    String mcn;
    String sem;
    String id;

    public static CourseListItem fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, CourseListItem.class);
    }

    public CourseListItem(String title,String mcn, String sem, String id) {
        this.title = title;
        this.mcn = mcn;
        this.sem = sem;
        this.id = id;
    }

    public CourseListItem(String title, String mcn, String sem, String id,String thumb) {
        this.title = title;
        this.thumb = thumb;
        this.mcn = mcn;
        this.sem = sem;
        this.id = id;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return mcn + " " + sem;
    }

    public String getThumb() {
        if (!thumb.contains("https://"))
            return "https://ocw.mit.edu" + thumb;
        else
            return thumb;
    }

    public String getMcn() {
        return mcn;
    }

    public String getSem() {
        return sem;
    }

    public String getId() {
        return id;
    }
}
