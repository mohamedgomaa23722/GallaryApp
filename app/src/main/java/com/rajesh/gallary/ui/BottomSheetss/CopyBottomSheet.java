package com.rajesh.gallary.ui.BottomSheetss;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import static com.rajesh.gallary.common.Constant.EXTERNAL_IMAGE;
import static com.rajesh.gallary.common.Constant.EXTERNAL_VIDEO;
import static com.rajesh.gallary.common.Constant.IMAGE_PROJECTION;
import static com.rajesh.gallary.common.Constant.VIDEO_PROJECTION;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rajesh.gallary.ui.Adapter.folderAdapter;
import com.rajesh.gallary.R;
import com.rajesh.gallary.model.AlbumsAndMedia;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.ui.viewModels.MainViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CopyBottomSheet extends BottomSheetDialogFragment {
    private MainViewModel viewModel;
    @Inject
    folderAdapter adapter;

    mediaModel data;
    List<mediaModel> list;

    public CopyBottomSheet(mediaModel data) {
        this.data = data;
    }

    public CopyBottomSheet(List<mediaModel> list) {
        this.list = list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.folders_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        //Set Parameter
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        // Define Views
        ConstraintLayout layout = view.findViewById(R.id.Folder_root);
        Button CreateNewAlbum_Btn = view.findViewById(R.id.CreateNewAlbum);
        RecyclerView recyclerView = view.findViewById(R.id.FolderRecyclerView);
        // Set Param to the parent
        FrameLayout.LayoutParams br = new FrameLayout.LayoutParams(MATCH_PARENT, (int) (displayMetrics.heightPixels * 3 / 4));
        layout.setLayoutParams(br);
        //Setup recycler view to Show all albums
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        // Observe Data on recycler view
        viewModel.initializeAlbumsData();
        viewModel.getAlbums().observe(this, albumsAndMedia -> {
            adapter.setAlbumsAndMedia(albumsAndMedia);
        });
        //Onclick listener Actions
        adapter.setOnAlbumClicked(folder -> {
            try {
                String FolderPath = folder.mediaModelList.get(0).getMediaPath().substring(0, folder.mediaModelList.get(0).getMediaPath().lastIndexOf("/"));
                Uri folderUri = Uri.parse(FolderPath);

                if (data !=null){
                    CopySingleItem( new File(folderUri.getPath() + "/"),folder);
                    Toast.makeText(getContext(), "Single Item", Toast.LENGTH_SHORT).show();
                }else{
                    CopyManyItems( new File(folderUri.getPath() + "/"),folder);
                    Toast.makeText(getContext(), "many Item", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            getDialog().dismiss();
        });

        //Create New folder Handling
        CreateNewAlbum_Btn.setOnClickListener(v -> {
            // open bottom sheet
            //Initialize Bottom Sheet
            FolderBottomSheet frag = null;
            if (data != null)
                frag = new FolderBottomSheet(data);
            else
                frag = new FolderBottomSheet(list);
            frag.show(getActivity().getSupportFragmentManager(), "Delete");
        });

    }

    private void copyOrMoveFile(mediaModel media, AlbumsAndMedia albumsAndMedia, File ImageFile, File CopyTO) throws IOException {
        File newFile = new File(CopyTO, ImageFile.getName());
        try (FileChannel outputChannel = new FileOutputStream(newFile).getChannel(); FileChannel inputChannel = new FileInputStream(ImageFile).getChannel()) {
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            //at here we only move not deleting media
            // inset new media
            // Create new Object of our model
            final mediaModel mediaModel = new mediaModel();
            //set values of selected media
            String FolderPath = albumsAndMedia.mediaModelList.get(0).getMediaPath().substring(0, albumsAndMedia.mediaModelList.get(0).getMediaPath().lastIndexOf("/"));
            String ImagePath = media.getMediaPath().substring(media.getMediaPath().lastIndexOf("/") + 1);

            mediaModel.setAlbumID(albumsAndMedia.albums.getAlbumID());
            mediaModel.setMediaPath(FolderPath + "/" + ImagePath);

            mediaModel.setMediaDate(media.getMediaDate());
            mediaModel.setMediaDateId(media.getMediaDateId());
            mediaModel.setMediaSize(media.getMediaSize());
            mediaModel.setFav(media.isFav());
            mediaModel.setImage(media.isImage());
            mediaModel.setMediaName(media.getMediaName());
            mediaModel.setSelected(media.getSelected());
            mediaModel.setMediaID(media.getMediaID());
            mediaModel.setVault(media.getVault());
            //Insert data
            if (media.isImage())
                viewModel.insertMedia(EXTERNAL_IMAGE, mediaModel, IMAGE_PROJECTION);
            else
                viewModel.insertMedia(EXTERNAL_VIDEO, mediaModel, VIDEO_PROJECTION);
        }
        Log.d("TAG", "copyOrMoveFile: insert data");
    }


    private void CopySingleItem(File CopyTO,AlbumsAndMedia albumsAndMedia) throws IOException {
        copyOrMoveFile(data,albumsAndMedia,new File(data.getMediaPath() + "/"), CopyTO);
        viewModel.RefreshData();

    }

    private void CopyManyItems(File CopyTo,AlbumsAndMedia albumsAndMedia) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = () -> {
            viewModel.RefreshData();

        };
        executorService.execute(() -> {
            for (mediaModel mediaModel : list) {
                try {
                    copyOrMoveFile(mediaModel,albumsAndMedia,new File(mediaModel.getMediaPath() + "/"), CopyTo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            handler.post(runnable);
        });
    }
}

