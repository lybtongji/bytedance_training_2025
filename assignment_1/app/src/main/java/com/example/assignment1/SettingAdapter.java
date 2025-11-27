package com.example.assignment1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingViewHolder> {
    private List<SettingItem> settingItems;

    public SettingAdapter(List<SettingItem> settingItems) {
        this.settingItems = settingItems;
    }

    @NonNull
    @Override
    public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.setting_item, parent, false);
        return new SettingViewHolder(itemRootView);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
        SettingItem settingItem = settingItems.get(position);
        holder.bindData(settingItem);
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(holder.itemView.getContext(), settingItem.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return settingItems != null ? settingItems.size() : 0;
    }
}
