package com.rajesh.gallary.Adapter.allPicsAndVideos;

import static com.rajesh.gallary.common.Constant.ADD_FAV_SELECTED;
import static com.rajesh.gallary.common.Constant.COPY_SELECTED;
import static com.rajesh.gallary.common.Constant.DELETE_SELECTED;
import static com.rajesh.gallary.common.Constant.MOVE_TO_VAULT;
import static com.rajesh.gallary.common.Constant.NEW_FILTER_DATE;
import static com.rajesh.gallary.common.Constant.SELECT_ALL;
import static com.rajesh.gallary.common.Constant.SHARE_SELECTED;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rajesh.gallary.R;
import com.rajesh.gallary.model.DateAndMedia;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.network.onItemClickListener;
import com.rajesh.gallary.network.onLongSelected;
import com.rajesh.gallary.network.popupCommunicator;
import com.rajesh.gallary.ui.ViewHolder.ParentMediaViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class DateAndMediaAdapter extends RecyclerView.Adapter<ParentMediaViewHolder> implements onLongSelected {
    private static final String TAG = "DateAndMediaAdapter";
    private List<DateAndMedia> mediaAndDateList = new ArrayList<>();
    private Context context;
    RecyclerView.RecycledViewPool viewPool;
    Activity activity;
    private onItemClickListener<mediaModel> onItemClickListener;
    popupCommunicator popupCommunicator;
    //Filter Data
    private int ViewType = 0;
    private int GridCount = 4;

    onLongSelected onLongSelected;
    private boolean isEnable = false;
    private List<mediaModel> selectedItems = new ArrayList<>();

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setViewType(int viewType) {
        ViewType = viewType;
        notifyDataSetChanged();
    }

    public void setOnLongSelected(com.rajesh.gallary.network.onLongSelected onLongSelected) {
        this.onLongSelected = onLongSelected;
    }

    public void setPopupCommunicator(com.rajesh.gallary.network.popupCommunicator popupCommunicator) {
        this.popupCommunicator = popupCommunicator;
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
        MediaAdapter mediaAdapter = new MediaAdapter(activity);
        mediaAdapter.setMediaModelList(dateAndMedia.mediaModelList);
        mediaAdapter.setGridCount(GridCount);
        mediaAdapter.setOnItemClickListener(onItemClickListener);
        mediaAdapter.setHasStableIds(true);
        mediaAdapter.setViewType(ViewType);
        mediaAdapter.setOnLongSelected(onLongSelected);
        viewPool.setMaxRecycledViews(R.layout.media_grid_item, dateAndMedia.mediaModelList.size());
        holder.ChildRecycler.setRecycledViewPool(viewPool);
        holder.ChildRecycler.setItemViewCacheSize(dateAndMedia.mediaModelList.size());
//        holder.ChildRecycler.getRecycledViewPool().setMaxRecycledViews(R.layout.media_item,dateAndMedia.mediaModelList.size());
        holder.ChildRecycler.setAdapter(mediaAdapter);
        holder.ChildRecycler.setHasFixedSize(true);
        mediaAdapter.setOnLongSelected(this);
        mediaAdapter.setEnable(isEnable);
        //Check the item data
        holder.Date.setText(dateAndMedia.date.getDate());
    }

    @Override
    public int getItemCount() {
        return mediaAndDateList.size();
    }

    private void SelectAllItems(boolean isselectedAll) {
        for (int i = 0; i < mediaAndDateList.size(); i++) {
            for (int j = 0; j < mediaAndDateList.get(i).mediaModelList.size(); j++) {
                mediaAndDateList.get(i).mediaModelList.get(j).setSelected(isselectedAll);
            }
        }
        notifyDataSetChanged();
    }

    private void SelectItem() {

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void longClicked(boolean isEnable, boolean isSelected, int Operation) {
        this.isEnable = isEnable;
        if (Operation == SELECT_ALL) {
            SelectAllItems(isSelected);
        } else if (Operation == DELETE_SELECTED) {
            popupCommunicator.SelectedItems(DELETE_SELECTED, GetAllSelectedItems());
        } else if (Operation == SHARE_SELECTED) {
            popupCommunicator.SelectedItems(SHARE_SELECTED, GetAllSelectedItems());
        } else if (Operation == COPY_SELECTED) {
            popupCommunicator.SelectedItems(COPY_SELECTED, GetAllSelectedItems());
        } else if (Operation == ADD_FAV_SELECTED) {
            popupCommunicator.SelectedItems(ADD_FAV_SELECTED, GetAllSelectedItems());
        } else if (Operation == MOVE_TO_VAULT) {
            popupCommunicator.SelectedItems(MOVE_TO_VAULT, GetAllSelectedItems());
        } else {
            // remove selection from all items
            SelectAllItems(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public void OnChildSelected(List<mediaModel> data) {

    }

    private List<mediaModel> GetAllSelectedItems() {
        List<mediaModel> result = new ArrayList<>();
        for (DateAndMedia dateAndMedia : mediaAndDateList) {
            for (mediaModel data : dateAndMedia.mediaModelList) {
                if (data.getSelected())
                    result.add(data);
            }
        }
        return result;
    }

}

