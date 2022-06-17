package com.rajesh.gallary.Adapter;



import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.ui.Fragments.PictureFragment;
import com.rajesh.gallary.ui.Fragments.videoFragment;

import java.util.List;



public class SliderAdapter extends FragmentStateAdapter {
    private List<mediaModel> mediaModelList;

    public SliderAdapter(@NonNull FragmentActivity fragmentActivity, List<mediaModel> mediaModelList) {
        super(fragmentActivity);
        this.mediaModelList = mediaModelList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        mediaModel mediaModel = mediaModelList.get(position);
        if (mediaModel.isImage())
            return PictureFragment.newInstance(mediaModel);
        else
            return videoFragment.newInstance(mediaModel);


    }

    @Override
    public int getItemCount() {
        return mediaModelList.size();
    }

}
