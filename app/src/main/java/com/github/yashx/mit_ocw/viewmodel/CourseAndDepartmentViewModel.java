package com.github.yashx.mit_ocw.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.yashx.mit_ocw.model.TabModel;
import com.google.android.material.tabs.TabLayout;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class CourseAndDepartmentViewModel extends ViewModel {
    private MutableLiveData<TabLayout.Tab> selectedTab;
    private MutableLiveData<String> urlToImage, textTitle;
    private MutableLiveData<ArrayList<TabModel>> allTabs;
    private MutableLiveData<Document> doc;

    public CourseAndDepartmentViewModel() {
        selectedTab = new MutableLiveData<>();
        urlToImage = new MutableLiveData<>();
        textTitle = new MutableLiveData<>();
        allTabs = new MutableLiveData<>();
        doc = new MutableLiveData<>();
    }

    public MutableLiveData<Document> getDoc() {
        return doc;
    }

    public MutableLiveData<TabLayout.Tab> getSelectedTab() {
        return selectedTab;
    }

    public MutableLiveData<String> getUrlToImage() {
        return urlToImage;
    }

    public MutableLiveData<String> getTextTitle() {
        return textTitle;
    }

    public MutableLiveData<ArrayList<TabModel>> getAllTabs() {
        return allTabs;
    }
}
