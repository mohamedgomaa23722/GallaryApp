package com.rajesh.gallary.Adapter.allPicsAndVideos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.rajesh.gallary.R;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.network.onItemClickListener;
import com.rajesh.gallary.ui.ViewHolder.MediaVIewHolder;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class MediaAdapter extends RecyclerView.Adapter<MediaVIewHolder> {

    private List<mediaModel> mediaModelList;
    private Context context;
    private onItemClickListener<mediaModel> onItemClickListener;

    public MediaAdapter(List<mediaModel> mediaModelList, Context context) {
        this.mediaModelList = mediaModelList;
//        Collections.sort(mediaModelList, new Comparator<mediaModel>() {
//            @Override
//            public int compare(mediaModel t1, mediaModel t2) {
//                return Long.compare( TimeUnit.DAYS.toMillis(t2.getMediaDate()), TimeUnit.DAYS.toMillis(t1.getMediaDate()));
//            }
//        });
        this.context = context;
    }

    public void setOnItemClickListener(onItemClickListener<mediaModel> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setMediaModelList(List<mediaModel> mediaModelList) {
        this.mediaModelList = mediaModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MediaVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MediaVIewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.media_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MediaVIewHolder holder, int position) {
        final mediaModel image = mediaModelList.get(position);

        Glide.with(context)
                .load(image.getMediaPath())
                .into(holder.Picture);
        if (!image.isImage())
            holder.Video.setVisibility(View.VISIBLE);
        else
            holder.Video.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(view -> {
            onItemClickListener.onClickedItem(image);
        });

    }

    @Override
    public int getItemCount() {
        return mediaModelList.size();
    }
}
