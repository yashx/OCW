package com.github.yashx.mit_ocw.model;

import com.google.gson.Gson;

import java.io.Serializable;

public class CourseListItem implements Serializable {
    String title;
    String thumb;
    String mcn;
    String sem;
    String href;

    public static CourseListItem fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, CourseListItem.class);
    }

    public CourseListItem(String title,String mcn, String sem,String href) {
        this.title = title;
        this.mcn = mcn;
        this.sem = sem;
        this.href = href;
    }

    public CourseListItem(String title, String mcn, String sem,String href, String thumb) {
        this.title = title;
        this.thumb = thumb;
        this.mcn = mcn;
        this.sem = sem;
        this.href = href;
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

    public String getHref() {
        return href;
    }

    public String getSem() {
        return sem;
    }

}
