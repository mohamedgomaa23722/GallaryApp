package com.rajesh.gallary.ui.viewModels;

import static com.rajesh.gallary.common.Constant.MAIN_VIEW_MODEL;

import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rajesh.gallary.Repository.MediaRepository;
import com.rajesh.gallary.model.DateAndMedia;
import com.rajesh.gallary.model.DateOfMedia;
import com.rajesh.gallary.model.mediaModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    private final MediaRepository repository;
    private final MutableLiveData<List<DateAndMedia>> allMediaItems = new MutableLiveData<>();

    @ViewModelInject
    public MainViewModel(MediaRepository repository) {
        this.repository = repository;
    }

    public void insertMedia(mediaModel mediaModelData) {
        repository.insertMedia(mediaModelData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(MAIN_VIEW_MODEL, "insertMedia: Media is Added"),
                        error -> Log.e(MAIN_VIEW_MODEL, "insertMedia: ", error));
    }

    public void insertMediaDate(DateOfMedia dateOfMedia) {
        repository.insertMediaDate(dateOfMedia)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(MAIN_VIEW_MODEL, "insertMedia: Date is Added"),
                        error -> Log.e(MAIN_VIEW_MODEL, "insertMedia: ", error));
    }

    public void deleteMedia(mediaModel mediaModelData) {
        repository.deleteMedia(mediaModelData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(MAIN_VIEW_MODEL, "insertMedia: Deleted successfully"),
                        error -> Log.e(MAIN_VIEW_MODEL, "insertMedia: ", error));
    }

    public void getAllMediaData() {
        repository.GetAllMedia()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(allMediaItems::setValue,
                        error -> Log.e(MAIN_VIEW_MODEL, "getAllMediaData: ", error),
                        () -> Log.d(MAIN_VIEW_MODEL, "getAllMediaData: Competed get all data"));
    }

}
