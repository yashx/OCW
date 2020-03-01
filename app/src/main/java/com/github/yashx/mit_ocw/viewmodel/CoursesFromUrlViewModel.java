package com.github.yashx.mit_ocw.viewmodel;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.github.yashx.mit_ocw.model.CourseListItem;

import org.jsoup.Jsoup;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CoursesFromUrlViewModel extends ViewModel {
    private MutableLiveData<ArrayList<CourseListItem>> courses;
    private MutableLiveData<String[]> urlArr;
    private AsyncTask asyncTask;

    public CoursesFromUrlViewModel() {
        courses = new MutableLiveData<>();
        urlArr = new MutableLiveData<>();
        urlArr.observeForever(new Observer<String[]>() {
            @Override
            public void onChanged(String[] urls) {
                if (asyncTask != null)
                    asyncTask.cancel(true);
                asyncTask = new CoursesFromUrlAsyncTask(urls).execute();
            }
        });

    }

    public MutableLiveData<String[]> getUrlArr() {
        return urlArr;
    }

    public MutableLiveData<ArrayList<CourseListItem>> getCourses() {
        return courses;
    }

    class CoursesFromUrlAsyncTask extends AsyncTask<Void, Void, ArrayList<CourseListItem>> {

        String[] urls;

        public CoursesFromUrlAsyncTask(String[] urls) {
            this.urls = urls;
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
                for (int i = 0; i < urls.length; i++) {
                    String url = urls[i];
                    if (url.isEmpty())
                        continue;
                    if (!url.contains("https://"))
                        url = "https://ocw.mit.edu" + url;
                    if (!url.endsWith("/"))
                        url += "/";
                    url = url + "index.json";
                    String json = Jsoup.connect(url).ignoreContentType(true).execute().body();
                    courseListItems.add(CourseListItem.fromCourseJson(json));
                    Log.e(TAG, "doInBackground: " + json);
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
