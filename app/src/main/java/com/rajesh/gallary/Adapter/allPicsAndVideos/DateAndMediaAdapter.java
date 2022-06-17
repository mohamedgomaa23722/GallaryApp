package com.rajesh.gallary.Adapter.allPicsAndVideos;

import static com.rajesh.gallary.common.Constant.NEW_FILTER_DATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rajesh.gallary.R;
import com.rajesh.gallary.model.DateAndMedia;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.network.onItemClickListener;
import com.rajesh.gallary.ui.ViewHolder.ParentMediaViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class DateAndMediaAdapter extends RecyclerView.Adapter<ParentMediaViewHolder> {
    private static final String TAG = "DateAndMediaAdapter";
    private List<DateAndMedia> mediaAndDateList = new ArrayList<>();
    private Context context;
    RecyclerView.RecycledViewPool viewPool;
    private onItemClickListener<mediaModel> onItemClickListener;
    //Filter Data
    private int ViewType = 0;
    private int GridCount = 4;

    public void setViewType(int viewType) {
        ViewType = viewType;
        notifyDataSetChanged();
    }

    @Inject
    public DateAndMediaAdapter(@ApplicationContext Context context) {
        // Do things with the list
        this.context = context;
        viewPool = new RecyclerView.RecycledViewPool();
    }



    public void setGridCount(int gridCount) {
        GridCount = gridCount;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void SetDateFilter(String Filter) {
        if (Filter.equals(NEW_FILTER_DATE))
            Collections.sort(this.mediaAndDateList, (t1, t2) -> Long.compare(t2.date.getRealDate(), t1.date.getRealDate()));
        else
            Collections.reverse(mediaAndDateList);
        notifyDataSetChanged();
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
        if (ViewType == 0) {
            //show as grid
            GridLayoutManager manager = new GridLayoutManager(context, GridCount);
            holder.ChildRecycler.setLayoutManager(manager);
        } else {
            //show as list
            LinearLayoutManager manager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            holder.ChildRecycler.setLayoutManager(manager);
        }
        MediaAdapter mediaAdapter = new MediaAdapter(holder.ChildRecycler.getContext());
        mediaAdapter.setMediaModelList(dateAndMedia.mediaModelList);
        mediaAdapter.setGridCount(GridCount);
        mediaAdapter.setOnItemClickListener(onItemClickListener);
        mediaAdapter.setHasStableIds(true);
        mediaAdapter.setViewType(ViewType);
        viewPool.setMaxRecycledViews(R.layout.media_grid_item, dateAndMedia.mediaModelList.size());
        holder.ChildRecycler.setRecycledViewPool(viewPool);
        holder.ChildRecycler.setItemViewCacheSize(dateAndMedia.mediaModelList.size());
//        holder.ChildRecycler.getRecycledViewPool().setMaxRecycledViews(R.layout.media_item,dateAndMedia.mediaModelList.size());
        holder.ChildRecycler.setAdapter(mediaAdapter);
        holder.ChildRecycler.setHasFixedSize(true);
        //Check the item data
        holder.Date.setText(dateAndMedia.date.getDate());
    }

    @Override
    public int getItemCount() {
        return mediaAndDateList.size();
    }
}
