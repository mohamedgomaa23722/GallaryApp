package com.rajesh.gallary.ui.Fragments;

import static com.rajesh.gallary.common.Constant.PAUSE_VIDEO;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.FragmentVideoBinding;
import com.rajesh.gallary.utils.VideoPlayerOperator;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class videoFragment extends Fragment implements View.OnClickListener {
    private FragmentVideoBinding binding;
    StyledPlayerView playerView;
    TextView video_title;
    String path;
    @Inject
    VideoPlayerOperator videoPlayerOperator;
    private ImageView playButton, seekBackButton, seekForwardButton, backButton, shareButton, moreButton, lockButton, fullScreenButton;
    private LinearLayout ToolbarView, operatorView;
    private RelativeLayout progressView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVideoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = getActivity().getIntent();
        path = intent.getStringExtra("path");
        playerView = view.findViewById(R.id.ExoPlayerVIew);
        setUpViews(view);
        playVideo();
    }

    private void setUpViews(View view) {
        playButton = view.findViewById(R.id.exo_play);
        seekBackButton = view.findViewById(R.id.exo_rew);
        seekForwardButton = view.findViewById(R.id.exo_forward);
        backButton = view.findViewById(R.id.video_back);
        shareButton = view.findViewById(R.id.video_share);
        moreButton = view.findViewById(R.id.video_more);
        video_title = view.findViewById(R.id.video_title);
        lockButton = view.findViewById(R.id.exo_unlock);
        fullScreenButton = view.findViewById(R.id.exo_zoom);

        ToolbarView =  view.findViewById(R.id.toolbar);
        operatorView = view. findViewById(R.id.bottom_icon);
        progressView = view. findViewById(R.id.progress);

        video_title.setText("good Video ");
        playButton.setOnClickListener(this);
        seekBackButton.setOnClickListener(this);
        seekForwardButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        moreButton.setOnClickListener(this);
        lockButton.setOnClickListener(this);
        fullScreenButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }
    private void playVideo() {
        ExoPlayer exoPlayer = videoPlayerOperator.InitializeVideoPlayer(Uri.parse(path));
        playerView.setPlayer(exoPlayer);
        playerView.setKeepScreenOn(true);
        exoPlayer.prepare();
    }

    @Override
    public void onPause() {
        super.onPause();
        changeVideoStatue();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_back:
                getActivity().finish();
                break;
            case R.id.video_share:
                ShareMedia(Uri.parse(path));
                break;
            case R.id.video_more:
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
        ToolbarView.setVisibility(Visibility);
        progressView.setVisibility(Visibility);
        operatorView.setVisibility(Visibility);
        fullScreenButton.setVisibility(Visibility);
    }

    private void LandScape(){
        // lock the current device orientation
        int currentOrientation = this.getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            Log.d("TAG", "LandScape: SCREEN_ORIENTATION_UNSPECIFIED");
        }
        else{
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            Log.d("TAG", "LandScape: SCREEN_ORIENTATION_LANDSCAPE");

        }
    }

    private void ShareMedia(Uri uri){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("video/*");
        startActivity(Intent.createChooser(shareIntent, null));

    }

    private void changeVideoStatue(){
        if (videoPlayerOperator.VideoStatue() == PAUSE_VIDEO)
            playButton.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
        else
            playButton.setImageResource(R.drawable.ic_outline_pause_circle_outline_24);
    }
}