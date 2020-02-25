package com.github.yashx.mit_ocw.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.yashx.mit_ocw.activity.abstracts.CourseAndDepartmentBaseActivity;
import com.github.yashx.mit_ocw.fragment.DepartmentAllCoursesFragment;
import com.github.yashx.mit_ocw.fragment.DepartmentFeaturedCoursesFragment;
import com.github.yashx.mit_ocw.fragment.DepartmentHomeFragment;
import com.github.yashx.mit_ocw.model.CourseListItem;
import com.github.yashx.mit_ocw.model.TabModel;
import com.google.android.material.tabs.TabLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

//see course activity for explanations not given as most code is same
public class ShowDepartmentActivity extends CourseAndDepartmentBaseActivity {

    private Document doc;
    private ArrayList<CourseListItem> courseListItemArrayList;
    private ArrayList<String> urlList;
    private DepartmentHomeFragment departmentHomeFragment;
    private DepartmentFeaturedCoursesFragment departmentFeaturedCoursesFragment;
    private DepartmentAllCoursesFragment departmentAllCoursesFragment;

    @Override
    public Fragment onTabPressed(TabLayout.Tab tab) {
        switch ((String) tab.getTag()) {
            default:
            case "home":
                if (departmentHomeFragment == null) {
                    departmentHomeFragment = DepartmentHomeFragment.newInstance(doc);
                    departmentHomeFragment.setRetainInstance(true);
                    System.out.println("making");
                }
                return departmentHomeFragment;
            case "Featured Courses":
                if (departmentFeaturedCoursesFragment == null) {
                    departmentFeaturedCoursesFragment = DepartmentFeaturedCoursesFragment.newInstance(urlList);
                    departmentFeaturedCoursesFragment.setRetainInstance(true);
                    System.out.println("making");

                }
                return departmentFeaturedCoursesFragment;
            case "All Courses":
                if (departmentAllCoursesFragment == null) {
                    departmentAllCoursesFragment = DepartmentAllCoursesFragment.newInstance(courseListItemArrayList);
                    departmentAllCoursesFragment.setRetainInstance(true);
                    System.out.println("making");

                }
                return departmentAllCoursesFragment;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ShowDepartmentActivity.JsoupDocumentAsyncLoader().execute(url);
    }

    class JsoupDocumentAsyncLoader extends AsyncTask<String, String, Document> {
        ArrayList<CourseListItem> courseListItems;
        ArrayList<String> urls;

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);

            courseListItemArrayList = courseListItems;
            urlList = urls;

            doc = document;
            //finds department pic absolute url
            String u = document.select("#global_inner > img").first().absUrl("src");
            //finds department name
            String t = document.select("#parent-fieldname-title").first().text();
            ArrayList<TabModel> tabs = new ArrayList<>();

            imageTextTabBarViewModel.getTextTitle().setValue(t);
            imageTextTabBarViewModel.getUrlToImage().setValue(u);

            //for tabs (nothing needs to be loaded as everything is on doc)
            tabs.add(new TabModel().setText("Home").setTag("Home"));
            tabs.add(new TabModel().setText("Featured Courses").setTag("Featured Courses"));
            tabs.add(new TabModel().setText("All Courses").setTag("All Courses"));

            imageTextTabBarViewModel.getAllTabs().setValue(tabs);


        }

        @Override
        protected Document doInBackground(String... strings) {
            //getting doc with jsoup
            Document doc = null;
            try {
                if (!strings[0].endsWith("/"))
                    strings[0] += "/";
                doc = Jsoup.connect(strings[0]).get();
            } catch (Exception e) {
                Log.e("TAG", "doInBackground: ", e);
            }

            //getting All Courses
            Elements eTs = doc.select("#global_inner > div.courseListDiv > ul > li:not(.courseListHeaderRow)");
            if (eTs != null) {
                courseListItems = new ArrayList<>(eTs.size());
                for (Element e : eTs) {
                    String href;
                    final CourseListItem courseListItem;
                    //getting absolute url (absUrl doesn't work as doc is loaded from html)
                    String url = e.selectFirst("a").attr("href");

                    if (!url.contains("https://"))
                        url = "https://ocw.mit.edu" + url;
                    if (!url.endsWith("/"))
                        url += "/";
                    href = url;

                    courseListItem = new CourseListItem(
                            e.attr("data-title"),
                            e.attr("data-courseno"),
                            e.attr("data-semester"),
                            href
                    );

                    courseListItems.add(courseListItem);
                }

            }

            //getting Featured Courses
            eTs = doc.select("#carousel_ul .item");
            if (eTs != null) {
                urls = new ArrayList<>(eTs.size());
                for (Element e : eTs) {
                    //getting absolute url (absUrl doesn't work as doc is loaded from html)
                    String url = e.selectFirst("a").attr("href");
                    if (!url.contains("https://"))
                        url = "https://ocw.mit.edu" + url;
                    if (!url.endsWith("/"))
                        url += "/";
                    int l = url.lastIndexOf("index.htm");
                    if (l != -1) {
                        url = url.substring(0, l) + "index.json";
                    }
                    urls.add(url);
                }
            }

            return doc;
        }
    }
}