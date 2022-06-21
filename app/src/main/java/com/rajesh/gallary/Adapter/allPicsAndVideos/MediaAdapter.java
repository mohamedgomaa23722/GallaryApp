package com.rajesh.gallary.Adapter.allPicsAndVideos;

import static com.rajesh.gallary.common.Constant.ADD_FAV_SELECTED;
import static com.rajesh.gallary.common.Constant.ALL_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.COPY_SELECTED;
import static com.rajesh.gallary.common.Constant.DELETE_SELECTED;
import static com.rajesh.gallary.common.Constant.MOVE_TO_VAULT;
import static com.rajesh.gallary.common.Constant.NEW_FILTER_DATE;
import static com.rajesh.gallary.common.Constant.SELECT_ALL;
import static com.rajesh.gallary.common.Constant.SHARE_SELECTED;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rajesh.gallary.R;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.network.onItemClickListener;
import com.rajesh.gallary.network.onLongSelected;
import com.rajesh.gallary.ui.Activities.MainActivity;
import com.rajesh.gallary.ui.ViewHolder.MediaVIewHolder;
import com.rajesh.gallary.utils.AutoFitRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class MediaAdapter extends RecyclerView.Adapter<MediaVIewHolder> {

    private List<mediaModel> mediaModelList = new ArrayList<>();
    private Activity context;
    private onItemClickListener<mediaModel> onItemClickListener;
    private onLongSelected onLongSelected;
    private boolean isEnable = false;
    private boolean isSelected = false;
    private boolean isFAv = false;
    private boolean isVault = false;
    private int ViewType = 0;
    private int GridCount = 4;
    int v;
    private AutoFitRecyclerView autoFitRecyclerView;
    private List<mediaModel> selectedItems = new ArrayList<>();

    public List<mediaModel> getMediaModelList() {
        return mediaModelList;
    }

    public void setFAv(boolean FAv) {
        isFAv = FAv;
    }

    public void setViewType(int viewType) {
        ViewType = viewType;
        notifyDataSetChanged();
    }

    public void setOnLongSelected(com.rajesh.gallary.network.onLongSelected onLongSelected) {
        this.onLongSelected = onLongSelected;
    }

    public void SortMediaByDate(String FilterTYPE) {
        if (FilterTYPE.equals(NEW_FILTER_DATE)) {
            Collections.sort(mediaModelList, (t1, t2) -> Long.compare(t2.getMediaDate(), t1.getMediaDate()));
        } else
            Collections.reverse(mediaModelList);

        notifyDataSetChanged();
    }

    public void setGridCount(int gridCount) {
        GridCount = gridCount;
        notifyDataSetChanged();
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public MediaAdapter(Activity context) {
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

    public void FilterMediaModelList(List<mediaModel> mediaModelList) {
        this.mediaModelList = mediaModelList;
        notifyDataSetChanged();
    }

    public void setVault(boolean vault) {
        isVault = vault;
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
        Log.d("TAG", "onBindViewHolder: type:" + ViewType + ", grid c=" + GridCount);
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
            if (!isEnable) {
                onItemClickListener.onClickedItem(image);
            } else {
                if (!image.getSelected())
                    image.setSelected(true);
                else
                    image.setSelected(false);

                mediaModelList.set(position, image);
                ClickItem(holder, position);
                notifyDataSetChanged();
            }
        });

        holder.selectedBack.setLayoutParams(layoutParams);
        holder.SelectedImage.setLayoutParams(layoutParams);

        //Check selection item
        if (image.getSelected()) {
            holder.SelectedImage.setVisibility(View.VISIBLE);
            holder.selectedBack.setVisibility(View.VISIBLE);
        } else {
            holder.SelectedImage.setVisibility(View.INVISIBLE);
            holder.selectedBack.setVisibility(View.INVISIBLE);
        }

        //set image name text
        if (ViewType == 1) {
            holder.Picture_Name.setVisibility(View.VISIBLE);
            holder.Picture_Name.setText(image.getMediaName());
        } else {
            holder.Picture_Name.setVisibility(View.GONE);

        }
        holder.itemView.setOnLongClickListener(v -> {
            onLongSelected.longClicked(true, true, 55);
            if (!image.getSelected())
                image.setSelected(true);
            else
                image.setSelected(false);
            ClickItem(holder, position);
            notifyDataSetChanged();

            if (!isEnable) {
                ActionMode.Callback callback = new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        MenuInflater menuInflater = mode.getMenuInflater();
                        menuInflater.inflate(R.menu.delet_menu, menu);
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        isEnable = true;
                        ClickItem(holder, position);
                        if (isFAv)
                            menu.findItem(R.id.Selected_fav).setTitle("Remove From favourites");
                        else if (isVault) {
                            menu.findItem(R.id.Selected_fav).setVisible(false);
                            menu.findItem(R.id.Selected_copyAllTo).setVisible(false);
                            menu.findItem(R.id.Selected_moveToVault).setTitle("remove from vault");
                        }
                        return true;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.Selected_selectAll:
                                onLongSelected.longClicked(true, !isSelected, SELECT_ALL);
                                isSelected = !isSelected;
                                return true;
                            case R.id.Selected_deleteMedia:
                                onLongSelected.longClicked(true, true, DELETE_SELECTED);
                                return true;
                            case R.id.Selected_share:
                                onLongSelected.longClicked(true, true, SHARE_SELECTED);
                                break;
                            case R.id.Selected_fav:
                                onLongSelected.longClicked(true, true, ADD_FAV_SELECTED);
                                break;
                            case R.id.Selected_copyAllTo:
                                onLongSelected.longClicked(true, true, COPY_SELECTED);
                                break;
                            case R.id.Selected_moveToVault:
                                onLongSelected.longClicked(true, true, MOVE_TO_VAULT);
                                break;
                        }
                        return true;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        isEnable = false;
                        isSelected = false;
                        selectedItems.clear();
                        onLongSelected.longClicked(false, false, 55);
                        notifyDataSetChanged();
                    }
                };
                context.startActionMode(callback);
            } else {
                ClickItem(holder, position);
            }
            return true;
        });


    }


    private void ClickItem(MediaVIewHolder holder, int po) {
        mediaModel message = mediaModelList.get(po);
        if (selectedItems.contains(message)) {
            selectedItems.add(message);
            holder.SelectedImage.setVisibility(View.GONE);
        } else {
            holder.SelectedImage.setVisibility(View.INVISIBLE);
            selectedItems.remove(message);
        }
    }

    @Override
    public int getItemCount() {
        return mediaModelList.size();
    }

}
