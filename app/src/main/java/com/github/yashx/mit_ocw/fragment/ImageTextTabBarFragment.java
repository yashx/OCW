package com.github.yashx.mit_ocw.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.model.TabModel;
import com.github.yashx.mit_ocw.viewmodel.CourseAndDepartmentViewModel;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageTextTabBarFragment extends Fragment {

    public static ImageTextTabBarFragment newInstance(String imageUrl, String titleText
            , ArrayList<String> tabNames, ArrayList<String> tabTags) {

        Bundle args = new Bundle();
        args.putString("imageUrl", imageUrl);
        args.putString("titleText", titleText);
        args.putStringArrayList("tabNames", tabNames);
        args.putStringArrayList("tabTags", tabTags);
        ImageTextTabBarFragment fragment = new ImageTextTabBarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_imagetexttabbar, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TabLayout tabLayout;
        tabLayout = view.findViewById(R.id.tabLayoutImageTextTabBarFragment);

        final CourseAndDepartmentViewModel courseAndDepartmentViewModel = new ViewModelProvider(requireActivity())
                .get(CourseAndDepartmentViewModel.class);

        courseAndDepartmentViewModel.getAllTabs().observe(getViewLifecycleOwner(), new Observer<ArrayList<TabModel>>() {
            @Override
            public void onChanged(ArrayList<TabModel> tabs) {
                tabLayout.removeAllTabs();
                for (int i = 0; i < tabs.size(); i++)
                    tabLayout.addTab(tabs.get(i).tabModelToTab(tabLayout));
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                courseAndDepartmentViewModel.getSelectedTab().setValue(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        final ImageView imageView = view.findViewById(R.id.imageViewImageTextTabBarFragment);
        imageView.setMinimumHeight((int) (Resources.getSystem().getDisplayMetrics().heightPixels * 0.4));
        imageView.setMinimumWidth(Resources.getSystem().getDisplayMetrics().widthPixels);
        courseAndDepartmentViewModel.getUrlToImage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Picasso.get().load(s).into(imageView);
            }
        });


        final TextView textView = view.findViewById(R.id.textViewImageTextTabBarFragment);
        courseAndDepartmentViewModel.getTextTitle().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText(s);
            }
        });
    }
}
