package com.rajesh.gallary.Adapter.allPicsAndVideos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajesh.gallary.R;
import com.rajesh.gallary.model.DateAndMedia;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.network.onItemClickListener;
import com.rajesh.gallary.ui.ViewHolder.ParentMediaViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class DateAndMediaAdapter extends RecyclerView.Adapter<ParentMediaViewHolder> {
    private static final String TAG = "DateAndMediaAdapter";
    private List<DateAndMedia> mediaAndDateList = new ArrayList<>();
    private Context context;
    RecyclerView.RecycledViewPool viewPool;
    private onItemClickListener<mediaModel> onItemClickListener;

    @Inject
    public DateAndMediaAdapter(@ApplicationContext Context context) {
        // Do things with the list
        this.context = context;
        viewPool = new RecyclerView.RecycledViewPool();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMediaAndDateList(List<DateAndMedia> mediaAndDateList) {
        this.mediaAndDateList = mediaAndDateList;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(com.rajesh.gallary.network.onItemClickListener<mediaModel> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ParentMediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ParentMediaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_media_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ParentMediaViewHolder holder, int position) {
        final DateAndMedia dateAndMedia = mediaAndDateList.get(position);
        MediaAdapter mediaAdapter = new MediaAdapter(dateAndMedia.mediaModelList,holder.ChildRecycler.getContext());
        mediaAdapter.setOnItemClickListener(onItemClickListener);
        mediaAdapter.setHasStableIds(true);
        viewPool.setMaxRecycledViews(R.layout.media_item,dateAndMedia.mediaModelList.size());
        holder.ChildRecycler.setRecycledViewPool(viewPool);
        holder.ChildRecycler.setItemViewCacheSize(dateAndMedia.mediaModelList.size());
//        holder.ChildRecycler.getRecycledViewPool().setMaxRecycledViews(R.layout.media_item,dateAndMedia.mediaModelList.size());
        holder.ChildRecycler.setAdapter(mediaAdapter);
        holder.ChildRecycler.setHasFixedSize(true);
        holder.Date.setText(new SimpleDateFormat("dd MMM ,YYYY").format(TimeUnit.DAYS.toMillis(dateAndMedia.date.getDate())));
    }

    @Override
    public int getItemCount() {
        return mediaAndDateList.size();
    }
}
