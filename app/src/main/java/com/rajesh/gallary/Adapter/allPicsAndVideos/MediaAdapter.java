package com.rajesh.gallary.Adapter.allPicsAndVideos;

import static com.rajesh.gallary.common.Constant.ALL_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.NEW_FILTER_DATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rajesh.gallary.R;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.network.onItemClickListener;
import com.rajesh.gallary.ui.ViewHolder.MediaVIewHolder;
import com.rajesh.gallary.utils.AutoFitRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class MediaAdapter extends RecyclerView.Adapter<MediaVIewHolder> {

    private List<mediaModel> mediaModelList = new ArrayList<>();
    private Context context;
    private onItemClickListener<mediaModel> onItemClickListener;
    private boolean isSelected = false;
    private int ViewType = 0;
    private int GridCount = 4;

    private AutoFitRecyclerView autoFitRecyclerView;

    public void setViewType(int viewType) {
        ViewType = viewType;
        notifyDataSetChanged();
    }

    public void SortMediaByDate(String FilterTYPE) {
        if (FilterTYPE.equals(NEW_FILTER_DATE))
            Collections.sort(mediaModelList, (t1, t2) -> Long.compare(t2.getMediaDate(), t1.getMediaDate()));
        else
            Collections.reverse(mediaModelList);

        notifyDataSetChanged();
    }

    public void setGridCount(int gridCount) {
        GridCount = gridCount;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public MediaAdapter(Context context) {

        this.context = context;
        autoFitRecyclerView = new AutoFitRecyclerView(context);
    }

    public void setOnItemClickListener(onItemClickListener<mediaModel> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setMediaModelList(List<mediaModel> mediaModelList) {
        this.mediaModelList = mediaModelList;
        Collections.sort(this.mediaModelList, (t1, t2) -> Long.compare(t2.getMediaDate(), t1.getMediaDate()));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MediaVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MediaVIewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.media_grid_item, parent, false));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull MediaVIewHolder holder, @SuppressLint("RecyclerView") int position) {
        final mediaModel image = mediaModelList.get(position);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(autoFitRecyclerView.calculateNoOfColumns(GridCount), autoFitRecyclerView.calculateNoOfColumns(GridCount));

        holder.Picture.setLayoutParams(layoutParams);
        Glide.with(context)
                .load(image.getMediaPath())
                .centerCrop()
                .into(holder.Picture);

        if (!image.isImage())
            holder.Video.setVisibility(View.VISIBLE);
        else
            holder.Video.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(view -> {
            if (!isSelected) {
                onItemClickListener.onClickedItem(image);
            } else {
                image.setSelected(true);
                mediaModelList.set(position, image);
                notifyDataSetChanged();
            }
        });

        //Check selection item
        if (image.getSelected()) {
            holder.SelectedImage.setVisibility(View.VISIBLE);
        } else {
            holder.SelectedImage.setVisibility(View.INVISIBLE);
        }

        //set image name text
        if (ViewType == 1) {
            holder.Picture_Name.setVisibility(View.VISIBLE);
            holder.Picture_Name.setText(image.getMediaName());
        } else {
            holder.Picture_Name.setVisibility(View.GONE);

        }
        holder.itemView.setOnLongClickListener(view -> {
            image.setSelected(true);
            mediaModelList.set(position, image);
            notifyDataSetChanged();
            isSelected = true;
            onItemClickListener.onLongTouch(image);
            return false;
        });

    }

    @Override
    public int getItemCount() {
        return mediaModelList.size();
    }


}
