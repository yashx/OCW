package com.github.yashx.mit_ocw.activity;

import android.view.Menu;

import androidx.fragment.app.Fragment;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.activity.abstracts.CourseAndDepartmentBaseActivity;
import com.github.yashx.mit_ocw.fragment.DepartmentAllCoursesFragment;
import com.github.yashx.mit_ocw.fragment.DepartmentFeaturedCoursesFragment;
import com.github.yashx.mit_ocw.fragment.DepartmentHomeFragment;
import com.github.yashx.mit_ocw.model.CourseListItem;
import com.github.yashx.mit_ocw.model.TabModel;
import com.google.android.material.tabs.TabLayout;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

//see course activity for explanations not given as most code is same
public class ShowDepartmentActivity extends CourseAndDepartmentBaseActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean b = super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.bookmarkToggleMenuItem).setVisible(false);
        return b;
    }

    @Override
    public Fragment onTabPressed(TabLayout.Tab tab) {
        switch ((String) tab.getTag()) {
            default:
            case "home":
                return new DepartmentHomeFragment();
            case "Featured Courses":
                return new DepartmentFeaturedCoursesFragment();
            case "All Courses":
                return new DepartmentAllCoursesFragment();
        }
    }

    @Override
    protected void onPageLoaded(Document doc) {

        //finds department pic absolute url
        setImageUrl(doc.select("#global_inner > img").first().absUrl("src"));
        //finds department name
        setTextTitle(doc.select("#parent-fieldname-title").first().text());


        ArrayList<TabModel> tabs = new ArrayList<>();
        //for tabs (nothing needs to be loaded as everything is on doc)
        tabs.add(new TabModel().setText("Home").setTag("Home"));
        tabs.add(new TabModel().setText("Featured Courses").setTag("Featured Courses"));
        tabs.add(new TabModel().setText("All Courses").setTag("All Courses"));

        setTabs(tabs);
    }
}