package com.rajesh.gallary.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rajesh.gallary.R;
import com.rajesh.gallary.model.AlbumsAndMedia;
import com.rajesh.gallary.network.onAlbumClicked;
import com.rajesh.gallary.ui.ViewHolder.FolderViewHolder;
import com.rajesh.gallary.ui.ViewHolder.MediaVIewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class folderAdapter extends RecyclerView.Adapter<FolderViewHolder> {
    private List<AlbumsAndMedia> albumsAndMedia = new ArrayList<>();
    private Context context;
    private onAlbumClicked<AlbumsAndMedia> onAlbumClicked;

    @Inject
    public folderAdapter(@ApplicationContext Context context) {
        this.context = context;
    }

    public void setOnAlbumClicked(onAlbumClicked<AlbumsAndMedia> onAlbumClicked) {
        this.onAlbumClicked = onAlbumClicked;
    }

    public void setAlbumsAndMedia(List<AlbumsAndMedia> albumsAndMedia) {
        this.albumsAndMedia = albumsAndMedia;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FolderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        final AlbumsAndMedia data = albumsAndMedia.get(position);
        Glide.with(context)
                .load(data.mediaModelList.get(0).getMediaPath())
                .centerCrop()
                .into(holder.folderImage);

        holder.folderName.setText(data.albums.getAlbumName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAlbumClicked.onAlbumClickedListener(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumsAndMedia.size();
    }
}
