package com.github.yashx.mit_ocw.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.yashx.mit_ocw.model.TabModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ImageTextTabBarViewModel extends ViewModel {
    private MutableLiveData<TabLayout.Tab> selectedTab;
    private MutableLiveData<String> urlToImage, textTitle;
    private MutableLiveData<ArrayList<TabModel>> allTabs;

    public ImageTextTabBarViewModel() {
        selectedTab = new MutableLiveData<>();
        urlToImage = new MutableLiveData<>();
        textTitle = new MutableLiveData<>();
        allTabs = new MutableLiveData<>();
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
