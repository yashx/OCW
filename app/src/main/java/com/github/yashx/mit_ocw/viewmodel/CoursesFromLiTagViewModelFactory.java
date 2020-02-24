package com.github.yashx.mit_ocw.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CoursesFromLiTagViewModelFactory implements ViewModelProvider.Factory {
    String urlToLoad;
    String selectorToLiTag;

    public CoursesFromLiTagViewModelFactory(String urlToLoad, String selectorToLiTag) {
        this.urlToLoad = urlToLoad;
        this.selectorToLiTag = selectorToLiTag;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CoursesFromLiTagViewModel(urlToLoad, selectorToLiTag);
    }
}
