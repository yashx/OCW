package com.github.yashx.mit_ocw.viewmodel;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.github.yashx.mit_ocw.model.CourseListItem;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class CoursesFromMultipleDepartmentJsonWithSearchViewModel extends ViewModel {

    private AsyncTask asyncTask;

    private MutableLiveData<ArrayList<CourseListItem>> filteredCourses;
    private MutableLiveData<String> textQuery;
    private ArrayList<CourseListItem> allCourses;
    private Observer<String> textQueryObserver;

    public CoursesFromMultipleDepartmentJsonWithSearchViewModel(String[] urlToLoad) {
        asyncTask = new PopularCoursesFromMultipleDepartmentJsonAsyncTask(urlToLoad).execute();


        textQueryObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.isEmpty())
                    filteredCourses.setValue(allCourses);
                else {
                    ArrayList<CourseListItem> courseListItems = new ArrayList<>();
                    for (CourseListItem c : allCourses) {
                        if (c.getTitle().toLowerCase().contains(s.toLowerCase())
                                || c.getMcn().toLowerCase().contains(s.toLowerCase())
                                || c.getSem().toLowerCase().contains(s.toLowerCase()))
                            courseListItems.add(c);
                    }
                    filteredCourses.setValue(courseListItems);
                }
            }
        };
        textQuery = new MutableLiveData<>();
        textQuery.observeForever(textQueryObserver);
        filteredCourses = new MutableLiveData<>();
    }

    public LiveData<ArrayList<CourseListItem>> getFilteredCourses() {
        return filteredCourses;
    }

    public MutableLiveData<String> getTextQuery() {
        return textQuery;
    }

    private class PopularCoursesFromMultipleDepartmentJsonAsyncTask extends AsyncTask<Void
            , ArrayList<CourseListItem>, ArrayList<CourseListItem>> {
        private String[] urlToLoad;

        PopularCoursesFromMultipleDepartmentJsonAsyncTask(String[] urlToLoad) {
            this.urlToLoad = urlToLoad;
        }

        @Override
        protected void onPostExecute(ArrayList<CourseListItem> courseListItems) {
            super.onPostExecute(courseListItems);
            allCourses = courseListItems;
            filteredCourses.setValue(courseListItems);
        }

        @Override
        protected ArrayList<CourseListItem> doInBackground(Void... voids) {
            ArrayList<CourseListItem> courseListItems = new ArrayList<>();
            try {
                Gson gson = new Gson();
                for (int i = 0; i < urlToLoad.length; i++) {
                    String json = Jsoup.connect(urlToLoad[i]).ignoreContentType(true).execute().body();
                    JsonArray jA = gson.fromJson(json,JsonArray.class);
                    for(JsonElement jO:jA) {
                        courseListItems.add(CourseListItem.fromJsonElement(jO));
                        System.out.println(jO.toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Sorting Started");
            Collections.sort(courseListItems, new Comparator<CourseListItem>() {
                @Override
                public int compare(CourseListItem o1, CourseListItem o2) {
                    return o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
                }
            });
//            for (int i = 0; i < courseListItems.size(); i++) {
//                for (int j = 0; j < courseListItems.size() - i - 1; j++)
//                    if (courseListItems.get(j).getTitle().toLowerCase()
//                            .compareTo(courseListItems.get(j + 1).getTitle().toLowerCase()) > 0) {
//                        CourseListItem temp = courseListItems.get(j);
//                        courseListItems.set(j, courseListItems.get(j + 1));
//                        courseListItems.set(j + 1, temp);
//                    }
//            }
            System.out.println("Sorting Done");
            return (courseListItems);

        }
    }

    @Override
    protected void onCleared() {
        asyncTask.cancel(true);
        textQuery.removeObserver(textQueryObserver);
        super.onCleared();
    }


}
