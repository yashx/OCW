package com.github.yashx.mit_ocw.viewmodel;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.yashx.mit_ocw.model.CourseListItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class CoursesFromLiTagViewModel extends ViewModel {

    private AsyncTask asyncTask;

    private MutableLiveData<ArrayList<CourseListItem>> courses;

    public CoursesFromLiTagViewModel(String urlToLoad, String selectorToLi) {
        asyncTask = new PopularCoursesFromLiTagAsyncTask(urlToLoad, selectorToLi).execute();

    }

    public LiveData<ArrayList<CourseListItem>> getCourses() {
        if (courses == null)
            courses = new MutableLiveData<>();
        return courses;
    }

    private class PopularCoursesFromLiTagAsyncTask extends AsyncTask<Void, ArrayList<CourseListItem>, ArrayList<CourseListItem>> {
        private String urlToLoad;
        private String selectorToLiTag;

        PopularCoursesFromLiTagAsyncTask(String urlToLoad, String selectorToLiTag) {
            this.urlToLoad = urlToLoad;
            this.selectorToLiTag = selectorToLiTag;
        }

        @Override
        protected void onPostExecute(ArrayList<CourseListItem> courseListItems) {
            super.onPostExecute(courseListItems);
            courses.setValue(courseListItems);
        }

        //        @Override
//        protected void onProgressUpdate(ArrayList<CourseListItem>... values) {
//            super.onProgressUpdate(values);
//            courses.setValue(values[0]);
//        }

        @Override
        protected ArrayList<CourseListItem> doInBackground(Void... voids) {
            ArrayList<CourseListItem> courseListItems = new ArrayList<>();
            try {
                Document doc = Jsoup.connect(urlToLoad).get();
                Elements eTs = doc.select(selectorToLiTag);
                for (int i = 0; i < eTs.size(); i++) {
                    String url = eTs.get(i).absUrl("href");
                    if (!url.contains("https://"))
                        url = "https://ocw.mit.edu" + url;
                    if (!url.endsWith("/"))
                        url += "/";

                    CourseListItem courseListItem = new CourseListItem(
                            eTs.get(i).attr("data-title"),
                            eTs.get(i).attr("data-courseno"),
                            eTs.get(i).attr("data-semester"),
                            url
                    );
                    courseListItems.add(courseListItem);
//                    publishProgress(courseListItems);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < courseListItems.size(); i++) {
                for (int j = 0; j < courseListItems.size() - i - 1; j++)
                    if (courseListItems.get(j).getTitle().toLowerCase()
                            .compareTo(courseListItems.get(j+1).getTitle().toLowerCase()) > 0) {
                        CourseListItem temp = courseListItems.get(j);
                        courseListItems.set(j, courseListItems.get(j+1));
                        courseListItems.set(j+1, temp);
                    }
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
