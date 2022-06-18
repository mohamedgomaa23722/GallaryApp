package com.rajesh.gallary.ui.Activities;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
import static com.rajesh.gallary.common.Constant.ALBUM_DATA;
import static com.rajesh.gallary.common.Constant.DATA;
import static com.rajesh.gallary.common.Constant.EXTERNAL_IMAGE;
import static com.rajesh.gallary.common.Constant.EXTERNAL_VIDEO;
import static com.rajesh.gallary.common.Constant.IMAGE_PROJECTION;
import static com.rajesh.gallary.common.Constant.INCLUDE_VIDEO;
import static com.rajesh.gallary.common.Constant.LOOP_VIDEO;
import static com.rajesh.gallary.common.Constant.SHARED_P_NAME;
import static com.rajesh.gallary.common.Constant.THEME;
import static com.rajesh.gallary.common.Constant.TIME;
import static com.rajesh.gallary.common.Constant.VIDEO_PROJECTION;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.rajesh.gallary.Adapter.SliderAdapter;
import com.rajesh.gallary.Adapter.folderAdapter;
import com.rajesh.gallary.BuildConfig;
import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.ActivityDisplayBinding;
import com.rajesh.gallary.model.AlbumsAndMedia;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.network.onAlbumClicked;
import com.rajesh.gallary.ui.viewModels.MainViewModel;
import com.rajesh.gallary.utils.SavedData;


import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DisplayActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityDisplayBinding binding;
    private mediaModel data;
    private MainViewModel viewModel;
    SliderAdapter sliderAdapter;
    private boolean isClicked = true;
    @Inject
    folderAdapter adapter;
    @Inject
    SavedData savedData;

    private int CurrentPosition;
    private int TotalSize;
    private Handler AutoSliderHandler;
    private Runnable AutoSliderRunnable;
    Timer timer;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        binding = ActivityDisplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        SetUpView();
        SetUpData();

    }

    private void SetUpView() {

        //Handle Onclick
        binding.AddTofav.setOnClickListener(this);
        binding.edit.setOnClickListener(this);
        binding.share.setOnClickListener(this);
        binding.delete.setOnClickListener(this);

    }

    private void SetUpData() {
        Intent intent = getIntent();
        data = (mediaModel) intent.getSerializableExtra(DATA);
        String AlbumId = intent.getStringExtra(ALBUM_DATA);
        Log.d("TAG", "SetUpData: " + AlbumId + ",date:" + data.getMediaDate());
        if (AlbumId == null)
            viewModel.getMediaTableData(0).observe(this, MediaResponse -> {
                sliderAdapter = new SliderAdapter(this, MediaResponse);
                binding.viewPager.setAdapter(sliderAdapter);
                CurrentPosition = getItemPosition(data.getMediaDate(), MediaResponse);
                TotalSize = MediaResponse.size();
                binding.viewPager.setCurrentItem(CurrentPosition, false);
                Log.d("TAG", "SetUpData: " + MediaResponse.get(0).getVault());
            });
        else {
            viewModel.InitializeMediaByAlbumData(AlbumId);
            viewModel.getMediaByAlbum().observe(this, MediaResponse -> {
                sliderAdapter = new SliderAdapter(this, MediaResponse);
                binding.viewPager.setAdapter(sliderAdapter);
                CurrentPosition = getItemPosition(data.getMediaDate(), MediaResponse);
                TotalSize = MediaResponse.size();
                binding.viewPager.setCurrentItem(CurrentPosition, false);
            });
        }

        viewModel.getSelectedItem().observe(this, mediaResponse -> {
            Objects.requireNonNull(getSupportActionBar()).setTitle(mediaResponse.getMediaName());
            if (!mediaResponse.isImage()) {
                binding.edit.setVisibility(View.GONE);
            } else {
                binding.edit.setVisibility(View.VISIBLE);
            }
            if (mediaResponse.isFav()) {
                binding.AddTofav.setImageResource(R.drawable.ic_baseline_favorite_24);
            } else {
                binding.AddTofav.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            }
            data = mediaResponse;
        });

        viewModel.getClickedItem().observe(this, aBoolean -> {

            if (isClicked) {
                getWindow().getDecorView().setSystemUiVisibility(View.VISIBLE);
                binding.OperationView.setVisibility(View.VISIBLE);
                if (AutoSliderHandler != null) {
                    AutoSliderHandler.removeCallbacks(AutoSliderRunnable);
                    Toast.makeText(this, "Slider stopped", Toast.LENGTH_SHORT).show();
                    timer.cancel();
                }
            } else {
                getWindow().getDecorView().setSystemUiVisibility(hideSystemBars());
                binding.OperationView.setVisibility(View.GONE);

            }
            isClicked = !isClicked;
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | FLAG_TRANSLUCENT_STATUS
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    }

    /**
     * At this Case we will use binary search to get the position of selected item
     * but the array is reversed so we will work from high -> low
     *
     * @param date
     * @param mediaModelList
     * @return
     */
    public int getItemPosition(long date, List<mediaModel> mediaModelList) {
        int low = mediaModelList.size() - 1;
        int high = 0;
        int mid = 0;
        while (low >= high) {
            mid = high + ((low - high) / 2);
            if (mediaModelList.get(mid).getMediaDate() < date) {
                low = mid - 1;
            } else {
                high = mid + 1;
            }
            if (mediaModelList.get(mid).getMediaDate() == date) {
                return mid;
            }
        }
        return 0;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void ShareMedia(Uri uri, boolean isImage) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        if (!isImage)
            shareIntent.setType("video/*");
        else
            shareIntent.setType("image/*");

        startActivity(Intent.createChooser(shareIntent, null));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.AddTofav:
                //First Change the Fav
                if (data.isFav()) {
                    binding.AddTofav.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                } else {
                    binding.AddTofav.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
                viewModel.AddToFav(data.getMediaPath(), !data.isFav());
                break;
            case R.id.edit:
                EditImage();
                break;
            case R.id.share:
                ShareMedia(Uri.fromFile(new File(data.getMediaPath())), data.isImage());
                break;
            case R.id.delete:
                DisplayDeleteBottomSheet();
                break;
        }
    }

    /**
     * DS photo Editor Lib Method that help me
     * to Initialize and change the main view
     * of that lib.
     */
    private void EditImage() {
        Intent intent = new Intent(getApplicationContext(), DsPhotoEditorActivity.class);
        Toast.makeText(this, "" + data.getMediaPath(), Toast.LENGTH_SHORT).show();
        intent.setData(Uri.fromFile(new File(data.getMediaPath())));
        //set out direc
        intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY,
                "Images");
        //set toolbar color
        intent.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR,
                Color.parseColor("#CB4C4C4C"));
        //set icons
        //filter
        intent.putExtra(DsPhotoEditorConstants.DS_TOOL_FILTER_DRAWABLE,
                R.drawable.ic_filters);
        //frame
        intent.putExtra(DsPhotoEditorConstants.DS_TOOL_FRAME_DRAWABLE,
                R.drawable.ic_frames);
        //round
        intent.putExtra(DsPhotoEditorConstants.DS_TOOL_ROUND_DRAWABLE,
                R.drawable.ic_baseline_rounded_corner_24);
        //exposure
        intent.putExtra(DsPhotoEditorConstants.DS_TOOL_EXPOSURE_DRAWABLE,
                R.drawable.ic_baseline_exposure_24);
        //contrast
        intent.putExtra(DsPhotoEditorConstants.DS_TOOL_CONTRAST_DRAWABLE,
                R.drawable.ic_baseline_contrast_24);
        //vignette
        intent.putExtra(DsPhotoEditorConstants.DS_TOOL_VIGNETTE_DRAWABLE,
                R.drawable.ic_baseline_vignette_24);
        //crop
        intent.putExtra(DsPhotoEditorConstants.DS_TOOL_CROP_DRAWABLE,
                R.drawable.ic_baseline_crop_24);
        //orientation Drawables
        intent.putExtra(DsPhotoEditorConstants.DS_TOOL_ORIENTATION_DRAWABLE,
                R.drawable.ic_orientation);
        //Saturation
        intent.putExtra(DsPhotoEditorConstants.DS_TOOL_SATURATION_DRAWABLE,
                R.drawable.ic_saturation);
        //sharpness
        intent.putExtra(DsPhotoEditorConstants.DS_TOOL_SHARPNESS_DRAWABLE,
                R.drawable.ic_star_svgrepo_com);
        //draw
        intent.putExtra(DsPhotoEditorConstants.DS_TOOL_DRAW_DRAWABLE,
                R.drawable.ic_draw_svgrepo_com);
        //sticker
        intent.putExtra(DsPhotoEditorConstants.DS_TOOL_STICKER_DRAWABLE,
                R.drawable.ic_sticker);
        //text
        intent.putExtra(DsPhotoEditorConstants.DS_TOOL_TEXT_DRAWABLE,
                R.drawable.ic_add_text_svgrepo_com);

        //Apply icon
        intent.putExtra(DsPhotoEditorConstants.DS_TOP_BUTTON_APPLY_DRAWABLE,
                R.drawable.ic_baseline_check_24);
        //Cancle icon
        intent.putExtra(DsPhotoEditorConstants.DS_TOP_BUTTON_CANCEL_DRAWABLE,
                R.drawable.ic_baseline_arrow_back_24);

        //set background color
        intent.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR,
                Color.parseColor("#CB4C4C4C"));
        //Hide tools
        intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,
                new int[]{DsPhotoEditorActivity.TOOL_WARMTH,
                        DsPhotoEditorActivity.TOOL_PIXELATE
                });
        startActivityForResult(intent, 101);
    }

    /**
     * Display Details bottom sheet method
     */
    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    private void DisplayDetailsSheet() {
        //Initialize bottom sheet
        final BottomSheetDialog detailsBottomSheet = new BottomSheetDialog(this);
        detailsBottomSheet.setContentView(R.layout.details_layout);
        //Define Views
        TextView Name_txt = detailsBottomSheet.findViewById(R.id.Media_Name_txt);
        TextView Path_txt = detailsBottomSheet.findViewById(R.id.Media_Path_txt);
        TextView Size_txt = detailsBottomSheet.findViewById(R.id.Media_Size_txt);
        TextView Date_txt = detailsBottomSheet.findViewById(R.id.Media_Date_txt);
        //observe data into the view
        Name_txt.setText("Name : " + data.getMediaName());
        Path_txt.setText("Path : " + data.getMediaPath());
        Size_txt.setText("Size : " + sizeConverter(data.getMediaSize()));
        Date_txt.setText("Created On : " + new SimpleDateFormat("dd MMM ,yyyy").format(data.getMediaDate() * 1000));
        //show BottomSheet
        detailsBottomSheet.show();
    }

    /**
     * Display Copy Bottom Sheet
     *
     * @param isCopy
     */
    public void DisplayCopyBottomSheet(boolean isCopy) {
        //Initialize Bottom Sheet
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.folders_bottom_sheet);
        //Set Parameter
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        // Define Views
        ConstraintLayout layout = bottomSheetDialog.findViewById(R.id.Folder_root);
        Button CreateNewAlbum_Btn = bottomSheetDialog.findViewById(R.id.CreateNewAlbum);
        RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.FolderRecyclerView);
        // Set Param to the parent
        FrameLayout.LayoutParams br = new FrameLayout.LayoutParams(MATCH_PARENT, (int) (displayMetrics.heightPixels * 3 / 4));
        layout.setLayoutParams(br);
        //Setup recycler view to Show all albums
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
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
                String ImagePath = data.getMediaPath().substring(data.getMediaPath().lastIndexOf("/") + 1);

                Uri folderUri = Uri.parse(FolderPath);
                Uri ImageUri = Uri.parse(data.getMediaPath());

                data.setAlbumID(folder.albums.getAlbumID());
                data.setMediaPath(FolderPath + "/" + ImagePath);
                Log.d("TAG", "DisplayBottomSheet: " + folder.albums.getAlbumID());
                copyOrMoveFile(new File(ImageUri.getPath() + "/"), new File(folderUri.getPath() + "/"), true, "");

            } catch (IOException e) {
                e.printStackTrace();
            }
            bottomSheetDialog.dismiss();
        });

        //Create New folder Handling
        CreateNewAlbum_Btn.setOnClickListener(view -> {
            DisplayCreateFolderBottomSheet();
        });

        bottomSheetDialog.show();
    }

    /**
     * Display the Create Folder Bottom Sheet Method
     */
    public void DisplayCreateFolderBottomSheet() {
        final BottomSheetDialog FolderNameBottomSheet = new BottomSheetDialog(this);
        FolderNameBottomSheet.setContentView(R.layout.folder_name_bottomsheet_layout);
        // Define View Att
        TextInputEditText Folder_Edx = FolderNameBottomSheet.findViewById(R.id.Folder_Name_EDX);
        Button Folder_Ok = FolderNameBottomSheet.findViewById(R.id.folder_ok_Btn);
        Button Folder_Cancle = FolderNameBottomSheet.findViewById(R.id.folder_cancle_Btn);
        //Handle on Click Operations
        Folder_Ok.setOnClickListener(v -> {
            //when User click ok as create the new file and copy the value to it
            String FolderName = Folder_Edx.getText().toString();
            //Create folder
            if (FolderName.length() > 0) {
                try {
                    createNewFile(FolderName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
                //Set error
                Toast.makeText(this, "Please Enter the name", Toast.LENGTH_SHORT).show();
            FolderNameBottomSheet.dismiss();
        });

        Folder_Cancle.setOnClickListener(v -> {
            //when user click on cancel to avoid any changes
            FolderNameBottomSheet.dismiss();
        });

        // show the bottom sheet
        FolderNameBottomSheet.show();
    }

    /**
     * Display Delete Bottom Sheet
     */
    public void DisplayDeleteBottomSheet() {
        //Initialize Bottom Sheet
        final BottomSheetDialog deleteBottomSheet = new BottomSheetDialog(this);
        deleteBottomSheet.setContentView(R.layout.delete_bottomsheet);
        //define Views
        Button Delete_Ok = deleteBottomSheet.findViewById(R.id.delete_ok_Btn);
        Button Delete_Cancel = deleteBottomSheet.findViewById(R.id.delete_cancle_Btn);
        //Handle on Click Operations
        Delete_Ok.setOnClickListener(v -> {
            if (data.isImage())
                viewModel.DeleteImage(EXTERNAL_IMAGE, data);
            else
                viewModel.DeleteImage(EXTERNAL_VIDEO, data);
            viewModel.deleteMedia(data);

            viewModel.getMediaByAlbum().observe(this, mediaModelList -> {
                if (mediaModelList.size() == 0) {
                    viewModel.deleteAlbum(data.getAlbumID());
                    finish();
                }
            });
            deleteBottomSheet.dismiss();
        });

        Delete_Cancel.setOnClickListener(v -> {
            deleteBottomSheet.dismiss();
        });

        // show the bottom sheet
        deleteBottomSheet.show();
    }

    private void copyOrMoveFile(File ImageFile, File CopyTO, boolean isCopy, String Name) throws IOException {
        File newFile = new File(CopyTO, ImageFile.getName());
        try (FileChannel outputChannel = new FileOutputStream(newFile).getChannel(); FileChannel inputChannel = new FileInputStream(ImageFile).getChannel()) {
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            if (!isCopy) {
                //delete the media from table and storage
                //Update media from room db table
                data.setMediaPath(CopyTO.getPath() + "/" + data.getMediaName());
                Log.d("TAG", "copyOrMoveFile: " + data.getMediaPath());
                if (data.isImage())
                    viewModel.insertMediaForNewAlbum(EXTERNAL_IMAGE, data, IMAGE_PROJECTION, Name);
                else
                    viewModel.insertMediaForNewAlbum(EXTERNAL_VIDEO, data, VIDEO_PROJECTION, Name);
            } else {
                //at here we only move not deleting media
                // inset new media
                // Create new Object of our model
                final mediaModel mediaModel = new mediaModel();
                //set values of selected media
                mediaModel.setMediaDate(data.getMediaDate());
                mediaModel.setMediaDateId(data.getMediaDateId());
                mediaModel.setMediaSize(data.getMediaSize());
                mediaModel.setMediaPath(data.getMediaPath());
                mediaModel.setFav(data.isFav());
                mediaModel.setImage(data.isImage());
                mediaModel.setMediaName(data.getMediaName());
                mediaModel.setSelected(data.getSelected());
                mediaModel.setAlbumID(data.getAlbumID());
                mediaModel.setMediaID(data.getMediaID());
                mediaModel.setVault(data.getVault());
                //Insert data
                if (data.isImage())
                    viewModel.insertMedia(EXTERNAL_IMAGE, mediaModel, IMAGE_PROJECTION);
                else
                    viewModel.insertMedia(EXTERNAL_VIDEO, mediaModel, VIDEO_PROJECTION);

            }
        }
        viewModel.RefreshData();
    }

    private File createNewFile(String Name) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory(), Name);
        if (!file.exists()) {
            file.mkdir();
            Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
            copyOrMoveFile(new File(data.getMediaPath() + "/"), new File(Uri.fromFile(file).getPath() + "/"), false, Name);
        } else {
            Toast.makeText(this, "Folder Already Exists", Toast.LENGTH_SHORT).show();
        }
        return file;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.slider_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.details:
                DisplayDetailsSheet();
                return true;
            case R.id.copyTo:
                DisplayCopyBottomSheet(true);
                return true;
            case R.id.MoveToVault:
                viewModel.UpdateImage(data, MODE_PRIVATE, data.getMediaID());
//                viewModel.AddMediaToVault(data.getMediaPath(), true);
                Toast.makeText(this, "Successfully Added", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.slideShow:
                AutoSlider();
                return true;
            case R.id.setAsWallPaper:
                SetAsWallpaper(new File(data.getMediaPath()), true);
                return true;
            case R.id.setAsLock:
                SetAsWallpaper(new File(data.getMediaPath()), false);
                return true;
            case R.id.settings:
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Get The Size of the media item Converter
     *
     * @param size
     * @return
     */
    private String sizeConverter(String size) {
        float converterSize = Float.parseFloat(size) / 1000f;
        if (converterSize < 1000)
            return converterSize + " KB";
        else if (converterSize > 1000) {
            converterSize = converterSize / 1000;
            return converterSize + "MB";
        } else {
            return (converterSize / 1000) + "G";
        }
    }

    /**
     * Auto Slider for View pager Method
     */
    private void AutoSlider() {
        int Time = savedData.getIntegerValue(TIME);
        AutoSliderHandler = new Handler();
        AutoSliderRunnable = () -> {

            if (CurrentPosition == TotalSize - 1) {
                if (savedData.getBooleanValue(LOOP_VIDEO))
                    CurrentPosition = 0;
                else
                    timer.cancel();
            }
            if (!savedData.getBooleanValue(INCLUDE_VIDEO))
                if (!data.isImage()) {
                    binding.viewPager.setCurrentItem(CurrentPosition+2, false);
                } else {
                    binding.viewPager.setCurrentItem(CurrentPosition++, true);
                }
            else{
                binding.viewPager.setCurrentItem(CurrentPosition++, true);
            }

        };
        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                AutoSliderHandler.post(AutoSliderRunnable);
            }
        }, 500, Time);
    }

    /**
     * method Handling set wallpaper and Lock Back image
     *
     * @param ImageURi
     * @param isWallPaper
     */
    private void SetAsWallpaper(File ImageURi, boolean isWallPaper) {
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        Runnable runnable = () -> {
            Toast.makeText(this, "Successfully Added as wallpaper", Toast.LENGTH_SHORT).show();
        };

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(() -> {
            Bitmap bitmap = null;
            try {
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(ImageURi));
                } else {
                    ImageDecoder.Source source = ImageDecoder.createSource(getApplicationContext().getContentResolver(), Uri.fromFile(ImageURi));
                    bitmap = ImageDecoder.decodeBitmap(source);
                }
                if (isWallPaper) {
                    wallpaperManager.setBitmap(bitmap);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            handler.post(runnable);
        });
    }
}