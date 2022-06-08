package com.rajesh.gallary.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rajesh.gallary.R;
import com.rajesh.gallary.model.Albums;
import com.rajesh.gallary.model.AlbumsAndMedia;
import com.rajesh.gallary.ui.ViewHolder.AlbumViewHolder;
import com.rajesh.gallary.ui.ViewHolder.ParentMediaViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumViewHolder> {
    private List<AlbumsAndMedia> albums = new ArrayList<>();
    private Context context;

    @Inject
    public AlbumsAdapter(@ApplicationContext Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAlbums(List<AlbumsAndMedia> albums) {
        this.albums = albums;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlbumViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        final AlbumsAndMedia albumsModel = albums.get(position);
        //set image
        Glide.with(context)
                .load(albumsModel.mediaModelList.get(0).getMediaPath())
                .into(holder.albumImage);
        //set name
        holder.album_name.setText(albumsModel.albums.getAlbumName());
        //set Size
        holder.album_size.setText(albumsModel.mediaModelList.size() + " Photos");
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }
}
