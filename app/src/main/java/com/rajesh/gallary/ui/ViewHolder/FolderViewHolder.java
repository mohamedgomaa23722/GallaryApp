package com.rajesh.gallary.ui.ViewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajesh.gallary.R;

public class FolderViewHolder extends RecyclerView.ViewHolder {
    public ImageView folderImage;
    public TextView folderName;

    public FolderViewHolder(@NonNull View itemView) {
        super(itemView);
        folderImage = itemView.findViewById(R.id.FolderImage);
        folderName = itemView.findViewById(R.id.FolderName);
    }
}
