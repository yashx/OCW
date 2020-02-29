package com.github.yashx.mit_ocw.viewmodel;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.github.yashx.mit_ocw.model.CourseListItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class CoursesFromLiTagWithSearchViewModel extends ViewModel {

    private AsyncTask asyncTask;

    private MutableLiveData<ArrayList<CourseListItem>> filteredCourses;
    private MutableLiveData<String> textQuery;
    private ArrayList<CourseListItem> allCourses;
    private Observer<String> textQueryObserver;

    public CoursesFromLiTagWithSearchViewModel(String urlToLoad, String selectorToLi) {
        asyncTask = new PopularCoursesFromLiTagAsyncTask(urlToLoad, selectorToLi).execute();


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

    private class PopularCoursesFromLiTagAsyncTask extends AsyncTask<Void
            , ArrayList<CourseListItem>, ArrayList<CourseListItem>> {
        private String urlToLoad;
        private String selectorToLiTag;

        PopularCoursesFromLiTagAsyncTask(String urlToLoad, String selectorToLiTag) {
            this.urlToLoad = urlToLoad;
            this.selectorToLiTag = selectorToLiTag;
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
                Document doc = Jsoup.connect(urlToLoad).get();
                Elements eTs = doc.select(selectorToLiTag);
                for (int i = 0; i < eTs.size(); i++) {
                    String url = eTs.get(i).selectFirst("a").absUrl("href");
                    if (!url.contains("https://"))
                        url = "https://ocw.mit.edu" + url;
                    if (!url.endsWith("/"))
                        url += "/";
                    boolean hasVideos = (eTs.get(i).attr("data-other_video")
                            .equalsIgnoreCase("true")
                            || eTs.get(i).attr("data-complete_video")
                            .equalsIgnoreCase("true"));

                    CourseListItem courseListItem = new CourseListItem(
                            eTs.get(i).attr("data-title"),
                            eTs.get(i).attr("data-courseno"),
                            eTs.get(i).attr("data-semester"),
                            hasVideos,
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
                            .compareTo(courseListItems.get(j + 1).getTitle().toLowerCase()) > 0) {
                        CourseListItem temp = courseListItems.get(j);
                        courseListItems.set(j, courseListItems.get(j + 1));
                        courseListItems.set(j + 1, temp);
                    }
            }
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
