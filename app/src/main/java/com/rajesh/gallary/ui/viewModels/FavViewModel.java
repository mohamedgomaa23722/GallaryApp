package com.rajesh.gallary.ui.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rajesh.gallary.Repository.MediaRepository;
import com.rajesh.gallary.model.mediaModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class FavViewModel extends ViewModel {
    private static final String TAG = "FavViewModel";
    private MediaRepository repository;
    private final MutableLiveData<List<mediaModel>> favMedia = new MutableLiveData<>();

    @Inject
    public FavViewModel(MediaRepository repository) {
        this.repository = repository;
    }

    public void InitializeFavMedia() {
        repository.getFavMedia().
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favMedia::setValue,
                        error -> Log.e(TAG, "InitializeFavMedia: ", error),
                        () -> Log.d(TAG, "InitializeFavMedia: "));
    }

    public MutableLiveData<List<mediaModel>> getFavMedia() {
        return favMedia;
    }
}
