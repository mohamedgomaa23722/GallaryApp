package com.rajesh.gallary;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.ui.Fragments.AlbumFragment;
import com.rajesh.gallary.ui.Fragments.AllMediaFragment;
import com.rajesh.gallary.ui.Fragments.FavouriteFragment;
import com.rajesh.gallary.ui.Fragments.PictureFragment;
import com.rajesh.gallary.ui.Fragments.videoFragment;

import java.util.List;

public class homeSliderAdapter extends FragmentStateAdapter {

    public homeSliderAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AllMediaFragment();
            case 1:
                return new AlbumFragment();
            default:
                return new FavouriteFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}