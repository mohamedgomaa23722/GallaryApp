package com.rajesh.gallary.ui.BottomSheetss;

import static com.rajesh.gallary.common.Constant.EXTERNAL_IMAGE;
import static com.rajesh.gallary.common.Constant.EXTERNAL_VIDEO;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rajesh.gallary.R;
import com.rajesh.gallary.model.AlbumsAndMedia;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.ui.viewModels.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class DeleteBottomSheet extends BottomSheetDialogFragment {
    private mediaModel data;
    private List<mediaModel> mediaModelList = new ArrayList<>();
    private List<AlbumsAndMedia> AlbumAndMediaData = new ArrayList<>();
    private MainViewModel viewModel;
    private boolean isAlbum = false;

    public DeleteBottomSheet(mediaModel data) {
        // Required empty public constructor
        this.data = data;
    }

    public DeleteBottomSheet(List<mediaModel> mediaModelList) {
        this.mediaModelList = mediaModelList;
    }

    public DeleteBottomSheet(List<AlbumsAndMedia> AlbumAndMediaData, boolean isAlbum) {
        this.AlbumAndMediaData = AlbumAndMediaData;
        this.isAlbum = isAlbum;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.delete_bottomsheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        //define Views
        Button Delete_Ok = view.findViewById(R.id.delete_ok_Btn);
        Button Delete_Cancel = view.findViewById(R.id.delete_cancle_Btn);
        //Handle on Click Operations
        Delete_Ok.setOnClickListener(v -> {
            if (data != null) {
                DeleteOneItem();
            } else {
                if (!isAlbum)
                    DeleteManyItems();
                else
                    DeleteAllAlbums();
            }
            getDialog().dismiss();
        });

        Delete_Cancel.setOnClickListener(v -> {
            getDialog().dismiss();
        });
    }

    private void DeleteAllAlbums() {
        for (AlbumsAndMedia albums : AlbumAndMediaData) {
            if (albums.albums.isSelected()) {
                viewModel.deleteAlbum(albums.albums.getAlbumID());
                DeleteManyItems(albums.mediaModelList);
            }
        }
    }

    private void DeleteOneItem() {
        if (data.isImage())
            viewModel.DeleteImage(EXTERNAL_IMAGE, data);
        else
            viewModel.DeleteImage(EXTERNAL_VIDEO, data);
        viewModel.deleteMedia(data);
        viewModel.InitializeMediaByAlbumData(data.getAlbumID());
        viewModel.getMediaByAlbum().observe(this, mediaModelList -> {
            if (mediaModelList.size() == 0) {
                viewModel.deleteAlbum(data.getAlbumID());
                getActivity().finish();
            }
        });
    }

    private void DeleteManyItems(List<mediaModel> mediaList) {
        for (mediaModel media : mediaList) {
            if (media.isImage())
                viewModel.DeleteImage(EXTERNAL_IMAGE, media);
            else
                viewModel.DeleteImage(EXTERNAL_VIDEO, media);
            viewModel.deleteMedia(media);
        }
    }

    private void DeleteManyItems() {
        for (mediaModel media : mediaModelList) {
            if (media.isImage())
                viewModel.DeleteImage(EXTERNAL_IMAGE, media);
            else
                viewModel.DeleteImage(EXTERNAL_VIDEO, media);
            viewModel.deleteMedia(media);
            viewModel.InitializeMediaByAlbumData(media.getAlbumID());
            viewModel.getMediaByAlbum().observe(this, mediaModelList -> {
                if (mediaModelList.size() == 0) {
                    viewModel.deleteAlbum(media.getAlbumID());
                    getActivity().finish();
                }
            });
        }
    }
}

