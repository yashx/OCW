package com.github.yashx.mit_ocw.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.util.ViewBuilders;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageTextTabBarFragment extends Fragment {

    private Context context;
    private Callbacks callbacks;

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

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
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String imageUrl;
        String titleText;
        TabLayout tabLayout;
        tabLayout = view.findViewById(R.id.tabLayoutImageTextTabBarFragment);

        imageUrl = getArguments().getString("imageUrl");
        titleText = getArguments().getString("titleText");
        ArrayList<String> tabNames = getArguments().getStringArrayList("tabNames");
        ArrayList<String> tabTags = getArguments().getStringArrayList("tabTags");

        for (int i = 0; i < tabNames.size(); i++)
            tabLayout.addTab(tabLayout.newTab().setText(tabNames.get(i)).setTag(tabTags.get(i)));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                callbacks.onTabPressed(tab.getTag());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        ImageView imageView = view.findViewById(R.id.imageViewImageTextTabBarFragment);
        imageView.setMinimumHeight((int) (Resources.getSystem().getDisplayMetrics().heightPixels * 0.4));
        imageView.setMinimumWidth(Resources.getSystem().getDisplayMetrics().widthPixels);
        Picasso.get().load(imageUrl).into(imageView);

        TextView textView = view.findViewById(R.id.textViewImageTextTabBarFragment);
        textView.setText(titleText);
    }

    public interface Callbacks {
        void onTabPressed(Object tabTag);
    }
}
