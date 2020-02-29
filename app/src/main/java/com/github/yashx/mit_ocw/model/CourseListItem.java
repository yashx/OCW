package com.github.yashx.mit_ocw.model;

import com.google.gson.Gson;

import java.io.Serializable;

public class CourseListItem implements Serializable {
    String title;
    String thumb;
    String mcn;
    String sem;
    String href;
    String features;
    boolean hasVideos;

    public static CourseListItem fromJson(String json) {
        Gson gson = new Gson();
        CourseListItem courseListItem = gson.fromJson(json, CourseListItem.class);
        courseListItem.setHrefFromThumb();
        courseListItem.setHasVideosFromFeatures();
        return courseListItem;
    }

    public CourseListItem(String title,String mcn, String sem,boolean hasVideos, String href) {
        this.title = title;
        this.hasVideos = hasVideos;
        this.mcn = mcn;
        this.sem = sem;
        if (!href.contains("https://"))
            href = "https://ocw.mit.edu" + href;
        if (!href.endsWith("/"))
            href += "/";
        this.href = href;
    }

    public CourseListItem(String title, String mcn, String sem,String href, boolean hasVideos,String thumb) {
        this.title = title;
        this.hasVideos = hasVideos;
        this.thumb = thumb;
        this.mcn = mcn;
        this.sem = sem;
        if (!href.contains("https://"))
            href = "https://ocw.mit.edu" + href;
        if (!href.endsWith("/"))
            href += "/";
        this.href = href;
    }

    private void setHrefFromThumb(){
            href = thumb.substring(0,thumb.lastIndexOf("/"));
        if (!href.contains("https://"))
            href = "https://ocw.mit.edu" + href;
        if (!href.endsWith("/"))
            href += "/";
    }

    private void setHasVideosFromFeatures(){
        if(features!=null && !features.isEmpty())
            hasVideos = features.toLowerCase().contains("video");
        else
            hasVideos = false;
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
        if (!href.contains("https://"))
            href = "https://ocw.mit.edu" + href;
        if (!href.endsWith("/"))
            href += "/";
        return href;
    }

    public String getSem() {
        return sem;
    }

}
