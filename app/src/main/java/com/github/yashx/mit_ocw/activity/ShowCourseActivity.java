package com.github.yashx.mit_ocw.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.yashx.mit_ocw.activity.abstracts.CourseAndDepartmentBaseActivity;
import com.github.yashx.mit_ocw.fragment.CourseHomeFragment;
import com.github.yashx.mit_ocw.fragment.HtmlRendererCourseFragment;
import com.github.yashx.mit_ocw.model.TabModel;
import com.google.android.material.tabs.TabLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class ShowCourseActivity extends CourseAndDepartmentBaseActivity {

    private Document doc;

    @Override
    public Fragment onTabPressed(TabLayout.Tab tab) {
        //deciding which fragment to replace with depending on tag
        //for home
        if ((tab.getTag()).equals("home"))
            return CourseHomeFragment.newInstance(doc);
            //for everything else
        else
            return HtmlRendererCourseFragment.newInstance((String) tab.getTag());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new JsoupDocumentAsyncLoader().execute(url);
    }

    class JsoupDocumentAsyncLoader extends AsyncTask<String, String, Document> {
        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);

            doc = document;

            //finding absolute url of course picture
            String u = document.select("#chpImage > div.image > img").first().absUrl("src");
            //finding course title
            String t = document.select("#course_title > h1").first().text();

            imageTextTabBarViewModel.getTextTitle().setValue(t);
            imageTextTabBarViewModel.getUrlToImage().setValue(u);


            //for side links (not combined as I want to store in bundle)
            ArrayList<TabModel> tabs = new ArrayList<>();

            int i = 0;
            //selecting all side links
            for (Element e : document.select("#course_nav > ul > li")) {
                //if the side link has sub links the selector changes
                Element el = e.selectFirst(".tlp_links");
                String selector = el == null ? "a" : "a:nth-child(2)";

                if (e.selectFirst(selector) != null && !e.selectFirst(selector).text().isEmpty()) {
                    //not storing first url from first link as it is already loaded and current doc can be reused
                    if (i == 0) {
                        i++;
                        tabs.add(((new TabModel()
                                .setText((e.selectFirst("a").text().trim()))
                                .setTag("home"))));
                    } else {
                        //storing url for other links except Invigilator Insights and Downloads (TODO)
                        String s = e.selectFirst(selector).text().trim().toLowerCase();
                        if (!(s.contains("insight") || s.contains("download"))) {
                            tabs.add(new TabModel()
                                    .setText(s)
                                    .setTag(e.selectFirst(selector).absUrl("href")));
                        }
                    }
                }
            }

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
            return doc;
        }
    }
}
