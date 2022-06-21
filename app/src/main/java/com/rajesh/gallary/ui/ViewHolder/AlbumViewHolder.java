package com.rajesh.gallary.ui.ViewHolder;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajesh.gallary.R;

public class AlbumViewHolder extends RecyclerView.ViewHolder {
    public TextView album_name, album_size;
    public ImageView albumImage,SelectedAlbum;
    public View selectedAlbumBack;
    public AlbumViewHolder(@NonNull View itemView) {
        super(itemView);
        album_name = itemView.findViewById(R.id.album_Name);
        album_size = itemView.findViewById(R.id.album_size);
        albumImage = itemView.findViewById(R.id.album_Image);
        selectedAlbumBack = itemView.findViewById(R.id.selectedAlbumBack);
        SelectedAlbum = itemView.findViewById(R.id.SelectedAlbum);
    }
}
