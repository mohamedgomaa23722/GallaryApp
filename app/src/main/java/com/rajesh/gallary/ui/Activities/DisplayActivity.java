package com.rajesh.gallary.ui.Activities;

import static android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
import static com.rajesh.gallary.common.Constant.ALBUM_DATA;
import static com.rajesh.gallary.common.Constant.DATA;
import static com.rajesh.gallary.common.Constant.FROM_VAULT_TO_SETTINGS;
import static com.rajesh.gallary.common.Constant.INCLUDE_VIDEO;
import static com.rajesh.gallary.common.Constant.LOOP_VIDEO;
import static com.rajesh.gallary.common.Constant.TIME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.WindowManager;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.rajesh.gallary.ui.Adapter.SliderAdapter;
import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.ActivityDisplayBinding;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.ui.BottomSheetss.CopyBottomSheet;
import com.rajesh.gallary.ui.BottomSheetss.DeleteBottomSheet;
import com.rajesh.gallary.ui.BottomSheetss.DetailsBottomSheet;
import com.rajesh.gallary.ui.viewModels.MainViewModel;
import com.rajesh.gallary.utils.SavedData;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    SavedData savedData;

    private int CurrentPosition;
    private int TotalSize;
    private Handler AutoSliderHandler;
    private Runnable AutoSliderRunnable;
    private Timer timer;
    private boolean StopSlider = false;
    private List<mediaModel> mediaModels = new ArrayList<>();

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
        boolean IsVault = intent.getBooleanExtra(FROM_VAULT_TO_SETTINGS, false);
        boolean Isfav = intent.getBooleanExtra("FAV", false);
        if (AlbumId == null && !IsVault && !Isfav)
            viewModel.getMediaTableData(0).observe(this, MediaResponse -> {
                sliderAdapter = new SliderAdapter(this, MediaResponse);
                binding.viewPager.setAdapter(sliderAdapter);
                CurrentPosition = getItemPosition(data.getMediaDate(), MediaResponse);
                TotalSize = MediaResponse.size();
                binding.viewPager.setCurrentItem(CurrentPosition, false);
                mediaModels.addAll(MediaResponse);
            });
        else if (IsVault) {
            viewModel.getMediaTableData(1).observe(this, MediaResponse -> {
                sliderAdapter = new SliderAdapter(this, MediaResponse);
                binding.viewPager.setAdapter(sliderAdapter);
                CurrentPosition = getItemPosition(data.getMediaDate(), MediaResponse);
                TotalSize = MediaResponse.size();
                binding.viewPager.setCurrentItem(CurrentPosition, false);
                mediaModels.addAll(MediaResponse);
            });
        } else if (Isfav) {
            viewModel.InitializeFavMedia();
            viewModel.getFavMedia().observe(this, MediaResponse -> {
                sliderAdapter = new SliderAdapter(this, MediaResponse);
                binding.viewPager.setAdapter(sliderAdapter);
                CurrentPosition = getItemPosition(data.getMediaDate(), MediaResponse);
                TotalSize = MediaResponse.size();
                binding.viewPager.setCurrentItem(CurrentPosition, false);
                Log.d("TAG", "SetUpData: " + MediaResponse.get(0).getVault());
                mediaModels.addAll(MediaResponse);
            });
        } else {
            viewModel.InitializeMediaByAlbumData(AlbumId);
            viewModel.getMediaByAlbum().observe(this, MediaResponse -> {
                sliderAdapter = new SliderAdapter(this, MediaResponse);
                binding.viewPager.setAdapter(sliderAdapter);
                CurrentPosition = getItemPosition(data.getMediaDate(), MediaResponse);
                TotalSize = MediaResponse.size();
                binding.viewPager.setCurrentItem(CurrentPosition, false);
                mediaModels.addAll(MediaResponse);
            });
        }

        viewModel.getSelectedItem().observe(this, mediaResponse -> {
            Objects.requireNonNull(getSupportActionBar()).setTitle(mediaResponse.getMediaName());
            CurrentPosition = mediaModels.indexOf(mediaResponse);

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
                if (!StopSlider && AutoSliderHandler !=null) {
                    AutoSliderHandler.removeCallbacks(AutoSliderRunnable);
                    Toast.makeText(this, "Slider stopped", Toast.LENGTH_SHORT).show();
                    timer.cancel();
                    StopSlider = true;
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
        DetailsBottomSheet frag = new DetailsBottomSheet(data);
        frag.show(getSupportFragmentManager(), "Details");
    }

    /**
     * Display Copy Bottom Sheet
     *
     * @param isCopy
     */
    public void DisplayCopyBottomSheet(boolean isCopy) {
        //Initialize Bottom Sheet
        CopyBottomSheet frag = new CopyBottomSheet(data);
        frag.show(getSupportFragmentManager(), "Copy");
    }

    /**
     * Display Delete Bottom Sheet
     */
    public void DisplayDeleteBottomSheet() {
        DeleteBottomSheet frag = new DeleteBottomSheet(data);
        frag.show(getSupportFragmentManager(), "Delete");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.slider_menu, menu);
        if (data.getVault() == 1)
            menu.findItem(R.id.MoveToVault).setTitle("Unhide");
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
                if (data.getVault() == 0) {
                    data.setVault(1);
                } else {
                    data.setVault(0);
                }
                viewModel.UpdateMedia(data);
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
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Auto Slider for View pager Method
     */
    private void AutoSlider() {
        StopSlider = false;
        int Time = savedData.getIntegerValue(TIME);
        AutoSliderHandler = new Handler();
        AutoSliderRunnable = () -> {

            if (CurrentPosition == TotalSize - 1) {
                if (savedData.getBooleanValue(LOOP_VIDEO, true))
                    CurrentPosition = 0;
                else {
                    StopSlider = true;
                    timer.cancel();
                }
            }
            if (!savedData.getBooleanValue(INCLUDE_VIDEO, true))
                if (!mediaModels.get(CurrentPosition).isImage()) {
                    binding.viewPager.setCurrentItem(CurrentPosition + 1, false);
                } else {
                    binding.viewPager.setCurrentItem(CurrentPosition++, true);
                }
            else {
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