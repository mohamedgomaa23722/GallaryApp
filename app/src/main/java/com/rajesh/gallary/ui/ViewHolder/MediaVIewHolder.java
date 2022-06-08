package com.rajesh.gallary.ui.ViewHolder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajesh.gallary.R;

public class MediaVIewHolder extends RecyclerView.ViewHolder {
    public ImageView Picture,Video;
    public MediaVIewHolder(@NonNull View itemView) {
        super(itemView);
        Picture =itemView.findViewById(R.id.Picture);
        Video =itemView.findViewById(R.id.VideoPlayer);

    }
}
