package com.rajesh.gallary.ui.Fragments.DisplayFragments;

import static com.rajesh.gallary.common.Constant.PAUSE_VIDEO;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.FragmentVideoBinding;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.ui.viewModels.MainViewModel;
import com.rajesh.gallary.utils.VideoPlayerOperator;

import java.io.File;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class videoFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "videoFragment";
    private FragmentVideoBinding binding;
    private mediaModel model;
    @Inject
    VideoPlayerOperator videoPlayerOperator;
    private ImageView playButton, seekBackButton, seekForwardButton, lockButton, fullScreenButton;
    private LinearLayout operatorView;
    private RelativeLayout progressView;
    private MainViewModel viewModel;

    public videoFragment() {
    }


    public static videoFragment newInstance(mediaModel model) {
        Bundle args = new Bundle();
        args.putSerializable("path", model);
        videoFragment f = new videoFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVideoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews(view);
        model = (mediaModel) getArguments().getSerializable("path");

        Glide.with(this)
                .load(model.getMediaPath())
                .into(binding.img);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    private void setUpViews(View view) {
        playButton = view.findViewById(R.id.exo_play);
        seekBackButton = view.findViewById(R.id.exo_rew);
        seekForwardButton = view.findViewById(R.id.exo_forward);
        lockButton = view.findViewById(R.id.exo_unlock);
        fullScreenButton = view.findViewById(R.id.exo_zoom);

        operatorView = view.findViewById(R.id.bottom_icon);
        progressView = view.findViewById(R.id.progress);

        playButton.setOnClickListener(this);
        seekBackButton.setOnClickListener(this);
        seekForwardButton.setOnClickListener(this);

        lockButton.setOnClickListener(this);
        fullScreenButton.setOnClickListener(this);
        binding.playVideo.setOnClickListener(this);

        binding.getRoot().setOnClickListener(this);
        binding.ExoPlayerVIew.setOnClickListener(this);
    }

    private void playVideo() {
        videoPlayerOperator.InitializeVideoPlayer(Uri.fromFile(new File(model.getMediaPath())));
        binding.ExoPlayerVIew.setPlayer(videoPlayerOperator.getPlayer());
        binding.ExoPlayerVIew.setKeepScreenOn(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoPlayerOperator.getPlayer() != null) {
            videoPlayerOperator.stopVideo();
            videoPlayerOperator.releaseVideo();
        }
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onStart: VideoFragment: is resume");
        viewModel.selectItem(model);

        if (videoPlayerOperator.getPlayer() != null) {
            binding.img.setVisibility(View.VISIBLE);
            binding.playVideo.setVisibility(View.VISIBLE);
            binding.ExoPlayerVIew.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (videoPlayerOperator.getPlayer() != null) {
            videoPlayerOperator.stopVideo();
            videoPlayerOperator.releaseVideo();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playVideo:
                binding.img.setVisibility(View.GONE);
                binding.playVideo.setVisibility(View.GONE);
                binding.ExoPlayerVIew.setVisibility(View.VISIBLE);
                playVideo();

                break;
            case R.id.exo_play:
                changeVideoStatue();
                break;
            case R.id.exo_forward:
                videoPlayerOperator.seekForward();
                break;
            case R.id.exo_rew:
                videoPlayerOperator.seekBack();
                break;
            case R.id.exo_zoom:
                LandScape();
                break;
            case R.id.exo_unlock:
                LockScreenOperator();
                viewModel.clickedItem(true);
                break;
            case R.id.videoRoot:
            case R.id.ExoPlayerVIew:
                viewModel.clickedItem(true);
                break;
        }
    }

    private void LockScreenOperator() {
        boolean isLocked = videoPlayerOperator.isLocked();
        if (isLocked) {
            LockOperation(View.VISIBLE);
            lockButton.setImageResource(R.drawable.ic_baseline_lock_open_24);
            videoPlayerOperator.setLocked(false);
        } else {
            LockOperation(View.GONE);
            lockButton.setImageResource(R.drawable.ic_baseline_lock_24);
            videoPlayerOperator.setLocked(true);
        }
    }

    private void LockOperation(int Visibility) {
        progressView.setVisibility(Visibility);
        operatorView.setVisibility(Visibility);
        fullScreenButton.setVisibility(Visibility);
    }

    private void LandScape() {
        // lock the current device orientation
        int currentOrientation = this.getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void changeVideoStatue() {
        if (videoPlayerOperator.VideoStatue() == PAUSE_VIDEO)
            playButton.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
        else
            playButton.setImageResource(R.drawable.ic_outline_pause_circle_outline_24);
    }
}