package com.github.yashx.mit_ocw.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CoursesFromMultipleDepartmentJsonWithSearchViewModelFactory implements ViewModelProvider.Factory {
    String[] urlToLoad;

    public CoursesFromMultipleDepartmentJsonWithSearchViewModelFactory(String[] urlToLoad) {
        this.urlToLoad = urlToLoad;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CoursesFromMultipleDepartmentJsonWithSearchViewModel(urlToLoad);
    }
}
