package com.rajesh.gallary.ui.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rajesh.gallary.Repository.MediaRepository;
import com.rajesh.gallary.model.AlbumsAndMedia;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class AlbumViewModel extends ViewModel {
    private static final String TAG = "AlbumViewModel";
    private MediaRepository repository;

    @Inject
    public AlbumViewModel(MediaRepository repository) {
        this.repository = repository;
    }


}
