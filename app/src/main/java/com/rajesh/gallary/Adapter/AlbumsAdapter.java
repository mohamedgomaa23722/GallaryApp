package com.rajesh.gallary.Adapter;

import static com.rajesh.gallary.common.Constant.NEW_FILTER_DATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rajesh.gallary.R;
import com.rajesh.gallary.model.Albums;
import com.rajesh.gallary.model.AlbumsAndMedia;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.network.onAlbumClicked;
import com.rajesh.gallary.ui.ViewHolder.AlbumViewHolder;
import com.rajesh.gallary.ui.ViewHolder.ParentMediaViewHolder;
import com.rajesh.gallary.utils.AutoFitRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumViewHolder> {
    private List<AlbumsAndMedia> albums = new ArrayList<>();
    private Context context;
    private onAlbumClicked<String> albumClicked;
    private AutoFitRecyclerView autoFitRecyclerView;
    private int ViewType = 0;
    private int GridCount = 3;
    private boolean sorted = false;

    public void sortByName() {
        Collections.sort(this.albums, (t1, t2) ->
                t1.albums.getAlbumName().compareTo(t2.albums.getAlbumName()));
        notifyDataSetChanged();
    }

    public void SortBySize() {
        Collections.sort(this.albums, (t1, t2) -> Integer.compare(t2.mediaModelList.size(), t1.mediaModelList.size()));
        notifyDataSetChanged();
    }

    @Inject
    public AlbumsAdapter(@ApplicationContext Context context) {
        this.context = context;
        autoFitRecyclerView = new AutoFitRecyclerView(context);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAlbums(List<AlbumsAndMedia> albums) {
        this.albums = albums;
        this.notifyDataSetChanged();
    }

    public void setAlbumClicked(onAlbumClicked<String> albumClicked) {
        this.albumClicked = albumClicked;
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
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(autoFitRecyclerView.calculateNoOfColumns(GridCount), autoFitRecyclerView.calculateNoOfColumns(GridCount));
        holder.itemView.setLayoutParams(layoutParams);
        if (albumsModel.mediaModelList.size() > 0){
        Glide.with(context)
                .load(albumsModel.mediaModelList.get(0).getMediaPath())
                .into(holder.albumImage);
        }
        //set name
        holder.album_name.setText(albumsModel.albums.getAlbumName());
        //set Size
        holder.album_size.setText(albumsModel.mediaModelList.size() + " Photos");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumClicked.onAlbumClickedListener(albumsModel.albums.getAlbumID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }


    public void SortMediaByDate(String FilterTYPE) {
        if (FilterTYPE.equals(NEW_FILTER_DATE)) {
            sorted = true;
        } else {
            sorted = false;
        }
        Collections.reverse(albums);
        notifyDataSetChanged();
    }

    public void setGridCount(int gridCount) {
        GridCount = gridCount;
    }
}
