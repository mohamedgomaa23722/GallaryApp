package com.rajesh.gallary.ui.ViewHolder;

import android.content.Context;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajesh.gallary.R;

public class MediaVIewHolder extends RecyclerView.ViewHolder {
    public ImageView Picture, Video, SelectedImage;
    public TextView Picture_Name;
    public View selectedBack;

    public MediaVIewHolder(@NonNull View itemView) {
        super(itemView);
        Picture = itemView.findViewById(R.id.Picture);
        Video = itemView.findViewById(R.id.VideoPlayer);
        SelectedImage = itemView.findViewById(R.id.SelectedImage);
        Picture_Name = itemView.findViewById(R.id.Picture_Name);
        selectedBack = itemView.findViewById(R.id.selectedBack);
    }


}

