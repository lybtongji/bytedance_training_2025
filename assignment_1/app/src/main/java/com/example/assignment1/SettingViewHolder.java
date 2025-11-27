package com.example.assignment1;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SettingViewHolder extends RecyclerView.ViewHolder {
    ImageView ivIcon;
    TextView tvTitle;

    public SettingViewHolder(@NonNull View itemView) {
        super(itemView);
        ivIcon = itemView.findViewById(R.id.setting_icon);
        tvTitle = itemView.findViewById(R.id.setting_title);
    }

    public void bindData(SettingItem settingItem) {
        ivIcon.setImageResource(settingItem.getResId());
        tvTitle.setText(settingItem.getTitle());
    }
}
