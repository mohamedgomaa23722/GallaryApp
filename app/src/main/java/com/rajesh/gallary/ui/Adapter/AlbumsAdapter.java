package com.rajesh.gallary.ui.Adapter;

import static com.rajesh.gallary.common.Constant.ADD_FAV_SELECTED;
import static com.rajesh.gallary.common.Constant.COPY_SELECTED;
import static com.rajesh.gallary.common.Constant.DELETE_SELECTED;
import static com.rajesh.gallary.common.Constant.MOVE_TO_VAULT;
import static com.rajesh.gallary.common.Constant.NEW_FILTER_DATE;
import static com.rajesh.gallary.common.Constant.SELECT_ALL;
import static com.rajesh.gallary.common.Constant.SHARE_SELECTED;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rajesh.gallary.R;
import com.rajesh.gallary.model.AlbumsAndMedia;
import com.rajesh.gallary.network.onAlbumClicked;
import com.rajesh.gallary.network.onLongSelected;
import com.rajesh.gallary.ui.ViewHolder.AlbumViewHolder;
import com.rajesh.gallary.utils.AutoFitRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumViewHolder> {
    private List<AlbumsAndMedia> albums = new ArrayList<>();
    private Activity context;
    private onAlbumClicked<String> albumClicked;
    private AutoFitRecyclerView autoFitRecyclerView;
    private int ViewType = 0;
    private int GridCount = 3;
    private boolean sorted = false;
    private onLongSelected onLongSelected;
    private boolean isEnable = false;
    private boolean isSelected = false;
    private List<AlbumsAndMedia> selectedItems = new ArrayList<>();

    public void sortByName() {
        Collections.sort(this.albums, (t1, t2) ->
                t1.albums.getAlbumName().compareTo(t2.albums.getAlbumName()));
        notifyDataSetChanged();
    }

    public List<AlbumsAndMedia> getAlbums() {
        return albums;
    }

    public void SortBySize() {
        Collections.sort(this.albums, (t1, t2) -> Integer.compare(t2.mediaModelList.size(), t1.mediaModelList.size()));
        notifyDataSetChanged();
    }

    public void setOnLongSelected(onLongSelected onLongSelected) {
        this.onLongSelected = onLongSelected;
    }

    public AlbumsAdapter( Activity context) {
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

        if (albumsModel.mediaModelList.size() > 0) {
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
                if (!isEnable) {
                    albumClicked.onAlbumClickedListener(albumsModel.albums.getAlbumID());
                } else {
                    if (!albumsModel.albums.isSelected())
                        albumsModel.albums.setSelected(true);
                    else
                        albumsModel.albums.setSelected(false);
                    ClickItem(holder, position);
                    notifyDataSetChanged();
                }
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            onLongSelected.longClicked(true, true, 55);
            if (!albumsModel.albums.isSelected())
                albumsModel.albums.setSelected(true);
            else
                albumsModel.albums.setSelected(false);
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
                        menu.findItem(R.id.Selected_copyAllTo).setVisible(false);
                        menu.findItem(R.id.Selected_moveToVault).setVisible(false);

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


        //Check selection item
        if (albumsModel.albums.isSelected()) {
            holder.SelectedAlbum.setVisibility(View.VISIBLE);
            holder.selectedAlbumBack.setVisibility(View.VISIBLE);
        } else {
            holder.SelectedAlbum.setVisibility(View.INVISIBLE);
            holder.selectedAlbumBack.setVisibility(View.INVISIBLE);
        }

    }

    private void ClickItem(AlbumViewHolder holder, int po) {
        AlbumsAndMedia message = albums.get(po);
        if (selectedItems.contains(message)) {
            selectedItems.add(message);
            holder.SelectedAlbum.setVisibility(View.GONE);
        } else {
            holder.SelectedAlbum.setVisibility(View.INVISIBLE);
            selectedItems.remove(message);
        }
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
