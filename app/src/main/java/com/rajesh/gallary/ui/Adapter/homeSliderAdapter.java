package com.rajesh.gallary.ui.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.rajesh.gallary.ui.Fragments.MainFragmentsActivity.AlbumFragment;
import com.rajesh.gallary.ui.Fragments.MainFragmentsActivity.AllMediaFragment;
import com.rajesh.gallary.ui.Fragments.MainFragmentsActivity.FavouriteFragment;

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