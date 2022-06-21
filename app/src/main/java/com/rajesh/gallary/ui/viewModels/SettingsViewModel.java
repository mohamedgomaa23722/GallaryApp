package com.rajesh.gallary.ui.viewModels;

import static com.rajesh.gallary.common.Constant.MAIN_VIEW_MODEL;

import android.util.Log;

import androidx.lifecycle.LiveData;
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
public class SettingsViewModel extends ViewModel {
    private MediaRepository repository;
    private final MutableLiveData<String> SettingsCommunication = new MutableLiveData<>();
    private final MutableLiveData<Boolean> DialogCommunication = new MutableLiveData<>();

    @Inject
    public SettingsViewModel(MediaRepository repository) {
        this.repository = repository;
    }

    public void UpdateMedia(mediaModel mediaModel) {
        repository.UpdateMedia(mediaModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(MAIN_VIEW_MODEL, "insertMedia: Updated successfully"),
                        error -> Log.e(MAIN_VIEW_MODEL, "insertMedia: ", error));
    }

    public LiveData<List<mediaModel>> getVaultMedia() {
        return repository.getMediaTableData(1);
    }

    public void setSettingsData(String Data) {
        SettingsCommunication.setValue(Data);
    }

    public LiveData<String> getSettingsCommunication() {
        return SettingsCommunication;
    }

    public LiveData<Boolean> getDialogCommunicationData() {
        return DialogCommunication;
    }

    public void setDialogCommunicationData(boolean value) {
        DialogCommunication.setValue(value);
    }

    public void RemoveFromVault(List<mediaModel> favList) {
        for (mediaModel mediaModel : favList) {
            mediaModel.setVault(0);
            mediaModel.setSelected(false);
            UpdateMedia(mediaModel);
        }
    }
}
