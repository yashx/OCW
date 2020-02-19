package com.github.yashx.mit_ocw.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.yashx.mit_ocw.R;

import org.jsoup.nodes.Document;

public class CourseHomeFragment extends Fragment {

    private Document doc;

    public CourseHomeFragment(Document doc) {
        this.doc = doc;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coursehome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView courseDesc = view.findViewById(R.id.courseDescText);
        courseDesc.setText(doc.selectFirst("#description > div > p").text().trim());
    }
}
