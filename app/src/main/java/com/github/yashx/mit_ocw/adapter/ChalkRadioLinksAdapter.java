package com.github.yashx.mit_ocw.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.yashx.mit_ocw.R;

public class ChalkRadioLinksAdapter extends RecyclerView.Adapter<ChalkRadioLinksAdapter.ChalkRadioLinkViewholder> {
    class ChalkRadioLinkViewholder extends RecyclerView.ViewHolder {
        ImageView platformLogo;
        TextView platformName;

        public ChalkRadioLinkViewholder(@NonNull View itemView) {
            super(itemView);
            platformLogo = itemView.findViewById(R.id.imageViewChalkRadioLinkListItem);
            platformName = itemView.findViewById(R.id.textViewChalkRadioLinkListItem);
        }
    }

    private String[] platforms;
    private String[] platformUrls;

    public ChalkRadioLinksAdapter(String[] platforms, String[] platformUrls) {
        this.platforms = platforms;
        this.platformUrls = platformUrls;
    }

    @Override
    public int getItemCount() {
        return platforms.length;
    }

    @NonNull
    @Override
    public ChalkRadioLinkViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChalkRadioLinkViewholder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_chalk_radio_link, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChalkRadioLinkViewholder holder, int position) {
        holder.platformName.setText(platforms[position]);

        int id = holder.itemView.getContext().getResources().getIdentifier(platforms[position]
                        .replace(" ", "_").toLowerCase(), "drawable"
                , holder.itemView.getContext().getPackageName());
        holder.platformLogo.setImageResource(id);

        final String url = platformUrls[position];
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                v.getContext().startActivity(i);
            }
        });
    }

}
