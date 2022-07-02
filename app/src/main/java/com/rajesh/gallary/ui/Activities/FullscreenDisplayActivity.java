package com.rajesh.gallary.ui.Activities;

import static com.rajesh.gallary.common.Constant.DATA;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Toast;

import com.rajesh.gallary.ui.Adapter.SliderAdapter;
import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.ActivityFullscreenDisplayBinding;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.ui.viewModels.MainViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
@AndroidEntryPoint
public class FullscreenDisplayActivity extends AppCompatActivity {
    private mediaModel data;
    private MainViewModel viewModel;
    SliderAdapter sliderAdapter;
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 10000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {


        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            if (Build.VERSION.SDK_INT >= 30) {
                mContentView.getWindowInsetsController().hide(
                        WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            } else {
                // Note that some of these constants are new as of API 16 (Jelly Bean)
                // and API 19 (KitKat). It is safe to use them, as they are inlined
                // at compile-time and do nothing on earlier devices.
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
            mControlsView.setVisibility(View.GONE);
            hide();
        }
    };

    private ActivityFullscreenDisplayBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFullscreenDisplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mVisible = true;
        mControlsView = binding.fullscreenContentControls;
        mContentView = binding.viewPager;



        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        SetUpView();
        SetUpData();
    }
    private void SetUpView() {
        Intent intent = getIntent();
        data = (mediaModel) intent.getSerializableExtra(DATA);

        //Handle Onclick
    }

    private void SetUpData() {
        viewModel.getMediaTableData(1).observe(this, MediaResponse -> {
            sliderAdapter = new SliderAdapter(this, MediaResponse);
            binding.viewPager.setAdapter(sliderAdapter);
            binding.viewPager.setCurrentItem(getItemPosition(data.getMediaDate(), MediaResponse), false);
        });


        viewModel.getSelectedItem().observe(this, mediaResponse -> {
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
            toggle();
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        });
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(2000);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        mVisible = false;
        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {
            mContentView.getWindowInsetsController().show(
                    WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        } else {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        mControlsView.setVisibility(View.VISIBLE);
        mVisible = true;
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
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
}