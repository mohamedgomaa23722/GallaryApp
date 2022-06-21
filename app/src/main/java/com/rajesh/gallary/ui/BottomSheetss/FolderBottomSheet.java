package com.rajesh.gallary.ui.BottomSheetss;

import static com.rajesh.gallary.common.Constant.EXTERNAL_IMAGE;
import static com.rajesh.gallary.common.Constant.EXTERNAL_VIDEO;
import static com.rajesh.gallary.common.Constant.IMAGE_PROJECTION;
import static com.rajesh.gallary.common.Constant.VIDEO_PROJECTION;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.rajesh.gallary.R;
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

public class FolderBottomSheet extends BottomSheetDialogFragment {

    mediaModel data;
    List<mediaModel> list;
    private MainViewModel viewModel;

    public FolderBottomSheet(mediaModel data) {
        this.data = data;
    }

    public FolderBottomSheet(List<mediaModel> list) {
        this.list = list;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.folder_name_bottomsheet_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        // Define View Att
        TextInputEditText Folder_Edx = view.findViewById(R.id.Folder_Name_EDX);
        Button Folder_Ok = view.findViewById(R.id.folder_ok_Btn);
        Button Folder_Cancle = view.findViewById(R.id.folder_cancle_Btn);
        //Handle on Click Operations
        Folder_Ok.setOnClickListener(v -> {
            //when User click ok as create the new file and copy the value to it
            String FolderName = Folder_Edx.getText().toString();
            //Create folder
            if (FolderName.length() > 0) {
                if (data != null)
                    CopySingleItem(FolderName);
                else
                    CopyManyItems(FolderName);
            } else
                //Set error
                Toast.makeText(getContext(), "Please Enter the name", Toast.LENGTH_SHORT).show();
            getDialog().dismiss();
        });

        Folder_Cancle.setOnClickListener(v -> {
            //when user click on cancel to avoid any changes
            getDialog().dismiss();
        });

    }

    private void copyOrMoveFile(File ImageFile, mediaModel data, File CopyTO, String Name) throws IOException {
        File newFile = new File(CopyTO, ImageFile.getName());
        try (FileChannel outputChannel = new FileOutputStream(newFile).getChannel(); FileChannel inputChannel = new FileInputStream(ImageFile).getChannel()) {
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            //delete the media from table and storage
            //Update media from room db table
            data.setMediaPath(CopyTO.getPath() + "/" + data.getMediaName());
            if (data.isImage())
                viewModel.insertMediaForNewAlbum(EXTERNAL_IMAGE, data, IMAGE_PROJECTION, Name);
            else
                viewModel.insertMediaForNewAlbum(EXTERNAL_VIDEO, data, VIDEO_PROJECTION, Name);
        }
    }

    private File createNewFile(String Name) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory(), Name);
        if (!file.exists()) {
            file.mkdir();
        } else {
        }
        return file;
    }

    private void CopySingleItem(String Name) {
        try {
            File file = createNewFile(Name);
            copyOrMoveFile(new File(data.getMediaPath() + "/"), data, new File(Uri.fromFile(file).getPath() + "/"), Name);
            viewModel.RefreshData();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void CopyManyItems(String Name) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = () -> {
            viewModel.RefreshData();
        };
        executorService.execute(() -> {
            try {
                File file = createNewFile(Name);
                for (mediaModel mediaModel : list) {
                    copyOrMoveFile(new File(mediaModel.getMediaPath() + "/"), mediaModel, new File(Uri.fromFile(file).getPath() + "/"), Name);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            handler.post(runnable);
        });
    }
}

