package com.github.yashx.mit_ocw.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.activity.ShowDepartmentActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class DepartmentListRecyclerAdapter extends RecyclerView.Adapter<DepartmentListRecyclerAdapter.DepartmentListViewHolder> {
    class DepartmentListViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        DepartmentListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewDepartmentListItem);
        }
    }

    private JsonArray jsonArray;

    public DepartmentListRecyclerAdapter(JsonArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    @Override
    public int getItemCount() {
        return jsonArray.size();
    }

    @NonNull
    @Override
    public DepartmentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DepartmentListViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_department, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentListViewHolder holder, int position) {
        final JsonObject jsonObject = jsonArray.get(position).getAsJsonObject();
        holder.textView.setText(jsonObject.get("name").toString().replace("\"",""));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ShowDepartmentActivity.class);
                i.putExtra(v.getContext().getResources().getString(R.string.urlExtra),
                        jsonObject.get("url").toString().replace("\"",""));
                v.getContext().startActivity(i);
            }
        });
    }
}
