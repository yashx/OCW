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

public class CoursesViewModel extends ViewModel {
    private MutableLiveData<ArrayList<CourseListItem>> courses;
    private AsyncTask asyncTask;

    public CoursesViewModel(String urlToLoad, String selectorToAnchorTag) {
        asyncTask = new PopularCoursesAsyncTask(urlToLoad,selectorToAnchorTag).execute();
    }

    public LiveData<ArrayList<CourseListItem>> getCourses() {
        if (courses == null) {
            courses = new MutableLiveData<>();
        }
        return courses;
    }

    private class PopularCoursesAsyncTask extends AsyncTask<Void, Void, ArrayList<CourseListItem>> {
        private String urlToLoad;
        private String selectorToAnchorTag;

        PopularCoursesAsyncTask(String urlToLoad, String selectorToAnchorTag) {
            this.urlToLoad = urlToLoad;
            this.selectorToAnchorTag = selectorToAnchorTag;
        }

        @Override
        protected void onPostExecute(ArrayList<CourseListItem> courseListItems) {
            super.onPostExecute(courseListItems);
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

                    Log.e(TAG, "doInBackground: " + url);
                    String json = Jsoup.connect(url).ignoreContentType(true).execute().body();
                    Log.e(TAG, "doInBackground: " + json);
                    courseListItems.add(CourseListItem.fromJson(json));
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