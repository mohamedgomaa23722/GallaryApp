package com.rajesh.gallary.utils;

import static com.rajesh.gallary.common.Constant.PAUSE_VIDEO;
import static com.rajesh.gallary.common.Constant.PLAY_VIDEO;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SeekParameters;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class VideoPlayerOperator {
    ExoPlayer player;
    private Context context;
    private int CurrentState = 1;
    private boolean isLocked = false;

    @Inject
    public VideoPlayerOperator(@ApplicationContext Context context) {
        this.context = context;
    }

    /**
     * At here we have some operations
     * 1- play video
     * 2- pause video
     * 3- seek to some position
     * 4- forward to some position
     */
    public ExoPlayer InitializeVideoPlayer(Uri videoUri) {
        this.player = new ExoPlayer.Builder(context).build();
        this.player.setSeekParameters(new SeekParameters(10000, 10000));
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        this.player.setMediaItem(mediaItem);
        return this.player;
    }

    public void playVideo() {
        player.play();
    }

    public void PauseVideo() {
        player.pause();
    }

    public void seekBack() {
        player.seekBack();
    }

    public void seekForward() {
        player.seekForward();
    }

    public void fullScreen() {
    }

    public int VideoStatue() {
        if (CurrentState == PLAY_VIDEO) {
            PauseVideo();
            CurrentState = PAUSE_VIDEO;
        } else {
            playVideo();
            CurrentState = PLAY_VIDEO;
        }
        return CurrentState;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}
