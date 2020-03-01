package com.github.yashx.mit_ocw.viewmodel;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.yashx.mit_ocw.model.CourseListItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CoursesFromCourseJsonViewModel extends ViewModel {
    private MutableLiveData<ArrayList<CourseListItem>> courses;
    private AsyncTask asyncTask;

    public CoursesFromCourseJsonViewModel(String urlToLoad, String selectorToAnchorTag) {
        asyncTask = new CoursesFromJsonAsyncTask(urlToLoad,selectorToAnchorTag).execute();
    }

    public LiveData<ArrayList<CourseListItem>> getCourses() {
        if (courses == null) {
            courses = new MutableLiveData<>();
        }
        return courses;
    }

    private class CoursesFromJsonAsyncTask extends AsyncTask<Void, ArrayList<CourseListItem>, ArrayList<CourseListItem>> {
        private String urlToLoad;
        private String selectorToAnchorTag;

        CoursesFromJsonAsyncTask(String urlToLoad, String selectorToAnchorTag) {
            this.urlToLoad = urlToLoad;
            this.selectorToAnchorTag = selectorToAnchorTag;
        }

        @Override
        protected void onPostExecute(ArrayList<CourseListItem> courseListItems) {
            courses.setValue(courseListItems);
        }


        @Override
        protected ArrayList<CourseListItem> doInBackground(Void... voids) {
            ArrayList<CourseListItem> courseListItems = new ArrayList<>();
            try {
                Document doc = Jsoup.connect(urlToLoad).get();
                Elements eTs = doc.select(selectorToAnchorTag);
                for (int i = 0; i < eTs.size(); i++) {
                    String url = eTs.get(i).absUrl("href");
                    if (!url.contains("https://"))
                        url = "https://ocw.mit.edu" + url;
                    if (!url.endsWith("/"))
                        url += "/";
                    url = url + "index.json";

                    String json = Jsoup.connect(url).ignoreContentType(true).execute().body();
                    courseListItems.add(CourseListItem.fromCourseJson(json));
                    Log.e(TAG, "doInBackground: "+json );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return (courseListItems);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        asyncTask.cancel(true);
    }
}